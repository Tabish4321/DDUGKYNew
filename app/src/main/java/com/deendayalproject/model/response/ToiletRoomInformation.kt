package com.deendayalproject.model.response

data class ToiletRoomInformation(  val wrappedList: MutableList<RoomInformation>,
                                      val responseCode: Int,
                                      val responseDesc: String
)

data class RoomInformation(
    val rfToiletId: String?,
    val flooring: String?,
    val floorPdf: String?,
    val runningWater: String?,
    val lightPdf: String?,
    val type: String?,
    val lights: String?,
)