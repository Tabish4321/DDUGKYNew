package com.deendayalproject.model.request

data class SavePreviousInsQueRes(
    val appVersion: String,
    val inspectionId: String,
    val questionId: String,
    val answer: String,
    val remark: String

)

