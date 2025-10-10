package com.deendayalproject.model.response

data class AcademicNonAcademicResponse(
    val wrappedList: MutableList<wrappedList>,
                                         val responseCode: Int,
                                         val responseDesc: String
)

data class wrappedList(
    val roomWidth: String,
    val roomNo: String,
    val roomArea: String,
    val roomLength: String,
    val maxPermissibleCandidate: String,
    val roomType: String
)
