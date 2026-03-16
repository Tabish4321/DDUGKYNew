package com.deendayalproject.model.uistate

/**
 * Created by Rishi Porwal
 */

data class InspectionSectionStatusUiState(

    val isLoading: Boolean = false,
    val error: String? = null,

    val recordStatus: Int = 0,
    val attendanceStatus: Int = 0,
    val assessmentStatus: Int = 0,
    val learningMaterialStatus: Int = 0,
    val entitlementsDistributionStatus: Int = 0,
    val rfVerificationStatus: Int = 0
)