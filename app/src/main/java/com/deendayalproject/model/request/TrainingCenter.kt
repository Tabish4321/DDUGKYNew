package com.deendayalproject.model.request

data class TrainingCenter(
    val trainingCenterId: Int,
    val senctionOrder: String,
    val trainingCenterAddress: String,
    val trainingCenterName: String,
    val districtName: String,
    val status: String,
    val remarks: String
)
