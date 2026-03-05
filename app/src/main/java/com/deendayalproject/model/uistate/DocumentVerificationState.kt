package com.deendayalproject.model.uistate

data class DocumentVerificationState(

    val title: String,
    val qid: Int,
    val image: String?,
    val answer: String? = null,
    val remarks: String = ""

)