package com.deendayalproject.model.request

data class TcCommonEquipmentRequest(
    val loginId: String,
    val imeiNo: String,
    val appVersion: String,
    val tcId: String,
    val sanctionOrder: String,
    val ecPowerBackup: String,
    val ecPowerBackupImage: String,
    val biometricDeviceInstall: String,
    val biometricDeviceInstallImage: String,
    val cctvMonitorInstall: String,
    val cctvMonitorInstallImage: String,
    val documentStorageSecuring: String,
    val documentStorageSecuringImage: String,
    val printerScanner: String,
    val printerScannerlImage: String,
    val digitalCamera: String,
    val digitalCameraImage: String,
    val grievanceRegister: String,
    val grievanceRegisterImage: String,
    val minimumEquipment: String,
    val minimumEquipmentImage: String,
    val directionBoards: String,
    val directionBoardsImage: String
)
