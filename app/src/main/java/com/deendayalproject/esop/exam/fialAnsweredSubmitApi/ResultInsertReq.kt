package com.example.esop.fialAnsweredSubmitApi

import com.google.gson.annotations.SerializedName

data class ResultInsertReq(
    @SerializedName("appVersion")
    val appVersion: String,
    @SerializedName("loginId")
    val loginId: String,
    @SerializedName("emailId")
    val emailId: String,
    @SerializedName("totalQuestion")
    val totalQuestion: Int,
    @SerializedName("wrongAns")
    val wrongAns: Int,
    @SerializedName("numberofAttempt")
    val numberofAttempt: Int,
    @SerializedName("notattempteQuestion")
    val notattempteQuestion: Int,
    @SerializedName("scoredPercentage")
    val scoredPercentage: Int,
    @SerializedName("passingPercentage")
    val passingPercentage: Int,
    @SerializedName("correctAns")
    val correctAns: Int,
    @SerializedName("finalResult")
    val finalResult: Int,
    @SerializedName("departmentCetegory")
    val departmentCetegory: String,
    @SerializedName("issueCertificate")
    val issueCertificate: String,

    @SerializedName("userTypeIe")
    val userTypeIe: String,

    @SerializedName("paaCategory")
    val paaCategory: String,
    @SerializedName("certificateType")
    val certificateType: String,
)

