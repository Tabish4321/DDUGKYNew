package com.deendayalproject.model.uistate

/**
 * Created by Rishi Porwal
 */
data class CandidateRecordsVerificationUiState(

    val inspectionId: Int = 0,
    val batchId: Int = 0,
    val candidateId: String = "",

    val povertyProof: String = "",
    val povertyProofRemark: String = "",

    val categoryProof: String = "",
    val categoryProofRemark: String = "",

    val minorityProof: String = "",
    val minorityProofRemark: String = "",

    val educationProof: String = "",
    val educationProofRemark: String = "",

    val pwdProof: String = "",
    val pwdProofRemark: String = "",

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)
