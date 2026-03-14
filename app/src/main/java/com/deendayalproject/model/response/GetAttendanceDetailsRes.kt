package com.deendayalproject.model.response

data class GetAttendanceDetailsRes(

    val wrappedList: List<AttendanceStatusItem>?,
    val responseCode: Int?,
    val responseDesc: String?
)
data class AttendanceStatusItem(
    val counsellingStatus: String?,
    val regularAttendance: String?,
    val attendanceStatus: String?
)