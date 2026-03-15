package com.deendayalproject.model.uistate

/**
 * Created by Rishi Porwal
 */
data class ResidentialFacilityUiState(

    val answers: List<String?> = List(29) { null },
    val remarks: List<String> = List(29) { "" },

    val washbasins: String = "",
    val washbasinsRemark: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)