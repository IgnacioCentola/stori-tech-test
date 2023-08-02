package com.nacho.domain.repository

import android.graphics.Bitmap
import com.nacho.model.User


interface UserRepository {

    fun registerUser(
        user: User,
        password: String,
        idPicture: Bitmap,
        onResult: (userId: String) -> Unit,
        onError: (errorMsg: String) -> Unit,
    )

    fun loginUser(
        email: String,
        password: String,
        onResult: (userId: String) -> Unit,
        onError: (errorMsg: String) -> Unit,
    )

    fun fetchUserData(
        userId: String,
        onResult: (user: User) -> Unit,
        onError: (errorMsg: String) -> Unit,
    )
}