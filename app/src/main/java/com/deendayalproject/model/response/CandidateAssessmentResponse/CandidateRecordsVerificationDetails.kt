package com.deendayalproject.model.response.CandidateAssessmentResponse

/**
 * Created by Rishi Porwal
 */
data class CandidateRecordsVerificationDetails(

    val inspectionId: Int,
    val batchId: Int,
    val candidateId: String,

    val povertyProof: String?,
    val povertyProofRemark: String?,

    val categoryProof: String?,
    val categoryProofRemark: String?,

    val minorityProof: String?,
    val minorityProofRemark: String?,

    val educationProof: String?,
    val educationProofRemark: String?,

    val pwdProof: String?,
    val pwdProofRemark: String?
)
