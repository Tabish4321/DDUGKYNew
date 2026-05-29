package com.deendayalproject.model.response

data class CaptivePiaOfficerSelfieResponse(
    val wrappedList: List<OfficerSelfieItem>?,
    val responseCode: Int,
    val responseDesc: String?
)
