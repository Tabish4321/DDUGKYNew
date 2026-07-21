package com.deendayalproject.esop.result

import com.google.gson.annotations.SerializedName

data class GetResultViewRequest(

    val appVersion: String,
    val loginId: String,
    val numberofAttempt: String,
    val certificateType: String,
    val departmentCetegory: String
)
