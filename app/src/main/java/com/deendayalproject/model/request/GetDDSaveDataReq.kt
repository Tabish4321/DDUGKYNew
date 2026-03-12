package com.deendayalproject.model.request

data class GetDDSaveDataReq(
    val inspectionId :String,
    val questionId: String,
    val appVersion: String,
    val trainingCenterId: String
)

