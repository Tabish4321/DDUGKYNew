package com.deendayalproject.model.request

data class TcDescriptionOtherAreasRequest(
    val loginId: String,
    val imeiNo: String,
    val appVersion: String,
    val tcId: String,
    val sanctionOrder: String,
    val corridorNo: String,
    val length: String,
    val width: String,
    val areas: String,
    val lights: String,
    val fans: String,
    val proofImage: String,
    val circulationArea: String,
    val circulationAreaImage: String,
    val openSpace: String,
    val openSpaceImage: String,
    val parkingSpace: String,
    val parkingSpaceImage: String
)

