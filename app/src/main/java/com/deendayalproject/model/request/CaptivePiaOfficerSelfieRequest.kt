package com.deendayalproject.model.request

data class CaptivePiaOfficerSelfieRequest(
    val appVersion: String,
    val comment: String,
    val officerLatitude: String,
    val officerLongitude: String,
    val officerPhoto: String,
    val loginId: String,
    val createdBy: String
)