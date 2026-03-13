package com.deendayalproject.model.response


//code commit 13/03/2026 Time 10:51 AM if user login DDUGKYUSER coditions mein static data use ho rhaa hai textView ke case mein
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

