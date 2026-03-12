package com.deendayalproject.model.response

data class PreviousObservationRes(
    val title: String,
    val conductedBy: String,
    val remarks: String,
    val questionId: Int,
    val preAnswer: String? = null,
    val preRemark: String? = null
)