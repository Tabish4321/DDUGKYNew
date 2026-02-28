package com.deendayalproject.model

import com.deendayalproject.model.uistate.CandidateVerificationUiState

/**
* Created by Rishi Porwal 
*/

data class QuestionConfig(
    val id: String,
    val title: String,
    val dependsOn: String? = null,
    val showIfYes: Boolean = true
)

val questionList = listOf(

    QuestionConfig("externalAssessment", "External Assessment Completed"),

    QuestionConfig(
        "passedFailed",
        "If Yes, Passed / Failed",
        dependsOn = "externalAssessment"
    ),

    QuestionConfig("certificateReceived", "Received certificate (if eligible)"),

    QuestionConfig("ojtJoined", "OJT Joined"),

    QuestionConfig("ojtCertificateReceived", "OJT certificate received (4.6B & 4.6C)"),

    QuestionConfig("ojtEntitlementReceived", "OJT Entitlement received"),

    QuestionConfig("ojtVerificationDone", "OJT verification done by the PIA Q.Team"),

    QuestionConfig("offerLetterReceived", "Got offer letter"),

    QuestionConfig("performancePlanFilled", "Performance Evaluation Plan (SF 4.3N) filled or not"),

    QuestionConfig("joinedJob", "Joined the job"),

    QuestionConfig("minimumWageMatch", "Is it match with the Minimum wages of the state"),

    QuestionConfig("ppsDisbursed", "PPS Amount disbursed to the candidates (as per the eligibility)")
)

fun CandidateVerificationUiState.getAnswer(id: String): String? =
    when (id) {
        "externalAssessment" -> externalAssessment
        "passedFailed" -> passedFailed
        "certificateReceived" -> certificateReceived
        "ojtJoined" -> ojtJoined
        "ojtCertificateReceived" -> ojtCertificateReceived
        "ojtEntitlementReceived" -> ojtEntitlementReceived
        "ojtVerificationDone" -> ojtVerificationDone
        "offerLetterReceived" -> offerLetterReceived
        "performancePlanFilled" -> performancePlanFilled
        "joinedJob" -> joinedJob
        "minimumWageMatch" -> minimumWageMatch
        "ppsDisbursed" -> ppsDisbursed
        else -> null
    }

fun CandidateVerificationUiState.getRemarks(id: String): String =
    when (id) {
        "externalAssessment" -> externalAssessmentRemarks
        "passedFailed" -> passedFailedRemarks
        "certificateReceived" -> certificateReceivedRemarks
        "ojtJoined" -> ojtJoinedRemarks
        "ojtCertificateReceived" -> ojtCertificateReceivedRemarks
        "ojtEntitlementReceived" -> ojtEntitlementRemarks
        "ojtVerificationDone" -> ojtVerificationRemarks
        "offerLetterReceived" -> offerLetterRemarks
        "performancePlanFilled" -> performancePlanRemarks
        "joinedJob" -> joinedJobRemarks
        "minimumWageMatch" -> minimumWageRemarks
        "ppsDisbursed" -> ppsDisbursedRemarks
        else -> ""
    }


fun CandidateVerificationUiState.updateAnswer(
    id: String,
    value: String
): CandidateVerificationUiState =
    when (id) {
        "externalAssessment" -> copy(externalAssessment = value, externalAssessmentRemarks = "")
        "passedFailed" -> copy(passedFailed = value, passedFailedRemarks = "")
        "certificateReceived" -> copy(certificateReceived = value, certificateReceivedRemarks = "")
        "ojtJoined" -> copy(ojtJoined = value, ojtJoinedRemarks = "")
        "ojtCertificateReceived" -> copy(ojtCertificateReceived = value, ojtCertificateReceivedRemarks = "")
        "ojtEntitlementReceived" -> copy(ojtEntitlementReceived = value, ojtEntitlementRemarks = "")
        "ojtVerificationDone" -> copy(ojtVerificationDone = value, ojtVerificationRemarks = "")
        "offerLetterReceived" -> copy(offerLetterReceived = value, offerLetterRemarks = "")
        "performancePlanFilled" -> copy(performancePlanFilled = value, performancePlanRemarks = "")
        "joinedJob" -> copy(joinedJob = value, joinedJobRemarks = "")
        "minimumWageMatch" -> copy(minimumWageMatch = value, minimumWageRemarks = "")
        "ppsDisbursed" -> copy(ppsDisbursed = value, ppsDisbursedRemarks = "")
        else -> this
    }

fun CandidateVerificationUiState.updateRemarks(
    id: String,
    value: String
): CandidateVerificationUiState =
    when (id) {
        "externalAssessment" -> copy(externalAssessmentRemarks = value)
        "passedFailed" -> copy(passedFailedRemarks = value)
        "certificateReceived" -> copy(certificateReceivedRemarks = value)
        "ojtJoined" -> copy(ojtJoinedRemarks = value)
        "ojtCertificateReceived" -> copy(ojtCertificateReceivedRemarks = value)
        "ojtEntitlementReceived" -> copy(ojtEntitlementRemarks = value)
        "ojtVerificationDone" -> copy(ojtVerificationRemarks = value)
        "offerLetterReceived" -> copy(offerLetterRemarks = value)
        "performancePlanFilled" -> copy(performancePlanRemarks = value)
        "joinedJob" -> copy(joinedJobRemarks = value)
        "minimumWageMatch" -> copy(minimumWageRemarks = value)
        "ppsDisbursed" -> copy(ppsDisbursedRemarks = value)
        else -> this
    }


 fun validateBeforeSave(state: CandidateVerificationUiState): String? {

    fun requireAnswer(answer: String?, field: String): String? =
        if (answer.isNullOrBlank())
            "Please select $field"
        else null

    fun requireRemarksIfNo(answer: String?, remarks: String, field: String): String? =
        if (answer.equals("No", true) && remarks.isBlank())
            "Please enter remarks for $field"
        else null

    requireAnswer(state.externalAssessment, "External Assessment")?.let { return it }
    requireRemarksIfNo(state.externalAssessment, state.externalAssessmentRemarks, "External Assessment")?.let { return it }

    if (state.externalAssessment.equals("Yes", true)) {
        requireAnswer(state.passedFailed, "Passed / Failed")?.let { return it }
        requireRemarksIfNo(state.passedFailed, state.passedFailedRemarks, "Passed / Failed")?.let { return it }
    }

    requireAnswer(state.certificateReceived, "Received Certificate")?.let { return it }
    requireRemarksIfNo(state.certificateReceived, state.certificateReceivedRemarks, "Certificate")?.let { return it }

    requireAnswer(state.ojtJoined, "OJT Joined")?.let { return it }
    requireRemarksIfNo(state.ojtJoined, state.ojtJoinedRemarks, "OJT Joined")?.let { return it }

    requireAnswer(state.ojtEntitlementReceived, "OJT Entitlement")?.let { return it }
    requireRemarksIfNo(state.ojtEntitlementReceived, state.ojtEntitlementRemarks, "OJT Entitlement")?.let { return it }

    if (state.ojtEntitlementReceived.equals("Yes", true)
        && state.ojtEntitlementDetails.isBlank()
    ) {
        return "Please enter OJT entitlement details"
    }

    requireAnswer(state.joinedJob, "Joined Job")?.let { return it }
    requireRemarksIfNo(state.joinedJob, state.joinedJobRemarks, "Joined Job")?.let { return it }

    if (state.joinedJob.equals("Yes", true)) {

        val salaryValue = state.salary.toDoubleOrNull()

        if (salaryValue == null || salaryValue <= 0.0) {
            return "Salary must be greater than 0"
        }
    }

    requireAnswer(state.minimumWageMatch, "Minimum Wage Match")?.let { return it }
    requireRemarksIfNo(state.minimumWageMatch, state.minimumWageRemarks, "Minimum Wage Match")?.let { return it }

    requireAnswer(state.ppsDisbursed, "PPS Amount Disbursed")?.let { return it }
    requireRemarksIfNo(state.ppsDisbursed, state.ppsDisbursedRemarks, "PPS Amount Disbursed")?.let { return it }

    if (state.replacementAction.isBlank()) {
        return "Please enter replacement action"
    }

    return null
}