package com.deendayalproject.model.response

data class TrainerStaff(
    val profileType: String,
    val name: String,
    val designation: String,
    val engagementType: String,
    val domainNonDomain: String,
    val assignedCourse: String,
    val whetherTotCer: String,
    val totCertificateNo: String
)
