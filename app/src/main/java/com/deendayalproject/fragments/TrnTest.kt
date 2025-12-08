//package com.deendayalproject.fragments
//
//import SharedViewModel
//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.content.Context
//import android.content.DialogInterface
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import android.os.Bundle
//import android.os.Environment
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Base64
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.inputmethod.InputMethodManager
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.Spinner
//import android.widget.TextView
//import android.widget.Toast
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AlertDialog
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.activityViewModels
//import androidx.lifecycle.lifecycleScope
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.deendayalproject.BuildConfig
//import com.deendayalproject.R
//import com.deendayalproject.adapter.AcademicAreaAdapter
//import com.deendayalproject.databinding.FragmentTrainingBinding
//import com.deendayalproject.model.SectionHandler
//import com.deendayalproject.model.request.AcademicNonAcademicArea
//import com.deendayalproject.model.request.CCTVComplianceRequest
//import com.deendayalproject.model.request.DLRequest
//import com.deendayalproject.model.request.ElectricalWiringRequest
//import com.deendayalproject.model.request.ITComeDomainLabDetailsRequest
//import com.deendayalproject.model.request.ITLabDetailsRequest
//import com.deendayalproject.model.request.InsertTcGeneralDetailsRequest
//import com.deendayalproject.model.request.OfficeRoomDetailsRequest
//import com.deendayalproject.model.request.ReceptionAreaRoomDetailsRequest
//import com.deendayalproject.model.request.SubmitOfficeCumCounsellingRoomDetailsRequest
//import com.deendayalproject.model.request.TCDLRequest
//import com.deendayalproject.model.request.TCITLDomainLabDetailsRequest
//import com.deendayalproject.model.request.TCRRequest
//import com.deendayalproject.model.request.TcAvailabilitySupportInfraRequest
//import com.deendayalproject.model.request.TcBasicInfoRequest
//import com.deendayalproject.model.request.TcCommonEquipmentRequest
//import com.deendayalproject.model.request.TcDescriptionOtherAreasRequest
//import com.deendayalproject.model.request.TcSignagesInfoBoardRequest
//import com.deendayalproject.model.request.ToiletDetailsRequest
//import com.deendayalproject.model.request.TrainingCenterInfo
//import com.deendayalproject.model.response.SectionStatus
//import com.deendayalproject.model.response.wrappedList
//import com.deendayalproject.util.AppConstant.STATUS_QM
//import com.deendayalproject.util.AppConstant.STATUS_SM
//import com.deendayalproject.util.AppUtil
//import com.deendayalproject.util.ProgressDialogUtil
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.location.Priority
//import com.google.android.material.textfield.TextInputEditText
//import com.google.gson.Gson
//import com.google.gson.GsonBuilder
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import java.io.File
//import java.io.IOException
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//class TrainingFragment : Fragment() {
//
//    private lateinit var Academicadapter: AcademicAreaAdapter
//    private var centersList = mutableListOf<wrappedList>()
//    private var RoomNumber: String = ""
//    private var RoomType: String = ""
//
//    private lateinit var binding: FragmentTrainingBinding
//    private val viewModel: SharedViewModel by activityViewModels()
//    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
//    private lateinit var permissionLauncher: ActivityResultLauncher<String>
//    private lateinit var photoUri: Uri
//    private var currentPhotoTarget: String = ""
//    private var centerId: String = ""
//    private var sanctionOrder: String = ""
//    private var status: String? = ""
//    private var remarks: String? = ""
//    private var trainingCenterName: String? = ""
//    private lateinit var sectionsStatus: SectionStatus
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//
//    // Training center information
//    private var base64MonitorFile: String? = null
//    private var base64ConformanceFile: String? = null
//    private var base64StorageFile: String? = null
//    private var base64DVRFile: String? = null
//
//    // Electrical Wiring
//    private var base64SwitchBoardImage: String? = null
//    private var base64WireSecurityImage: String? = null
//
//    // General Details
//    private var base64LeakageImage: String? = null
//    private var base64StairsImage: String? = null
//
//    // Base64 Image Strings for Signages
//    private var base64TcNameBoardImage: String? = null
//    private var base64ActivityAchievementBoardImage: String? = null
//    private var base64StudentEntitlementBoardImage: String? = null
//    private var base64ContactDetailBoardImage: String? = null
//    private var base64BasicInfoBoardImage: String? = null
//    private var base64CodeConductBoardImage: String? = null
//    private var base64StudentAttendanceBoardImage: String? = null
//
//    // Common Equipment Base64
//    private var base64PowerBackupImage: String? = null
//    private var base64BiometricDevices: String? = null
//    private var base64CCTVImage: String? = null
//    private var base64DocumentStorageImage: String? = null
//    private var base64PrinterScanner: String? = null
//    private var base64DigitalCamera: String? = null
//    private var base64GrievanceRegisterImage: String? = null
//    private var base64MinimumEquipmentImage: String? = null
//    private var base64DirectionBoardsImage: String? = null
//
//    // Support Infra
//    private var base64SafeDrinkingWater: String? = null
//    private var base64FireFightingEquipment: String? = null
//    private var base64FirstAidKit: String? = null
//
//    // description of other area
//    private var base64ProofUploadImage: String? = null
//    private var base64CirculationProofImage: String? = null
//    private var base64penSpaceProofImage: String? = null
//    private var base64ParkingSpaceProofImage: String? = null
//
//    // WashBasin
//    private var base64ProofMaleToilets: String? = null
//    private var base64ProofMaleToiletsSignage: String? = null
//    private var base64ProofFemaleToilets: String? = null
//    private var base64ProofFemaleToiletsSignage: String? = null
//    private var base64ProofMaleUrinals: String? = null
//    private var base64ProofMaleWashBasins: String? = null
//    private var base64ProofFemaleWashBasins: String? = null
//    private var base64ProofOverheadTanks: String? = null
//    private var base64ProofFlooring: String? = null
//
//    // IT LAB Images
//    private var base64ProofPreviewITLTypeofRoofItLab: String? = null
//    private var base64ProofITLFalseCellingProvide: String? = null
//    private var base64ProofITLHeightOfCelling: String? = null
//    private var base64ProofITLVentilationAreaInSqFt: String? = null
//    private var base64ProofITLSoundLevelAsPerSpecifications: String? = null
//    private var base64ProofITLSoundLevelInDb: String? = null
//    private var base64ProofITLwhether_all_the_academic: String? = null
//    private var base64ProofITLAcademicRoomInformationBoard: String? = null
//    private var base64ProofITLInternalSignage: String? = null
//    private var base64ProofITLCctcCamerasWithAudioFacility: String? = null
//    private var base64ProofITLLanEnabledComputersInNo: String? = null
//    private var base64ProofITLInternetConnections: String? = null
//    private var base64ProofITLDoAllComputersHaveTypingTutor: String? = null
//    private var base64ProofITLTablets: String? = null
//    private var base64ProofITLStoolsChairs: String? = null
//    private var base64ProofITLTrainerChair: String? = null
//    private var base64ProofITLTrainerTable: String? = null
//    private var base64ProofITLLightsInNo: String? = null
//    private var base64ProofITLFansInNo: String? = null
//    private var base64ProofITLElectricaPowerBackUpForThRoom: String? = null
//    private var base64ProofITLItLabPhotograph: String? = null
//    private var base64ProofITLDoes_the_room_has: String? = null
//
//    // Office Cum(Counselling room)
//    private var base64ProofPreviewOfficeRoomPhotograph: String? = null
//    private var base64ProofOfficeCumTypeofRoofItLab: String? = null
//    private var base64ProofOfficeCumFalseCellingProvide: String? = null
//    private var base64ProofOfficeCumHeightOfCelling: String? = null
//    private var base64ProofOfficeCumSplaceforSecuringDoc: String? = null
//    private var base64ProofOCCROfficeTable: String? = null
//    private var base64ProofOfficeCumAnOfficeTableNo: String? = null
//    private var base64ProofOfficeCumChairs: String? = null
//    private var base64ProofOfficeCumTableOfofficeCumpter: String? = null
//    private var base64ProofOfficeCumPrinterCumScannerInNo: String? = null
//    private var base64ProofOfficeCumDigitalCameraInNo: String? = null
//    private var base64ProofOfficeCumElectricialPowerBackup: String? = null
//
//    // Reception Area
//    private var base64ProofPreviewReceptionAreaPhotogragh: String? = null
//
//    // Counselling Room
//    private var base64ProofPreviewCounsellingRoomPhotogragh: String? = ""
//
//    // Office Room
//    private var base64ProofPreviewOROfficeRoomORPhotograph: String? = null
//    private var base64ProofORTypeofRoofItLab: String? = null
//    private var base64ProofORFalseCellingProvide: String? = null
//    private var base64ProofORHeightOfCelling: String? = null
//    private var base64ProofORSplaceforSecuringDoc: String? = null
//    private var base64ProofORAnOfficeTableNo: String? = null
//    private var base64ProofORChairs: String? = null
//    private var base64ProofORTableOfofficeCumpter: String? = null
//    private var base64ProofORPrinterCumScannerInNo: String? = null
//    private var base64ProofORDigitalCameraInNo: String? = null
//    private var base64ProofORElectricialPowerBackup: String? = null
//
//    // IT Come Domain Lab
//    private var base64ProofPreviewITCDLTypeofRoofItLab: String? = null
//    private var base64ProofITCDLFalseCellingProvide: String? = null
//    private var base64ProofITCDLabHeightOfCelling: String? = null
//    private var base64ProofITCDLVentilationAreaInSqFt: String? = null
//    private var base64ProofITCDLabSoundLevelInDb: String? = null
//    private var base64ProofITCDLwhether_all_the_academic: String? = null
//    private var base64ProofITCDLAcademicRoomInformationBoard: String? = null
//    private var base64ProofITCDLInternalSignage: String? = null
//    private var base64ProofITCDLCctcCamerasWithAudioFacility: String? = null
//    private var base64ProofITCDLLanEnabledComputersInNo: String? = null
//    private var base64ProofITCDLInternetConnections: String? = null
//    private var base64ProofITCDLDoAllComputersHaveTypingTutor: String? = null
//    private var base64ProofITCDLTablets: String? = null
//    private var base64ProofITCDLStoolsChairs: String? = null
//    private var base64ProofITCDLTrainerChair: String? = null
//    private var base64ProofITCDLLightsInNo: String? = null
//    private var base64ProofITCDLTrainerTable: String? = null
//    private var base64ProofITCDLFansInNo: String? = null
//    private var base64ProofITCDLElectricaPowerBackUpForThRoom: String? = null
//    private var base64ProofITCDLItLabPhotograph: String? = null
//    private var base64ProofITCDLListofDomain: String? = null
//    private var base64ProofITCDLDoes_the_room_has: String? = null
//
//    // Theory Cum IT Lab
//    private var base64ProofPreviewTCILListofDomain: String? = null
//    private var base64ProofPreviewTCILTypeofRoofItLab: String? = null
//    private var base64ProofPreviewTCILFalseCellingProvide: String? = null
//    private var base64ProofPreviewTCILHeightOfCelling: String? = null
//    private var base64ProofPreviewTCILVentilationAreaInSqFt: String? = null
//    private var base64ProofPreviewTTCILSoundLevelInDb: String? = null
//    private var base64ProofPreviewTCILwhether_all_the_academic: String? = null
//    private var base64ProofPreviewTCILAcademicRoomInformationBoard: String? = null
//    private var base64ProofPreviewTCILInternalSignage: String? = null
//    private var base64ProofPreviewTCILCctcCamerasWithAudioFacility: String? = null
//    private var base64ProofPreviewTCILLanEnabledComputersInNo: String? = null
//    private var base64ProofPreviewTCILInternetConnections: String? = null
//    private var base64ProofPreviewTCILDoAllComputersHaveTypingTutor: String? = null
//    private var base64ProofPreviewTCILTablets: String? = null
//    private var base64ProofPreviewTCILStoolsChairs: String? = null
//    private var base64ProofPreviewTCILTrainerTable: String? = null
//    private var base64ProofPreviewTCILTrainerChair: String? = null
//    private var base64ProofPreviewTCILLightsInNo: String? = null
//    private var base64ProofPreviewTCILFansInNo: String? = null
//    private var base64ProofPreviewTCILElectricaPowerBackUpForThRoom: String? = null
//    private var base64ProofPreviewTCILTheoryCumItLabPhotogragh: String? = null
//    private var base64ProofPreviewTCILDoes_the_room_has: String? = null
//
//    // Theory Cum Domain Lab
//    private var base64ProofPreviewTCDLTypeofRoofItLab: String? = null
//    private var base64ProofPreviewTCDLFalseCellingProvide: String? = null
//    private var base64ProofPreviewTCDLHeightOfCelling: String? = null
//    private var base64ProofPreviewTCDLVentilationAreaInSqFt: String? = null
//    private var base64ProofPreviewTCDLSoundLevelInDb: String? = null
//    private var base64ProofPreviewTCDLwhether_all_the_academic: String? = null
//    private var base64ProofPreviewTCDLAcademicRoomInformationBoard: String? = null
//    private var base64ProofPreviewTCDLInternalSignage: String? = null
//    private var base64ProofPreviewTCDLCctcCamerasWithAudioFacility: String? = null
//    private var base64ProofPreviewTCDLLcdDigitalProjector: String? = null
//    private var base64ProofPreviewTCDLChairForCandidatesInNo: String? = null
//    private var base64ProofPreviewTCDLTrainerChair: String? = null
//    private var base64ProofPreviewTCDLTrainerTable: String? = null
//    private var base64ProofPreviewTCDLWritingBoard: String? = null
//    private var base64ProofPreviewTCDLLightsInNo: String? = null
//    private var base64ProofPreviewTCDLFansInNo: String? = null
//    private var base64ProofPreviewTCDLElectricaPowerBackUpForThRoom: String? = null
//    private var base64ProofPreviewTCDLListofDomain: String? = null
//    private var base64ProofPreviewTCDLDomainLabPhotogragh: String? = null
//    private var base64ProofPreviewTCDLDoes_the_room_has: String? = null
//
//    // Domain Lab
//    private var base64ProofPreviewDLTypeofRoofItLab: String? = null
//    private var base64ProofPreviewDLFalseCellingProvide: String? = null
//    private var base64ProofPreviewDLHeightOfCelling: String? = null
//    private var base64ProofPreviewDLVentilationAreaInSqFt: String? = null
//    private var base64ProofPreviewDLSoundLevelInDb: String? = null
//    private var base64ProofPreviewDLwhether_all_the_academic: String? = null
//    private var base64ProofPreviewDLAcademicRoomInformationBoard: String? = null
//    private var base64ProofPreviewDLInternalSignage: String? = null
//    private var base64ProofPreviewDLCctcCamerasWithAudioFacility: String? = null
//    private var base64ProofPreviewDLLcdDigitalProjector: String? = null
//    private var base64ProofPreviewDLChairForCandidatesInNo: String? = null
//    private var base64ProofPreviewDLTrainerChair: String? = null
//    private var base64ProofPreviewDLTrainerTable: String? = null
//    private var base64ProofPreviewDLWritingBoard: String? = null
//    private var base64ProofPreviewDLLightsInNo: String? = null
//    private var base64ProofPreviewDLFansInNo: String? = null
//    private var base64ProofPreviewDLDomainLabPhotogragh: String? = null
//    private var base64ProofPreviewDLElectricaPowerBackUpForThRoom: String? = null
//    private var base64ProofPreviewDLILListofDomain: String? = null
//    private var base64ProofPreviewDLDoes_the_room_has: String? = null
//
//    // Theory Class Room
//    private var base64ProofPreviewTCRTypeofRoofItLab: String? = null
//    private var base64ProofPreviewTCRFalseCellingProvide: String? = null
//    private var base64ProofPreviewTCRHeightOfCelling: String? = null
//    private var base64ProofPreviewTCRVentilationAreaInSqFt: String? = null
//    private var base64ProofPreviewTCRSoundLevelInDb: String? = null
//    private var base64ProofPreviewTCRwhether_all_the_academic: String? = null
//    private var base64ProofPreviewTCRAcademicRoomInformationBoard: String? = null
//    private var base64ProofPreviewTCRInternalSignage: String? = null
//    private var base64ProofPreviewTCRCctcCamerasWithAudioFacility: String? = null
//    private var base64ProofPreviewTCRLcdDigitalProjector: String? = null
//    private var base64ProofPreviewTCRChairForCandidatesInNo: String? = null
//    private var base64ProofPreviewTCRTrainerChair: String? = null
//    private var base64ProofPreviewTCRTrainerTable: String? = null
//    private var base64ProofPreviewTCRWritingBoard: String? = null
//    private var base64ProofPreviewTCRLightsInNo: String? = null
//    private var base64ProofPreviewTCRFansInNo: String? = null
//    private var base64ProofPreviewTCRElectricaPowerBackUpForThRoom: String? = null
//    private var base64ProofPreviewTCRDomainLabPhotogragh: String? = null
//    private var base64ProofPreviewTCRDoes_the_room_has: String? = null
//
//    private val photoUploadButtons: Map<Int, String> = mapOf(
//        // ITLAB
//        R.id.btnITLTypeofRoofItLab to "itltypeofroofitlab",
//        R.id.btnITLFalseCellingProvide to "itlfalsecellingprovide",
//        R.id.btnITLHeightOfCelling to "itlheightofcelling",
//        R.id.btnITLVentilationAreaInSqFt to "itlventilationareainsqft",
//        R.id.btnITLSoundLevelAsPerSpecifications to "itlsoundlevelasperspecifications",
//        R.id.btnITLSoundLevelInDb to "itlsoundlevelindb",
//        R.id.btnITLwhether_all_the_academic to "itlwhether_all_the_academic",
//        R.id.btnITLAcademicRoomInformationBoard to "itlacadmicroominformationboard",
//        R.id.btnITLInternalSignage to "itlinternalsignage",
//        R.id.btnITLCctcCamerasWithAudioFacility to "itlcctccameraswithaudiofacility",
//        R.id.btnITLLanEnabledComputersInNo to "itllanenabledcomputersinno",
//        R.id.btnITLInternetConnections to "itlinternetconnections",
//        R.id.btnITLDoAllComputersHaveTypingTutor to "itldoallcomputershavetypingtutor",
//        R.id.btnITLTablets to "itltablets",
//        R.id.btnITLStoolsChairs to "itlstoolschairs",
//        R.id.btnITLTrainerChair to "itltrainerchair",
//        R.id.btnITLTrainerTable to "itltrainertable",
//        R.id.btnITLLightsInNo to "itllightsinno",
//        R.id.btnITLFansInNo to "itlfansinno",
//        R.id.btnITLElectricaPowerBackUpForThRoom to "itlelectricapowerbackupforthroom",
//        R.id.btnITLItLabPhotograph to "itlitlabphotograph",
//        R.id.btnITLLDoes_the_room_has to "itlldoes_the_room_has",
//
//        // Office Cum(Counselling room)
//        R.id.btnUploadOfficeRoomPhotograph to "btnuploadofficeroomphotograph",
//        R.id.btnUploadOfficeCumTypeofRoofItLab to "btnuploadofficecumtypeofroofitlab",
//        R.id.btnOfficeCumFalseCellingProvide to "btnofficecumfalsecellingprovide",
//        R.id.btnOfficeCumHeightOfCelling to "btnofficecumheightofcelling",
//        R.id.btnOfficeCumSplaceforSecuringDoc to "btnofficecumsplaceforsecuringdoc",
//        R.id.btnUploadOCCROfficeTable to "btnUploadOCCROfficeTable",
//        R.id.btnOfficeCumAnOfficeTableNo to "btnofficecumanofficetableno",
//        R.id.btnOfficeCumChairs to "btnofficecumchairs",
//        R.id.btnOfficeCumTableOfofficeCumpter to "btnofficecumtableofofficecumpter",
//        R.id.btnOfficeCumPrinterCumScannerInNo to "btnofficecumprintercumscannerinno",
//        R.id.btnOfficeCumDigitalCameraInNo to "btnofficecumdigitalcamerainno",
//        R.id.btnOfficeCumElectricialPowerBackup to "btnofficecumelectricialpowerbackup",
//
//        // Reception Area
//        R.id.btnReceptionAreaPhotogragh to "btnReceptionAreaPhotogragh",
//        R.id.btnCounsellingRoomAreaPhotograph to "btnCounsellingRoomAreaPhotograph",
//
//        // Office Room
//        R.id.btnOROfficeRoomPhotograph to "btnOROfficeRoomPhotograph",
//        R.id.btnORTypeofRoofItLab to "btnORTypeofRoofItLab",
//        R.id.btnORFalseCellingProvide to "btnORFalseCellingProvide",
//        R.id.btnORHeightOfCelling to "btnORHeightOfCelling",
//        R.id.btnORSplaceforSecuringDoc to "btnORSplaceforSecuringDoc",
//        R.id.btnORAnOfficeTableNo to "btnORAnOfficeTableNo",
//        R.id.btnORChairs to "btnORChairs",
//        R.id.btnORTableOfofficeCumpter to "btnORTableOfofficeCumpter",
//        R.id.btnORPrinterCumScannerInNo to "btnORPrinterCumScannerInNo",
//        R.id.btnORDigitalCameraInNo to "btnORDigitalCameraInNo",
//        R.id.btnORElectricialPowerBackup to "btnORElectricialPowerBackup",
//
//        // IT Come Domain Lab
//        R.id.btnITCDLTypeofRoofItLab to "btnITCDLTypeofRoofItLab",
//        R.id.btnITCDLFalseCellingProvide to "btnITCDLFalseCellingProvide",
//        R.id.btnITCDLHeightOfCelling to "btnITCDLHeightOfCelling",
//        R.id.btnITCDLVentilationAreaInSqFt to "btnITCDLVentilationAreaInSqFt",
//        R.id.btnITCDLabSoundLevelInDb to "btnITCDLabSoundLevelInDb",
//        R.id.btnITDLwhether_all_the_academic to "btnITDLwhether_all_the_academic",
//        R.id.btnITCDLAcademicRoomInformationBoard to "btnITCDLAcademicRoomInformationBoard",
//        R.id.btnITCDLInternalSignage to "btnITCDLInternalSignage",
//        R.id.btnITCDLCctcCamerasWithAudioFacility to "btnITCDLCctcCamerasWithAudioFacility",
//        R.id.btnITCDLLanEnabledComputersInNo to "btnITCDLLanEnabledComputersInNo",
//        R.id.btnITCDLInternetConnections to "btnITCDLInternetConnections",
//        R.id.btnITCDLTrainerChair to "btnITCDLTrainerChair",
//        R.id.btnITCDLTablets to "btnITCDLTablets",
//        R.id.btnITCDLTrainerTable to "btnITCDLTrainerTable",
//        R.id.btnITCDLLightsInNo to "btnITCDLLightsInNo",
//        R.id.btnITCDLFansInNo to "btnITCDLFansInNo",
//        R.id.btnITCDLElectricaPowerBackUpForThRoom to "btnITCDLElectricaPowerBackUpForThRoom",
//        R.id.btnITCDLItLabPhotograph to "btnITCDLItLabPhotograph",
//        R.id.btnITCDLListofDomain to "btnITCDLListofDomain",
//        R.id.btnITCDLDoes_the_room_has to "btnITCDLDoes_the_room_has",
//        R.id.btnITCDLDoAllComputersHaveTypingTutor to "btnITCDLDoAllComputersHaveTypingTutor",
//        R.id.btnITCDLStoolsChairs to "btnITCDLStoolsChairs",
//
//        // Theory Cum IT Lab
//        R.id.btnTCILListofDomain to "btnTCILListofDomain",
//        R.id.btnTCILTypeofRoofItLab to "btnTCILTypeofRoofItLab",
//        R.id.btnTCILFalseCellingProvide to "btnTCILFalseCellingProvide",
//        R.id.btnTCILHeightOfCelling to "btnTCILHeightOfCelling",
//        R.id.btnTCILVentilationAreaInSqFt to "btnTCILVentilationAreaInSqFt",
//        R.id.btnTCILSoundLevelAsPerSpecifications to "btnTCILSoundLevelAsPerSpecifications",
//        R.id.btnTCILSoundLevelInDb to "btnTCILSoundLevelInDb",
//        R.id.btnTCILwhether_all_the_academic to "btnTCILwhether_all_the_academic",
//        R.id.btnTCILAcademicRoomInformationBoard to "btnTCILAcademicRoomInformationBoard",
//        R.id.btnTCILInternalSignage to "btnTCILInternalSignage",
//        R.id.btnTCILCctcCamerasWithAudioFacility to "btnTCILCctcCamerasWithAudioFacility",
//        R.id.btnTCILLanEnabledComputersInNo to "btnTCILLanEnabledComputersInNo",
//        R.id.btnTCILInternetConnections to "btnTCILInternetConnections",
//        R.id.btnTCILDoAllComputersHaveTypingTutor to "btnTCILDoAllComputersHaveTypingTutor",
//        R.id.btnTCILTablets to "btnTCILTablets",
//        R.id.btnTCILStoolsChairs to "btnTCILStoolsChairs",
//        R.id.btnTCILTrainerChair to "btnTCILTrainerChair",
//        R.id.btnTCILTrainerTable to "btnTCILTrainerTable",
//        R.id.btnTCILLightsInNo to "btnTCILLightsInNo",
//        R.id.btnTCILFansInNo to "btnTCILFansinno",
//        R.id.btnTCILElectricaPowerBackUpForThRoom to "btnTCILElectricaPowerBackUpForThRoom",
//        R.id.btnTCILTheoryCumItLabPhotogragh to "btnTCILTheoryCumItLabPhotogragh",
//        R.id.btnTCILDoes_the_room_has to "btnTCILDoes_the_room_has",
//
//        // Theory Cum Domain Lab
//        R.id.btnTCDLTypeofRoofItLab to "btnTCDLTypeofRoofItLab",
//        R.id.btnTCDLFalseCellingProvide to "btnTCDLFalseCellingProvide",
//        R.id.btnTCDLHeightOfCelling to "btnTCDLHeightOfCelling",
//        R.id.btnTCDLVentilationAreaInSqFt to "btnTCDLVentilationAreaInSqFt",
//        R.id.btnTCDLSoundLevelInDb to "btnTCDLSoundLevelInDb",
//        R.id.btnTCDLwhether_all_the_academic to "btnTCDLwhether_all_the_academic",
//        R.id.btnTCDLAcademicRoomInformationBoard to "btnTCDLAcademicRoomInformationBoard",
//        R.id.btnTCDLInternalSignage to "btnTCDLInternalSignage",
//        R.id.btnTCDLCctcCamerasWithAudioFacility to "btnTCDLCctcCamerasWithAudioFacility",
//        R.id.btnTCDLLcdDigitalProjector to "btnTCDLLcdDigitalProjector",
//        R.id.btnTCDLChairForCandidatesInNo to "btnTCDLChairForCandidatesInNo",
//        R.id.btnTCDLUploaadTrainerChair to "btnTCDLUploaadTrainerChair",
//        R.id.btnTCDLTrainerTable to "btnTCDLTrainerTable",
//        R.id.btnTCDLWritingBoard to "btnTCDLWritingBoard",
//        R.id.btnTCDLLightsInNo to "btnTCDLLightsInNo",
//        R.id.btnTCDLFansInNo to "btnTCDLFansInNo",
//        R.id.btnTCDLElectricaPowerBackUpForThRoom to "btnTCDLElectricaPowerBackUpForThRoom",
//        R.id.btnTCDLListofDomain to "btnTCDLListofDomain",
//        R.id.btnTCDLDomainLabPhotogragh to "btnTCDLDomainLabPhotogragh",
//        R.id.btnTCDLDoes_the_room_has to "btnTCDLDoes_the_room_has",
//
//        // Domain Lab
//        R.id.btnDLTypeofRoofItLab to "btnDLTypeofRoofItLab",
//        R.id.btnDLFalseCellingProvide to "btnDLFalseCellingProvide",
//        R.id.btnDLHeightOfCelling to "btnDLHeightOfCelling",
//        R.id.btnDLVentilationAreaInSqFt to "btnDLVentilationAreaInSqFt",
//        R.id.btnDLSoundLevelAsPerSpecifications to "btnDLSoundLevelAsPerSpecifications",
//        R.id.btnDLSoundLevelInDb to "btnDLSoundLevelInDb",
//        R.id.btnDLwhether_all_the_academic to "btnDLwhether_all_the_academic",
//        R.id.btnDLAcademicRoomInformationBoard to "btnDLAcademicRoomInformationBoard",
//        R.id.btnDLInternalSignage to "btnDLInternalSignage",
//        R.id.btnDLCctcCamerasWithAudioFacility to "btnDLCctcCamerasWithAudioFacility",
//        R.id.btnDLLcdDigitalProjector to "btnDLLcdDigitalProjector",
//        R.id.btnDLChairForCandidatesInNo to "btnDLChairForCandidatesInNo",
//        R.id.btnDLUploaadTrainerChair to "btnDLUploaadTrainerChair",
//        R.id.btnDLTrainerTable to "btnDLTrainerTable",
//        R.id.btnDLWritingBoard to "btnDLWritingBoard",
//        R.id.btnDLLightsInNo to "btnDLLightsInNo",
//        R.id.btnDLFansInNo to "btnDLFansInNo",
//        R.id.btnDLElectricaPowerBackUpForThRoom to "btnDLElectricaPowerBackUpForThRoom",
//        R.id.btnDLILListofDomain to "btnDLILListofDomain",
//        R.id.btnDLDomainLabPhotogragh to "btnDLDomainLabPhotogragh",
//        R.id.btnDLDoes_the_room_has to "btnDLDoes_the_room_has",
//
//        // Theory Class Room
//        R.id.btnTCRTypeofRoofItLab to "btnTCRTypeofRoofItLab",
//        R.id.btnTCRFalseCellingProvide to "btnTCRFalseCellingProvide",
//        R.id.btnTCRHeightOfCelling to "btnTCRHeightOfCelling",
//        R.id.btnTCRVentilationAreaInSqFt to "btnTCRVentilationAreaInSqFt",
//        R.id.btnTCRSoundLevelInDb to "btnTCRSoundLevelInDb",
//        R.id.btnTCRwhether_all_the_academic to "btnTCRwhether_all_the_academic",
//        R.id.btnTCRAcademicRoomInformationBoard to "btnTCRAcademicRoomInformationBoard",
//        R.id.btnTCRCctcCamerasWithAudioFacility to "btnTCRCctcCamerasWithAudioFacility",
//        R.id.btnTCRLcdDigitalProjector to "btnTCRLcdDigitalProjector",
//        R.id.btnTCRChairForCandidatesInNo to "btnTCRChairForCandidatesInNo",
//        R.id.btnTCRTrainerChair to "btnTCRTrainerChair",
//        R.id.btnTCRTrainerTable to "btnTCRTrainerTable",
//        R.id.btnTCRWritingBoard to "btnTCRWritingBoard",
//        R.id.btnTCRLightsInNo to "btnTCRLightsInNo",
//        R.id.btnTCRFansInNo to "btnTCRFansInNo",
//        R.id.btnTCRElectricaPowerBackUpForThRoom to "btnTCRElectricaPowerBackUpForThRoom",
//        R.id.btnTCRDomainLabPhotogragh to "btnTCRDomainLabPhotogragh",
//        R.id.btnTCRDoes_the_room_has to "btnTCRDoes_the_room_has",
//        R.id.btnTCRInternalSignage to "btnTCRInternalSignage",
//
//        // CCTV
//        R.id.btnUploadMonitorPhoto to "monitor",
//        R.id.btnUploadConformancePhoto to "conformance",
//        R.id.btnUploadStoragePhoto to "storage",
//        R.id.btnUploadDVRPhoto to "dvr",
//
//        // Electrical
//        R.id.btnUploadSwitchBoards to "switchBoard",
//        R.id.btnUploadSecuringWires to "WireSecurity",
//
//        // General
//        R.id.btnUploadLeaSkageProof to "leakage",
//        R.id.btnUploadProtectionStairs to "stairs",
//
//        // Signages info boards
//        R.id.btnUploadTrainingCentreNameBoard to "tcNameBoard",
//        R.id.btnUploadActivitySummaryBoard to "activityAchievementBoard",
//        R.id.btnUploadEntitlementBoard to "studentEntitlementBoard",
//        R.id.btnUploadImportantContacts to "contactDetailBoard",
//        R.id.btnUploadBasicInfoBoard to "basicInfoBoard",
//        R.id.btnUploadCodeOfConductBoard to "codeConductBoard",
//        R.id.btnUploadAttendanceSummaryBoard to "studentAttendanceBoard",
//
//        // Support infra
//        R.id.btnUploadFirstAidKit to "FirstAidKit",
//        R.id.btnUploadFireFightingEquipment to "FireFightingEquipment",
//        R.id.btnUploadSafeDrinkingWater to "SafeDrinkingWater",
//
//        // desc Other areas
//        R.id.btnUploadProof to "proofUpload",
//        R.id.btnUploadCirculationProof to "circulationProof",
//        R.id.btnUploadParkingProof to "parking",
//        R.id.btnUploadOpenSpaceProof to "openSpaceProof",
//
//        // Common Equipment
//        R.id.btnUploadPowerBackup to "powerBackup",
//        R.id.btnUploadBiometricDevices to "biometricDevices",
//        R.id.btnUploadCCTV to "cctv",
//        R.id.btnUploadDocumentStorage to "documentStorage",
//        R.id.btnUploadPrinterScanner to "printerScanner",
//        R.id.btnUploadDigitalCamera to "digitalCamera",
//        R.id.btnUploadGrievanceRegister to "grievanceRegister",
//        R.id.btnUploadMinimumEquipment to "minimumEquipment",
//        R.id.btnUploadDirectionBoards to "directionBoards",
//
//        // Wash basin upload buttons
//        R.id.btnUploadProofMaleToilets to "maleToiletsProof",
//        R.id.btnUploadProofMaleToiletsSignage to "maleToiletsSignageProof",
//        R.id.btnUploadProofFemaleToilets to "femaleToiletsProof",
//        R.id.btnUploadProofFemaleToiletsSignage to "femaleToiletsSignageProof",
//        R.id.btnUploadProofMaleUrinals to "maleUrinalsProof",
//        R.id.btnUploadProofMaleWashBasins to "maleWashBasinsProof",
//        R.id.btnUploadProofFemaleWashBasins to "femaleWashBasinsProof",
//        R.id.btnUploadProofOverheadTanks to "overheadTanksProof",
//        R.id.btnUploadProofFlooring to "flooringProof",
//    )
//
//    // Permission request launcher
//    private val requestPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
//            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
//
//            if (fineLocationGranted || coarseLocationGranted) {
//                getCurrentLocation()
//            } else {
//                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        cameraLauncher =
//            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
//                if (success) {
//                    Log.d("Camera", "Captured image URI: $photoUri")
//                    when (currentPhotoTarget) {
//                        "monitor" -> {
//                            binding.ivMonitorPreview.setImageURI(photoUri)
//                            binding.ivMonitorPreview.visibility = View.VISIBLE
//                            base64MonitorFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "conformance" -> {
//                            binding.ivConformancePreview.setImageURI(photoUri)
//                            binding.ivConformancePreview.visibility = View.VISIBLE
//                            base64ConformanceFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "storage" -> {
//                            binding.ivStoragePreview.setImageURI(photoUri)
//                            binding.ivStoragePreview.visibility = View.VISIBLE
//                            base64StorageFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "dvr" -> {
//                            binding.ivDVRPreview.setImageURI(photoUri)
//                            binding.ivDVRPreview.visibility = View.VISIBLE
//                            base64DVRFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "switchBoard" -> {
//                            binding.ivSwitchBoardPreview.setImageURI(photoUri)
//                            binding.ivSwitchBoardPreview.visibility = View.VISIBLE
//                            base64SwitchBoardImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "WireSecurity" -> {
//                            binding.ivWireSecurityPreview.setImageURI(photoUri)
//                            binding.ivWireSecurityPreview.visibility = View.VISIBLE
//                            base64WireSecurityImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "leakage" -> {
//                            binding.ivLeakagePreview.setImageURI(photoUri)
//                            binding.ivLeakagePreview.visibility = View.VISIBLE
//                            base64LeakageImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "stairs" -> {
//                            binding.ivStairsPreview.setImageURI(photoUri)
//                            binding.ivStairsPreview.visibility = View.VISIBLE
//                            base64StairsImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "tcNameBoard" -> {
//                            binding.ivTcNameBoardPreview.setImageURI(photoUri)
//                            binding.ivTcNameBoardPreview.visibility = View.VISIBLE
//                            base64TcNameBoardImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "activityAchievementBoard" -> {
//                            binding.ivActivityAchievementBoardPreview.setImageURI(photoUri)
//                            binding.ivActivityAchievementBoardPreview.visibility = View.VISIBLE
//                            base64ActivityAchievementBoardImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "studentEntitlementBoard" -> {
//                            binding.ivStudentEntitlementBoardPreview.setImageURI(photoUri)
//                            binding.ivStudentEntitlementBoardPreview.visibility = View.VISIBLE
//                            base64StudentEntitlementBoardImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "contactDetailBoard" -> {
//                            binding.ivContactDetailBoardPreview.setImageURI(photoUri)
//                            binding.ivContactDetailBoardPreview.visibility = View.VISIBLE
//                            base64ContactDetailBoardImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "basicInfoBoard" -> {
//                            binding.ivBasicInfoBoardPreview.setImageURI(photoUri)
//                            binding.ivBasicInfoBoardPreview.visibility = View.VISIBLE
//                            base64BasicInfoBoardImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "codeConductBoard" -> {
//                            binding.ivCodeConductBoardPreview.setImageURI(photoUri)
//                            binding.ivCodeConductBoardPreview.visibility = View.VISIBLE
//                            base64CodeConductBoardImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "studentAttendanceBoard" -> {
//                            binding.ivStudentAttendanceBoardPreview.setImageURI(photoUri)
//                            binding.ivStudentAttendanceBoardPreview.visibility = View.VISIBLE
//                            base64StudentAttendanceBoardImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "SafeDrinkingWater" -> {
//                            binding.ivSafeDrinkingWaterPreview.setImageURI(photoUri)
//                            binding.ivSafeDrinkingWaterPreview.visibility = View.VISIBLE
//                            base64SafeDrinkingWater = AppUtil.imageUriToBase64(context = requireContext(), photoUri)
//                        }
//                        "FireFightingEquipment" -> {
//                            binding.ivFireFightingEquipmentPreview.setImageURI(photoUri)
//                            binding.ivFireFightingEquipmentPreview.visibility = View.VISIBLE
//                            base64FireFightingEquipment = AppUtil.imageUriToBase64(context = requireContext(), photoUri)
//                        }
//                        "FirstAidKit" -> {
//                            binding.ivFirstAidKitPreview.setImageURI(photoUri)
//                            binding.ivFirstAidKitPreview.visibility = View.VISIBLE
//                            base64FirstAidKit = AppUtil.imageUriToBase64(context = requireContext(), photoUri)
//                        }
//                        "powerBackup" -> {
//                            binding.ivPowerBackupPreview.setImageURI(photoUri)
//                            binding.ivPowerBackupPreview.visibility = View.VISIBLE
//                            base64PowerBackupImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "biometricDevices" -> {
//                            binding.ivBiometricDevicesPreview.setImageURI(photoUri)
//                            binding.ivBiometricDevicesPreview.visibility = View.VISIBLE
//                            base64BiometricDevices = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "cctv" -> {
//                            binding.ivCCTVPreview.setImageURI(photoUri)
//                            binding.ivCCTVPreview.visibility = View.VISIBLE
//                            base64CCTVImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "documentStorage" -> {
//                            binding.ivDocumentStoragePreview.setImageURI(photoUri)
//                            binding.ivDocumentStoragePreview.visibility = View.VISIBLE
//                            base64DocumentStorageImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "printerScanner" -> {
//                            binding.ivPrinterScannerPreview.setImageURI(photoUri)
//                            binding.ivPrinterScannerPreview.visibility = View.VISIBLE
//                            base64PrinterScanner = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "digitalCamera" -> {
//                            binding.ivDigitalCameraPreview.setImageURI(photoUri)
//                            binding.ivDigitalCameraPreview.visibility = View.VISIBLE
//                            base64DigitalCamera = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "grievanceRegister" -> {
//                            binding.ivGrievanceRegisterPreview.setImageURI(photoUri)
//                            binding.ivGrievanceRegisterPreview.visibility = View.VISIBLE
//                            base64GrievanceRegisterImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "minimumEquipment" -> {
//                            binding.ivMinimumEquipmentPreview.setImageURI(photoUri)
//                            binding.ivMinimumEquipmentPreview.visibility = View.VISIBLE
//                            base64MinimumEquipmentImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "directionBoards" -> {
//                            binding.ivDirectionBoardsPreview.setImageURI(photoUri)
//                            binding.ivDirectionBoardsPreview.visibility = View.VISIBLE
//                            base64DirectionBoardsImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "maleToiletsProof" -> {
//                            try {
//                                binding.ivPreviewProofMaleToilets.setImageURI(photoUri)
//                                binding.ivPreviewProofMaleToilets.visibility = View.VISIBLE
//                                base64ProofMaleToilets = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                            } catch (e: Exception) {
//                                Log.e("ImagePreview", "Error in maleToiletsProof", e)
//                            }
//                        }
//                        "maleToiletsSignageProof" -> {
//                            binding.ivPreviewProofMaleToiletsSignage.setImageURI(photoUri)
//                            binding.ivPreviewProofMaleToiletsSignage.visibility = View.VISIBLE
//                            base64ProofMaleToiletsSignage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "femaleToiletsProof" -> {
//                            binding.ivPreviewProofFemaleToilets.setImageURI(photoUri)
//                            binding.ivPreviewProofFemaleToilets.visibility = View.VISIBLE
//                            base64ProofFemaleToilets = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "femaleToiletsSignageProof" -> {
//                            binding.ivPreviewProofFemaleToiletsSignage.setImageURI(photoUri)
//                            binding.ivPreviewProofFemaleToiletsSignage.visibility = View.VISIBLE
//                            base64ProofFemaleToiletsSignage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "maleUrinalsProof" -> {
//                            binding.ivPreviewProofMaleUrinals.setImageURI(photoUri)
//                            binding.ivPreviewProofMaleUrinals.visibility = View.VISIBLE
//                            base64ProofMaleUrinals = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "maleWashBasinsProof" -> {
//                            binding.ivPreviewProofMaleWashBasins.setImageURI(photoUri)
//                            binding.ivPreviewProofMaleWashBasins.visibility = View.VISIBLE
//                            base64ProofMaleWashBasins = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "femaleWashBasinsProof" -> {
//                            AppUtil.hideKeyboard(requireContext(), requireView())
//                            binding.ivPreviewProofFemaleWashBasins.setImageURI(photoUri)
//                            binding.ivPreviewProofFemaleWashBasins.visibility = View.VISIBLE
//                            base64ProofFemaleWashBasins = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "overheadTanksProof" -> {
//                            binding.ivPreviewProofOverheadTanks.setImageURI(photoUri)
//                            binding.ivPreviewProofOverheadTanks.visibility = View.VISIBLE
//                            base64ProofOverheadTanks = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "flooringProof" -> {
//                            binding.ivPreviewProofFlooring.setImageURI(photoUri)
//                            binding.ivPreviewProofFlooring.visibility = View.VISIBLE
//                            base64ProofFlooring = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "proofUpload" -> {
//                            binding.ivProofPreview.setImageURI(photoUri)
//                            binding.ivProofPreview.visibility = View.VISIBLE
//                            base64ProofUploadImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "circulationProof" -> {
//                            binding.ivCirculationProofPreview.setImageURI(photoUri)
//                            binding.ivCirculationProofPreview.visibility = View.VISIBLE
//                            base64CirculationProofImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "openSpaceProof" -> {
//                            binding.ivOpenSpaceProofPreview.setImageURI(photoUri)
//                            binding.ivOpenSpaceProofPreview.visibility = View.VISIBLE
//                            base64penSpaceProofImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "parking" -> {
//                            binding.ivParkingProofPreview.setImageURI(photoUri)
//                            binding.ivParkingProofPreview.visibility = View.VISIBLE
//                            base64ParkingSpaceProofImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        // IT LAB
//                        "itltypeofroofitlab" -> {
//                            binding.ivPreviewITLTypeofRoofItLab.setImageURI(photoUri)
//                            binding.ivPreviewITLTypeofRoofItLab.visibility = View.VISIBLE
//                            base64ProofPreviewITLTypeofRoofItLab = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlfalsecellingprovide" -> {
//                            binding.ivPreviewITLFalseCellingProvide.setImageURI(photoUri)
//                            binding.ivPreviewITLFalseCellingProvide.visibility = View.VISIBLE
//                            base64ProofITLFalseCellingProvide = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlheightofcelling" -> {
//                            binding.ivPreviewITLHeightOfCelling.setImageURI(photoUri)
//                            binding.ivPreviewITLHeightOfCelling.visibility = View.VISIBLE
//                            base64ProofITLHeightOfCelling = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlventilationareainsqft" -> {
//                            binding.ivPreviewITLVentilationAreaInSqFt.setImageURI(photoUri)
//                            binding.ivPreviewITLVentilationAreaInSqFt.visibility = View.VISIBLE
//                            base64ProofITLVentilationAreaInSqFt = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlsoundlevelasperspecifications" -> {
//                            binding.ivPreviewITLSoundLevelAsPerSpecifications.setImageURI(photoUri)
//                            binding.ivPreviewITLSoundLevelAsPerSpecifications.visibility = View.VISIBLE
//                            base64ProofITLSoundLevelAsPerSpecifications = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlsoundlevelindb" -> {
//                            binding.ivPreviewITLSoundLevelInDb.setImageURI(photoUri)
//                            binding.ivPreviewITLSoundLevelInDb.visibility = View.VISIBLE
//                            base64ProofITLSoundLevelInDb = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlwhether_all_the_academic" -> {
//                            binding.ivPreviewITLwhether_all_the_academic.setImageURI(photoUri)
//                            binding.ivPreviewITLwhether_all_the_academic.visibility = View.VISIBLE
//                            base64ProofITLwhether_all_the_academic = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlacadmicroominformationboard" -> {
//                            binding.ivPreviewITLAcademicRoomInformationBoard.setImageURI(photoUri)
//                            binding.ivPreviewITLAcademicRoomInformationBoard.visibility = View.VISIBLE
//                            base64ProofITLAcademicRoomInformationBoard = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlinternalsignage" -> {
//                            binding.ivPreviewITLInternalSignage.setImageURI(photoUri)
//                            binding.ivPreviewITLInternalSignage.visibility = View.VISIBLE
//                            base64ProofITLInternalSignage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlcctccameraswithaudiofacility" -> {
//                            binding.ivPreviewITLCctcCamerasWithAudioFacility.setImageURI(photoUri)
//                            binding.ivPreviewITLCctcCamerasWithAudioFacility.visibility = View.VISIBLE
//                            base64ProofITLCctcCamerasWithAudioFacility = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itllanenabledcomputersinno" -> {
//                            binding.ivPreviewITLLanEnabledComputersInNo.setImageURI(photoUri)
//                            binding.ivPreviewITLLanEnabledComputersInNo.visibility = View.VISIBLE
//                            base64ProofITLLanEnabledComputersInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlinternetconnections" -> {
//                            binding.ivPreviewITLInternetConnections.setImageURI(photoUri)
//                            binding.ivPreviewITLInternetConnections.visibility = View.VISIBLE
//                            base64ProofITLInternetConnections = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itldoallcomputershavetypingtutor" -> {
//                            binding.ivPreviewITLDoAllComputersHaveTypingTutor.setImageURI(photoUri)
//                            binding.ivPreviewITLDoAllComputersHaveTypingTutor.visibility = View.VISIBLE
//                            base64ProofITLDoAllComputersHaveTypingTutor = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itltablets" -> {
//                            binding.ivPreviewITLTablets.setImageURI(photoUri)
//                            binding.ivPreviewITLTablets.visibility = View.VISIBLE
//                            base64ProofITLTablets = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlstoolschairs" -> {
//                            binding.ivPreviewITLStoolsChairs.setImageURI(photoUri)
//                            binding.ivPreviewITLStoolsChairs.visibility = View.VISIBLE
//                            base64ProofITLStoolsChairs = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itltrainerchair" -> {
//                            binding.ivPreviewITLTrainerChair.setImageURI(photoUri)
//                            binding.ivPreviewITLTrainerChair.visibility = View.VISIBLE
//                            base64ProofITLTrainerChair = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itltrainertable" -> {
//                            binding.ivPreviewITLTrainerTable.setImageURI(photoUri)
//                            binding.ivPreviewITLTrainerTable.visibility = View.VISIBLE
//                            base64ProofITLTrainerTable = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itllightsinno" -> {
//                            binding.ivPreviewITLLightsInNo.setImageURI(photoUri)
//                            binding.ivPreviewITLLightsInNo.visibility = View.VISIBLE
//                            base64ProofITLLightsInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlfansinno" -> {
//                            binding.ivPreviewITLFansInNo.setImageURI(photoUri)
//                            binding.ivPreviewITLFansInNo.visibility = View.VISIBLE
//                            base64ProofITLFansInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlelectricapowerbackupforthroom" -> {
//                            binding.ivPreviewITLElectricaPowerBackUpForThRoom.setImageURI(photoUri)
//                            binding.ivPreviewITLElectricaPowerBackUpForThRoom.visibility = View.VISIBLE
//                            base64ProofITLElectricaPowerBackUpForThRoom = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlitlabphotograph" -> {
//                            binding.ivPreviewITLItLabPhotograph.setImageURI(photoUri)
//                            binding.ivPreviewITLItLabPhotograph.visibility = View.VISIBLE
//                            base64ProofITLItLabPhotograph = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "itlldoes_the_room_has" -> {
//                            binding.ivPreviewITLDoes_the_room_has.setImageURI(photoUri)
//                            binding.ivPreviewITLDoes_the_room_has.visibility = View.VISIBLE
//                            base64ProofITLDoes_the_room_has = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        // Office Cum(Counselling room)
//                        "btnuploadofficeroomphotograph" -> {
//                            binding.ivPreviewOfficeRoomPhotograph.setImageURI(photoUri)
//                            binding.ivPreviewOfficeRoomPhotograph.visibility = View.VISIBLE
//                            base64ProofPreviewOfficeRoomPhotograph = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnuploadofficecumtypeofroofitlab" -> {
//                            binding.ivPreviewOfficeCumTypeofRoofItLab.setImageURI(photoUri)
//                            binding.ivPreviewOfficeCumTypeofRoofItLab.visibility = View.VISIBLE
//                            base64ProofOfficeCumTypeofRoofItLab = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnofficecumfalsecellingprovide" -> {
//                            binding.ivPreviewOfficeCumFalseCellingProvide.setImageURI(photoUri)
//                            binding.ivPreviewOfficeCumFalseCellingProvide.visibility = View.VISIBLE
//                            base64ProofOfficeCumFalseCellingProvide = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnofficecumheightofcelling" -> {
//                            binding.ivPreviewOfficeCumHeightOfCelling.setImageURI(photoUri)
//                            binding.ivPreviewOfficeCumHeightOfCelling.visibility = View.VISIBLE
//                            base64ProofOfficeCumHeightOfCelling = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnofficecumsplaceforsecuringdoc" -> {
//                            binding.ivPreviewOfficeCumSplaceforSecuringDoc.setImageURI(photoUri)
//                            binding.ivPreviewOfficeCumSplaceforSecuringDoc.visibility = View.VISIBLE
//                            base64ProofOfficeCumSplaceforSecuringDoc = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnUploadOCCROfficeTable" -> {
//                            binding.ivPreviewOCCROfficeTable.setImageURI(photoUri)
//                            binding.ivPreviewOCCROfficeTable.visibility = View.VISIBLE
//                            base64ProofOCCROfficeTable = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnofficecumanofficetableno" -> {
//                            binding.ivPreviewOfficeCumAnOfficeTableNo.setImageURI(photoUri)
//                            binding.ivPreviewOfficeCumAnOfficeTableNo.visibility = View.VISIBLE
//                            base64ProofOfficeCumAnOfficeTableNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnofficecumchairs" -> {
//                            binding.ivPreviewOfficeCumChairs.setImageURI(photoUri)
//                            binding.ivPreviewOfficeCumChairs.visibility = View.VISIBLE
//                            base64ProofOfficeCumChairs = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnofficecumtableofofficecumpter" -> {
//                            binding.ivPreviewOfficeCumTableOfofficeCumpter.setImageURI(photoUri)
//                            binding.ivPreviewOfficeCumTableOfofficeCumpter.visibility = View.VISIBLE
//                            base64ProofOfficeCumTableOfofficeCumpter = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnofficecumprintercumscannerinno" -> {
//                            binding.ivPreviewOfficeCumPrinterCumScannerInNo.setImageURI(photoUri)
//                            binding.ivPreviewOfficeCumPrinterCumScannerInNo.visibility = View.VISIBLE
//                            base64ProofOfficeCumPrinterCumScannerInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnofficecumdigitalcamerainno" -> {
//                            binding.ivPreviewOfficeCumDigitalCameraInNo.setImageURI(photoUri)
//                            binding.ivPreviewOfficeCumDigitalCameraInNo.visibility = View.VISIBLE
//                            base64ProofOfficeCumDigitalCameraInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnofficecumelectricialpowerbackup" -> {
//                            binding.ivPreviewOfficeCumElectricialPowerBackup.setImageURI(photoUri)
//                            binding.ivPreviewOfficeCumElectricialPowerBackup.visibility = View.VISIBLE
//                            base64ProofOfficeCumElectricialPowerBackup = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        // ReceptionArea
//                        "btnReceptionAreaPhotogragh" -> {
//                            binding.ivPreviewReceptionAreaPhotogragh.setImageURI(photoUri)
//                            binding.ivPreviewReceptionAreaPhotogragh.visibility = View.VISIBLE
//                            base64ProofPreviewReceptionAreaPhotogragh = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnCounsellingRoomAreaPhotograph" -> {
//                            binding.ivPreviewCounsellingRoomAreaPhotograph.setImageURI(photoUri)
//                            binding.ivPreviewCounsellingRoomAreaPhotograph.visibility = View.VISIBLE
//                            base64ProofPreviewCounsellingRoomPhotogragh = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        // Office Room
//                        "btnOROfficeRoomPhotograph" -> {
//                            binding.ivPreviewOROfficeRoomPhotograph.setImageURI(photoUri)
//                            binding.ivPreviewOROfficeRoomPhotograph.visibility = View.VISIBLE
//                            base64ProofPreviewOROfficeRoomORPhotograph = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnORTypeofRoofItLab" -> {
//                            binding.ivPreviewORTypeofRoofItLab.setImageURI(photoUri)
//                            binding.ivPreviewORTypeofRoofItLab.visibility = View.VISIBLE
//                            base64ProofORTypeofRoofItLab = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnORFalseCellingProvide" -> {
//                            binding.ivPreviewORFalseCellingProvide.setImageURI(photoUri)
//                            binding.ivPreviewORFalseCellingProvide.visibility = View.VISIBLE
//                            base64ProofORFalseCellingProvide = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnORHeightOfCelling" -> {
//                            binding.ivPreviewORHeightOfCelling.setImageURI(photoUri)
//                            binding.ivPreviewORHeightOfCelling.visibility = View.VISIBLE
//                            base64ProofORHeightOfCelling = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnORSplaceforSecuringDoc" -> {
//                            binding.ivPreviewORSplaceforSecuringDoc.setImageURI(photoUri)
//                            binding.ivPreviewORSplaceforSecuringDoc.visibility = View.VISIBLE
//                            base64ProofORSplaceforSecuringDoc = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnORAnOfficeTableNo" -> {
//                            binding.ivPreviewORAnOfficeTableNo.setImageURI(photoUri)
//                            binding.ivPreviewORAnOfficeTableNo.visibility = View.VISIBLE
//                            base64ProofORAnOfficeTableNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnORChairs" -> {
//                            binding.ivPreviewORChairs.setImageURI(photoUri)
//                            binding.ivPreviewORChairs.visibility = View.VISIBLE
//                            base64ProofORChairs = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnORTableOfofficeCumpter" -> {
//                            binding.ivPreviewORTableOfofficeCumpter.setImageURI(photoUri)
//                            binding.ivPreviewORTableOfofficeCumpter.visibility = View.VISIBLE
//                            base64ProofORTableOfofficeCumpter = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnORPrinterCumScannerInNo" -> {
//                            binding.ivPreviewORPrinterCumScannerInNo.setImageURI(photoUri)
//                            binding.ivPreviewORPrinterCumScannerInNo.visibility = View.VISIBLE
//                            base64ProofORPrinterCumScannerInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnORDigitalCameraInNo" -> {
//                            binding.ivPreviewORDigitalCameraInNo.setImageURI(photoUri)
//                            binding.ivPreviewORDigitalCameraInNo.visibility = View.VISIBLE
//                            base64ProofORDigitalCameraInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnORElectricialPowerBackup" -> {
//                            binding.ivPreviewORElectricialPowerBackup.setImageURI(photoUri)
//                            binding.ivPreviewORElectricialPowerBackup.visibility = View.VISIBLE
//                            base64ProofORElectricialPowerBackup = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        // IT Come Domain Lab
//                        "btnITCDLTypeofRoofItLab" -> {
//                            binding.ivPreviewITCDLTypeofRoofItLab.setImageURI(photoUri)
//                            binding.ivPreviewITCDLTypeofRoofItLab.visibility = View.VISIBLE
//                            base64ProofPreviewITCDLTypeofRoofItLab = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLFalseCellingProvide" -> {
//                            binding.ivPreviewITCDLFalseCellingProvide.setImageURI(photoUri)
//                            binding.ivPreviewITCDLFalseCellingProvide.visibility = View.VISIBLE
//                            base64ProofITCDLFalseCellingProvide = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLHeightOfCelling" -> {
//                            binding.ivPreviewITCDLabHeightOfCelling.setImageURI(photoUri)
//                            binding.ivPreviewITCDLabHeightOfCelling.visibility = View.VISIBLE
//                            base64ProofITCDLabHeightOfCelling = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLVentilationAreaInSqFt" -> {
//                            binding.ivPreviewITCDLVentilationAreaInSqFt.setImageURI(photoUri)
//                            binding.ivPreviewITCDLVentilationAreaInSqFt.visibility = View.VISIBLE
//                            base64ProofITCDLVentilationAreaInSqFt = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLabSoundLevelInDb" -> {
//                            binding.ivPreviewITCDLabSoundLevelInDb.setImageURI(photoUri)
//                            binding.ivPreviewITCDLabSoundLevelInDb.visibility = View.VISIBLE
//                            base64ProofITCDLabSoundLevelInDb = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITDLwhether_all_the_academic" -> {
//                            binding.ivPreviewITCDLwhether_all_the_academic.setImageURI(photoUri)
//                            binding.ivPreviewITCDLwhether_all_the_academic.visibility = View.VISIBLE
//                            base64ProofITCDLwhether_all_the_academic = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLAcademicRoomInformationBoard" -> {
//                            binding.ivPreviewITCDLAcademicRoomInformationBoard.setImageURI(photoUri)
//                            binding.ivPreviewITCDLAcademicRoomInformationBoard.visibility = View.VISIBLE
//                            base64ProofITCDLAcademicRoomInformationBoard = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLInternalSignage" -> {
//                            binding.ivPreviewITCDLInternalSignage.setImageURI(photoUri)
//                            binding.ivPreviewITCDLInternalSignage.visibility = View.VISIBLE
//                            base64ProofITCDLInternalSignage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLCctcCamerasWithAudioFacility" -> {
//                            binding.ivPreviewITCDLCctcCamerasWithAudioFacility.setImageURI(photoUri)
//                            binding.ivPreviewITCDLCctcCamerasWithAudioFacility.visibility = View.VISIBLE
//                            base64ProofITCDLCctcCamerasWithAudioFacility = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLLanEnabledComputersInNo" -> {
//                            binding.ivPreviewITCDLLanEnabledComputersInNo.setImageURI(photoUri)
//                            binding.ivPreviewITCDLLanEnabledComputersInNo.visibility = View.VISIBLE
//                            base64ProofITCDLLanEnabledComputersInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLInternetConnections" -> {
//                            binding.ivPreviewITCDLInternetConnections.setImageURI(photoUri)
//                            binding.ivPreviewITCDLInternetConnections.visibility = View.VISIBLE
//                            base64ProofITCDLInternetConnections = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLTrainerChair" -> {
//                            binding.ivPreviewITCDLTrainerChair.setImageURI(photoUri)
//                            binding.ivPreviewITCDLTrainerChair.visibility = View.VISIBLE
//                            base64ProofITCDLTrainerChair = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLTrainerTable" -> {
//                            binding.ivPreviewITCDLTrainerTable.setImageURI(photoUri)
//                            binding.ivPreviewITCDLTrainerTable.visibility = View.VISIBLE
//                            base64ProofITCDLTrainerTable = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLLightsInNo" -> {
//                            binding.ivPreviewITCDLLightsInNo.setImageURI(photoUri)
//                            binding.ivPreviewITCDLLightsInNo.visibility = View.VISIBLE
//                            base64ProofITCDLLightsInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLFansInNo" -> {
//                            binding.ivPreviewITCDLFansInNo.setImageURI(photoUri)
//                            binding.ivPreviewITCDLFansInNo.visibility = View.VISIBLE
//                            base64ProofITCDLFansInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLElectricaPowerBackUpForThRoom" -> {
//                            binding.ivPreviewITCDLElectricaPowerBackUpForThRoom.setImageURI(photoUri)
//                            binding.ivPreviewITCDLElectricaPowerBackUpForThRoom.visibility = View.VISIBLE
//                            base64ProofITCDLElectricaPowerBackUpForThRoom = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLItLabPhotograph" -> {
//                            binding.ivPreviewITCDLItLabPhotograph.setImageURI(photoUri)
//                            binding.ivPreviewITCDLItLabPhotograph.visibility = View.VISIBLE
//                            base64ProofITCDLItLabPhotograph = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLListofDomain" -> {
//                            binding.ivPreviewITCDLListofDomain.setImageURI(photoUri)
//                            binding.ivPreviewITCDLListofDomain.visibility = View.VISIBLE
//                            base64ProofITCDLListofDomain = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLDoes_the_room_has" -> {
//                            binding.ivPreviewITCDLDoes_the_room_has.setImageURI(photoUri)
//                            binding.ivPreviewITCDLDoes_the_room_has.visibility = View.VISIBLE
//                            base64ProofITCDLDoes_the_room_has = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLTablets" -> {
//                            binding.ivPreviewITCDLTablets.setImageURI(photoUri)
//                            binding.ivPreviewITCDLTablets.visibility = View.VISIBLE
//                            base64ProofITCDLTablets = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLDoAllComputersHaveTypingTutor" -> {
//                            binding.ivPreviewITCDLDoAllComputersHaveTypingTutor.setImageURI(photoUri)
//                            binding.ivPreviewITCDLDoAllComputersHaveTypingTutor.visibility = View.VISIBLE
//                            base64ProofITCDLDoAllComputersHaveTypingTutor = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnITCDLStoolsChairs" -> {
//                            binding.ivPreviewITCDLStoolsChairs.setImageURI(photoUri)
//                            binding.ivPreviewITCDLStoolsChairs.visibility = View.VISIBLE
//                            base64ProofITCDLStoolsChairs = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        // Theory Cum IT Lab
//                        "btnTCILListofDomain" -> {
//                            binding.ivPreviewTCILListofDomain.setImageURI(photoUri)
//                            binding.ivPreviewTCILListofDomain.visibility = View.VISIBLE
//                            base64ProofPreviewTCILListofDomain = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILTypeofRoofItLab" -> {
//                            binding.ivPreviewTCILTypeofRoofItLab.setImageURI(photoUri)
//                            binding.ivPreviewTCILTypeofRoofItLab.visibility = View.VISIBLE
//                            base64ProofPreviewTCILTypeofRoofItLab = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILFalseCellingProvide" -> {
//                            binding.ivPreviewTCILFalseCellingProvide.setImageURI(photoUri)
//                            binding.ivPreviewTCILFalseCellingProvide.visibility = View.VISIBLE
//                            base64ProofPreviewTCILFalseCellingProvide = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILHeightOfCelling" -> {
//                            binding.ivPreviewTCILHeightOfCelling.setImageURI(photoUri)
//                            binding.ivPreviewTCILHeightOfCelling.visibility = View.VISIBLE
//                            base64ProofPreviewTCILHeightOfCelling = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILVentilationAreaInSqFt" -> {
//                            binding.ivPreviewTCILVentilationAreaInSqFt.setImageURI(photoUri)
//                            binding.ivPreviewTCILVentilationAreaInSqFt.visibility = View.VISIBLE
//                            base64ProofPreviewTCILVentilationAreaInSqFt = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILSoundLevelInDb" -> {
//                            binding.ivPreviewTCILSoundLevelInDb.setImageURI(photoUri)
//                            binding.ivPreviewTCILSoundLevelInDb.visibility = View.VISIBLE
//                            base64ProofPreviewTTCILSoundLevelInDb = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILwhether_all_the_academic" -> {
//                            binding.ivPreviewTCILwhether_all_the_academic.setImageURI(photoUri)
//                            binding.ivPreviewTCILwhether_all_the_academic.visibility = View.VISIBLE
//                            base64ProofPreviewTCILwhether_all_the_academic = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILAcademicRoomInformationBoard" -> {
//                            binding.ivPreviewTCILAcademicRoomInformationBoard.setImageURI(photoUri)
//                            binding.ivPreviewTCILAcademicRoomInformationBoard.visibility = View.VISIBLE
//                            base64ProofPreviewTCILAcademicRoomInformationBoard = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILInternalSignage" -> {
//                            binding.ivPreviewTCILInternalSignage.setImageURI(photoUri)
//                            binding.ivPreviewTCILInternalSignage.visibility = View.VISIBLE
//                            base64ProofPreviewTCILInternalSignage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILCctcCamerasWithAudioFacility" -> {
//                            binding.ivPreviewTCILCctcCamerasWithAudioFacility.setImageURI(photoUri)
//                            binding.ivPreviewTCILCctcCamerasWithAudioFacility.visibility = View.VISIBLE
//                            base64ProofPreviewTCILCctcCamerasWithAudioFacility = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILLanEnabledComputersInNo" -> {
//                            binding.ivPreviewTCILLanEnabledComputersInNo.setImageURI(photoUri)
//                            binding.ivPreviewTCILLanEnabledComputersInNo.visibility = View.VISIBLE
//                            base64ProofPreviewTCILLanEnabledComputersInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILInternetConnections" -> {
//                            binding.ivPreviewTCILInternetConnections.setImageURI(photoUri)
//                            binding.ivPreviewTCILInternetConnections.visibility = View.VISIBLE
//                            base64ProofPreviewTCILInternetConnections = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILDoAllComputersHaveTypingTutor" -> {
//                            binding.ivPreviewTCILDoAllComputersHaveTypingTutor.setImageURI(photoUri)
//                            binding.ivPreviewTCILDoAllComputersHaveTypingTutor.visibility = View.VISIBLE
//                            base64ProofPreviewTCILDoAllComputersHaveTypingTutor = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILTablets" -> {
//                            binding.ivPreviewTCILTablets.setImageURI(photoUri)
//                            binding.ivPreviewTCILTablets.visibility = View.VISIBLE
//                            base64ProofPreviewTCILTablets = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILStoolsChairs" -> {
//                            binding.ivPreviewTCILStoolsChairs.setImageURI(photoUri)
//                            binding.ivPreviewTCILStoolsChairs.visibility = View.VISIBLE
//                            base64ProofPreviewTCILStoolsChairs = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILTrainerChair" -> {
//                            binding.ivPreviewTCILTrainerChair.setImageURI(photoUri)
//                            binding.ivPreviewTCILTrainerChair.visibility = View.VISIBLE
//                            base64ProofPreviewTCILTrainerChair = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILTrainerTable" -> {
//                            binding.ivPreviewTCILTrainerTable.setImageURI(photoUri)
//                            binding.ivPreviewTCILTrainerTable.visibility = View.VISIBLE
//                            base64ProofPreviewTCILTrainerTable = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILLightsInNo" -> {
//                            binding.ivPreviewTCILLightsInNo.setImageURI(photoUri)
//                            binding.ivPreviewTCILLightsInNo.visibility = View.VISIBLE
//                            base64ProofPreviewTCILLightsInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILFansInNo" -> {
//                            binding.ivPreviewTCILFansInNo.setImageURI(photoUri)
//                            binding.ivPreviewTCILFansInNo.visibility = View.VISIBLE
//                            base64ProofPreviewTCILFansInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILElectricaPowerBackUpForThRoom" -> {
//                            binding.ivPreviewTCILElectricaPowerBackUpForThRoom.setImageURI(photoUri)
//                            binding.ivPreviewTCILElectricaPowerBackUpForThRoom.visibility = View.VISIBLE
//                            base64ProofPreviewTCILElectricaPowerBackUpForThRoom = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILTheoryCumItLabPhotogragh" -> {
//                            binding.ivPreviewTCILTheoryCumItLabPhotogragh.setImageURI(photoUri)
//                            binding.ivPreviewTCILTheoryCumItLabPhotogragh.visibility = View.VISIBLE
//                            base64ProofPreviewTCILTheoryCumItLabPhotogragh = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCILDoes_the_room_has" -> {
//                            binding.ivPreviewTCILDoes_the_room_has.setImageURI(photoUri)
//                            binding.ivPreviewTCILDoes_the_room_has.visibility = View.VISIBLE
//                            base64ProofPreviewTCILDoes_the_room_has = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        // Theory Cum Domain Lab
//                        "btnTCDLTypeofRoofItLab" -> {
//                            binding.ivPreviewTCDLTypeofRoofItLab.setImageURI(photoUri)
//                            binding.ivPreviewTCDLTypeofRoofItLab.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLTypeofRoofItLab = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLFalseCellingProvide" -> {
//                            binding.ivPreviewTCDLFalseCellingProvide.setImageURI(photoUri)
//                            binding.ivPreviewTCDLFalseCellingProvide.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLFalseCellingProvide = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLHeightOfCelling" -> {
//                            binding.ivPreviewTCDLHeightOfCelling.setImageURI(photoUri)
//                            binding.ivPreviewTCDLHeightOfCelling.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLHeightOfCelling = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLVentilationAreaInSqFt" -> {
//                            binding.ivPreviewTCDLVentilationAreaInSqFt.setImageURI(photoUri)
//                            binding.ivPreviewTCDLVentilationAreaInSqFt.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLVentilationAreaInSqFt = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLSoundLevelInDb" -> {
//                            binding.ivPreviewTCDLSoundLevelInDb.setImageURI(photoUri)
//                            binding.ivPreviewTCDLSoundLevelInDb.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLSoundLevelInDb = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLwhether_all_the_academic" -> {
//                            binding.ivPreviewTCDLwhether_all_the_academic.setImageURI(photoUri)
//                            binding.ivPreviewTCDLwhether_all_the_academic.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLwhether_all_the_academic = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLAcademicRoomInformationBoard" -> {
//                            binding.ivPreviewTCDLAcademicRoomInformationBoard.setImageURI(photoUri)
//                            binding.ivPreviewTCDLAcademicRoomInformationBoard.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLAcademicRoomInformationBoard = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLInternalSignage" -> {
//                            binding.ivPreviewTCDLInternalSignage.setImageURI(photoUri)
//                            binding.ivPreviewTCDLInternalSignage.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLInternalSignage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLCctcCamerasWithAudioFacility" -> {
//                            binding.ivPreviewTCDLCctcCamerasWithAudioFacility.setImageURI(photoUri)
//                            binding.ivPreviewTCDLCctcCamerasWithAudioFacility.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLCctcCamerasWithAudioFacility = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLLcdDigitalProjector" -> {
//                            binding.ivPreviewTCDLLcdDigitalProjector.setImageURI(photoUri)
//                            binding.ivPreviewTCDLLcdDigitalProjector.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLLcdDigitalProjector = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLChairForCandidatesInNo" -> {
//                            binding.ivPreviewTCDLChairForCandidatesInNo.setImageURI(photoUri)
//                            binding.ivPreviewTCDLChairForCandidatesInNo.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLChairForCandidatesInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLUploaadTrainerChair" -> {
//                            binding.ivPreviewTCDLTrainerChair.setImageURI(photoUri)
//                            binding.ivPreviewTCDLTrainerChair.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLTrainerChair = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLTrainerTable" -> {
//                            binding.ivPreviewTCDLTrainerTable.setImageURI(photoUri)
//                            binding.ivPreviewTCDLTrainerTable.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLTrainerTable = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLWritingBoard" -> {
//                            binding.ivPreviewTCDLWritingBoard.setImageURI(photoUri)
//                            binding.ivPreviewTCDLWritingBoard.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLWritingBoard = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLLightsInNo" -> {
//                            binding.ivPreviewTCDLLightsInNo.setImageURI(photoUri)
//                            binding.ivPreviewTCDLLightsInNo.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLLightsInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLFansInNo" -> {
//                            binding.ivPreviewTCDLFansInNo.setImageURI(photoUri)
//                            binding.ivPreviewTCDLFansInNo.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLFansInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLElectricaPowerBackUpForThRoom" -> {
//                            binding.ivPreviewTCDLElectricaPowerBackUpForThRoom.setImageURI(photoUri)
//                            binding.ivPreviewTCDLElectricaPowerBackUpForThRoom.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLElectricaPowerBackUpForThRoom = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLListofDomain" -> {
//                            binding.ivPreviewTCDLListofDomain.setImageURI(photoUri)
//                            binding.ivPreviewTCDLListofDomain.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLListofDomain = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLDomainLabPhotogragh" -> {
//                            binding.ivPreviewTCDLDomainLabPhotogragh.setImageURI(photoUri)
//                            binding.ivPreviewTCDLDomainLabPhotogragh.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLDomainLabPhotogragh = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCDLDoes_the_room_has" -> {
//                            binding.ivPreviewTCDLDoes_the_room_has.setImageURI(photoUri)
//                            binding.ivPreviewTCDLDoes_the_room_has.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLDoes_the_room_has = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        // Domain Lab
//                        "btnDLTypeofRoofItLab" -> {
//                            binding.ivPreviewDLTypeofRoofItLab.setImageURI(photoUri)
//                            binding.ivPreviewDLTypeofRoofItLab.visibility = View.VISIBLE
//                            base64ProofPreviewDLTypeofRoofItLab = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLFalseCellingProvide" -> {
//                            binding.ivPreviewDLFalseCellingProvide.setImageURI(photoUri)
//                            binding.ivPreviewDLFalseCellingProvide.visibility = View.VISIBLE
//                            base64ProofPreviewDLFalseCellingProvide = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLHeightOfCelling" -> {
//                            binding.ivPreviewDLHeightOfCelling.setImageURI(photoUri)
//                            binding.ivPreviewDLHeightOfCelling.visibility = View.VISIBLE
//                            base64ProofPreviewDLHeightOfCelling = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLVentilationAreaInSqFt" -> {
//                            binding.ivPreviewDLVentilationAreaInSqFt.setImageURI(photoUri)
//                            binding.ivPreviewDLVentilationAreaInSqFt.visibility = View.VISIBLE
//                            base64ProofPreviewDLVentilationAreaInSqFt = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLSoundLevelInDb" -> {
//                            binding.ivPreviewDLSoundLevelInDb.setImageURI(photoUri)
//                            binding.ivPreviewDLSoundLevelInDb.visibility = View.VISIBLE
//                            base64ProofPreviewDLSoundLevelInDb = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLwhether_all_the_academic" -> {
//                            binding.ivPreviewDLwhether_all_the_academic.setImageURI(photoUri)
//                            binding.ivPreviewDLwhether_all_the_academic.visibility = View.VISIBLE
//                            base64ProofPreviewDLwhether_all_the_academic = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLAcademicRoomInformationBoard" -> {
//                            binding.ivPreviewDLAcademicRoomInformationBoard.setImageURI(photoUri)
//                            binding.ivPreviewDLAcademicRoomInformationBoard.visibility = View.VISIBLE
//                            base64ProofPreviewDLAcademicRoomInformationBoard = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLInternalSignage" -> {
//                            binding.ivPreviewDLInternalSignage.setImageURI(photoUri)
//                            binding.ivPreviewDLInternalSignage.visibility = View.VISIBLE
//                            base64ProofPreviewDLInternalSignage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLCctcCamerasWithAudioFacility" -> {
//                            binding.ivPreviewDLCctcCamerasWithAudioFacility.setImageURI(photoUri)
//                            binding.ivPreviewDLCctcCamerasWithAudioFacility.visibility = View.VISIBLE
//                            base64ProofPreviewDLCctcCamerasWithAudioFacility = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLLcdDigitalProjector" -> {
//                            binding.ivPreviewDLLcdDigitalProjector.setImageURI(photoUri)
//                            binding.ivPreviewDLLcdDigitalProjector.visibility = View.VISIBLE
//                            base64ProofPreviewDLLcdDigitalProjector = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLChairForCandidatesInNo" -> {
//                            binding.ivPreviewDLChairForCandidatesInNo.setImageURI(photoUri)
//                            binding.ivPreviewDLChairForCandidatesInNo.visibility = View.VISIBLE
//                            base64ProofPreviewDLChairForCandidatesInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLUploaadTrainerChair" -> {
//                            binding.ivPreviewDLTrainerChair.setImageURI(photoUri)
//                            binding.ivPreviewDLTrainerChair.visibility = View.VISIBLE
//                            base64ProofPreviewDLTrainerChair = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLTrainerTable" -> {
//                            binding.ivPreviewDLTrainerTable.setImageURI(photoUri)
//                            binding.ivPreviewDLTrainerTable.visibility = View.VISIBLE
//                            base64ProofPreviewDLTrainerTable = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLWritingBoard" -> {
//                            binding.ivPreviewDLWritingBoard.setImageURI(photoUri)
//                            binding.ivPreviewDLWritingBoard.visibility = View.VISIBLE
//                            base64ProofPreviewDLWritingBoard = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLLightsInNo" -> {
//                            binding.ivPreviewDLLightsInNo.setImageURI(photoUri)
//                            binding.ivPreviewDLLightsInNo.visibility = View.VISIBLE
//                            base64ProofPreviewDLLightsInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLFansInNo" -> {
//                            binding.ivPreviewDLFansInNo.setImageURI(photoUri)
//                            binding.ivPreviewDLFansInNo.visibility = View.VISIBLE
//                            base64ProofPreviewDLFansInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLElectricaPowerBackUpForThRoom" -> {
//                            binding.ivPreviewDLElectricaPowerBackUpForThRoom.setImageURI(photoUri)
//                            binding.ivPreviewDLElectricaPowerBackUpForThRoom.visibility = View.VISIBLE
//                            base64ProofPreviewDLElectricaPowerBackUpForThRoom = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLILListofDomain" -> {
//                            binding.ivPreviewDLILListofDomain.setImageURI(photoUri)
//                            binding.ivPreviewDLILListofDomain.visibility = View.VISIBLE
//                            base64ProofPreviewDLILListofDomain = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLDomainLabPhotogragh" -> {
//                            binding.ivPreviewDLDomainLabPhotogragh.setImageURI(photoUri)
//                            binding.ivPreviewDLDomainLabPhotogragh.visibility = View.VISIBLE
//                            base64ProofPreviewDLDomainLabPhotogragh = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnDLDoes_the_room_has" -> {
//                            binding.ivPreviewDLDoes_the_room_has.setImageURI(photoUri)
//                            binding.ivPreviewDLDoes_the_room_has.visibility = View.VISIBLE
//                            base64ProofPreviewDLDoes_the_room_has = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        // Theory Class Room
//                        "btnTCRTypeofRoofItLab" -> {
//                            binding.ivPreviewTCRTypeofRoofItLab.setImageURI(photoUri)
//                            binding.ivPreviewTCRTypeofRoofItLab.visibility = View.VISIBLE
//                            base64ProofPreviewTCRTypeofRoofItLab = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRFalseCellingProvide" -> {
//                            binding.ivPreviewTCRFalseCellingProvide.setImageURI(photoUri)
//                            binding.ivPreviewTCRFalseCellingProvide.visibility = View.VISIBLE
//                            base64ProofPreviewTCRFalseCellingProvide = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRHeightOfCelling" -> {
//                            binding.ivPreviewTCRHeightOfCelling.setImageURI(photoUri)
//                            binding.ivPreviewTCRHeightOfCelling.visibility = View.VISIBLE
//                            base64ProofPreviewTCRHeightOfCelling = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRVentilationAreaInSqFt" -> {
//                            binding.ivPreviewTCRVentilationAreaInSqFt.setImageURI(photoUri)
//                            binding.ivPreviewTCRVentilationAreaInSqFt.visibility = View.VISIBLE
//                            base64ProofPreviewTCRVentilationAreaInSqFt = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRSoundLevelInDb" -> {
//                            binding.ivPreviewTCRSoundLevelInDb.setImageURI(photoUri)
//                            binding.ivPreviewTCRSoundLevelInDb.visibility = View.VISIBLE
//                            base64ProofPreviewTCRSoundLevelInDb = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRwhether_all_the_academic" -> {
//                            binding.ivPreviewTCRwhether_all_the_academic.setImageURI(photoUri)
//                            binding.ivPreviewTCRwhether_all_the_academic.visibility = View.VISIBLE
//                            base64ProofPreviewTCRwhether_all_the_academic = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRAcademicRoomInformationBoard" -> {
//                            binding.ivPreviewTCRAcademicRoomInformationBoard.setImageURI(photoUri)
//                            binding.ivPreviewTCRAcademicRoomInformationBoard.visibility = View.VISIBLE
//                            base64ProofPreviewTCRAcademicRoomInformationBoard = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRCctcCamerasWithAudioFacility" -> {
//                            binding.ivPreviewTCRCctcCamerasWithAudioFacility.setImageURI(photoUri)
//                            binding.ivPreviewTCRCctcCamerasWithAudioFacility.visibility = View.VISIBLE
//                            base64ProofPreviewTCRCctcCamerasWithAudioFacility = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRLcdDigitalProjector" -> {
//                            binding.ivPreviewTCRLcdDigitalProjector.setImageURI(photoUri)
//                            binding.ivPreviewTCRLcdDigitalProjector.visibility = View.VISIBLE
//                            base64ProofPreviewTCRLcdDigitalProjector = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRChairForCandidatesInNo" -> {
//                            binding.ivPreviewTCRChairForCandidatesInNo.setImageURI(photoUri)
//                            binding.ivPreviewTCRChairForCandidatesInNo.visibility = View.VISIBLE
//                            base64ProofPreviewTCRChairForCandidatesInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRTrainerChair" -> {
//                            binding.ivPreviewTCRTrainerChair.setImageURI(photoUri)
//                            binding.ivPreviewTCRTrainerChair.visibility = View.VISIBLE
//                            base64ProofPreviewTCRTrainerChair = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRTrainerTable" -> {
//                            binding.ivPreviewTCRTrainerTable.setImageURI(photoUri)
//                            binding.ivPreviewTCRTrainerTable.visibility = View.VISIBLE
//                            base64ProofPreviewTCRTrainerTable = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRWritingBoard" -> {
//                            binding.ivPreviewTCRWritingBoard.setImageURI(photoUri)
//                            binding.ivPreviewTCRWritingBoard.visibility = View.VISIBLE
//                            base64ProofPreviewTCRWritingBoard = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRLightsInNo" -> {
//                            binding.ivPreviewTCRLightsInNo.setImageURI(photoUri)
//                            binding.ivPreviewTCRLightsInNo.visibility = View.VISIBLE
//                            base64ProofPreviewTCRLightsInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRFansInNo" -> {
//                            binding.ivPreviewTCRFansInNo.setImageURI(photoUri)
//                            binding.ivPreviewTCRFansInNo.visibility = View.VISIBLE
//                            base64ProofPreviewTCRFansInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRElectricaPowerBackUpForThRoom" -> {
//                            binding.ivPreviewTCRElectricaPowerBackUpForThRoom.setImageURI(photoUri)
//                            binding.ivPreviewTCRElectricaPowerBackUpForThRoom.visibility = View.VISIBLE
//                            base64ProofPreviewTCRElectricaPowerBackUpForThRoom = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRDomainLabPhotogragh" -> {
//                            binding.ivPreviewTCRDomainLabPhotogragh.setImageURI(photoUri)
//                            binding.ivPreviewTCRDomainLabPhotogragh.visibility = View.VISIBLE
//                            base64ProofPreviewTCRDomainLabPhotogragh = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRDoes_the_room_has" -> {
//                            binding.ivPreviewTCRDoes_the_room_has.setImageURI(photoUri)
//                            binding.ivPreviewTCRDoes_the_room_has.visibility = View.VISIBLE
//                            base64ProofPreviewTCRDoes_the_room_has = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                        "btnTCRInternalSignage" -> {
//                            binding.ivPreviewTCRInternalSignage.setImageURI(photoUri)
//                            binding.ivPreviewTCRInternalSignage.visibility = View.VISIBLE
//                            base64ProofPreviewTCRInternalSignage = AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
//                    }
//                } else {
//                    Toast.makeText(requireContext(), "Photo capture failed", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        permissionLauncher =
//            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//                if (isGranted) launchCamera()
//                else Toast.makeText(
//                    requireContext(),
//                    "Camera permission is required.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentTrainingBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setupExpandableSections()
//        setupPhotoUploadButtons()
//        collectSectionStatus()
//        collectFinalSubmitData()
//
//        centerId = arguments?.getString("centerId").toString()
//        sanctionOrder = arguments?.getString("sanctionOrder").toString()
//        status = arguments?.getString("status")
//        remarks = arguments?.getString("remarks")
//        trainingCenterName = arguments?.getString("trainingCenterName")
//
//        binding.tvTitle.text = trainingCenterName
//        RecyClerViewUI()
//
//        binding.root.setOnTouchListener { v, event ->
//            AppUtil.hideKeyboard(requireActivity())
//            v.performClick()
//            false
//        }
//
//        if (status == STATUS_QM || status == STATUS_SM) {
//            AlertDialog.Builder(requireContext())
//                .setTitle("Remarks")
//                .setMessage(remarks)
//                .setPositiveButton("Okay") { dialog: DialogInterface?, _: Int ->
//                    dialog?.dismiss()
//                }
//                .show()
//        }
//
//        val requestTcInfraReq = TrainingCenterInfo(
//            appVersion = BuildConfig.VERSION_NAME,
//            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
//            tcId = centerId.toInt(),
//            sanctionOrder = sanctionOrder,
//            imeiNo = AppUtil.getAndroidId(requireContext())
//        )
//
//        viewModel.getSectionsStatusData(requestTcInfraReq)
//
//        binding.btnBack.setOnClickListener {
//            findNavController().navigateUp()
//        }
//
//        // Make fields uneditable for area calculation
//        binding.tvArea.isFocusable = false
//        binding.tvArea.isClickable = false
//
//        // Add text watcher for area calculation
//        binding.etWidth.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable?) {
//                calculateAndShowArea()
//            }
//        })
//
//        // Calculate Area button
//        binding.btnCalculateArea.setOnClickListener {
//            val length = binding.etDescLength.text.toString().toDoubleOrNull() ?: 0.0
//            val width = binding.etDescWidth.text.toString().toDoubleOrNull() ?: 0.0
//            binding.etArea.setText("${length * width}")
//        }
//
//        // Submit Add More button
//        binding.btnSubmitAdddMore.setOnClickListener {
//            binding.LayoutLinear.visibility = View.VISIBLE
//        }
//
//        // Initialize spinner for area selection
//        val items = listOf(
//            "Select Area",
//            "Office Cum Counselling Room",
//            "Reception Area",
//            "Counselling Room",
//            "Office Room",
//            "IT cum Domain Lab",
//            "Theory Cum IT Lab",
//            "Theory Cum Domain Lab",
//            "IT Lab",
//            "Domain Lab",
//            "Theory Class Room"
//        )
//
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.AcsdemicSpinner.adapter = adapter
//
//        binding.AcsdemicSpinner.onItemSelectedListener = object