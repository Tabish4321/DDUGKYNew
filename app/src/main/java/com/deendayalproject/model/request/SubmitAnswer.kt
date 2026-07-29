package com.example.esop.AswersOptionSubmit

import com.google.gson.annotations.SerializedName

data class SubmitAnswer(
    @SerializedName("question_id")
    val question_id: Int,

    @SerializedName("answer_given")
    val answer_given: String
)
