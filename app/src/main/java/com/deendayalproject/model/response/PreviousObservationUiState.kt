package com.deendayalproject.model.response

data class PreviousObservationUiState(
    val conductedBy: String,
    val title: String,
    val originalRemarks: String,
    val selectionYesNo: String? = null,
    val inputRemarks: String = "",
    val questionId: Int,
)
