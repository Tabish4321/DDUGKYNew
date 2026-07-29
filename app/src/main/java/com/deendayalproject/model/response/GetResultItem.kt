package com.deendayalproject.model.response



import com.google.gson.annotations.SerializedName
data class GetResultItem(


    @SerializedName("responseCode")
    val responseCode: Int,

    @SerializedName("responseDesc")
    val responseDesc: String,

    @SerializedName("wrappedList")
    val wrappedList: List<ResultItem> = emptyList()

)
