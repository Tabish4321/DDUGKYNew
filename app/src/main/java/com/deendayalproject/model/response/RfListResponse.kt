package com.deendayalproject.model.response

data class RfListResponse(
    val wrappedList: List<TrainingCenterItem>,
    val responseCode: Int,
    val responseDesc: String
)

data class TrainingCenterItem(
    val trainingCenterId: Int,
    val senctionOrder: String,
    val districtName: String,
    val stateName: String,
    val trainingCenterAddress: String,
    val schemeName: String,
    val stateCode: String,
    val trainingCenterName: String,
    val remarks: String,
    val status: String

) : java.io.Serializable

