package com.nacho.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nacho.model.User
import javax.inject.Inject

private const val usersCollection = "users"

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) :
    UserRepository {

//    auth = Firebase.getInstance()
//    database = Firebase.database.reference

    override fun registerUser(
        user: User,
        password: String,
        onResult: (user: User) -> Unit,
        onError: (errorMsg: String) -> Unit,
    ) {
        Log.d("PERON", "registerUser() called")
        auth.createUserWithEmailAndPassword(user.email.toString(), password)
            .addOnSuccessListener {
                Log.d("PERON", "Register OnSuccessListener called")
                val currentUserId = auth.currentUser?.uid.toString()
                firestore.collection(usersCollection).document(currentUserId).get()
                    .addOnSuccessListener { snapshot ->
                        Log.d("PERON", "fetchUserData() OnSuccessListener called")
                        val fetchedUser = snapshot.toObject(User::class.java)
                        Log.d("PERON", "$fetchedUser")
                        fetchedUser?.let {
                            onResult(it)
                        }
                    }
                    .addOnFailureListener {
                        Log.d("PERON", "fetchUserData() OnFailureListener called")
                        onError(it.message ?: it.toString())
                    }
            }
            .addOnFailureListener {
                onError(it.message ?: it.toString())
            }

    }

    override fun loginUser(
        email: String,
        password: String,
        onResult: (user: User) -> Unit,
        onError: (errorMsg: String) -> Unit,
    ) {
        Log.d("PERON", "loginUser() called")
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                Log.d("PERON", "Login OnSuccessListener called")
                Log.d("PERON", "User: ${authResult.user}")
                val currentUserId = auth.currentUser?.uid.toString()
                firestore.collection(usersCollection).document(currentUserId).get()
                    .addOnSuccessListener { snapshot ->
                        Log.d("PERON", "fetchUserData() OnSuccessListener called")
                        val user = snapshot.toObject(User::class.java)
                        Log.d("PERON", "$user")
                        user?.let {
                            onResult(it)
                        }
                    }
                    .addOnFailureListener {
                        Log.d("PERON", "fetchUserData() OnFailureListener called")
                        onError(it.message ?: it.toString())
                    }

                Log.d("PERON", "Fetched id from login: $currentUserId")
            }
            .addOnFailureListener {
                Log.d("PERON", "Login OnFailureListener called")
                onError(it.message ?: it.toString())
            }
    }
}