package com.deendayalproject.model.response

data class OJT_VerifiedBatchListSRLM_Res(

    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: List<VerifiedBatchListSRLM>
)

data class VerifiedBatchListSRLM(
    val batchId: Int,
    val batchName: String,
    val batchRegNo: String,
    val courseName: String,
    val numberOfOJT: Int
)
