package com.deendayalproject.model.response

data class ITLAbDetailsErrorResponse(

    val responseCode: Int,
    val requestDate: Long,
    val url: String,
    val errorMsg: String,
    val responseDesc: String
)
