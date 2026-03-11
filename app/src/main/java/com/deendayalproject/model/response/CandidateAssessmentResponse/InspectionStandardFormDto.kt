package com.deendayalproject.model.response.CandidateAssessmentResponse

data class InspectionStandardFormDto(

    val inspectionId: Int,

    val questionId: Int,

    val answer: String?,

    val remark: String?
)