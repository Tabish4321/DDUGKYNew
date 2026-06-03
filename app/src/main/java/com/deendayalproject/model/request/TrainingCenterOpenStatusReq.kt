package com.deendayalproject.model.request

data class TrainingCenterOpenStatusReq(
     val appVersion: String,
     val trainingCenterId: String,
     val inspectionId: String,
     val trainingCenterOpenOrNot: String,
     val tcOpenOrNotRemark: String,
     val tcOpenOrNotAttachment: String
)
