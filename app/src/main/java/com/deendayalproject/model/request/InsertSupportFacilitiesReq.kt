package com.deendayalproject.model.request


data class InsertSupportFacilitiesReq(
    val loginId: String,
    val appVersion: String,
    val imeiNo: String,
    val trainingCentre: Int,
    val sanctionOrder: String,
    val safeDrinking: String,
    val firstAidKit: String,
    val fireFighting: String,
    val biometricDevice: String,
    val powerBackup: String,
    val grievanceRegister: String,
    val powerBackupFile: String,
    val safeDrinkingFile: String,
    val firstAidKitFile: String,
    val fireFightingFile: String,
    val biometricDeviceFile: String,
    val grievanceRegisterFile: String,
    val facilityId: String

    )
