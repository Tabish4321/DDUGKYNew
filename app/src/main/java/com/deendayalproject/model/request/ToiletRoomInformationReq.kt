package com.deendayalproject.model.request

data class ToiletRoomInformationReq(
    val appVersion: String,
    val tcId: Int,
    val sanctionOrder: String,
    val toiletType: String,
    val facilityId:Int
)