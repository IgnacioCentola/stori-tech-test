package com.nacho.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacho.domain.usecase.AuthenticateUserUseCase
import com.nacho.model.AuthUiState
import com.nacho.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authenticateUserUseCase: AuthenticateUserUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun registerUser(
        user: User,
        password: String
    ) {
        viewModelScope.launch {
            authenticateUserUseCase.registerUser(user = user, password = password)
                .also { response ->
                    val error = response.errorMsg ?: "Unknown error"
                    _uiState.value = response.user?.let {
                        AuthUiState.Success(user = it)
                    } ?: AuthUiState.Error(msg = error)
                }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            authenticateUserUseCase.loginUser(email, password).also { response ->
                val error = response.errorMsg ?: "Unknown error"
                _uiState.value = response.user?.let {
                    AuthUiState.Success(user = it)
                } ?: AuthUiState.Error(msg = error)
            }
        }
    }

}
