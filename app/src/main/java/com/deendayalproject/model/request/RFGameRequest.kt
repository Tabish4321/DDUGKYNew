package com.deendayalproject.model.request

data class RFGameRequest(
    val appVersion: String,
    val imeiNo: String,
    val tcId: Int,
    val sanctionOrder: String,
    val facilityId:Int
)
