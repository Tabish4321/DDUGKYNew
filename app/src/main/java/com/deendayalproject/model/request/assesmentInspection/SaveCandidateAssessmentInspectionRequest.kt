package com.deendayalproject.model.request.assesmentInspection

/**
 * Created by Rishi Porwal
 */
data class SaveCandidateAssessmentInspectionRequest(

    val appVersion: String,
    val candidateId: String,
    val batchId: Int,
    val inspectionId: Int,

    val presentAssessmentQid: Int,
    val presentAssessment: String,
    val presentAssessmentRemark: String,

    val cameraVerifiedQid: Int,
    val cameraVerified: String,
    val cameraVerifiedRemark: String,

    val seriousnessQid: Int,
    val seriousness: String,
    val seriousnessRemark: String,

    val malpracticesObservedQid: Int,
    val malpracticesObserved: String,
    val malpracticesObservedRemark: String,

    val actualAndRevaluationMarksQid: Int,
    val actualAndRevaluationMarks: String,
    val actualAndRevaluationMarksRemark: String,

    val retestMarksDifferenceQid: Int,
    val retestMarksDifference: String,
    val retestMarksDifferenceRemark: String
)