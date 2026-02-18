package com.deendayalproject.model.uistate

data class CandidateVerificationUiState(

    val externalAssessment: String? = null,
    val externalAssessmentRemarks: String = "",

    val passed: String? = null,
    val passedRemarks: String = "",

    val certificateReceived: String? = null,
    val certificateRemarks: String = "",

    val ojtJoined: String? = null,
    val ojtJoinedRemarks: String = "",

    val ojtCertificateReceived: String? = null,
    val ojtCertificateRemarks: String = "",

    val ojtEntitlementReceived: String? = null,
    val ojtEntitlementRemarks: String = "",

    val ojtDetails: String = "",

    val ojtVerificationDone: String? = null,
    val ojtVerificationRemarks: String = "",

    val videoVerification: String? = null,
    val videoVerificationRemarks: String = "",

    val performancePlanFilled: String? = null,
    val performancePlanRemarks: String = "",

    val offerLetterReceived: String? = null,
    val offerLetterRemarks: String = "",

    val joinedJob: String? = null,
    val joinedJobRemarks: String = "",

    val salary: String = "",
    val salaryRemarks: String = "",

    val minimumWageMatch: String? = null,
    val minimumWageRemarks: String = "",

    val workingStatus: String? = null,
    val workingStatusRemarks: String = "",

    val ppsDisbursed: String? = null,
    val ppsRemarks: String = "",

    val monthsWorking: String = "",

    val notWorkingReason: String = "",

    val actionTaken: String = ""
)
