package com.deendayalproject.model.response.CandidateAssessmentResponse

data class PreviousInspectionObservationResponse(
    val wrappedList: List<Map<String, List<QuestionObservationDto>>>
)

data class QuestionObservationDto(

    val sactionName: String? = null,

    val sactionType: String? = null,

    val inspectionId: Int? = null,

    val questionId: Int? = null,

    val question: String? = null,

    val remark: String? = null,

    val subjectType: String? = null,

    val trainerCode: Int? = null,

    val trainerName: String? = null,

    val candidateId: String? = null,

    val candidateName: String? = null,

    val batchId: Int? = null
)