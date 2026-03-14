package com.deendayalproject.model.response

data class RFSupportFacilitiesAvailableResponse(
    val wrappedList: MutableList<RFSupportFacilitiesAvailableDetails>,
    val responseCode: Int,
    val responseDesc: String
)
data class RFSupportFacilitiesAvailableDetails(
    val grievanceRegister: String,
    val fireFighting: String,
    val biometricDevicePdf: String,
    val firstAidKitPdf: String,
    val fireFightingPdf: String,
    val grievanceRegisterPdf: String,
    val biometricDevice: String,
    val firstAidKit: String,
    val safeDrinkingPdf: String,
    val safeDrinking: String,
    val powerBackupPdf: String,
    val powerBackup: String

)