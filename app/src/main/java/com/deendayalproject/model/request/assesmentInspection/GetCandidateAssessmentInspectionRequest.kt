package com.deendayalproject.model.request.assesmentInspection

/**
 * Created by Rishi Porwal
 */

data class GetCandidateAssessmentInspectionRequest(
    val appVersion: String,
    val candidateId: String,
    val batchId: Int,
    val inspectionId: Int
)