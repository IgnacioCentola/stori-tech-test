package com.nacho.model

sealed class AuthUiState {
    data class Success(val user: User) : AuthUiState()
    object Loading : AuthUiState()
    data class Error(val msg: String) : AuthUiState()
}