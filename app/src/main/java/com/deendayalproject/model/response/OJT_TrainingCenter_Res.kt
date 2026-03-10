package com.deendayalproject.model.response

data class OJT_TrainingCenter_Res(

    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: List<OJTTrainingCenterName>
)

data class OJTTrainingCenterName(
    val trainingCenterId: Int,
    val trainingCenterName: String
)

