package com.nacho.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacho.domain.repository.StoreRepository
import com.nacho.domain.usecase.AuthenticateUserUseCase
import com.nacho.model.AuthUiState
import com.nacho.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val storeRepository: StoreRepository
) :
    ViewModel() {
    private companion object {
        const val USER_ID = "user_id"
    }

    fun getCachedUserId() = runBlocking {
        storeRepository.getString(USER_ID).orEmpty()
    }

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Default)
    val uiState = _uiState.asStateFlow()

    fun registerUser(
        user: User,
        password: String
    ) {
        viewModelScope.launch {
            authenticateUserUseCase.registerUser(
                user = user,
                password = password,
                onResult = { id ->
                    viewModelScope.launch {
                        storeRepository.putString(USER_ID, id)
                    }
                    _uiState.value = AuthUiState.Success
                },
                onError = {
                    _uiState.value = AuthUiState.Error(msg = it)
                })
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            authenticateUserUseCase.loginUser(
                email = email,
                password = password,
                onResult = { id ->
                    viewModelScope.launch {
                        storeRepository.putString(USER_ID, id)
                    }
                    Log.d("PERON", "AuthViewModel saved userId: $id")
                    _uiState.value = AuthUiState.Success
                },
                onError = {
                    _uiState.value = AuthUiState.Error(msg = it)
                })
        }
    }

}

