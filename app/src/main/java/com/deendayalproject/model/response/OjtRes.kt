package com.deendayalproject.model.response

data class OjtRes(

    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: List<OjtBatchRes>
)

data class OjtBatchRes(
    val candidateId: String,
    val candidateName: String,
    val verificationDate: String,
    val verificationStatus: String,
    val ojtPlanId: Int
//    val employerId: Int,
//    val employerName: String,
//    val numberOfCandidate: Int,
//    val ojtplanId: Int,
//    val ojtVerificationCompleted: String,
//    val ojtVerificationPending: Int
)

