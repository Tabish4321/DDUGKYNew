package com.deendayalproject.model.uistate

data class CommonInspectionItem(
    val sectionName: String,
    val sectionType: String? = null,
    val subject: String? = null,
    val trainerName: String? = null,
    val candidateName: String? = null,
    val question: String,
    val questionId: Int,
    val candidateId: String? = null,
    val trainerCode: Int? = null,
    val batchId: Int? = null,
    val inspectionId: Int,
    val previousRemark: String? = null,
    val baseImage: String? = null
)
