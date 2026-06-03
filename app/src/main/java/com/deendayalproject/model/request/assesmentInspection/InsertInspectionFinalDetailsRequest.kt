package com.deendayalproject.model.request.assesmentInspection

data class InsertInspectionFinalDetailsRequest(
    val appVersion: String,
    val inspectionId: Int,
    val trainingCenterId: Int,
    val finalRemark: String,
    val finalRemarkAttachment:String
)