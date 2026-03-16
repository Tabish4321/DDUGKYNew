package com.deendayalproject.model.request

data class CandidatePreviousBatchReq(
    val batchId: Int,
    val appVersion: String,
    val inspectionId: Int
)

