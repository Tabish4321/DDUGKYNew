package com.deendayalproject.model.request.assesmentInspection

data class GetCandidateAttendanceInspectionRequest(

    val appVersion: String,
    val candidateId: String,
    val inspectionId: Int,
    val batchId: Int

)