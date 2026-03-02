package com.deendayalproject.model.request

/**
 * Created by Rishi Porwal
 */
data class SaveBatchVerificationRequest(
    val appVersion: String,
    val batchId: Int,
    val inspectionId: Int,
    val candidateId: String,

    val externalAssessmentQId: Int = 1,
    val externalAssessment: String?,
    val externalAssessmentRemark: String?,

    val passedFailedQId: Int = 2,
    val passedFailed: String?,
    val passedFailedRemark: String?,

    val receivedCertificateQId: Int = 3,
    val receivedCertificate: String?,
    val receivedCertificateRemark: String?,

    val ojtJoinedQId: Int = 4,
    val ojtJoined: String?,
    val ojtJoinedRemark: String?,

    val ojtCertificateReceivedQId: Int = 5,
    val ojtCertificateReceived: String?,
    val ojtCertificateReceivedRemark: String?,

    val ojtEntitlementReceivedQId: Int = 6,
    val ojtEntitlementReceived: String?,
    val ojtEntitlementReceivedRemark: String?,
    val entitlementDetails: String?,

    val ojtVerificationQId: Int = 7,
    val ojtVerification: String?,
    val ojtVerificationRemark: String?,

    val performanceEvaluationPlanQId: Int = 8,
    val performanceEvaluationPlan: String?,
    val performanceEvaluationPlanRemark: String?,

    val gotOfferLetterQId: Int = 9,
    val gotOfferLetter: String?,
    val gotOfferLetterRemark: String?,

    val jobJoinedQId: Int = 10,
    val jobJoined: String?,
    val jobJoinedRemark: String?,

    val salaryQId: Int = 11,
    val salary: String?,
    val salaryRemark: String?,

    val matchWithMinimumWagesOfTheStateQId: Int = 12,
    val matchWithMinimumWagesOfTheState: String?,
    val matchWithMinimumWagesOfTheStateRemark: String?,

    val candidateCurrentStatusQId: Int = 13,
    val candidateCurrentStatus: String?,
    val candidateCurrentStatusRemark: String?,

    val pssAmountDisbursedToTheCandidateQId: Int = 14,
    val pssAmountDisbursedToTheCandidate: String?,
    val pssAmountDisbursedToTheCandidateRemark: String?,

    val workingMonthNumbers: Int?,
    val reasonForNotWorking: String?,
    val replacementActionByPia: String?
)