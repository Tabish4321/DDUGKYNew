package com.deendayalproject.network

sealed class UiState {
    object Loading : UiState()
    data class ShowResult(val base64: String, val count: Int) : UiState()
    data class Error(val message: String) : UiState()
}