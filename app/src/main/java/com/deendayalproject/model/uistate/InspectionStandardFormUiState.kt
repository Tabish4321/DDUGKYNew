package com.deendayalproject.model.uistate

data class InspectionStandardFormUiState(

    val inspectionId: Int? = null,

    val answers: List<String?> = List(45) { null },

    val remarks: List<String> = List(45) { "" },

    val isLoading: Boolean = false,

    val error: String? = null,
    val saveSuccess: Boolean = false

)