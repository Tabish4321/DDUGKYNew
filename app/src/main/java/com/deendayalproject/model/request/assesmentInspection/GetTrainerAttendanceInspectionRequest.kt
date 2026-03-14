package com.deendayalproject.model.request.assesmentInspection

/**
 * Created by Rishi Porwal
 */

data class GetTrainerAttendanceInspectionRequest(

    val appVersion: String,
    val trainerCode:Int,
    val inspectionId: Int

)