package com.deendayalproject.model.response

data class OjtListChildSRLMRes(

    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: List<ChildSRLM>
)

data class ChildSRLM(
    val candidateId: String,
    val ojtStartDate: String,
    val ojtEndDate: String,
    val batchStartDate: String,
    val batchEndDate: String,
    val rollNo: Int,
    val trainingCenterName: String,
    val piaName: String,
    val fatherName: String,
    val districtName: String,
    val status: String,
    val workplaceName: String
)
