package com.deendayalproject.model.response.CandidateAssessmentResponse

import com.deendayalproject.model.request.assesmentInspection.SaveCandidateAssessmentInspectionRequest
import com.deendayalproject.model.uistate.CandidateAssessmentUiState

/**
 * Created by Rishi Porwal
 */
data class CandidateAssessmentInspectionDetails(

    val inspectionId: Int,
    val batchId: Int,
    val candidateId: String,

    val presentAssessmentQid: Int,
    val presentAssessment: String?,
    val presentAssessmentRemark: String?,

    val cameraVerifiedQid: Int,
    val cameraVerified: String?,
    val cameraVerifiedRemark: String?,

    val seriousnessQid: Int,
    val seriousness: String?,
    val seriousnessRemark: String?,

    val malpracticesObservedQid: Int,
    val malpracticesObserved: String?,
    val malpracticesObservedRemark: String?,

    val actualAndRevaluationMarksQid: Int,
    val actualAndRevaluationMarks: String?,
    val actualAndRevaluationMarksRemark: String?,

    val retestMarksDifferenceQid: Int,
    val retestMarksDifference: String?,
    val retestMarksDifferenceRemark: String?
)





