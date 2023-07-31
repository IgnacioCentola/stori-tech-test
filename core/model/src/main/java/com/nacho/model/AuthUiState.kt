package com.nacho.model

sealed class AuthUiState {
    object Default: AuthUiState()
    object Success : AuthUiState()
    object Loading : AuthUiState()
    data class Error(val msg: String) : AuthUiState()
}