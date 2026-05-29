package com.deendayalproject.model.response

data class OJT_OjtVerifiedTrainingCenter_Res(

    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: List<OjtVerifiedTrainingCenter>
)

data class OjtVerifiedTrainingCenter(
    val trainingCenterId: Int,
    val traininrCenterName: String,
    val traininrCenterCode: String
)

