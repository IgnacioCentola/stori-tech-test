package com.nacho.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacho.domain.preferences.Preferences
import com.nacho.domain.usecase.AuthenticateUserUseCase
import com.nacho.model.AuthUiState
import com.nacho.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val preferences: Preferences
) :
    ViewModel() {

    var userId = preferences.getUserId()
        private set

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
                    preferences.saveUserId(id)
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
                    preferences.saveUserId(id)
                    Log.d("PERON", "AuthViewModel saved userId: $userId")
                    _uiState.value = AuthUiState.Success
                },
                onError = {
                    _uiState.value = AuthUiState.Error(msg = it)
                })
        }
    }

}

