package com.nacho.data.repository

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import com.nacho.domain.repository.UserRepository
import com.nacho.model.User
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val database: DatabaseReference,
    private val storageRef: StorageReference
) :
    UserRepository {

    //    auth = Firebase.getInstance()
//    database = Firebase.database.reference
    companion object {
        const val USERS_COLLECTION = "users"
        const val IMAGES_STORAGE_COLLECTION = "images"
    }

    override fun registerUser(
        user: User,
        password: String,
        idPicture: Bitmap,
        onResult: (userId: String) -> Unit,
        onError: (errorMsg: String) -> Unit,
    ) {
        Log.d("PERON", "registerUser() called")
        val userCollectionRef = firestore.collection(USERS_COLLECTION)

        val imageFileName = "userid_${SimpleDateFormat("yyyyMMdd").format(Date())}.jpeg"
        val imagesRef = storageRef.child(IMAGES_STORAGE_COLLECTION)
        val userImgRef = imagesRef.child(imageFileName)

        var userDocId: String

        val baos = ByteArrayOutputStream()
        idPicture.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        try {
            //1 Create user
            auth.createUserWithEmailAndPassword(user.email.toString(), password)
                .addOnSuccessListener {
                    Log.d("PERON", "Register OnSuccessListener called")
                    //2 Add user to users collection
                    userCollectionRef.add(user)
                        .addOnSuccessListener { snapshot ->
                            userDocId = snapshot.id
                            Log.d(
                                "PERON",
                                "createUser OnSuccessListener called with userId: $userDocId"
                            )
                            //3 Upload picture to Storage
                            val uploadTask = userImgRef.putBytes(data)
                                .addOnFailureListener {
                                    onError(it.message ?: it.toString())
                                }

                            uploadTask.continueWithTask { urlTask ->
                                if (!urlTask.isSuccessful) {
                                    urlTask.exception?.let {
                                        onError(it.localizedMessage ?: it.toString())
                                    }
                                }
                                userImgRef.downloadUrl
                            }.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val downloadUrl = task.result
                                    //4 Update value of imageUrl in users collection
                                    userCollectionRef.document(userDocId)
                                        .update("idImageUrl", downloadUrl)
                                        .addOnSuccessListener {
                                            onResult(userDocId)
                                        }
                                        .addOnFailureListener {
                                            onError(it.localizedMessage ?: it.toString())
                                        }
                                } else {
                                    task.exception?.let {
                                        onError(it.localizedMessage ?: "Something went wrong")
                                    }
                                }
                            }
                        }
                        .addOnFailureListener {
                            onError(it.message ?: it.toString())
                        }
                }
                .addOnFailureListener {
                    onError(it.message ?: it.toString())
                }
        } catch (e: Exception){
            onError(e.localizedMessage ?: e.toString())
        }

    }

    override fun loginUser(
        email: String,
        password: String,
        onResult: (userId: String) -> Unit,
        onError: (errorMsg: String) -> Unit,
    ) {
        Log.d("PERON", "loginUser() called")
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                Log.d("PERON", "Login OnSuccessListener called")
                Log.d("PERON", "User: ${authResult.user}")
                val currentUserId = auth.currentUser?.uid.toString()
                onResult(currentUserId)
                Log.d("PERON", "Fetched id from login: $currentUserId")
            }
            .addOnFailureListener {
                Log.d("PERON", "Login OnFailureListener called")
                onError(it.message ?: it.toString())
            }
    }

    override fun fetchUserData(
        userId: String,
        onResult: (user: User) -> Unit,
        onError: (errorMsg: String) -> Unit
    ) {
//        database.child(USERS_COLLECTION).child(userId).get()
//            .addOnSuccessListener {
//                Log.d("PERON", "fetchUserData() OnSuccessListener called with userId: $userId")
//                Log.d("PERON", "Snapshot data: $it")
//                Log.i("PERON", "Got value ${it.value}")
//                val user = it.getValue(User::class.java)
//                Log.d("PERON", "User data: $user")
//                if (user != null)
//                    onResult(user)
//                else
//                    onError("User data was null")
//            }.addOnFailureListener {
//                onError(it.message ?: it.toString())
//            }
        firestore.collection(USERS_COLLECTION).document(userId).get()
            .addOnSuccessListener { snapshot ->
                Log.d("PERON", "fetchUserData() OnSuccessListener called with userId: $userId")
                Log.d("PERON", "Snapshot data: $snapshot")
                Log.d("PERON", "Document exists: ${snapshot.exists()}")
                val user =
                    snapshot.toObject<User>()
                Log.d("PERON", "User data: $user")
                if (user != null)
                    onResult(user)
                else
                    onError("User data was null")
            }
            .addOnFailureListener {
                Log.d("PERON", "fetchUserData() OnFailureListener called")
                onError(it.message ?: it.toString())
            }
    }
}