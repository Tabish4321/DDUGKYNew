package com.deendayalproject.model.request

data class ElectricalWiringRequest(

    val loginId: String,
    val imeiNo: String,
    val appVersion: String,
    val tcId: String,
    val sanctionOrder: String,
    val switchBoard: String,
    val switchBoardImage: String,
    val wireSecurity: String,
    val wireSecurityImage: String

)
