package com.deendayalproject.model.uistate

data class PreviousObservationUiState(
    val questionId: Int,
    val title: String,
    val conductedBy: String,
    val originalRemarks: String,
    val selectionYesNo: String? = null,
    val inputRemarks: String = ""
)