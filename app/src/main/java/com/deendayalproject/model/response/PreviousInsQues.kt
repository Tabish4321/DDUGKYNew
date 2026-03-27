package com.deendayalproject.model.response

data class PreviousInsQues(

    val wrappedList: List<InspectionFullDetails>,
    val responseCode: Int,
    val responseDesc: String
)

data class InspectionFullDetails(

    val DocumentsStandardFormsAvailabilitySaction: List<DocumentsStandardFormsAvailability>?,
    val TrainingQualitySection: List<TrainingQuality>?,
    val ValidateTrainerAttendanceSaction: List<ValidateTrainerAttendance>?,
    val PreviousBatchDataVerificationSaction: List<PreviousBatchDataVerification>?,
    val OngoingBatchCandidateSection: List<OngoingBatchCandidate>?

)

data class DocumentsStandardFormsAvailability(
    val sactionName: String?,
    val inspectionId: Int?,
    val questionId: Int?,
    val question: String?,
    val remark: String?
)

data class TrainingQuality(
    val sactionName: String?,
    val inspectionId: Int?,
    val subjectType: String?,
    val questionId: Int?,
    val question: String?,
    val remark: String?
)

data class ValidateTrainerAttendance(
    val sactionName: String?,
    val inspectionId: Int?,
    val trainerCode: Int?,
    val questionId: Int?,
    val question: String?,
    val remark: String?,
    val trainerName: String?,
    val previousInspectionId:Int?
)

data class PreviousBatchDataVerification(
    val sactionName: String?,
    val candidateId: String?,
    val candidateName: String?,
    val batchId: Int?,
    val inspectionId: Int?,
    val questionId: Int?,
    val question: String?,
    val remark: String?
)

data class OngoingBatchCandidate(
    val sactionName: String?,
    val sactionType: String?,
    val candidateId: String?,
    val candidateName: String?,
    val batchId: Int?,
    val inspectionId: Int?,
    val questionId: Int?,
    val question: String?,
    val baseImageUrl: String?,
    val remark: String?
)