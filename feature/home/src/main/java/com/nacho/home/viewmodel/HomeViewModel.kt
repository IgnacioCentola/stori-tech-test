package com.nacho.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacho.domain.repository.StoreRepository
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
    private val storeRepository: StoreRepository
) :
    ViewModel() {

    private companion object {
        const val USER_ID = "user_id"
    }

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getUserData(userId: String) {
        viewModelScope.launch {
            Log.d("PERON", "HomeViewModel getUserData() called with id: $userId")
            getUserDataUseCase.getUserData(
                userId = userId,
                onResult = {
                    viewModelScope.launch {
                        storeRepository.putString(USER_ID, it.toString())

                    }
                    _uiState.value = HomeUiState.Success(it)
                },
                onError = {
                    _uiState.value = HomeUiState.Error(it)
                })
        }
    }

}