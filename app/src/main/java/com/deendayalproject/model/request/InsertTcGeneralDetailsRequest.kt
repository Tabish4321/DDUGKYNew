package com.deendayalproject.model.request

data class InsertTcGeneralDetailsRequest(
    val loginId: String,
    val imeiNo: String,
    val appVersion: String,
    val tcId: String,
    val sanctionOrder: String,
    val signLeakages: String,
    val signLeakagesImage: String,
    val stairsProtection: String,
    val stairsProtectionImage: String,
    val dduConformance: String,
    val centerSafety: String
)
