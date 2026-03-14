package com.deendayalproject.model.uistate

data class TrainerClassObservationUiState(

    val subject: String = "",

    val answers: List<String?> = List(14) { null },

    val remarks: List<String> = List(14) { "" },

    val isLoading: Boolean = false,

    val error: String? = null,

    val saveSuccess: Boolean = false
)