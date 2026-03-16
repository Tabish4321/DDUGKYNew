package com.deendayalproject.model.response

data class ToiletRoomInformationDataResponse(
    val rfToiletId: Int,
    val flooring: String,
    val floorPdf: String,
    val runningWater: String,
    val lightPdf: String,
    val type: String,
    val lights: String,
    val runningWaterFile: String?,

)


