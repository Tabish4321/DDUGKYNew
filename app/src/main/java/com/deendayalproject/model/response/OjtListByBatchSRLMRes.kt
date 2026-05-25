package com.deendayalproject.model.response

data class OjtListByBatchSRLMRes(

    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: List<ListByBatchSRLM>
)

data class ListByBatchSRLM(
    val candidateId: String,
    val candidateName: String,
    val verificationDate: String,
    val verificationStatus: String,
    val ojtPlanId: Int
)

//"candidateId": "2608023051",
//            "candidateName": "Minni Sharma",
//            "verificationDate": "NA",
//            "verificationStatus": "NA",
//            "ojtPlanId": 15