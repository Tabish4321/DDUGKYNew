package com.deendayalproject.model.request



data class UrinalWashbasinReq(
    val loginId: String,
    val appVersion: String,
    val imeiNo: String,
    val facilityId: Int,
    val trainingCentre: Int,
    val sanctionOrder: String,

    val urinal: Int,
    val washbasin: Int,
    val overheadTank: String,

    val urinalFile: String,
    val washbasinFile: String,
    val overheadTankFile: String
)
