package com.nacho.domain.usecase

import com.nacho.data.repository.UserRepository
import com.nacho.model.User
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    fun registerUser(
        user: User, password: String, onResult: (user: User) -> Unit,
        onError: (errorMsg: String) -> Unit,
    ) =
        userRepository.registerUser(
            user = user,
            password = password,
            onResult = onResult,
            onError = onError
        )

    fun loginUser(
        email: String, password: String, onResult: (user: User) -> Unit,
        onError: (errorMsg: String) -> Unit,
    ) =
        userRepository.loginUser(
            email = email,
            password = password,
            onResult = onResult,
            onError = onError
        )

}