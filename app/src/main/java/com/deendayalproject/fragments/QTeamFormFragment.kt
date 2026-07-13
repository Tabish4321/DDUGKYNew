package com.deendayalproject.fragments

import SharedViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.adapter.TrainerStaffAdapter
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.DescriptionAcademiaLayoutBinding
import com.deendayalproject.databinding.FragmentQTeamFormBinding
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.response.Trainer
import com.deendayalproject.util.AppUtil
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.deendayalproject.BuildConfig.USER_NAME_FOR_APP
import com.deendayalproject.MainActivity
import com.deendayalproject.databinding.CounsellingRoomBinding
import com.deendayalproject.databinding.DomainLabLayoutBinding
import com.deendayalproject.databinding.ItCumDomainLabLayoutBinding
import com.deendayalproject.databinding.ItLabLayoutBinding
import com.deendayalproject.databinding.OfficeCumCouncelingRoomLayoutBinding
import com.deendayalproject.databinding.OfficeRoomLayoutBinding
import com.deendayalproject.databinding.ReceptionAreaLayoutBinding
import com.deendayalproject.databinding.TheoryClassRoomBinding
import com.deendayalproject.databinding.TheoryCumDomainLabLayoutBinding
import com.deendayalproject.databinding.TheoryCumItLabLayoutBinding
import com.deendayalproject.model.request.AllRoomDetaisReques
import com.deendayalproject.model.request.TcQTeamInsertReq
import com.deendayalproject.model.response.RoomDetail
import com.deendayalproject.model.response.RoomItem
import com.deendayalproject.util.toastLong
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.gson.GsonBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.util.Date
import kotlin.getValue
import kotlin.random.Random

class QTeamFormFragment : BaseFragment<FragmentQTeamFormBinding>(
    bindingInflater = FragmentQTeamFormBinding::inflate
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

    //All Room Var
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
    private var ceilingHeight =""
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
    //end all room var

    private var centerId: String = ""
    private var centerName: String = ""
    private var sanctionOrder: String = ""

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
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var latValue: String = ""
    private var langValue: String = ""


    override fun initializeViews() {
        Log.d("FRAGMENT NAME", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━QTeamFormFragment━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")


        // Get arguments safely
        centerId = arguments?.getString("centerId").toString() ?: ""
        centerName = arguments?.getString("centerName").toString() ?: ""
        sanctionOrder = arguments?.getString("sanctionOrder").toString() ?: ""

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (hasLocationPermission()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }




        request = TrainingCenterInfo(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext())
        )


            setupToolbar(
                root = binding.root,
                title = "Dashboard",
                showBack = true,
                showLang = false,
                showProfile = false,
            )




        setupRecyclerView()
        setupAdapters()



    }

    override fun setupObservers() {
        if(AppUtil.getSavedLoginIdPreference(requireContext()) == BuildConfig.USER_NAME_FOR_APP){
            dismissProgressDialog()
            return
        }





//        if (AppUtil.getSavedLoginIdPreference(requireContext())=="DDUGKYUSER") {
//
//            binding. yesNoMaleToilet.setText("Ajit")
//            binding. yesNoMaleUrinals.text="19"
//            binding. yesNoMaleWashBasin.text="17"
//            binding. yesNoFemaleToilet.text="26"
//            binding. yesNoFemaleWashBasin.text="29"
//            binding. yesNoTypeOfFlooring.text="Yes"
//
//
//
//        }
//        else{

            collectTCInfoResponse()
            collectTCStaffResponse()
            collectTCElectrical()
            collectTCGeneral()
            collectTCTeaching()
            collectTCDescOtherArea()
            collectTCToiletAndWash()
            collectTCAcademiaNonAcademia()
            collectTCInfraResponse()
            collectTCSignage()
            collectTCIpEnabele()
            collectTCCommonEquipment()
            collectTCSupportInfra()
            collectTCStandardForms()
            collectAllRoomDetails()
            collectQTeamInsertRes()



//        }







    }
    private lateinit var request: TrainingCenterInfo

    override fun setupClickListeners() {
        setupAllClickListeners()
    }



    override fun loadInitialData() {
        if(AppUtil.getSavedLoginIdPreference(requireContext()) == BuildConfig.USER_NAME_FOR_APP){
            binding.toolbar.btnBack.visibility= View.GONE
            dismissProgressDialog()
            return
        }
//        if (USER_NAME_FOR_APP == request.loginId) {
//
//        }
//        else {
//
//        }
        viewModel.getTrainerCenterInfo(request)
        viewModel.getTcStaffDetails(request)
    }


    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val binding = DescriptionAcademiaLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return object : RecyclerView.ViewHolder(binding.root) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val room = academiaList[position]
                val binding = DescriptionAcademiaLayoutBinding.bind(holder.itemView)

                binding.tvMaxCandidate.text = room.maxPermissibleCandidate
                binding.tvLength.text = room.roomLength
                binding.tvWidth.text = room.roomWidth
                binding.tvArea.text = room.roomArea
                binding.tvRoomType.text = room.roomType

                binding.btnView.setOnClickListener {
                    handleRoomItemClick(room)
                }
            }

            override fun getItemCount() = academiaList.size
        }
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

//        // All Adapter setups
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

    private fun setupAllClickListeners() {
        // Spinner click listeners
        setupSpinnerSelectionListeners()

        // Back button
//        binding.backButton.setOnClickListener {
//            findNavController().navigateUp()
//        }

        // Image click listeners
        setupImageClickListeners()

        // PDF click listeners
        setupPdfClickListeners()

        // Trainer and staff view
        binding.trainingCenterInfoLayout.tvViewTrainerAndStaff.setOnClickListener {
            showTrainerStaffDialog()
        }

        // All navigation buttons
        setupNavigationButtons()
    }


    // Handle spinner selection and show/hide remarks
    private fun setupSpinnerSelectionListeners() {
        binding.trainingCenterInfoLayout.SpinnerTcInfo.setOnItemClickListener { _, _, pos, _ -> onApprovalSelected(pos, "tcInfo") }
        binding.SpinnerDescAcademia.setOnItemClickListener { _, _, pos, _ -> onApprovalSelected(pos, "descAcademia") }
        binding.SpinnerTcInfra.setOnItemClickListener { _, _, pos, _ -> onApprovalSelected(pos, "infra") }
        binding.SpinnerBasin.setOnItemClickListener { _, _, pos, _ -> onApprovalSelected(pos, "basin") }
        binding.SpinnerDescOtherArea.setOnItemClickListener { _, _, pos, _ -> onApprovalSelected(pos, "descOtherArea") }
        binding.SpinnerTeaching.setOnItemClickListener { _, _, pos, _ -> onApprovalSelected(pos, "teaching") }
        binding.SpinnerGeneral.setOnItemClickListener { _, _, pos, _ -> onApprovalSelected(pos, "general") }
        binding.SpinnerElectrical.setOnItemClickListener { _, _, pos, _ -> onApprovalSelected(pos, "electrical") }
        binding.signageLayout.SpinnerSignage.setOnItemClickListener { _, _, pos, _ -> onApprovalSelected(pos, "signage") }
        binding.ipCameraLayout.SpinnerIpEnable.setOnItemClickListener { _, _, pos, _ -> onApprovalSelected(pos, "ipEnable") }
        binding.commonEquipmentLayout.SpinnerCommonEquipment.setOnItemClickListener { _, _, pos, _ -> onApprovalSelected(pos, "commonEquipment") }
        binding.availSupportInfraLayout.SpinnerAvailSupportInfra.setOnItemClickListener { _, _, pos, _ -> onApprovalSelected(pos, "supportInfra") }
        binding.availOfStandardFormsLayout.SpinnerAvailOfStandardForms.setOnItemClickListener { _, _, pos, _ -> onApprovalSelected(pos, "standardForms") }
    }

    private fun onApprovalSelected(position: Int, section: String) {
        val selected = approvalList[position]
        val isModify = selected == "Send for modification"

        when (section) {
            "tcInfo" -> { selectedTcInfoApproval = selected; toggleRemarks(binding.trainingCenterInfoLayout.InfoRemarks, binding.trainingCenterInfoLayout.etInfoRemarks, isModify) }
            "descAcademia" -> { selectedTcDescAcademiaApproval = selected; toggleRemarks(binding.DescAcademiaRemarks, binding.etDescAcademiaRemarks, isModify) }
            "infra" -> { selectedTcInfraApproval = selected; toggleRemarks(binding.InfraRemarks, binding.etInfraRemarks, isModify) }
            "basin" -> { selectedTcBasinApproval = selected; toggleRemarks(binding.BasinRemarks, binding.etBasinRemarks, isModify) }
            "descOtherArea" -> { selectedTcDescOtherAreaApproval = selected; toggleRemarks(binding.DescOtherAreaRemarks, binding.etDescOtherAreaRemarks, isModify) }
            "teaching" -> { selectedTcTeachingApproval = selected; toggleRemarks(binding.TeachingRemarks, binding.etTeachingRemarks, isModify) }
            "general" -> { selectedTcGeneralApproval = selected; toggleRemarks(binding.GeneralRemarks, binding.etGeneralRemarks, isModify) }
            "electrical" -> { selectedTcElectricalApproval = selected; toggleRemarks(binding.ElectricalRemarks, binding.etElectricalRemarks, isModify) }
            "signage" -> { selectedTcSignageApproval = selected; toggleRemarks(binding.signageLayout.SignageRemarks, binding.signageLayout.etSignageRemarks, isModify) }
            "ipEnable" -> { selectedTcIpEnableApproval = selected; toggleRemarks(binding.ipCameraLayout.IpEnableRemarks, binding.ipCameraLayout.etIpEnableRemarks, isModify) }
            "commonEquipment" -> { selectedTcCommonEquipmentApproval = selected; toggleRemarks(binding.commonEquipmentLayout.CommonEquipmentRemarks, binding.commonEquipmentLayout.etCommonEquipmentRemarks, isModify) }
            "supportInfra" -> { selectedTcAvailSupportInfraApproval = selected; toggleRemarks(binding.availSupportInfraLayout.AvailSupportInfraRemarks, binding.availSupportInfraLayout.etAvailSupportInfraRemarks, isModify) }
            "standardForms" -> { selectedTcAvailOfStandardFormApproval = selected; toggleRemarks(binding.availOfStandardFormsLayout.AvailOfStandardFormsRemarks, binding.availOfStandardFormsLayout.etAvailOfStandardFormsRemarks, isModify) }
        }
    }



    private fun toggleRemarks(label: View, editText: View, show: Boolean) {
        label.visibility = if (show) View.VISIBLE else View.GONE
        editText.visibility = if (show) View.VISIBLE else View.GONE
    }


//    private fun observeFansCount(fansImage: String,title: String) {
//        viewModel.getFansCountAPI.removeObservers(viewLifecycleOwner)
//        viewModel.getFansCountAPI.observe(viewLifecycleOwner) { result ->
//            result.onSuccess {
//                when (it.responseCode) {
//                    200 -> {
//                        showBase64ImageWithCountDialog(
//                            base64ImageString=fansImage,
//                          title =   title,
//                            count = "${it.facilityId}"
//                        )
//                    }
//                    301 ->Toast.makeText(
//                        requireContext(),
//                        "Please upgrade your app first.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//            result.onFailure {
//                Toast.makeText(
//                    requireContext(),
//                     "Something went wrong",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }






    private fun setupImageClickListeners() {
        // Wash basin images
        binding.valueMaleToilet.setOnClickListener { showBase64ImageDialog(maleToiletImage, "Male Toilet") }
        binding.valueProofMaleSignageToilet.setOnClickListener { showBase64ImageDialog(maleToiletSignageImage, "Male Toilet Signage") }
        binding.valueMaleUrinals.setOnClickListener { showBase64ImageDialog(maleToiletUrinalsImage, "Male Urinals") }
        binding.valueMaleWashBasin.setOnClickListener { showBase64ImageDialog(maleToiletWashbasinImage, "Male Wash Basin") }
        binding.valueFemaleToilet.setOnClickListener { showBase64ImageDialog(femaleToiletImage, "Female Toilet") }
        binding.valueProofFemaleSignageToilet.setOnClickListener { showBase64ImageDialog(femaleToiletSignageImage, "Female Toilet Signage") }
        binding.valueFemaleWashBasin.setOnClickListener { showBase64ImageDialog(femaleToiletWashbasinImage, "Female Wash Basin") }
        binding.valueOverheadTank.setOnClickListener { showBase64ImageDialog(ovrHeadTankImage, "Overhead Tank") }
        binding.valueTypeOfFlooring.setOnClickListener { showBase64ImageDialog(typeOfFlooringImage, "Type of Flooring") }

        // Desc area images
        binding.valueFans.setOnClickListener {showBase64ImageDialog(fansImage, "Fans")} // observeFansCount(fansImage = fansImage, title = "Fans") } // showBase64ImageDialog(fansImage, "Fans")
        binding.valueCirculationArea.setOnClickListener { showBase64ImageDialog(circulationAreaImage, "Circulation Area") }
        binding.valueOpenSpace.setOnClickListener { showBase64ImageDialog(openSpaceImage, "Open Space") }
        binding.valueParking.setOnClickListener { showBase64ImageDialog(parkingSpaceImage, "Parking Space") }

        // Teaching images
        binding.valueIsWelcomeKitAvail.setOnClickListener { showBase64ImageDialog(welcomeKitImage, "Welcome Kit") }

        // General details images
        binding.valueSignOfLiakage.setOnClickListener { showBase64ImageDialog(signOfLeakageImage, "Sign of Leakage") }
        binding.valueProtectionOfStairs.setOnClickListener { showBase64ImageDialog(protectionStairsBalImage, "Protection of Stairs") }

        // Electrical wiring images
        binding.valueSecuringWire.setOnClickListener { showBase64ImageDialog(securingWiringImage, "Securing Wiring") }
        binding.valueSwitchBoard.setOnClickListener { showBase64ImageDialog(switchBoardImage, "Switch Board") }

        // Signage images

        binding.signageLayout.valueCenterNameBoard.setOnClickListener { showBase64ImageDialog(tcNameBoardImage, "Training Center Name Board") }
        binding.signageLayout.valueSummaryAcheivement.setOnClickListener { showBase64ImageDialog(activitySummaryBoardImage, "Activity Summary Achievement") }
        binding.signageLayout.valueStudentEntitlement.setOnClickListener { showBase64ImageDialog(studentEntitlementBoardImage, "Student Entitlement Board") }
        binding.signageLayout.valueContactDetail.setOnClickListener { showBase64ImageDialog(contactDetailImpoPeopleImage, "Contact Details") }
        binding.signageLayout.valueBasicInfoBoard.setOnClickListener { showBase64ImageDialog(basicInfoBoardImage, "Basic Info Board") }
        binding.signageLayout.valueCodeOfConduct.setOnClickListener { showBase64ImageDialog(codeOfConductImage, "Code of Conduct") }
        binding.signageLayout.valueAttendanceSummary.setOnClickListener { showBase64ImageDialog(studentAttendanceImage, "Attendance Summary") }

        // IP Enable images
        binding.ipCameraLayout.valueCentralMonitor.setOnClickListener { showBase64ImageDialog(centralMonitorImage, "Central Monitor") }
        binding.ipCameraLayout.valueConformanceCCTV.setOnClickListener { showBase64ImageDialog(conformationOfCCTVImage, "CCTV Conformance") }
        binding.ipCameraLayout.valueStorageCCTV.setOnClickListener { showBase64ImageDialog(storageOfCCtvImage, "CCTV Storage") }
        binding.ipCameraLayout.valueDvrStaticIP.setOnClickListener { showBase64ImageDialog(dvrImage, "DVR Static IP") }

        // Common equipment images
        binding.commonEquipmentLayout.valueElectricalPowerBackup.setOnClickListener { showBase64ImageDialog(electricPowerImage, "Electrical Power Backup") }
        binding.commonEquipmentLayout.valueBiometricDevices.setOnClickListener { showBase64ImageDialog(installBiometricImage, "Biometric Devices") }
        binding.commonEquipmentLayout.valueCCTVMonitor.setOnClickListener { showBase64ImageDialog(installationCCTVImage, "CCTV Monitor") }
        binding.commonEquipmentLayout.valueStorageDocs.setOnClickListener { showBase64ImageDialog(storagePlaceSecuringDocImage, "Storage Documents") }
        binding.commonEquipmentLayout.valuePrinterScanner.setOnClickListener { showBase64ImageDialog(printerCumImage, "Printer Scanner") }
        binding.commonEquipmentLayout.valueDigitalCamera.setOnClickListener { showBase64ImageDialog(digitalCameraImage, "Digital Camera") }
        binding.commonEquipmentLayout.valueGrievanceRegister.setOnClickListener { showBase64ImageDialog(grievanceImage, "Grievance Register") }
        binding.commonEquipmentLayout.valueMinEquipment.setOnClickListener { showBase64ImageDialog(minimumEquipmentImage, "Minimum Equipment") }
        binding.commonEquipmentLayout.valueDirectionBoards.setOnClickListener { showBase64ImageDialog(directionBoardsImage, "Direction Boards") }

        // Support infra images
        binding.availSupportInfraLayout.valueSafeDrinkingWater.setOnClickListener { showBase64ImageDialog(safeDrinkingImage, "Safe Drinking Water") }
        binding.availSupportInfraLayout.valueFireFighting.setOnClickListener { showBase64ImageDialog(fireFightingImage, "Fire Fighting") }
        binding.availSupportInfraLayout.valueFirstAidKit.setOnClickListener { showBase64ImageDialog(firstAidImage, "First Aid Kit") }

    }


    private  fun TextView.onImageDialogClick(base64: String, fileName: String) {
        setOnClickListener { showBase64ImageDialog(base64, fileName) }
    }



    private  fun TextView.onPdfClick(base64: String, fileName: String) {
        setOnClickListener { downloadAndOpenBase64Pdf(base64, fileName) }
    }


    private fun setupPdfClickListeners() {

        binding.tvSelfDeclarationPdf.setOnClickListener {
            downloadAndOpenBase64Pdf(selfDeclarationPdf, "selfDeclarationPdf.pdf")
        }

        binding.tvPhotosOfBuildingPdf.setOnClickListener {
            downloadAndOpenBase64Pdf(buildingPdf, "buildingPdf.pdf")
        }

        binding.tvSchematicBuildingPlanPdf.setOnClickListener {
            downloadAndOpenBase64Pdf(schematicPdf, "schematicPdf.pdf")
        }

        binding.tvInternalExternalWallsPdf.setOnClickListener {
            downloadAndOpenBase64Pdf(internalExternalWallPdf, "internalExternalWallsPdf.pdf")
        }
    }

    private fun setupNavigationButtons() {
        binding.apply {
            trainingCenterInfoLayout.btnInfoNext.setOnClickListener { handleInfoNext() }
            btnInfraNext.setOnClickListener { handleInfraNext() }
            btnInfraPrevious.setOnClickListener { handleInfraPrevious() }
            btnDescAcademiaNext.setOnClickListener { handleDescAcademiaNext() }
            btnDescAcademiaPrevious.setOnClickListener { handleDescAcademiaPrevious() }
            btnBasinNext.setOnClickListener { handleBasinNext() }
            btnBasinPrevious.setOnClickListener { handleBasinPrevious() }
            btnDescOtherAreaNext.setOnClickListener { handleDescOtherAreaNext() }
            btnDescOtherAreaPrevious.setOnClickListener { handleDescOtherAreaPrevious() }
            btnTeachingNext.setOnClickListener { handleTeachingNext() }
            btnTeachingPrevious.setOnClickListener { handleTeachingPrevious() }
            btnGeneralNext.setOnClickListener { handleGeneralNext() }
            btnGeneralPrevious.setOnClickListener { handleGeneralPrevious() }
            btnElectricalNext.setOnClickListener { handleElectricalNext() }
            btnElectricalPrevious.setOnClickListener { handleElectricalPrevious() }
            signageLayout.btnSignageNext.setOnClickListener { handleSignageNext() }
            signageLayout.btnSignagePrevious.setOnClickListener { handleSignagePrevious() }
            ipCameraLayout.btnIpEnableNext.setOnClickListener { handleIpEnableNext() }
            ipCameraLayout.btnIpEnablePrevious.setOnClickListener { handleIpEnablePrevious() }
            commonEquipmentLayout.btnCommonEquipmentNext.setOnClickListener { handleCommonEquipmentNext() }
            commonEquipmentLayout.btnCommonEquipmentPrevious.setOnClickListener { handleCommonEquipmentPrevious() }
            availSupportInfraLayout.btnAvailSupportInfraNext.setOnClickListener { handleAvailSupportInfraNext() }
            availSupportInfraLayout.btnAvailSupportInfraPrevious.setOnClickListener { handleAvailSupportInfraPrevious() }
            availOfStandardFormsLayout.btnAvailOfStandardFormsNext.setOnClickListener { handleAvailOfStandardFormsNext() }
            availOfStandardFormsLayout.btnAvailOfStandardFormsPrevious.setOnClickListener { handleAvailOfStandardFormsPrevious() }  }
    }

    private fun handleInfoNext() {
        if (!validateApproval(selectedTcInfoApproval, "Approval")) return
        if (selectedTcInfoApproval == "Send for modification") {
            selectedTcInfoRemarks = binding.trainingCenterInfoLayout.etInfoRemarks.text.toString()
            if (!validateRemarks(selectedTcInfoRemarks)) return
        } else selectedTcInfoRemarks = ""

        if (USER_NAME_FOR_APP == request.loginId) {

        }
        else{
            viewModel.getTrainerCenterInfra(request)
        }


        navigateToNextSection(
            binding.trainingCenterInfoLayout.trainingInfoExpand,
            binding.trainingCenterInfoLayout.viewInfo,
            binding.trainingCenterInfoLayout.tvTrainInfo,
            binding.mainInfra,
            binding.viewInfra
        )
    }

    private fun handleInfraNext() {
        if (!validateApproval(selectedTcInfraApproval, "Approval")) return
        if (selectedTcInfraApproval == "Send for modification") {
            selectedTcInfraRemarks = binding.etInfraRemarks.text.toString()
            if (!validateRemarks(selectedTcInfraRemarks)) return
        } else selectedTcInfraRemarks = ""

         if (USER_NAME_FOR_APP == request.loginId) {

        }
       else{
             viewModel.getTcAcademicNonAcademicArea(request)
       }

        navigateToNextSection(
            binding.trainingInfraExpand,
            binding.viewInfra,
            binding.tvTrainInfra,
            binding.mainDescAcademia,
            binding.viewDescAcademia
        )
    }

    private fun handleInfraPrevious() {
        navigateToPreviousSection(
            binding.trainingCenterInfoLayout.trainingInfoExpand,
            binding.trainingCenterInfoLayout.viewInfo,
            binding.mainInfra,
            binding.viewInfra
        )
    }

    private fun handleDescAcademiaNext() {
        if (!validateApproval(selectedTcDescAcademiaApproval, "Approval")) return
        if (selectedTcDescAcademiaApproval == "Send for modification") {
            selectedTcDescAcademiaRemarks = binding.etDescAcademiaRemarks.text.toString()
            if (!validateRemarks(selectedTcDescAcademiaRemarks)) return
        } else selectedTcDescAcademiaRemarks = ""
        if (USER_NAME_FOR_APP == request.loginId) {
            binding. yesNoMaleToilet.text="19"


            binding. yesNoMaleUrinals.text="19"
            binding. yesNoMaleWashBasin.text="17"
            binding. yesNoFemaleToilet.text="26"
            binding. yesNoFemaleWashBasin.text="29"
            binding. yesNoTypeOfFlooring.text="Yes"
        }
        else{
            viewModel.getTcToiletWashBasin(request)
        }


        navigateToNextSection(
            binding.trainingDescAcademiaExpand,
            binding.viewDescAcademia,
            binding.tvTrainDescAcademia,
            binding.mainToilet,
            binding.viewToilet
        )
    }

    private fun handleDescAcademiaPrevious() {
        navigateToPreviousSection(
            binding.trainingInfraExpand,
            binding.viewInfra,
            binding.mainDescAcademia,
            binding.viewDescAcademia
        )
    }

    private fun handleBasinNext() {
        if (!validateApproval(selectedTcBasinApproval, "Approval")) return
        if (selectedTcBasinApproval == "Send for modification") {
            selectedTcBasinRemarks = binding.etBasinRemarks.text.toString()
            if (!validateRemarks(selectedTcBasinRemarks)) return
        } else selectedTcBasinRemarks = ""

        if (USER_NAME_FOR_APP == request.loginId) {

            binding.valueCorridorNo.text = "25"
            binding.valueLenghth.text = "34"
            binding.valueWidth.text ="69"
            binding.valueArea.text ="25"
            binding.valueLights.text = "45"
            binding.yesNoFans.text ="65"
            binding.yesNoCirculationArea.text = "250"
            binding.yesNoOpenSpace.text = "Yes"
            binding.yesNoParking.text = "Yes"




        }
        else{
            viewModel.getDescriptionOtherArea(request)
        }


        navigateToNextSection(
            binding.trainingToiletExpand,
            binding.viewToilet,
            binding.tvTrainToilet,
            binding.mainDescOfOtherArea,
            binding.viewDescOfOtherArea
        )
    }

    private fun handleBasinPrevious() {
        navigateToPreviousSection(
            binding.trainingDescAcademiaExpand,
            binding.viewDescAcademia,
            binding.mainToilet,
            binding.viewToilet
        )
    }

    private fun handleDescOtherAreaNext() {
        if (!validateApproval(selectedTcDescOtherAreaApproval, "Approval")) return
        if (selectedTcDescOtherAreaApproval == "Send for modification") {
            selectedTcDescOtherAreaRemarks = binding.etDescOtherAreaRemarks.text.toString()
            if (!validateRemarks(selectedTcDescOtherAreaRemarks)) return
        } else selectedTcDescOtherAreaRemarks = ""

        if (USER_NAME_FOR_APP == request.loginId) {



           binding. yesNoTrade.text = "Yes"
           binding. yesNoNatureofTraining.text = "No"
           binding. yesNoTradeAsPerProject.text = "Yes"
           binding. yesNoIsTrainingPlanAvail.text = "No"
           binding. yesNoIsDomainCirAvail.text = "Yes"
           binding. yesNoIsActivityCumLess.text = "Yes"
           binding. yesNoIsWelcomeKitAvail.text = "Yes"
           binding. yesNoNameOfCertifyingAg.text = "Yes"
           binding. yesNoAssessmentMaterial.text = "No"
        }
        else{
            viewModel.getTeachingLearningMaterial(request)
        }


        navigateToNextSection(
            binding.trainingDescOfOtherAreaExpand,
            binding.viewDescOfOtherArea,
            binding.tvTrainDescOfOtherArea,
            binding.mainTeaching,
            binding.viewTeaching
        )
    }

    private fun handleDescOtherAreaPrevious() {
        navigateToPreviousSection(
            binding.trainingToiletExpand,
            binding.viewToilet,
            binding.mainDescOfOtherArea,
            binding.viewDescOfOtherArea
        )
    }

    private fun handleTeachingNext() {
        if (!validateApproval(selectedTcTeachingApproval, "Approval")) return
        if (selectedTcTeachingApproval == "Send for modification") {
            selectedTcTeachingRemarks = binding.etTeachingRemarks.text.toString()
            if (!validateRemarks(selectedTcTeachingRemarks)) return
        } else selectedTcTeachingRemarks = ""
        if (USER_NAME_FOR_APP == request.loginId) {

            binding.yesNoSignOfLiakage.text = "Yes"
            binding.yesNoProtectionOfStairs.text = "No"
            binding.yesNoconformanceDDUGKY.text = "Yes"
            binding.yesNoCandidateComeSafely.text = "No"




        }
        else{
            viewModel.getGeneralDetails(request)
        }


        navigateToNextSection(
            binding.trainingTeachingExpand,
            binding.viewTeaching,
            binding.tvTrainTeaching,
            binding.mainGeneralDetails,
            binding.viewGeneralDetails
        )
    }

    private fun handleTeachingPrevious() {
        navigateToPreviousSection(
            binding.trainingDescOfOtherAreaExpand,
            binding.viewDescOfOtherArea,
            binding.mainTeaching,
            binding.viewTeaching
        )
    }

    private fun handleGeneralNext() {
        if (!validateApproval(selectedTcGeneralApproval, "Approval")) return
        if (selectedTcGeneralApproval == "Send for modification") {
            selectedTcGeneralRemarks = binding.etGeneralRemarks.text.toString()
            if (!validateRemarks(selectedTcGeneralRemarks)) return
        } else selectedTcGeneralRemarks = ""

        if (USER_NAME_FOR_APP == request.loginId) {
            binding.yesNoSecuringWire.text = "Yes"
            binding.yesNoSwitchBoard.text = "Yes"
        }
        else{
            viewModel.getElectricalWiringStandard(request)
        }


        navigateToNextSection(
            binding.trainingGeneralDetailsExpand,
            binding.viewGeneralDetails,
            binding.tvTrainGeneralDetails,
            binding.mainElectricalDetails,
            binding.viewElectricalDetails
        )
    }

    private fun handleGeneralPrevious() {
        navigateToPreviousSection(
            binding.trainingTeachingExpand,
            binding.viewTeaching,
            binding.mainGeneralDetails,
            binding.viewGeneralDetails
        )
    }

    private fun handleElectricalNext() {
        if (!validateApproval(selectedTcElectricalApproval, "Approval")) return
        if (selectedTcElectricalApproval == "Send for modification") {
            selectedTcElectricalRemarks = binding.etElectricalRemarks.text.toString()
            if (!validateRemarks(selectedTcElectricalRemarks)) return
        } else selectedTcElectricalRemarks = ""

        if (USER_NAME_FOR_APP == request.loginId) {
            binding.signageLayout.apply {
            yesNoCenterNameBoard.text = "Yes"
            yesNoSummaryAcheivement.text = "No"
            yesNoStudentEntitlement.text = "No"
            yesNoContactDetail.text = "No"
            yesNoBasicInfoBoard.text = "Yes"
            yesNoCodeOfConduct.text = "No"
            yesNoAttendanceSummary.text = "Yes"
        }}
        else{
            viewModel.getSignagesAndInfoBoard(request)
        }


        navigateToNextSection(
            binding.trainingElectricalDetailsExpand,
            binding.viewElectricalDetails,
            binding.tvTrainElectricalDetails,
            binding.mainSignageBoardDetails,
            binding.signageLayout.viewSignageBoardDetails
        )
    }

    private fun handleElectricalPrevious() {
        navigateToPreviousSection(
            binding.trainingGeneralDetailsExpand,
            binding.viewGeneralDetails,
            binding.mainElectricalDetails,
            binding.viewElectricalDetails
        )
    }

    private fun handleSignageNext() {
        if (!validateApproval(selectedTcSignageApproval, "Approval")) return
        if (selectedTcSignageApproval == "Send for modification") {
            selectedTcSignageRemarks = binding.signageLayout.etSignageRemarks.text.toString()
            if (!validateRemarks(selectedTcSignageRemarks)) return
        } else selectedTcSignageRemarks = ""

        if (USER_NAME_FOR_APP == request.loginId) {
            binding.ipCameraLayout.apply {
                yesNoCentralMonitor.text = "Yes"
                yesNoConformanceCCTV.text = "Yes"
                yesNoStorageCCTV.text ="Yes"
                yesNoDvrStaticIP.text = "Yes"
                yesNoIpEnabled.text ="Yes"
                yesNoResolution.text = "Yes"
                yesNoVideoStream.text = "Yes"
                yesNoRemoteAccessWeb.text = "Yes"
                yesNoRemoteAccessUsers.text = "Yes"
                yesNoSupportedProtocols.text ="Yes"
                yesNoColorAudio.text = "Yes"
                yesNoStorageFacility.text = "Yes"
            }
        }
        else{
            viewModel.getIpEnabledCamera(request)
        }


        navigateToNextSection(
            binding.signageLayout.trainingSignageBoardlDetailsExpand,
            binding.signageLayout.viewSignageBoardDetails,
            binding.signageLayout.tvTrainSignageBoardDetails,
            binding.mainIPEnableCameraDetails,
            binding.ipCameraLayout.viewIPEnableCameraDetails
        )
    }

    private fun handleSignagePrevious() {
        navigateToPreviousSection(
            binding.trainingElectricalDetailsExpand,
            binding.viewElectricalDetails,
            binding.mainSignageBoardDetails,
            binding.signageLayout.viewSignageBoardDetails
        )
    }

    private fun handleIpEnableNext() {
        if (!validateApproval(selectedTcIpEnableApproval, "Approval")) return
        if (selectedTcIpEnableApproval == "Send for modification") {
            selectedTcIpEnableRemarks = binding.ipCameraLayout.etIpEnableRemarks.text.toString()
            if (!validateRemarks(selectedTcIpEnableRemarks)) return
        } else selectedTcIpEnableRemarks = ""

        if (USER_NAME_FOR_APP == request.loginId) {
            binding.commonEquipmentLayout.apply {
                yesNoElectricalPowerBackup.text ="Yes"
                yesNoBiometricDevices.text = "Yes"
                yesNoCCTVMonitor.text = "Yes"
                yesNoStorageDocs.text = "Yes"
                yesNoPrinterScanner.text = "36"
                yesNoDigitalCamera.text = "26"
                yesNoGrievanceRegister.text = "Yes"
                yesNoMinEquipment.text = "Yes"
                yesNoDirectionBoards.text = "Yes"
            }
        }
        else{
            viewModel.getCommonEquipment(request)
        }


        navigateToNextSection(
            binding.ipCameraLayout.trainingIPEnableCameralDetailsExpand,
            binding.ipCameraLayout.viewIPEnableCameraDetails,
            binding.ipCameraLayout.tvTrainIPEnableCameraDetails,
            binding.mainCommonEquipmentDetails,
            binding.commonEquipmentLayout.viewCommonEquipmentDetails
        )
    }

    private fun handleIpEnablePrevious() {
        navigateToPreviousSection(
            binding.signageLayout.trainingSignageBoardlDetailsExpand,
            binding.signageLayout.viewSignageBoardDetails,
            binding.mainIPEnableCameraDetails,
            binding.ipCameraLayout.viewIPEnableCameraDetails
        )
    }

    private fun handleCommonEquipmentNext() {
        if (!validateApproval(selectedTcCommonEquipmentApproval, "Approval")) return
        if (selectedTcCommonEquipmentApproval == "Send for modification") {
            selectedTcCommonEquipmentRemarks = binding.commonEquipmentLayout.etCommonEquipmentRemarks.text.toString()
            if (!validateRemarks(selectedTcCommonEquipmentRemarks)) return
        } else selectedTcCommonEquipmentRemarks = ""

        if (USER_NAME_FOR_APP == request.loginId) {
            binding.availSupportInfraLayout.apply {
                yesNoSafeDrinkingWater.text = "Yes"
                yesNoFireFighting.text = "No"
                yesNoFirstAidKit.text = "Yes"
            }
        }
        else{
            viewModel.getAvailabilitySupportInfra(request)
        }


        navigateToNextSection(
            binding.commonEquipmentLayout.trainingCommonEquipmentDetailsExpand,
            binding.commonEquipmentLayout.viewCommonEquipmentDetails,
            binding.commonEquipmentLayout.tvTrainCommonEquipmentDetails,
            binding.mainAvailSupportInfra,
            binding.availSupportInfraLayout.viewAvailSupportInfra
        )
    }

    private fun handleCommonEquipmentPrevious() {
        navigateToPreviousSection(
            binding.ipCameraLayout.trainingIPEnableCameralDetailsExpand,
            binding.ipCameraLayout.viewIPEnableCameraDetails,
            binding.mainCommonEquipmentDetails,
            binding.commonEquipmentLayout.viewCommonEquipmentDetails
        )
    }

    private fun handleAvailSupportInfraNext() {
        if (!validateApproval(selectedTcAvailSupportInfraApproval, "Approval")) return
        if (selectedTcAvailSupportInfraApproval == "Send for modification") {
            selectedTcAvailSupportInfraRemarks = binding.availSupportInfraLayout.etAvailSupportInfraRemarks.text.toString()
            if (!validateRemarks(selectedTcAvailSupportInfraRemarks)) return
        } else selectedTcAvailSupportInfraRemarks = ""


        if (USER_NAME_FOR_APP == request.loginId) {
            with(binding.availOfStandardFormsLayout) {
                yesNoPlanOfTraining.text= "Yes"
                yesNoLessonPlanner.text= "Yes"
                yesNoOnJobTraining.text= "Yes"
                yesNoDailyTablets.text="Yes"
                yesNoStudentEntitlementBanner.text =      "Yes"
                yesNoParentsConsentForm.text= "Yes"
                yesNoCandidateAttendanceRegister.text    = "Yes"
                yesNoTrainerAttendanceRegister.text      = "Yes"
                yesNoItemsChecklist.text= "Yes"
                yesNoEvaluationSummary.text= "Yes"
                yesNoTADARecord.text= "Yes"
                yesNoTrainingCertificate.text= "Yes"
                yesNoTrainingCompletionCertificateRecord.text = "Yes"
                yesNoEquipmentTrainingCentre.text= "Yes"
                yesNoEquipmentAccommodation.text= "Yes"
                yesNoTrainingCentreInspection.text       = "Yes"
                yesNoAssessmentCertification.text        = "Yes"
                yesNoLetterSRLMInfo.text= "Yes"
                yesNoLetterFromSRLM.text= "Yes"
                yesNoOnFieldRegistration.text= "Yes"
                yesNoOverviewAptitudeTest.text           ="Yes"
                yesNoCandidateApplicationForm.text       = "Yes"
                yesNoTrainersProfile.text= "Yes"
                yesNoCandidatesEnrolled.text             = "Yes"
                yesNoCandidateDossierIndex.text          = "Yes"
                yesNoPerformanceCan.text                 = "No"
                yesNoListOfCandidateAfterBatchFreezing.text = "Yes"
                yesNoDailyFailureReport.text             ="Yes"
                yesNo15DaysSummary.text= "Yes"
                yesNoContentCounselling.text             = "Yes"
                yesNoCandidateIDTemplate.text            ="No"
                yesNoStaffSummary.text                   = "Yes"
                yesNoDullyIfApplicable.text              ="Yes"
                yesNoPerformanceTrainer.text             = "Yes"
                yesNoDully.text                          = "Yes"
                yesNoIpEnabled.text                     = "Yes"
            }
        }
        else{
            viewModel.getAvailabilityStandardForms(request)
        }

        navigateToNextSection(
            binding.availSupportInfraLayout.trainingAvailSupportInfraExpand,
            binding.availSupportInfraLayout.viewAvailSupportInfra,
            binding.availSupportInfraLayout.tvTrainAvailSupportInfra,
            binding.mainAvailOfStandardForms,
            binding.availOfStandardFormsLayout.viewAvailOfStandardForms
        )
    }

    private fun handleAvailSupportInfraPrevious() {
        navigateToPreviousSection(
            binding.commonEquipmentLayout.trainingCommonEquipmentDetailsExpand,
            binding.commonEquipmentLayout.viewCommonEquipmentDetails,
            binding.mainAvailSupportInfra,
            binding.availSupportInfraLayout.viewAvailSupportInfra
        )
    }

    private fun handleAvailOfStandardFormsNext() {
        if (!validateApproval(selectedTcAvailOfStandardFormApproval, "Approval")) return
        if (selectedTcAvailOfStandardFormApproval == "Send for modification") {
            selectedTcAvailOfStandardFormRemarks = binding.availOfStandardFormsLayout.etAvailOfStandardFormsRemarks.text.toString()
            if (!validateRemarks(selectedTcAvailOfStandardFormRemarks)) return
        } else selectedTcAvailOfStandardFormRemarks = ""


        if (USER_NAME_FOR_APP == request.loginId) {


            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.confirmation))
                .setMessage(getString(R.string.are_you_sure_you_want_to_submit_these_details))
                .setCancelable(false)

                .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }

                .setPositiveButton(getString(R.string.submit)) { dialog, _ ->

                    dialog.dismiss()

                    val intent = Intent(requireActivity(), MainActivity::class.java)

                    startActivity(intent)
                    requireActivity().finish()
                    setupRecyclerView()
                    setupAdapters()
                }
                .show()


        }
        else{
            showConfirmationDialog()
        }


    }

    private fun handleAvailOfStandardFormsPrevious() {
        navigateToPreviousSection(
            binding.availSupportInfraLayout.trainingAvailSupportInfraExpand,
            binding.availSupportInfraLayout.viewAvailSupportInfra,
            binding.mainAvailOfStandardForms,
            binding.availOfStandardFormsLayout.viewAvailOfStandardForms
        )
    }

    private fun validateApproval(approval: String, fieldName: String): Boolean {
        if (approval.isEmpty()) {
            showToast(getString(R.string.kindly_select_first, fieldName))
            return false
        }
        return true
    }

    private fun validateRemarks(remarks: String): Boolean {
        if (remarks.isEmpty()) {
            showToast(getString(R.string.kindly_enter_remarks_first))
            return false
        }
        return true
    }

    private fun navigateToNextSection(
        currentExpand: View,
        currentView: View,
        currentTitle: TextView,
        nextSection: View,
        nextView: View
    ) {
        currentExpand.hide()
        currentView.hide()
        currentTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
        nextSection.show()
        nextView.show()

        binding.scroll.post {
            binding.scroll.smoothScrollTo(0, 0)
        }
    }

    private fun navigateToPreviousSection(
        previousExpand: View,
        previousView: View,
        currentSection: View,
        currentView: View
    ) {
        previousExpand.show()
        previousView.show()
        currentSection.hide()
        currentView.hide()

        binding.scroll.post {
            binding.scroll.smoothScrollTo(0, 0)
        }
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirmation))
            .setMessage(getString(R.string.are_you_sure_you_want_to_submit_these_details))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.submit)) { dialog, _ ->
                submitQTeamForm()
                dialog.dismiss()
            }
            .show()
    }


    private fun submitQTeamForm() {
        showProgressDialog(getString(R.string.submitting))
        if(AppUtil.getSavedLoginIdPreference(requireContext()) == BuildConfig.USER_NAME_FOR_APP){
            showToast(getString(R.string.data_successfully_saved))
            dismissProgressDialog()
            findNavController().popBackStack()
            return
        }
        val requestTcQTeamSubmit = TcQTeamInsertReq(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            latitude = latValue,
            longitude = langValue,

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




        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.insertQTeamVerification(requestTcQTeamSubmit)
        }

        binding.availOfStandardFormsLayout.viewAvailOfStandardForms.hide()
        binding.availOfStandardFormsLayout.trainingAvailOfStandardFormsExpand.hide()
        binding.availOfStandardFormsLayout.tvTrainAvailOfStandardForms.setCompoundDrawablesWithIntrinsicBounds(
            0, 0, R.drawable.ic_verified, 0
        )

        binding.scroll.post {
            binding.scroll.smoothScrollTo(0, 0)
        }
    }

    private fun hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocation = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return fineLocation == PackageManager.PERMISSION_GRANTED || coarseLocation == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted || coarseLocationGranted) {
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), getString(R.string.location_permission_denied), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getCurrentLocation() {
        // Uses high accuracy priority for precise location
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    // binding.basicInfoInclude?.tvLatLang?.text = location.latitude.toString() + "," + location.longitude.toString()
                    latValue = location.latitude.toString()
                    langValue = location.longitude.toString()
                } else {

                    Toast.makeText(requireContext(),
                        getString(R.string.unable_to_get_location), Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(), getString(R.string.failed_to_get_location)+": ${it.message}", Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun showTrainerStaffDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_trainer_staff)

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvTrainerStaff)
        val closeButton = dialog.findViewById<TextView>(R.id.tvClose)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = TrainerStaffAdapter(dataStaffList)

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun handleRoomItemClick(room: RoomItem) {
        when (room.roomType) {
            "Theory Class Room" -> showTheoryClassRoomDialog(room)
            "Office Cum Counselling Room" -> showOfficeCumCounsellingRoomDialog(room)
            "Reception Area" -> showReceptionAreaDialog(room)
            "Counselling Room" -> showCounsellingRoomDialog(room)
            "Office Room" -> showOfficeRoomDialog(room)
            "IT cum Domain Lab" -> showItCumDomainLabDialog(room)
            "Theory Cum IT Lab" -> showTheoryCumItLabDialog(room)
            "IT Lab" -> showItLabDialog(room)
            "Domain Lab" -> showDomainLabDialog(room)
            "Theory Cum Domain Lab" -> showTheoryCumDomainLabDialog(room)
            else -> showToast("No layout found for ${room.roomType}")
        }
    }

    private fun showTheoryClassRoomDialog(room: RoomItem) {
        showProgressDialog()
        val binding = TheoryClassRoomBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


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

            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonResponse = gson.toJson(result)
            Log.d("getAcademicRoomDetails", "✅ Success Response:\n$jsonResponse")

            result.onSuccess { response ->
                dismissProgressDialog()
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
                    binding.apply {
                        yesNoTypeOfRoof.text = safeText(data.roofType)
                        yesNoFalseCeiling.text = safeText(data.falseCeiling)
                        yesNoHeightCeiling.text = safeText(data.ceilingHeight.toString())
                        yesNoVentilationArea.text = safeText(data.ventilationArea.toString())
                        yesNoSoundLevel.text = safeText(data.soundLevel.toString())
                        yesNoSoundProofAC.text = safeText(data.centerSoundProof)
                        yesNoInfoBoard.text = safeText(data.roomInfoBoard)
                        yesNoInternalSignage.text = safeText(data.internalSignage)
                        yesNoCCTV.text = safeText(data.audioCamera)
                        yesNoLCDComputers.text = safeText(data.digitalProjector)
                        yesNoChairForCan.text = safeText(data.candidateChair)
                        yesNoWritingBoard.text = safeText(data.writingBoard)
                        yesNoTrainerChair.text = safeText(data.trainerChair)
                        yesNoTrainerTable.text = safeText(data.trainerTable)
                        yesNoLights.text = safeText(data.lights.toString())
                        yesNoFans.text = safeText(data.fans.toString())
                        yesNoPowerBackup.text = safeText(data.ecPowerBackup)
                        yesNoLabPhoto.text = safeText(data.roomsPhotographs)
                        yesNoAirConditioning.text = safeText(data.airConditionRoom)
                       // data.lightAiCount
                    }
                    fansRoomImage=safeText(data.fansImage)
                    setupTheoryClassRoomImageClicks(binding, data)
//                    showBase64Image(imagesMap) { imageView ->
//                        when (imageView.id) {
//                            R.id.ivProofPreview -> showBadgeSafely(imageView, x.lightAiCount ?: 0, ObjectType.LIGHT.name)
                            //R.id.ivProofFanPreview -> showBadgeSafely(imageView, x.fanAiCount ?: 0, ObjectType.FAN.name)
//                        }
//                    }
                } else {
                    showToast(getString(R.string.no_data_available))
                }
            }

            result.onFailure {
                dismissProgressDialog()
                showErrorToast(getString(R.string.failed, it.message))
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun setupTheoryClassRoomImageClicks(binding: TheoryClassRoomBinding, data: Any) {
        val c= data as? RoomDetail
        binding.valueTypeOfRoof.setOnClickListener { showBase64ImageDialog(roofTypeImage, "Roof Type Image") }
        binding.valueFalseCeiling.setOnClickListener { showBase64ImageDialog(falseCeilingImage, "False Ceiling Image") }
        binding.valueHeightCeiling.setOnClickListener { showBase64ImageDialog(ceilingHeightImage, "Ceiling Height Image") }
        binding.valueVentilationArea.setOnClickListener { showBase64ImageDialog(ventilationAreaImage, "Ventilation Area Image") }
        binding.valueSoundLevel.setOnClickListener { showBase64ImageDialog(soundLevelImage, "Sound Level Image") }
        binding.valueSoundProofAC.setOnClickListener { showBase64ImageDialog(centerSoundProofImage, "Sound Proof & AC Image") }
        binding.valueInfoBoard.setOnClickListener { showBase64ImageDialog(roomInfoBoardImage, "Information Board Image") }
        binding.valueInternalSignage.setOnClickListener { showBase64ImageDialog(internalSignageImage, "Internal Signage Image") }
        binding.valueCCTV.setOnClickListener { showBase64ImageDialog(audioCameraImage, "CCTV & Audio Image") }
        binding.valueLCDComputers.setOnClickListener { showBase64ImageDialog(digitalProjectorImage, "Digital Projector / LCD Image") }
        binding.valueChairForCan.setOnClickListener { showBase64ImageDialog(candidateChairImage, "Candidate Chair Image") }
        binding.valueWritingBoard.setOnClickListener { showBase64ImageDialog(writingBoardImage, "Writing Board Image") }
        binding.valueTrainerChair.setOnClickListener { showBase64ImageDialog(trainerChairImage, "Trainer Chair Image") }
        binding.valueTrainerTable.setOnClickListener { showBase64ImageDialog(trainerTableImage, "Trainer Table Image") }
        binding.valueLights.setOnClickListener { showBase64ImageWithCountDialog(lightsImage, "Lights Image",c?.lightAiCount.toString() ) }
        binding.valueFans.setOnClickListener { showBase64ImageWithCountDialog(fansRoomImage, "Fans Image",c?.fanAiCount.toString()) }
        binding.valuePowerBackup.setOnClickListener { showBase64ImageDialog(ecPowerBackupImage, "Power Backup Image") }
        binding.valueITLabPhoto.setOnClickListener { showBase64ImageDialog(roomsPhotographsImage, "Room Photos") }
        binding.valueAirConditioning.setOnClickListener { showBase64ImageDialog(airConditionRoomImage, "Air Conditioning Image") }
    }

    private fun showOfficeCumCounsellingRoomDialog(room: RoomItem) {
        showProgressDialog()
        val binding = OfficeCumCouncelingRoomLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


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
            result.onSuccess { response ->
                dismissProgressDialog()
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
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

                    setupOfficeCumCounsellingImageClicks(binding, data)
                } else {
                    showToast(getString(R.string.no_data_available))
                }
            }

            result.onFailure {
                dismissProgressDialog()
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun setupOfficeCumCounsellingImageClicks(binding: OfficeCumCouncelingRoomLayoutBinding, data: Any) {
        binding.valueOfficeRoomPhoto.setOnClickListener { showBase64ImageDialog(roomsPhotographsImage, "Room Photo") }
        binding.valueRoofType.setOnClickListener { showBase64ImageDialog(roofTypeImage, "Roof Type Image") }
        binding.valueFalseCeiling.setOnClickListener { showBase64ImageDialog(falseCeilingImage, "False Ceiling Image") }
        binding.valueCeilingHeight.setOnClickListener { showBase64ImageDialog(ceilingHeightImage, "Ceiling Height Image") }
        binding.valueStorage.setOnClickListener { showBase64ImageDialog(secureDocumentStorageImage, "Storage Image") }
        binding.valueOfficeTable.setOnClickListener { showBase64ImageDialog(officeTableImage, "Office Table Image") }
        binding.valueChairs.setOnClickListener { showBase64ImageDialog(officeChairImage, "Chairs Image") }
        binding.valueComputerTable.setOnClickListener { showBase64ImageDialog(officeComputerImagePath, "Computer Table Image") }
        binding.valuePrinter.setOnClickListener { showBase64ImageDialog(printerScannerImage, "Printer / Scanner Image") }
        binding.valueCamera.setOnClickListener { showBase64ImageDialog(digitalCameraImage, "Digital Camera Image") }
        binding.valuePowerBackup.setOnClickListener { showBase64ImageDialog(ecPowerBackupImage, "Power Backup Image") }
    }

    private fun showReceptionAreaDialog(room: RoomItem) {
        showProgressDialog()
        val binding = ReceptionAreaLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


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
            result.onSuccess { response ->
                dismissProgressDialog()
                val data = response.wrappedList.firstOrNull()
                Log.d("getAcademicRoomDetails", response.toString())

                if (data != null) {
                    binding.yesNoReceptionAreaPhoto.text = safeText(data.roomsPhotographs)
                    binding.valueReceptionAreaPhoto.setOnClickListener {
                        showBase64ImageDialog(data.roomsPhotographsImage, "Reception Area Photo")
                    }
                } else {
                    showToast(getString(R.string.no_data_available))
                }
            }

            result.onFailure {
                dismissProgressDialog()
                showErrorToast(getString(R.string.failed, it.message))
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun showCounsellingRoomDialog(room: RoomItem) {
        showProgressDialog()
        val binding = CounsellingRoomBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


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
            result.onSuccess { response ->
                dismissProgressDialog()
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
                    binding.yesNoCounsellingAreaPhoto.text = safeText(data.roomsPhotographs)
                    binding.valueCounsellingAreaPhoto.setOnClickListener {
                        showBase64ImageDialog(data.roomsPhotographsImage, "Counselling Area Photo")
                    }
                } else {
                    showToast(getString(R.string.no_data_available))
                }
            }

            result.onFailure {
                dismissProgressDialog()
                showErrorToast("Failed: ${it.message}")
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun showOfficeRoomDialog(room: RoomItem) {
        showProgressDialog()
        val binding = OfficeRoomLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


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
            result.onSuccess { response ->
                dismissProgressDialog()
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
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

                    setupOfficeRoomImageClicks(binding, data)
                } else {
                    showToast(getString(R.string.no_data_available))
                }
            }

            result.onFailure {
                dismissProgressDialog()
                showErrorToast("Failed: ${it.message}")
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun setupOfficeRoomImageClicks(binding: OfficeRoomLayoutBinding, data: Any) {
        binding.valueOfficeRoomPhoto.setOnClickListener { showBase64ImageDialog(roomsPhotographsImage, "Office Room Photo") }
        binding.valueRoofType.setOnClickListener { showBase64ImageDialog(roofTypeImage, "Roof Type Image") }
        binding.valueFalseCeiling.setOnClickListener { showBase64ImageDialog(falseCeilingImage, "False Ceiling Image") }
        binding.valueCeilingHeight.setOnClickListener { showBase64ImageDialog(ceilingHeightImage, "Ceiling Height Image") }
        binding.valueStorage.setOnClickListener { showBase64ImageDialog(secureDocumentStorageImage, "Storage Place Image") }
        binding.valueOfficeTable.setOnClickListener { showBase64ImageDialog(officeTableImage, "Office Table Image") }
        binding.valueChairs.setOnClickListener { showBase64ImageDialog(officeChairImage, "Chairs Image") }
        binding.valueComputerTable.setOnClickListener { showBase64ImageDialog(officeComputerImagePath, "Computer Table Image") }
        binding.valuePrinter.setOnClickListener { showBase64ImageDialog(printerScannerImage, "Printer / Scanner Image") }
        binding.valueCamera.setOnClickListener { showBase64ImageDialog(digitalCameraRoomImage, "Digital Camera Image") }
        binding.valuePowerBackup.setOnClickListener { showBase64ImageDialog(ecPowerBackupImage, "Power Backup Image") }
    }

    private fun showItCumDomainLabDialog(room: RoomItem) {
        showProgressDialog()
        val binding = ItCumDomainLabLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)


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
            result.onSuccess { response ->
                dismissProgressDialog()
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
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
                    fansImage= safeText(data.fansImage)
                    setupItCumDomainLabImageClicks(binding, data)
                } else {
                    showToast(getString(R.string.no_data_available))
                }
            }

            result.onFailure {
                dismissProgressDialog()
                showErrorToast(getString(R.string.failed, it.message))
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun setupItCumDomainLabImageClicks(binding: ItCumDomainLabLayoutBinding, data: RoomDetail) {
        binding.valueTypeOfRoof.setOnClickListener { showBase64ImageDialog(roofTypeImage, "Roof Type") }
        binding.valueFalseCeiling.setOnClickListener { showBase64ImageDialog(falseCeilingImage, "False Ceiling") }
        binding.valueHeightCeiling.setOnClickListener { showBase64ImageDialog(ceilingHeightImage, "Height of Ceiling") }
        binding.valueVentilationArea.setOnClickListener { showBase64ImageDialog(ventilationAreaImage, "Ventilation Area") }
        binding.valueSoundLevel.setOnClickListener { showBase64ImageDialog(soundLevelImage, "Sound Level") }
        binding.valueSoundProofAC.setOnClickListener { showBase64ImageDialog(centerSoundProofImage, "Sound Proof / AC") }
        binding.valueInfoBoard.setOnClickListener { showBase64ImageDialog(roomInfoBoardImage, "Information Board") }
        binding.valueInternalSignage.setOnClickListener { showBase64ImageDialog(internalSignageImage, "Internal Signage") }
        binding.valueCCTV.setOnClickListener { showBase64ImageDialog(audioCameraImage, "CCTV Camera") }
        binding.valueLANComputers.setOnClickListener { showBase64ImageDialog(lanEnabledImage, "LAN Computers") }
        binding.valueInternet.setOnClickListener { showBase64ImageDialog(internetConnectionImage, "Internet Connection") }
        binding.valueTypingTutor.setOnClickListener { showBase64ImageDialog(typingTuterCompImage, "Typing Tutor") }
        binding.valueTablets.setOnClickListener { showBase64ImageDialog(tabletImage, "Tablets") }
        binding.valueTrainerChair.setOnClickListener { showBase64ImageDialog(trainerChairImage, "Trainer Chair") }
        binding.valueTrainerTable.setOnClickListener { showBase64ImageDialog(trainerTableImage, "Trainer Table") }
        binding.valueLights.setOnClickListener { showBase64ImageWithCountDialog(lightsImage, "Lights",data?.lightAiCount.toString()) }
        binding.valueFans.setOnClickListener { showBase64ImageWithCountDialog(fansImage, "Fans",data?.fanAiCount.toString()) }
        binding.valuePowerBackup.setOnClickListener { showBase64ImageDialog(ecPowerBackupImage, "Power Backup") }
        binding.valueAirConditioning.setOnClickListener { showBase64ImageDialog(airConditionRoomImage, "Air Conditioning") }
        binding.valueITLabPhoto.setOnClickListener { showBase64ImageDialog(roomsPhotographsImage, "IT cum Domain Lab Photo") }
        binding.valueStools.setOnClickListener { showBase64ImageDialog(candidateChairImage, "Domain Related Equipment") }
    }

    private fun showTheoryCumItLabDialog(room: RoomItem) {
        showProgressDialog()
        val binding = TheoryCumItLabLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


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
            result.onSuccess { response ->
                dismissProgressDialog()
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
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

                    setupTheoryCumItLabImageClicks(binding, data)
                } else {
                    showToast(getString(R.string.no_data_available))
                }
            }

            result.onFailure {
                dismissProgressDialog()
                showErrorToast(getString(R.string.failed, it.message))
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun setupTheoryCumItLabImageClicks(binding: TheoryCumItLabLayoutBinding, data: RoomDetail) {
        binding.valueTypeOfRoof.setOnClickListener { showBase64ImageDialog(roofTypeImage, "Type of Roof") }
        binding.valueFalseCeiling.setOnClickListener { showBase64ImageDialog(falseCeilingImage, "False Ceiling") }
        binding.valueHeightCeiling.setOnClickListener { showBase64ImageDialog(ceilingHeightImage, "Height of Ceiling") }
        binding.valueVentilationArea.setOnClickListener { showBase64ImageDialog(ventilationAreaImage, "Ventilation Area") }
        binding.valueSoundLevel.setOnClickListener { showBase64ImageDialog(soundLevelImage, "Sound Level") }
        binding.valueSoundProofAC.setOnClickListener { showBase64ImageDialog(centerSoundProofImage, "Sound Proof & AC") }
        binding.valueInfoBoard.setOnClickListener { showBase64ImageDialog(roomInfoBoardImage, "Room Info Board") }
        binding.valueInternalSignage.setOnClickListener { showBase64ImageDialog(internalSignageImage, "Internal Signage") }
        binding.valueCCTV.setOnClickListener { showBase64ImageDialog(audioCameraImage, "CCTV Cameras") }
        binding.valueLANComputers.setOnClickListener { showBase64ImageDialog(lanEnabledImage, "LAN Enabled Computers") }
        binding.valueInternet.setOnClickListener { showBase64ImageDialog(internetConnectionImage, "Internet Connection") }
        binding.valueTypingTutor.setOnClickListener { showBase64ImageDialog(typingTuterCompImage, "Typing Tutor Computers") }
        binding.valueTablets.setOnClickListener { showBase64ImageDialog(tabletImage, "Tablets") }
        binding.valueStools.setOnClickListener { showBase64ImageDialog(candidateChairImage, "Candidate Chair") }
        binding.valueTrainerChair.setOnClickListener { showBase64ImageDialog(trainerChairImage, "Trainer Chair") }
        binding.valueTrainerTable.setOnClickListener { showBase64ImageDialog(trainerTableImage, "Trainer Table") }
        binding.valueLights.setOnClickListener { showBase64ImageWithCountDialog(lightsImage, "Lights",data?.lightAiCount.toString()) }
        binding.valueFans.setOnClickListener { showBase64ImageWithCountDialog(fansRoomImage, "Fans",data?.fanAiCount.toString()) }
        binding.valuePowerBackup.setOnClickListener { showBase64ImageDialog(ecPowerBackupImage, "Power Backup") }
        binding.valueITLabPhoto.setOnClickListener { showBase64ImageDialog(roomsPhotographsImage, "IT Lab Photograph") }
        binding.valuedomainrelatedequipPhoto.setOnClickListener { showBase64ImageDialog(domainEquipmentImage, "Domain Equipment") }
        binding.valueAirConditioning.setOnClickListener { showBase64ImageDialog(airConditionRoomImage, "Air Conditioning") }
    }

    private fun showItLabDialog(room: RoomItem) {
        showProgressDialog()
        val binding = ItLabLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)


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
            result.onSuccess { response ->
                dismissProgressDialog()
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
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

                    setupItLabImageClicks(binding, data)
                } else {
                    showToast(getString(R.string.no_data_available))
                }
            }

            result.onFailure {
                dismissProgressDialog()
                showErrorToast(getString(R.string.failed, it.message))
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun setupItLabImageClicks(binding: ItLabLayoutBinding, data: RoomDetail) {
        binding.valueTypeOfRoof.setOnClickListener { showBase64ImageDialog(roofTypeImage, "Type of Roof") }
        binding.valueFalseCeiling.setOnClickListener { showBase64ImageDialog(falseCeilingImage, "False Ceiling") }
        binding.valueHeightCeiling.setOnClickListener { showBase64ImageDialog(ceilingHeightImage, "Height of Ceiling") }
        binding.valueVentilationArea.setOnClickListener { showBase64ImageDialog(ventilationAreaImage, "Ventilation Area") }
        binding.valueSoundLevel.setOnClickListener { showBase64ImageDialog(soundLevelImage, "Sound Level") }
        binding.valueSoundProofAC.setOnClickListener { showBase64ImageDialog(centerSoundProofImage, "Sound Proof / AC") }
        binding.valueInfoBoard.setOnClickListener { showBase64ImageDialog(roomInfoBoardImage, "Information Board") }
        binding.valueInternalSignage.setOnClickListener { showBase64ImageDialog(internalSignageImage, "Internal Signage") }
        binding.valueCCTV.setOnClickListener { showBase64ImageDialog(audioCameraImage, "CCTV Camera") }
        binding.valueLANComputers.setOnClickListener { showBase64ImageDialog(lanEnabledImage, "LAN Computers") }
        binding.valueInternet.setOnClickListener { showBase64ImageDialog(internetConnectionImage, "Internet Connection") }
        binding.valueTypingTutor.setOnClickListener { showBase64ImageDialog(typingTuterCompImage, "Typing Tutor") }
        binding.valueTablets.setOnClickListener { showBase64ImageDialog(tabletImage, "Tablets") }
        binding.valueStools.setOnClickListener { showBase64ImageDialog(candidateChairImage, "Stools / Chairs") }
        binding.valueTrainerChair.setOnClickListener { showBase64ImageDialog(trainerChairImage, "Trainer Chair") }
        binding.valueTrainerTable.setOnClickListener { showBase64ImageDialog(trainerTableImage, "Trainer Table") }
        binding.valueLights.setOnClickListener { showBase64ImageWithCountDialog(lightsImage, "Lights",data?.lightAiCount.toString()) }
        binding.valueFans.setOnClickListener {showBase64ImageWithCountDialog(fansImage, title = "Fans",data?.fanAiCount.toString()) }
        binding.valuePowerBackup.setOnClickListener { showBase64ImageDialog(ecPowerBackupImage, "Power Backup") }
        binding.valueAirConditioning.setOnClickListener { showBase64ImageDialog(airConditionRoomImage, "Air Conditioning") }
        binding.valueITLabPhoto.setOnClickListener { showBase64ImageDialog(roomsPhotographsImage, "IT Lab Photo") }
    }

    private fun showDomainLabDialog(room: RoomItem) {
        showProgressDialog()
        val binding = DomainLabLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)


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
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonResponse = gson.toJson(result)
            Log.d("showDomainLabDialog", "✅ Success Response:\n$jsonResponse")
            result.onSuccess { response ->
                dismissProgressDialog()
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
                    binding.apply {
                        yesNoTypeOfRoof.text = safeText(data.roofType)
                        yesNoFalseCeiling.text = safeText(data.falseCeiling)
                        yesNoHeightCeiling.text = safeText(data.ceilingHeight)
                        yesNoVentilationArea.text = safeText(data.ventilationArea)
                        yesNoSoundLevel.text = safeText(data.soundLevel)
                        yesNoSoundProofAC.text = safeText(data.centerSoundProof)
                        yesNoInfoBoard.text = safeText(data.roomInfoBoard)
                        yesNoInternalSignage.text = safeText(data.internalSignage)
                        yesNoCCTV.text = safeText(data.audioCamera)
                        yesNoLCDComputers.text = safeText(data.lanEnabled)
                        yesNoChairForCan.text = safeText(data.candidateChair)
                        yesNoWritingBoard.text = safeText(data.writingBoard)
                        yesNoTrainerChair.text = safeText(data.trainerChair)
                        yesNoTrainerTable.text = safeText(data.trainerTable)
                        yesNoLights.text = safeText(data.lights)
                        yesNoFans.text = safeText(data.fans)
                        yesNoPowerBackup.text = safeText(data.ecPowerBackup)
                        yesNoLabPhoto.text = safeText(data.roomsPhotographs)
                        yesNodomainrelatedequipPhoto.text = safeText(data.domainEquipment)
                        yesNoAirConditioning.text = safeText(data.airConditionRoom)
                    }
                    setupDomainLabImageClicks(binding, data)
                } else {
                    showToast(getString(R.string.no_data_available))
                }
            }

            result.onFailure {
                dismissProgressDialog()
                showErrorToast(getString(R.string.failed, it.message))
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun setupDomainLabImageClicks(binding: DomainLabLayoutBinding, data: RoomDetail) {
        binding.apply {
            valueTypeOfRoof.setOnClickListener { showBase64ImageDialog(roofTypeImage, "Type of Roof") }
            valueFalseCeiling.setOnClickListener { showBase64ImageDialog(falseCeilingImage, "False Ceiling") }
            valueHeightCeiling.setOnClickListener { showBase64ImageDialog(ceilingHeightImage, "Height of Ceiling") }
            valueVentilationArea.setOnClickListener { showBase64ImageDialog(ventilationAreaImage, "Ventilation Area") }
            valueSoundLevel.setOnClickListener { showBase64ImageDialog(soundLevelImage, "Sound Level") }
            valueSoundProofAC.setOnClickListener { showBase64ImageDialog(centerSoundProofImage, "Sound Proof & AC") }
            valueInfoBoard.setOnClickListener { showBase64ImageDialog(roomInfoBoardImage, "Room Info Board") }
            valueInternalSignage.setOnClickListener { showBase64ImageDialog(internalSignageImage, "Internal Signage") }
            valueCCTV.setOnClickListener { showBase64ImageDialog(audioCameraImage, "CCTV & Audio") }
            valueLCDComputers.setOnClickListener { showBase64ImageDialog(lanEnabledImage, "LAN / LCD Digital Projector") }
            valueChairForCan.setOnClickListener { showBase64ImageDialog(candidateChairImage, "Chair for Candidates") }
            valueWritingBoard.setOnClickListener { showBase64ImageDialog(writingBoardImage, "Writing Board") }
            valueTrainerChair.setOnClickListener { showBase64ImageDialog(trainerChairImage, "Trainer Chair") }
            valueTrainerTable.setOnClickListener { showBase64ImageDialog(trainerTableImage, "Trainer Table") }
            valueLights.setOnClickListener { showBase64ImageWithCountDialog(lightsImage, "Lights",data?.lightAiCount.toString()) }
            valueFans.setOnClickListener { showBase64ImageWithCountDialog(fansRoomImage, "Fans",data?.fanAiCount.toString()) }
            valuePowerBackup.setOnClickListener { showBase64ImageDialog(ecPowerBackupImage, "Power Backup") }
            valueITLabPhoto.setOnClickListener { showBase64ImageDialog(roomsPhotographsImage, "Domain Lab Photo") }
            valuedomainrelatedequipPhoto.setOnClickListener { showBase64ImageDialog(domainEquipmentImage, "Domain Related Equipment") }
            valueAirConditioning.setOnClickListener { showBase64ImageDialog(airConditionRoomImage, "Air Conditioning") }
        }
    }


    private fun showTheoryCumDomainLabDialog(room: RoomItem) {
        showProgressDialog()
        val binding = TheoryCumDomainLabLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

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
            result.onSuccess { response ->
                dismissProgressDialog()
                val data = response.wrappedList.firstOrNull()
                if (data != null) {
                    binding.apply {
                        yesNoTypeOfRoof.text = safeText(data.roofType)
                        yesNoFalseCeiling.text = safeText(data.falseCeiling)
                        yesNoHeightCeiling.text = safeText(data.ceilingHeight)
                        yesNoVentilationArea.text = safeText(data.ventilationArea)
                        yesNoSoundLevel.text = safeText(data.soundLevel)
                        yesNoSoundProofAC.text = safeText(data.centerSoundProof)
                        yesNoInfoBoard.text = safeText(data.roomInfoBoard)
                        yesNoInternalSignage.text = safeText(data.internalSignage)
                        yesNoCCTV.text = safeText(data.audioCamera)
                        yesNoLANComputers.text = safeText(data.lanEnabled)
                        yesNoInternet.text = safeText(data.internetConnection)
                        yesNoTypingTutor.text = safeText(data.typingTuterComp)
                        yesNoTablets.text = safeText(data.tablet)
                        yesNoStools.text = safeText(data.candidateChair)
                        yesNoTrainerChair.text = safeText(data.trainerChair)
                        yesNoTrainerTable.text = safeText(data.trainerTable)
                        yesNoLights.text = safeText(data.lights)
                        yesNoFans.text = safeText(data.fans)
                        yesNoPowerBackup.text = safeText(data.ecPowerBackup)
                        yesNoLabPhoto.text = safeText(data.roomsPhotographs)
                        yesNodomainrelatedequipPhoto.text = safeText(data.domainEquipment)
                        yesNoAirConditioning.text = safeText(data.airConditionRoom)
                    }



                    setupTheoryCumDomainLabImageClicks(binding, data)
                } else {
                    showToast(getString(R.string.no_data_available))
                }
            }

            result.onFailure {
                dismissProgressDialog()
                showErrorToast(getString(R.string.failed, it.message))
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun setupTheoryCumDomainLabImageClicks(binding: TheoryCumDomainLabLayoutBinding, data: RoomDetail) {
        binding.apply {
            valueTypeOfRoof.setOnClickListener { showBase64ImageDialog(roofTypeImage, "Type of Roof") }
            valueFalseCeiling.setOnClickListener { showBase64ImageDialog(falseCeilingImage, "False Ceiling") }
            valueHeightCeiling.setOnClickListener { showBase64ImageDialog(ceilingHeightImage, "Height of Ceiling") }
            valueVentilationArea.setOnClickListener { showBase64ImageDialog(ventilationAreaImage, "Ventilation Area") }
            valueSoundLevel.setOnClickListener { showBase64ImageDialog(soundLevelImage, "Sound Level") }
            valueSoundProofAC.setOnClickListener { showBase64ImageDialog(centerSoundProofImage, "Sound Proof & AC") }
            valueInfoBoard.setOnClickListener { showBase64ImageDialog(roomInfoBoardImage, "Room Info Board") }
            valueInternalSignage.setOnClickListener { showBase64ImageDialog(internalSignageImage, "Internal Signage") }
            valueCCTV.setOnClickListener { showBase64ImageDialog(audioCameraImage, "CCTV Cameras") }
            valueLANComputers.setOnClickListener { showBase64ImageDialog(lanEnabledImage, "LAN Enabled Computers") }
            valueInternet.setOnClickListener { showBase64ImageDialog(internetConnectionImage, "Internet Connection") }
            valueTypingTutor.setOnClickListener { showBase64ImageDialog(typingTuterCompImage, "Typing Tutor Computers") }
            valueTablets.setOnClickListener { showBase64ImageDialog(tabletImage, "Tablets") }
            valueStools.setOnClickListener { showBase64ImageDialog(candidateChairImage, "Candidate Chair") }
            valueTrainerChair.setOnClickListener { showBase64ImageDialog(trainerChairImage, "Trainer Chair") }
            valueTrainerTable.setOnClickListener { showBase64ImageDialog(trainerTableImage, "Trainer Table") }
            valueLights.setOnClickListener { showBase64ImageWithCountDialog(lightsImage, "Lights",  data?.lightAiCount.toString()) }
            valueFans.setOnClickListener { showBase64ImageWithCountDialog(fansRoomImage, "Fans",  data?.fanAiCount.toString()) }
            valuePowerBackup.setOnClickListener { showBase64ImageDialog(ecPowerBackupImage, "Power Backup") }
            valueITLabPhoto.setOnClickListener { showBase64ImageDialog(roomsPhotographsImage, "IT Lab Photograph") }
            valuedomainrelatedequipPhoto.setOnClickListener { showBase64ImageDialog(domainEquipmentImage, "Domain Equipment") }
            valueAirConditioning.setOnClickListener { showBase64ImageDialog(airConditionRoomImage, "Air Conditioning") }
        }

    }


    @SuppressLint("Recycle")
    private fun downloadAndOpenBase64Pdf(base64: String, fileName: String = "document.pdf${Random(2)}") {
        try {
            val cleanBase64 = base64
                .replace("data:application/pdf;base64,", "")
                .trim()

            val pdfBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            if (pdfBytes.isEmpty()) {
                showToast(getString(R.string.invalid_pdf_data))
                return
            }

            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) downloadsDir.mkdirs()

            val file = File(downloadsDir, fileName)
            FileOutputStream(file).use { it.write(pdfBytes) }

            val uri = Uri.fromFile(file)
            requireContext().sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            showToast(getString(R.string.pdf_downloaded_to_downloads, file.name))
            openBase64Pdf(cleanBase64)

        } catch (e: Exception) {
            e.printStackTrace()
            showErrorToast(getString(R.string.error, e.message))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun collectTCInfoResponse() {
        viewModel.trainingCentersInfo.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
                    onSuccess = { data ->
                        data?.forEach { x ->
                            binding.trainingCenterInfoLayout.apply {
                                tvSchemeName.text = x.schemeName
                                tvCenterName.text = x.centerName
                                tvProjectState.text = x.projectState
                                tvTypeOfArea.text = x.addressType
                                tvlatAndLang.text = "${x.latitude} , ${x.longitude}"
                                tvDistanceBus.text = x.distanceFromBusStand
                                tvDistanceAuto.text = x.distanceFromAutoStand
                                tvSanctionOrder.text = x.sanctionOrderNo
                                tvTypeOfTraining.text = x.tcType
                                tvNatureOfTraining.text = x.tcNature
                                tvSpecialArea.text = x.specialArea
                                tvTrainingCenterAddress.text = "${x.latitude},${x.tcAddress}"
                                tvTrainingCenterEmail.text = x.tcEmailID
                                tvMobileNumber.text = x.tcMobileNo
                                tvLandlineNumber.text = x.tcLandline
                                tvParliamentaryConstituency.text = x.parliamentaryConstituency
                                tvAssemblyConstituency.text = x.assemblyConstituency
                                tvCenterIncharge.text = x.centerIncharge
                                tvCenterInchargeMobile.text = x.inchargeMobileNo
                                tvCenterInchargeEmail.text = x.inchargeMailId
                            }
                        }
                    }
                )
            }
            result.onFailure {
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
    }

    private fun collectTCStaffResponse() {
        viewModel.getTcStaffDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
                    onSuccess = { data ->
                        dataStaffList = data?.toMutableList() ?: mutableListOf()
                    }
                )
            }
            result.onFailure {
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
    }

    private fun collectTCInfraResponse() {
        viewModel.getTrainerCenterInfra.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
                    onSuccess = { data ->
                        val tcInfoData = it.wrappedList
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val jsonResponse = gson.toJson(data)

                        Log.d("RFQTeamFrom", "NonAreaInformation Success Response:\n$jsonResponse")
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
                    }
                )
            }
            result.onFailure {
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
    }

    private fun collectTCAcademiaNonAcademia() {
        viewModel.getTcAcademicNonAcademicArea.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
                    onSuccess = { data ->
                        academiaList.clear()
                        data?.let { it1 -> academiaList.addAll(it1) }
                        binding.recyclerView.adapter?.notifyDataSetChanged() // Important!
                    }
                )
            }
            result.onFailure {
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
    }

    private fun collectTCToiletAndWash() {
        viewModel.getTcToiletWashBasin.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
                    onSuccess = { data ->
                        data?.forEach { x ->
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val jsonResponse = gson.toJson(x)
                            Log.d("collectTCToiletAndWash", "✅ Success Response:\n$jsonResponse")
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
                    }
                )
            }
            result.onFailure {
                showErrorToast("Failed: ${it.message}")
            }
        }
    }

    private fun collectTCDescOtherArea() {
        viewModel.getDescriptionOtherArea.observe(viewLifecycleOwner) { result ->
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonResponse = gson.toJson(result)
            Log.d("QTrainingFragment", "✅collectTCDescOtherArea Success Response:\n$jsonResponse")
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
                    onSuccess = { data ->
                        data?.forEach { x ->
                            binding.apply {
                                valueCorridorNo.text = x.corridorNo
                                valueLenghth.text = x.length
                                valueWidth.text = x.width
                                valueArea.text = x.areas
                                valueLights.text = x.numberOfLights
                                yesNoFans.text = x.numberOfFans
                                yesNoCirculationArea.text = x.circulationArea
                                yesNoOpenSpace.text = x.openSpace
                                yesNoParking.text = x.parkingSpace
                            }

                            fansImage = x.descProofImagePath.toString()
                            circulationAreaImage = x.circulationAreaImagePath.toString()
                            openSpaceImage = x.openSpaceImagePath.toString()
                            parkingSpaceImage = x.parkingSpaceImagePath.toString()
                        }
                    }
                )
            }
            result.onFailure {
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
    }

    private fun collectTCTeaching() {
        viewModel.getTeachingLearningMaterial.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
                    onSuccess = { data ->
                        data?.forEach { x ->
                            binding.apply {
                                yesNoTrade.text = x.tradeName
                                yesNoNatureofTraining.text = x.trainingNature
                                yesNoTradeAsPerProject.text = x.tradesAvailable
                                yesNoIsTrainingPlanAvail.text = x.trainingPlan
                                yesNoIsDomainCirAvail.text = x.domainCurriculum
                                yesNoIsActivityCumLess.text = x.availableACLP
                                yesNoIsWelcomeKitAvail.text = x.welcomeKit
                                yesNoNameOfCertifyingAg.text = x.certifingAgencyName
                                yesNoAssessmentMaterial.text = x.assessmentMaterial
                            }

                            welcomeKitImage = x.welcomeKitPdf
                        }
                    }
                )
            }
            result.onFailure {
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
    }

    private fun collectTCGeneral() {
        viewModel.getGeneralDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
                    onSuccess = { data ->
                        data?.forEach { x ->
                            signOfLeakageImage = x.signLeakageImage
                            protectionStairsBalImage = x.stairsProtectionImage

                            binding.yesNoSignOfLiakage.text = x.signLeakage
                            binding.yesNoProtectionOfStairs.text = x.stairsProtection
                            binding.yesNoconformanceDDUGKY.text = x.ddugkyConfrence
                            binding.yesNoCandidateComeSafely.text = x.centerSafty
                        }
                    }
                )
            }
            result.onFailure {
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
    }

    private fun collectTCElectrical() {
        viewModel.getElectricalWiringStandard.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
                    onSuccess = { data ->
                        data?.forEach { x ->
                            securingWiringImage = x.wireSecurityImage.toString()
                            switchBoardImage = x.switchBoardImage.toString()
                            binding.yesNoSecuringWire.text = x.wireSecurity
                            binding.yesNoSwitchBoard.text = x.switchBoard
                        }
                    }
                )
            }
            result.onFailure {
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
    }

    private fun collectTCSignage() {
        viewModel.getSignagesAndInfoBoard.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
                    onSuccess = { data ->
                        data?.forEach { x ->
                            tcNameBoardImage = x.tcNameImage.toString()
                            activitySummaryBoardImage = x.activityAchivementImage.toString()
                            studentEntitlementBoardImage = x.studentEntitlementImage.toString()
                            contactDetailImpoPeopleImage = x.contactDetailsImage.toString()
                            basicInfoBoardImage = x.basicInfoImage.toString()
                            codeOfConductImage = x.codeConductImage.toString()
                            studentAttendanceImage = x.studentsAttendanceImage.toString()

                            binding.signageLayout.apply {
                                yesNoCenterNameBoard.text = x.tcName
                                yesNoSummaryAcheivement.text = x.activityAchivement
                                yesNoStudentEntitlement.text = x.studentEntitlement
                                yesNoContactDetail.text = x.contactDetails
                                yesNoBasicInfoBoard.text = x.basicInfo
                                yesNoCodeOfConduct.text = x.codeConduct
                                yesNoAttendanceSummary.text = x.studentsAttendance
                            }
                        }
                    }
                )
            }
            result.onFailure {
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
    }

    private fun collectTCIpEnabele() {
        viewModel.getIpEnabledCamera.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
                    onSuccess = { data ->
                        data?.forEach { x ->
                            centralMonitorImage = x.centralMonitorImagePath.toString()
                            conformationOfCCTVImage = x.cctvConformanceImagePath.toString()
                            storageOfCCtvImage = x.cctvStorageImagePath.toString()
                            dvrImage = x.dvrStaticIpImagePath.toString()

                            binding.ipCameraLayout.apply {
                                yesNoCentralMonitor.text = x.centralMonitor
                                yesNoConformanceCCTV.text = x.cctvConformance
                                yesNoStorageCCTV.text = x.cctvStorage
                                yesNoDvrStaticIP.text = x.dvrStaticIp
                                yesNoIpEnabled.text = x.ipEnable
                                yesNoResolution.text = x.resolution
                                yesNoVideoStream.text = x.videoStream
                                yesNoRemoteAccessWeb.text = x.remoteAccessBrowser
                                yesNoRemoteAccessUsers.text = x.simultaneousAccess
                                yesNoSupportedProtocols.text = x.supportedProtocol
                                yesNoColorAudio.text = x.colorVideoAudit
                                yesNoStorageFacility.text = x.storageFacility
                            }
                        }
                    }
                )
            }
            result.onFailure {
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
    }

    private fun collectTCCommonEquipment() {
        viewModel.getCommonEquipment.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
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

                            binding.commonEquipmentLayout.apply {
                                yesNoElectricalPowerBackup.text = x.ecPowerBackup
                                yesNoBiometricDevices.text = x.biomatricDeviceInstallation
                                yesNoCCTVMonitor.text = x.cctvMoniotrInstall
                                yesNoStorageDocs.text = x.storageSecuring
                                yesNoPrinterScanner.text = x.printerScanner.toString()
                                yesNoDigitalCamera.text = x.digitalCamera.toString()
                                yesNoGrievanceRegister.text = x.grievanceRegister.toString()
                                yesNoMinEquipment.text = x.minimumEquipment.toString()
                                yesNoDirectionBoards.text = x.directionBoard.toString()
                            }


                        }
                    }
                )
            }
            result.onFailure {
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
    }

    private fun collectTCSupportInfra() {
        viewModel.getAvailabilitySupportInfra.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
                    onSuccess = { data ->
                        data?.forEach { x ->
                            safeDrinkingImage = x.drinkingWaterImage.toString()
                            fireFightingImage = x.fireFighterEquipImage.toString()
                            firstAidImage = x.firstAidKitImage.toString()

                            binding.availSupportInfraLayout.apply {
                                yesNoSafeDrinkingWater.text = x.drinkingWater
                                yesNoFireFighting.text = x.fireFighterEquip
                                yesNoFirstAidKit.text = x.firstAidKit
                            }
                        }
                    }
                )
            }
            result.onFailure {
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
    }

    private fun collectTCStandardForms() {
        viewModel.getAvailabilityStandardForms.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
                    onSuccess = { data ->
                        data?.forEach { x ->
                            with(binding.availOfStandardFormsLayout) {
                                yesNoPlanOfTraining.text= x.trainingPlan
                                yesNoLessonPlanner.text= x.aclp
                                yesNoOnJobTraining.text= x.batchJobTrainingPlan
                                yesNoDailyTablets.text= x.tabletsDistribution
                                yesNoStudentEntitlementBanner.text       = x.studentEntitlement
                                yesNoParentsConsentForm.text= x.parentsConsentForm
                                yesNoCandidateAttendanceRegister.text    = x.candidateAttendRegBio
                                yesNoTrainerAttendanceRegister.text      = x.trainersAttendRegBoi
                                yesNoItemsChecklist.text= x.candidateChecklistItem
                                yesNoEvaluationSummary.text= x.evaluationAssessmentSumm
                                yesNoTADARecord.text= x.tadaCalcRecord
                                yesNoTrainingCertificate.text= x.trainingCertificate
                                yesNoTrainingCompletionCertificateRecord.text = x.trainingCompCertDisbRecord
                                yesNoEquipmentTrainingCentre.text= x.equipmentList
                                yesNoEquipmentAccommodation.text= x.tafEquipment
                                yesNoTrainingCentreInspection.text       = x.tcInspection
                                yesNoAssessmentCertification.text        = x.candidateCertificateAsmt
                                yesNoLetterSRLMInfo.text= x.letterToMobilizationPlan
                                yesNoLetterFromSRLM.text= x.letterFromMobilizationPlan
                                yesNoOnFieldRegistration.text= x.candidateOnFieldReg
                                yesNoOverviewAptitudeTest.text           = x.aptitudeTest
                                yesNoCandidateApplicationForm.text       = x.candidateAppForm
                                yesNoTrainersProfile.text= x.trainerProfile
                                yesNoCandidatesEnrolled.text             = x.enrolledCandidateList
                                yesNoCandidateDossierIndex.text          = x.indexInvdcandidateDossier
                                yesNoPerformanceCan.text                 = x.prfEvelPlanCandidate
                                yesNoListOfCandidateAfterBatchFreezing.text = x.candidateAfterBatchFreeze
                                yesNoDailyFailureReport.text             = x.dailyFailureItemReport
                                yesNo15DaysSummary.text= x.days15Summery
                                yesNoContentCounselling.text             = x.tradeCounselling
                                yesNoCandidateIDTemplate.text            = x.candidateIdTemp
                                yesNoStaffSummary.text                   = x.deployedStaffSumm
                                yesNoDullyIfApplicable.text              = x.dulySignedformProofApplicable
                                yesNoPerformanceTrainer.text             = x.prfEvelPlanTrainers
                                yesNoDully.text                          = x.dulySignedformProof
                                yesNoIpEnabled.text                     = x.ipEnabledCamera
                            }
                        }
                    }
                )
            }
            result.onFailure {
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
    }

    private fun collectAllRoomDetails() {
        viewModel.getAcademicRoomDetails.observe(viewLifecycleOwner) { result ->
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonResponse = gson.toJson(result)
            Log.d("QTrainingFragment", "✅collectAllRoomDetails Success Response:\n$jsonResponse")
            result.onSuccess {
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it.wrappedList,
                    onSuccess = { data ->
                        data?.forEach { x ->
                            fansRoomImage = x.fansImage ?: ""
                            writingBoard = x.writingBoard ?: ""
                            internetConnectionImage = x.internetConnectionImage ?: ""
                            roomInfoBoardImage = x.roomInfoBoardImage ?: ""
                            digitalProjectorImage = x.digitalProjectorImage ?: ""
                            officeComputer = x.officeComputer ?: ""
                            printerScannerImage = x.printerScannerImage ?: ""
                            digitalCameraImage = x.digitalCameraImage?:""
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
                    }
                )
            }
            result.onFailure {
                showErrorToast(getString(R.string.failed, it.message))
            }
        }
    }

    private fun collectQTeamInsertRes() {
        viewModel.insertQTeamVerification.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                dismissProgressDialog()
                handleApiResponse(
                    responseCode = it.responseCode,
                    data = it,
                    onSuccess = {
                        showSuccessToast(it!!.responseDesc)
                        findNavController().navigateUp()
                    }
                )
            }
            result.onFailure {
                dismissProgressDialog()
                showErrorToast(getString(R.string.failed, it.message))
            }
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
