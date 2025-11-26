package com.deendayalproject.model.response

data class LivingAreaListRes(    val wrappedList: List<LivingRoomListItem>,
                                 val errorsMap: Map<String, String>,
                                 val responseCode: Int,
                                 val responseDesc: String)



data class LivingRoomListItem(
    val area: Double,
    val livingRoomId: Int,
    val roomNo: Int,
    val length: Double,
    val totalRfCapacity: Int,
    val width: Double
)

