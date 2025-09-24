package com.deendayalproject.model.response

data class TcAcademiaNonAcademiaRes(
    val wrappedList: MutableList<RoomItem>,
    val responseCode: Int,
    val responseDesc: String
)

data class RoomItem(
    val roomWidth: String,
    val roomNo: String,
    val roomArea: String,
    val roomLength: String,
    val maxPermissibleCandidate: String,
    val roomType: String
)
