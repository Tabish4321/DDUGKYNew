package com.deendayalproject.model.request

import com.google.gson.annotations.SerializedName


data class EsopResultRequest(

    @SerializedName("appVersion")
    val appVersion: String,

    @SerializedName("loginId")
    val loginId: String,

    @SerializedName("emailId")
    val emailId: String
)
//data class EsopResultRequest(
//    val appVersion: String,
//    val loginId: String,   // ✅
//    val emailId: String
//)
