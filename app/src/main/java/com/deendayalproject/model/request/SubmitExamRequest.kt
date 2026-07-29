package com.example.esop.AswersOptionSubmit

import com.google.gson.annotations.SerializedName

data class SubmitExamRequest(
    @SerializedName("appVersion")
    val appVersion: String,
    @SerializedName("Course_Type")
    val courseType: Int,
    @SerializedName("courseName")
    val courseName: String,

    @SerializedName("certificateType")
    val certificateType: String,

    @SerializedName("userTypeIe")
    val userTypeIe: String,

    @SerializedName("paaCategory")
     val paaCategory: String,
    @SerializedName("loginId")
    val loginId: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("answers")
    val answers: List<SubmitAnswer>
)





