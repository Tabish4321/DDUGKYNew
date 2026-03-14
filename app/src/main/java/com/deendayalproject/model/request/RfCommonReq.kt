package com.deendayalproject.model.request

data class RfCommonReq(

    val appVersion: String,
    val loginId: String,
    val tcId: Int,
    val sanctionOrder: String,
    val imeiNo: String,
    val facilityId: Int
)
