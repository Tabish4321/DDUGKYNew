package com.deendayalproject.model.response

data class TcStaffAndTrainerResponse(
    val wrappedList: MutableList<Trainer>,
    val responseCode: Int,
    val responseDesc: String
)

data class Trainer(
    val totCertificateNo: String,
    val trainerName: String,
    val trainerDesignation: String,
    val profileType: String,
    val assignedCourse: String,
    val engagementType: String,
    val totCertificate: String,
    val domainNondomain: String
)
