package com.deendayalproject.model.response

data class CandidateListInspectionRes(
    val candidateId: String,
    val name: String,
    val rollNumber: String,
    val contactNumber: String,
    val candidateProfilePic: String? = null,
    val isLoading: Boolean = false,
    val status:Int =0

)
