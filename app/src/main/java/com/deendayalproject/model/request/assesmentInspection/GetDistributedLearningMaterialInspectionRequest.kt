package com.deendayalproject.model.request.assesmentInspection

/**
 * Created by Rishi Porwal
 */
data class GetDistributedLearningMaterialInspectionRequest(
    val appVersion: String,
    val candidateId: String,
    val batchId: Int,
    val inspectionId: Int
)