package com.nacho.data.repository

import com.nacho.model.User

data class UserRepositoryResponse(
    val user: User? = null,
    val errorMsg: String? = null
)
