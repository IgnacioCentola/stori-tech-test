package com.nacho.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacho.domain.usecase.GetUserDataUseCase
import com.nacho.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
) :
    ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getUserData(userId: String) {
        viewModelScope.launch {
            Log.d("PERON", "HomeViewModel getUserData() called with id: $userId")
            getUserDataUseCase.getUserData(
                userId = userId,
                onResult = {
                    _uiState.value = HomeUiState.Success(it)
                },
                onError = {
                    _uiState.value = HomeUiState.Error(it)
                })
        }
    }

}