package com.deendayalproject.model.request.assesmentInspection

data class SaveCandidateAttendanceInspectionRequest(

    val appVersion: String,

    val candidateId: String,
    val inspectionId: Int,
    val batchId: Int,

    val biomatricAttendanceQid: Int,
    val biomatricAttendance: String,
    val biomatricAttendanceRemark: String,
    val biomatricAttendancePiaRemark: String = "",

    val attendanceCounsellingQid: Int,
    val attendanceCounselling: String,
    val attendanceCounsellingRemark: String,
    val attendanceCounsellingPiaRemark: String = "",

    val regularlyAttendQid: Int,
    val regularlyAttend: String,
    val regularlyAttendRemark: String,
    val regularlyAttendPiaRemark: String = ""
)