package com.deendayalproject.model.request

data class PreviousInsQuesReq(
    val appVersion: String,
   // val inspectionId: Int
     val previousInspectionId: Int
)

data class PreviousInsQuesReqN(
    val appVersion: String,
     val inspectionId: Int
)