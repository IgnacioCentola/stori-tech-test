package com.nacho.data.repository

import com.nacho.model.User

interface UserRepository {

    fun registerUser(
        user: User,
        password: String
    ): UserRepositoryResponse

    fun loginUser(email: String, password: String): UserRepositoryResponse
}