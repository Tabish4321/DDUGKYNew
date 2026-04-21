package com.deendayalproject.model.response

data class InspectionTcDetailsRes(
    val wrappedList: List<TrainingInspCenterDetails>,
    val responseCode: Int,
    val responseDesc: String


)


data class TrainingInspCenterDetails(
    val trainingCenterName: String,
    val trainingCenterId: String,
    val trainingCenterIncharge: String,
    val inchargeName: String,
    val mobileNumber: String,
    val emailId: String,
    val tradeAndCapacity: String,
    val coordinate: String,
    val roleName: String,
    val revisedSanctionOrderDoc: String
)