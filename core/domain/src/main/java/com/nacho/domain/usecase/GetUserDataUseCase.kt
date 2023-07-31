package com.nacho.domain.usecase

import com.nacho.domain.repository.UserRepository
import com.nacho.model.User
import javax.inject.Inject


class GetUserDataUseCase @Inject constructor(private val userRepository: UserRepository){

    fun getUserData(
        userId: String,
        onResult: (user: User) -> Unit,
        onError: (errorMsg: String) -> Unit,
    ) =
        userRepository.fetchUserData(
            userId = userId,
            onError = onError,
            onResult = onResult
        )
}