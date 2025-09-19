package com.deendayalproject.fragments

import SharedViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.model.request.CCTVComplianceRequest
import com.deendayalproject.model.request.ElectricalWiringRequest
import com.deendayalproject.model.request.InsertTcGeneralDetailsRequest
import com.deendayalproject.model.request.TcAvailabilitySupportInfraRequest
import com.deendayalproject.model.request.TcBasicInfoRequest
import com.deendayalproject.model.request.TcCommonEquipmentRequest
import com.deendayalproject.model.request.TcDescriptionOtherAreasRequest
import com.deendayalproject.model.request.TcSignagesInfoBoardRequest
import com.deendayalproject.util.AppUtil
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.String
class TrainingFragment : Fragment() {
    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var photoUri: Uri
    private var currentPhotoTarget: String = ""
    private var centerId: String = ""

    // CCTV Photos (IP-enabled camera)
    private var base64MonitorFile: String? = null
    private var base64ConformanceFile: String? = null
    private var base64StorageFile: String? = null
    private var base64DVRFile: String? = null

    private lateinit var ivMonitorPreview: ImageView
    private lateinit var ivConformancePreview: ImageView
    private lateinit var ivStoragePreview: ImageView
    private lateinit var ivDVRPreview: ImageView

    // Electrical Wiring
    private var base64SwitchBoardImage: String? = null
    private var base64WireSecurityImage: String? = null

    private lateinit var ivSwitchBoardPreview: ImageView
    private lateinit var ivWireSecurityPreview: ImageView

    // General Details
    private var base64LeakageImage: String? = null
    private var base64StairsImage: String? = null

    private lateinit var ivLeakagePreview: ImageView
    private lateinit var ivStairsPreview: ImageView

    private lateinit var spinnerLeakageCheck: Spinner
    private lateinit var spinnerProtectionStairs: Spinner
    private lateinit var spinnerDDUConformance: Spinner
    private lateinit var spinnerCandidateSafety: Spinner

    // Spinners
    private lateinit var spinnerTcNameBoard: Spinner
    private lateinit var spinnerActivityAchievementBoard: Spinner
    private lateinit var spinnerStudentEntitlementBoard: Spinner
    private lateinit var spinnerContactDetailBoard: Spinner
    private lateinit var spinnerBasicInfoBoard: Spinner
    private lateinit var spinnerCodeConductBoard: Spinner
    private lateinit var spinnerStudentAttendanceBoard: Spinner

    // ImageViews
    private lateinit var ivTcNameBoardPreview: ImageView
    private lateinit var ivActivityAchievementBoardPreview: ImageView
    private lateinit var ivStudentEntitlementBoardPreview: ImageView
    private lateinit var ivContactDetailBoardPreview: ImageView
    private lateinit var ivBasicInfoBoardPreview: ImageView
    private lateinit var ivCodeConductBoardPreview: ImageView
    private lateinit var ivStudentAttendanceBoardPreview: ImageView

    // Base64 Image Strings
    private var base64TcNameBoardImage: String? = null
    private var base64ActivityAchievementBoardImage: String? = null
    private var base64StudentEntitlementBoardImage: String? = null
    private var base64ContactDetailBoardImage: String? = null
    private var base64BasicInfoBoardImage: String? = null
    private var base64CodeConductBoardImage: String? = null
    private var base64StudentAttendanceBoardImage: String? = null

    // Spinners
    private lateinit var spinnerPowerBackup: Spinner
    private lateinit var spinnerCCTV: Spinner
    private lateinit var spinnerDocumentStorage: Spinner
    private lateinit var spinnerGrievanceRegister: Spinner
    private lateinit var spinnerMinimumEquipment: Spinner
    private lateinit var spinnerDirectionBoards: Spinner
    private lateinit var etBiometricDevices: TextInputEditText
    private lateinit var etPrinterScanner: TextInputEditText
    private lateinit var etDigitalCamera: TextInputEditText

    // Base64 Image Strings
    private var base64PowerBackupImage: String? = null
    private var base64BiometricDevices: String? = null
    private var base64CCTVImage: String? = null
    private var base64DocumentStorageImage: String? = null
    private var base64PrinterScanner: String? = null
    private var base64DigitalCamera: String? = null
    private var base64GrievanceRegisterImage: String? = null
    private var base64MinimumEquipmentImage: String? = null
    private var base64DirectionBoardsImage: String? = null

    private lateinit var ivPowerBackupPreview: ImageView
    private lateinit var ivBiometricDevicesPreview: ImageView
    private lateinit var ivCCTVPreview: ImageView
    private lateinit var ivDocumentStoragePreview: ImageView
    private lateinit var ivPrinterScannerPreview: ImageView
    private lateinit var ivDigitalCameraPreview: ImageView
    private lateinit var ivGrievanceRegisterPreview: ImageView
    private lateinit var ivMinimumEquipmentPreview: ImageView
    private lateinit var ivDirectionBoardsPreview: ImageView

    // description
    private var base64ProofUploadImage: String? = null
    private var base64CirculationProofImage: String? = null
    private var base64OpenSpaceProofImage: String? = null
    private var base64ParkingSpaceProofImage: String? = null

    private lateinit var ivProofPreview: ImageView
    private lateinit var ivCirculationProofPreview: ImageView
    private lateinit var ivOpenSpaceProofPreview: ImageView
    private lateinit var ivParkingProofPreview: ImageView


    private lateinit var etCorridorNo: TextInputEditText
    private lateinit var etDescLength: TextInputEditText
    private lateinit var etDescWidth: TextInputEditText
    private lateinit var etArea: TextInputEditText
    private lateinit var etLights: TextInputEditText
    private lateinit var etFans: TextInputEditText
    private lateinit var etCirculationArea: TextInputEditText
    private lateinit var etOpenSpace: TextInputEditText
    private lateinit var etExclusiveParkingSpace: TextInputEditText

    //Support Infra
    private lateinit var spinnerFirstAidKit: Spinner
    private lateinit var spinnerSafeDrinkingWater: Spinner
    private lateinit var editFireFightingEquipment: EditText

    // Safe Drinking Water
    private lateinit var ivSafeDrinkingWaterPreview: ImageView
    private var base64SafeDrinkingWater: String? = null

    // Fire Fighting Equipment
    private lateinit var ivFireFightingEquipmentPreview: ImageView
    private var base64FireFightingEquipment: String? = null

    // First Aid Kit
    private lateinit var ivFirstAidKitPreview: ImageView
    private var base64FirstAidKit: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    Log.d("Camera", "Captured image URI: $photoUri")
                    when (currentPhotoTarget) {
                        "monitor" -> {
                            ivMonitorPreview.setImageURI(photoUri)
                            ivMonitorPreview.visibility = View.VISIBLE
                            base64MonitorFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "conformance" -> {
                            ivConformancePreview.setImageURI(photoUri)
                            ivConformancePreview.visibility = View.VISIBLE
                            base64ConformanceFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "storage" -> {
                            ivStoragePreview.setImageURI(photoUri)
                            ivStoragePreview.visibility = View.VISIBLE
                            base64StorageFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "dvr" -> {
                            ivDVRPreview.setImageURI(photoUri)
                            ivDVRPreview.visibility = View.VISIBLE
                            base64DVRFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "switchBoard" -> {
                            ivSwitchBoardPreview.setImageURI(photoUri)
                            ivSwitchBoardPreview.visibility = View.VISIBLE
                            base64SwitchBoardImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "WireSecurity" -> {
                            ivWireSecurityPreview.setImageURI(photoUri)
                            ivWireSecurityPreview.visibility = View.VISIBLE
                            base64WireSecurityImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "leakage" -> {
                            ivLeakagePreview.setImageURI(photoUri)
                            ivLeakagePreview.visibility = View.VISIBLE
                            base64LeakageImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "stairs" -> {
                            ivStairsPreview.setImageURI(photoUri)
                            ivStairsPreview.visibility = View.VISIBLE
                            base64StairsImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "tcNameBoard" -> {
                            ivTcNameBoardPreview.setImageURI(photoUri)
                            ivTcNameBoardPreview.visibility = View.VISIBLE
                            base64TcNameBoardImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "activityAchievementBoard" -> {
                            ivActivityAchievementBoardPreview.setImageURI(photoUri)
                            ivActivityAchievementBoardPreview.visibility = View.VISIBLE
                            base64ActivityAchievementBoardImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "studentEntitlementBoard" -> {
                            ivStudentEntitlementBoardPreview.setImageURI(photoUri)
                            ivStudentEntitlementBoardPreview.visibility = View.VISIBLE
                            base64StudentEntitlementBoardImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "contactDetailBoard" -> {
                            ivContactDetailBoardPreview.setImageURI(photoUri)
                            ivContactDetailBoardPreview.visibility = View.VISIBLE
                            base64ContactDetailBoardImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "basicInfoBoard" -> {
                            ivBasicInfoBoardPreview.setImageURI(photoUri)
                            ivBasicInfoBoardPreview.visibility = View.VISIBLE
                            base64BasicInfoBoardImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "codeConductBoard" -> {
                            ivCodeConductBoardPreview.setImageURI(photoUri)
                            ivCodeConductBoardPreview.visibility = View.VISIBLE
                            base64CodeConductBoardImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "studentAttendanceBoard" -> {
                            ivStudentAttendanceBoardPreview.setImageURI(photoUri)
                            ivStudentAttendanceBoardPreview.visibility = View.VISIBLE
                            base64StudentAttendanceBoardImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "SafeDrinkingWater" -> {
                            ivSafeDrinkingWaterPreview.setImageURI(photoUri)
                            ivSafeDrinkingWaterPreview.visibility = View.VISIBLE
                            base64SafeDrinkingWater = AppUtil.imageUriToBase64(context = requireContext(), photoUri)
                        }

                        "FireFightingEquipment" -> {
                            ivFireFightingEquipmentPreview.setImageURI(photoUri)
                            ivFireFightingEquipmentPreview.visibility = View.VISIBLE
                            base64FireFightingEquipment =
                                AppUtil.imageUriToBase64(context = requireContext(), photoUri)
                        }

                        "FirstAidKit" -> {
                            ivFirstAidKitPreview.setImageURI(photoUri)
                            ivFirstAidKitPreview.visibility = View.VISIBLE
                            base64FirstAidKit =
                                AppUtil.imageUriToBase64(context = requireContext(), photoUri)
                        }
                        "powerBackup" -> {
                            ivPowerBackupPreview.setImageURI(photoUri)
                            ivPowerBackupPreview.visibility = View.VISIBLE
                            base64PowerBackupImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "biometricDevices" -> {
                            ivBiometricDevicesPreview.setImageURI(photoUri)
                            ivBiometricDevicesPreview.visibility = View.VISIBLE
                            base64BiometricDevices = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "cctv" -> {
                            ivCCTVPreview.setImageURI(photoUri)
                            ivCCTVPreview.visibility = View.VISIBLE
                            base64CCTVImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "documentStorage" -> {
                            ivDocumentStoragePreview.setImageURI(photoUri)
                            ivDocumentStoragePreview.visibility = View.VISIBLE
                            base64DocumentStorageImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "printerScanner" -> {
                            ivPrinterScannerPreview.setImageURI(photoUri)
                            ivPrinterScannerPreview.visibility = View.VISIBLE
                            base64PrinterScanner = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "digitalCamera" -> {
                            ivDigitalCameraPreview.setImageURI(photoUri)
                            ivDigitalCameraPreview.visibility = View.VISIBLE
                            base64DigitalCamera = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "grievanceRegister" -> {
                            ivGrievanceRegisterPreview.setImageURI(photoUri)
                            ivGrievanceRegisterPreview.visibility = View.VISIBLE
                            base64GrievanceRegisterImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "minimumEquipment" -> {
                            ivMinimumEquipmentPreview.setImageURI(photoUri)
                            ivMinimumEquipmentPreview.visibility = View.VISIBLE
                            base64MinimumEquipmentImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "directionBoards" -> {
                            ivDirectionBoardsPreview.setImageURI(photoUri)
                            ivDirectionBoardsPreview.visibility = View.VISIBLE
                            base64DirectionBoardsImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Photo capture failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) launchCamera()
                else Toast.makeText(
                    requireContext(),
                    "Camera permission is required.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_training, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupExpandableSections(view)

         centerId = arguments?.getString("centerId").toString()

        // Initialize CCTV ImageViews
        ivMonitorPreview = view.findViewById(R.id.ivMonitorPreview)
        ivConformancePreview = view.findViewById(R.id.ivConformancePreview)
        ivStoragePreview = view.findViewById(R.id.ivStoragePreview)
        ivDVRPreview = view.findViewById(R.id.ivDVRPreview)

        // Initialize Electrical Wiring ImageViews
        ivSwitchBoardPreview = view.findViewById(R.id.ivSwitchBoardPreview)
        ivWireSecurityPreview = view.findViewById(R.id.ivWireSecurityPreview)

        // Initialize General Details ImageViews
        ivLeakagePreview = view.findViewById(R.id.ivLeakagePreview)
        ivStairsPreview = view.findViewById(R.id.ivStairsPreview)

        //Initialize support infra ImageViews
        ivFirstAidKitPreview = view.findViewById(R.id.ivFirstAidKitPreview)
        ivFireFightingEquipmentPreview = view.findViewById(R.id.ivFireFightingEquipmentPreview)
        ivSafeDrinkingWaterPreview = view.findViewById(R.id.ivSafeDrinkingWaterPreview)
        spinnerFirstAidKit = view.findViewById(R.id.spinnerFirstAidKit)
        spinnerSafeDrinkingWater = view.findViewById(R.id.spinnerSafeDrinkingWater)
        editFireFightingEquipment = view.findViewById<TextInputEditText>(R.id.editFireFightingEquipment)

        // ImageView preview  commmon equipment IDs
        ivPowerBackupPreview = view.findViewById(R.id.ivPowerBackupPreview)
        ivBiometricDevicesPreview = view.findViewById(R.id.ivBiometricDevicesPreview)
        ivCCTVPreview = view.findViewById(R.id.ivCCTVPreview)
        ivDocumentStoragePreview = view.findViewById(R.id.ivDocumentStoragePreview)
        ivPrinterScannerPreview = view.findViewById(R.id.ivPrinterScannerPreview)
        ivDigitalCameraPreview = view.findViewById(R.id.ivDigitalCameraPreview)
        ivGrievanceRegisterPreview = view.findViewById(R.id.ivGrievanceRegisterPreview)
        ivMinimumEquipmentPreview = view.findViewById(R.id.ivMinimumEquipmentPreview)
        ivDirectionBoardsPreview = view.findViewById(R.id.ivDirectionBoardsPreview)
        etBiometricDevices = view.findViewById(R.id.etBiometricDevices)
        etPrinterScanner = view.findViewById(R.id.etPrinterScanner)
        etDigitalCamera = view.findViewById(R.id.etDigitalCamera)
        spinnerPowerBackup=view.findViewById(R.id.spinnerPowerBackup)
        spinnerCCTV=view.findViewById(R.id.spinnerCCTV)
        spinnerDocumentStorage=view.findViewById(R.id.spinnerDocumentStorage)
        spinnerGrievanceRegister=view.findViewById(R.id.spinnerGrievanceRegister)
        spinnerMinimumEquipment=view.findViewById(R.id.spinnerMinimumEquipment)
        spinnerDirectionBoards=view.findViewById(R.id.spinnerDirectionBoards)

        // Description of other areas
        ivProofPreview = view.findViewById(R.id.ivProofPreview)
        ivCirculationProofPreview = view.findViewById(R.id.ivCirculationProofPreview)
        ivOpenSpaceProofPreview = view.findViewById(R.id.ivOpenSpaceProofPreview)
        ivParkingProofPreview = view.findViewById(R.id.ivParkingProofPreview)
        etCorridorNo =view.findViewById(R.id.etCorridorNo)
        etDescLength=view.findViewById(R.id.etDescLength)
        etDescWidth =view.findViewById(R.id.etDescWidth)
        etArea =view.findViewById(R.id.etArea)
        etLights =view.findViewById(R.id.etLights)
        etFans=view.findViewById(R.id.etFans)
        etCirculationArea =view.findViewById(R.id.etCirculationArea)
        etOpenSpace =view.findViewById(R.id.etOpenSpace)
        etExclusiveParkingSpace =view.findViewById(R.id.etExclusiveParkingSpace)

        // Setup Yes/No adapter
        val yesNoAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "Yes", "No")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        // Setup CCTV & Electrical Spinners& upport infra spinners
           val spinners = listOf(
            R.id.spinnerMonitorAccessible, R.id.spinnerConformance, R.id.spinnerStorage,
            R.id.spinnerDVRStaticIP, R.id.spinnerIPEnabled, R.id.spinnerResolution,
            R.id.spinnerVideoStream, R.id.spinnerRemoteAccessBrowser, R.id.spinnerSimultaneousAccess,
            R.id.spinnerSupportedProtocols, R.id.spinnerColorVideoAudio, R.id.spinnerStorageFacility,
            R.id.spinnerSwitchBoards, R.id.spinnerSecuringWires,R.id.spinnerFirstAidKit,R.id.spinnerSafeDrinkingWater,R.id.spinnerPowerBackup,R.id.spinnerCCTV,R.id.spinnerStorage,
        )
        spinners.forEach {
            view.findViewById<Spinner>(it).adapter = yesNoAdapter
        }
        val spinnersWithButtons = listOf(
            Pair(view.findViewById<Spinner>(R.id.spinnerMonitorAccessible), view.findViewById<Button>(R.id.btnUploadMonitorPhoto)),
            Pair(view.findViewById<Spinner>(R.id.spinnerConformance), view.findViewById<Button>(R.id.btnUploadConformancePhoto)),
            Pair(view.findViewById<Spinner>(R.id.spinnerStorage), view.findViewById<Button>(R.id.btnUploadStoragePhoto)),
            Pair(view.findViewById<Spinner>(R.id.spinnerDVRStaticIP), view.findViewById<Button>(R.id.btnUploadDVRPhoto)),
            Pair(view.findViewById<Spinner>(R.id.spinnerSwitchBoards), view.findViewById<Button>(R.id.btnUploadSwitchBoards)),
            Pair(view.findViewById<Spinner>(R.id.spinnerSecuringWires), view.findViewById<Button>(R.id.btnUploadSecuringWires)),
            Pair(view.findViewById<Spinner>(R.id.spinnerLeakageCheck), view.findViewById<Button>(R.id.btnUploadLeakageProof)),
            Pair(view.findViewById<Spinner>(R.id.spinnerProtectionStairs), view.findViewById<Button>(R.id.btnUploadProtectionStairs)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTrainingCentreNameBoard), view.findViewById<Button>(R.id.btnUploadTrainingCentreNameBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerActivitySummaryBoard), view.findViewById<Button>(R.id.btnUploadActivitySummaryBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerEntitlementBoard), view.findViewById<Button>(R.id.btnUploadEntitlementBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerImportantContacts), view.findViewById<Button>(R.id.btnUploadImportantContacts)),
            Pair(view.findViewById<Spinner>(R.id.spinnerBasicInfoBoard), view.findViewById<Button>(R.id.btnUploadBasicInfoBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerCodeOfConductBoard), view.findViewById<Button>(R.id.btnUploadCodeOfConductBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerAttendanceSummaryBoard), view.findViewById<Button>(R.id.btnUploadAttendanceSummaryBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerFirstAidKit), view.findViewById<Button>(R.id.btnUploadFirstAidKit)),
            Pair(view.findViewById<Spinner>(R.id.spinnerSafeDrinkingWater), view.findViewById<Button>(R.id.btnUploadSafeDrinkingWater)),
            Pair(view.findViewById<Spinner>(R.id.spinnerPowerBackup), view.findViewById<Button>(R.id.btnUploadPowerBackup)),
            Pair(view.findViewById<Spinner>(R.id.spinnerCCTV), view.findViewById<Button>(R.id.btnUploadCCTV)),
            Pair(view.findViewById<Spinner>(R.id.spinnerDocumentStorage), view.findViewById<Button>(R.id.btnUploadDocumentStorage)),
            Pair(view.findViewById<Spinner>(R.id.spinnerGrievanceRegister), view.findViewById<Button>(R.id.btnSubmitGeneralDetails)),
            Pair(view.findViewById<Spinner>(R.id.spinnerMinimumEquipment), view.findViewById<Button>(R.id.btnUploadMinimumEquipment)),
            Pair(view.findViewById<Spinner>(R.id.spinnerDirectionBoards), view.findViewById<Button>(R.id.btnUploadDirectionBoards))
        )

        spinnersWithButtons.forEach { (spinner, button) ->
            spinner.adapter = yesNoAdapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selected = parent.getItemAtPosition(position).toString()
                    button.visibility = if (selected == "No") View.GONE else View.VISIBLE
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
        // General Details Spinners
        spinnerLeakageCheck = view.findViewById(R.id.spinnerLeakageCheck)
        spinnerProtectionStairs = view.findViewById(R.id.spinnerProtectionStairs)
        spinnerDDUConformance = view.findViewById(R.id.spinnerDDUConformance)
        spinnerCandidateSafety = view.findViewById(R.id.spinnerCandidateSafety)

        spinnerLeakageCheck.adapter = yesNoAdapter
        spinnerProtectionStairs.adapter = yesNoAdapter
        spinnerDDUConformance.adapter = yesNoAdapter
        spinnerCandidateSafety.adapter = yesNoAdapter

        // signage info baords spinners
        spinnerTcNameBoard = view.findViewById(R.id.spinnerTrainingCentreNameBoard)
        spinnerActivityAchievementBoard = view.findViewById(R.id.spinnerActivitySummaryBoard)
        spinnerStudentEntitlementBoard = view.findViewById(R.id.spinnerEntitlementBoard)
        spinnerContactDetailBoard = view.findViewById(R.id.spinnerImportantContacts)
        spinnerBasicInfoBoard = view.findViewById(R.id.spinnerBasicInfoBoard)
        spinnerCodeConductBoard = view.findViewById(R.id.spinnerCodeOfConductBoard)
        spinnerStudentAttendanceBoard = view.findViewById(R.id.spinnerAttendanceSummaryBoard)


        // ImageView setup
        ivTcNameBoardPreview = view.findViewById(R.id.ivTcNameBoardPreview)
        ivActivityAchievementBoardPreview = view.findViewById(R.id.ivActivityAchievementBoardPreview)
        ivStudentEntitlementBoardPreview = view.findViewById(R.id.ivStudentEntitlementBoardPreview)
        ivContactDetailBoardPreview = view.findViewById(R.id.ivContactDetailBoardPreview)
        ivBasicInfoBoardPreview = view.findViewById(R.id.ivBasicInfoBoardPreview)
        ivCodeConductBoardPreview = view.findViewById(R.id.ivCodeConductBoardPreview)
        ivStudentAttendanceBoardPreview = view.findViewById(R.id.ivStudentAttendanceBoardPreview)

        spinnerTcNameBoard.adapter = yesNoAdapter
        spinnerActivityAchievementBoard.adapter = yesNoAdapter
        spinnerStudentEntitlementBoard.adapter = yesNoAdapter
        spinnerContactDetailBoard.adapter = yesNoAdapter
        spinnerBasicInfoBoard.adapter = yesNoAdapter
        spinnerCodeConductBoard.adapter = yesNoAdapter
        spinnerStudentAttendanceBoard.adapter = yesNoAdapter

        // CCTV Photo Upload Buttons
        view.findViewById<Button>(R.id.btnUploadMonitorPhoto).setOnClickListener {
            currentPhotoTarget = "monitor"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadConformancePhoto).setOnClickListener {
            currentPhotoTarget = "conformance"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadStoragePhoto).setOnClickListener {
            currentPhotoTarget = "storage"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadDVRPhoto).setOnClickListener {
            currentPhotoTarget = "dvr"
            checkAndLaunchCamera()
        }

        // Electrical Photo Upload Buttons
        view.findViewById<Button>(R.id.btnUploadSwitchBoards).setOnClickListener {
            currentPhotoTarget = "switchBoard"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadSecuringWires).setOnClickListener {
            currentPhotoTarget = "WireSecurity"
            checkAndLaunchCamera()
        }

        // General Photo Upload Buttons
        view.findViewById<Button>(R.id.btnUploadLeakageProof).setOnClickListener {
            currentPhotoTarget = "leakage"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadProtectionStairs).setOnClickListener {
            currentPhotoTarget = "stairs"
            checkAndLaunchCamera()
        }

        // Button click listeners to upload photos (you must define buttons in XML accordingly)
        // signages info boards
        view.findViewById<Button>(R.id.btnUploadTrainingCentreNameBoard).setOnClickListener {
            currentPhotoTarget = "tcNameBoard"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadActivitySummaryBoard).setOnClickListener {
            currentPhotoTarget = "activityAchievementBoard"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadEntitlementBoard).setOnClickListener {
            currentPhotoTarget = "studentEntitlementBoard"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadImportantContacts).setOnClickListener {
            currentPhotoTarget = "contactDetailBoard"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadBasicInfoBoard).setOnClickListener {
            currentPhotoTarget = "basicInfoBoard"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadCodeOfConductBoard).setOnClickListener {
            currentPhotoTarget = "codeConductBoard"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadAttendanceSummaryBoard).setOnClickListener {
            currentPhotoTarget = "studentAttendanceBoard"
            checkAndLaunchCamera()
        }
        //support infra
        view.findViewById<Button>(R.id.btnUploadFirstAidKit).setOnClickListener {
            currentPhotoTarget = "FirstAidKit"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadFireFightingEquipment).setOnClickListener {
            currentPhotoTarget = "FireFightingEquipment"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadSafeDrinkingWater).setOnClickListener {
            currentPhotoTarget = "SafeDrinkingWater"
            checkAndLaunchCamera()
        }

        // desc of other areas
        view.findViewById<Button>(R.id.btnUploadProof).setOnClickListener {
            currentPhotoTarget = "proof"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadCirculationProof).setOnClickListener {
            currentPhotoTarget = "circulationProof"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadParkingProof).setOnClickListener {
            currentPhotoTarget = "parking"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadOpenSpaceProof).setOnClickListener {
            currentPhotoTarget = "openSpaceProof"
            checkAndLaunchCamera()
        }



        // Common Equipment Upload Buttons
        view.findViewById<Button>(R.id.btnUploadPowerBackup).setOnClickListener {
            currentPhotoTarget = "powerBackup"
            checkAndLaunchCamera()
        }

        view.findViewById<Button>(R.id.btnUploadBiometricDevices).setOnClickListener {
            currentPhotoTarget = "biometricDevices"
            checkAndLaunchCamera()
        }

        view.findViewById<Button>(R.id.btnUploadCCTV).setOnClickListener {
            currentPhotoTarget = "cctv"
            checkAndLaunchCamera()
        }

        view.findViewById<Button>(R.id.btnUploadDocumentStorage).setOnClickListener {
            currentPhotoTarget = "documentStorage"
            checkAndLaunchCamera()
        }

        view.findViewById<Button>(R.id.btnUploadPrinterScanner).setOnClickListener {
            currentPhotoTarget = "printerScanner"
            checkAndLaunchCamera()
        }

        view.findViewById<Button>(R.id.btnUploadDigitalCamera).setOnClickListener {
            currentPhotoTarget = "digitalCamera"
            checkAndLaunchCamera()
        }

        view.findViewById<Button>(R.id.btnUploadGrievanceRegister).setOnClickListener {
            currentPhotoTarget = "grievanceRegister"
            checkAndLaunchCamera()
        }

        view.findViewById<Button>(R.id.btnUploadMinimumEquipment).setOnClickListener {
            currentPhotoTarget = "minimumEquipment"
            checkAndLaunchCamera()
        }

        view.findViewById<Button>(R.id.btnUploadDirectionBoards).setOnClickListener {
            currentPhotoTarget = "irectionBoards"
            checkAndLaunchCamera()
        }

        // Submit buttons
        view.findViewById<Button>(R.id.btnSubmitCCTVComplianceDetails).setOnClickListener {
            if (validateCCTVForm(view)) submitCCTVData(view)
            else Toast.makeText(
                requireContext(),
                "Complete all CCTV fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitElectricalDetails)?.setOnClickListener {
            if (validateElectricalForm(view)) submitElectricalData(view)
            else Toast.makeText(
                requireContext(),
                "Complete all electrical fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitGeneralDetails).setOnClickListener {
            if (validateGeneralDetailsForm()) submitGeneralDetails()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields and photos for General Details.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitTCeDetails).setOnClickListener {
            if (validateTcBasicInfoFields()) submitTCInfoDeatails()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for TC Details.",
                Toast.LENGTH_LONG
            ).show()

        }
        view.findViewById<Button>(R.id.btnSignagesInfoDetails).setOnClickListener {
            if (validateSignagesInfoBoards()) submitSignagesInfoBoards()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Signages&InfoBoards Details.",
                Toast.LENGTH_LONG
            ).show()

        }
        view.findViewById<Button>(R.id.btnSubmitSupportInfrastructure).setOnClickListener {
            if (validateSupportInfrastructure()) submitInfraDetails()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Availabilty of support InfraStructure Details.",
                Toast.LENGTH_LONG
            ).show()
        }
        //17th  sept
        view.findViewById<Button>(R.id.btnCommonEquipmentDetails).setOnClickListener {
            if (validateCommonEquipment()) submitCommonEquipment()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Common Equipment Details.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitDescOtherArea).setOnClickListener {
            if (validateDescriptionForm()) submitDescriptionOtherAreas()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Description of other area Details.",
                Toast.LENGTH_LONG
            ).show()
        }
        // Observers
        viewModel.insertCCTVdata.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "CCTV data submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    "CCTV submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        viewModel.insertIpenabledata.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "Electrical data submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    "Electrical submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        viewModel.insertGeneralDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "General details submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    "General details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        viewModel.insertTCInfoDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "Training details submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    "Training details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        viewModel.insertSignagesInfoBoardsDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "Signages&InfoBoards details submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    "Signages&InfoBoards details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        viewModel.insertSupportInfraDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "Support Infrastructure details submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    "Support Infrastructure details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
        viewModel.insertCommonEquipDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "Common equipment details submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    " Common equipment details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
        viewModel.insertDescAreaDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "Description of other area details submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    " Description of other area  details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }
    private fun setupExpandableSections(view: View) {
        val sections = listOf(
            Triple(R.id.headerTCBasicInfo, R.id.layoutTCBasicInfoContent, R.id.ivToggleTCBasicInfo),
            /*Triple(
                R.id.headerInfrastructure,
                R.id.layoutInfrastructureContent,
                R.id.ivToggleInfrastructure
            ),*/
            Triple(
                R.id.headerCCTVCompliance,
                R.id.layoutCCTVComplianceContent,
                R.id.ivToggleCCTVCompliance
            ),
            Triple(
                R.id.headerElectricalWiring,
                R.id.layoutElectricalWiringContent,
                R.id.ivToggleElectricalWiring
            ),
            Triple(
                R.id.headerGeneralDetails,
                R.id.layoutGeneralDetailsContent,
                R.id.ivToggleGeneralDetails
            ),
            Triple(
                R.id.headerCommonEquipment,
                R.id.layoutCommonEquipmentContent,
                R.id.ivToggleCommonEquipment
            ),
            Triple(
                R.id.headerSignagesInfoBoards,
                R.id.layoutSignagesInfoBoardsContent,
                R.id.ivToggleSignagesInfoBoards
            ),
           /* Triple(
                R.id.headerAvailableTrainers,
                R.id.layoutAvailableTrainersContent,
                R.id.ivToggleAvailableTrainers
            ),*/
            /*Triple(
                R.id.headerTeachingLearningMaterials,
                R.id.layoutTeachingLearningMaterialsContent,
                R.id.ivToggleTeachingLearningMaterials
            ),*/
            Triple(
                R.id.headerDescriptionOtherAreas,
                R.id.layoutDescriptionOtherAreasContent,
                R.id.ivToggleDescriptionOtherAreas
            ),
            Triple(
                R.id.headerToiletsWashBasins,
                R.id.layoutToiletsWashBasinsContent,
                R.id.ivToggleToiletsWashBasins
            ),
            Triple(
                R.id.headerSupportInfrastructure,
                R.id.layoutSupportInfrastructureContent,
                R.id.ivToggleSupportInfrastructure
            ),
            Triple(
                R.id.headerCommonEquipment,
                R.id.layoutCommonEquipmentContent,
                R.id.ivToggleCommonEquipment
            ),


            )
        val expansionStates = MutableList(sections.size) { false }

        sections.forEachIndexed { index, (headerId, contentId, iconId) ->
            val header = view.findViewById<LinearLayout>(headerId)
            val content = view.findViewById<LinearLayout>(contentId)
            val icon = view.findViewById<ImageView>(iconId)

            header.setOnClickListener {
                expansionStates[index] = !expansionStates[index]
                content.visibility = if (expansionStates[index]) View.VISIBLE else View.GONE
                icon.setImageResource(
                    if (expansionStates[index]) R.drawable.outline_arrow_upward_24
                    else R.drawable.ic_dropdown_arrow
                )
            }
        }
    }

    private fun checkAndLaunchCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
            launchCamera()
        else permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun launchCamera() {
        val photoFile = createImageFile()
        if (photoFile == null) {
            Toast.makeText(requireContext(), "Failed to create image file", Toast.LENGTH_SHORT)
                .show()
            return
        }
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            photoFile
        )
        cameraLauncher.launch(photoUri)
    }

    private fun createImageFile(): File? {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return try {
            File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun validateCCTVForm(view: View): Boolean {
        val spinnerOk = listOf(
            R.id.spinnerMonitorAccessible,
            R.id.spinnerConformance,
            R.id.spinnerStorage,
            R.id.spinnerDVRStaticIP,
            R.id.spinnerIPEnabled,
            R.id.spinnerResolution,
            R.id.spinnerVideoStream,
            R.id.spinnerRemoteAccessBrowser,
            R.id.spinnerSimultaneousAccess,
            R.id.spinnerSupportedProtocols,
            R.id.spinnerColorVideoAudio,
            R.id.spinnerStorageFacility
        ).all {
            view.findViewById<Spinner>(it).selectedItem != "--Select--"
        }

        val photosOk = base64MonitorFile != null && base64ConformanceFile != null &&
                base64StorageFile != null && base64DVRFile != null
        return spinnerOk && photosOk
    }

    private fun validateElectricalForm(view: View): Boolean {
        val switchSelected =
            view.findViewById<Spinner>(R.id.spinnerSwitchBoards).selectedItem != "--Select--"
        val wireSelected =
            view.findViewById<Spinner>(R.id.spinnerSecuringWires).selectedItem != "--Select--"
        val photosOk = base64SwitchBoardImage != null && base64WireSecurityImage != null
        return switchSelected && wireSelected && photosOk
    }

    private fun validateGeneralDetailsForm(): Boolean {
        return spinnerLeakageCheck.selectedItem != "--Select--"
                && spinnerProtectionStairs.selectedItem != "--Select--"
                && spinnerDDUConformance.selectedItem != "--Select--"
                && spinnerCandidateSafety.selectedItem != "--Select--"
                && base64LeakageImage != null
                && base64StairsImage != null
    }

    private fun validateSupportInfrastructure(): Boolean {
        var isValid = true

        // Helper function to check spinner selection
        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            return if (spinner.selectedItemPosition == 0) {
                spinner.requestFocus()
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT)
                    .show()
                false
            } else {
                true
            }
        }

        // Helper function to check EditText input
        fun checkEditText(editText: EditText, fieldName: String): Boolean {
            return if (editText.text.toString().trim().isEmpty()) {
                editText.error = "Please enter $fieldName"
                editText.requestFocus()
                false
            } else {
                true
            }
        }
        // Validate First Aid Kit spinner
        if (!checkSpinner(spinnerFirstAidKit, "First Aid Kit")) isValid = false

        // Validate Safe Drinking Water spinner
        if (!checkSpinner(spinnerSafeDrinkingWater, "Safe Drinking Water")) isValid = false

        // Validate Fire Fighting Equipment EditText
        if (!checkEditText(editFireFightingEquipment, "Fire Fighting Equipment")) isValid = false

        return isValid
    }

    private fun validateTcBasicInfoFields(): Boolean {
        var isValid = true

        val distanceBus = requireView().findViewById<TextInputEditText>(R.id.etDistanceBusStand)
        val distanceAuto = requireView().findViewById<TextInputEditText>(R.id.etDistanceAutoStand)
        val distanceRailway =
            requireView().findViewById<TextInputEditText>(R.id.etDistanceRailwayStation)
        val latitude = requireView().findViewById<TextInputEditText>(R.id.etLatitude)
        val longitude = requireView().findViewById<TextInputEditText>(R.id.etLongitude)

        // Helper to validate one field
        fun validateField(field: TextInputEditText, fieldName: String): Boolean {
            return if (field.text.isNullOrBlank()) {
                field.error = "$fieldName cannot be empty"
                false
            } else {
                field.error = null
                true
            }
        }
        isValid = validateField(distanceBus, "Distance from Bus Stand") && isValid
        isValid = validateField(distanceAuto, "Distance from Auto Stand") && isValid
        isValid = validateField(distanceRailway, "Distance from Railway Station") && isValid
        isValid = validateField(latitude, "Latitude") && isValid
        isValid = validateField(longitude, "Longitude") && isValid


        return isValid
    }

    private fun validateCommonEquipment(): Boolean {
        var isValid = true

        // Helper function to check spinner selection
        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            return if (spinner.selectedItemPosition == 0) {
                spinner.requestFocus()
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT)
                    .show()
                false
            } else {
                true
            }
        }

        // Helper function to check TextInputEditText input
        fun checkTextInput(editText: TextInputEditText, fieldName: String): Boolean {
            return if (editText.text.isNullOrBlank()) {
                editText.error = "Please enter $fieldName"
                editText.requestFocus()
                false
            } else {
                true
            }
        }

        // Validate all required Spinners
        if (!checkSpinner(spinnerPowerBackup, "Electrical Power Backup")) isValid = false
        if (!checkSpinner(spinnerCCTV, "Installation of CCTV Monitor")) isValid = false
        if (!checkSpinner(spinnerDocumentStorage, "Storage Place for Securing Documents")) isValid =
            false
        if (!checkSpinner(spinnerGrievanceRegister, "Grievance Register")) isValid = false
        if (!checkSpinner(spinnerMinimumEquipment, "Minimum Equipment as per SF 5.1P")) isValid =
            false
        if (!checkSpinner(spinnerDirectionBoards, "Direction Boards")) isValid = false

        // Validate required TextInputEditTexts
        if (!checkTextInput(etBiometricDevices, "Biometric Devices details")) isValid = false
        if (!checkTextInput(etPrinterScanner, "Printer Cum Scanner number")) isValid = false
        if (!checkTextInput(etDigitalCamera, "Digital Camera number")) isValid = false

        return isValid
    }

    private fun validateSignagesInfoBoards(): Boolean {
        var isValid = true

        // Helper function to check spinner selection
        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            return if (spinner.selectedItemPosition == 0) {
                spinner.requestFocus()
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT)
                    .show()
                false
            } else {
                true
            }
        }

        if (!checkSpinner(spinnerTcNameBoard, "Training Centre Name Board")) isValid = false
        if (!checkSpinner(spinnerActivityAchievementBoard, "Activity Summary Board")) isValid =
            false
        if (!checkSpinner(spinnerStudentEntitlementBoard, "Student Entitlement Board")) isValid =
            false
        if (!checkSpinner(spinnerContactDetailBoard, "Contact Detail Board")) isValid = false
        if (!checkSpinner(spinnerBasicInfoBoard, "Basic Info Board")) isValid = false
        if (!checkSpinner(spinnerCodeConductBoard, "Code of Conduct Board")) isValid = false
        if (!checkSpinner(spinnerStudentAttendanceBoard, "Attendance Summary Board")) isValid =
            false

        return isValid
    }
    private fun validateDescriptionForm(): Boolean {
        var isValid = true

        // Helper to check if a TextInputEditText is empty
        fun checkEmpty(editText: TextInputEditText, fieldName: String): Boolean {
            val text = editText.text?.toString()?.trim()
            return if (text.isNullOrEmpty()) {
                editText.error = "$fieldName is required"
                false
            } else {
                editText.error = null
                true
            }
        }
        // Validate all required fields
        isValid = checkEmpty(etCorridorNo, "Corridor No") && isValid
        isValid = checkEmpty(etDescLength, "Length") && isValid
        isValid = checkEmpty(etDescWidth, "Width") && isValid
        isValid = checkEmpty(etArea, "Area") && isValid
        isValid = checkEmpty(etLights, "Lights") && isValid
        isValid = checkEmpty(etFans, "Fans") && isValid
        isValid = checkEmpty(etCirculationArea, "Circulation Area") && isValid
        isValid = checkEmpty(etOpenSpace, "Open Space") && isValid
        isValid = checkEmpty(etExclusiveParkingSpace, "Exclusive Parking Space") && isValid
        return isValid
    }


    private fun submitCCTVData(view: View) {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val request = CCTVComplianceRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            centralMonitor = view.findViewById<Spinner>(R.id.spinnerMonitorAccessible).selectedItem.toString(),
            centralMonitorFile = base64MonitorFile ?: "",
            cctvConformance = view.findViewById<Spinner>(R.id.spinnerConformance).selectedItem.toString(),
            cctvConformanceFile = base64ConformanceFile ?: "",
            cctvStorage = view.findViewById<Spinner>(R.id.spinnerStorage).selectedItem.toString(),
            cctvStorageFile = base64StorageFile ?: "",
            dvrStaticIp = view.findViewById<Spinner>(R.id.spinnerDVRStaticIP).selectedItem.toString(),
            dvrStaticIpFile = base64DVRFile ?: "",
            ipEnabled = view.findViewById<Spinner>(R.id.spinnerIPEnabled).selectedItem.toString(),
            resolution = view.findViewById<Spinner>(R.id.spinnerResolution).selectedItem.toString(),
            videoStream = view.findViewById<Spinner>(R.id.spinnerVideoStream).selectedItem.toString(),
            remoteAccessBrowser = view.findViewById<Spinner>(R.id.spinnerRemoteAccessBrowser).selectedItem.toString(),
            simultaneousAccess = view.findViewById<Spinner>(R.id.spinnerSimultaneousAccess).selectedItem.toString(),
            supportedProtocols = view.findViewById<Spinner>(R.id.spinnerSupportedProtocols).selectedItem.toString(),
            colorVideoAudio = view.findViewById<Spinner>(R.id.spinnerColorVideoAudio).selectedItem.toString(),
            storageFacility = view.findViewById<Spinner>(R.id.spinnerStorageFacility).selectedItem.toString(),
        )
        viewModel.submitCCTVDataToServer(request, token)
    }

    private fun submitElectricalData(view: View) {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val request = ElectricalWiringRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            switchBoard = view.findViewById<Spinner>(R.id.spinnerSwitchBoards).selectedItem.toString(),
            switchBoardImage = base64SwitchBoardImage ?: "",
            wireSecurity = view.findViewById<Spinner>(R.id.spinnerSecuringWires).selectedItem.toString(),
            wireSecurityImage = base64WireSecurityImage ?: "",
            tcId = centerId,
            sanctionOrder = AppUtil.getSavedSanctionOrder(requireContext())
        )
        viewModel.submitElectricalData(request, token)
    }

    private fun submitGeneralDetails() {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val request = InsertTcGeneralDetailsRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            signLeakages = spinnerLeakageCheck.selectedItem.toString(),
            signLeakagesImage = base64LeakageImage ?: "",
            stairsProtection = spinnerProtectionStairs.selectedItem.toString(),
            stairsProtectionImage = base64StairsImage ?: "",
            dduConformance = spinnerDDUConformance.selectedItem.toString(),
            centerSafety = spinnerCandidateSafety.selectedItem.toString(),
            tcId = centerId,
            sanctionOrder = AppUtil.getSavedSanctionOrder(requireContext())
        )
        viewModel.submitGeneralDetails(request, token)

    }

    private fun submitTCInfoDeatails() {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val distanceBus =
            view?.findViewById<TextInputEditText>(R.id.etDistanceBusStand)?.text.toString()
        val distanceAuto =
            view?.findViewById<TextInputEditText>(R.id.etDistanceAutoStand)?.text.toString()
        val distanceRailway =
            view?.findViewById<TextInputEditText>(R.id.etDistanceRailwayStation)?.text.toString()
        val latitude = view?.findViewById<TextInputEditText>(R.id.etLatitude)?.text.toString()
        val longitude = view?.findViewById<TextInputEditText>(R.id.etLongitude)?.text.toString()

        // For geoAddress, assuming you have an EditText or a way to get this value
        val geoAddress =
            "Jeevan Bharti Building Delhi - 110001"  // Replace with actual input if available

        val request = TcBasicInfoRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder = AppUtil.getSavedSanctionOrder(requireContext()),
            distanceBus = distanceBus,
            distanceAuto = distanceAuto,
            distanceRailway = distanceRailway,
            latitude = latitude,
            longitude = longitude,
            geoAddress = geoAddress

        )
        viewModel.submitTcBasicDataToServer(request, token)
    }

    //Rohit submitSignagesInfoDetails
    private fun submitSignagesInfoBoards() {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val trainingCentreNameBoard = spinnerTcNameBoard.selectedItem.toString()
        val activitySummaryBoard = spinnerActivityAchievementBoard.selectedItem.toString()
        val entitlementBoard = spinnerStudentEntitlementBoard.selectedItem.toString()
        val importantContacts = spinnerContactDetailBoard.selectedItem.toString()
        val basicInfoBoard = spinnerBasicInfoBoard.selectedItem.toString()
        val codeOfConductBoard = spinnerCodeConductBoard.selectedItem.toString()
        val attendanceSummaryBoard = spinnerStudentAttendanceBoard.selectedItem.toString()

        val request = TcSignagesInfoBoardRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder = AppUtil.getSavedSanctionOrder(requireContext()),

            tcNameBoard = trainingCentreNameBoard,
            tcNameBoardImage = base64TcNameBoardImage ?: "",

            activityAchievmentBoard = activitySummaryBoard,
            activityAchievmentBoardImage = base64ActivityAchievementBoardImage ?: "",

            studentEntitlementResponsibilityBoard = entitlementBoard,
            studentEntitlementResponsibilityBoardImage = base64StudentEntitlementBoardImage ?: "",

            contactDetailImpPeople = importantContacts,
            contactDetailImpPeopleImage = base64ContactDetailBoardImage ?: "",

            basicInfoBoard = basicInfoBoard,
            basicInfoBoardImage = base64BasicInfoBoardImage ?: "",

            codeConductBoard = codeOfConductBoard,
            codeConductBoardImage = base64CodeConductBoardImage ?: "",

            studentAttendanceEntitlementBoard = attendanceSummaryBoard,
            studentAttendanceEntitlementBoardImage = base64StudentAttendanceBoardImage ?: ""
        )
        viewModel.submitTcInfoSignageDataToServer(request, token)
    }

    private fun submitInfraDetails() {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""
        val firstAidKit = spinnerFirstAidKit.selectedItem.toString()
        val safeDrinkingWater = spinnerSafeDrinkingWater.selectedItem.toString()
        val fireFightingEquipment =
            view?.findViewById<TextInputEditText>(R.id.editFireFightingEquipment)?.text.toString()

        val request = TcAvailabilitySupportInfraRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId =centerId,
            sanctionOrder = AppUtil.getSavedSanctionOrder(requireContext()),
            drinkingWater = safeDrinkingWater,
            drinkingWaterImage = base64SafeDrinkingWater ?: "",
            fireFightingEquipment = fireFightingEquipment,
            fireFightingEquipmentImage = base64FireFightingEquipment ?: "",
            firstAidKit = firstAidKit,
            firstAidKitImage = base64FirstAidKit ?: "",
        )
        viewModel.submitTcSupportInfraDataToserver(request, token)
    }

    private fun submitCommonEquipment() {
        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val electricalPowerBackup = spinnerPowerBackup.selectedItem.toString()
        val biometricDevicesDetails = etBiometricDevices.text.toString()
        val cctvInstallation = spinnerCCTV.selectedItem.toString()
        val documentStorage = spinnerDocumentStorage.selectedItem.toString()
        val printerScannerCount = etPrinterScanner.text.toString()
        val digitalCameraCount = etDigitalCamera.text.toString()
        val grievanceRegister = spinnerGrievanceRegister.selectedItem.toString()
        val minimumEquipment = spinnerMinimumEquipment.selectedItem.toString()
        val directionBoards = spinnerDirectionBoards.selectedItem.toString()

        val request = TcCommonEquipmentRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder = AppUtil.getSavedSanctionOrder(requireContext()),

            ecPowerBackup = electricalPowerBackup,
            ecPowerBackupImage = base64PowerBackupImage ?: "",

            biometricDeviceInstall = biometricDevicesDetails,
            biometricDeviceInstallImage = base64BiometricDevices ?: "",

            cctvMonitorInstall = cctvInstallation,
            cctvMonitorInstallImage = base64CCTVImage ?: "",

            documentStorageSecuring = documentStorage,
            documentStorageSecuringImage = base64DocumentStorageImage ?: "",

            printerScanner = printerScannerCount,
            printerScannerlImage = base64PrinterScanner ?: "",

            digitalCamera = digitalCameraCount,
            digitalCameraImage = base64DigitalCamera ?: "",

            grievanceRegister = grievanceRegister,
            grievanceRegisterImage = base64GrievanceRegisterImage ?: "",

            minimumEquipment = minimumEquipment,
            minimumEquipmentImage = base64MinimumEquipmentImage ?: "",

            directionBoards = directionBoards,
            directionBoardsImage = base64DirectionBoardsImage ?: ""
        )

        viewModel.submitTcCommonEquipment(request, token)
    }

    private fun submitDescriptionOtherAreas() {
        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val corridorNo = etCorridorNo.text.toString()
        val descLength = etDescLength.text.toString()
        val descWidth = etDescWidth.text.toString()
        val area = etArea.text.toString()
        val lights = etLights.text.toString()
        val fans = etFans.text.toString()
        val circulationArea = etCirculationArea.text.toString()
        val openSpace = etOpenSpace.text.toString()
        val exclusiveParkingSpace = etExclusiveParkingSpace.text.toString()

        val request = TcDescriptionOtherAreasRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder = AppUtil.getSavedSanctionOrder(requireContext()),
            corridorNo = corridorNo,
            length = descLength,
            width = descWidth,
            areas = area,
            lights = lights,
            fans = fans,
            circulationArea = circulationArea,
            circulationAreaImage = base64CirculationProofImage ?: "",
            openSpace = openSpace,
            openSpaceImage = base64OpenSpaceProofImage ?: "",
            parkingSpace = exclusiveParkingSpace,
            parkingSpaceImage = base64ParkingSpaceProofImage ?: "",
            proofImage = base64ProofUploadImage?:"",
        )

        viewModel.submitTcDescriptionArea(request, token)
    }


}
