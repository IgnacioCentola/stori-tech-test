package com.nacho.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.nacho.domain.repository.UserRepository
import com.nacho.model.User
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val database: DatabaseReference
) :
    UserRepository {

    //    auth = Firebase.getInstance()
//    database = Firebase.database.reference
    companion object {
        const val USERS_COLLECTION = "users"
    }

    override fun registerUser(
        user: User,
        password: String,
        onResult: (userId: String) -> Unit,
        onError: (errorMsg: String) -> Unit,
    ) {
        Log.d("PERON", "registerUser() called")
        auth.createUserWithEmailAndPassword(user.email.toString(), password)
            .addOnSuccessListener {
                Log.d("PERON", "Register OnSuccessListener called")
//                val currentUserId = auth.currentUser?.uid.toString()
                firestore.collection(USERS_COLLECTION).add(user)
                    .addOnSuccessListener { snapshot ->
                        val docId = snapshot.id
                        Log.d(
                            "PERON",
                            "createUser OnSuccessListener called with userId: $docId"
                        )
                        onResult(docId)
                    }
                    .addOnFailureListener {
                        Log.d("PERON", "fetchUserData() OnFailureListener called")
                        onError(it.message ?: it.toString())
                    }
//                database.child(USERS_COLLECTION).child(currentUserId).setValue(user)
//                    .addOnSuccessListener {
//                        onResult(currentUserId)
//                    }
//                    .addOnFailureListener {
//                        onError(it.message ?: it.toString())
//                    }
            }
            .addOnFailureListener {
                onError(it.message ?: it.toString())
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