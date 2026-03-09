package com.deendayalproject.model.response.CandidateAssessmentResponse

/**
 * Created by Rishi Porwal
 */

data class InspectionSectionStatusResponse(
    val recordStatus: Int,
    val attendanceStatus: Int,
    val assessmentStatus: Int,
    val learningMaterialStatus: Int,
    val entitlementsDistributionStatus: Int,
    val rfVerificationStatus: Int
)