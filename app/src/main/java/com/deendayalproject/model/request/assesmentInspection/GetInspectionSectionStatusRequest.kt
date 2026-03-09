package com.deendayalproject.model.request.assesmentInspection

/**
 * Created by Rishi Porwal
 */

data class GetInspectionSectionStatusRequest(
    val appVersion: String,
    val candidateId: String,
    val inspectionId: Int
)