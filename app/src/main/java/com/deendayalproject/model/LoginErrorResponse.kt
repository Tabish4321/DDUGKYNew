package com.deendayalproject.model

data class LoginErrorResponse(
    val responseCode: Int,
    val requestDate: String?,
    val url: String?,
    val errorMsg: String,
    val errors: Any?,
    val responseDesc: String
)
