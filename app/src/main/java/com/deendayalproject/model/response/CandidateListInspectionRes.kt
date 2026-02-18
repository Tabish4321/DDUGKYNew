package com.deendayalproject.model.response

data class CandidateListInspectionRes(
    val candidateId: Int,
    val name: String,
    val rollNumber: String,
    val contactNumber: String,
    val imageUrl: String? = null,
    val isLoading: Boolean = false


)
