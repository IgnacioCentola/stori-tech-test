package com.nacho.auth

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun onPhotoCaptured(bitmap: Bitmap) {
        updateCapturedPhotoState(bitmap)
    }

    fun onCapturedPhotoConsumed() {
        updateCapturedPhotoState(null)
    }

    private fun updateCapturedPhotoState(updatedPhoto: Bitmap?) {
        _state.value.capturedImage?.recycle()
        _state.value = _state.value.copy(capturedImage = updatedPhoto)
    }

    override fun onCleared() {
        _state.value.capturedImage?.recycle()
        super.onCleared()
    }
}