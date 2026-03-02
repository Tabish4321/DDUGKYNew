package com.deendayalproject.model.response

data class OngoingCandidateRes(

    val wrappedList: List<OngoingCandidateItem>?,
    val responseCode: Int?,
    val responseDesc: String?
)

data class OngoingCandidateItem(
    val candidateId: String?,
    val candidateName: String?,
    val rollNo: Int?,
    val mobileNo: String?,
    val candidateProfilePic: String?,
    val status:Int =0

)