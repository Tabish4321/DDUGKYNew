package com.deendayalproject.model.request

data class savePreviousInspectionQuesReq(
    val appVersion: String,
    val previousInspectionId: Int,
    val candidateId: String,
    val batchId: Int,
    val questionId: Int,
    val answer: String,
    val remark: String,
    val sactionName: String,
    val sactionType: String,
    val attachment: String,
    val trainerCode: Int,
    val subject: String
)
