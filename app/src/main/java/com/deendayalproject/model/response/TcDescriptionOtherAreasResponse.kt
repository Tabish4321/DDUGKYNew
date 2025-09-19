package com.deendayalproject.model.response

data class TcDescriptionOtherAreasResponse(
    val responseCode: Int,
    val responseDesc: String,
    val requestDate: Long? = null, // only in error responses
    val url: String? = null,       // only in error responses
    val errorMsg: String? = null   // only in error responses
)
