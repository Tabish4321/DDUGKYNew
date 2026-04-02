package com.deendayalproject.model.request.assesmentInspection

/**
 * Created by Rishi Porwal
 */


data class SaveTrainerAttendanceInspectionRequest(

    val appVersion: String,
    val inspectionId: Int,
    val trainerCode:Int,


    val trainerAttendanceMatchQid: Int,
    val trainerAttendanceMatch: String,
    val trainerAttendanceMatchRemark: String?,

    val trainerCounsellingArrangedQid: Int,
    val trainerCounsellingArranged: String,
    val trainerCounsellingArrangedRemark: String?,

    val trainerEntryExitAclpQid: Int,
    val trainerEntryExitAclp: String,
    val trainerEntryExitAclpRemark: String?,

    val replacementArrangementQid: Int,
    val replacementArrangement: String?,
    val replacementArrangementRemark: String?

)

