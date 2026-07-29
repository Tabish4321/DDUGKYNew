package com.deendayalproject.model.request

import com.google.gson.annotations.SerializedName

data class QuestiontReq(

    @SerializedName("appVersion")
    val appVersion: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("certytype")
    val certytype: String,
    @SerializedName("paacategory")
    val paacategory: String
)