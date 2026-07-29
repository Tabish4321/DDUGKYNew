package com.deendayalproject.model.response

import com.google.gson.annotations.SerializedName

data class ResultItem(
    @SerializedName("answer_given")
    val answer_given: String? = null,

    @SerializedName("Question_id")
    val Question_id: Int? = null,

    @SerializedName("question_title")
    val question_title: String? = null,

    @SerializedName("correctAnswer")
    val correctAnswer: String? = null,

    @SerializedName("correctAnswertext")
    val correctAnswertext: String? = null,

    @SerializedName("AnswerGiventext")
    val AnswerGiventext: String? = null
)
