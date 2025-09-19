package com.deendayalproject.model.response

data class TcSignagesInfoBoardResponse(
    val responseCode: Int,
    val responseDesc: String,
    val requestDate: Long? = null,
    val url: String? = null,
    val errorMsg: String? = null
)

