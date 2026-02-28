package com.deendayalproject.model.uistate

data class CandidateVerificationUiState(

    val isLoading: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null,
    val showValidation: Boolean = false,

    var externalAssessment: String? = null,
    var externalAssessmentRemarks: String = "",

    var passedFailed: String? = null,
    var passedFailedRemarks: String = "",
    var certificateReceived: String? = null,
    var certificateReceivedRemarks: String = "",

    var ojtJoined: String? = null,
    var ojtJoinedRemarks: String = "",

    var ojtCertificateReceived: String? = null,
    var ojtCertificateReceivedRemarks: String = "",

    var ojtEntitlementReceived: String? = null,
    var ojtEntitlementRemarks: String = "",



    // OJT Entitlement Details (If Yes)
    var ojtEntitlementDetails: String = "",

// OJT Verification
    var ojtVerificationDone: String? = null,
    var ojtVerificationRemarks: String = "",



// Performance Plan
    var performancePlanFilled: String? = null,
    var performancePlanRemarks: String = "",

// Offer Letter
    var offerLetterReceived: String? = null,
    var offerLetterRemarks: String = "",


    var joinedJob: String? = null,
    var joinedJobRemarks: String = "",

    var salary: String = "",

    var minimumWageMatch: String? = null,
    var minimumWageRemarks: String = "",

    var currentStatus: String? = null,
    var currentStatusRemarks: String = "",

    var workingMonths: String = "",
    var notWorkingReason: String = "",


    var ppsDisbursed: String? = null,
    var ppsDisbursedRemarks: String = "",

    var replacementAction: String = ""

    )


