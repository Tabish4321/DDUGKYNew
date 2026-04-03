package com.deendayalproject.model.response

data class OjtListByBatch_Res(

    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: List<OjtListByBatch>
)

data class OjtListByBatch(
    val employerId: Int,
    val employerName: String,
    val numberOfCandidate: Int,
    val ojtplanId: Int,
    val ojtVerificationCompleted: String,
    val ojtVerificationPending: Int
)

