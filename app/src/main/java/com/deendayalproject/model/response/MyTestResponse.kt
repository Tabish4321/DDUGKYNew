package com.example.esop.mytest

import com.google.gson.annotations.SerializedName


data class MyTestResponse(

    @SerializedName("responseCode")
    val responseCode: Int,

    @SerializedName("responseDesc")
    val responseDesc: String,

    @SerializedName("wrappedList")
    val wrappedList: List<MyTestItem> = emptyList()
)






