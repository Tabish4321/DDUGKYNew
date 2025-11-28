package com.deendayalproject.fragments

import SharedViewModel
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentSrlmverificatiomFormBinding
import com.deendayalproject.databinding.DescriptionAcademiaLayoutBinding
import com.deendayalproject.databinding.ItemTrainerStaffBinding
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.response.Trainer
import com.deendayalproject.util.AppUtil
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.deendayalproject.databinding.*
import com.deendayalproject.model.request.AllRoomDetaisReques
import com.deendayalproject.model.request.TcQTeamInsertReq
import com.deendayalproject.model.response.RoomDetail
import com.deendayalproject.model.response.RoomItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class SrlmVerificationForm : BaseFragment<FragmentSrlmverificatiomFormBinding>(
    FragmentSrlmverificatiomFormBinding::inflate
) {
    private lateinit var viewModel: SharedViewModel
    var dataStaffList: MutableList<Trainer> = mutableListOf()
    var academiaList: MutableList<RoomItem> = mutableListOf()

    private val approvalList = listOf("Approved", "Send for modification")
    private lateinit var tcInfoAdapter: ArrayAdapter<String>
    private var selectedTcInfoApproval = ""
    private var selectedTcInfoRemarks = ""

    private lateinit var tcDescAcademiaAdapter: ArrayAdapter<String>
    private var selectedTcDescAcademiaApproval = ""
    private var selectedTcDescAcademiaRemarks = ""

    private lateinit var roomDetails: RoomDetail

    private lateinit var tcInfraAdapter: ArrayAdapter<String>
    private var selectedTcInfraApproval = ""
    private var selectedTcInfraRemarks = ""

    private lateinit var tcBasinAdapter: ArrayAdapter<String>
    private var selectedTcBasinApproval = ""
    private var selectedTcBasinRemarks = ""

    private lateinit var tcDescOtherAreaAdapter: ArrayAdapter<String>
    private var selectedTcDescOtherAreaApproval = ""
    private var selectedTcDescOtherAreaRemarks = ""

    private lateinit var tcTeachingAdapter: ArrayAdapter<String>
    private var selectedTcTeachingApproval = ""
    private var selectedTcTeachingRemarks = ""

    private lateinit var tcGeneralAdapter: ArrayAdapter<String>
    private var selectedTcGeneralApproval = ""
    private var selectedTcGeneralRemarks = ""

    private lateinit var tcElectricalAdapter: ArrayAdapter<String>
    private var selectedTcElectricalApproval = ""
    private var selectedTcElectricalRemarks = ""

    private lateinit var tcSignageAdapter: ArrayAdapter<String>
    private var selectedTcSignageApproval = ""
    private var selectedTcSignageRemarks = ""

    private lateinit var tcIpEnableAdapter: ArrayAdapter<String>
    private var selectedTcIpEnableApproval = ""
    private var selectedTcIpEnableRemarks = ""

    private lateinit var tcCommonEquipmentAdapter: ArrayAdapter<String>
    private var selectedTcCommonEquipmentApproval = ""
    private var selectedTcCommonEquipmentRemarks = ""

    private lateinit var tcAvailSupportInfraAdapter: ArrayAdapter<String>
    private var selectedTcAvailSupportInfraApproval = ""
    private var selectedTcAvailSupportInfraRemarks = ""

    private lateinit var tcAvailOfStandardFormAdapter: ArrayAdapter<String>
    private var selectedTcAvailOfStandardFormApproval = ""
    private var selectedTcAvailOfStandardFormRemarks = ""

    // All Room Variables
    private var fansRoomImage = ""
    private var writingBoard = ""
    private var internetConnectionImage = ""
    private var roomInfoBoardImage = ""
    private var digitalProjectorImage = ""
    private var officeComputer = ""
    private var printerScannerImage = ""
    private var centerSoundProof = ""
    private var falseCeiling = ""
    private var tablet = ""
    private var typingTuterCompImage = ""
    private var lanEnabledImage = ""
    private var internalSignageImage = ""
    private var airConditionRoom = ""
    private var roomsPhotographs = ""
    private var roomsPhotographsImage = ""
    private var audioCamera = ""
    private var lanEnabled = ""
    private var soundLevelImage = ""
    private var centerSoundProofImage = ""
    private var digitalCameraRoomImage = ""
    private var internetConnection = ""
    private var officeChair = ""
    private var officeTableImage = ""
    private var printerScanner = ""
    private var trainerChair = ""
    private var domainEquipmentImage = ""
    private var ecPowerBackup = ""
    private var tabletImage = ""
    private var soundLevel = ""
    private var trainerTable = ""
    private var falseCeilingImage = ""
    private var roomInfoBoard = ""
    private var roofTypeImage = ""
    private var digitalProjector = ""
    private var secureDocumentStorage = ""
    private var airConditionRoomImage = ""
    private var sounfLevelSpecific = ""
    private var ventilationArea = ""
    private var domainEquipment = ""
    private var officeTable = ""
    private var officeChairImage = ""
    private var typingTuterComp = ""
    private var ceilingHeightImage = ""
    private var candidateChair = ""
    private var candidateChairImage = ""
    private var ceilingHeight = ""
    private var lightsImage = ""
    private var secureDocumentStorageImage = ""
    private var writingBoardImage = ""
    private var lights = ""
    private var digitalCamera = ""
    private var audioCameraImage = ""
    private var internalSignage = ""
    private var trainerChairImage = ""
    private var ventilationAreaImage = ""
    private var roofType = ""
    private var trainerTableImage = ""
    private var fans = ""
    private var officeComputerImagePath = ""
    private var ecPowerBackupImage = ""

    private var centerId = ""
    private var sanctionOrder = ""
    private var centerName = ""

    private var selfDeclarationPdf = ""
    private var buildingPdf = ""
    private var schematicPdf = ""
    private var internalExternalWallPdf = ""

    private var maleToiletImage = ""
    private var maleToiletSignageImage = ""
    private var maleToiletUrinalsImage = ""
    private var maleToiletWashbasinImage = ""

    private var femaleToiletImage = ""
    private var femaleToiletSignageImage = ""
    private var femaleToiletWashbasinImage = ""
    private var ovrHeadTankImage = ""
    private var typeOfFlooringImage = ""

    private var fansImage = ""
    private var circulationAreaImage = ""
    private var openSpaceImage = ""
    private var parkingSpaceImage = ""
    private var welcomeKitImage = ""
    private var signOfLeakageImage = ""
    private var protectionStairsBalImage = ""
    private var securingWiringImage = ""
    private var switchBoardImage = ""

    var roomData: RoomDetail? = null
    private var tcNameBoardImage = ""
    private var activitySummaryBoardImage = ""
    private var studentEntitlementBoardImage = ""
    private var contactDetailImpoPeopleImage = ""
    private var basicInfoBoardImage = ""
    private var codeOfConductImage = ""
    private var studentAttendanceImage = ""
    private var centralMonitorImage = ""
    private var conformationOfCCTVImage = ""
    private var storageOfCCtvImage = ""
    private var dvrImage = ""

    private var electricPowerImage = ""
    private var installBiometricImage = ""
    private var installationCCTVImage = ""
    private var storagePlaceSecuringDocImage = ""
    private var printerCumImage = ""
    private var digitalCameraImage = ""
    private var grievanceImage = ""
    private var minimumEquipmentImage = ""
    private var directionBoardsImage = ""
    private var safeDrinkingImage = ""
    private var fireFightingImage = ""
    private var firstAidImage = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        centerId = arguments?.getString("centerId").toString()
        centerName = arguments?.getString("centerName").toString()
        sanctionOrder = arguments?.getString("sanctionOrder").toString()

        initializeViews()
        setupObservers()
        setupClickListeners()
        loadInitialData()
    }

    override fun initializeViews() {
        setupRecyclerViews()
        setupAdapters()
        setupInitialUIState()
    }

    override fun setupObservers() {
        collectTCInfoResponse()
        collectTCStaffResponse()
        collectTCInfraResponse()
        collectTCAcademiaNonAcademia()
        collectTCToiletAndWash()
        collectTCDescOtherArea()
        collectTCTeaching()
        collectTCGeneral()
        collectTCElectrical()
        collectTCSignage()
        collectTCIpEnabele()
        collectTCCommonEquipment()
        collectTCSupportInfra()
        collectTCStandardForms()
        collectAllRoomDetails()
        collectQTeamInsertRes()
    }

    override fun setupClickListeners() {
        setupAllClickListeners()
        setupNavigationListeners()
        setupApprovalSpinnerListeners()
        setupImageClickListeners()
        setupPdfClickListeners()
    }

    override fun loadInitialData() {
        // TrainingCenterInfo API
        val requestTcInfo = TrainingCenterInfo(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.getTrainerCenterInfo(requestTcInfo)

        // TrainingCenterStaffList API
        val requestStaffList = TrainingCenterInfo(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.getTcStaffDetails(requestStaffList)
    }

    // ==================== PRIVATE METHODS ====================

    private fun setupRecyclerViews() {
        // Setup Description Academia RecyclerView using BaseFragment
        setupRecyclerView(
            recyclerView = binding.recyclerView,
            items = academiaList,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = { inflater, parent, _ ->
                DescriptionAcademiaLayoutBinding.inflate(inflater, parent, false)
            },
            onBind = { room, itemBinding, position ->
                itemBinding.tvMaxCandidate.text = room.maxPermissibleCandidate
                itemBinding.tvLength.text = room.roomLength
                itemBinding.tvWidth.text = room.roomWidth
                itemBinding.tvArea.text = room.roomArea
                itemBinding.tvRoomType.text = room.roomType

                itemBinding.btnView.setOnClickListener {
                    handleRoomViewClick(room)
                }
            }
        )
    }


    private val approvalAdapters = mutableMapOf<TextView, ArrayAdapter<String>>()


    private fun setupApprovalSpinners() {
        val spinners = listOf(
            binding.trainingCenterInfoLayout.SpinnerTcInfo,
            binding.SpinnerDescAcademia,
            binding.SpinnerTcInfra,
            binding.SpinnerBasin,
            binding.SpinnerDescOtherArea,
            binding.SpinnerTeaching,
            binding.SpinnerGeneral,
            binding.SpinnerElectrical,
            binding.signageLayout.SpinnerSignage,
            binding.ipCameraLayout.SpinnerIpEnable,
            binding.commonEquipmentLayout.SpinnerCommonEquipment,
            binding.availSupportInfraLayout.SpinnerAvailSupportInfra,
            binding.availOfStandardFormsLayout.SpinnerAvailOfStandardForms
        )

        spinners.forEach { spinner ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
            spinner.setAdapter(adapter)
            approvalAdapters[spinner] = adapter
        }
    }




    private fun setupAdapters() {
        setupApprovalSpinners()
        // All approval adapters
//        tcInfoAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
//        binding.trainingCenterInfoLayout.SpinnerTcInfo.setAdapter(tcInfoAdapter)
//
//        tcDescAcademiaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
//        binding.SpinnerDescAcademia.setAdapter(tcDescAcademiaAdapter)
//
//        tcInfraAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
//        binding.SpinnerTcInfra.setAdapter(tcInfraAdapter)
//
//        tcBasinAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
//        binding.SpinnerBasin.setAdapter(tcBasinAdapter)
//
//        tcDescOtherAreaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
//        binding.SpinnerDescOtherArea.setAdapter(tcDescOtherAreaAdapter)
//
//        tcTeachingAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
//        binding.SpinnerTeaching.setAdapter(tcTeachingAdapter)
//
//        tcGeneralAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
//        binding.SpinnerGeneral.setAdapter(tcGeneralAdapter)
//
//        tcElectricalAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
//        binding.SpinnerElectrical.setAdapter(tcElectricalAdapter)
//
//        tcSignageAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
//        binding.signageLayout.SpinnerSignage.setAdapter(tcSignageAdapter)
//
//        tcIpEnableAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
//        binding.ipCameraLayout.SpinnerIpEnable.setAdapter(tcIpEnableAdapter)
//
//        tcCommonEquipmentAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
//        binding.commonEquipmentLayout.SpinnerCommonEquipment.setAdapter(tcCommonEquipmentAdapter)
//
//        tcAvailSupportInfraAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
//        binding.availSupportInfraLayout.SpinnerAvailSupportInfra.setAdapter(tcAvailSupportInfraAdapter)
//
//        tcAvailOfStandardFormAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
//        binding.availOfStandardFormsLayout.SpinnerAvailOfStandardForms.setAdapter(tcAvailOfStandardFormAdapter)
    }

    private fun setupInitialUIState() {
        binding.root.setOnTouchListener { v, event ->
            AppUtil.hideKeyboard(requireActivity())
            v.performClick()
            false
        }
    }

    private fun setupAllClickListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.trainingCenterInfoLayout.tvViewTrainerAndStaff.setOnClickListener {
            showTrainerStaffDialog()
        }

        // Setup all navigation buttons
        setupNavigationButtonListeners()
    }

    private fun setupNavigationButtonListeners() {
        // Info Next
        binding.trainingCenterInfoLayout.btnInfoNext.setOnClickListener {
            if (validateApproval(selectedTcInfoApproval, selectedTcInfoRemarks,
                    binding.trainingCenterInfoLayout.etInfoRemarks.text.toString())) {
                selectedTcInfoRemarks = if (selectedTcInfoApproval == "Send for modification")
                    binding.trainingCenterInfoLayout.etInfoRemarks.text.toString() else ""

                navigateToNextSection(
                    currentSection = binding.trainingCenterInfoLayout.trainingInfoExpand,
                    currentView = binding.trainingCenterInfoLayout.viewInfo,
                    currentTextView = binding.trainingCenterInfoLayout.tvTrainInfo,
                    nextSection = binding.mainInfra,
                    nextView = binding.viewInfra
                )

                // Load infra data
                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )
                viewModel.getTrainerCenterInfra(requestTcInfraReq)
            }
        }

        // Infra Next
        binding.btnInfraNext.setOnClickListener {
            if (validateApproval(selectedTcInfraApproval, selectedTcInfraRemarks,
                    binding.etInfraRemarks.text.toString())) {
                selectedTcInfraRemarks = if (selectedTcInfraApproval == "Send for modification")
                    binding.etInfraRemarks.text.toString() else ""

                navigateToNextSection(
                    currentSection = binding.trainingInfraExpand,
                    currentView = binding.viewInfra,
                    currentTextView = binding.tvTrainInfra,
                    nextSection = binding.mainDescAcademia,
                    nextView = binding.viewDescAcademia
                )

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )
                viewModel.getTcAcademicNonAcademicArea(requestTcInfraReq)
            }
        }

        // Desc Academia Next
        binding.btnDescAcademiaNext.setOnClickListener {
            if (validateApproval(selectedTcDescAcademiaApproval, selectedTcDescAcademiaRemarks,
                    binding.etDescAcademiaRemarks.text.toString())) {
                selectedTcDescAcademiaRemarks = if (selectedTcDescAcademiaApproval == "Send for modification")
                    binding.etDescAcademiaRemarks.text.toString() else ""

                navigateToNextSection(
                    currentSection = binding.trainingDescAcademiaExpand,
                    currentView = binding.viewDescAcademia,
                    currentTextView = binding.tvTrainDescAcademia,
                    nextSection = binding.mainToilet,
                    nextView = binding.viewToilet
                )

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )
                viewModel.getTcToiletWashBasin(requestTcInfraReq)
            }
        }

        // Basin Next
        binding.btnBasinNext.setOnClickListener {
            if (validateApproval(selectedTcBasinApproval, selectedTcBasinRemarks,
                    binding.etBasinRemarks.text.toString())) {
                selectedTcBasinRemarks = if (selectedTcBasinApproval == "Send for modification")
                    binding.etBasinRemarks.text.toString() else ""

                navigateToNextSection(
                    currentSection = binding.trainingToiletExpand,
                    currentView = binding.viewToilet,
                    currentTextView = binding.tvTrainToilet,
                    nextSection = binding.mainDescOfOtherArea,
                    nextView = binding.viewDescOfOtherArea
                )

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )
                viewModel.getDescriptionOtherArea(requestTcInfraReq)
            }
        }

        // Desc Other Area Next
        binding.btnDescOtherAreaNext.setOnClickListener {
            if (validateApproval(selectedTcDescOtherAreaApproval, selectedTcDescOtherAreaRemarks,
                    binding.etDescOtherAreaRemarks.text.toString())) {
                selectedTcDescOtherAreaRemarks = if (selectedTcDescOtherAreaApproval == "Send for modification")
                    binding.etDescOtherAreaRemarks.text.toString() else ""

                navigateToNextSection(
                    currentSection = binding.trainingDescOfOtherAreaExpand,
                    currentView = binding.viewDescOfOtherArea,
                    currentTextView = binding.tvTrainDescOfOtherArea,
                    nextSection = binding.mainTeaching,
                    nextView = binding.viewTeaching
                )

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )
                viewModel.getTeachingLearningMaterial(requestTcInfraReq)
            }
        }

        // Teaching Next
        binding.btnTeachingNext.setOnClickListener {
            if (validateApproval(selectedTcTeachingApproval, selectedTcTeachingRemarks,
                    binding.etTeachingRemarks.text.toString())) {
                selectedTcTeachingRemarks = if (selectedTcTeachingApproval == "Send for modification")
                    binding.etTeachingRemarks.text.toString() else ""

                navigateToNextSection(
                    currentSection = binding.trainingTeachingExpand,
                    currentView = binding.viewTeaching,
                    currentTextView = binding.tvTrainTeaching,
                    nextSection = binding.mainGeneralDetails,
                    nextView = binding.viewGeneralDetails
                )

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )
                viewModel.getGeneralDetails(requestTcInfraReq)
            }
        }

        // General Next
        binding.btnGeneralNext.setOnClickListener {
            if (validateApproval(selectedTcGeneralApproval, selectedTcGeneralRemarks,
                    binding.etGeneralRemarks.text.toString())) {
                selectedTcGeneralRemarks = if (selectedTcGeneralApproval == "Send for modification")
                    binding.etGeneralRemarks.text.toString() else ""

                navigateToNextSection(
                    currentSection = binding.trainingGeneralDetailsExpand,
                    currentView = binding.viewGeneralDetails,
                    currentTextView = binding.tvTrainGeneralDetails,
                    nextSection = binding.mainElectricalDetails,
                    nextView = binding.viewElectricalDetails
                )

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )
                viewModel.getElectricalWiringStandard(requestTcInfraReq)
            }
        }

        // Electrical Next
        binding.btnElectricalNext.setOnClickListener {
            if (validateApproval(selectedTcElectricalApproval, selectedTcElectricalRemarks,
                    binding.etElectricalRemarks.text.toString())) {
                selectedTcElectricalRemarks = if (selectedTcElectricalApproval == "Send for modification")
                    binding.etElectricalRemarks.text.toString() else ""

                navigateToNextSection(
                    currentSection = binding.trainingElectricalDetailsExpand,
                    currentView = binding.viewElectricalDetails,
                    currentTextView = binding.tvTrainElectricalDetails,
                    nextSection = binding.mainSignageBoardDetails,
                    nextView = binding.signageLayout.viewSignageBoardDetails
                )

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )
                viewModel.getSignagesAndInfoBoard(requestTcInfraReq)
            }
        }

        // Signage Next
        binding.signageLayout.btnSignageNext.setOnClickListener {
            if (validateApproval(selectedTcSignageApproval, selectedTcSignageRemarks,
                    binding.signageLayout.etSignageRemarks.text.toString())) {
                selectedTcSignageRemarks = if (selectedTcSignageApproval == "Send for modification")
                    binding.signageLayout.etSignageRemarks.text.toString() else ""

                navigateToNextSection(
                    currentSection = binding.signageLayout.trainingSignageBoardlDetailsExpand,
                    currentView = binding.signageLayout.viewSignageBoardDetails,
                    currentTextView = binding.signageLayout.tvTrainSignageBoardDetails,
                    nextSection = binding.mainIPEnableCameraDetails,
                    nextView = binding.ipCameraLayout.viewIPEnableCameraDetails
                )

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )
                viewModel.getIpEnabledCamera(requestTcInfraReq)
            }
        }

        // IP Enable Next
        binding.ipCameraLayout.btnIpEnableNext.setOnClickListener {
            if (validateApproval(selectedTcIpEnableApproval, selectedTcIpEnableRemarks,
                    binding.ipCameraLayout.etIpEnableRemarks.text.toString())) {
                selectedTcIpEnableRemarks = if (selectedTcIpEnableApproval == "Send for modification")
                    binding.ipCameraLayout.etIpEnableRemarks.text.toString() else ""

                navigateToNextSection(
                    currentSection = binding.ipCameraLayout.trainingIPEnableCameralDetailsExpand,
                    currentView = binding.ipCameraLayout.viewIPEnableCameraDetails,
                    currentTextView = binding.ipCameraLayout.tvTrainIPEnableCameraDetails,
                    nextSection = binding.mainCommonEquipmentDetails,
                    nextView = binding.commonEquipmentLayout.viewCommonEquipmentDetails
                )

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )
                viewModel.getCommonEquipment(requestTcInfraReq)
            }
        }

        // Common Equipment Next
        binding.commonEquipmentLayout.btnCommonEquipmentNext.setOnClickListener {
            if (validateApproval(selectedTcCommonEquipmentApproval, selectedTcCommonEquipmentRemarks,
                    binding.commonEquipmentLayout.etCommonEquipmentRemarks.text.toString())) {
                selectedTcCommonEquipmentRemarks = if (selectedTcCommonEquipmentApproval == "Send for modification")
                    binding.commonEquipmentLayout.etCommonEquipmentRemarks.text.toString() else ""

                navigateToNextSection(
                    currentSection = binding.commonEquipmentLayout.trainingCommonEquipmentDetailsExpand,
                    currentView = binding.commonEquipmentLayout.viewCommonEquipmentDetails,
                    currentTextView = binding.commonEquipmentLayout.tvTrainCommonEquipmentDetails,
                    nextSection = binding.mainAvailSupportInfra,
                    nextView = binding.availSupportInfraLayout.viewAvailSupportInfra
                )

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )
                viewModel.getAvailabilitySupportInfra(requestTcInfraReq)
            }
        }

        // Avail Support Infra Next
        binding.availSupportInfraLayout.btnAvailSupportInfraNext.setOnClickListener {
            if (validateApproval(selectedTcAvailSupportInfraApproval, selectedTcAvailSupportInfraRemarks,
                    binding.availSupportInfraLayout.etAvailSupportInfraRemarks.text.toString())) {
                selectedTcAvailSupportInfraRemarks = if (selectedTcAvailSupportInfraApproval == "Send for modification")
                    binding.availSupportInfraLayout.etAvailSupportInfraRemarks.text.toString() else ""

                navigateToNextSection(
                    currentSection = binding.availSupportInfraLayout.trainingAvailSupportInfraExpand,
                    currentView = binding.availSupportInfraLayout.viewAvailSupportInfra,
                    currentTextView = binding.availSupportInfraLayout.tvTrainAvailSupportInfra,
                    nextSection = binding.mainAvailOfStandardForms,
                    nextView = binding.availOfStandardFormsLayout.viewAvailOfStandardForms
                )

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )
                viewModel.getAvailabilityStandardForms(requestTcInfraReq)
            }
        }

        // Avail Of Standard Forms Next
        binding.availOfStandardFormsLayout.btnAvailOfStandardFormsNext.setOnClickListener {
            if (validateApproval(selectedTcAvailOfStandardFormApproval, selectedTcAvailOfStandardFormRemarks,
                    binding.availOfStandardFormsLayout.etAvailOfStandardFormsRemarks.text.toString())) {
                selectedTcAvailOfStandardFormRemarks = if (selectedTcAvailOfStandardFormApproval == "Send for modification")
                    binding.availOfStandardFormsLayout.etAvailOfStandardFormsRemarks.text.toString() else ""

                showConfirmationDialog()
            }
        }
    }

    private fun setupApprovalSpinnerListeners() {
        binding.trainingCenterInfoLayout.SpinnerTcInfo.setOnItemClickListener { parent, _, position, _ ->
            selectedTcInfoApproval = parent.getItemAtPosition(position).toString()
            toggleRemarksVisibility(
                selectedTcInfoApproval == "Send for modification",
                binding.trainingCenterInfoLayout.InfoRemarks,
                binding.trainingCenterInfoLayout.etInfoRemarks
            )
        }

        binding.SpinnerDescAcademia.setOnItemClickListener { parent, _, position, _ ->
            selectedTcDescAcademiaApproval = parent.getItemAtPosition(position).toString()
            toggleRemarksVisibility(
                selectedTcDescAcademiaApproval == "Send for modification",
                binding.DescAcademiaRemarks,
                binding.etDescAcademiaRemarks
            )
        }

        binding.SpinnerTcInfra.setOnItemClickListener { parent, _, position, _ ->
            selectedTcInfraApproval = parent.getItemAtPosition(position).toString()
            toggleRemarksVisibility(
                selectedTcInfraApproval == "Send for modification",
                binding.InfraRemarks,
                binding.etInfraRemarks
            )
        }

        binding.SpinnerBasin.setOnItemClickListener { parent, _, position, _ ->
            selectedTcBasinApproval = parent.getItemAtPosition(position).toString()
            toggleRemarksVisibility(
                selectedTcBasinApproval == "Send for modification",
                binding.BasinRemarks,
                binding.etBasinRemarks
            )
        }

        binding.SpinnerDescOtherArea.setOnItemClickListener { parent, _, position, _ ->
            selectedTcDescOtherAreaApproval = parent.getItemAtPosition(position).toString()
            toggleRemarksVisibility(
                selectedTcDescOtherAreaApproval == "Send for modification",
                binding.DescOtherAreaRemarks,
                binding.etDescOtherAreaRemarks
            )
        }

        binding.SpinnerTeaching.setOnItemClickListener { parent, _, position, _ ->
            selectedTcTeachingApproval = parent.getItemAtPosition(position).toString()
            toggleRemarksVisibility(
                selectedTcTeachingApproval == "Send for modification",
                binding.TeachingRemarks,
                binding.etTeachingRemarks
            )
        }

        binding.SpinnerGeneral.setOnItemClickListener { parent, _, position, _ ->
            selectedTcGeneralApproval = parent.getItemAtPosition(position).toString()
            toggleRemarksVisibility(
                selectedTcGeneralApproval == "Send for modification",
                binding.GeneralRemarks,
                binding.etGeneralRemarks
            )
        }

        binding.SpinnerElectrical.setOnItemClickListener { parent, _, position, _ ->
            selectedTcElectricalApproval = parent.getItemAtPosition(position).toString()
            toggleRemarksVisibility(
                selectedTcElectricalApproval == "Send for modification",
                binding.ElectricalRemarks,
                binding.etElectricalRemarks
            )
        }

        binding.signageLayout.SpinnerSignage.setOnItemClickListener { parent, _, position, _ ->
            selectedTcSignageApproval = parent.getItemAtPosition(position).toString()
            toggleRemarksVisibility(
                selectedTcSignageApproval == "Send for modification",
                binding.signageLayout.SignageRemarks,
                binding.signageLayout.etSignageRemarks
            )
        }

        binding.ipCameraLayout.SpinnerIpEnable.setOnItemClickListener { parent, _, position, _ ->
            selectedTcIpEnableApproval = parent.getItemAtPosition(position).toString()
            toggleRemarksVisibility(
                selectedTcIpEnableApproval == "Send for modification",
                binding.ipCameraLayout.IpEnableRemarks,
                binding.ipCameraLayout.etIpEnableRemarks
            )
        }

        binding.commonEquipmentLayout.SpinnerCommonEquipment.setOnItemClickListener { parent, _, position, _ ->
            selectedTcCommonEquipmentApproval = parent.getItemAtPosition(position).toString()
            toggleRemarksVisibility(
                selectedTcCommonEquipmentApproval == "Send for modification",
                binding.commonEquipmentLayout.CommonEquipmentRemarks,
                binding.commonEquipmentLayout.etCommonEquipmentRemarks
            )
        }

        binding.availSupportInfraLayout.SpinnerAvailSupportInfra.setOnItemClickListener { parent, _, position, _ ->
            selectedTcAvailSupportInfraApproval = parent.getItemAtPosition(position).toString()
            toggleRemarksVisibility(
                selectedTcAvailSupportInfraApproval == "Send for modification",
                binding.availSupportInfraLayout.AvailSupportInfraRemarks,
                binding.availSupportInfraLayout.etAvailSupportInfraRemarks
            )
        }

        binding.availOfStandardFormsLayout.SpinnerAvailOfStandardForms.setOnItemClickListener { parent, _, position, _ ->
            selectedTcAvailOfStandardFormApproval = parent.getItemAtPosition(position).toString()
            toggleRemarksVisibility(
                selectedTcAvailOfStandardFormApproval == "Send for modification",
                binding.availOfStandardFormsLayout.AvailOfStandardFormsRemarks,
                binding.availOfStandardFormsLayout.etAvailOfStandardFormsRemarks
            )
        }
    }

    private fun setupImageClickListeners() {
        // Wash basin images
        binding.valueMaleToilet.setOnClickListener {
            showBase64ImageDialog(maleToiletImage, "Male Toilet Image")
        }

        binding.valueProofMaleSignageToilet.setOnClickListener {
            showBase64ImageDialog(maleToiletSignageImage, "Male Toilet Signage Image")
        }

        binding.valueMaleUrinals.setOnClickListener {
            showBase64ImageDialog(maleToiletUrinalsImage, "Male Toilet Urinals Image")
        }

        binding.valueMaleWashBasin.setOnClickListener {
            showBase64ImageDialog(maleToiletWashbasinImage, "Male Toilet Washbasin Image")
        }

        binding.valueFemaleToilet.setOnClickListener {
            showBase64ImageDialog(femaleToiletImage, "Female Toilet Image")
        }

        binding.valueProofFemaleSignageToilet.setOnClickListener {
            showBase64ImageDialog(femaleToiletSignageImage, "Female Toilet Signage Image")
        }

        binding.valueFemaleWashBasin.setOnClickListener {
            showBase64ImageDialog(femaleToiletWashbasinImage, "Female Toilet Washbasin Image")
        }

        binding.valueOverheadTank.setOnClickListener {
            showBase64ImageDialog(ovrHeadTankImage, "Overhead Tank Image")
        }

        binding.valueTypeOfFlooring.setOnClickListener {
            showBase64ImageDialog(typeOfFlooringImage, "Type Of Flooring Image")
        }

        // Desc area image set
        binding.valueFans.setOnClickListener {
            showBase64ImageDialog(fansImage, "Fan Image")
        }

        binding.valueCirculationArea.setOnClickListener {
            showBase64ImageDialog(circulationAreaImage, "Circulation Area Image")
        }

        binding.valueOpenSpace.setOnClickListener {
            showBase64ImageDialog(openSpaceImage, "Open Space Image")
        }

        binding.valueParking.setOnClickListener {
            showBase64ImageDialog(parkingSpaceImage, "Parking Space Image")
        }

        // Availability Teaching image set
        binding.valueIsWelcomeKitAvail.setOnClickListener {
            showBase64ImageDialog(welcomeKitImage, "Welcome Kit Image")
        }

        // General Details image set
        binding.valueSignOfLiakage.setOnClickListener {
            showBase64ImageDialog(signOfLeakageImage, "Sign Of Leakage Image")
        }

        binding.valueProtectionOfStairs.setOnClickListener {
            showBase64ImageDialog(protectionStairsBalImage, "Protection Stairs Balcony Image")
        }

        // Electrical wiring
        binding.valueSecuringWire.setOnClickListener {
            showBase64ImageDialog(securingWiringImage, "Securing Wiring Image")
        }

        binding.valueSwitchBoard.setOnClickListener {
            showBase64ImageDialog(switchBoardImage, "Switch Board Image")
        }

        // Signage's and info boards
        binding.signageLayout.valueCenterNameBoard.setOnClickListener {
            showBase64ImageDialog(tcNameBoardImage, "Training Center Name Board")
        }

        binding.signageLayout.valueSummaryAcheivement.setOnClickListener {
            showBase64ImageDialog(activitySummaryBoardImage, "Activity Summary Achievement")
        }

        binding.signageLayout.valueStudentEntitlement.setOnClickListener {
            showBase64ImageDialog(studentEntitlementBoardImage, "Student Entitlement Board Image")
        }

        binding.signageLayout.valueContactDetail.setOnClickListener {
            showBase64ImageDialog(contactDetailImpoPeopleImage, "Contact Detail Important People Image")
        }

        binding.signageLayout.valueBasicInfoBoard.setOnClickListener {
            showBase64ImageDialog(basicInfoBoardImage, "Basic Info Board Image")
        }

        binding.signageLayout.valueCodeOfConduct.setOnClickListener {
            showBase64ImageDialog(codeOfConductImage, "Code of Conduct")
        }

        binding.signageLayout.valueAttendanceSummary.setOnClickListener {
            showBase64ImageDialog(studentAttendanceImage, "Student Attendance Image")
        }

        // Ip Enable
        binding.ipCameraLayout.valueCentralMonitor.setOnClickListener {
            showBase64ImageDialog(centralMonitorImage, "Central Monitor Image")
        }

        binding.ipCameraLayout.valueConformanceCCTV.setOnClickListener {
            showBase64ImageDialog(conformationOfCCTVImage, "Conformation Of CCTV Image")
        }

        binding.ipCameraLayout.valueStorageCCTV.setOnClickListener {
            showBase64ImageDialog(storageOfCCtvImage, "Storage Of CCtv Image")
        }

        binding.ipCameraLayout.valueDvrStaticIP.setOnClickListener {
            showBase64ImageDialog(dvrImage, "DVR is Connected")
        }

        // Common equipment
        binding.commonEquipmentLayout.valueElectricalPowerBackup.setOnClickListener {
            showBase64ImageDialog(electricPowerImage, "Electric Power Image")
        }

        binding.commonEquipmentLayout.valueBiometricDevices.setOnClickListener {
            showBase64ImageDialog(installBiometricImage, "Install Biometric Image")
        }

        binding.commonEquipmentLayout.valueCCTVMonitor.setOnClickListener {
            showBase64ImageDialog(installationCCTVImage, "Installation CCTV Image")
        }

        binding.commonEquipmentLayout.valueStorageDocs.setOnClickListener {
            showBase64ImageDialog(storagePlaceSecuringDocImage, "Storage Place Securing Doc Image")
        }

        binding.commonEquipmentLayout.valuePrinterScanner.setOnClickListener {
            showBase64ImageDialog(printerCumImage, "Printer Cum Image")
        }

        binding.commonEquipmentLayout.valueDigitalCamera.setOnClickListener {
            showBase64ImageDialog(digitalCameraImage, "Digital Camera Image")
        }

        binding.commonEquipmentLayout.valueGrievanceRegister.setOnClickListener {
            showBase64ImageDialog(grievanceImage, "Grievance Image")
        }

        binding.commonEquipmentLayout.valueMinEquipment.setOnClickListener {
            showBase64ImageDialog(minimumEquipmentImage, "Minimum Equipment Image")
        }

        binding.commonEquipmentLayout.valueDirectionBoards.setOnClickListener {
            showBase64ImageDialog(directionBoardsImage, "Direction Boards Image")
        }

        // Availability of support infra
        binding.availSupportInfraLayout.valueSafeDrinkingWater.setOnClickListener {
            showBase64ImageDialog(safeDrinkingImage, "Safe Drinking Image")
        }

        binding.availSupportInfraLayout.valueFireFighting.setOnClickListener {
            showBase64ImageDialog(fireFightingImage, "Fire Fighting Image")
        }

        binding.availSupportInfraLayout.valueFirstAidKit.setOnClickListener {
            showBase64ImageDialog(firstAidImage, "First Aid Image")
        }
    }

    private fun setupPdfClickListeners() {
        binding.tvSelfDeclarationPdf.setOnClickListener {
            downloadAndOpenBase64Pdf(requireContext(), selfDeclarationPdf, "selfDeclarationPdf.pdf")
        }

        binding.tvPhotosOfBuildingPdf.setOnClickListener {
            downloadAndOpenBase64Pdf(requireContext(), buildingPdf, "buildingPdf.pdf")
        }

        binding.tvSchematicBuildingPlanPdf.setOnClickListener {
            downloadAndOpenBase64Pdf(requireContext(), schematicPdf, "schematicPdf.pdf")
        }

        binding.tvInternalExternalWallsPdf.setOnClickListener {
            downloadAndOpenBase64Pdf(requireContext(), internalExternalWallPdf, "internalExternalWalls.pdf")
        }
    }

    private fun setupNavigationListeners() {
        // Previous buttons
        binding.btnInfraPrevious.setOnClickListener {
            navigateToPreviousSection(
                previousSection = binding.trainingCenterInfoLayout.trainingInfoExpand,
                previousView = binding.trainingCenterInfoLayout.viewInfo,
                currentSection = binding.mainInfra,
                currentView = binding.viewInfra
            )
        }

        binding.btnDescAcademiaPrevious.setOnClickListener {
            navigateToPreviousSection(
                previousSection = binding.trainingInfraExpand,
                previousView = binding.viewInfra,
                currentSection = binding.mainDescAcademia,
                currentView = binding.viewDescAcademia
            )
        }

        binding.btnBasinPrevious.setOnClickListener {
            navigateToPreviousSection(
                previousSection = binding.trainingDescAcademiaExpand,
                previousView = binding.viewDescAcademia,
                currentSection = binding.mainToilet,
                currentView = binding.viewToilet
            )
        }

        binding.btnDescOtherAreaPrevious.setOnClickListener {
            navigateToPreviousSection(
                previousSection = binding.trainingToiletExpand,
                previousView = binding.viewToilet,
                currentSection = binding.mainDescOfOtherArea,
                currentView = binding.viewDescOfOtherArea
            )
        }

        binding.btnTeachingPrevious.setOnClickListener {
            navigateToPreviousSection(
                previousSection = binding.trainingDescOfOtherAreaExpand,
                previousView = binding.viewDescOfOtherArea,
                currentSection = binding.mainTeaching,
                currentView = binding.viewTeaching
            )
        }

        binding.btnGeneralPrevious.setOnClickListener {
            navigateToPreviousSection(
                previousSection = binding.trainingTeachingExpand,
                previousView = binding.viewTeaching,
                currentSection = binding.mainGeneralDetails,
                currentView = binding.viewGeneralDetails
            )
        }

        binding.btnElectricalPrevious.setOnClickListener {
            navigateToPreviousSection(
                previousSection = binding.trainingGeneralDetailsExpand,
                previousView = binding.viewGeneralDetails,
                currentSection = binding.mainElectricalDetails,
                currentView = binding.viewElectricalDetails
            )
        }

        binding.signageLayout.btnSignagePrevious.setOnClickListener {
            navigateToPreviousSection(
                previousSection = binding.trainingElectricalDetailsExpand,
                previousView = binding.viewElectricalDetails,
                currentSection = binding.mainSignageBoardDetails,
                currentView = binding.signageLayout.viewSignageBoardDetails
            )
        }

        binding.ipCameraLayout.btnIpEnablePrevious.setOnClickListener {
            navigateToPreviousSection(
                previousSection = binding.signageLayout.trainingSignageBoardlDetailsExpand,
                previousView = binding.signageLayout.viewSignageBoardDetails,
                currentSection = binding.mainIPEnableCameraDetails,
                currentView = binding.ipCameraLayout.viewIPEnableCameraDetails
            )
        }

        binding.commonEquipmentLayout.btnCommonEquipmentPrevious.setOnClickListener {
            navigateToPreviousSection(
                previousSection = binding.ipCameraLayout.trainingIPEnableCameralDetailsExpand,
                previousView = binding.ipCameraLayout.viewIPEnableCameraDetails,
                currentSection = binding.mainCommonEquipmentDetails,
                currentView = binding.commonEquipmentLayout.viewCommonEquipmentDetails
            )
        }

        binding.availSupportInfraLayout.btnAvailSupportInfraPrevious.setOnClickListener {
            navigateToPreviousSection(
                previousSection = binding.commonEquipmentLayout.trainingCommonEquipmentDetailsExpand,
                previousView = binding.commonEquipmentLayout.viewCommonEquipmentDetails,
                currentSection = binding.mainAvailSupportInfra,
                currentView = binding.availSupportInfraLayout.viewAvailSupportInfra
            )
        }

        binding.availOfStandardFormsLayout.btnAvailOfStandardFormsPrevious.setOnClickListener {
            navigateToPreviousSection(
                previousSection = binding.availSupportInfraLayout.trainingAvailSupportInfraExpand,
                previousView = binding.availSupportInfraLayout.viewAvailSupportInfra,
                currentSection = binding.mainAvailOfStandardForms,
                currentView = binding.availOfStandardFormsLayout.viewAvailOfStandardForms
            )
        }
    }

    // ==================== HELPER METHODS ====================

    private fun validateApproval(approval: String, remarks: String, enteredRemarks: String): Boolean {
        if (approval.isEmpty()) {
            showToast("Kindly select Approval first")
            return false
        }

        if (approval == "Send for modification" && enteredRemarks.isEmpty()) {
            showToast("Kindly enter remarks first")
            return false
        }

        return true
    }

    private fun toggleRemarksVisibility(show: Boolean, remarksLabel: View, remarksEditText: View) {
        if (show) {
            remarksLabel.show()
            remarksEditText.show()
        } else {
            remarksLabel.hide()
            remarksEditText.hide()
        }
    }

    private fun navigateToNextSection(
        currentSection: View,
        currentView: View,
        currentTextView: TextView,
        nextSection: View,
        nextView: View
    ) {
        currentSection.hide()
        currentView.hide()
        currentTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
        nextSection.show()
        nextView.show()

        binding.scroll.post {
            binding.scroll.smoothScrollTo(0, 0)
        }
    }

    private fun navigateToPreviousSection(
        previousSection: View,
        previousView: View,
        currentSection: View,
        currentView: View
    ) {
        previousSection.show()
        previousView.show()
        currentSection.hide()
        currentView.hide()

        binding.scroll.post {
            binding.scroll.smoothScrollTo(0, 0)
        }
    }

    private fun showTrainerStaffDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_trainer_staff)

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvTrainerStaff)
        val closeButton = dialog.findViewById<TextView>(R.id.tvClose)

        // Use BaseFragment's recyclerView setup for trainer staff
        setupRecyclerView(
            recyclerView = recyclerView,
            items = dataStaffList,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = { inflater, parent, _ ->
                ItemTrainerStaffBinding.inflate(inflater, parent, false)
            },
            onBind = { staff, itemBinding, _ ->
                itemBinding.tvProfileType.text = "Profile Type: ${staff.profileType}"
                itemBinding.tvName.text = "Name: ${staff.trainerName}"
                itemBinding.tvDesignation.text = "Designation: ${staff.trainerDesignation}"
                itemBinding.tvEngagementType.text = "Engagement Type: ${staff.engagementType}"
                itemBinding.tvDomain.text = "Domain/Non-Domain: ${staff.domainNondomain}"
                itemBinding.tvAssignedCourse.text = "Assigned Course: ${staff.assignedCourse}"
                itemBinding.tvTotCert.text = "TOT Certificate: ${staff.totCertificate}"
                itemBinding.tvTotCertNo.text = "TOT Cert. No: ${staff.totCertificateNo}"
            }
        )

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun handleRoomViewClick(room: RoomItem) {
        when (room.roomType) {
            "Theory Class Room" -> showTheoryClassRoomDialog(room)
            "Office Cum Counselling Room" -> showOfficeCumCounsellingRoomDialog(room)
            "Reception Area" -> showReceptionAreaDialog(room)
            "Counselling Room" -> showCounsellingRoomDialog(room)
            "Office Room" -> showOfficeRoomDialog(room)
            "IT cum Domain Lab" -> showItCumDomainLabDialog(room)
            "Theory Cum IT Lab" -> showTheoryCumItLabDialog(room)
            "Theory Cum Domain Lab" -> showTheoryCumDomainLabDialog(room)
            "IT Lab" -> showItLabDialog(room)
            "Domain Lab" -> showDomainLabDialog(room)
            else -> showToast("No layout found for ${room.roomType}")
        }
    }

    private fun showTheoryClassRoomDialog(room: RoomItem) {
        showProgressDialog("Loading room details...")
        val binding = TheoryClassRoomBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()

        val requestTcRoomDetails = AllRoomDetaisReques(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            roomType = room.roomType,
            roomNo = room.roomNo.toInt(),
            sanctionOrder = sanctionOrder,
        )

        viewModel.getAcademicRoomDetails(requestTcRoomDetails)

        viewModel.getAcademicRoomDetails.observe(viewLifecycleOwner) { result ->
            dismissProgressDialog()
            result.onSuccess { response ->
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
                    setupTheoryClassRoomData(binding, data)
                } else {
                    showToast("No data available")
                }
            }
            result.onFailure {
                showErrorToast("Failed to load room details")
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun showOfficeCumCounsellingRoomDialog(room: RoomItem) {
        showProgressDialog("Loading room details...")
        val binding = OfficeCumCouncelingRoomLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()

        val requestTcRoomDetails = AllRoomDetaisReques(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            roomType = room.roomType,
            roomNo = room.roomNo.toInt(),
            sanctionOrder = sanctionOrder
        )

        viewModel.getAcademicRoomDetails(requestTcRoomDetails)

        viewModel.getAcademicRoomDetails.observe(viewLifecycleOwner) { result ->
            dismissProgressDialog()
            result.onSuccess { response ->
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
                    setupOfficeCumCounsellingRoomData(binding, data)
                } else {
                    showToast("No data available")
                }
            }
            result.onFailure {
                showErrorToast("Failed to load room details")
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun showReceptionAreaDialog(room: RoomItem) {
        showProgressDialog("Loading room details...")
        val binding = ReceptionAreaLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()

        val requestTcRoomDetails = AllRoomDetaisReques(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            roomType = room.roomType,
            roomNo = room.roomNo.toInt(),
            sanctionOrder = sanctionOrder
        )

        viewModel.getAcademicRoomDetails(requestTcRoomDetails)

        viewModel.getAcademicRoomDetails.observe(viewLifecycleOwner) { result ->
            dismissProgressDialog()
            result.onSuccess { response ->
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
                    binding.yesNoReceptionAreaPhoto.text = safeText(data.roomsPhotographs)
                    binding.valueReceptionAreaPhoto.setOnClickListener {
                        showBase64ImageDialog(data.roomsPhotographsImage, "Reception Area Photo")
                    }
                } else {
                    showToast("No data available")
                }
            }
            result.onFailure {
                showErrorToast("Failed to load room details")
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun showCounsellingRoomDialog(room: RoomItem) {
        showProgressDialog("Loading room details...")
        val binding = CounsellingRoomBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()

        val requestTcRoomDetails = AllRoomDetaisReques(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            roomType = room.roomType,
            roomNo = room.roomNo.toIntOrNull() ?: 0,
            sanctionOrder = sanctionOrder
        )

        viewModel.getAcademicRoomDetails(requestTcRoomDetails)

        viewModel.getAcademicRoomDetails.observe(viewLifecycleOwner) { result ->
            dismissProgressDialog()
            result.onSuccess { response ->
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
                    binding.yesNoCounsellingAreaPhoto.text = safeText(data.roomsPhotographs)
                    binding.valueCounsellingAreaPhoto.setOnClickListener {
                        showBase64ImageDialog(data.roomsPhotographsImage, "Counselling Area Photo")
                    }
                } else {
                    showToast("No data available")
                }
            }
            result.onFailure {
                showErrorToast("Failed to load room details")
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun showOfficeRoomDialog(room: RoomItem) {
        showProgressDialog("Loading room details...")
        val binding = OfficeRoomLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()

        val requestTcRoomDetails = AllRoomDetaisReques(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            roomType = room.roomType,
            roomNo = room.roomNo.toIntOrNull() ?: 0,
            sanctionOrder = sanctionOrder
        )

        viewModel.getAcademicRoomDetails(requestTcRoomDetails)

        viewModel.getAcademicRoomDetails.observe(viewLifecycleOwner) { result ->
            dismissProgressDialog()
            result.onSuccess { response ->
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
                    setupOfficeRoomData(binding, data)
                } else {
                    showToast("No data available")
                }
            }
            result.onFailure {
                showErrorToast("Failed to load room details")
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun showItCumDomainLabDialog(room: RoomItem) {
        showProgressDialog("Loading room details...")
        val binding = ItCumDomainLabLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()

        val requestTcRoomDetails = AllRoomDetaisReques(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            roomType = room.roomType,
            roomNo = room.roomNo.toIntOrNull() ?: 0,
            sanctionOrder = sanctionOrder
        )

        viewModel.getAcademicRoomDetails(requestTcRoomDetails)

        viewModel.getAcademicRoomDetails.observe(viewLifecycleOwner) { result ->
            dismissProgressDialog()
            result.onSuccess { response ->
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
                    setupItCumDomainLabData(binding, data)
                } else {
                    showToast("No data available")
                }
            }
            result.onFailure {
                showErrorToast("Failed to load room details")
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun showTheoryCumItLabDialog(room: RoomItem) {
        showProgressDialog("Loading room details...")
        val binding = TheoryCumItLabLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()

        val requestTcRoomDetails = AllRoomDetaisReques(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            roomType = room.roomType,
            roomNo = room.roomNo.toIntOrNull() ?: 0,
            sanctionOrder = sanctionOrder
        )

        viewModel.getAcademicRoomDetails(requestTcRoomDetails)

        viewModel.getAcademicRoomDetails.observe(viewLifecycleOwner) { result ->
            dismissProgressDialog()
            result.onSuccess { response ->
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
                    setupTheoryCumItLabData(binding, data)
                } else {
                    showToast("No data available")
                }
            }
            result.onFailure {
                showErrorToast("Failed to load room details")
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun showTheoryCumDomainLabDialog(room: RoomItem) {
        showProgressDialog("Loading room details...")
        val binding = TheoryCumDomainLabLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()

        val requestTcRoomDetails = AllRoomDetaisReques(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            roomType = room.roomType,
            roomNo = room.roomNo.toInt(),
            sanctionOrder = sanctionOrder,
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAcademicRoomDetails(requestTcRoomDetails)
            delay(2000L)
            dismissProgressDialog()

            binding.yesNoTypeOfRoof.text = safeText(roofType)
            binding.valueTypeOfRoof.setOnClickListener {
                showBase64ImageDialog(roofTypeImage, "Type of Roof")
            }

            binding.yesNoFalseCeiling.text = safeText(falseCeiling)
            binding.valueFalseCeiling.setOnClickListener {
                showBase64ImageDialog(falseCeilingImage, "False Ceiling")
            }

            binding.yesNoHeightCeiling.text = safeText(ceilingHeight)
            binding.valueHeightCeiling.setOnClickListener {
                showBase64ImageDialog(ceilingHeightImage, "Height of Ceiling")
            }

            binding.yesNoVentilationArea.text = safeText(ventilationArea)
            binding.valueVentilationArea.setOnClickListener {
                showBase64ImageDialog(ventilationAreaImage, "Ventilation Area")
            }

            binding.yesNoSoundLevel.text = safeText(soundLevel)
            binding.valueSoundLevel.setOnClickListener {
                showBase64ImageDialog(soundLevelImage, "Sound Level")
            }

            binding.yesNoSoundProofAC.text = safeText(centerSoundProof)
            binding.valueSoundProofAC.setOnClickListener {
                showBase64ImageDialog(centerSoundProofImage, "Sound Proof & AC")
            }

            binding.yesNoInfoBoard.text = safeText(roomInfoBoard)
            binding.valueInfoBoard.setOnClickListener {
                showBase64ImageDialog(roomInfoBoardImage, "Room Info Board")
            }

            binding.yesNoInternalSignage.text = safeText(internalSignage)
            binding.valueInternalSignage.setOnClickListener {
                showBase64ImageDialog(internalSignageImage, "Internal Signage")
            }

            binding.yesNoCCTV.text = safeText(audioCamera)
            binding.valueCCTV.setOnClickListener {
                showBase64ImageDialog(audioCameraImage, "CCTV Cameras")
            }

            binding.yesNoLANComputers.text = safeText(lanEnabled)
            binding.valueLANComputers.setOnClickListener {
                showBase64ImageDialog(lanEnabledImage, "LAN Enabled Computers")
            }

            binding.yesNoInternet.text = safeText(internetConnection)
            binding.valueInternet.setOnClickListener {
                showBase64ImageDialog(internetConnectionImage, "Internet Connection")
            }

            binding.yesNoTypingTutor.text = safeText(typingTuterComp)
            binding.valueTypingTutor.setOnClickListener {
                showBase64ImageDialog(typingTuterCompImage, "Typing Tutor Computers")
            }

            binding.yesNoTablets.text = safeText(tablet)
            binding.valueTablets.setOnClickListener {
                showBase64ImageDialog(tabletImage, "Tablets")
            }

            binding.yesNoStools.text = safeText(candidateChair)
            binding.valueStools.setOnClickListener {
                showBase64ImageDialog(candidateChairImage, "Candidate Chair")
            }

            binding.yesNoTrainerChair.text = safeText(trainerChair)
            binding.valueTrainerChair.setOnClickListener {
                showBase64ImageDialog(trainerChairImage, "Trainer Chair")
            }

            binding.yesNoTrainerTable.text = safeText(trainerTable)
            binding.valueTrainerTable.setOnClickListener {
                showBase64ImageDialog(trainerTableImage, "Trainer Table")
            }

            binding.yesNoLights.text = safeText(lights)
            binding.valueLights.setOnClickListener {
                showBase64ImageDialog(lightsImage, "Lights")
            }

            binding.yesNoFans.text = safeText(fans)
            binding.valueFans.setOnClickListener {
                showBase64ImageDialog(fansRoomImage, "Fans")
            }

            binding.yesNoPowerBackup.text = safeText(ecPowerBackup)
            binding.valuePowerBackup.setOnClickListener {
                showBase64ImageDialog(ecPowerBackupImage, "Power Backup")
            }

            binding.yesNoLabPhoto.text = safeText(roomsPhotographs)
            binding.valueITLabPhoto.setOnClickListener {
                showBase64ImageDialog(roomsPhotographsImage, "IT Lab Photograph")
            }

            binding.yesNodomainrelatedequipPhoto.text = safeText(domainEquipment)
            binding.valuedomainrelatedequipPhoto.setOnClickListener {
                showBase64ImageDialog(domainEquipmentImage, "Domain Equipment")
            }

            binding.yesNoAirConditioning.text = safeText(airConditionRoom)
            binding.valueAirConditioning.setOnClickListener {
                showBase64ImageDialog(airConditionRoomImage, "Air Conditioning")
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun showItLabDialog(room: RoomItem) {
        showProgressDialog("Loading room details...")
        val binding = ItLabLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()

        val requestTcRoomDetails = AllRoomDetaisReques(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            roomType = room.roomType,
            roomNo = room.roomNo.toIntOrNull() ?: 0,
            sanctionOrder = sanctionOrder
        )

        viewModel.getAcademicRoomDetails(requestTcRoomDetails)

        viewModel.getAcademicRoomDetails.observe(viewLifecycleOwner) { result ->
            dismissProgressDialog()
            result.onSuccess { response ->
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
                    setupItLabData(binding, data)
                } else {
                    showToast("No data available")
                }
            }
            result.onFailure {
                showErrorToast("Failed to load room details")
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun showDomainLabDialog(room: RoomItem) {
        showProgressDialog("Loading room details...")
        val binding = DomainLabLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()

        val requestTcRoomDetails = AllRoomDetaisReques(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            roomType = room.roomType,
            roomNo = room.roomNo.toIntOrNull() ?: 0,
            sanctionOrder = sanctionOrder
        )

        viewModel.getAcademicRoomDetails(requestTcRoomDetails)

        viewModel.getAcademicRoomDetails.observe(viewLifecycleOwner) { result ->
            dismissProgressDialog()
            result.onSuccess { response ->
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
                    setupDomainLabData(binding, data)
                } else {
                    showToast("No data available")
                }
            }
            result.onFailure {
                showErrorToast("Failed to load room details")
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun setupTheoryClassRoomData(binding: TheoryClassRoomBinding, data: RoomDetail) {
        binding.yesNoTypeOfRoof.text = safeText(data.roofType)
        binding.yesNoFalseCeiling.text = safeText(data.falseCeiling)
        binding.yesNoHeightCeiling.text = safeText(data.ceilingHeight.toString())
        binding.yesNoVentilationArea.text = safeText(data.ventilationArea.toString())
        binding.yesNoSoundLevel.text = safeText(data.soundLevel.toString())
        binding.yesNoSoundProofAC.text = safeText(data.centerSoundProof)
        binding.yesNoInfoBoard.text = safeText(data.roomInfoBoard)
        binding.yesNoInternalSignage.text = safeText(data.internalSignage)
        binding.yesNoCCTV.text = safeText(data.audioCamera)
        binding.yesNoLCDComputers.text = safeText(data.digitalProjector)
        binding.yesNoChairForCan.text = safeText(data.candidateChair)
        binding.yesNoWritingBoard.text = safeText(data.writingBoard)
        binding.yesNoTrainerChair.text = safeText(data.trainerChair)
        binding.yesNoTrainerTable.text = safeText(data.trainerTable)
        binding.yesNoLights.text = safeText(data.lights.toString())
        binding.yesNoFans.text = safeText(data.fans.toString())
        binding.yesNoPowerBackup.text = safeText(data.ecPowerBackup)
        binding.yesNoLabPhoto.text = safeText(data.roomsPhotographs)
        binding.yesNoAirConditioning.text = safeText(data.airConditionRoom)

        binding.valueTypeOfRoof.setOnClickListener {
            showBase64ImageDialog(data.roofTypeImage, "Roof Type Image")
        }
        binding.valueFalseCeiling.setOnClickListener {
            showBase64ImageDialog(data.falseCeilingImage, "False Ceiling Image")
        }
        binding.valueHeightCeiling.setOnClickListener {
            showBase64ImageDialog(data.ceilingHeightImage, "Ceiling Height Image")
        }
        binding.valueVentilationArea.setOnClickListener {
            showBase64ImageDialog(data.ventilationAreaImage, "Ventilation Area Image")
        }
        binding.valueSoundLevel.setOnClickListener {
            showBase64ImageDialog(data.soundLevelImage, "Sound Level Image")
        }
        binding.valueSoundProofAC.setOnClickListener {
            showBase64ImageDialog(data.centerSoundProofImage, "Sound Proof & AC Image")
        }
        binding.valueInfoBoard.setOnClickListener {
            showBase64ImageDialog(data.roomInfoBoardImage, "Information Board Image")
        }
        binding.valueInternalSignage.setOnClickListener {
            showBase64ImageDialog(data.internalSignageImage, "Internal Signage Image")
        }
        binding.valueCCTV.setOnClickListener {
            showBase64ImageDialog(data.audioCameraImage, "CCTV & Audio Image")
        }
        binding.valueLCDComputers.setOnClickListener {
            showBase64ImageDialog(data.digitalProjectorImage, "Digital Projector / LCD Image")
        }
        binding.valueChairForCan.setOnClickListener {
            showBase64ImageDialog(data.candidateChairImage, "Candidate Chair Image")
        }
        binding.valueWritingBoard.setOnClickListener {
            showBase64ImageDialog(data.writingBoardImage, "Writing Board Image")
        }
        binding.valueTrainerChair.setOnClickListener {
            showBase64ImageDialog(data.trainerChairImage, "Trainer Chair Image")
        }
        binding.valueTrainerTable.setOnClickListener {
            showBase64ImageDialog(data.trainerTableImage, "Trainer Table Image")
        }
        binding.valueLights.setOnClickListener {
            showBase64ImageDialog(data.lightsImage, "Lights Image")
        }
        binding.valueFans.setOnClickListener {
            showBase64ImageDialog(data.fansImage, "Fans Image")
        }
        binding.valuePowerBackup.setOnClickListener {
            showBase64ImageDialog(data.ecPowerBackupImage, "Power Backup Image")
        }
        binding.valueITLabPhoto.setOnClickListener {
            showBase64ImageDialog(data.roomsPhotographsImage, "Room Photos")
        }
        binding.valueAirConditioning.setOnClickListener {
            showBase64ImageDialog(data.airConditionRoomImage, "Air Conditioning Image")
        }
    }

    private fun setupOfficeCumCounsellingRoomData(binding: OfficeCumCouncelingRoomLayoutBinding, data: RoomDetail) {
        binding.yesNoOfficeRoomPhoto.text = safeText(data.roomsPhotographs)
        binding.yesNoRoofType.text = safeText(data.roofType)
        binding.yesNoFalseCeiling.text = safeText(data.falseCeiling)
        binding.yesNoCeilingHeight.text = safeText(data.ceilingHeight.toString())
        binding.yesNoStorage.text = safeText(data.secureDocumentStorage)
        binding.yesNoOfficeTable.text = safeText(data.officeTable)
        binding.yesNoChairs.text = safeText(data.officeChair)
        binding.yesNoComputerTable.text = safeText(data.officeComputer)
        binding.yesNoPrinter.text = safeText(data.printerScanner)
        binding.yesNoCamera.text = safeText(data.digitalCamera)
        binding.yesNoPowerBackup.text = safeText(data.ecPowerBackup)

        binding.valueOfficeRoomPhoto.setOnClickListener {
            showBase64ImageDialog(data.roomsPhotographsImage, "Room Photo")
        }
        binding.valueRoofType.setOnClickListener {
            showBase64ImageDialog(data.roofTypeImage, "Roof Type Image")
        }
        binding.valueFalseCeiling.setOnClickListener {
            showBase64ImageDialog(data.falseCeilingImage, "False Ceiling Image")
        }
        binding.valueCeilingHeight.setOnClickListener {
            showBase64ImageDialog(data.ceilingHeightImage, "Ceiling Height Image")
        }
        binding.valueStorage.setOnClickListener {
            showBase64ImageDialog(data.secureDocumentStorageImage, "Storage Image")
        }
        binding.valueOfficeTable.setOnClickListener {
            showBase64ImageDialog(data.officeTableImage, "Office Table Image")
        }
        binding.valueChairs.setOnClickListener {
            showBase64ImageDialog(data.officeChairImage, "Chairs Image")
        }
        binding.valueComputerTable.setOnClickListener {
            showBase64ImageDialog(data.officeComputerImagePath, "Computer Table Image")
        }
        binding.valuePrinter.setOnClickListener {
            showBase64ImageDialog(data.printerScannerImage, "Printer / Scanner Image")
        }
        binding.valueCamera.setOnClickListener {
            showBase64ImageDialog(data.digitalCameraImage, "Digital Camera Image")
        }
        binding.valuePowerBackup.setOnClickListener {
            showBase64ImageDialog(data.ecPowerBackupImage, "Power Backup Image")
        }
    }

    private fun setupOfficeRoomData(binding: OfficeRoomLayoutBinding, data: RoomDetail) {
        binding.yesNoOfficeRoomPhoto.text = safeText(data.roomsPhotographs)
        binding.yesNoRoofType.text = safeText(data.roofType)
        binding.yesNoFalseCeiling.text = safeText(data.falseCeiling)
        binding.yesNoCeilingHeight.text = safeText(data.ceilingHeight)
        binding.yesNoStorage.text = safeText(data.secureDocumentStorage)
        binding.yesNoOfficeTable.text = safeText(data.officeTable)
        binding.yesNoChairs.text = safeText(data.officeChair)
        binding.yesNoComputerTable.text = safeText(data.officeComputer)
        binding.yesNoPrinter.text = safeText(data.printerScanner)
        binding.yesNoCamera.text = safeText(data.digitalCamera)
        binding.yesNoPowerBackup.text = safeText(data.ecPowerBackup)

        binding.valueOfficeRoomPhoto.setOnClickListener {
            showBase64ImageDialog(data.roomsPhotographsImage, "Office Room Photo")
        }
        binding.valueRoofType.setOnClickListener {
            showBase64ImageDialog(data.roofTypeImage, "Roof Type Image")
        }
        binding.valueFalseCeiling.setOnClickListener {
            showBase64ImageDialog(data.falseCeilingImage, "False Ceiling Image")
        }
        binding.valueCeilingHeight.setOnClickListener {
            showBase64ImageDialog(data.ceilingHeightImage, "Ceiling Height Image")
        }
        binding.valueStorage.setOnClickListener {
            showBase64ImageDialog(data.secureDocumentStorageImage, "Storage Place Image")
        }
        binding.valueOfficeTable.setOnClickListener {
            showBase64ImageDialog(data.officeTableImage, "Office Table Image")
        }
        binding.valueChairs.setOnClickListener {
            showBase64ImageDialog(data.officeChairImage, "Chairs Image")
        }
        binding.valueComputerTable.setOnClickListener {
            showBase64ImageDialog(data.officeComputerImagePath, "Computer Table Image")
        }
        binding.valuePrinter.setOnClickListener {
            showBase64ImageDialog(data.printerScannerImage, "Printer / Scanner Image")
        }
        binding.valueCamera.setOnClickListener {
            showBase64ImageDialog(data.digitalCameraImage, "Digital Camera Image")
        }
        binding.valuePowerBackup.setOnClickListener {
            showBase64ImageDialog(data.ecPowerBackupImage, "Power Backup Image")
        }
    }

    private fun setupItCumDomainLabData(binding: ItCumDomainLabLayoutBinding, data: RoomDetail) {
        binding.yesNoTypeOfRoof.text = safeText(data.roofType)
        binding.yesNoFalseCeiling.text = safeText(data.falseCeiling)
        binding.yesNoHeightCeiling.text = safeText(data.ceilingHeight)
        binding.yesNoVentilationArea.text = safeText(data.ventilationArea)
        binding.yesNoSoundLevel.text = safeText(data.soundLevel)
        binding.yesNoSoundProofAC.text = safeText(data.centerSoundProof)
        binding.yesNoInfoBoard.text = safeText(data.roomInfoBoard)
        binding.yesNoInternalSignage.text = safeText(data.internalSignage)
        binding.yesNoCCTV.text = safeText(data.audioCamera)
        binding.yesNoLANComputers.text = safeText(data.lanEnabled)
        binding.yesNoInternet.text = safeText(data.internetConnection)
        binding.yesNoTypingTutor.text = safeText(data.typingTuterComp)
        binding.yesNoTablets.text = safeText(data.tablet)
        binding.yesNoTrainerChair.text = safeText(data.trainerChair)
        binding.yesNoTrainerTable.text = safeText(data.trainerTable)
        binding.yesNoLights.text = safeText(data.lights)
        binding.yesNoFans.text = safeText(data.fans)
        binding.yesNoPowerBackup.text = safeText(data.ecPowerBackup)
        binding.yesNoAirConditioning.text = safeText(data.airConditionRoom)
        binding.yesNoLabPhoto.text = safeText(data.roomsPhotographs)
        binding.yesNodomainrelatedequipPhoto.text = safeText(data.domainEquipment)
        binding.yesNoStools.text = safeText(data.candidateChair)

        binding.valueTypeOfRoof.setOnClickListener { showBase64ImageDialog(data.roofTypeImage, "Roof Type") }
        binding.valueFalseCeiling.setOnClickListener { showBase64ImageDialog(data.falseCeilingImage, "False Ceiling") }
        binding.valueHeightCeiling.setOnClickListener { showBase64ImageDialog(data.ceilingHeightImage, "Height of Ceiling") }
        binding.valueVentilationArea.setOnClickListener { showBase64ImageDialog(data.ventilationAreaImage, "Ventilation Area") }
        binding.valueSoundLevel.setOnClickListener { showBase64ImageDialog(data.soundLevelImage, "Sound Level") }
        binding.valueSoundProofAC.setOnClickListener { showBase64ImageDialog(data.centerSoundProofImage, "Sound Proof / AC") }
        binding.valueInfoBoard.setOnClickListener { showBase64ImageDialog(data.roomInfoBoardImage, "Information Board") }
        binding.valueInternalSignage.setOnClickListener { showBase64ImageDialog(data.internalSignageImage, "Internal Signage") }
        binding.valueCCTV.setOnClickListener { showBase64ImageDialog(data.audioCameraImage, "CCTV Camera") }
        binding.valueLANComputers.setOnClickListener { showBase64ImageDialog(data.lanEnabledImage, "LAN Computers") }
        binding.valueInternet.setOnClickListener { showBase64ImageDialog(data.internetConnectionImage, "Internet Connection") }
        binding.valueTypingTutor.setOnClickListener { showBase64ImageDialog(data.typingTuterCompImage, "Typing Tutor") }
        binding.valueTablets.setOnClickListener { showBase64ImageDialog(data.tabletImage, "Tablets") }
        binding.valueTrainerChair.setOnClickListener { showBase64ImageDialog(data.trainerChairImage, "Trainer Chair") }
        binding.valueTrainerTable.setOnClickListener { showBase64ImageDialog(data.trainerTableImage, "Trainer Table") }
        binding.valueLights.setOnClickListener { showBase64ImageDialog(data.lightsImage, "Lights") }
        binding.valueFans.setOnClickListener { showBase64ImageDialog(data.fansImage, "Fans") }
        binding.valuePowerBackup.setOnClickListener { showBase64ImageDialog(data.ecPowerBackupImage, "Power Backup") }
        binding.valueAirConditioning.setOnClickListener { showBase64ImageDialog(data.airConditionRoomImage, "Air Conditioning") }
        binding.valueITLabPhoto.setOnClickListener { showBase64ImageDialog(data.roomsPhotographsImage, "IT cum Domain Lab Photo") }
        binding.valueStools.setOnClickListener { showBase64ImageDialog(data.candidateChairImage, "Domain Related Equipment") }
    }

    private fun setupTheoryCumItLabData(binding: TheoryCumItLabLayoutBinding, data: RoomDetail) {
        binding.yesNoTypeOfRoof.text = safeText(data.roofType)
        binding.yesNoFalseCeiling.text = safeText(data.falseCeiling)
        binding.yesNoHeightCeiling.text = safeText(data.ceilingHeight)
        binding.yesNoVentilationArea.text = safeText(data.ventilationArea)
        binding.yesNoSoundLevel.text = safeText(data.soundLevel)
        binding.yesNoSoundProofAC.text = safeText(data.centerSoundProof)
        binding.yesNoInfoBoard.text = safeText(data.roomInfoBoard)
        binding.yesNoInternalSignage.text = safeText(data.internalSignage)
        binding.yesNoCCTV.text = safeText(data.audioCamera)
        binding.yesNoLANComputers.text = safeText(data.lanEnabled)
        binding.yesNoInternet.text = safeText(data.internetConnection)
        binding.yesNoTypingTutor.text = safeText(data.typingTuterComp)
        binding.yesNoTablets.text = safeText(data.tablet)
        binding.yesNoStools.text = safeText(data.candidateChair)
        binding.yesNoTrainerChair.text = safeText(data.trainerChair)
        binding.yesNoTrainerTable.text = safeText(data.trainerTable)
        binding.yesNoLights.text = safeText(data.lights)
        binding.yesNoFans.text = safeText(data.fans)
        binding.yesNoPowerBackup.text = safeText(data.ecPowerBackup)
        binding.yesNoLabPhoto.text = safeText(data.roomsPhotographs)
        binding.yesNodomainrelatedequipPhoto.text = safeText(data.domainEquipment)
        binding.yesNoAirConditioning.text = safeText(data.airConditionRoom)

        binding.valueTypeOfRoof.setOnClickListener { showBase64ImageDialog(data.roofTypeImage, "Type of Roof") }
        binding.valueFalseCeiling.setOnClickListener { showBase64ImageDialog(data.falseCeilingImage, "False Ceiling") }
        binding.valueHeightCeiling.setOnClickListener { showBase64ImageDialog(data.ceilingHeightImage, "Height of Ceiling") }
        binding.valueVentilationArea.setOnClickListener { showBase64ImageDialog(data.ventilationAreaImage, "Ventilation Area") }
        binding.valueSoundLevel.setOnClickListener { showBase64ImageDialog(data.soundLevelImage, "Sound Level") }
        binding.valueSoundProofAC.setOnClickListener { showBase64ImageDialog(data.centerSoundProofImage, "Sound Proof & AC") }
        binding.valueInfoBoard.setOnClickListener { showBase64ImageDialog(data.roomInfoBoardImage, "Room Info Board") }
        binding.valueInternalSignage.setOnClickListener { showBase64ImageDialog(data.internalSignageImage, "Internal Signage") }
        binding.valueCCTV.setOnClickListener { showBase64ImageDialog(data.audioCameraImage, "CCTV Cameras") }
        binding.valueLANComputers.setOnClickListener { showBase64ImageDialog(data.lanEnabledImage, "LAN Enabled Computers") }
        binding.valueInternet.setOnClickListener { showBase64ImageDialog(data.internetConnectionImage, "Internet Connection") }
        binding.valueTypingTutor.setOnClickListener { showBase64ImageDialog(data.typingTuterCompImage, "Typing Tutor Computers") }
        binding.valueTablets.setOnClickListener { showBase64ImageDialog(data.tabletImage, "Tablets") }
        binding.valueStools.setOnClickListener { showBase64ImageDialog(data.candidateChairImage, "Candidate Chair") }
        binding.valueTrainerChair.setOnClickListener { showBase64ImageDialog(data.trainerChairImage, "Trainer Chair") }
        binding.valueTrainerTable.setOnClickListener { showBase64ImageDialog(data.trainerTableImage, "Trainer Table") }
        binding.valueLights.setOnClickListener { showBase64ImageDialog(data.lightsImage, "Lights") }
        binding.valueFans.setOnClickListener { showBase64ImageDialog(data.fansImage, "Fans") }
        binding.valuePowerBackup.setOnClickListener { showBase64ImageDialog(data.ecPowerBackupImage, "Power Backup") }
        binding.valueITLabPhoto.setOnClickListener { showBase64ImageDialog(data.roomsPhotographsImage, "IT Lab Photograph") }
        binding.valuedomainrelatedequipPhoto.setOnClickListener { showBase64ImageDialog(data.domainEquipmentImage, "Domain Equipment") }
        binding.valueAirConditioning.setOnClickListener { showBase64ImageDialog(data.airConditionRoomImage, "Air Conditioning") }
    }

    private fun setupItLabData(binding: ItLabLayoutBinding, data: RoomDetail) {
        binding.yesNoTypeOfRoof.text = safeText(data.roofType)
        binding.yesNoFalseCeiling.text = safeText(data.falseCeiling)
        binding.yesNoHeightCeiling.text = safeText(data.ceilingHeight)
        binding.yesNoVentilationArea.text = safeText(data.ventilationArea)
        binding.yesNoSoundLevel.text = safeText(data.soundLevel)
        binding.yesNoSoundProofAC.text = safeText(data.centerSoundProof)
        binding.yesNoInfoBoard.text = safeText(data.roomInfoBoard)
        binding.yesNoInternalSignage.text = safeText(data.internalSignage)
        binding.yesNoCCTV.text = safeText(data.audioCamera)
        binding.yesNoLANComputers.text = safeText(data.lanEnabled)
        binding.yesNoInternet.text = safeText(data.internetConnection)
        binding.yesNoTypingTutor.text = safeText(data.typingTuterComp)
        binding.yesNoTablets.text = safeText(data.tablet)
        binding.yesNoStools.text = safeText(data.candidateChair)
        binding.yesNoTrainerChair.text = safeText(data.trainerChair)
        binding.yesNoTrainerTable.text = safeText(data.trainerTable)
        binding.yesNoLights.text = safeText(data.lights)
        binding.yesNoFans.text = safeText(data.fans)
        binding.yesNoPowerBackup.text = safeText(data.ecPowerBackup)
        binding.yesNoAirConditioning.text = safeText(data.airConditionRoom)
        binding.yesNoLabPhoto.text = safeText(data.roomsPhotographs)

        binding.valueTypeOfRoof.setOnClickListener { showBase64ImageDialog(data.roofTypeImage, "Type of Roof") }
        binding.valueFalseCeiling.setOnClickListener { showBase64ImageDialog(data.falseCeilingImage, "False Ceiling") }
        binding.valueHeightCeiling.setOnClickListener { showBase64ImageDialog(data.ceilingHeightImage, "Height of Ceiling") }
        binding.valueVentilationArea.setOnClickListener { showBase64ImageDialog(data.ventilationAreaImage, "Ventilation Area") }
        binding.valueSoundLevel.setOnClickListener { showBase64ImageDialog(data.soundLevelImage, "Sound Level") }
        binding.valueSoundProofAC.setOnClickListener { showBase64ImageDialog(data.centerSoundProofImage, "Sound Proof / AC") }
        binding.valueInfoBoard.setOnClickListener { showBase64ImageDialog(data.roomInfoBoardImage, "Information Board") }
        binding.valueInternalSignage.setOnClickListener { showBase64ImageDialog(data.internalSignageImage, "Internal Signage") }
        binding.valueCCTV.setOnClickListener { showBase64ImageDialog(data.audioCameraImage, "CCTV Camera") }
        binding.valueLANComputers.setOnClickListener { showBase64ImageDialog(data.lanEnabledImage, "LAN Computers") }
        binding.valueInternet.setOnClickListener { showBase64ImageDialog(data.internetConnectionImage, "Internet Connection") }
        binding.valueTypingTutor.setOnClickListener { showBase64ImageDialog(data.typingTuterCompImage, "Typing Tutor") }
        binding.valueTablets.setOnClickListener { showBase64ImageDialog(data.tabletImage, "Tablets") }
        binding.valueStools.setOnClickListener { showBase64ImageDialog(data.candidateChairImage, "Stools / Chairs") }
        binding.valueTrainerChair.setOnClickListener { showBase64ImageDialog(data.trainerChairImage, "Trainer Chair") }
        binding.valueTrainerTable.setOnClickListener { showBase64ImageDialog(data.trainerTableImage, "Trainer Table") }
        binding.valueLights.setOnClickListener { showBase64ImageDialog(data.lightsImage, "Lights") }
        binding.valueFans.setOnClickListener { showBase64ImageDialog(data.fansImage, "Fans") }
        binding.valuePowerBackup.setOnClickListener { showBase64ImageDialog(data.ecPowerBackupImage, "Power Backup") }
        binding.valueAirConditioning.setOnClickListener { showBase64ImageDialog(data.airConditionRoomImage, "Air Conditioning") }
        binding.valueITLabPhoto.setOnClickListener { showBase64ImageDialog(data.roomsPhotographsImage, "IT Lab Photo") }
    }

    private fun setupDomainLabData(binding: DomainLabLayoutBinding, data: RoomDetail) {
        binding.yesNoTypeOfRoof.text = safeText(data.roofType)
        binding.yesNoFalseCeiling.text = safeText(data.falseCeiling)
        binding.yesNoHeightCeiling.text = safeText(data.ceilingHeight)
        binding.yesNoVentilationArea.text = safeText(data.ventilationArea)
        binding.yesNoSoundLevel.text = safeText(data.soundLevel)
        binding.yesNoSoundProofAC.text = safeText(data.centerSoundProof)
        binding.yesNoInfoBoard.text = safeText(data.roomInfoBoard)
        binding.yesNoInternalSignage.text = safeText(data.internalSignage)
        binding.yesNoCCTV.text = safeText(data.audioCamera)
        binding.yesNoLCDComputers.text = safeText(data.lanEnabled)
        binding.yesNoChairForCan.text = safeText(data.candidateChair)
        binding.yesNoWritingBoard.text = safeText(data.writingBoard)
        binding.yesNoTrainerChair.text = safeText(data.trainerChair)
        binding.yesNoTrainerTable.text = safeText(data.trainerTable)
        binding.yesNoLights.text = safeText(data.lights)
        binding.yesNoFans.text = safeText(data.fans)
        binding.yesNoPowerBackup.text = safeText(data.ecPowerBackup)
        binding.yesNoLabPhoto.text = safeText(data.roomsPhotographs)
        binding.yesNodomainrelatedequipPhoto.text = safeText(data.domainEquipment)
        binding.yesNoAirConditioning.text = safeText(data.airConditionRoom)

        binding.valueTypeOfRoof.setOnClickListener { showBase64ImageDialog(data.roofTypeImage, "Type of Roof") }
        binding.valueFalseCeiling.setOnClickListener { showBase64ImageDialog(data.falseCeilingImage, "False Ceiling") }
        binding.valueHeightCeiling.setOnClickListener { showBase64ImageDialog(data.ceilingHeightImage, "Height of Ceiling") }
        binding.valueVentilationArea.setOnClickListener { showBase64ImageDialog(data.ventilationAreaImage, "Ventilation Area") }
        binding.valueSoundLevel.setOnClickListener { showBase64ImageDialog(data.soundLevelImage, "Sound Level") }
        binding.valueSoundProofAC.setOnClickListener { showBase64ImageDialog(data.centerSoundProofImage, "Sound Proof & AC") }
        binding.valueInfoBoard.setOnClickListener { showBase64ImageDialog(data.roomInfoBoardImage, "Room Info Board") }
        binding.valueInternalSignage.setOnClickListener { showBase64ImageDialog(data.internalSignageImage, "Internal Signage") }
        binding.valueCCTV.setOnClickListener { showBase64ImageDialog(data.audioCameraImage, "CCTV & Audio") }
        binding.valueLCDComputers.setOnClickListener { showBase64ImageDialog(data.lanEnabledImage, "LAN / LCD Digital Projector") }
        binding.valueChairForCan.setOnClickListener { showBase64ImageDialog(data.candidateChairImage, "Chair for Candidates") }
        binding.valueWritingBoard.setOnClickListener { showBase64ImageDialog(data.writingBoardImage, "Writing Board") }
        binding.valueTrainerChair.setOnClickListener { showBase64ImageDialog(data.trainerChairImage, "Trainer Chair") }
        binding.valueTrainerTable.setOnClickListener { showBase64ImageDialog(data.trainerTableImage, "Trainer Table") }
        binding.valueLights.setOnClickListener { showBase64ImageDialog(data.lightsImage, "Lights") }
        binding.valueFans.setOnClickListener { showBase64ImageDialog(data.fansImage, "Fans") }
        binding.valuePowerBackup.setOnClickListener { showBase64ImageDialog(data.ecPowerBackupImage, "Power Backup") }
        binding.valueITLabPhoto.setOnClickListener { showBase64ImageDialog(data.roomsPhotographsImage, "Domain Lab Photo") }
        binding.valuedomainrelatedequipPhoto.setOnClickListener { showBase64ImageDialog(data.domainEquipmentImage, "Domain Related Equipment") }
        binding.valueAirConditioning.setOnClickListener { showBase64ImageDialog(data.airConditionRoomImage, "Air Conditioning") }
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to submit these details?")
            .setCancelable(false)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Submit") { dialog, _ ->
                submitFormData()
                dialog.dismiss()
            }
            .show()
    }

    private fun submitFormData() {
        val requestTcQTeamSubmit = TcQTeamInsertReq(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,

            tcInfoStatus = mapApproval(selectedTcInfoApproval),
            tcInfoRemark = selectedTcInfoRemarks,

            tcAcademicStatus = mapApproval(selectedTcDescAcademiaApproval),
            tcAcademicRemark = selectedTcDescAcademiaRemarks,

            tcInfraStatus = mapApproval(selectedTcInfraApproval),
            tcInfraRemark = selectedTcInfraRemarks,

            tcToiletStatus = mapApproval(selectedTcBasinApproval),
            tcToiletRemark = selectedTcBasinRemarks,

            tcDescOtherAreaStatus = mapApproval(selectedTcDescOtherAreaApproval),
            tcDescOtherAreaRemark = selectedTcDescOtherAreaRemarks,

            tcLearningMaterialStatus = mapApproval(selectedTcTeachingApproval),
            tcLearningMaterialRemark = selectedTcTeachingRemarks,

            tcGdStatus = mapApproval(selectedTcGeneralApproval),
            tcGdRemark = selectedTcGeneralRemarks,

            tcEcWiringStatus = mapApproval(selectedTcElectricalApproval),
            tcEcWiringRemark = selectedTcElectricalRemarks,

            tcSignageInfoStatus = mapApproval(selectedTcSignageApproval),
            tcSignageInfoRemark = selectedTcSignageRemarks,

            tcIpEnableStatus = mapApproval(selectedTcIpEnableApproval),
            tcIpEnableRemark = selectedTcIpEnableRemarks,

            tcCommonEquipmentStatus = mapApproval(selectedTcCommonEquipmentApproval),
            tcCommonEquipmentRemark = selectedTcCommonEquipmentRemarks,

            tcSupportInfraStatus = mapApproval(selectedTcAvailSupportInfraApproval),
            tcSupportInfraRemark = selectedTcAvailSupportInfraRemarks,

            tcStandardFormStatus = mapApproval(selectedTcAvailOfStandardFormApproval),
            tcStandardFormRemark = selectedTcAvailOfStandardFormRemarks
        )

        showProgressDialog("Submitting data...")

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.insertSrlmVerification(requestTcQTeamSubmit)
        }

        binding.availOfStandardFormsLayout.viewAvailOfStandardForms.visibility = View.GONE
        binding.availOfStandardFormsLayout.trainingAvailOfStandardFormsExpand.visibility = View.GONE
        binding.availOfStandardFormsLayout.tvTrainAvailOfStandardForms.setCompoundDrawablesWithIntrinsicBounds(
            0, 0, R.drawable.ic_verified, 0
        )

        binding.scroll.post {
            binding.scroll.smoothScrollTo(0, 0)
        }
    }

    // ==================== API RESPONSE COLLECTORS ====================

    @SuppressLint("SetTextI18n")
    private fun collectTCInfoResponse() {
        viewModel.trainingCentersInfo.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.forEach { x ->
                        binding.trainingCenterInfoLayout.tvSchemeName.text = x.schemeName
                        binding.trainingCenterInfoLayout.tvCenterName.text = x.centerName
                        binding.trainingCenterInfoLayout.tvProjectState.text = x.projectState
                        binding.trainingCenterInfoLayout.tvTypeOfArea.text = x.addressType
                        binding.trainingCenterInfoLayout.tvlatAndLang.text = "${x.latitude} , ${x.longitude}"
                        binding.trainingCenterInfoLayout.tvDistanceBus.text = x.distanceFromBusStand
                        binding.trainingCenterInfoLayout.tvDistanceAuto.text = x.distanceFromAutoStand
                        binding.trainingCenterInfoLayout.tvSanctionOrder.text = x.sanctionOrderNo
                        binding.trainingCenterInfoLayout.tvTypeOfTraining.text = x.tcType
                        binding.trainingCenterInfoLayout.tvNatureOfTraining.text = x.tcNature
                        binding.trainingCenterInfoLayout.tvSpecialArea.text = x.specialArea
                        binding.trainingCenterInfoLayout.tvTrainingCenterAddress.text = "${x.latitude},${x.tcAddress}"
                        binding.trainingCenterInfoLayout.tvTrainingCenterEmail.text = x.tcEmailID
                        binding.trainingCenterInfoLayout.tvMobileNumber.text = x.tcMobileNo
                        binding.trainingCenterInfoLayout.tvLandlineNumber.text = x.tcLandline
                        binding.trainingCenterInfoLayout.tvParliamentaryConstituency.text = x.parliamentaryConstituency
                        binding.trainingCenterInfoLayout.tvAssemblyConstituency.text = x.assemblyConstituency
                        binding.trainingCenterInfoLayout.tvCenterIncharge.text = x.centerIncharge
                        binding.trainingCenterInfoLayout.tvCenterInchargeMobile.text = x.inchargeMobileNo
                        binding.trainingCenterInfoLayout.tvCenterInchargeEmail.text = x.inchargeMailId
                    }
                },
                onNoData = {
                    showToast("No data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectTCStaffResponse() {
        viewModel.getTcStaffDetails.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.let {
                        dataStaffList.clear()
                        dataStaffList.addAll(it)
                        updateRecyclerViewData(binding.recyclerView.id, academiaList)
                    }
                },
                onNoData = {
                    showToast("No staff data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectTCInfraResponse() {
        viewModel.getTrainerCenterInfra.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.forEach { x ->
                        binding.tvOwnershipOfBuilding.text = x.buildingOwner
                        binding.tvAreaOfBuilding.text = x.buildingArea
                        binding.tvRoofOfBuilding.text = x.buildingRoof
                        binding.tvPlasteringPainting.text = x.painting

                        selfDeclarationPdf = x.selfDeclaration
                        buildingPdf = x.roofCeilingPhoto
                        schematicPdf = x.buildingPlan
                        internalExternalWallPdf = x.buildingWallImage
                    }
                },
                onNoData = {
                    showToast("No infra data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectTCAcademiaNonAcademia() {
        viewModel.getTcAcademicNonAcademicArea.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.let {
                        academiaList.clear()
                        academiaList.addAll(it)
                        updateRecyclerViewData(binding.recyclerView.id, academiaList)
                    }
                },
                onNoData = {
                    showToast("No academia data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectTCToiletAndWash() {
        viewModel.getTcToiletWashBasin.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.forEach { x ->
                        maleToiletImage = x.maleToiletImage
                        maleToiletSignageImage = x.maleToiletSignageImage
                        maleToiletUrinalsImage = x.maleUrinalImage
                        maleToiletWashbasinImage = x.maleWashBasinImage
                        femaleToiletImage = x.femaleToiletImage
                        femaleToiletSignageImage = x.femaleToiletSignageImage
                        femaleToiletWashbasinImage = x.femaleWashBasinImage
                        ovrHeadTankImage = x.overheadTankImage
                        typeOfFlooringImage = x.flooringTypeImage
                        binding.yesNoMaleToilet.text = x.maleToilet.toString()
                        binding.yesNoMaleUrinals.text = x.maleUrinal.toString()
                        binding.yesNoMaleWashBasin.text = x.maleWashBasin.toString()
                        binding.yesNoFemaleToilet.text = x.femaleToilet.toString()
                        binding.yesNoFemaleWashBasin.text = x.femaleWashBasin.toString()
                        binding.yesNoOverheadTank.text = x.overheadTanks
                        binding.yesNoTypeOfFlooring.text = x.flooringType
                    }
                },
                onNoData = {
                    showToast("No toilet data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectTCDescOtherArea() {
        viewModel.getDescriptionOtherArea.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.forEach { x ->
                        binding.valueCorridorNo.text = x.corridorNo
                        binding.valueLenghth.text = x.length
                        binding.valueWidth.text = x.width
                        binding.valueArea.text = x.areas
                        binding.valueLights.text = x.numberOfLights
                        binding.yesNoFans.text = x.numberOfFans
                        binding.yesNoCirculationArea.text = x.circulationArea
                        binding.yesNoOpenSpace.text = x.openSpace
                        binding.yesNoParking.text = x.parkingSpace

                        fansImage = x.descProofImagePath.toString()
                        circulationAreaImage = x.circulationAreaImagePath.toString()
                        openSpaceImage = x.openSpaceImagePath.toString()
                        parkingSpaceImage = x.parkingSpaceImagePath.toString()
                    }
                },
                onNoData = {
                    showToast("No other area data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectTCTeaching() {
        viewModel.getTeachingLearningMaterial.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.forEach { x ->
                        binding.yesNoTrade.text = x.trade
                        binding.yesNoNatureofTraining.text = x.trainingNature
                        binding.yesNoTradeAsPerProject.text = x.tradesAvailable
                        binding.yesNoIsTrainingPlanAvail.text = x.trainingPlan
                        binding.yesNoIsDomainCirAvail.text = x.domainCurriculum
                        binding.yesNoIsActivityCumLess.text = x.availableACLP
                        binding.yesNoIsWelcomeKitAvail.text = x.welcomeKit
                        binding.yesNoNameOfCertifyingAg.text = x.certifingAgencyName
                        binding.yesNoAssessmentMaterial.text = x.assessmentMaterial

                        welcomeKitImage = x.welcomeKitPdf
                    }
                },
                onNoData = {
                    showToast("No teaching data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectTCGeneral() {
        viewModel.getGeneralDetails.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.forEach { x ->
                        signOfLeakageImage = x.signLeakageImage
                        protectionStairsBalImage = x.stairsProtectionImage

                        binding.yesNoSignOfLiakage.text = x.signLeakage
                        binding.yesNoProtectionOfStairs.text = x.stairsProtection
                        binding.yesNoconformanceDDUGKY.text = x.ddugkyConfrence
                        binding.yesNoCandidateComeSafely.text = x.centerSafty
                    }
                },
                onNoData = {
                    showToast("No general data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectTCElectrical() {
        viewModel.getElectricalWiringStandard.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.forEach { x ->
                        securingWiringImage = x.wireSecurityImage.toString()
                        switchBoardImage = x.switchBoardImage.toString()

                        binding.yesNoSecuringWire.text = x.wireSecurity
                        binding.yesNoSwitchBoard.text = x.switchBoard
                    }
                },
                onNoData = {
                    showToast("No electrical data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectTCSignage() {
        viewModel.getSignagesAndInfoBoard.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.forEach { x ->
                        tcNameBoardImage = x.tcNameImage.toString()
                        activitySummaryBoardImage = x.activityAchivementImage.toString()
                        studentEntitlementBoardImage = x.studentEntitlementImage.toString()
                        contactDetailImpoPeopleImage = x.contactDetailsImage.toString()
                        basicInfoBoardImage = x.basicInfoImage.toString()
                        codeOfConductImage = x.codeConductImage.toString()
                        studentAttendanceImage = x.studentsAttendanceImage.toString()

                        binding.signageLayout.yesNoCenterNameBoard.text = x.tcName
                        binding.signageLayout.yesNoSummaryAcheivement.text = x.activityAchivement
                        binding.signageLayout.yesNoStudentEntitlement.text = x.studentEntitlement
                        binding.signageLayout.yesNoContactDetail.text = x.contactDetails
                        binding.signageLayout.yesNoBasicInfoBoard.text = x.basicInfo
                        binding.signageLayout.yesNoCodeOfConduct.text = x.codeConduct
                        binding.signageLayout.yesNoAttendanceSummary.text = x.studentsAttendance
                    }
                },
                onNoData = {
                    showToast("No signage data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectTCIpEnabele() {
        viewModel.getIpEnabledCamera.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.forEach { x ->
                        centralMonitorImage = x.centralMonitorImagePath.toString()
                        conformationOfCCTVImage = x.cctvConformanceImagePath.toString()
                        storageOfCCtvImage = x.cctvStorageImagePath.toString()
                        dvrImage = x.dvrStaticIpImagePath.toString()

                        binding.ipCameraLayout.yesNoCentralMonitor.text = x.centralMonitor
                        binding.ipCameraLayout.yesNoConformanceCCTV.text = x.cctvConformance
                        binding.ipCameraLayout.yesNoStorageCCTV.text = x.cctvStorage
                        binding.ipCameraLayout.yesNoDvrStaticIP.text = x.dvrStaticIp
                        binding.ipCameraLayout.yesNoIpEnabled.text = x.ipEnable
                        binding.ipCameraLayout.yesNoResolution.text = x.resolution
                        binding.ipCameraLayout.yesNoVideoStream.text = x.videoStream
                        binding.ipCameraLayout.yesNoRemoteAccessWeb.text = x.remoteAccessBrowser
                        binding.ipCameraLayout.yesNoRemoteAccessUsers.text = x.simultaneousAccess
                        binding.ipCameraLayout.yesNoSupportedProtocols.text = x.supportedProtocol
                        binding.ipCameraLayout.yesNoColorAudio.text = x.colorVideoAudit
                        binding.ipCameraLayout.yesNoStorageFacility.text = x.storageFacility
                    }
                },
                onNoData = {
                    showToast("No IP camera data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectTCCommonEquipment() {
        viewModel.getCommonEquipment.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.forEach { x ->
                        electricPowerImage = x.ecPowerBackupImage.toString()
                        installBiometricImage = x.biomatricDeviceInstallationImage.toString()
                        installationCCTVImage = x.cctvMoniotrInstallImage.toString()
                        storagePlaceSecuringDocImage = x.storageSecuringImage.toString()
                        printerCumImage = x.printerScannerImage.toString()
                        digitalCameraImage = x.digitalCameraImage.toString()
                        grievanceImage = x.grievanceRegisterImage.toString()
                        minimumEquipmentImage = x.minimumEquipmentImage.toString()
                        directionBoardsImage = x.directionBoardImage.toString()

                        binding.commonEquipmentLayout.yesNoElectricalPowerBackup.text = x.ecPowerBackup
                        binding.commonEquipmentLayout.yesNoBiometricDevices.text = x.biomatricDeviceInstallation
                        binding.commonEquipmentLayout.yesNoCCTVMonitor.text = x.cctvMoniotrInstall
                        binding.commonEquipmentLayout.yesNoStorageDocs.text = x.storageSecuring
                        binding.commonEquipmentLayout.yesNoPrinterScanner.text = x.printerScanner.toString()
                        binding.commonEquipmentLayout.yesNoDigitalCamera.text = x.digitalCamera.toString()
                        binding.commonEquipmentLayout.yesNoGrievanceRegister.text = x.grievanceRegister.toString()
                        binding.commonEquipmentLayout.yesNoMinEquipment.text = x.minimumEquipment.toString()
                        binding.commonEquipmentLayout.yesNoDirectionBoards.text = x.directionBoard.toString()
                    }
                },
                onNoData = {
                    showToast("No common equipment data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectTCSupportInfra() {
        viewModel.getAvailabilitySupportInfra.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.forEach { x ->
                        safeDrinkingImage = x.drinkingWaterImage.toString()
                        fireFightingImage = x.fireFighterEquipImage.toString()
                        firstAidImage = x.firstAidKitImage.toString()

                        binding.availSupportInfraLayout.yesNoSafeDrinkingWater.text = x.drinkingWater
                        binding.availSupportInfraLayout.yesNoFireFighting.text = x.fireFighterEquip
                        binding.availSupportInfraLayout.yesNoFirstAidKit.text = x.firstAidKit
                    }
                },
                onNoData = {
                    showToast("No support infra data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectTCStandardForms() {
        viewModel.getAvailabilityStandardForms.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.forEach { x ->
                        binding.availOfStandardFormsLayout.yesNoPlanOfTraining.text = x.trainingPlan
                        binding.availOfStandardFormsLayout.yesNoLessonPlanner.text = x.aclp
                        binding.availOfStandardFormsLayout.yesNoOnJobTraining.text = x.batchJobTrainingPlan
                        binding.availOfStandardFormsLayout.yesNoDailyTablets.text = x.tabletsDistribution
                        binding.availOfStandardFormsLayout.yesNoStudentEntitlementBanner.text = x.studentEntitlement
                        binding.availOfStandardFormsLayout.yesNoParentsConsentForm.text = x.parentsConsentForm
                        binding.availOfStandardFormsLayout.yesNoCandidateAttendanceRegister.text = x.candidateAttendRegBio
                        binding.availOfStandardFormsLayout.yesNoTrainerAttendanceRegister.text = x.trainersAttendRegBoi
                        binding.availOfStandardFormsLayout.yesNoItemsChecklist.text = x.candidateChecklistItem
                        binding.availOfStandardFormsLayout.yesNoEvaluationSummary.text = x.evaluationAssessmentSumm
                        binding.availOfStandardFormsLayout.yesNoTADARecord.text = x.tadaCalcRecord
                        binding.availOfStandardFormsLayout.yesNoTrainingCertificate.text = x.trainingCertificate
                        binding.availOfStandardFormsLayout.yesNoTrainingCompletionCertificateRecord.text = x.trainingCompCertDisbRecord
                        binding.availOfStandardFormsLayout.yesNoEquipmentTrainingCentre.text = x.equipmentList
                        binding.availOfStandardFormsLayout.yesNoEquipmentAccommodation.text = x.tafEquipment
                        binding.availOfStandardFormsLayout.yesNoTrainingCentreInspection.text = x.tcInspection
                        binding.availOfStandardFormsLayout.yesNoAssessmentCertification.text = x.candidateCertificateAsmt
                        binding.availOfStandardFormsLayout.yesNoLetterSRLMInfo.text = x.letterToMobilizationPlan
                        binding.availOfStandardFormsLayout.yesNoLetterFromSRLM.text = x.letterFromMobilizationPlan
                        binding.availOfStandardFormsLayout.yesNoOnFieldRegistration.text = x.candidateOnFieldReg
                        binding.availOfStandardFormsLayout.yesNoOverviewAptitudeTest.text = x.aptitudeTest
                        binding.availOfStandardFormsLayout.yesNoCandidateApplicationForm.text = x.candidateAppForm
                        binding.availOfStandardFormsLayout.yesNoTrainersProfile.text = x.trainerProfile
                        binding.availOfStandardFormsLayout.yesNoCandidatesEnrolled.text = x.enrolledCandidateList

                        binding.availOfStandardFormsLayout.yesNoCandidateDossierIndex.text = x.indexInvdcandidateDossier
                        binding.availOfStandardFormsLayout.yesNoPerformanceCan.text = x.prfEvelPlanCandidate
                        binding.availOfStandardFormsLayout.yesNoListOfCandidateAfterBatchFreezing.text = x.candidateAfterBatchFreeze
                        binding.availOfStandardFormsLayout.yesNoDailyFailureReport.text = x.dailyFailureItemReport
                        binding.availOfStandardFormsLayout.yesNo15DaysSummary.text = x.days15Summery
                        binding.availOfStandardFormsLayout.yesNoContentCounselling.text = x.tradeCounselling
                        binding.availOfStandardFormsLayout.yesNoCandidateIDTemplate.text = x.candidateIdTemp
                        binding.availOfStandardFormsLayout.yesNoStaffSummary.text = x.deployedStaffSumm
                        binding.availOfStandardFormsLayout.yesNoDullyIfApplicable.text = x.dulySignedformProofApplicable
                        binding.availOfStandardFormsLayout.yesNoPerformanceTrainer.text = x.prfEvelPlanTrainers
                        binding.availOfStandardFormsLayout.yesNoDully.text = x.dulySignedformProof
                        binding.availOfStandardFormsLayout.yesNoIpEnabled.text = x.ipEnabledCamera
                    }
                },
                onNoData = {
                    showToast("No standard forms data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectAllRoomDetails() {
        viewModel.getAcademicRoomDetails.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.forEach { x ->
                        fansRoomImage = x.fansImage ?: ""
                        writingBoard = x.writingBoard ?: ""
                        internetConnectionImage = x.internetConnectionImage ?: ""
                        roomInfoBoardImage = x.roomInfoBoardImage ?: ""
                        digitalProjectorImage = x.digitalProjectorImage ?: ""
                        officeComputer = x.officeComputer ?: ""
                        printerScannerImage = x.printerScannerImage ?: ""
                        centerSoundProof = x.centerSoundProof ?: ""
                        falseCeiling = x.falseCeiling ?: ""
                        tablet = x.tablet.toString()
                        typingTuterCompImage = x.typingTuterCompImage ?: ""
                        lanEnabledImage = x.lanEnabledImage ?: ""
                        internalSignageImage = x.internalSignageImage ?: ""
                        airConditionRoom = x.airConditionRoom ?: ""
                        roomsPhotographs = x.roomsPhotographs ?: ""
                        roomsPhotographsImage = x.roomsPhotographsImage ?: ""
                        audioCamera = x.audioCamera ?: ""
                        lanEnabled = x.lanEnabled.toString()
                        soundLevelImage = x.soundLevelImage ?: ""
                        centerSoundProofImage = x.centerSoundProofImage ?: ""
                        digitalCameraRoomImage = x.digitalCameraImage ?: ""
                        internetConnection = x.internetConnection ?: ""
                        officeChair = x.officeChair.toString()
                        officeTableImage = x.officeTableImage ?: ""
                        printerScanner = x.printerScanner.toString()
                        trainerChair = x.trainerChair ?: ""
                        domainEquipmentImage = x.domainEquipmentImage ?: ""
                        ecPowerBackup = x.ecPowerBackup ?: ""
                        tabletImage = x.tabletImage ?: ""
                        soundLevel = x.soundLevel.toString()
                        trainerTable = x.trainerTable ?: ""
                        falseCeilingImage = x.falseCeilingImage ?: ""
                        roomInfoBoard = x.roomInfoBoard ?: ""
                        roofTypeImage = x.roofTypeImage ?: ""
                        digitalProjector = x.digitalProjector ?: ""
                        secureDocumentStorage = x.secureDocumentStorage ?: ""
                        airConditionRoomImage = x.airConditionRoomImage ?: ""
                        sounfLevelSpecific = x.sounfLevelSpecific ?: ""
                        ventilationArea = x.ventilationArea.toString()
                        domainEquipment = x.domainEquipment.toString()
                        officeTable = x.officeTable.toString()
                        officeChairImage = x.officeChairImage ?: ""
                        typingTuterComp = x.typingTuterComp ?: ""
                        ceilingHeightImage = x.ceilingHeightImage ?: ""
                        candidateChair = x.candidateChair ?: ""
                        candidateChairImage = x.candidateChairImage ?: ""
                        ceilingHeight = x.ceilingHeight.toString()
                        lightsImage = x.lightsImage ?: ""
                        secureDocumentStorageImage = x.secureDocumentStorageImage ?: ""
                        writingBoardImage = x.writingBoardImage ?: ""
                        lights = x.lights.toString()
                        digitalCamera = x.digitalCamera.toString()
                        audioCameraImage = x.audioCameraImage ?: ""
                        internalSignage = x.internalSignage ?: ""
                        trainerChairImage = x.trainerChairImage ?: ""
                        ventilationAreaImage = x.ventilationAreaImage ?: ""
                        roofType = x.roofType
                        trainerTableImage = x.trainerTableImage ?: ""
                        fans = x.fans.toString()
                        officeComputerImagePath = x.officeComputerImagePath ?: ""
                        ecPowerBackupImage = x.ecPowerBackupImage ?: ""
                    }
                },
                onNoData = {
                    showToast("No room details available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun collectQTeamInsertRes() {
        viewModel.insertSrlmVerification.observe(viewLifecycleOwner) { result ->
            dismissProgressDialog()
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull(),
                onSuccess = { data ->
                    showSuccessToast("Data submitted successfully!")
                    findNavController().navigateUp()
                },
                onNoData = {
                    showToast("No response from server.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    // ==================== UTILITY METHODS ====================

    @SuppressLint("Recycle")
    private fun downloadAndOpenBase64Pdf(context: Context, base64: String, fileName: String = "document.pdf") {
        try {
            val cleanBase64 = base64
                .replace("data:application/pdf;base64,", "")
                .trim()

            val pdfBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            if (pdfBytes.isEmpty()) {
                showToast("Invalid PDF data")
                return
            }

            launchIO {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) downloadsDir.mkdirs()

                val file = File(downloadsDir, fileName)
                FileOutputStream(file).use { it.write(pdfBytes) }

                val uri = Uri.fromFile(file)
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))

                launchMain {
                    showSuccessToast("PDF downloaded to Downloads: ${file.name}")
                    openBase64Pdf(context, base64)
                }
            }
        } catch (e: Exception) {
            logCrashlyticsError("downloadAndOpenBase64Pdf", e)
            showErrorToast("Error: ${e.message}")
        }
    }

    private fun openBase64Pdf(context: Context, base64: String) {
        try {
            val cleanBase64 = base64
                .replace("data:application/pdf;base64,", "")
                .trim()

            val pdfBytes = Base64.decode(cleanBase64, Base64.DEFAULT)

            if (pdfBytes.isEmpty() || !String(pdfBytes.copyOfRange(0, 4)).startsWith("%PDF")) {
                showToast("Invalid PDF data")
                return
            }

            val pdfFile = File.createTempFile("temp_", ".pdf", context.cacheDir)
            pdfFile.outputStream().use { it.write(pdfBytes) }

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                pdfFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(Intent.createChooser(intent, "Open PDF with"))
            } else {
                showToast("No PDF viewer installed")
            }

        } catch (e: Exception) {
            logCrashlyticsError("openBase64Pdf", e)
            showErrorToast("Failed to open PDF")
        }
    }

    private fun mapApproval(approval: String): String {
        return when (approval) {
            "Send for modification" -> "M"
            "Approved" -> "A"
            else -> ""
        }
    }
}