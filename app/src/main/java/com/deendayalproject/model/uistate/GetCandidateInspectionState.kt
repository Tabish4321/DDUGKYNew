package com.deendayalproject.model.uistate

/**
 * Created by Rishi Porwal
 */

data class GetCandidateInspectionRequest(
    val appVersion: String,
    val batchId: Int,
    val inspectionId: Int,
    val candidateId: String
)



data class CandidateInspectionDto(
    val externalAssessment: String?,
    val externalAssessmentRemark: String?,
    val passedFailed: String?,
    val passedFailedRemark: String?,
    val receivedCertificate: String?,
    val receivedCertificateRemark: String?,
    val ojtJoined: String?,
    val ojtJoinedRemark: String?,
    val ojtCertificateReceived: String?,
    val ojtCertificateReceivedRemark: String?,
    val ojtEntitlementReceived: String?,
    val ojtEntitlementReceivedRemark: String?,
    val entitlementDetails: String?,
    val ojtVerification: String?,
    val ojtVerificationRemark: String?,
    val performanceEvaluationPlan: String?,
    val performanceEvaluationPlanRemark: String?,
    val gotOfferLetter: String?,
    val gotOfferLetterRemark: String?,
    val jobJoined: String?,
    val jobJoinedRemark: String?,
    val salary: Double?,
    val salaryRemark: String?,
    val matchWithMinimumWagesOfTheState: String?,
    val matchWithMinimumWagesOfTheStateRemark: String?,
    val candidateCurrentStatus: String?,
    val candidateCurrentStatusRemark: String?,
    val pssAmountDisbursedToTheCandidate: String?,
    val pssAmountDisbursedToTheCandidateRemark: String?,
    val workingMonthNumbers: Int?,
    val reasonForNotWorking: String?,
    val replacementActionByPia: String?
)