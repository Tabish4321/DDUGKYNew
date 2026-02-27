package com.deendayalproject.base

data class BaseResponse<T>(
    val wrappedList: T?,
    val errorsMap: Map<String, String>?,
    val responseCode: Int,
    val responseDesc: String,
    val facilityId: Int,
    val resultImage: String?
)
