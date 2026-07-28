package com.deendayalproject.esop.certificate

import com.google.gson.annotations.SerializedName

data class CertificateRequest(


//    @SerializedName("appVersion")
//    val appVersion: String,

    @SerializedName("loginId")
    val loginId: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("certificateType")
    val certificateType: String,

)
