package com.deendayalproject.model.request

data class InsertFacultyAttendance(

    val appVersion :String,
    val batchId: Int,
    val trainerCode: String,
    val batchRegNo: String,
    val checkIn: String,
    val checkOut: String,
    val attandanceDate: String,
    val totalHours: String,
    val imeiNo: String,

)
