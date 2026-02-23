package com.deendayalproject.model.response

data class NonceResponse(
    val nonce: String,
    val responseCode: Int,
    val responseDesc: String
)
