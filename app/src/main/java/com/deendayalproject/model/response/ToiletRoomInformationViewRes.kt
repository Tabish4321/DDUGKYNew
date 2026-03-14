package com.deendayalproject.model.response

data class ToiletRoomInformationViewRes(
    val wrappedList: MutableList<ToiletRoomInformationDataResponse>,
    val responseCode: Int,
    val responseDesc: String
)
