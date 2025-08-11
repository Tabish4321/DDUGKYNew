package com.deendayalproject.model.request

data class LoginRequest(
    val loginId: String,
    val password: String,
    val imeiNo: String,
    val appVersion: String
)