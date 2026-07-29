package com.example.esop.quetions_esop


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QuestionResponse(

    @SerializedName("status")
    @Expose
    val status: String,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("Questions")
    @Expose
    val Questions: List<Question>,

    @SerializedName("summary")
    @Expose
    val summary: Summary
)

data class Question(

    @SerializedName("questionId")
    @Expose
    val questionId: Int,

    @SerializedName("questionTitle")
    @Expose
    val questionTitle: String,

    @SerializedName("options")
    @Expose
    val options: List<Option>
)

data class Option(

    @SerializedName("option_Key")
    @Expose
    val option_Key: String,

    @SerializedName("option_value")
    @Expose
    val option_value: String
)

data class Summary(

    @SerializedName("totalQuestions")
    @Expose
    val totalQuestions: Int,

    @SerializedName("easyCount")
    @Expose
    val easyCount: Int,

    @SerializedName("mediumCount")
    @Expose
    val mediumCount: Int,

    @SerializedName("hardCount")
    @Expose
    val hardCount: Int,

    @SerializedName("numberofAttempt")
    @Expose
    val numberofAttempt: Int,

    @SerializedName("easyPercentage")
    @Expose
    val easyPercentage: Double,

    @SerializedName("mediumPercentage")
    @Expose
    val mediumPercentage: Double,

    @SerializedName("hardPercentage")
    @Expose
    val hardPercentage: Double
)

