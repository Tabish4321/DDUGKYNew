package com.deendayalproject.model.response

data class TrainingCenterListInspecRes(
    val id: Int,
    val prnNumber: String,
    val sanctionLetterNo: String,
    val inspectionType: String,
    val inspectionId: String,
    val centerType: String,
    val inspectionDate: String?,
    val trainingCenterCode: String?,
    val piaName: String?,
    val trainingCenterName: String?,
    val inspectionCode: String?
)
