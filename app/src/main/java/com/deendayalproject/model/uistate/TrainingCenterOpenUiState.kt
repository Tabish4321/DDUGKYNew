package com.deendayalproject.model.uistate

data class TrainingCenterOpenUiState(
    val answer: String? = null,
    val remarks: String = "",
    val attachmentBase64: String? = null,
    val remarksError: Boolean = false,
    val attachmentError: Boolean = false
)
