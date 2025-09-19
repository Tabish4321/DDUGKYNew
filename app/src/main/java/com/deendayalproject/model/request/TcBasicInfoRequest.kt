package com.deendayalproject.model.request

data class TcBasicInfoRequest(

    val loginId: String,
    val imeiNo: String,
    val appVersion: String,
    val tcId: String,
    val sanctionOrder: String,
    val distanceBus: String,
    val distanceAuto: String,
    val distanceRailway: String,
    val latitude: String,
    val longitude: String,
    val geoAddress: String

)
