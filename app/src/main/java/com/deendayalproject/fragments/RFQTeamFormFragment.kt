package com.deendayalproject.fragments

import SharedViewModel
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
//import com.bumptech.glide.Glide
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.databinding.RfQteamFormFagmentBinding
import com.deendayalproject.model.request.CompliancesRFQTReq
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.response.RoomDetail
import com.deendayalproject.model.response.RoomItem
import com.deendayalproject.model.response.Trainer
import com.deendayalproject.util.AppUtil
import java.io.File

class RFQTeamFormFragment : Fragment() {

    private var _binding: RfQteamFormFagmentBinding? = null
    private val binding get() = _binding!!


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
    private var selectedRFQTApproval = ""
    private var selectedIDCApproval = ""
    private var RFQTresFacilityId = ""
    private var selectedRFQTRemarks = ""
    private var selectedIDCRemarks = ""


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

    //end all room var


    private var centerId = ""
    private var sanctionOrder = ""
    private var centerName = ""

    //    private var selfDeclarationPdf = ""
    private var RFQTBasicInfoPdf = ""
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RfQteamFormFagmentBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//       GetRfBasicInformation AjitRanjan 17/10/2025
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        centerId = arguments?.getString("centerId").toString()
        centerName = arguments?.getString("centerName").toString()
        sanctionOrder = arguments?.getString("sanctionOrder").toString()

        // TrainingCenterInfo API
        val requestTcInfo = TrainingCenterInfo(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.getRfBasicInformationrInfo(requestTcInfo)
        collectTCInfoResponse()
        init()
        binding.backButton.setOnClickListener {

            findNavController().navigateUp()
        }

    }

    private fun init() {

        listener()
    }

    private fun listener() {


    }


    @SuppressLint("SetTextI18n")
    private fun collectTCInfoResponse() {
        viewModel.ResidentialFacilityQTeam.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {

                            binding.residentialfacilityqteamInfoLayout.tvSchemeName.text =
                                x.schemeName
                            binding.residentialfacilityqteamInfoLayout.tvtraningCentreName.text =
                                x.trainingCenterName
//                            binding.residentialfacilityqteamInfoLayout.tvProjectState.text = x.stateName
//                            binding.residentialfacilityqteamInfoLayout.tvblock.text = x.blockName
                            binding.residentialfacilityqteamInfoLayout.tvssanctionNumbere.text =
                                x.senctionOrder
                            binding.residentialfacilityqteamInfoLayout.tvresidentialFacilitytype.text =
                                x.residentialType
                            binding.residentialfacilityqteamInfoLayout.tvllocationOfRc.text =
                                x.residentialCenterLocation
                            binding.residentialfacilityqteamInfoLayout.tvhouseNo.text = x.houseNo
                            binding.residentialfacilityqteamInfoLayout.tvStreet.text = x.streetNo1
                            binding.residentialfacilityqteamInfoLayout.tvstateUi.text = x.stateName
                            binding.residentialfacilityqteamInfoLayout.tvblock.text = x.blockName
                            binding.residentialfacilityqteamInfoLayout.tvVillageWardNo.text =
                                x.villageName
                            binding.residentialfacilityqteamInfoLayout.tvpincode.text = x.pincode
//                            binding.residentialfacilityqteamInfoLayout.tvpincode.text = x.pincode
                            binding.residentialfacilityqteamInfoLayout.tvresidentialFacilityPhone.text =
                                x.residentialFacilitiesPhNo
                            binding.residentialfacilityqteamInfoLayout.tvEmail.text = x.email
                            binding.residentialfacilityqteamInfoLayout.tvTrainingCenterAddress.text =
                                x.geoAddress
                            binding.residentialfacilityqteamInfoLayout.tvLongitude.text =
                                x.longitude
                            binding.residentialfacilityqteamInfoLayout.tvGeoAddress.text =
                                x.geoAddress
                            binding.residentialfacilityqteamInfoLayout.tvCategoryofTClocation.text =
                                x.categoryOfTc
                            binding.residentialfacilityqteamInfoLayout.tvApproximateDistancefroma.text =
                                x.longitude
                            binding.residentialfacilityqteamInfoLayout.tvParliamentaryConstituency.text =
                                x.residentialFacilitiesPhNo
                            binding.residentialfacilityqteamInfoLayout.CentretoResidential.text =
                                x.residentialCenterLocation
                            binding.residentialfacilityqteamInfoLayout.tvEmployeeID.text = x.wardEmpId
                            binding.residentialfacilityqteamInfoLayout.tvresidentialFacilitytype.text =
                                x.resFacilityId

                            binding.residentialfacilityqteamInfoLayout.tvMobileNo.text = x.mobile

                            RFQTresFacilityId=x.resFacilityId.toString()




//                            binding.residentialfacilityqteamInfoLayout.tvPoliceVerificationStatus.text = x.policeVerfictnImage
//                            binding.residentialfacilityqteamInfoLayout.tvAppointmentLetter.text = x.empLetterImage
                            // ✅ Load image using Glide and ViewBinding
                            // Load image

                            RFQTBasicInfoPdf= x.policeVerfictnImage.toString()


                            binding.residentialfacilityqteamInfoLayout.valueRFQTInfoPhoto.setOnClickListener {


//                                showBase64ImageDialog(requireContext(), x.policeVerfictnImage, "RFQTeam Basic Info Appointment Letter Photo")
                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }
                            binding.residentialfacilityqteamInfoLayout.tvStreet2.text =
                                x.streetNo2

                        }
                    }

                    202 -> Toast.makeText(
                        requireContext(),
                        "No data available.",
                        Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(),
                        "Please upgrade your app.",
                        Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }


        //Adapter Electrical
        tcElectricalAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.residentialfacilityqteamInfoLayout.SpinnerTcInfo.setAdapter(tcElectricalAdapter)

        binding.residentialfacilityqteamInfoLayout.SpinnerTcInfo.setOnItemClickListener { parent, view, position, id ->
            selectedRFQTApproval = parent.getItemAtPosition(position).toString()
            if (selectedRFQTApproval == "Send for modification") {
                binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks.visibility =
                    View.VISIBLE
                binding.residentialfacilityqteamInfoLayout.textViewRFQTInfoRemarks.visibility =
                    View.VISIBLE


            } else {

                binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks.visibility = View.GONE
                binding.residentialfacilityqteamInfoLayout.textViewRFQTInfoRemarks.visibility =
                    View.GONE

            }

        }

        binding.residentialfacilityqteamInfoLayout.btnRFQTInfoNext.setOnClickListener {


            if (selectedRFQTApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }
//            return@setOnClickListener
            binding.residentialfacilityqteamInfoLayout.viewRFQTInfo.visibility = View.GONE
            binding.residentialfacilityqteamInfoLayout.RFQTInfoExpand.visibility = View.GONE
            binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand.visibility = View.VISIBLE
            binding.tvinfrastructureDetailsAndCompliances.visibility = View.VISIBLE
            binding.infrastructureDetailsAndCompliancesLayout.viewIDC.visibility = View.VISIBLE
//            binding.mainDescAcademia.visibility = View.GONE
//            binding.viewDescAcademia.visibility = View.GONE
//            viewIDC
//    IDetailsComplainExpand
//    llTopIDC
//    tvIDC


            binding.residentialfacilityqteamInfoLayout.tvTrainInfo.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }
            if (selectedRFQTApproval == "Send for modification") {
                selectedRFQTRemarks = binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks.text.toString()
                if (selectedRFQTRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
//                return@setOnClickListener
            } else selectedRFQTRemarks = ""

//            Ajit Ranjan create 21/October/2026  CompliancesRFQTReqRFQT
            val requestCompliancesRFQT = CompliancesRFQTReq(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                facilityId = "1",
                imeiNo = AppUtil.getAndroidId(requireContext())
            )

            viewModel.getCompliancesRFQTReqRFQT(requestCompliancesRFQT)
            collectInsfrastructureDetailsAndComplains()
        }


    }

    @SuppressLint("SetTextI18n")
    private fun collectInsfrastructureDetailsAndComplains() {
        viewModel.CompliancesRFQTReqRFQT.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {
                            binding.infrastructureDetailsAndCompliancesLayout.onwershipOfBulding.text=x.ownership
                            binding.infrastructureDetailsAndCompliancesLayout.areaOfTheBuilding.text = x.buildingArea
                            binding.infrastructureDetailsAndCompliancesLayout.roofBuildingLabel.text = x.roof
//                            binding.infrastructureDetailsAndCompliancesLayout.WhetherItIsStructurally.text = x.roof
                            binding.infrastructureDetailsAndCompliancesLayout.visibleSignsOfLeakages.text = x.leakage
                            binding.infrastructureDetailsAndCompliancesLayout.ConformanceToDduGky.text = x.conformanceDdu
                            binding.infrastructureDetailsAndCompliancesLayout.ProtectionOfStairs.text = x.protectionStairs
                            binding.infrastructureDetailsAndCompliancesLayout.CirculatingArea.text = x.circulatingArea
                            binding.infrastructureDetailsAndCompliancesLayout.Corridor.text = x.corridor
//                            binding.infrastructureDetailsAndCompliancesLayout.ElectricalWiringAndStandards.text = x.corridor
                            binding.infrastructureDetailsAndCompliancesLayout.SwitchBoardsAndPanelBoards.text = x.switchBoardsPanelBoards
                            binding.infrastructureDetailsAndCompliancesLayout.StudentEntitlementBoard.text = x.studentEntitlementBoard
                            binding.infrastructureDetailsAndCompliancesLayout.FoodSpecificationBoard.text = x.foodSpecificationBoard
                            binding.infrastructureDetailsAndCompliancesLayout.Area.text = x.openSpaceArea
                            RFQTresFacilityId=x.resFacilityId.toString()



//                            binding.residentialfacilityqteamInfoLayout.valueRFQTInfoPhoto.setOnClickListener {
//
//
////                                showBase64ImageDialog(requireContext(), x.policeVerfictnImage, "RFQTeam Basic Info Appointment Letter Photo")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
//                            }


                        }
                    }

                    202 -> Toast.makeText(
                        requireContext(),
                        "No data available.",
                        Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(),
                        "Please upgrade your app.",
                        Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }


//        tvSelectApprovalIDC
//        llTcIDC
//        SpinnerIDC
//        IDCRemarks
//        etIDCRemarks
//        btnIDCPrevious
//        btnIDCNext

        //Adapter Electrical
        tcElectricalAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.infrastructureDetailsAndCompliancesLayout.SpinnerIDC.setAdapter(tcElectricalAdapter)

        binding.infrastructureDetailsAndCompliancesLayout.SpinnerIDC.setOnItemClickListener { parent, view, position, id ->
            selectedIDCApproval = parent.getItemAtPosition(position).toString()
            if (selectedIDCApproval == "Send for modification") {
                binding.infrastructureDetailsAndCompliancesLayout.tvSelectApprovalIDC.visibility =
                    View.VISIBLE
                binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks.visibility =
                    View.VISIBLE


            } else {

                binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks.visibility = View.GONE
                binding.infrastructureDetailsAndCompliancesLayout.tvSelectApprovalIDC.visibility =
                    View.GONE

            }
//
        }
//        binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand.visibility = View.GONE


        binding.infrastructureDetailsAndCompliancesLayout.btnIDCPrevious.setOnClickListener {
            binding.tvinfrastructureDetailsAndCompliances.visibility= View.GONE
            binding.residentialfacilityqteamInfoLayout.RFQTInfoExpand.visibility= View.VISIBLE
        }
        binding.infrastructureDetailsAndCompliancesLayout.btnIDCNext.setOnClickListener {


            if (selectedIDCApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }
//            return@setOnClickListener
            binding.infrastructureDetailsAndCompliancesLayout.viewIDC.visibility = View.GONE
            binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand.visibility = View.GONE
//            binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand.visibility = View.VISIBLE
//            binding.tvinfrastructureDetailsAndCompliances.visibility = View.VISIBLE
//            binding.infrastructureDetailsAndCompliancesLayout.viewIDC.visibility = View.VISIBLE
            binding.infrastructureDetailsAndCompliancesLayout.tvIDC.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }
            if (selectedIDCApproval == "Send for modification") {
                selectedIDCRemarks = binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks.text.toString()
                if (selectedIDCRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
//                return@setOnClickListener
            } else selectedIDCRemarks = ""

//            Ajit Ranjan create 21/October/2026  CompliancesRFQTReqRFQT
//            val requestCompliancesRFQT = CompliancesRFQTReq(
//                appVersion = BuildConfig.VERSION_NAME,
//                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
//                facilityId = RFQTresFacilityId,
//                imeiNo = AppUtil.getAndroidId(requireContext())
//            )
//
//            viewModel.getCompliancesRFQTReqRFQT(requestCompliancesRFQT)
        }


    }


    private fun openBase64Pdf(context: Context, base64: String) {
        try {
            // 1. Clean Base64 (remove header if present)
            val cleanBase64 = base64
                .replace("data:application/pdf;base64,", "")
                .trim()

            // 2. Decode Base64
            val pdfBytes = Base64.decode(cleanBase64, Base64.DEFAULT)

            // 3. Verify PDF header
            if (pdfBytes.isEmpty() || !String(pdfBytes.copyOfRange(0, 4)).startsWith("%PDF")) {
                Toast.makeText(context, "Invalid PDF data", Toast.LENGTH_SHORT).show()
                return
            }

            // 4. Save temporarily in cache
            val pdfFile = File.createTempFile("temp_", ".pdf", context.cacheDir)
            pdfFile.outputStream().use { it.write(pdfBytes) }

            // 5. Get URI via FileProvider
            val uri: Uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",  // must match manifest authority
                pdfFile
            )

            // 6. Create intent
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            // 7. Check if any app can handle PDFs
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(Intent.createChooser(intent, "Open PDF with"))
            } else {
                Toast.makeText(context, "No PDF viewer installed", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to open PDF", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showBase64ImageDialog(context: Context, base64ImageString: String?, title: String = "Image") {
        val imageView = ImageView(context)

        // Decode Base64 → Bitmap
        val bitmap: Bitmap? = if (!base64ImageString.isNullOrBlank()) {
            try {
                val cleanBase64 = base64ImageString
                    .replace("data:image/png;base64,", "")
                    .replace("data:image/jpg;base64,", "")
                    .replace("data:image/jpeg;base64,", "")
                    .replace("\\s".toRegex(), "")

                val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }

        // If bitmap is null → show default image
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
        } else {
            imageView.setImageResource(R.drawable.no_image) // your fallback drawable
        }

        imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.setPadding(20, 20, 20, 20)

        // Show in dialog
        AlertDialog.Builder(context)
            .setTitle(title)
            .setView(imageView)
            .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}