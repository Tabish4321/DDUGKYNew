package com.deendayalproject.fragments

import SharedViewModel
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.adapter.IndoorGameRFAdapter
import com.deendayalproject.adapter.LivingAreaInformationAdapter
import com.deendayalproject.adapter.RFToiletAdapter
import com.deendayalproject.databinding.RfSrlmFormFragmentBinding
import com.deendayalproject.databinding.RoominformationPopdialogBinding
import com.deendayalproject.databinding.TriPopdialogBinding
import com.deendayalproject.model.request.CompliancesRFQTReq
import com.deendayalproject.model.request.GetUrinalWashReq
import com.deendayalproject.model.request.LivingRoomListViewRQ
import com.deendayalproject.model.request.RFGameRequest
import com.deendayalproject.model.request.RFQteamVerificationRequest
import com.deendayalproject.model.request.RfCommonReq
import com.deendayalproject.model.request.RfLivingAreaInformationRQ
import com.deendayalproject.model.request.ToiletCountListReq
import com.deendayalproject.model.request.ToiletRoomInformationReq
import com.deendayalproject.model.request.ToiletRoomReq
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.response.IndoorRFGameResponseDetails
import com.deendayalproject.util.AppUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class RFSRLMFormFragment : Fragment() {

    // new changes


    private var  RFsafeDrinkingeFile =  ""
    private var  RFfirstAidKitFile =  ""
    private var RFfireFightingFile =  ""
    private var RFbiometricDeviceFile =  ""
    private var RFpowerBackupFile =  ""
    private var RFgrievanceRegisterFile =  ""
    private var _binding: RfSrlmFormFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel

    private val progress: androidx.appcompat.app.AlertDialog? by lazy {
        AppUtil.getProgressDialog(context)
    }
    private var RFQInfraContactDetailOfImportantPeopleFile = ""
    private lateinit var adapter: LivingAreaInformationAdapter
    private lateinit var adapterToilet: RFToiletAdapter
    private var rfToiletId = ""
    private lateinit var adapterIndoorGame: IndoorGameRFAdapter
    private val RFindoorGamesList = mutableListOf<IndoorRFGameResponseDetails>()
    private val approvalList = listOf("Approved", "Send for modification")
    private var selectedRFBasicInformationApproval = ""
    private var selectedRFLevingAreaInformationApproval = ""
    private var selectedRFToiletApproval = ""
    private var selectedInfrastctureDetailsComplainsApproval = ""
    private var selectedIndoorGameApproval = ""
    private var selectedResidintislFacilityApproval = ""
    private var selectedResidintislSupportFacilityApproval = ""
    private var selectedNonAreaInfoApproval = ""
    private var RFQTresFacilityId = ""


    //       Ajit Ranjan 03/11/2025 Non Room Information
    private var PreparedFoodFile = ""
    private var ReceptionAreaPdf = ""


    //       Ajit Ranjan 06/11/2025 Residential Facilities
    private var RFWardenCareFile = ""
    private var RFMaleDoctorFile = ""
    private var RFFemaleDoctorFile = ""
    private var RFWardenCaretakerMaleFile = ""
    private var RFHostelsSeparatedFile = ""
    private var RFSecurityGuardsFile = ""


    private var selectedRFBasicInformationRemarks = ""
    private var selectedRFLevingAreaInformationRemarks = ""
    private var selectedRFToiletRemarks = ""
    private var selectedRFNonLivingAreaRemarks = ""
    private var selectedInfrastctureDetailsComplainsRemarks = ""
    private var selectedResidintislFacilityApprovalRemark = ""
    private var selectedResidintislSupportFacilityApprovalRemark = ""
    private var selectedRFToiletAdditionalSanctionApproval = ""
    private var selectedRFToiletAdditionalSanctionRemarks = ""
    private var selectedIndoorGameApprovalRemark = ""
    private lateinit var nfrastructureDetailsAndCompliancesAdapter: ArrayAdapter<String>
    private lateinit var BasicInformationAdapter: ArrayAdapter<String>
    private lateinit var RFIndoorGameAdapter: ArrayAdapter<String>
    private lateinit var RFResidentialFacilitiesAvailableAdapter: ArrayAdapter<String>
    private lateinit var RFResidentialSupportFacilitiesAvailableAdapter: ArrayAdapter<String>
    private lateinit var tvLivingAreaInformationAdapter: ArrayAdapter<String>
    private lateinit var tvToiletAdapter: ArrayAdapter<String>
    private lateinit var tvToiletAdditionalSectionAdapter: ArrayAdapter<String>
    private lateinit var tvNonLivingAreaAdapter: ArrayAdapter<String>

    private var centerId = ""
    private var sanctionOrder = ""
    private var facilityId = 0
    private var centerName = ""
    private var RFQTBasicInfoPdf = ""
    private var RFQTBasicInfoAppointMent = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//       GetRfBasicInformation AjitRanjan 17/10/2025
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        centerId = arguments?.getString("centerId").toString()
        centerName = arguments?.getString("centerName").toString()
        sanctionOrder = arguments?.getString("sanctionOrder").toString()
        facilityId = arguments?.getInt("facilityId",0)!!
        val token = AppUtil.getSavedTokenPreference(requireContext())
//
        val TokeValue=token


        binding.residentialfacilityqteamInfoLayout.PoliceVerificationStatus.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQTBasicInfoPdf, "police verification ")

        }

        binding.residentialfacilityqteamInfoLayout.AppointmentLetter.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQTBasicInfoAppointMent, "police verification ")

        }





        binding.RFNonLivingAreaLayout.ReceptionAreaFile.setOnClickListener {


            showBase64ImageDialog(requireContext(), ReceptionAreaPdf, "Preview")



        }

        binding.RFNonLivingAreaLayout.WhetherFoodForFile.setOnClickListener {

            showBase64ImageDialog(requireContext(), PreparedFoodFile, "Preview")


        }




//       Ajit Ranjan 06/11/2025 Residential Facilities
        binding.RFResidentialFacilitiesAvailable.WardenCareFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFWardenCareFile,"Warden Care")

        }
        binding.RFResidentialFacilitiesAvailable.MaleDoctorFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFMaleDoctorFile,"Male Doctor File")

        }
        binding.RFResidentialFacilitiesAvailable.FemaleDoctorFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFFemaleDoctorFile,"Female Doctor")

        }
        binding.RFResidentialFacilitiesAvailable.WardenCaretakerMaleFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFWardenCaretakerMaleFile,"Warden Caretaker Male")

        }
        binding.RFResidentialFacilitiesAvailable.HostelsSeparatedFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFHostelsSeparatedFile,"Hostels Separated")

        }
        binding.RFResidentialFacilitiesAvailable.SecurityGuardsFile.setOnClickListener {
//            openBase64Pdf(requireContext(), RFSecurityGuardsFile)
            showBase64ImageDialog(requireContext(), RFSecurityGuardsFile,"Security Guards")

        }



        binding.rfSupportFacilitiesAvailableLayout.SafeDrinikingAavailableFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFsafeDrinkingeFile,"Preview")

        }
        binding.rfSupportFacilitiesAvailableLayout.FirstAidKitFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFfirstAidKitFile,"Preview")

        }
        binding.rfSupportFacilitiesAvailableLayout.FireFightingEquipmentrFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFfireFightingFile,"Preview")

        }
        binding.rfSupportFacilitiesAvailableLayout.BiometricDeviceFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFbiometricDeviceFile,"Preview")

        }
        binding.rfSupportFacilitiesAvailableLayout.ElectricalPowerBackupFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFpowerBackupFile,"Preview")

        }
        binding.rfSupportFacilitiesAvailableLayout.GrievanceRegisterFile.setOnClickListener {
//            openBase64Pdf(requireContext(), RFSecurityGuardsFile)
            showBase64ImageDialog(requireContext(), RFgrievanceRegisterFile,"Preview")

        }







//             Ajit Ranjan create 07/Novmber/2025     insertRFQteamVerificationRequest

        // Final Submit

        binding.btnSubmitFinal.setOnClickListener {
            val requestTcInfraReq = RFQteamVerificationRequest(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                trainingCentre = centerId.toInt(),
                sanctionOrder = sanctionOrder,
                imeiNo = AppUtil.getAndroidId(requireContext()),
                basicInfoStatus=selectedRFBasicInformationApproval,
                basicInfoRemark=selectedRFBasicInformationRemarks.toString(),
                infraComplianceStatus=selectedInfrastctureDetailsComplainsApproval,
                infraComplianceRemark=selectedInfrastctureDetailsComplainsRemarks.toString(),
                livingAreaInfoStatus=selectedRFLevingAreaInformationApproval,
                livingAreaInfoRemark=selectedRFLevingAreaInformationRemarks.toString(),
                toiletStatus=selectedRFToiletApproval,
                toiletRemark=selectedRFToiletRemarks.toString(),
                nonLivingAreaStatus=selectedNonAreaInfoApproval,
                nonLivingAreaRemark=selectedRFNonLivingAreaRemarks.toString(),
                indoorGameStatus=selectedIndoorGameApproval,
                indoorGameRemark=selectedIndoorGameApprovalRemark.toString(),
                rfAvailableStatus=selectedResidintislFacilityApproval,
                rfAvailableRemark=selectedResidintislFacilityApprovalRemark.toString(),
                supportFacilityAvailableStatus=selectedResidintislSupportFacilityApproval,
                supportFacilityAvailableRemark=selectedResidintislSupportFacilityApprovalRemark.toString(),
                addToiletStatus=selectedRFToiletAdditionalSanctionApproval.toString(),
                addToiletRemark=selectedRFToiletAdditionalSanctionRemarks.toString(),
                facilityId = facilityId
            )
//            selectedRFNonLivingAreaRemarks

            viewModel.getFinalSubmitinsertRFinsertRFSrlmVerificationRequestData(requestTcInfraReq)
            collectFinalSubmitData()
            showProgressBar()

        }





        // TrainingCenterInfo API
        val requestTcInfo = RfCommonReq(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            facilityId = facilityId
        )
        viewModel.getRfBasicInformationrInfo(requestTcInfo)
        collectTCInfoResponse()
        showProgressBar()


        binding.backButton.setOnClickListener {

            findNavController().navigateUp()
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RfSrlmFormFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }



    @SuppressLint("SetTextI18n")
    private fun collectTCInfoResponse() {
        viewModel.ResidentialFacilityQTeam.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        hideProgressBar()
                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {

                            binding.residentialfacilityqteamInfoLayout.ResidentialFacilityName.text =
                                safeText(x.residentialFacilityName)
                            binding.residentialfacilityqteamInfoLayout.ResidentialFacilityType.text =
                                safeText(x.residentialType)
                            binding.residentialfacilityqteamInfoLayout.HouseNo.text =
                                safeText(x.houseNo)
                            binding.residentialfacilityqteamInfoLayout.Street.text =
                                safeText(x.streetNo1)
                            binding.residentialfacilityqteamInfoLayout.Landmark.text =
                                safeText(x.landmark)
                            binding.residentialfacilityqteamInfoLayout.StateUtTc.text =   safeText(x.stateName)
                            binding.residentialfacilityqteamInfoLayout.DistrictTc.text =   safeText(x.districtName)
                            binding.residentialfacilityqteamInfoLayout.Block.text =   safeText(x.blockName)
                            binding.residentialfacilityqteamInfoLayout.GramPanchayat.text =   safeText(x.gpName)
                            binding.residentialfacilityqteamInfoLayout.VillageWardNo.text =
                                safeText(x.villageName)
                            binding.residentialfacilityqteamInfoLayout.PoliceStation.text = x.policeStation
//                            binding.residentialfacilityqteamInfoLayout.tvpincode.text = x.pincode
                            binding.residentialfacilityqteamInfoLayout.LatitudeLongitude.text =
                                safeText(x.latitude)
                            binding.residentialfacilityqteamInfoLayout.Pincode.text = x.pincode
                            binding.residentialfacilityqteamInfoLayout.WardenMobileNo.text =
                                safeText(x.geoAddress)
                            binding.residentialfacilityqteamInfoLayout.Mobile.text =
                                safeText(x.mobile)
                            binding.residentialfacilityqteamInfoLayout.RFPNoWSC.text =
                                safeText(x.residentialFacilitiesPhNo)
                            binding.residentialfacilityqteamInfoLayout.Email.text =
                                safeText(x.email)
                            binding.residentialfacilityqteamInfoLayout.TypeofArea.text =
                                safeText(x.typeOfArea)
                            binding.residentialfacilityqteamInfoLayout.categoryOfTCLocaXYZanyOtherArea.text =
                                safeText(x.categoryOfTc)
                            binding.residentialfacilityqteamInfoLayout.ApproximateDistanceFrom.text =
                                safeText(x.distBusStand)
                            binding.residentialfacilityqteamInfoLayout.DistanceFromTheTrainingCenter.text = safeText(x.distFromTc)
                            binding.residentialfacilityqteamInfoLayout.AvailabilityOfPick.text = safeText(x.distRailStand)
                            binding.residentialfacilityqteamInfoLayout.DistanceFromRailwayStand.text = safeText(x.distRailStand)

                            binding.residentialfacilityqteamInfoLayout.DistanceFromAutoTraining.text = safeText(x.distAutoStand)
                            binding.residentialfacilityqteamInfoLayout.WadrenName.text = safeText(x.wardName)
                            binding.residentialfacilityqteamInfoLayout.WardenGender.text = safeText(x.wardgender)
                            binding.residentialfacilityqteamInfoLayout.WardenAddress.text = safeText(x.wardAddress)
                            binding.residentialfacilityqteamInfoLayout.WardenEmployeeId.text = safeText(x.wardEmpId)
                            binding.residentialfacilityqteamInfoLayout.WardenEmailId.text = safeText(x.wardEmail)
                            binding.residentialfacilityqteamInfoLayout.WardenMobileNo.text = safeText(x.wardMobile)
//                            binding.residentialfacilityqteamInfoLayout.PoliceStation.text = safeText(x.policeStation)
//                            binding.residentialfacilityqteamInfoLayout.AppointmentLetter.text = safeText(x.policeStation)

                            RFQTresFacilityId=x.resFacilityId.toString()

//distFromTc


//                            binding.residentialfacilityqteamInfoLayout.tvPoliceVerificationStatus.text = x.policeVerfictnImage
//                            binding.residentialfacilityqteamInfoLayout.tvAppointmentLetter.text = x.empLetterImage
                            // ✅ Load image using Glide and ViewBinding
                            // Load image

                            RFQTBasicInfoPdf= x.policeVerfictnImage.toString()
                            RFQTBasicInfoAppointMent= x.empLetterImage.toString()


//                            binding.residentialfacilityqteamInfoLayout.valueRFQTInfoPhoto.setOnClickListener {
//
//
////                                showBase64ImageDialog(requireContext(), x.policeVerfictnImage, "RFQTeam Basic Info Appointment Letter Photo")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
//                            }
//                            binding.residentialfacilityqteamInfoLayout.tvStreet2.text =
//                                x.streetNo2

                        }
                    }

                    202 ->
                    {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "No data available.",
                            Toast.LENGTH_SHORT
                        ).show()}


                    301 ->

                    {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()}

                    401 ->  {
                        hideProgressBar()

                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }





        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }


        //Adapter Electrical
        BasicInformationAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.residentialfacilityqteamInfoLayout.SpinnerTcInfo.setAdapter(BasicInformationAdapter)


        binding.residentialfacilityqteamInfoLayout.SpinnerTcInfo.setOnItemClickListener { parent, view, position, id ->
            selectedRFBasicInformationApproval = parent.getItemAtPosition(position).toString()

            if (selectedRFBasicInformationApproval == "Send for modification") {
                binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks.visibility = View.VISIBLE
                binding.residentialfacilityqteamInfoLayout.textViewRFQTInfoRemarks.visibility = View.VISIBLE
                selectedRFBasicInformationApproval = "M"
            } else {
                selectedRFBasicInformationApproval = "A"
                binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks.visibility = View.GONE
                binding.residentialfacilityqteamInfoLayout.textViewRFQTInfoRemarks.visibility = View.GONE
            }
        }

        binding.residentialfacilityqteamInfoLayout.btnRFQTInfoNext.setOnClickListener {

            // ✅ Step 1: Check if approval selected
            if (selectedRFBasicInformationApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Step 2: If selected “M” (Send for modification), remarks are mandatory
            if (selectedRFBasicInformationApproval == "M") {
                selectedRFBasicInformationRemarks = binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks.text.toString().trim()

                if (selectedRFBasicInformationRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                selectedRFBasicInformationRemarks = ""
            }

            // ✅ Step 3: Proceed only after validation success
            binding.residentialfacilityqteamInfoLayout.viewRFQTInfo.visibility = View.GONE
            binding.residentialfacilityqteamInfoLayout.RFQTInfoExpand.visibility = View.GONE
            binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand.visibility = View.VISIBLE
            binding.tvinfrastructureDetailsAndCompliances.visibility = View.VISIBLE
            binding.infrastructureDetailsAndCompliancesLayout.viewIDC.visibility = View.VISIBLE

            binding.residentialfacilityqteamInfoLayout.tvTrainInfo.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.baseline_info_24,
                0,
                R.drawable.ic_verified,
                0
            )

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

            // ✅ Step 4: Create request and call API
            val requestCompliancesRFQT = CompliancesRFQTReq(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                facilityId = RFQTresFacilityId,
                imeiNo = AppUtil.getAndroidId(requireContext()),
                tcId = centerId,
                sanctionOrder = sanctionOrder
            )

            viewModel.getCompliancesRFQTReqRFQT(requestCompliancesRFQT)
            collectInsfrastructureDetailsAndComplains()
            showProgressBar()
        }





        //Adapter Living Area Information 27/10/2025
        tvLivingAreaInformationAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )

        binding.livingareainformationLayout.SpinnerLivingAreaInformation.setAdapter(tvLivingAreaInformationAdapter)

        binding.livingareainformationLayout.SpinnerLivingAreaInformation.setOnItemClickListener { parent, view, position, id ->
            selectedRFLevingAreaInformationApproval = parent.getItemAtPosition(position).toString()

            if (selectedRFLevingAreaInformationApproval == "Send for modification") {
                binding.livingareainformationLayout.LivingAreaInformationRemarks.visibility = View.VISIBLE
                binding.livingareainformationLayout.etLivingAreaInformationRemarks.visibility = View.VISIBLE
                selectedRFLevingAreaInformationApproval = "M"
            } else {
                selectedRFLevingAreaInformationApproval = "A"
                binding.livingareainformationLayout.etLivingAreaInformationRemarks.visibility = View.GONE
                binding.livingareainformationLayout.LivingAreaInformationRemarks.visibility = View.GONE
            }
        }

        binding.livingareainformationLayout.btnLivingAreaInformationNext.setOnClickListener {

            // ✅ Step 1: Validate approval selection
            if (selectedRFLevingAreaInformationApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Step 2: If “Send for modification”, remarks are mandatory
            if (selectedRFLevingAreaInformationApproval == "M") {
                selectedRFLevingAreaInformationRemarks =
                    binding.livingareainformationLayout.etLivingAreaInformationRemarks.text.toString().trim()

                if (selectedRFLevingAreaInformationRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                selectedRFLevingAreaInformationRemarks = ""
            }

            // ✅ Step 3: Proceed only after validation success
            binding.livingareainformationLayout.viewLAI.visibility = View.GONE
            binding.livingareainformationLayout.LivingAreaInformationExpand.visibility = View.GONE
            binding.RFTioletLayout.toiletsExpand.visibility = View.VISIBLE
            binding.tvRFTiolet.visibility = View.VISIBLE

            binding.livingareainformationLayout.tvLAI.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_property,
                0,
                R.drawable.ic_verified,
                0
            )

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

            // ✅ Step 4: Proceed with Toilet Room API + RecyclerView setup
            adapterToilet = RFToiletAdapter(emptyList()) { selectedItem ->
                val data = selectedItem.rfToiletId
                lifecycleScope.launch(Dispatchers.IO) {
                    val toiletRoomReq = ToiletRoomReq(
                        appVersion = BuildConfig.VERSION_NAME,
                        rfToiletId = rfToiletId,
                    )
                    viewModel.getRfToiletRoomInformation(toiletRoomReq)
                }
            }

            ToiletRecyclerView()
            showProgressBar()
        }


        binding.livingareainformationLayout.btnLivingAreaInformationPrevious.setOnClickListener {
            binding.tvlivingareainformation.visibility= View.GONE
            binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand.visibility= View.VISIBLE



        }
        //Adapter   Ajit Ranjan Toilets 27/10/2025

        tvToiletAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.RFTioletLayout.SpinnerToilet.setAdapter(tvToiletAdapter)


        binding.RFTioletLayout.SpinnerToilet.setOnItemClickListener { parent, view, position, id ->
            selectedRFToiletApproval = parent.getItemAtPosition(position).toString()

            if (selectedRFToiletApproval == "Send for modification") {
                binding.RFTioletLayout.LivingToiletRemarks.visibility = View.VISIBLE
                binding.RFTioletLayout.etToiletRemarks.visibility = View.VISIBLE
                selectedRFToiletApproval = "M"
            } else {
                selectedRFToiletApproval = "A"
                binding.RFTioletLayout.etToiletRemarks.visibility = View.GONE
                binding.RFTioletLayout.LivingToiletRemarks.visibility = View.GONE
            }
        }

        binding.RFTioletLayout.btnToiletNext.setOnClickListener {

            // ✅ Step 1: Validate approval selection
            if (selectedRFToiletApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Step 2: If “Send for modification”, remarks are mandatory
            if (selectedRFToiletApproval == "M") {
                selectedRFToiletRemarks = binding.RFTioletLayout.etToiletRemarks.text.toString().trim()

                if (selectedRFToiletRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                selectedRFToiletRemarks = ""
            }

            // ✅ Step 3: Proceed only if validation successful



            binding.RFTioletLayout.toiletsExpand.visibility = View.GONE
            binding.RFTioletLayout.viewToilet.visibility = View.GONE
            binding.tvRFtoiletAdditionalSection.visibility = View.VISIBLE




            binding.RFTioletLayout.tvToilet.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.toilet,
                0,
                R.drawable.ic_verified,
                0
            )

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }










        }


        binding.RFTioletLayout.btnToiletPrevious.setOnClickListener {
            binding.tvlivingareainformation.visibility= View.VISIBLE
            binding.livingareainformationLayout.LivingAreaInformationExpand.visibility= View.VISIBLE
            binding.tvRFTiolet.visibility= View.GONE

        }


        val requestTcInfo = GetUrinalWashReq(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId,
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            facilityId = facilityId.toString()
        )
        viewModel.getToiletWashbasinDetails(requestTcInfo)
        showProgressBar()

        GetToiletWashbasinDetails()


    }




    private fun GetToiletWashbasinDetails() {
        viewModel.getToiletWashbasinDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 ->{


                        for (x in it.wrappedList){


                            binding.RFTioletAdditionalSectionLayout.UrinalAdditionalSection.setText(x.urinal)
                            binding.RFTioletAdditionalSectionLayout.WashbasinsAdditionalSection.setText(x.washbasin)
                            binding.RFTioletAdditionalSectionLayout.OverHeadTankAdditionalSection.setText(x.overheadTank)

                            binding.RFTioletAdditionalSectionLayout.ToiletAdditionalSectionFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.urinalFile,"Preview")

                            }
                            binding.RFTioletAdditionalSectionLayout.WashbasinsAdditionalSectionFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.washbasinFile,"Preview")

                            }
                            binding.RFTioletAdditionalSectionLayout.OverHeadTankAdditionalSectionFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.overheadTankFile,"Preview")

                            }



                        }





                    }
                    202 -> {
                        hideProgressBar()
                        Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()

                    }

                    301 ->
                    {
                        hideProgressBar()
                        Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    }

                    401 ->

                    {
                        hideProgressBar()

                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())

                    }

                }
            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->

        }





        //AdditionalSection Adapter   Ajit Ranjan Toilets 27/10/2025

        tvToiletAdditionalSectionAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.RFTioletAdditionalSectionLayout.SpinnerAdditionalSection.setAdapter(tvToiletAdditionalSectionAdapter)


        binding.RFTioletAdditionalSectionLayout.SpinnerAdditionalSection.setOnItemClickListener { parent, view, position, id ->
            selectedRFToiletAdditionalSanctionApproval = parent.getItemAtPosition(position).toString()

            if (selectedRFToiletAdditionalSanctionApproval == "Send for modification") {
                binding.RFTioletAdditionalSectionLayout.textViewAdditionalSectionRemarks.visibility = View.VISIBLE
                binding.RFTioletAdditionalSectionLayout.etAdditionalSectionRemarks.visibility = View.VISIBLE
                selectedRFToiletAdditionalSanctionApproval = "M"
            } else {
                selectedRFToiletAdditionalSanctionApproval = "A"
                binding.RFTioletAdditionalSectionLayout.etAdditionalSectionRemarks.visibility = View.GONE
                binding.RFTioletAdditionalSectionLayout.textViewAdditionalSectionRemarks.visibility = View.GONE
            }
        }

        binding.RFTioletAdditionalSectionLayout.btnAdditionalSectionNext.setOnClickListener {

            // ✅ Step 1: Validate approval selection
            if (selectedRFToiletAdditionalSanctionApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Step 2: If “Send for modification”, remarks are mandatory
            if (selectedRFToiletAdditionalSanctionApproval == "M") {
                selectedRFToiletAdditionalSanctionRemarks = binding.RFTioletAdditionalSectionLayout.etAdditionalSectionRemarks.text.toString().trim()

                if (selectedRFToiletAdditionalSanctionRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                selectedRFToiletAdditionalSanctionRemarks = ""
            }

            // ✅ Step 3: Proceed only if validation successful



            binding.RFTioletAdditionalSectionLayout.AdditionalSectionExpand.visibility = View.GONE
            binding.RFTioletAdditionalSectionLayout.viewToiletAdditionalSection.visibility = View.GONE
            binding.tvRFConstraintLayoutNonLivingArea.visibility = View.VISIBLE



            binding.RFTioletAdditionalSectionLayout.tvToiletAdditionalSection.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.toilet,
                0,
                R.drawable.ic_verified,
                0
            )

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }




// ✅ Step 4: Proceed to API call for Non-Living Area Information
            val requestLRLVRQ = LivingRoomListViewRQ(
                appVersion = BuildConfig.VERSION_NAME,
                tcId = centerId.toInt(),
                sanctionOrder = sanctionOrder,
                facilityId = facilityId
            )

            viewModel.getRfNonLivingAreaInformation(requestLRLVRQ)
            NonAreaInformation()






        }


        binding.RFTioletAdditionalSectionLayout.btnAdditionalSectionPrevious.setOnClickListener {
            binding.tvRFTiolet.visibility= View.VISIBLE
            binding.RFTioletLayout.toiletsExpand.visibility= View.VISIBLE
            binding.tvRFtoiletAdditionalSection.visibility= View.GONE

        }









    }







    @SuppressLint("SetTextI18n", "SuspiciousIndentation")

    private fun NonAreaInformation() {
        viewModel.NonAreaInformationRoom.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {



                            binding.RFNonLivingAreaLayout.WhetherFoodFor.text = safeText(x.preparedFood)
                            binding.RFNonLivingAreaLayout.reTheDiningAndRecreationAreaSeparate.text = safeText(x.separateAreas)
                            binding.RFNonLivingAreaLayout.NoOfStoolsChairsBenches.text = safeText(x.noOfSeats)
                            binding.RFNonLivingAreaLayout.WashArea.text = safeText(x.washArea)
                            binding.RFNonLivingAreaLayout.WhetherTv.text = safeText(x.noOfSeats)
                            binding.RFNonLivingAreaLayout.DiningLength.text = safeText(x.diningLength)
                            binding.RFNonLivingAreaLayout.DiningWidth.text = safeText(x.diningWidth)
                            binding.RFNonLivingAreaLayout.DiningArea.text = safeText(x.diningArea)
                            binding.RFNonLivingAreaLayout.RecreationLength.text = safeText(x.recreationLength)
                            binding.RFNonLivingAreaLayout.RecreationWidth.text = safeText(x.recreationWidth)
                            binding.RFNonLivingAreaLayout.RecreationArea.text = safeText(x.recreationArea)
                            binding.RFNonLivingAreaLayout.ReceptionArea.text = safeText(x.receptionArea)




                            binding.RFNonLivingAreaLayout.LengthRecreationAndDining.text = safeText(x.diningLength)
                            binding.RFNonLivingAreaLayout.AreaRecreationAndDining.text = safeText(x.diningArea)
                            binding.RFNonLivingAreaLayout.WidthRecreationAndDining.text = safeText(x.diningWidth)




                            binding.RFNonLivingAreaLayout.recreationFile.setOnClickListener {

                                showBase64ImageDialog(requireContext(), x.diningAreaFile, "Preview")


                            }
                            binding.RFNonLivingAreaLayout.recreationAndDiningFile.setOnClickListener {

                                showBase64ImageDialog(requireContext(), x.diningRecreationAreaFile, "Preview")


                            }





                            PreparedFoodFile=x.preprationFoodPdf
                            ReceptionAreaPdf= x.receptionAreaPdf.toString()

                            if (x.separateAreas=="Yes"){
                                binding.RFNonLivingAreaLayout.recreationFile.visibility=View.VISIBLE
                                binding.RFNonLivingAreaLayout.LinLayOutrecreationAndDiningNo.visibility=View.VISIBLE
                                binding.RFNonLivingAreaLayout.recreationAndDiningYes.visibility=View.GONE
                                binding.RFNonLivingAreaLayout.recreationAndDiningFile.visibility=View.GONE

                            }
                            else{

                                binding.RFNonLivingAreaLayout.recreationAndDiningFile.visibility=View.VISIBLE
                                binding.RFNonLivingAreaLayout.recreationAndDiningYes.visibility=View.VISIBLE
                                binding.RFNonLivingAreaLayout.LinLayOutrecreationAndDiningNo.visibility=View.GONE
                                binding.RFNonLivingAreaLayout.recreationFile.visibility=View.GONE
                            }

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

                    401 ->  {
                        hideProgressBar()

                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        //NonLiving Area Adapter
        tvNonLivingAreaAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.RFNonLivingAreaLayout.SpinnerNonLivingAreaInformation.setAdapter(tvNonLivingAreaAdapter)


        binding.RFNonLivingAreaLayout.SpinnerNonLivingAreaInformation.setOnItemClickListener { parent, view, position, id ->
            selectedNonAreaInfoApproval = parent.getItemAtPosition(position).toString()

            if (selectedNonAreaInfoApproval == "Send for modification") {
                binding.RFNonLivingAreaLayout.tvNonLivingAreaInformationRemarks.visibility = View.VISIBLE
                binding.RFNonLivingAreaLayout.etNonLivingAreaInformationRemarks.visibility = View.VISIBLE
                selectedNonAreaInfoApproval = "M"
            } else {
                selectedNonAreaInfoApproval = "A"
                binding.RFNonLivingAreaLayout.tvNonLivingAreaInformationRemarks.visibility = View.GONE
                binding.RFNonLivingAreaLayout.etNonLivingAreaInformationRemarks.visibility = View.GONE
            }
        }

        binding.RFNonLivingAreaLayout.btnNonLivingAreaInformationNext.setOnClickListener {

            // ✅ Step 1: Validate Approval selection
            if (selectedNonAreaInfoApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Step 2: Validate remarks if modification selected
            if (selectedNonAreaInfoApproval == "M") {
                selectedRFNonLivingAreaRemarks = binding.RFNonLivingAreaLayout.etNonLivingAreaInformationRemarks.text.toString().trim()
                if (selectedRFNonLivingAreaRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                selectedRFNonLivingAreaRemarks = ""
            }

            // ✅ Step 3: Proceed if validation passed
            binding.RFNonLivingAreaLayout.viewNonLivingAreaInfor.visibility = View.GONE
            binding.RFNonLivingAreaLayout.NonLivingAreaInfoExpand.visibility = View.GONE
            binding.tvRFConstraintLayoutIndoorGame.visibility = View.VISIBLE

            binding.RFNonLivingAreaLayout.tvNonLivingAreaInfor.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_class,
                0,
                R.drawable.ic_verified,
                0
            )

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

            // ✅ Step 4: Prepare and set Indoor Game RecyclerView
            adapterIndoorGame = IndoorGameRFAdapter(emptyList()) { game ->
                showBase64ImageDialog(requireContext(), game.indoorGamePdf, "Preview")
            }

            binding.RFIndoorGameLayout.recyclerViewInddorGame.adapter = adapterIndoorGame
            binding.RFIndoorGameLayout.recyclerViewInddorGame.layoutManager = LinearLayoutManager(requireContext())

            // ✅ Step 5: API Call for Indoor Game Details
            val rfGameRequest = RFGameRequest(
                appVersion = BuildConfig.VERSION_NAME,
                tcId = centerId.toInt(),
                sanctionOrder = sanctionOrder,
                imeiNo = AppUtil.getAndroidId(requireContext()),
                facilityId = facilityId
            )

            showProgressBar()
            viewModel.getRfIndoorGameDetails(rfGameRequest)
            IndoorGameRecyclerView()
        }


        binding.RFNonLivingAreaLayout.btnNonLivingAreaInformationPrevious.setOnClickListener {

            binding.tvRFConstraintLayoutNonLivingArea.visibility= View.GONE
            binding.RFTioletLayout.toiletsExpand.visibility = View.VISIBLE



        }
    }


    @SuppressLint("SuspiciousIndentation")
    private fun collectInsfrastructureDetailsAndComplains() {
        viewModel.CompliancesRFQTReqRFQT.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        hideProgressBar()
                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {
                            binding.infrastructureDetailsAndCompliancesLayout.onwershipOfBulding.text=x.ownership
                            binding.infrastructureDetailsAndCompliancesLayout.areaOfTheBuilding.text = x.buildingArea
                            binding.infrastructureDetailsAndCompliancesLayout.HostelNameBoard.text = x.hostelNameBoard
                            binding.infrastructureDetailsAndCompliancesLayout.BasicInformationBoard.text = x.basicInformationBoard
                            binding.infrastructureDetailsAndCompliancesLayout.SecuringWiresDone.text = x.securingWiresDone
                            binding.infrastructureDetailsAndCompliancesLayout.RoofofBulding.text = x.roof
                            binding.infrastructureDetailsAndCompliancesLayout.WhetherItIsStructurally.text = x.plastring
                            binding.infrastructureDetailsAndCompliancesLayout.visibleSignsOfLeakages.text = x.leakage
                            binding.infrastructureDetailsAndCompliancesLayout.ConformanceToDduGky.text = x.conformanceDdu
                            binding.infrastructureDetailsAndCompliancesLayout.ProtectionOfStairs.text = x.protectionStairs
                            binding.infrastructureDetailsAndCompliancesLayout.CirculatingArea.text = x.circulatingArea
                            binding.infrastructureDetailsAndCompliancesLayout.Corridor.text = x.corridor
//                            binding.infrastructureDetailsAndCompliancesLayout.ElectricalWiringAndStandards.text = x.corridor
                            binding.infrastructureDetailsAndCompliancesLayout.SwitchBoardsAndPanelBoards.text = x.switchBoardsPanelBoards
                            binding.infrastructureDetailsAndCompliancesLayout.StudentEntitlement.text = x.studentEntitlementBoard
                            binding.infrastructureDetailsAndCompliancesLayout.ContactDetailOfImportantPeople.text = x.contactDetailImportantPeople
                            binding.infrastructureDetailsAndCompliancesLayout.FoodSpecificationBoard.text = x.foodSpecificationBoard
                            binding.infrastructureDetailsAndCompliancesLayout.Area.text = x.openSpaceArea




                            binding.infrastructureDetailsAndCompliancesLayout.OnwershipOfBuldingFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.selfDeclaration, "RFQInfraDetailbuildingPhotosFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }


                            binding.infrastructureDetailsAndCompliancesLayout.BuildingAreaSQFPlanFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.buildingPlanFile, "")
//
                            }


                            binding.infrastructureDetailsAndCompliancesLayout.RoofLavelFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.buildingPhotosFile, "RFQInfraDetailprotectionStairsProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }




                            binding.infrastructureDetailsAndCompliancesLayout.WhetherItIsStructurallyFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.wallPhotosFile, "")

                            }




                            binding.infrastructureDetailsAndCompliancesLayout.VisibleSignsLeakagesFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.leakagesProofFile, "RFQInfraDetailhostelNameBoardProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }

                            binding.infrastructureDetailsAndCompliancesLayout.ProtectionOfStairsFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.protectionStairsProofFile, "RFQInfraDetailhostelNameBoardProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }
                            binding.infrastructureDetailsAndCompliancesLayout.HostelNameBoardFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.hostelNameBoardProofFile, "RFQInfraDetailhostelNameBoardProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }

                            binding.infrastructureDetailsAndCompliancesLayout.StudentEntitlementFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.studentEntitlementBoardProofFile, "RFQInfraDetailhostelNameBoardProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }



                            binding.infrastructureDetailsAndCompliancesLayout.ContactDetailOfImportantPeopleFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.contactDetailImportantPeopleproofFile, "Contact Detail Of Important People")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }
                            binding.infrastructureDetailsAndCompliancesLayout.SpecificationBoardFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.foodSpecificationBoardFile, "RFQInfraDetailfoodSpecificationBoardFileo")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }
                            binding.infrastructureDetailsAndCompliancesLayout.BasicInformationBoardFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.basicInformationBoardproofFile, "RFQInfraDetailbasicInformationBoardproofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }
                            binding.infrastructureDetailsAndCompliancesLayout.SecuringWiresDoneFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.securingWiresDoneProofFile, "RFQInfraDetailbasicsecuringWiresDoneProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }
                            binding.infrastructureDetailsAndCompliancesLayout.CorridorFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.corridorProofFile, "RFQInfraDetailcorridorProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }
                            binding.infrastructureDetailsAndCompliancesLayout.circulatingAreaProofFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.circulatingAreaProofFile, "RFQInfraDetailcirculatingAreaProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }

                            binding.infrastructureDetailsAndCompliancesLayout.VisibleSignsLeakagesFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.leakagesProofFile, "RFQInfraDetailleakagesProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)
                            }
                            binding.infrastructureDetailsAndCompliancesLayout.ConformanceToDduGkyFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.conformanceDduProofFile, "RFQInfraDetailconformanceDduProofFile")
//                                openBase64Pdf(requireContext(), RFQTBasicInfoPdf)


                            }
                            binding.infrastructureDetailsAndCompliancesLayout.SwitchBoardsAndPanelBoardsFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.switchBoardsPanelBoardsProofFile, "RFQInfraDetailswitchBoardsPanelBoardsProofFile")

                            }

                            binding.infrastructureDetailsAndCompliancesLayout.StudentEntitlementFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.studentEntitlementBoardProofFile, "Detail student Entitlement")

                            }




//                            RFQInfraDetailswallPhotosFileFile = x.wallPhotosFile.toString()
////                            RFQInfraDetailsRoofbuildingFile = x.buildingPlanFile.toString()
//                            RFQInfraDetailsbuildingPlanFile = x.buildingPlanFile.toString()
//                            RFQInfraDetailprotectionStairsProofFile =x.protectionStairsProofFile.toString()
//                            RFQInfraDetailhostelNameBoardProofFile = x.hostelNameBoardProofFile.toString()
//                            RFQInfraDetailfoodSpecificationBoardFile = x.foodSpecificationBoardFile.toString()
//                            RFQInfraContactDetailOfImportantPeopleFile = x.contactDetailImportantPeopleproofFile.toString()
//                            RFQInfraDetailbasicInformationBoardproofFile = x.basicInformationBoardproofFile.toString()
//                            RFQInfraDetailbasicsecuringWiresDoneProofFile = x.securingWiresDoneProofFile.toString()
//                            RFQInfraDetailcorridorProofFile =x.corridorProofFile.toString()
//                            RFQInfraDetailcirculatingAreaProofFile = x.circulatingAreaProofFile.toString()
//                            RFQInfraDetailbuildingPhotosFile = x.selfDeclaration.toString()
//                            RFQInfraDetailleakagesProofFile =x.leakagesProofFile.toString()
//                            RFQInfraDetailconformanceDduProofFile= x.conformanceDduProofFile.toString()
//                            RFQInfraDetailswitchBoardsPanelBoardsProofFile= x.switchBoardsPanelBoardsProofFile.toString()
//                            RFQInfraDetailcontactDetailImportantPeopleproofFile= x.contactDetailImportantPeopleproofFile.toString()
//                            RFQInfraDetailstudentEntitlementBoardProofFile= x.studentEntitlementBoardProofFile.toString()


                        }
                    }

                    202 -> {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "No data available.",
                            Toast.LENGTH_SHORT
                        ).show()}

                    301 -> {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()}

                    401 ->  {
                        hideProgressBar()

                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        //Adapter Electrical
        nfrastructureDetailsAndCompliancesAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.infrastructureDetailsAndCompliancesLayout.SpinnerIDC.setAdapter(nfrastructureDetailsAndCompliancesAdapter)
        binding.infrastructureDetailsAndCompliancesLayout.SpinnerIDC.setOnItemClickListener { parent, view, position, id ->
            selectedInfrastctureDetailsComplainsApproval = parent.getItemAtPosition(position).toString()

            if (selectedInfrastctureDetailsComplainsApproval == "Send for modification") {
                binding.infrastructureDetailsAndCompliancesLayout.tvSelectApprovalIDC.visibility = View.VISIBLE
                binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks.visibility = View.VISIBLE
                selectedInfrastctureDetailsComplainsApproval = "M"
            } else {
                selectedInfrastctureDetailsComplainsApproval = "A"
                binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks.visibility = View.GONE
                binding.infrastructureDetailsAndCompliancesLayout.tvSelectApprovalIDC.visibility = View.GONE
            }
        }

        binding.infrastructureDetailsAndCompliancesLayout.btnIDCNext.setOnClickListener {

            // ✅ Step 1: Validate approval selection
            if (selectedInfrastctureDetailsComplainsApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Step 2: If selected “M” (Send for modification), remarks must not be empty
            if (selectedInfrastctureDetailsComplainsApproval == "M") {
                selectedInfrastctureDetailsComplainsRemarks =
                    binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks.text.toString().trim()

                if (selectedInfrastctureDetailsComplainsRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                selectedInfrastctureDetailsComplainsRemarks = ""
            }

            // ✅ Step 3: Proceed only if validation passed
            binding.infrastructureDetailsAndCompliancesLayout.viewIDC.visibility = View.GONE
            binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand.visibility = View.GONE
            binding.tvlivingareainformation.visibility = View.VISIBLE

            binding.infrastructureDetailsAndCompliancesLayout.tvIDC.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.infrastructure,
                0,
                R.drawable.ic_verified,
                0
            )

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

            // ✅ Step 4: Proceed to API + RecyclerView setup only if validation OK
            adapter = LivingAreaInformationAdapter(emptyList()) { center ->
                val requestTcRoomDetails = RfLivingAreaInformationRQ(
                    appVersion = BuildConfig.VERSION_NAME,
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    roomNo = center.roomNo.toInt(),
                    facilityId = facilityId
                )

                viewModel.getRfLivingAreaInformation(requestTcRoomDetails)
                showProgressBar()

                viewModel.fLivingAreaInformation.observe(viewLifecycleOwner) { result ->
                    result.onSuccess {
                        hideProgressBar()
                        when (it.responseCode) {
                            200 -> {
                                val tcInfoData = it.wrappedList
                                for (x in tcInfoData) {
                                    val dialogBinding = RoominformationPopdialogBinding.inflate(layoutInflater)
                                    val dialog = AlertDialog.Builder(requireContext())
                                        .setView(dialogBinding.root)
                                        .create()
                                    dialog.show()

                                    dialogBinding.progressBar.visibility = View.VISIBLE
                                    dialogBinding.progressBar.visibility = View.GONE

                                    // ✅ Set Text Values
                                    dialogBinding.laiTypeOfRoof.text = safeText(x.roofType)
                                    dialogBinding.laiFalseCelling.text = safeText(x.falseCeiling)
                                    dialogBinding.laiHeightofCelling.text = safeText(x.ceilingHeight.toString())

                                    val noOfStudentPermitted = x.windowArea!!.toDouble() / 25.0
                                    dialogBinding.NoOfStudentPermitted.text = noOfStudentPermitted.toString()
                                    dialogBinding.laiLength.text = safeText(x.length.toString())
                                    dialogBinding.laiWidth.text = safeText(x.width.toString())
                                    dialogBinding.laiArea.text = safeText(x.area.toString())
                                    dialogBinding.laiwindowsArea.text = safeText(x.windowArea.toString())
                                    dialogBinding.laiCotInNo.text = safeText(x.cot.toString())
                                    dialogBinding.laiMattersInNo.text = safeText(x.mattress.toString())
                                    dialogBinding.laiBedSheetInNo.text = safeText(x.bedSheet.toString())
                                    dialogBinding.laiAirCondtion.text = safeText(x.airCondtion.toString())
                                    dialogBinding.laiLights.text = safeText(x.lights.toString())
                                    dialogBinding.laiStorage.text = safeText(x.storage.toString())
                                    dialogBinding.LiaBasicInformationBoard.text = safeText(x.infoBoard.toString())

                                    // ✅ Set Image Click Listeners
                                    dialogBinding.LiaBasicInformationBoardFile.setOnClickListener {
                                        showBase64ImageDialog(requireContext(), "", "Room Preview")
                                    }
                                    dialogBinding.laiTypeOfRoofFile.setOnClickListener {
                                        showBase64ImageDialog(requireContext(), x.roofTypePdf, "Room Preview")
                                    }
                                    dialogBinding.laiFalseCellingFile.setOnClickListener {
                                        showBase64ImageDialog(requireContext(), x.falseCeilingPdf, "False Ceiling Preview")
                                    }
                                    dialogBinding.laiHeightofCellingFile.setOnClickListener {
                                        showBase64ImageDialog(requireContext(), x.ceilingHeightPdf, "Ceiling Height Preview")
                                    }
                                    dialogBinding.laiwindowsAreaFile.setOnClickListener {
                                        showBase64ImageDialog(requireContext(), x.windowAreaPdf, "Window Area Preview")
                                    }
                                    dialogBinding.laiCotInNoFile.setOnClickListener {
                                        showBase64ImageDialog(requireContext(), x.cotPdf, "Cot Preview")
                                    }
                                    dialogBinding.laiMattersInNoFile.setOnClickListener {
                                        showBase64ImageDialog(requireContext(), x.mattressPdf, "Mattress Preview")
                                    }
                                    dialogBinding.laiBedSheetInNoFile.setOnClickListener {
                                        showBase64ImageDialog(requireContext(), x.bedSheetPdf, "Bed Sheet Preview")
                                    }
                                    dialogBinding.laiAirCondtionFile.setOnClickListener {
                                        showBase64ImageDialog(requireContext(), x.airConditionPdf, "AirCondition Preview")
                                    }
                                    dialogBinding.laiLightsFile.setOnClickListener {
                                        showBase64ImageDialog(requireContext(), x.lightPdf, "Light Preview")
                                    }
                                    dialogBinding.laiStorageFile.setOnClickListener {
                                        showBase64ImageDialog(requireContext(), x.storagePdf, "Storage Preview")
                                    }

                                    dialogBinding.backButton.setOnClickListener { dialog.dismiss() }
                                }
                            }

                            202 ->

                            {

                                hideProgressBar()
                                Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                            }

                            301 ->
                            {

                                hideProgressBar()
                                Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                            }
                            401 -> {
                                hideProgressBar()

                                AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                            }
                        }
                    }
                    result.onFailure {
                        hideProgressBar()
                        Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                viewModel.loading.observe(viewLifecycleOwner) { loading ->
                    binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
                }
            }

            binding.livingareainformationLayout.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.livingareainformationLayout.recyclerView.adapter = adapter

            val livingRoomListViewReq = LivingRoomListViewRQ(
                appVersion = BuildConfig.VERSION_NAME,
                tcId = centerId.toInt(),
                sanctionOrder = sanctionOrder,
                facilityId = facilityId
            )

            viewModel.getlivingRoomListView(livingRoomListViewReq)
            showProgressBar()
            RoomRecyclerView()
        }

        binding.infrastructureDetailsAndCompliancesLayout.btnIDCPrevious.setOnClickListener {
            binding.tvinfrastructureDetailsAndCompliances.visibility= View.GONE
            binding.residentialfacilityqteamInfoLayout.RFQTInfoExpand.visibility= View.VISIBLE
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private  fun IndoorGameRecyclerView(){
        viewModel.RfIndoorGameDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {

                when (it.responseCode) {
                    200 -> {
                        hideProgressBar()
                        adapterIndoorGame.updateData(it.wrappedList ?: emptyList())

                    }

                    202 ->
                    {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "No data available.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }




                    301 ->



                    {
                        hideProgressBar()

                        Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    401 -> {
                        hideProgressBar()

                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }

                result.onFailure {
                    hideProgressBar()
                    Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        //Indoor Game Adapter
        RFIndoorGameAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.RFIndoorGameLayout.SpinnerIndoorGame.setAdapter(RFIndoorGameAdapter)
        binding.RFIndoorGameLayout.SpinnerIndoorGame.setOnItemClickListener { parent, view, position, id ->
            selectedIndoorGameApproval = parent.getItemAtPosition(position).toString()

            if (selectedIndoorGameApproval == "Send for modification") {
                binding.RFIndoorGameLayout.IndoorGameRemarks.visibility = View.VISIBLE
                binding.RFIndoorGameLayout.etIndoorGameRemarks.visibility = View.VISIBLE
                selectedIndoorGameApproval = "M"
            } else {
                selectedIndoorGameApproval = "A"
                binding.RFIndoorGameLayout.IndoorGameRemarks.visibility = View.GONE
                binding.RFIndoorGameLayout.etIndoorGameRemarks.visibility = View.GONE
            }
        }

        binding.RFIndoorGameLayout.btnIndoorGameNext.setOnClickListener {

            // ✅ Step 1: Validate Approval selection
            if (selectedIndoorGameApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Step 2: Validate Remarks if “Modification” selected
            if (selectedIndoorGameApproval == "M") {
                selectedIndoorGameApprovalRemark = binding.RFIndoorGameLayout.etIndoorGameRemarks.text.toString().trim()
                if (selectedIndoorGameApprovalRemark.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                selectedIndoorGameApprovalRemark = ""
            }

            // ✅ Step 3: Proceed if validation passed
            binding.RFIndoorGameLayout.viewIndoorGame.visibility = View.GONE
            binding.RFIndoorGameLayout.IndoorGameExpand.visibility = View.GONE
            binding.RFResidentialConstraintLayoutFacilitiesAvailable.visibility = View.VISIBLE

            // ✅ Step 4: Show verified icon for section
            binding.RFIndoorGameLayout.tvIndoorGame.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.tabletennis,
                0,
                R.drawable.ic_verified,
                0
            )

            // ✅ Step 5: Smooth scroll to top
            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

            // ✅ Step 6: API Call for Residential Facilities Available
            val requestTcInfo = RfCommonReq(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                tcId = centerId.toInt(),
                sanctionOrder = sanctionOrder,
                imeiNo = AppUtil.getAndroidId(requireContext()),
                facilityId = facilityId
            )

            showProgressBar()
            viewModel.getResidentialFacilitiesAvailable(requestTcInfo)

            // ✅ Step 7: Move to next layout
            ResidentialFacilitiesForm()
        }



        binding.RFIndoorGameLayout.btnIndoorGamePrevious.setOnClickListener {
            binding.tvRFConstraintLayoutIndoorGame.visibility= View.GONE
            binding.RFNonLivingAreaLayout.NonLivingAreaInfoExpand.visibility= View.VISIBLE
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private  fun RFSupportFacilitiesRecyclerView(){
        viewModel.RFSupportFacilitiesAvailable.observe(viewLifecycleOwner) { result ->
            result.onSuccess {

                when (it.responseCode) {
                    200 -> {
                        hideProgressBar()
                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {
                            binding.rfSupportFacilitiesAvailableLayout.SafeDrinikingAavailable.text =
                                (x.safeDrinking)
                            binding.rfSupportFacilitiesAvailableLayout.FirstAidKit.text =
                                (x.firstAidKit)
                            binding.rfSupportFacilitiesAvailableLayout.FireFightingEquipmentr.text =
                                (x.fireFighting)
                            binding.rfSupportFacilitiesAvailableLayout.BiometricDevice.text =
                                (x.biometricDevice)
                            binding.rfSupportFacilitiesAvailableLayout.ElectricalPowerBackup.text =
                                (x.powerBackup)
                            binding.rfSupportFacilitiesAvailableLayout.GrievanceRegister.text =
                                (x.grievanceRegister)
//                               ImageData Store in Variable

                            RFsafeDrinkingeFile = x.safeDrinkingPdf
                            RFfirstAidKitFile = x.firstAidKitPdf
                            RFfireFightingFile = x.fireFightingPdf
                            RFbiometricDeviceFile = x.biometricDevicePdf
                            RFpowerBackupFile = x.powerBackupPdf
                            RFgrievanceRegisterFile = x.biometricDevicePdf
                        }

                    }

                    202 ->
                    {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "No data available.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }




                    301 ->



                    {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    401 -> {
                        hideProgressBar()

                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }

                result.onFailure {
                    hideProgressBar()
                    Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        //Support Facilities Available Adapter
        RFResidentialSupportFacilitiesAvailableAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.rfSupportFacilitiesAvailableLayout.SpinnerRFResidentialsupportFacality.setAdapter(RFResidentialSupportFacilitiesAvailableAdapter)


        binding.rfSupportFacilitiesAvailableLayout.SpinnerRFResidentialsupportFacality.setOnItemClickListener { parent, view, position, id ->
            selectedResidintislSupportFacilityApproval = parent.getItemAtPosition(position).toString()

            if (selectedResidintislSupportFacilityApproval == "Send for modification") {
                binding.rfSupportFacilitiesAvailableLayout.tvRFResidentialsupportFacalityRemarks.visibility = View.VISIBLE
                binding.rfSupportFacilitiesAvailableLayout.etRFResidentialsupportFacalityRemarks.visibility = View.VISIBLE
                selectedResidintislSupportFacilityApproval = "M"
            } else {
                selectedResidintislSupportFacilityApproval = "A"
                binding.rfSupportFacilitiesAvailableLayout.tvRFResidentialsupportFacalityRemarks.visibility = View.GONE
                binding.rfSupportFacilitiesAvailableLayout.etRFResidentialsupportFacalityRemarks.visibility = View.GONE
            }
        }

        binding.rfSupportFacilitiesAvailableLayout.btnRFResidentialsupportFacalityNext.setOnClickListener {

            // ✅ Step 1: Validate Approval Selection
            if (selectedResidintislSupportFacilityApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Step 2: Validate Remarks if "Send for modification"
            if (selectedResidintislSupportFacilityApproval == "M") {
                selectedResidintislSupportFacilityApprovalRemark =
                    binding.rfSupportFacilitiesAvailableLayout.etRFResidentialsupportFacalityRemarks.text.toString().trim()

                if (selectedResidintislSupportFacilityApprovalRemark.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                selectedResidintislSupportFacilityApprovalRemark = ""
            }

            // ✅ Step 3: Proceed Only After Validation Passes
            binding.rfSupportFacilitiesAvailableLayout.viewRFResidentialsupportFacality.visibility = View.GONE
            binding.rfSupportFacilitiesAvailableLayout.RFResidentialsupportFacalityExpand.visibility = View.GONE

            // ✅ Step 4: Show Submit Button
            binding.btnSubmitFinal.visibility = View.VISIBLE

            // ✅ Step 5: Update Icon to “Verified”
            binding.rfSupportFacilitiesAvailableLayout.tvRFResidentialSupportFacality.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_signage,
                0,
                R.drawable.ic_verified,
                0
            )

            // ✅ Step 6: Smooth Scroll to Top
            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

            // ✅ Step 7: (Optional) Submit or Final API trigger can be placed here if needed
        }



        binding.rfSupportFacilitiesAvailableLayout.btnRFResidentialsupportFacalityPrevious.setOnClickListener {
            binding.RFRFSupportFacilitiesAvailable.visibility= View.GONE
            binding.btnSubmitFinal.visibility= View.GONE
            binding.RFResidentialFacilitiesAvailable.RFResidentialFacalityExpand.visibility= View.VISIBLE
        }
    }


    private fun collectFinalSubmitData() {

        viewModel.insertRFSrlmVerification.observe(viewLifecycleOwner) { result ->

            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        hideProgressBar()
                        if (selectedRFBasicInformationApproval == "M" ||
                            selectedInfrastctureDetailsComplainsApproval == "M" ||
                            selectedRFLevingAreaInformationApproval == "M" ||
                            selectedRFToiletApproval == "M" ||
                            selectedNonAreaInfoApproval == "M" ||
                            selectedIndoorGameApproval == "M" ||
                            selectedResidintislFacilityApproval == "M" ||
                            selectedResidintislSupportFacilityApproval == "M"
                        )
                        {
                            hideProgressBar()
                            // ✅ All approvals are "M"
                            Toast.makeText(
                                requireContext(),
                                "Send to Operation Team Successfully!!",
                                Toast.LENGTH_LONG
                            ).show()

                            // Optional: You can call your API or navigation here
                            // viewModel.sendToOperationTeam()
                            // findNavController().navigate(R.id.nextScreen)

                        } else {
                            hideProgressBar()
                            // ❌ Some or all approvals are not "M"
                            Toast.makeText(
                                requireContext(),
                                "Saved  Successfully!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        findNavController().navigateUp()
                    }

                    202 ->

                    {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            it.responseDesc,
                            Toast.LENGTH_SHORT
                        ).show()}

                    301 -> {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()}

                    401 ->

                    {
                        hideProgressBar()

                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }



    private  fun ResidentialFacilitiesForm(){
        viewModel.RFResidentialFacilitiesAvailable.observe(viewLifecycleOwner) { result ->
            result.onSuccess {

                when (it.responseCode) {
                    200 -> {
                        hideProgressBar()
                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {

                            binding.RFResidentialFacilitiesAvailable.WardenCare.text=(x.wardenCaretakerFemale)
                            binding.RFResidentialFacilitiesAvailable.MaleDoctor.text=(x.maleDoctor)
                            binding.RFResidentialFacilitiesAvailable.WardenCaretakerMale.text=(x.wardenCaretakerMale)
                            binding.RFResidentialFacilitiesAvailable.HostelsSeparated.text=(x.hostelsSeparated)
                            binding.RFResidentialFacilitiesAvailable.FemaleDoctor.text=(x.femaleDoctor)
                            binding.RFResidentialFacilitiesAvailable.SecurityGuards.text=(x.securityGuards)
//                               ImageData Store in Variable
                            RFWardenCareFile=x.wardenCaretakerFemalePdf
                            RFMaleDoctorFile=x.maleDoctorPdf
                            RFFemaleDoctorFile=x.femaleDoctorPdf
                            RFWardenCaretakerMaleFile=x.wardenCaretakerMalePdf
                            RFHostelsSeparatedFile =x.hostelsSeparatedPdf
                            RFSecurityGuardsFile=x.securityGuardsPdf




                        }





                    }

                    202 ->
                    {
                        hideProgressBar()

                        Toast.makeText(
                            requireContext(),
                            "No data available.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }




                    301 ->



                    {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    401 ->

                    {
                        hideProgressBar()

                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }



                }

                result.onFailure {
                    hideProgressBar()
                    Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        //Indoor Game Adapter
        RFResidentialFacilitiesAvailableAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.RFResidentialFacilitiesAvailable.SpinnerRFResidentialFacality.setAdapter(RFResidentialFacilitiesAvailableAdapter)


        binding.RFResidentialFacilitiesAvailable.SpinnerRFResidentialFacality.setOnItemClickListener { parent, view, position, id ->
            selectedResidintislFacilityApproval = parent.getItemAtPosition(position).toString()

            if (selectedResidintislFacilityApproval == "Send for modification") {
                binding.RFResidentialFacilitiesAvailable.tvRFResidentialFacalityRemarks.visibility = View.VISIBLE
                binding.RFResidentialFacilitiesAvailable.etRFResidentialFacalityRemarks.visibility = View.VISIBLE
                selectedResidintislFacilityApproval = "M"
            } else {
                selectedResidintislFacilityApproval = "A"
                binding.RFResidentialFacilitiesAvailable.tvRFResidentialFacalityRemarks.visibility = View.GONE
                binding.RFResidentialFacilitiesAvailable.etRFResidentialFacalityRemarks.visibility = View.GONE
            }
        }

        binding.RFResidentialFacilitiesAvailable.btnRFResidentialFacalityNext.setOnClickListener {

            // ✅ Step 1: Validate approval selection
            if (selectedResidintislFacilityApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Step 2: Validate remarks if "Send for modification" selected
            if (selectedResidintislFacilityApproval == "M") {
                selectedResidintislFacilityApprovalRemark =
                    binding.RFResidentialFacilitiesAvailable.etRFResidentialFacalityRemarks.text.toString().trim()

                if (selectedResidintislFacilityApprovalRemark.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                selectedResidintislFacilityApprovalRemark = ""
            }

            // ✅ Step 3: Proceed only if validation passes
            binding.RFResidentialFacilitiesAvailable.viewRFResidentialFacality.visibility = View.GONE
            binding.RFResidentialFacilitiesAvailable.RFResidentialFacalityExpand.visibility = View.GONE
            binding.RFRFSupportFacilitiesAvailable.visibility = View.VISIBLE

            // ✅ Step 4: Set verified icon for this section
            binding.RFResidentialFacilitiesAvailable.tvRFResidentialFacality.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_equipment,
                0,
                R.drawable.ic_verified,
                0
            )

            // ✅ Step 5: Smooth scroll to top
            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

            // ✅ Step 6: API call for Support Facilities
            val rfGameRequest = RFGameRequest(
                appVersion = BuildConfig.VERSION_NAME,
                tcId = centerId.toInt(),
                sanctionOrder = sanctionOrder,
                imeiNo = AppUtil.getAndroidId(requireContext()),
                facilityId = facilityId
            )

            showProgressBar()
            viewModel.getRFSupportFacilitiesAvailable(rfGameRequest)
            RFSupportFacilitiesRecyclerView()
        }



        binding.RFResidentialFacilitiesAvailable.btnRFResidentialFacalityPrevious.setOnClickListener {

            binding.RFResidentialConstraintLayoutFacilitiesAvailable.visibility= View.GONE
            binding.RFIndoorGameLayout.IndoorGameExpand.visibility= View.VISIBLE
        }}
    private  fun RoomRecyclerView(){
        viewModel.livingRoomListView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {

                    200 ->{


                        hideProgressBar()
                        adapter.updateData(it.wrappedList ?: emptyList())


                    }








                    202 -> {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "No data available.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    301 ->
                    {

                        hideProgressBar()

                        Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }



                    401 ->
                    {
                        hideProgressBar()

                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }
    private  fun ToiletRecyclerView()
    {




        viewModel.ToiletRoomInformationView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        hideProgressBar()
                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {
                            // Wait for 2 seconds
                            val binding =
                                TriPopdialogBinding.inflate(layoutInflater)
                            val dialog = AlertDialog.Builder(requireContext())
                                .setView(binding.root)
                                .create()
                            dialog.show()
                            binding.progressBar.visibility = View.VISIBLE
//                                    delay(2000L)

                            // Hide progress bar
                            binding.progressBar.visibility = View.GONE


                            // Now set data to TextViews
                            binding.triTypeOfFlooring.text =
                                safeText(x.flooring)
                            binding.ConnectionToRunningWater.text = safeText(x.runningWater)
                            binding.ToiletType.text =
                                safeText(x.type.toString())
                            binding.TriLights.text = safeText(x.lights.toString())




                            //open Image
                            binding.laiTypeOfFlooringFile.setOnClickListener {

                                showBase64ImageDialog(requireContext(), x.floorPdf, "Floor Preview")


                            }
                            binding.TriLightsFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.lightPdf, "Light Preview")


                            }
                            binding.ConnectionToRunningWaterFile.setOnClickListener {
                                showBase64ImageDialog(requireContext(), x.runningWaterFile, "Running WaterFile Preview")


                            }




                            binding.backButton.setOnClickListener { dialog.dismiss() }
                        }

                    }





                    202 -> {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "No data available.",
                            Toast.LENGTH_SHORT
                        ).show()}

                    301 ->



                    {

                        hideProgressBar()


                        Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()}

                    401 ->  {
                        hideProgressBar()

                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }

                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE


        }




        binding.RFTioletLayout.recyclerViewToilet.layoutManager = LinearLayoutManager(requireContext())
        binding.RFTioletLayout.recyclerViewToilet.adapter = adapterToilet

        val ToiletCountListReq = ToiletCountListReq(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            facilityId = facilityId.toString()

        )



        viewModel.getToiletCountList(ToiletCountListReq)


        viewModel.ToiletCountListView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {


                    200 ->

                    {


                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {

                            binding.RFTioletLayout.TvToilet.text=x.toiletCount

                            binding.RFTioletLayout.TvBathroom.text=x.washroomCount
                            binding.RFTioletLayout.TvToiletBathroom.text=x.toiletWashroomCount


                            binding.RFTioletLayout.TvToilet.paintFlags = binding.RFTioletLayout.TvToilet.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                            binding.RFTioletLayout.TvBathroom.paintFlags = binding.RFTioletLayout.TvBathroom.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                            binding.RFTioletLayout.TvToiletBathroom.paintFlags = binding.RFTioletLayout.TvToiletBathroom.paintFlags or Paint.UNDERLINE_TEXT_FLAG


                            binding.RFTioletLayout.tvToilet.setOnClickListener {

                                binding.RFTioletLayout.LinLayoutCardView.visibility= View.VISIBLE
                                binding.RFTioletLayout.LinLayoutRecyclerView.visibility= View.  GONE

                                binding.RFTioletLayout.tvToilet.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.toilet,
                                    0,
                                    0,
                                    0
                                )


                            }
                            binding.RFTioletLayout.linLayoutToilet.setOnClickListener {


                                if (x.toiletCount!="0") {
                                    ListViewToilet("Toilet")
                                    binding.RFTioletLayout.tvToilet.setCompoundDrawablesWithIntrinsicBounds(
                                        R.drawable.back_black,
                                        0,
                                        0,
                                        0
                                    )

                                    binding.RFTioletLayout.LinLayoutCardView.visibility= View.GONE
                                    binding.RFTioletLayout.LinLayoutRecyclerView.visibility= View.VISIBLE

                                }

                            }
                            binding.RFTioletLayout.LinLayoutBathroom.setOnClickListener {


                                if (x.washroomCount!="0"){
                                    binding.RFTioletLayout.tvToilet.setCompoundDrawablesWithIntrinsicBounds(
                                        R.drawable.back_black,
                                        0,
                                        0,
                                        0
                                    )
                                    binding.RFTioletLayout.LinLayoutCardView.visibility= View.GONE
                                    binding.RFTioletLayout.LinLayoutRecyclerView.visibility= View.VISIBLE
                                    ListViewToilet("Washroom")

                                }



                            }
                            binding.RFTioletLayout.LinLayoutToiletAndBathroom.setOnClickListener {


                                if (x.toiletWashroomCount!="0") {
                                    ListViewToilet("Toilet Cum Washroom")

                                    binding.RFTioletLayout.tvToilet.setCompoundDrawablesWithIntrinsicBounds(
                                        R.drawable.back_black,
                                        0,
                                        0,
                                        0
                                    )

                                    binding.RFTioletLayout.LinLayoutCardView.visibility= View.GONE
                                    binding.RFTioletLayout.LinLayoutRecyclerView.visibility= View.VISIBLE

                                }


                            }










                        }




                        hideProgressBar()


                    }
                    202 ->


                    {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "No data available.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    301 -> {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()}

                    401 ->  {
                        hideProgressBar()

                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }







    }


    private fun ListViewToilet(toiletType: String){

        binding.RFTioletLayout.recyclerViewToilet.layoutManager = LinearLayoutManager(requireContext())
        binding.RFTioletLayout.recyclerViewToilet.adapter = adapterToilet

        val livingRoomlistViewReq = ToiletRoomInformationReq(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            facilityId = facilityId,
            toiletType=toiletType

        )

        viewModel.getToiletRoomListView(livingRoomlistViewReq)

        showProgressBar()




        viewModel.ToiletRoomListView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {


                    200 ->

                    {
                        hideProgressBar()
                        adapterToilet.updateData(it.wrappedList ?: emptyList())
                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {

                            rfToiletId=x.rfToiletId


                        }



                    }















                    202 ->


                    {

                        adapterToilet.updateData(emptyList())
                        adapterToilet.updateData(mutableListOf())
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "No data available.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    301 -> {
                        adapterToilet.updateData(emptyList())
                        adapterToilet.updateData(mutableListOf())
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()}

                    401 ->  {

                        hideProgressBar()

                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
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
//                openPdfFile(base64)


            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to open PDF", Toast.LENGTH_SHORT).show()
        }
    }

    fun safeText(value: String?): String {
        return if (value.isNullOrBlank() || value.equals("null", ignoreCase = true)) {
            "N/A"
        } else value
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



    fun showProgressBar() {
        if (context != null && isAdded && progress?.isShowing == false) {
            progress?.show()
        }
    }
    //
    fun hideProgressBar() {
        if (progress?.isShowing == true) {
            progress?.dismiss()
        }
    }







}






