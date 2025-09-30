package com.deendayalproject.model.request

data class AllRoomDetaisReques(
    val loginId: String,
    val imeiNo: String,
    val appVersion: String,
    val tcId: String,
    val roomType: String,
    val roomNo: Int,
    val sanctionOrder: String
)
