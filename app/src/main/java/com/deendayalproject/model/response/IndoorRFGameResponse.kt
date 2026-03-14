package com.deendayalproject.model.response

data class IndoorRFGameResponse(
    val wrappedList: MutableList<IndoorRFGameResponseDetails>,
    val responseCode: Int,
    val responseDesc: String
)
