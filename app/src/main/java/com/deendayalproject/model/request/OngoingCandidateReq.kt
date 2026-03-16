package com.deendayalproject.model.request

data class OngoingCandidateReq(
    val batchId: Int,
    val appVersion: String,
    val inspectionId: Int
)
