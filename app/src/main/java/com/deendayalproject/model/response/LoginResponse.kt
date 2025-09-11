package com.deendayalproject.model.response


data class LoginResponse(
    val accessToken: String,
    val responseCode: Int,
    val responseDesc: String,
    val userBasicDtls: Any?
)