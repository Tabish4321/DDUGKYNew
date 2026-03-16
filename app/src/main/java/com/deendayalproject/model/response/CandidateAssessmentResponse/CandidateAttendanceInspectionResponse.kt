package com.deendayalproject.model.response.CandidateAssessmentResponse

data class CandidateAttendanceInspectionResponse(

    val inspectionId: Int,
    val batchId: Int,
    val candidateId: String,

    val biomatricAttendanceQid: Int,
    val biomatricAttendance: String?,
    val biomatricAttendanceRemark: String?,

    val attendanceCounsellingQid: Int,
    val attendanceCounselling: String?,
    val attendanceCounsellingRemark: String?,

    val regularlyAttendQid: Int,
    val regularlyAttend: String?,
    val regularlyAttendRemark: String?,
    val responseCode: Int,
    val responseDesc: String,
)