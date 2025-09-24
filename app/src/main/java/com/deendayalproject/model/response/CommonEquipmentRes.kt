package com.deendayalproject.model.response



data class CommonEquipmentRes(
    val wrappedList: MutableList<EquipmentItem>,
    val errorsMap: Map<String, Any>,
    val responseCode: Int,
    val responseDesc: String
)

data class EquipmentItem(
    val grievanceRegister: String,
    val digitalCameraImage: String,
    val storageSecuringImage: String,
    val cctvMoniotrInstall: String,
    val printerScanner: Int,
    val printerScannerImage: String,
    val minimumEquipment: String,
    val ecPowerBackup: String,
    val grievanceRegisterImage: String,
    val cctvMoniotrInstallImage: String,
    val storageSecuring: String,
    val directionBoardImage: String,
    val ecPowerBackupImage: String,
    val biomatricDeviceInstallationImage: String,
    val minimumEquipmentImage: String,
    val directionBoard: String,
    val biomatricDeviceInstallation: String,
    val digitalCamera: Int
)

