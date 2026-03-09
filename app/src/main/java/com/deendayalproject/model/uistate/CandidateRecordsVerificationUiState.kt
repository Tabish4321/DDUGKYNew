package com.deendayalproject.model.uistate

/**
 * Created by Rishi Porwal
 */
data class CandidateRecordsVerificationUiState(

    val isLoading: Boolean = false,
    val error: String? = null,

    val inspectionId: Int? = null,
    val batchId: Int? = null,
    val candidateId: String? = null,

    val povertyProof: String? = null,
    val povertyProofRemark: String = "",

    val categoryProof: String? = null,
    val categoryProofRemark: String = "",

    val minorityProof: String? = null,
    val minorityProofRemark: String = "",

    val educationProof: String? = null,
    val educationProofRemark: String = "",

    val pwdProof: String? = null,
    val pwdProofRemark: String = ""
)
