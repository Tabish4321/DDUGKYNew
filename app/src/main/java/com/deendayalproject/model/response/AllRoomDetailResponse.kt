package com.deendayalproject.model.response


data class AllRoomDetailResponse(
    val wrappedList: List<RoomDetail>,
    val errorsMap: Map<String, Any>,
    val responseCode: Int,
    val responseDesc: String
)

data class RoomDetail(
    val fansImage: String?,
    val writingBoard: String?,
    val internetConnectionImage: String?,
    val roomInfoBoardImage: String?,
    val digitalProjectorImage: String?,
    val officeComputer: String?,
    val printerScannerImage: String?,
    val centerSoundProof: String?,
    val falseCeiling: String?,
    val tablet: String?,   // changed
    val typingTuterCompImage: String?,
    val lanEnabledImage: String?,
    val internalSignageImage: String?,
    val airConditionRoom: String?,
    val roomsPhotographs: String?,
    val roomsPhotographsImage: String?,
    val audioCamera: String?,
    val lanEnabled: String?,   // changed
    val soundLevelImage: String?,
    val centerSoundProofImage: String?,
    val digitalCameraImage: String?,
    val internetConnection: String?,
    val officeChair: String?,   // changed
    val officeTableImage: String?,
    val printerScanner: String?,   // changed
    val trainerChair: String?,
    val domainEquipmentImage: String?,
    val ecPowerBackup: String?,
    val tabletImage: String?,
    val soundLevel: String?,   // changed
    val trainerTable: String?,
    val falseCeilingImage: String?,
    val roomInfoBoard: String?,
    val roofTypeImage: String?,
    val digitalProjector: String?,
    val secureDocumentStorage: String?,
    val airConditionRoomImage: String?,
    val sounfLevelSpecific: String?,
    val ventilationArea: String?,   // changed
    val domainEquipment: String?,   // changed
    val officeTable: String?,   // changed
    val officeChairImage: String?,
    val typingTuterComp: String?,
    val ceilingHeightImage: String?,
    val candidateChair: String?,
    val candidateChairImage: String?,
    val ceilingHeight: String?,   // changed
    val lightsImage: String?,
    val secureDocumentStorageImage: String?,
    val writingBoardImage: String?,
    val lights: String?,   // changed
    val digitalCamera: String?,   // changed
    val audioCameraImage: String?,
    val internalSignage: String?,
    val trainerChairImage: String?,
    val ventilationAreaImage: String?,
    val roofType: String,
    val trainerTableImage: String?,
    val fans: String?,   // changed
    val officeComputerImagePath: String?,
    val ecPowerBackupImage: String?
)

