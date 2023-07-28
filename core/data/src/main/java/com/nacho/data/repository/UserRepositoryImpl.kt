package com.nacho.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.nacho.model.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dbReference: DatabaseReference
) :
    UserRepository {

//    auth = Firebase.getInstance()
//    database = Firebase.database.reference

    override fun registerUser(
        user: User,
        password: String
    ): UserRepositoryResponse {
        Log.d("PERON", "registerUser() called")
        var response = UserRepositoryResponse()
        auth.createUserWithEmailAndPassword(user.email.toString(), password)
            .addOnSuccessListener {
                Log.d("PERON", "Register OnSuccessListener called")
                dbReference.child("users").child(auth.currentUser!!.uid).setValue(user)
                    .addOnSuccessListener {
                        response = fetchUserData()
                    }
                    .addOnFailureListener {
                        response = response.copy(
                            errorMsg = it.localizedMessage
                        )
                    }
            }
            .addOnFailureListener {
                response = response.copy(
                    errorMsg = it.localizedMessage
                )
            }

        return response
    }

    override fun loginUser(email: String, password: String): UserRepositoryResponse {
        Log.d("PERON", "loginUser() called")
        var response = UserRepositoryResponse()
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.d("PERON", "Login OnSuccessListener called")
                Log.d("PERON", "User: ${it.user}")
                response = it.user?.let {
                    fetchUserData()
                } ?: response.copy(
                    errorMsg = "User not found"
                )
                Log.d("PERON", "Fetched user from login: ${response.user}")
            }
            .addOnFailureListener {
                response = response.copy(
                    errorMsg = it.localizedMessage
                )
            }

        return response
    }

    private fun fetchUserData(): UserRepositoryResponse {
        Log.d("PERON", "fetchUserData() called")
        var response = UserRepositoryResponse()
        dbReference.child("users").child(auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                Log.d("PERON", "fetchUserData() OnSuccessListener called")
                response = response.copy(
                    user = it.getValue(User::class.java)
                )
//                Log.d("PERON", "Fetched user: ${response.user}")
            }
            .addOnFailureListener {
                response = response.copy(
                    errorMsg = it.localizedMessage
                )
            }

        return response
    }
}