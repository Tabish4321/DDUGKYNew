package com.deendayalproject.model.uistate

data class CandidateAttendanceUiState(

    val inspectionId: Int? = null,
    val batchId: Int? = null,
    val candidateId: String? = null,

    val biomatricAttendance: String? = null,
    val biomatricAttendanceRemark: String = "",

    val attendanceCounselling: String? = null,
    val attendanceCounsellingRemark: String = "",

    val regularlyAttend: String? = null,
    val regularlyAttendRemark: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)