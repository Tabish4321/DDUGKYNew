package com.deendayalproject.model.uistate

/**
 * Created by Rishi Porwal
 */

data class TrainerAttendanceUiState(

    val inspectionId: Int? = null,
    val trainerCode: Int? = null,

    val trainerAttendanceMatch: String? = null,
    val trainerAttendanceMatchRemark: String = "",

    val trainerCounsellingArranged: String? = null,
    val trainerCounsellingArrangedRemark: String = "",

    val trainerEntryExitAclp: String? = null,
    val trainerEntryExitAclpRemark: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)