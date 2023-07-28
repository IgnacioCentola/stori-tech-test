package com.nacho.domain.usecase

import com.nacho.model.User
import com.nacho.data.repository.UserRepository
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    fun registerUser(user: User, password: String) =
        userRepository.registerUser(
            user = user,
            password = password
        )

    fun loginUser(email: String, password: String) =
        userRepository.loginUser(email, password)

}