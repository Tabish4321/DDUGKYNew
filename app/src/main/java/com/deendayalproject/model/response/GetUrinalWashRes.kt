package com.deendayalproject.model.response

data class GetUrinalWashRes(
    val wrappedList: List<UrinalData>,
    val responseCode: Int,
    val responseDesc: String
)

data class UrinalData(
    val urinal: String,
    val washbasin: String,
    val overheadTank: String,
    val urinalFile: String,
    val washbasinFile: String,
    val overheadTankFile: String,
)
