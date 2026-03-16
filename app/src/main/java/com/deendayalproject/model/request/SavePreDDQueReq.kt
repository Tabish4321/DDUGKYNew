package com.deendayalproject.model.request

data class SavePreDDQueReq(
    val appVersion: String = "",
    val inspectionId: Int = 0,
    val questionId: Int = 0,
    val answer: String = "",
    val trainingCenterId: Int = 0,
    val remark: String = ""

)
