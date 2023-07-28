package com.nacho.data.repository

import com.nacho.model.User


interface UserRepository {

    fun registerUser(
        user: User,
        password: String,
        onResult: (user: User) -> Unit,
        onError: (errorMsg: String) -> Unit,
    )

    fun loginUser(
        email: String,
        password: String,
        onResult: (user: User) -> Unit,
        onError: (errorMsg: String) -> Unit,
    )
}