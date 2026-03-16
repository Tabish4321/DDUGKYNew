package com.deendayalproject.model.request

data class InsertToiletDataReq(
    val loginId: String,
    val appVersion: String,
    val imeiNo: String,
    val facilityId: Int,
    val trainingCentre: Int,
    val sanctionOrder: String,
    val type: String,
    val lights: Int,
    val flooring: String,
    val runningWater: String,
    val runningWaterFile: String,
    val proofFloor: String,
    val proofLight: String
)
