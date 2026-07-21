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

//data class MyTestResponse(
//
//
//    val responseCode: Int,
//
//    val responseDesc: String,
//
//    val wrappedList: List<MyTestItem>
//
//
//)






