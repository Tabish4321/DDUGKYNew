package com.deendayalproject.model.response

data class CCTVComplianceResponse(

    val responseCode: Int,
    val requestDate: String?,
    val url: String?,
    val errorMsg: String?,
    val errors: ErrorDetails?,
    val responseDesc: String?,
    val wrappedList: List<Any>? = null,   // Optional, present in success response
    val errorsMap: Map<String, String>? = null  // Optional, present in success response
)

data class ErrorDetails(
    val errorMsg: String?
)
