package com.nacho.model

sealed class HomeUiState{
    data class Success(val user: User) : HomeUiState()
    object Loading : HomeUiState()
    data class Error(val msg: String) : HomeUiState()
}
