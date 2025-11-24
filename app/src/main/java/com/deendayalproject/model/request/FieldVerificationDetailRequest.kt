package com.deendayalproject.model.request

data class FieldVerificationDetailRequest(

    val loginId: String,
    val appVersion: String,
    val captiveEmpanelmentId: String,
    val prnNo: String


)
