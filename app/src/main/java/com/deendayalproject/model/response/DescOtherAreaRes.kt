package com.deendayalproject.model.response

data class DescOtherAreaRes(
    val wrappedList: MutableList<CirculationItem>,
    val errorsMap: Map<String, String>?,
    val responseCode: Int,
    val responseDesc: String
)

data class CirculationItem(
    val circulationAreaImagePath: String?,
    val length: String?,
    val corridorNo: String?,
    val areas: String?,
    val parkingSpace: String?,
    val numberOfLights: String?,
    val circulationArea: String?,
    val openSpaceImagePath: String?,
    val parkingSpaceImagePath: String?,
    val width: String?,
    val descProofImagePath: String?,
    val openSpace: String?,
    val numberOfFans: String?
)
