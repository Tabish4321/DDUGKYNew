package com.deendayalproject.model.response

data class ElectricalWireRes(
    val wrappedList: MutableList<ElectricalItem>,
    val responseCode: Int,
    val responseDesc: String
)

data class ElectricalItem(
    val switchBoard: String?,
    val wireSecurity: String?,
    val wireSecurityImage: String?,
    val switchBoardImage: String?
)
