package com.deendayalproject.model.response

data class EsopCandidateRes(
    val wrappedList: List<EsopCandidate>,
    val responseCode: Int,
    val responseDesc: String,
    val facilityId: Int,
    val resultImage: String?,
    val wrappedLista: Any?
) {

    data class EsopCandidate(
        val loginId: String,
        val gender: String?,
        val dob: String?,
        val mobile: String,
        val emailId: String,
        val categories: List<EsopCategory>,
        val userName: String,
        val aadhaarNumber: String,
    )


    data class EsopCategory(
        val category: String
    )
}