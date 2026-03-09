package com.deendayalproject.model.request.assesmentInspection

/**
 * Created by Rishi Porwal
 */

data class GetCandidateRecordsVerificationRequest(
    val appVersion: String,
    val candidateId: String,
    val inspectionId: Int,
    val batchId: Int
)
