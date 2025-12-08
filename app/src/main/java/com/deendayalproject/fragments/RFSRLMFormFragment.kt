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
import android.widget.*
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
import com.deendayalproject.databinding.*
import com.deendayalproject.model.request.*
import com.deendayalproject.model.response.IndoorRFGameResponseDetails
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.toastShort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import kotlin.reflect.KMutableProperty1

class RFSRLMFormFragment : Fragment() {

    private var _binding: RfSrlmFormFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SharedViewModel

    private val progress: androidx.appcompat.app.AlertDialog? by lazy {
        AppUtil.getProgressDialog(context)
    }

    // Image Files
    private var RFQTBasicInfoPdf = ""
    private var RFQTBasicInfoAppointMent = ""
    private var ReceptionAreaPdf = ""
    private var PreparedFoodFile = ""
    private var RFWardenCareFile = ""
    private var RFMaleDoctorFile = ""
    private var RFFemaleDoctorFile = ""
    private var RFWardenCaretakerMaleFile = ""
    private var RFHostelsSeparatedFile = ""
    private var RFSecurityGuardsFile = ""
    private var RFsafeDrinkingeFile = ""
    private var RFfirstAidKitFile = ""
    private var RFfireFightingFile = ""
    private var RFbiometricDeviceFile = ""
    private var RFpowerBackupFile = ""
    private var RFgrievanceRegisterFile = ""

    // Approval States
    private var approvalList = listOf("Approved", "Send for modification")

    private var selectedRFBasicInformationApproval = ""
    private var selectedRFBasicInformationRemarks = ""
    private var selectedInfrastctureDetailsComplainsApproval = ""
    private var selectedInfrastctureDetailsComplainsRemarks = ""
    private var selectedRFLevingAreaInformationApproval = ""
    private var selectedRFLevingAreaInformationRemarks = ""
    private var selectedRFToiletApproval = ""
    private var selectedRFToiletRemarks = ""
    private var selectedRFToiletAdditionalSanctionApproval = ""
    private var selectedRFToiletAdditionalSanctionRemarks = ""
    private var selectedNonAreaInfoApproval = ""
    private var selectedRFNonLivingAreaRemarks = ""
    private var selectedIndoorGameApproval = ""
    private var selectedIndoorGameApprovalRemark = ""
    private var selectedResidintislFacilityApproval = ""
    private var selectedResidintislFacilityApprovalRemark = ""
    private var selectedResidintislSupportFacilityApproval = ""
    private var selectedResidintislSupportFacilityApprovalRemark = ""

    // Data
    private var centerId = ""
    private var sanctionOrder = ""
    private var facilityId = 0
    private var RFQTresFacilityId = ""
    private var rfToiletId = ""

    // Adapters
    private lateinit var livingAreaAdapter: LivingAreaInformationAdapter
    private lateinit var toiletAdapter: RFToiletAdapter
    private lateinit var indoorGameAdapter: IndoorGameRFAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RfSrlmFormFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        centerId = arguments?.getString("centerId").toString()
        sanctionOrder = arguments?.getString("sanctionOrder").toString()
        facilityId = arguments?.getInt("facilityId", 0)!!

        setupAdapters()
        setupImageListeners()
        setupApprovalSections()
        setupFinalSubmit()
        setupBackButton()

        loadBasicInformation()
    }

    private fun setupAdapters() {
        livingAreaAdapter = LivingAreaInformationAdapter(emptyList()) { roomNo ->
            loadRoomDialog(roomNo.roomNo)
        }
        binding.livingareainformationLayout.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.livingareainformationLayout.recyclerView.adapter = livingAreaAdapter

        toiletAdapter = RFToiletAdapter(emptyList()) { id ->
            rfToiletId = id.rfToiletId//id.toString()
            loadToiletDialog()
        }
        binding.RFTioletLayout.recyclerViewToilet.layoutManager = LinearLayoutManager(requireContext())
        binding.RFTioletLayout.recyclerViewToilet.adapter = toiletAdapter

        indoorGameAdapter = IndoorGameRFAdapter(emptyList()) { pdf ->
            showBase64ImageDialog(requireContext(), pdf.indoorGamePdf, "Indoor Game")
        }
        binding.RFIndoorGameLayout.recyclerViewInddorGame.layoutManager = LinearLayoutManager(requireContext())
        binding.RFIndoorGameLayout.recyclerViewInddorGame.adapter = indoorGameAdapter
    }

    private fun setupImageListeners() {
        binding.residentialfacilityqteamInfoLayout.PoliceVerificationStatus.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQTBasicInfoPdf, "Police Verification")
        }
        binding.residentialfacilityqteamInfoLayout.AppointmentLetter.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFQTBasicInfoAppointMent, "Appointment Letter")
        }
        binding.RFNonLivingAreaLayout.ReceptionAreaFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), ReceptionAreaPdf, "Reception Area")
        }
        binding.RFNonLivingAreaLayout.WhetherFoodForFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), PreparedFoodFile, "Prepared Food")
        }
        binding.RFResidentialFacilitiesAvailable.WardenCareFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFWardenCareFile, "Warden Care")
        }
        binding.RFResidentialFacilitiesAvailable.MaleDoctorFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFMaleDoctorFile, "Male Doctor")
        }
        binding.RFResidentialFacilitiesAvailable.FemaleDoctorFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFFemaleDoctorFile, "Female Doctor")
        }
        binding.RFResidentialFacilitiesAvailable.WardenCaretakerMaleFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFWardenCaretakerMaleFile, "Caretaker Male")
        }
        binding.RFResidentialFacilitiesAvailable.HostelsSeparatedFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFHostelsSeparatedFile, "Hostels Separated")
        }
        binding.RFResidentialFacilitiesAvailable.SecurityGuardsFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFSecurityGuardsFile, "Security Guards")
        }
        binding.rfSupportFacilitiesAvailableLayout.SafeDrinikingAavailableFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFsafeDrinkingeFile, "Safe Drinking")
        }
        binding.rfSupportFacilitiesAvailableLayout.FirstAidKitFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFfirstAidKitFile, "First Aid Kit")
        }
        binding.rfSupportFacilitiesAvailableLayout.FireFightingEquipmentrFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFfireFightingFile, "Fire Fighting")
        }
        binding.rfSupportFacilitiesAvailableLayout.BiometricDeviceFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFbiometricDeviceFile, "Biometric Device")
        }
        binding.rfSupportFacilitiesAvailableLayout.ElectricalPowerBackupFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFpowerBackupFile, "Power Backup")
        }
        binding.rfSupportFacilitiesAvailableLayout.GrievanceRegisterFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFgrievanceRegisterFile, "Grievance Register")
        }
    }

    private fun setupApprovalSections() {
        val options = listOf("Approved", "Send for modification")

        // Reusable function that works for BOTH Spinner & AutoCompleteTextView
        fun configureApproval(
            dropdownView: View,                    // Can be Spinner or AutoCompleteTextView
            remarksEt: EditText,
            remarksLabel: TextView? = null,
            nextBtn: View,
            verifiedView: TextView,
            onApproved: () -> Unit
        ) {
            // Set adapter
            if (dropdownView is Spinner || dropdownView is AutoCompleteTextView) {
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, options)
                if (dropdownView is Spinner) {
                    dropdownView.adapter = adapter
                } else {
                    (dropdownView as AutoCompleteTextView).setAdapter(adapter)
                }
            }

            // Listen to selection (works for both)
            val listener = object : AdapterView.OnItemSelectedListener,
                AdapterView.OnItemClickListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) = updateVisibility(true)
                override fun onNothingSelected(parent: AdapterView<*>?) = updateVisibility(false)
                override fun onItemClick(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) = updateVisibility(true)

                private fun updateVisibility(isSelected: Boolean) {
                    val selectedText = when (dropdownView) {
                        is Spinner -> dropdownView.selectedItem?.toString()
                        is AutoCompleteTextView -> dropdownView.text.toString()
                        else -> null
                    }
                    val isModification = selectedText == "Send for modification"
                    remarksEt.visibility = if (isModification) View.VISIBLE else View.GONE
                    remarksLabel?.visibility = if (isModification) View.VISIBLE else View.GONE
                }
            }

            // Apply correct listener
            when (dropdownView) {
                is Spinner -> dropdownView.onItemSelectedListener = listener
                is AutoCompleteTextView -> dropdownView.onItemClickListener = listener
            }

            // Next button click
            nextBtn.setOnClickListener {
                val selectedText = when (dropdownView) {
                    is Spinner -> dropdownView.selectedItem?.toString()
                    is AutoCompleteTextView -> dropdownView.text.toString()
                    else -> ""
                }

                if (selectedText.isNullOrBlank()) {
                    toastShort("Please select approval status")
                    return@setOnClickListener
                }
                if (selectedText == "Send for modification" && remarksEt.text.toString().trim().isEmpty()) {
                    toastShort("Please enter remarks")
                    return@setOnClickListener
                }

                // Save remark
                val remark = remarksEt.text.toString().trim()
                // You can store remark here if needed

                onApproved()
                verifiedView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
                scrollToTop()
            }
        }

        // ALL YOUR 9 SECTIONS — SUPER CLEAN
        configureApproval(
            dropdownView = binding.residentialfacilityqteamInfoLayout.SpinnerTcInfo,
            remarksEt = binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks,
            remarksLabel = binding.residentialfacilityqteamInfoLayout.textViewRFQTInfoRemarks,
            nextBtn = binding.residentialfacilityqteamInfoLayout.btnRFQTInfoNext,
            verifiedView = binding.residentialfacilityqteamInfoLayout.tvTrainInfo
        ) {
            selectedRFBasicInformationApproval = "A"
            selectedRFBasicInformationRemarks = binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks.text.toString().trim()
            showSection(binding.infrastructureDetailsAndCompliancesLayout.root)
            loadInfrastructureCompliances()
        }

        configureApproval(
            dropdownView = binding.infrastructureDetailsAndCompliancesLayout.SpinnerIDC,
            remarksEt = binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks,
            nextBtn = binding.infrastructureDetailsAndCompliancesLayout.btnIDCNext,
            verifiedView = binding.infrastructureDetailsAndCompliancesLayout.tvIDC
        ) {
            selectedInfrastctureDetailsComplainsApproval = "A"
            selectedInfrastctureDetailsComplainsRemarks = binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks.text.toString().trim()
            showSection(binding.livingareainformationLayout.root)
            loadLivingAreaList()
        }

        configureApproval(
            dropdownView = binding.livingareainformationLayout.SpinnerLivingAreaInformation,
            remarksEt = binding.livingareainformationLayout.etLivingAreaInformationRemarks,
            nextBtn = binding.livingareainformationLayout.btnLivingAreaInformationNext,
            verifiedView = binding.livingareainformationLayout.tvLAI
        ) {
            selectedRFLevingAreaInformationApproval = "A"
            selectedRFLevingAreaInformationRemarks = binding.livingareainformationLayout.etLivingAreaInformationRemarks.text.toString().trim()
            showSection(binding.RFTioletLayout.root)
            loadToiletCount()
        }

        configureApproval(
            dropdownView = binding.RFTioletLayout.SpinnerToilet,
            remarksEt = binding.RFTioletLayout.etToiletRemarks,
            nextBtn = binding.RFTioletLayout.btnToiletNext,
            verifiedView = binding.RFTioletLayout.tvToilet
        ) {
            selectedRFToiletApproval = "A"
            selectedRFToiletRemarks = binding.RFTioletLayout.etToiletRemarks.text.toString().trim()
            showSection(binding.RFTioletAdditionalSectionLayout.root)
            loadAdditionalToilet()
        }

        configureApproval(
            dropdownView = binding.RFTioletAdditionalSectionLayout.SpinnerAdditionalSection,
            remarksEt = binding.RFTioletAdditionalSectionLayout.etAdditionalSectionRemarks,
            nextBtn = binding.RFTioletAdditionalSectionLayout.btnAdditionalSectionNext,
            verifiedView = binding.RFTioletAdditionalSectionLayout.tvToiletAdditionalSection
        ) {
            selectedRFToiletAdditionalSanctionApproval = "A"
            selectedRFToiletAdditionalSanctionRemarks = binding.RFTioletAdditionalSectionLayout.etAdditionalSectionRemarks.text.toString().trim()
            showSection(binding.RFNonLivingAreaLayout.root)
            loadNonLivingAreaInfo()
        }

        configureApproval(
            dropdownView = binding.RFNonLivingAreaLayout.SpinnerNonLivingAreaInformation,
            remarksEt = binding.RFNonLivingAreaLayout.etNonLivingAreaInformationRemarks,
            nextBtn = binding.RFNonLivingAreaLayout.btnNonLivingAreaInformationNext,
            verifiedView = binding.RFNonLivingAreaLayout.tvNonLivingAreaInfor
        ) {
            selectedNonAreaInfoApproval = "A"
            selectedRFNonLivingAreaRemarks = binding.RFNonLivingAreaLayout.etNonLivingAreaInformationRemarks.text.toString().trim()
            showSection(binding.RFIndoorGameLayout.root)
            loadIndoorGamesList()
        }

        configureApproval(
            dropdownView = binding.RFIndoorGameLayout.SpinnerIndoorGame,
            remarksEt = binding.RFIndoorGameLayout.etIndoorGameRemarks,
            nextBtn = binding.RFIndoorGameLayout.btnIndoorGameNext,
            verifiedView = binding.RFIndoorGameLayout.tvIndoorGame
        ) {
            selectedIndoorGameApproval = "A"
            selectedIndoorGameApprovalRemark = binding.RFIndoorGameLayout.etIndoorGameRemarks.text.toString().trim()
            showSection(binding.RFResidentialFacilitiesAvailable.root)
            loadResidentialFacilities()
        }

        configureApproval(
            dropdownView = binding.RFResidentialFacilitiesAvailable.SpinnerRFResidentialFacality,
            remarksEt = binding.RFResidentialFacilitiesAvailable.etRFResidentialFacalityRemarks,
            nextBtn = binding.RFResidentialFacilitiesAvailable.btnRFResidentialFacalityNext,
            verifiedView = binding.RFResidentialFacilitiesAvailable.tvRFResidentialFacality
        ) {
            selectedResidintislFacilityApproval = "A"
            selectedResidintislFacilityApprovalRemark = binding.RFResidentialFacilitiesAvailable.etRFResidentialFacalityRemarks.text.toString().trim()
            showSection(binding.rfSupportFacilitiesAvailableLayout.root)
            loadSupportFacilities()
        }

        configureApproval(
            dropdownView = binding.rfSupportFacilitiesAvailableLayout.SpinnerRFResidentialsupportFacality,
            remarksEt = binding.rfSupportFacilitiesAvailableLayout.etRFResidentialsupportFacalityRemarks,
            nextBtn = binding.rfSupportFacilitiesAvailableLayout.btnRFResidentialsupportFacalityNext,
            verifiedView = binding.rfSupportFacilitiesAvailableLayout.tvRFResidentialSupportFacality
        ) {
            selectedResidintislSupportFacilityApproval = "A"
            selectedResidintislSupportFacilityApprovalRemark = binding.rfSupportFacilitiesAvailableLayout.etRFResidentialsupportFacalityRemarks.text.toString().trim()
            binding.btnSubmitFinal.visibility = View.VISIBLE
        }
    }



//    private fun setupApprovalSections() {
//        var commonAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList)
//
//        // Basic Information
//        binding.residentialfacilityqteamInfoLayout.SpinnerTcInfo.adapter = commonAdapter
//        binding.residentialfacilityqteamInfoLayout.SpinnerTcInfo.onItemSelectedListener = approvalItemSelectedListener(
//            binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks,
//            binding.residentialfacilityqteamInfoLayout.textViewRFQTInfoRemarks
//        ) {
//            selectedRFBasicInformationApproval = it
//        }
//        binding.residentialfacilityqteamInfoLayout.btnRFQTInfoNext.setOnClickListener {
//            validateAndProceed(
//                selectedRFBasicInformationApproval,
//                selectedRFBasicInformationRemarks,
//                binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks
//            ) {
//                hideSection(binding.residentialfacilityqteamInfoLayout.root)
//                showSection(binding.infrastructureDetailsAndCompliancesLayout.root)
//                loadInfrastructureCompliances()
//                binding.residentialfacilityqteamInfoLayout.tvTrainInfo.setCompoundDrawablesWithIntrinsicBounds(
//                    R.drawable.baseline_info_24, 0, R.drawable.ic_verified, 0
//                )
//            }
//        }
//
//        // Infrastructure
//        binding.infrastructureDetailsAndCompliancesLayout.SpinnerIDC.adapter = commonAdapter
//        binding.infrastructureDetailsAndCompliancesLayout.SpinnerIDC.onItemSelectedListener = approvalItemSelectedListener(
//            binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks,
//            binding.infrastructureDetailsAndCompliancesLayout.tvSelectApprovalIDC
//        ) {
//            selectedInfrastctureDetailsComplainsApproval = it
//        }
//        binding.infrastructureDetailsAndCompliancesLayout.btnIDCNext.setOnClickListener {
//            validateAndProceed(
//                selectedInfrastctureDetailsComplainsApproval,
//                selectedInfrastctureDetailsComplainsRemarks,
//                binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks
//            ) {
//                hideSection(binding.infrastructureDetailsAndCompliancesLayout.root)
//                showSection(binding.livingareainformationLayout.root)
//                loadLivingAreaList()
//                binding.infrastructureDetailsAndCompliancesLayout.tvIDC.setCompoundDrawablesWithIntrinsicBounds(
//                    R.drawable.infrastructure, 0, R.drawable.ic_verified, 0
//                )
//            }
//        }
//
//        // Living Area
//        binding.livingareainformationLayout.SpinnerLivingAreaInformation.adapter = commonAdapter
//        binding.livingareainformationLayout.SpinnerLivingAreaInformation.onItemSelectedListener = approvalItemSelectedListener(
//            binding.livingareainformationLayout.etLivingAreaInformationRemarks,
//            binding.livingareainformationLayout.LivingAreaInformationRemarks
//        ) {
//            selectedRFLevingAreaInformationApproval = it
//        }
//        binding.livingareainformationLayout.btnLivingAreaInformationNext.setOnClickListener {
//            validateAndProceed(
//                selectedRFLevingAreaInformationApproval,
//                selectedRFLevingAreaInformationRemarks,
//                binding.livingareainformationLayout.etLivingAreaInformationRemarks
//            ) {
//                hideSection(binding.livingareainformationLayout.root)
//                showSection(binding.RFTioletLayout.root)
//                loadToiletCount()
//                binding.livingareainformationLayout.tvLAI.setCompoundDrawablesWithIntrinsicBounds(
//                    R.drawable.ic_property, 0, R.drawable.ic_verified, 0
//                )
//            }
//        }
//
//        // Toilet
//        binding.RFTioletLayout.SpinnerToilet.adapter = commonAdapter
//        binding.RFTioletLayout.SpinnerToilet.onItemSelectedListener = approvalItemSelectedListener(
//            binding.RFTioletLayout.etToiletRemarks,
//            binding.RFTioletLayout.LivingToiletRemarks
//        ) {
//            selectedRFToiletApproval = it
//        }
//        binding.RFTioletLayout.btnToiletNext.setOnClickListener {
//            validateAndProceed(
//                selectedRFToiletApproval,
//                selectedRFToiletRemarks,
//                binding.RFTioletLayout.etToiletRemarks
//            ) {
//                hideSection(binding.RFTioletLayout.root)
//                showSection(binding.RFTioletAdditionalSectionLayout.root)
//                loadAdditionalToilet()
//                binding.RFTioletLayout.tvToilet.setCompoundDrawablesWithIntrinsicBounds(
//                    R.drawable.toilet, 0, R.drawable.ic_verified, 0
//                )
//            }
//        }
//
//        // Additional Toilet
//        binding.RFTioletAdditionalSectionLayout.SpinnerAdditionalSection.adapter = commonAdapter
//        binding.RFTioletAdditionalSectionLayout.SpinnerAdditionalSection.onItemSelectedListener = approvalItemSelectedListener(
//            binding.RFTioletAdditionalSectionLayout.etAdditionalSectionRemarks,
//            binding.RFTioletAdditionalSectionLayout.textViewAdditionalSectionRemarks
//        ) {
//            selectedRFToiletAdditionalSanctionApproval = it
//        }
//        binding.RFTioletAdditionalSectionLayout.btnAdditionalSectionNext.setOnClickListener {
//            validateAndProceed(
//                selectedRFToiletAdditionalSanctionApproval,
//                selectedRFToiletAdditionalSanctionRemarks,
//                binding.RFTioletAdditionalSectionLayout.etAdditionalSectionRemarks
//            ) {
//                hideSection(binding.RFTioletAdditionalSectionLayout.root)
//                showSection(binding.RFNonLivingAreaLayout.root)
//                loadNonLivingAreaInfo()
//                binding.RFTioletAdditionalSectionLayout.tvToiletAdditionalSection.setCompoundDrawablesWithIntrinsicBounds(
//                    R.drawable.toilet, 0, R.drawable.ic_verified, 0
//                )
//            }
//        }
//
//        // Non Living
//        binding.RFNonLivingAreaLayout.SpinnerNonLivingAreaInformation.adapter = commonAdapter
//        binding.RFNonLivingAreaLayout.SpinnerNonLivingAreaInformation.onItemSelectedListener = approvalItemSelectedListener(
//            binding.RFNonLivingAreaLayout.etNonLivingAreaInformationRemarks,
//            binding.RFNonLivingAreaLayout.tvNonLivingAreaInformationRemarks
//        ) {
//            selectedNonAreaInfoApproval = it
//        }
//        binding.RFNonLivingAreaLayout.btnNonLivingAreaInformationNext.setOnClickListener {
//            validateAndProceed(
//                selectedNonAreaInfoApproval,
//                selectedRFNonLivingAreaRemarks,
//                binding.RFNonLivingAreaLayout.etNonLivingAreaInformationRemarks
//            ) {
//                hideSection(binding.RFNonLivingAreaLayout.root)
//                showSection(binding.RFIndoorGameLayout.root)
//                loadIndoorGamesList()
//                binding.RFNonLivingAreaLayout.tvNonLivingAreaInfor.setCompoundDrawablesWithIntrinsicBounds(
//                    R.drawable.ic_class, 0, R.drawable.ic_verified, 0
//                )
//            }
//        }
//
//        // Indoor Game
//        binding.RFIndoorGameLayout.SpinnerIndoorGame.adapter = commonAdapter
//        binding.RFIndoorGameLayout.SpinnerIndoorGame.onItemSelectedListener = approvalItemSelectedListener(
//            binding.RFIndoorGameLayout.etIndoorGameRemarks,
//            binding.RFIndoorGameLayout.IndoorGameRemarks
//        ) {
//            selectedIndoorGameApproval = it
//        }
//        binding.RFIndoorGameLayout.btnIndoorGameNext.setOnClickListener {
//            validateAndProceed(
//                selectedIndoorGameApproval,
//                selectedIndoorGameApprovalRemark,
//                binding.RFIndoorGameLayout.etIndoorGameRemarks
//            ) {
//                hideSection(binding.RFIndoorGameLayout.root)
//                showSection(binding.RFResidentialFacilitiesAvailable.root)
//                loadResidentialFacilities()
//                binding.RFIndoorGameLayout.tvIndoorGame.setCompoundDrawablesWithIntrinsicBounds(
//                    R.drawable.tabletennis, 0, R.drawable.ic_verified, 0
//                )
//            }
//        }
//
//        // Residential Facilities
//        binding.RFResidentialFacilitiesAvailable.SpinnerRFResidentialFacality.adapter = commonAdapter
//        binding.RFResidentialFacilitiesAvailable.SpinnerRFResidentialFacality.onItemSelectedListener = approvalItemSelectedListener(
//            binding.RFResidentialFacilitiesAvailable.etRFResidentialFacalityRemarks,
//            binding.RFResidentialFacilitiesAvailable.tvRFResidentialFacalityRemarks
//        ) {
//            selectedResidintislFacilityApproval = it
//        }
//        binding.RFResidentialFacilitiesAvailable.btnRFResidentialFacalityNext.setOnClickListener {
//            validateAndProceed(
//                selectedResidintislFacilityApproval,
//                selectedResidintislFacilityApprovalRemark,
//                binding.RFResidentialFacilitiesAvailable.etRFResidentialFacalityRemarks
//            ) {
//                hideSection(binding.RFResidentialFacilitiesAvailable.root)
//                showSection(binding.rfSupportFacilitiesAvailableLayout.root)
//                loadSupportFacilities()
//                binding.RFResidentialFacilitiesAvailable.tvRFResidentialFacality.setCompoundDrawablesWithIntrinsicBounds(
//                    R.drawable.ic_equipment, 0, R.drawable.ic_verified, 0
//                )
//            }
//        }
//
//        // Support Facilities
//        binding.rfSupportFacilitiesAvailableLayout.SpinnerRFResidentialsupportFacality.adapter = commonAdapter
//        binding.rfSupportFacilitiesAvailableLayout.SpinnerRFResidentialsupportFacality.onItemSelectedListener = approvalItemSelectedListener(
//            binding.rfSupportFacilitiesAvailableLayout.etRFResidentialsupportFacalityRemarks,
//            binding.rfSupportFacilitiesAvailableLayout.tvRFResidentialsupportFacalityRemarks
//        ) {
//            selectedResidintislSupportFacilityApproval = it
//        }
//        binding.rfSupportFacilitiesAvailableLayout.btnRFResidentialsupportFacalityNext.setOnClickListener {
//            validateAndProceed(
//                selectedResidintislSupportFacilityApproval,
//                selectedResidintislSupportFacilityApprovalRemark,
//                binding.rfSupportFacilitiesAvailableLayout.etRFResidentialsupportFacalityRemarks
//            ) {
//                binding.btnSubmitFinal.visibility = View.VISIBLE
//                binding.rfSupportFacilitiesAvailableLayout.tvRFResidentialSupportFacality.setCompoundDrawablesWithIntrinsicBounds(
//                    R.drawable.ic_signage, 0, R.drawable.ic_verified, 0
//                )
//            }
//        }
//    }

//    private fun approvalItemSelectedListener(
//        remarksEt: EditText,
//        remarksTv: TextView,
//        setter: (String) -> Unit
//    ): AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            val selected = parent?.getItemAtPosition(position).toString()
//            val isModification = selected == "Send for modification"
//            remarksEt.visibility = if (isModification) View.VISIBLE else View.GONE
//            remarksTv.visibility = if (isModification) View.VISIBLE else View.GONE
//            setter(if (isModification) "M" else "A")
//        }
//
//        override fun onNothingSelected(parent: AdapterView<*>?) {}
//    }

//    private fun validateAndProceed(
//        approval: String,
//        remark: String,
//        remarksEt: EditText,
//        onProceed: () -> Unit
//    ) {
//        if (approval.isEmpty()) {
//            Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (approval == "M") {
//            val r = remarksEt.text.toString().trim()
//            if (r.isEmpty()) {
//                Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
//                return
//            }
//            // Update remark
//        }
//        onProceed()
//        scrollToTop()
//    }

    private fun hideSection(view: View) {
        view.visibility = View.GONE
    }

    private fun showSection(view: View) {
        view.visibility = View.VISIBLE
    }

    private fun scrollToTop() {
        binding.scroll.post { binding.scroll.smoothScrollTo(0, 0) }
    }

    private fun setupFinalSubmit() {
        binding.btnSubmitFinal.setOnClickListener {
            val request = RFQteamVerificationRequest(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                trainingCentre = centerId.toInt(),
                sanctionOrder = sanctionOrder,
                imeiNo = AppUtil.getAndroidId(requireContext()),
                basicInfoStatus = selectedRFBasicInformationApproval,
                basicInfoRemark = selectedRFBasicInformationRemarks,
                infraComplianceStatus = selectedInfrastctureDetailsComplainsApproval,
                infraComplianceRemark = selectedInfrastctureDetailsComplainsRemarks,
                livingAreaInfoStatus = selectedRFLevingAreaInformationApproval,
                livingAreaInfoRemark = selectedRFLevingAreaInformationRemarks,
                toiletStatus = selectedRFToiletApproval,
                toiletRemark = selectedRFToiletRemarks,
                nonLivingAreaStatus = selectedNonAreaInfoApproval,
                nonLivingAreaRemark = selectedRFNonLivingAreaRemarks,
                indoorGameStatus = selectedIndoorGameApproval,
                indoorGameRemark = selectedIndoorGameApprovalRemark,
                rfAvailableStatus = selectedResidintislFacilityApproval,
                rfAvailableRemark = selectedResidintislFacilityApprovalRemark,
                supportFacilityAvailableStatus = selectedResidintislSupportFacilityApproval,
                supportFacilityAvailableRemark = selectedResidintislSupportFacilityApprovalRemark,
                addToiletStatus = selectedRFToiletAdditionalSanctionApproval,
                addToiletRemark = selectedRFToiletAdditionalSanctionRemarks,
                facilityId = facilityId
            )
            viewModel.getFinalSubmitinsertRFinsertRFSrlmVerificationRequestData(request)
            observeFinalSubmit()
            showProgressBar()
        }
    }

    private fun observeFinalSubmit() {
        viewModel.insertRFSrlmVerification.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        val hasMod = listOf(selectedRFBasicInformationApproval, selectedInfrastctureDetailsComplainsApproval, selectedRFLevingAreaInformationApproval, selectedRFToiletApproval, selectedNonAreaInfoApproval, selectedIndoorGameApproval, selectedResidintislFacilityApproval, selectedResidintislSupportFacilityApproval).any { it == "M" }
                        Toast.makeText(requireContext(), if (hasMod) "Send to Operation Team Successfully!!" else "Saved Successfully!!", Toast.LENGTH_LONG).show()
                        findNavController().navigateUp()
                    }
                    202 -> Toast.makeText(requireContext(), it.responseDesc, Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
    }

    private fun loadBasicInformation() {
        val request = RfCommonReq(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            facilityId = facilityId
        )
        viewModel.getRfBasicInformationrInfo(request)
        observeBasicInfo()
        showProgressBar()
    }

    private fun observeBasicInfo() {
        viewModel.ResidentialFacilityQTeam.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        it.wrappedList.firstOrNull()?.let { data ->
                            with(binding.residentialfacilityqteamInfoLayout) {
                                ResidentialFacilityName.text = safeText(data.residentialFacilityName)
                                ResidentialFacilityType.text = safeText(data.residentialType)
                                HouseNo.text = safeText(data.houseNo)
                                Street.text = safeText(data.streetNo1)
                                Landmark.text = safeText(data.landmark)
                                StateUtTc.text = safeText(data.stateName)
                                DistrictTc.text = safeText(data.districtName)
                                Block.text = safeText(data.blockName)
                                GramPanchayat.text = safeText(data.gpName)
                                VillageWardNo.text = safeText(data.villageName)
                                PoliceStation.text = safeText(data.policeStation)
                                LatitudeLongitude.text = safeText(data.latitude)
                                Pincode.text = safeText(data.pincode)
                                WardenMobileNo.text = safeText(data.geoAddress)
                                Mobile.text = safeText(data.mobile)
                                RFPNoWSC.text = safeText(data.residentialFacilitiesPhNo)
                                Email.text = safeText(data.email)
                                TypeofArea.text = safeText(data.typeOfArea)
                                categoryOfTCLocaXYZanyOtherArea.text = safeText(data.categoryOfTc)
                                ApproximateDistanceFrom.text = safeText(data.distBusStand)
                                DistanceFromTheTrainingCenter.text = safeText(data.distFromTc)
                                AvailabilityOfPick.text = safeText(data.distRailStand)
                                DistanceFromRailwayStand.text = safeText(data.distRailStand)
                                DistanceFromAutoTraining.text = safeText(data.distAutoStand)
                                WadrenName.text = safeText(data.wardName)
                                WardenGender.text = safeText(data.wardgender)
                                WardenAddress.text = safeText(data.wardAddress)
                                WardenEmployeeId.text = safeText(data.wardEmpId)
                                WardenEmailId.text = safeText(data.wardEmail)
                                WardenMobileNo.text = safeText(data.wardMobile)
                            }
                            RFQTresFacilityId = data.resFacilityId.toString()
                            RFQTBasicInfoPdf = data.policeVerfictnImage.toString()
                            RFQTBasicInfoAppointMent = data.empLetterImage.toString()
                        }
                    }
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadInfrastructureCompliances() {
        val request = CompliancesRFQTReq(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            facilityId = RFQTresFacilityId,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            tcId = centerId,
            sanctionOrder = sanctionOrder
        )
        viewModel.getCompliancesRFQTReqRFQT(request)
        observeInfrastructureCompliances()
        showProgressBar()
    }

    private fun observeInfrastructureCompliances() {
        viewModel.CompliancesRFQTReqRFQT.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        it.wrappedList.firstOrNull()?.let { data ->
                            with(binding.infrastructureDetailsAndCompliancesLayout) {
                                onwershipOfBulding.text = data.ownership
                                areaOfTheBuilding.text = data.buildingArea
                                HostelNameBoard.text = data.hostelNameBoard
                                BasicInformationBoard.text = data.basicInformationBoard
                                SecuringWiresDone.text = data.securingWiresDone
                                RoofofBulding.text = data.roof
                                WhetherItIsStructurally.text = data.plastring
                                visibleSignsOfLeakages.text = data.leakage
                                ConformanceToDduGky.text = data.conformanceDdu
                                ProtectionOfStairs.text = data.protectionStairs
                                CirculatingArea.text = data.circulatingArea
                                Corridor.text = data.corridor
                                SwitchBoardsAndPanelBoards.text = data.switchBoardsPanelBoards
                                StudentEntitlement.text = data.studentEntitlementBoard
                                ContactDetailOfImportantPeople.text = data.contactDetailImportantPeople
                                FoodSpecificationBoard.text = data.foodSpecificationBoard
                                Area.text = data.openSpaceArea

                                OnwershipOfBuldingFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.selfDeclaration, "Ownership") }
                                BuildingAreaSQFPlanFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.buildingPlanFile, "Building Plan") }
                                RoofLavelFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.buildingPhotosFile, "Roof") }
                                WhetherItIsStructurallyFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.wallPhotosFile, "Walls") }
                                VisibleSignsLeakagesFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.leakagesProofFile, "Leakages") }
                                ProtectionOfStairsFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.protectionStairsProofFile, "Stairs") }
                                HostelNameBoardFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.hostelNameBoardProofFile, "Name Board") }
                                StudentEntitlementFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.studentEntitlementBoardProofFile, "Entitlement") }
                                ContactDetailOfImportantPeopleFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.contactDetailImportantPeopleproofFile, "Contact") }
                                SpecificationBoardFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.foodSpecificationBoardFile, "Food Spec") }
                                BasicInformationBoardFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.basicInformationBoardproofFile, "Basic Info") }
                                SecuringWiresDoneFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.securingWiresDoneProofFile, "Wires") }
                                CorridorFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.corridorProofFile, "Corridor") }
                                circulatingAreaProofFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.circulatingAreaProofFile, "Circulating Area") }
                                ConformanceToDduGkyFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.conformanceDduProofFile, "Conformance") }
                                SwitchBoardsAndPanelBoardsFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.switchBoardsPanelBoardsProofFile, "Switch Boards") }
                            }
                        }
                    }
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadLivingAreaList() {
        val request = LivingRoomListViewRQ(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            facilityId = facilityId
        )
        viewModel.getlivingRoomListView(request)
        observeLivingAreaList()
        showProgressBar()
    }

    private fun observeLivingAreaList() {
        viewModel.livingRoomListView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> livingAreaAdapter.updateData(it.wrappedList ?: emptyList())
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadRoomDialog(roomNo: Int) {
        val request = RfLivingAreaInformationRQ(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            roomNo = roomNo,
            facilityId = facilityId
        )
        viewModel.getRfLivingAreaInformation(request)
        observeRoomDialog()
        showProgressBar()
    }

    private fun observeRoomDialog() {
        viewModel.fLivingAreaInformation.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        it.wrappedList.firstOrNull()?.let { data ->
                            val dialogBinding = RoominformationPopdialogBinding.inflate(layoutInflater)
                            AlertDialog.Builder(requireContext()).setView(dialogBinding.root).show()
                            dialogBinding.laiTypeOfRoof.text = safeText(data.roofType)
                            dialogBinding.laiFalseCelling.text = safeText(data.falseCeiling)
                            dialogBinding.laiHeightofCelling.text = safeText(data.ceilingHeight.toString())
                            val noOfStudentPermitted = data.windowArea!!.toDouble() / 25.0
                            dialogBinding.NoOfStudentPermitted.text = noOfStudentPermitted.toString()
                            dialogBinding.laiLength.text = safeText(data.length.toString())
                            dialogBinding.laiWidth.text = safeText(data.width.toString())
                            dialogBinding.laiArea.text = safeText(data.area.toString())
                            dialogBinding.laiwindowsArea.text = safeText(data.windowArea.toString())
                            dialogBinding.laiCotInNo.text = safeText(data.cot.toString())
                            dialogBinding.laiMattersInNo.text = safeText(data.mattress.toString())
                            dialogBinding.laiBedSheetInNo.text = safeText(data.bedSheet.toString())
                            dialogBinding.laiAirCondtion.text = safeText(data.airCondtion.toString())
                            dialogBinding.laiLights.text = safeText(data.lights.toString())
                            dialogBinding.laiStorage.text = safeText(data.storage.toString())
                            dialogBinding.LiaBasicInformationBoard.text = safeText(data.infoBoard.toString())

                            dialogBinding.laiTypeOfRoofFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.roofTypePdf, "Roof Type") }
                            dialogBinding.laiFalseCellingFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.falseCeilingPdf, "False Ceiling") }
                            dialogBinding.laiHeightofCellingFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.ceilingHeightPdf, "Ceiling Height") }
                            dialogBinding.laiwindowsAreaFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.windowAreaPdf, "Window Area") }
                            dialogBinding.laiCotInNoFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.cotPdf, "Cot") }
                            dialogBinding.laiMattersInNoFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.mattressPdf, "Mattress") }
                            dialogBinding.laiBedSheetInNoFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.bedSheetPdf, "Bed Sheet") }
                            dialogBinding.laiAirCondtionFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.airConditionPdf, "Air Condition") }
                            dialogBinding.laiLightsFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.lightPdf, "Lights") }
                            dialogBinding.laiStorageFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.storagePdf, "Storage") }

                            dialogBinding.backButton.setOnClickListener { (context as AlertDialog).dismiss() }
                        }
                    }
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadToiletCount() {
        val request = ToiletCountListReq(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            facilityId = facilityId.toString()
        )
        viewModel.getToiletCountList(request)
        observeToiletCount()
        showProgressBar()
    }

    private fun observeToiletCount() {
        viewModel.ToiletCountListView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        it.wrappedList.firstOrNull()?.let { data ->
                            with(binding.RFTioletLayout) {
                                TvToilet.text = data.toiletCount
                                TvBathroom.text = data.washroomCount
                                TvToiletBathroom.text = data.toiletWashroomCount
                                TvToilet.paintFlags = TvToilet.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                                TvBathroom.paintFlags = TvBathroom.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                                TvToiletBathroom.paintFlags = TvToiletBathroom.paintFlags or Paint.UNDERLINE_TEXT_FLAG

                                tvToilet.setOnClickListener {
                                    LinLayoutCardView.visibility = View.VISIBLE
                                    LinLayoutRecyclerView.visibility = View.GONE
                                    tvToilet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.toilet, 0, 0, 0)
                                }

                                linLayoutToilet.setOnClickListener {
                                    if (data.toiletCount != "0") {
                                        ListViewToilet("Toilet")
                                        tvToilet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.back_black, 0, 0, 0)
                                        LinLayoutCardView.visibility = View.GONE
                                        LinLayoutRecyclerView.visibility = View.VISIBLE
                                    }
                                }

                                LinLayoutBathroom.setOnClickListener {
                                    if (data.washroomCount != "0") {
                                        ListViewToilet("Washroom")
                                        tvToilet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.back_black, 0, 0, 0)
                                        LinLayoutCardView.visibility = View.GONE
                                        LinLayoutRecyclerView.visibility = View.VISIBLE
                                    }
                                }

                                LinLayoutToiletAndBathroom.setOnClickListener {
                                    if (data.toiletWashroomCount != "0") {
                                        ListViewToilet("Toilet Cum Washroom")
                                        tvToilet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.back_black, 0, 0, 0)
                                        LinLayoutCardView.visibility = View.GONE
                                        LinLayoutRecyclerView.visibility = View.VISIBLE
                                    }
                                }
                            }
                        }
                    }
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ListViewToilet(toiletType: String) {
        val request = ToiletRoomInformationReq(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            facilityId = facilityId,
            toiletType = toiletType
        )
        viewModel.getToiletRoomListView(request)
        observeToiletList()
        showProgressBar()
    }

    private fun observeToiletList() {
        viewModel.ToiletRoomListView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        toiletAdapter.updateData(it.wrappedList ?: emptyList())
                        it.wrappedList.firstOrNull()?.let { data -> rfToiletId = data.rfToiletId }
                    }
                    202 -> {
                        toiletAdapter.updateData(emptyList())
                        Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    }
                    301 -> {
                        toiletAdapter.updateData(emptyList())
                        Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    }
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadToiletDialog() {
        val request = ToiletRoomReq(
            appVersion = BuildConfig.VERSION_NAME,
            rfToiletId = rfToiletId
        )
        viewModel.getRfToiletRoomInformation(request)
        observeToiletDialog()
    }

    private fun observeToiletDialog() {
        viewModel.ToiletRoomInformationView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        it.wrappedList.firstOrNull()?.let { data ->
                            val dialogBinding = TriPopdialogBinding.inflate(layoutInflater)
                            AlertDialog.Builder(requireContext()).setView(dialogBinding.root).show()
                            dialogBinding.triTypeOfFlooring.text = safeText(data.flooring)
                            dialogBinding.ConnectionToRunningWater.text = safeText(data.runningWater)
                            dialogBinding.ToiletType.text = safeText(data.type.toString())
                            dialogBinding.TriLights.text = safeText(data.lights.toString())

                            dialogBinding.laiTypeOfFlooringFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.floorPdf, "Floor") }
                            dialogBinding.TriLightsFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.lightPdf, "Lights") }
                            dialogBinding.ConnectionToRunningWaterFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.runningWaterFile, "Running Water") }

                            dialogBinding.backButton.setOnClickListener { (context as AlertDialog).dismiss() }
                        }
                    }
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadAdditionalToilet() {
        val request = GetUrinalWashReq(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId,
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            facilityId = facilityId.toString()
        )
        viewModel.getToiletWashbasinDetails(request)
        observeAdditionalToilet()
        showProgressBar()
    }

    private fun observeAdditionalToilet() {
        viewModel.getToiletWashbasinDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        it.wrappedList.firstOrNull()?.let { data ->
                            with(binding.RFTioletAdditionalSectionLayout) {
                                UrinalAdditionalSection.setText(data.urinal)
                                WashbasinsAdditionalSection.setText(data.washbasin)
                                OverHeadTankAdditionalSection.setText(data.overheadTank)

                                ToiletAdditionalSectionFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.urinalFile, "Urinal") }
                                WashbasinsAdditionalSectionFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.washbasinFile, "Washbasin") }
                                OverHeadTankAdditionalSectionFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.overheadTankFile, "Overhead Tank") }
                            }
                        }
                    }
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadNonLivingAreaInfo() {
        val request = LivingRoomListViewRQ(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            facilityId = facilityId
        )
        viewModel.getRfNonLivingAreaInformation(request)
        observeNonLivingArea()
        showProgressBar()
    }

    private fun observeNonLivingArea() {
        viewModel.NonAreaInformationRoom.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        it.wrappedList.firstOrNull()?.let { data ->
                            with(binding.RFNonLivingAreaLayout) {
                                WhetherFoodFor.text = safeText(data.preparedFood)
                                reTheDiningAndRecreationAreaSeparate.text = safeText(data.separateAreas)
                                NoOfStoolsChairsBenches.text = safeText(data.noOfSeats)
                                WashArea.text = safeText(data.washArea)
                                WhetherTv.text = safeText(data.noOfSeats)
                                DiningLength.text = safeText(data.diningLength)
                                DiningWidth.text = safeText(data.diningWidth)
                                DiningArea.text = safeText(data.diningArea)
                                RecreationLength.text = safeText(data.recreationLength)
                                RecreationWidth.text = safeText(data.recreationWidth)
                                RecreationArea.text = safeText(data.recreationArea)
                                ReceptionArea.text = safeText(data.receptionArea)
                                LengthRecreationAndDining.text = safeText(data.diningLength)
                                AreaRecreationAndDining.text = safeText(data.diningArea)
                                WidthRecreationAndDining.text = safeText(data.diningWidth)

                                recreationFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.diningAreaFile, "Recreation") }
                                recreationAndDiningFile.setOnClickListener { showBase64ImageDialog(requireContext(), data.diningRecreationAreaFile, "Dining Recreation") }

                                PreparedFoodFile = data.preprationFoodPdf
                                ReceptionAreaPdf = data.receptionAreaPdf.toString()

                                if (data.separateAreas == "Yes") {
                                    recreationFile.visibility = View.VISIBLE
                                    LinLayOutrecreationAndDiningNo.visibility = View.VISIBLE
                                    recreationAndDiningYes.visibility = View.GONE
                                    recreationAndDiningFile.visibility = View.GONE
                                } else {
                                    recreationAndDiningFile.visibility = View.VISIBLE
                                    recreationAndDiningYes.visibility = View.VISIBLE
                                    LinLayOutrecreationAndDiningNo.visibility = View.GONE
                                    recreationFile.visibility = View.GONE
                                }
                            }
                        }
                    }
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadIndoorGamesList() {
        val request = RFGameRequest(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            facilityId = facilityId
        )
        viewModel.getRfIndoorGameDetails(request)
        observeIndoorGames()
        showProgressBar()
    }

    private fun observeIndoorGames() {
        viewModel.RfIndoorGameDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> indoorGameAdapter.updateData(it.wrappedList ?: emptyList())
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadResidentialFacilities() {
        val request = RfCommonReq(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            facilityId = facilityId
        )
        viewModel.getResidentialFacilitiesAvailable(request)
        observeResidentialFacilities()
        showProgressBar()
    }

    private fun observeResidentialFacilities() {
        viewModel.RFResidentialFacilitiesAvailable.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        it.wrappedList.firstOrNull()?.let { data ->
                            with(binding.RFResidentialFacilitiesAvailable) {
                                WardenCare.text = data.wardenCaretakerFemale
                                MaleDoctor.text = data.maleDoctor
                                WardenCaretakerMale.text = data.wardenCaretakerMale
                                HostelsSeparated.text = data.hostelsSeparated
                                FemaleDoctor.text = data.femaleDoctor
                                SecurityGuards.text = data.securityGuards

                                RFWardenCareFile = data.wardenCaretakerFemalePdf
                                RFMaleDoctorFile = data.maleDoctorPdf
                                RFFemaleDoctorFile = data.femaleDoctorPdf
                                RFWardenCaretakerMaleFile = data.wardenCaretakerMalePdf
                                RFHostelsSeparatedFile = data.hostelsSeparatedPdf
                                RFSecurityGuardsFile = data.securityGuardsPdf
                            }
                        }
                    }
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadSupportFacilities() {
        val request = RFGameRequest(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            facilityId = facilityId
        )
        viewModel.getRFSupportFacilitiesAvailable(request)
        observeSupportFacilities()
        showProgressBar()
    }

    private fun observeSupportFacilities() {
        viewModel.RFSupportFacilitiesAvailable.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        it.wrappedList.firstOrNull()?.let { data ->
                            with(binding.rfSupportFacilitiesAvailableLayout) {
                                SafeDrinikingAavailable.text = data.safeDrinking
                                FirstAidKit.text = data.firstAidKit
                                FireFightingEquipmentr.text = data.fireFighting
                                BiometricDevice.text = data.biometricDevice
                                ElectricalPowerBackup.text = data.powerBackup
                                GrievanceRegister.text = data.grievanceRegister

                                RFsafeDrinkingeFile = data.safeDrinkingPdf
                                RFfirstAidKitFile = data.firstAidKitPdf
                                RFfireFightingFile = data.fireFightingPdf
                                RFbiometricDeviceFile = data.biometricDevicePdf
                                RFpowerBackupFile = data.powerBackupPdf
                                RFgrievanceRegisterFile = data.grievanceRegisterPdf
                            }
                        }
                    }
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openBase64Pdf(context: Context, base64: String) {
        try {
            val cleanBase64 = base64.replace("data:application/pdf;base64,", "").trim()
            val pdfBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            if (pdfBytes.isEmpty() || !String(pdfBytes.copyOfRange(0, 4)).startsWith("%PDF")) {
                Toast.makeText(context, "Invalid PDF data", Toast.LENGTH_SHORT).show()
                return
            }
            val pdfFile = File.createTempFile("temp_", ".pdf", context.cacheDir)
            pdfFile.outputStream().use { it.write(pdfBytes) }
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", pdfFile)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(Intent.createChooser(intent, "Open PDF"))
            } else {
                Toast.makeText(context, "No PDF viewer installed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to open PDF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun safeText(value: String?): String {
        return if (value.isNullOrBlank() || value.equals("null", ignoreCase = true)) {
            "N/A"
        } else value
    }

    private fun showBase64ImageDialog(context: Context, base64ImageString: String?, title: String = "Image") {
        val imageView = ImageView(context)
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
        } else null

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
        } else {
            imageView.setImageResource(R.drawable.no_image)
        }

        imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.setPadding(20, 20, 20, 20)

        AlertDialog.Builder(context)
            .setTitle(title)
            .setView(imageView)
            .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showProgressBar() {
        progress?.show()
    }

    private fun hideProgressBar() {
        progress?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}