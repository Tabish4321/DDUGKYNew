package com.deendayalproject.model.uistate

/**
 * Created by Rishi Porwal
 */
data class CandidateAssessmentUiState(

    /* -------------------------------- */
    /* COMMON UI STATES */
    /* -------------------------------- */

    val isLoading: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false,
    val showValidation: Boolean = false,


    /* -------------------------------- */
    /* ASSESSMENT STATUS API */
    /* assessmentStatusForInspection */
    /* -------------------------------- */

    val assessmentStatus: String? = null,
    val isPresent: String? = null,
    val assessmentDate: String? = null,


    /* -------------------------------- */
    /* PRESENT DURING ASSESSMENT */
    /* QID 1 */
    /* -------------------------------- */

    val presentAssessmentQid: Int = 1,
    val presentAssessment: String? = null,
    val presentAssessmentRemark: String = "",


    /* -------------------------------- */
    /* CAMERA VERIFIED */
    /* QID 2 */
    /* -------------------------------- */

    val cameraVerifiedQid: Int = 2,
    val cameraVerified: String? = null,
    val cameraVerifiedRemark: String = "",


    /* -------------------------------- */
    /* SERIOUSNESS DURING EXAM */
    /* QID 3 */
    /* -------------------------------- */

    val seriousnessQid: Int = 3,
    val seriousness: String? = null,
    val seriousnessRemark: String = "",


    /* -------------------------------- */
    /* MALPRACTICE OBSERVED */
    /* QID 4 */
    /* -------------------------------- */

    val malpracticesObservedQid: Int = 4,
    val malpracticesObserved: String? = null,
    val malpracticesObservedRemark: String = "",


    /* -------------------------------- */
    /* ACTUAL VS REVALUATION MARKS */
    /* QID 5 */
    /* -------------------------------- */

    val actualAndRevaluationMarksQid: Int = 5,
    val actualAndRevaluationMarks: String? = null,
    val actualAndRevaluationMarksRemark: String = "",


    /* -------------------------------- */
    /* RETEST MARK DIFFERENCE */
    /* QID 6 */
    /* -------------------------------- */

    val retestMarksDifferenceQid: Int = 6,
    val retestMarksDifference: String? = null,
    val retestMarksDifferenceRemark: String = ""

)


data class CandidateAssessmentStatusUiState(

    val isLoading: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false,

    val assessmentStatus: String? = null,
    val isPresent: String? = null,
    val assessmentDate: String? = null
)
