package com.nacho.domain.usecase

import android.graphics.Bitmap
import com.nacho.domain.repository.UserRepository
import com.nacho.model.User
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    fun registerUser(
        user: User,
        password: String,
        onResult: (userId: String) -> Unit,
        onError: (errorMsg: String) -> Unit,
        idPicture: Bitmap
    ) =
        userRepository.registerUser(
            user = user,
            password = password,
            onResult = onResult,
            onError = onError,
            idPicture = idPicture
        )

    fun loginUser(
        email: String,
        password: String,
        onResult: (userId: String) -> Unit,
        onError: (errorMsg: String) -> Unit,
    ) =
        userRepository.loginUser(
            email = email,
            password = password,
            onResult = onResult,
            onError = onError
        )
}