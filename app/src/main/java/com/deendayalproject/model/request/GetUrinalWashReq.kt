package com.deendayalproject.model.request

data class GetUrinalWashReq(
    val appVersion: String,
    val facilityId: String,
    val imeiNo: String,
    val loginId: String,
    val sanctionOrder: String,
    val tcId: String
)





