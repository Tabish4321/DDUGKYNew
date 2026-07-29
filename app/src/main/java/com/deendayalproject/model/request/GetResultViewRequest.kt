package com.deendayalproject.model.request

data class GetResultViewRequest(

    val appVersion: String,
    val loginId: String,
    val numberofAttempt: String,
    val certificateType: String,
    val departmentCetegory: String
)