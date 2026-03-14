package com.deendayalproject.model.request

data class TrainingCenter(
    val trainingCenterId: Int,
    val senctionOrder: String,
    val trainingCenterAddress: String,
    val trainingCenterName: String,
    val districtName: String,
    val tcMaleCapacity: String,
    val tcFemaleCapacity: String,
    val tcCapacity: String,
    val status: String,
    val remarks: String,
    val letitude: String?,
    //val latitude: String?,
    val longitude: String?,
    val radius: Int?
)
