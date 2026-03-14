package com.deendayalproject.model.response.CandidateAssessmentResponse

/**
 * Created by Rishi Porwal
 */

data class TrainerAttendanceInspectionResponse(

    val inspectionId: Int,
    val trainerCode:Int,
    val trainerAttendanceMatchQid: Int,
    val trainerAttendanceMatch: String?,
    val trainerAttendanceMatchRemark: String?,

    val trainerCounsellingArrangedQid: Int,
    val trainerCounsellingArranged: String?,
    val trainerCounsellingArrangedRemark: String?,

    val trainerEntryExitAclpQid: Int,
    val trainerEntryExitAclp: String?,
    val trainerEntryExitAclpRemark: String?
)