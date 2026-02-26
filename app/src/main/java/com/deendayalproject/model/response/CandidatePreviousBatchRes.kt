package com.deendayalproject.model.response


data class CandidatePreviousBatchRes(
    val wrappedList: List<CandidateItem>?,
    val responseCode: Int?,
    val responseDesc: String?
)

data class CandidateItem(
    val candidateId: String?,
    val candidateName: String?,
    val rollNo: Int?,
    val mobileNo: String?,
    val candidateProfilePic: String?
)