package com.deendayalproject.model.response

data class TrainingCenterListInspecRes(

    val id: Int,
    val prnNumber: String,
    val sanctionLetterNo: String,
    val centerNameAddress: String,
    val inspectionType: String
)
