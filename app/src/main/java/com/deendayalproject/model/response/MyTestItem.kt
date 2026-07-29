package com.example.esop.mytest


import com.google.gson.annotations.SerializedName

data class MyTestItem(

//    @SerializedName("loginid")
//    val loginid: String? = null,

    @SerializedName("wrongAns")
    val wrongAns: String? = null,

    @SerializedName("scoredPercentage")
    val scoredPercentage: String? = null,

    @SerializedName("departmentCetegory")
    val departmentCetegory: String? = null,

    @SerializedName("numberofAttempt")
    val numberofAttempt: String? = null,

    @SerializedName("passingPercentage")
    val passingPercentage: String? = null,

    @SerializedName("notattempteQuestion")
    val notattempteQuestion: String? = null,


    @SerializedName("id")
     val id: Int? = null,


    @SerializedName("issueCertificate")
    val issueCertificate: String? = null,

    @SerializedName("resultdate")
    val resultdate: String? = null,

    @SerializedName("correctAns")
    val correctAns: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("totalQuestion")
    val totalQuestion: String? = null,

    @SerializedName("finalResult")
    val finalResult: String? = null,

    @SerializedName("certificateType")
    val certificateType: String? = null
)

