package com.deendayalproject.model.request

data class OngoingSubmitBasicRecordsReq (

    val appVersion: String,
    val candidateId: String,
    val inspectionId: Int,
    val batchId: Int,

    val povertyProofQid: Int,
    val povertyProof: String,
    val povertyProofRemark: String,

    val categoryProofQid: Int,
    val categoryProof: String,
    val categoryProofRemark: String,

    val minorityProofQid: Int,
    val minorityProof: String,
    val minorityProofRemark: String,

    val educationProofQid: Int,
    val educationProof: String,
    val educationProofRemark: String,

        val pwdProofQid: Int,
        val pwdProof: String,
    val pwdProofRemark: String
)
