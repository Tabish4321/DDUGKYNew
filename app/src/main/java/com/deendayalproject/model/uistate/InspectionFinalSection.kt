package com.deendayalproject.model.uistate

import com.deendayalproject.model.response.CandidateAssessmentResponse.QuestionObservationDto

data class InspectionSection(

    val sectionName: String,

    val items: List<QuestionObservationDto>

)


//data class PreviousInspectionUiState(
//    val isLoading: Boolean = false,
//    val sections: List<InspectionSection> = emptyList(),
//    val error: String? = null
//
//)
//
//
//private fun mapSections(
//    sectionMap: Map<String, List<QuestionObservationDto>>
//): List<InspectionSection> {
//
//    return sectionMap.map { entry ->
//
//        InspectionSection(
//
//            sectionName = entry.key,
//
//            items = entry.value
//
//        )
//
//    }
//}

data class PreviousInspectionObservationDto(

    val DocumentsStandardFormsAvailabilitySaction: List<ObservationItemDto>? = null,

    val TrainingQualitySection: List<ObservationItemDto>? = null,

    val ValidateTrainerAttendanceSaction: List<ObservationItemDto>? = null,

    val PreviousBatchDataVerificationSaction: List<ObservationItemDto>? = null,

    val OngoingBatchCandidateSection: List<ObservationItemDto>? = null
)


data class ObservationItemDto(

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

//fun mapSections(
//    dto: PreviousInspectionObservationDto
//): Map<String, List<ObservationItemDto>> {
//
//    return buildMap {
//
//        dto.DocumentsStandardFormsAvailabilitySaction?.let {
//            put("DocumentsStandardFormsAvailability", it)
//        }
//
//        dto.TrainingQualitySection?.let {
//            put("TrainingQuality", it)
//        }
//
//        dto.ValidateTrainerAttendanceSaction?.let {
//            put("ValidateTrainerAttendance", it)
//        }
//
//        dto.PreviousBatchDataVerificationSaction?.let {
//            put("PreviousBatchDataVerification", it)
//        }
//
//        dto.OngoingBatchCandidateSection?.let {
//            put("OngoingBatchCandidate", it)
//        }
//    }
//}

data class PreviousInspectionObservationUiState(
    val isLoading: Boolean = false,

    val sections: Map<String, List<ObservationItemDto>> = emptyMap(),

    val answers: Map<Int, String> = emptyMap(),

    val remarks: Map<Int, String> = emptyMap(),

    val submitSuccess: Boolean = false,

    val error: String? = null

)

data class FinalSubmitUiState(

    val isLoading: Boolean = false,

    val submitSuccess: Boolean = false,

    val error: String? = null

)