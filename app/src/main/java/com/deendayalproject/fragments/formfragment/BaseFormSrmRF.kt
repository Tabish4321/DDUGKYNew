package com.deendayalproject.fragments.formfragment

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
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
import com.deendayalproject.base.ImagePreviewDialogFragment
import com.deendayalproject.databinding.RfSrlmFormFragmentBinding
import com.deendayalproject.databinding.RoominformationPopdialogBinding
import com.deendayalproject.databinding.TriPopdialogBinding
import com.deendayalproject.model.request.CompliancesRFQTReq
import com.deendayalproject.model.request.GetUrinalWashReq
import com.deendayalproject.model.request.LivingRoomListViewRQ
import com.deendayalproject.model.request.RFGameRequest
import com.deendayalproject.model.request.RfCommonReq
import com.deendayalproject.model.request.RfLivingAreaInformationRQ
import com.deendayalproject.model.request.ToiletCountListReq
import com.deendayalproject.model.request.ToiletRoomInformationReq
import com.deendayalproject.model.request.ToiletRoomReq
import com.deendayalproject.model.response.CountList
import com.deendayalproject.model.response.LivingAreaInformation
import com.deendayalproject.model.response.ToiletRoomInformationDataResponse
import com.deendayalproject.util.AppUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

open class BaseFormSrmRF  : Fragment() {

    private var _binding: RfSrlmFormFragmentBinding? = null
    protected val binding get() = _binding!!
    protected lateinit var viewModel: SharedViewModel
    protected val progress by lazy { AppUtil.getProgressDialog(context) }
    private val approvalList = listOf("Approved", "Send for modification")
    protected var centerId = ""
    protected var sanctionOrder = ""
    protected var facilityId = 0
    protected var centerName = ""
    protected var RFQTresFacilityId = ""

    // File variables
    private var RFQTBasicInfoPdf = ""
    private var RFQTBasicInfoAppointMent = ""
    private var PreparedFoodFile = ""
    private var ReceptionAreaPdf = ""
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

    // Approval status variables
    protected var selectedRFBasicInformationApproval = ""
    protected var selectedInfrastctureDetailsComplainsApproval = ""
    protected var selectedRFLevingAreaInformationApproval = ""
    protected var selectedRFToiletApproval = ""
    protected var selectedRFToiletAdditionalSanctionApproval = ""
    protected var selectedNonAreaInfoApproval = ""
    protected var selectedIndoorGameApproval = ""
    protected var selectedResidintislFacilityApproval = ""
    protected var selectedResidintislSupportFacilityApproval = ""

    // Remarks variables
    protected var selectedRFBasicInformationRemarks = ""
    protected var selectedInfrastctureDetailsComplainsRemarks = ""
    protected var selectedRFLevingAreaInformationRemarks = ""
    protected var selectedRFToiletRemarks = ""
    protected var selectedRFToiletAdditionalSanctionRemarks = ""
    protected var selectedRFNonLivingAreaRemarks = ""
    protected var selectedIndoorGameApprovalRemark = ""
    protected var selectedResidintislFacilityApprovalRemark = ""
    protected var selectedResidintislSupportFacilityApprovalRemark = ""

    // Adapters
    private lateinit var adapter: LivingAreaInformationAdapter
    private lateinit var adapterToilet: RFToiletAdapter
    private lateinit var adapterIndoorGame: IndoorGameRFAdapter
    private lateinit var BasicInformationAdapter: ArrayAdapter<String>
    private lateinit var nfrastructureDetailsAndCompliancesAdapter: ArrayAdapter<String>
    private lateinit var tvLivingAreaInformationAdapter: ArrayAdapter<String>
    private lateinit var tvToiletAdapter: ArrayAdapter<String>
    private lateinit var tvToiletAdditionalSectionAdapter: ArrayAdapter<String>
    private lateinit var tvNonLivingAreaAdapter: ArrayAdapter<String>
    private lateinit var RFIndoorGameAdapter: ArrayAdapter<String>
    private lateinit var RFResidentialFacilitiesAvailableAdapter: ArrayAdapter<String>
    private lateinit var RFResidentialSupportFacilitiesAvailableAdapter: ArrayAdapter<String>

    private var rfToiletId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RfSrlmFormFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        centerId = arguments?.getString("centerId").orEmpty()
        centerName = arguments?.getString("centerName").orEmpty()
        sanctionOrder = arguments?.getString("sanctionOrder").orEmpty()
        facilityId = arguments?.getInt("facilityId") ?: 0

        tvtitle()

        loadInitialData()
        setupClickListeners()
        setupImageClickListeners()
        setupSpinnerAdapters()
        setupBackNavigation()
    }

    open  fun tvtitle(){}

    // -----------------------------------------
    // INITIAL LOAD
    // -----------------------------------------

    private fun loadInitialData() {
        val req = RfCommonReq(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            facilityId = facilityId
        )

        showProgress()
        viewModel.getRfBasicInformationrInfo(req)
        collectTCInfoResponse()

        val toiletReq = GetUrinalWashReq(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId,
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            facilityId = facilityId.toString()
        )
        viewModel.getToiletWashbasinDetails(toiletReq)
        GetToiletWashbasinDetails()
    }

    // -----------------------------------------
    // SETUP METHODS
    // -----------------------------------------

    private fun setupClickListeners() {
        // showAllSectionsAsList()
        binding.residentialfacilityqteamInfoLayout.RFQTInfoExpand.visibility = View.VISIBLE

        binding.residentialfacilityqteamInfoLayout.btnRFQTInfoNext.setOnClickListener {
            if (!validateApprovalAndRemarks(
                    selectedRFBasicInformationApproval,
                    selectedRFBasicInformationRemarks,
                    binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks
                )
            ) return@setOnClickListener

            // Store remarks if modification selected
            if (selectedRFBasicInformationApproval == "M") {
                selectedRFBasicInformationRemarks = binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks.text.toString().trim()
            } else {
                selectedRFBasicInformationRemarks = ""
            }

            // Update UI - show verified icon
            binding.residentialfacilityqteamInfoLayout.tvTrainInfo.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.baseline_info_24,
                0,
                R.drawable.ic_verified,
                0
            )

            // Collapse current section, show next section expanded
            collapseAllSections()
            binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand.visibility = View.VISIBLE
            binding.tvinfrastructureDetailsAndCompliances.visibility = View.VISIBLE
            binding.infrastructureDetailsAndCompliancesLayout.viewIDC.visibility = View.VISIBLE

            scrollToTop()
            loadInfrastructureDetails()
        }

        binding.infrastructureDetailsAndCompliancesLayout.btnIDCNext.setOnClickListener {
            if (!validateApprovalAndRemarks(
                    selectedInfrastctureDetailsComplainsApproval,
                    selectedInfrastctureDetailsComplainsRemarks,
                    binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks
                )
            ) return@setOnClickListener

            // Store remarks if modification selected
            if (selectedInfrastctureDetailsComplainsApproval == "M") {
                selectedInfrastctureDetailsComplainsRemarks = binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks.text.toString().trim()
            } else {
                selectedInfrastctureDetailsComplainsRemarks = ""
            }

            // Update UI - show verified icon
            binding.infrastructureDetailsAndCompliancesLayout.tvIDC.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.infrastructure,
                0,
                R.drawable.ic_verified,
                0
            )

            // Collapse current section, show next section expanded
            collapseAllSections()
            binding.livingareainformationLayout.LivingAreaInformationExpand.visibility = View.VISIBLE
            binding.tvlivingareainformation.visibility = View.VISIBLE
            binding.livingareainformationLayout.viewLAI.visibility = View.VISIBLE

            scrollToTop()
            loadLivingAreaInformation()
        }

        binding.livingareainformationLayout.btnLivingAreaInformationNext.setOnClickListener {
            if (!validateApprovalAndRemarks(
                    selectedRFLevingAreaInformationApproval,
                    selectedRFLevingAreaInformationRemarks,
                    binding.livingareainformationLayout.etLivingAreaInformationRemarks
                )
            ) return@setOnClickListener

            // Store remarks if modification selected
            if (selectedRFLevingAreaInformationApproval == "M") {
                selectedRFLevingAreaInformationRemarks = binding.livingareainformationLayout.etLivingAreaInformationRemarks.text.toString().trim()
            } else {
                selectedRFLevingAreaInformationRemarks = ""
            }

            // Update UI - show verified icon
            binding.livingareainformationLayout.tvLAI.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_property,
                0,
                R.drawable.ic_verified,
                0
            )

            // Collapse current section, show next section expanded
            collapseAllSections()
            binding.RFTioletLayout.toiletsExpand.visibility = View.VISIBLE
            binding.tvRFTiolet.visibility = View.VISIBLE
            binding.RFTioletLayout.viewToilet.visibility = View.VISIBLE

            scrollToTop()
            setupToiletRecyclerView()
        }

        binding.RFTioletLayout.btnToiletNext.setOnClickListener {
            if (!validateApprovalAndRemarks(
                    selectedRFToiletApproval,
                    selectedRFToiletRemarks,
                    binding.RFTioletLayout.etToiletRemarks
                )
            ) return@setOnClickListener

            // Store remarks if modification selected
            if (selectedRFToiletApproval == "M") {
                selectedRFToiletRemarks = binding.RFTioletLayout.etToiletRemarks.text.toString().trim()
            } else {
                selectedRFToiletRemarks = ""
            }

            // Update UI - show verified icon
            binding.RFTioletLayout.tvToilet.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.toilet,
                0,
                R.drawable.ic_verified,
                0
            )

            // Collapse current section, show next section expanded
            collapseAllSections()
            binding.RFTioletAdditionalSectionLayout.AdditionalSectionExpand.visibility = View.VISIBLE
            binding.tvRFtoiletAdditionalSection.visibility = View.VISIBLE
            binding.RFTioletAdditionalSectionLayout.viewToiletAdditionalSection.visibility = View.VISIBLE

            scrollToTop()
        }

        binding.RFTioletAdditionalSectionLayout.btnAdditionalSectionNext.setOnClickListener {
            if (!validateApprovalAndRemarks(
                    selectedRFToiletAdditionalSanctionApproval,
                    selectedRFToiletAdditionalSanctionRemarks,
                    binding.RFTioletAdditionalSectionLayout.etAdditionalSectionRemarks
                )
            ) return@setOnClickListener

            // Store remarks if modification selected
            if (selectedRFToiletAdditionalSanctionApproval == "M") {
                selectedRFToiletAdditionalSanctionRemarks = binding.RFTioletAdditionalSectionLayout.etAdditionalSectionRemarks.text.toString().trim()
            } else {
                selectedRFToiletAdditionalSanctionRemarks = ""
            }

            // Update UI - show verified icon
            binding.RFTioletAdditionalSectionLayout.tvToiletAdditionalSection.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.toilet,
                0,
                R.drawable.ic_verified,
                0
            )

            // Collapse current section, show next section expanded
            collapseAllSections()
            binding.RFNonLivingAreaLayout.NonLivingAreaInfoExpand.visibility = View.VISIBLE
            binding.tvRFConstraintLayoutNonLivingArea.visibility = View.VISIBLE
            binding.RFNonLivingAreaLayout.viewNonLivingAreaInfor.visibility = View.VISIBLE

            scrollToTop()
            loadNonLivingAreaInformation()
        }

        binding.RFNonLivingAreaLayout.btnNonLivingAreaInformationNext.setOnClickListener {
            if (!validateApprovalAndRemarks(
                    selectedNonAreaInfoApproval,
                    selectedRFNonLivingAreaRemarks,
                    binding.RFNonLivingAreaLayout.etNonLivingAreaInformationRemarks
                )
            ) return@setOnClickListener

            // Store remarks if modification selected
            if (selectedNonAreaInfoApproval == "M") {
                selectedRFNonLivingAreaRemarks = binding.RFNonLivingAreaLayout.etNonLivingAreaInformationRemarks.text.toString().trim()
            } else {
                selectedRFNonLivingAreaRemarks = ""
            }

            // Update UI - show verified icon
            binding.RFNonLivingAreaLayout.tvNonLivingAreaInfor.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_class,
                0,
                R.drawable.ic_verified,
                0
            )

            // Collapse current section, show next section expanded
            collapseAllSections()
            binding.RFIndoorGameLayout.IndoorGameExpand.visibility = View.VISIBLE
            binding.tvRFConstraintLayoutIndoorGame.visibility = View.VISIBLE
            binding.RFIndoorGameLayout.viewIndoorGame.visibility = View.VISIBLE

            scrollToTop()
            loadIndoorGames()
        }

        binding.RFIndoorGameLayout.btnIndoorGameNext.setOnClickListener {
            if (!validateApprovalAndRemarks(
                    selectedIndoorGameApproval,
                    selectedIndoorGameApprovalRemark,
                    binding.RFIndoorGameLayout.etIndoorGameRemarks
                )
            ) return@setOnClickListener

            // Store remarks if modification selected
            if (selectedIndoorGameApproval == "M") {
                selectedIndoorGameApprovalRemark = binding.RFIndoorGameLayout.etIndoorGameRemarks.text.toString().trim()
            } else {
                selectedIndoorGameApprovalRemark = ""
            }

            // Update UI - show verified icon
            binding.RFIndoorGameLayout.tvIndoorGame.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.tabletennis,
                0,
                R.drawable.ic_verified,
                0
            )

            // Collapse current section, show next section expanded
            collapseAllSections()
            binding.RFResidentialFacilitiesAvailable.RFResidentialFacalityExpand.visibility = View.VISIBLE
            binding.RFResidentialConstraintLayoutFacilitiesAvailable.visibility = View.VISIBLE
            binding.RFResidentialFacilitiesAvailable.viewRFResidentialFacality.visibility = View.VISIBLE

            scrollToTop()
            loadResidentialFacilities()
        }

        binding.RFResidentialFacilitiesAvailable.btnRFResidentialFacalityNext.setOnClickListener {
            if (!validateApprovalAndRemarks(
                    selectedResidintislFacilityApproval,
                    selectedResidintislFacilityApprovalRemark,
                    binding.RFResidentialFacilitiesAvailable.etRFResidentialFacalityRemarks
                )
            ) return@setOnClickListener

            // Store remarks if modification selected
            if (selectedResidintislFacilityApproval == "M") {
                selectedResidintislFacilityApprovalRemark = binding.RFResidentialFacilitiesAvailable.etRFResidentialFacalityRemarks.text.toString().trim()
            } else {
                selectedResidintislFacilityApprovalRemark = ""
            }

            // Update UI - show verified icon
            binding.RFResidentialFacilitiesAvailable.tvRFResidentialFacality.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_equipment,
                0,
                R.drawable.ic_verified,
                0
            )

            // Collapse current section, show next section expanded
            collapseAllSections()
            binding.rfSupportFacilitiesAvailableLayout.RFResidentialsupportFacalityExpand.visibility = View.VISIBLE
            binding.RFRFSupportFacilitiesAvailable.visibility = View.VISIBLE
            binding.rfSupportFacilitiesAvailableLayout.viewRFResidentialsupportFacality.visibility = View.VISIBLE

            scrollToTop()
            loadSupportFacilities()
        }

        binding.rfSupportFacilitiesAvailableLayout.btnRFResidentialsupportFacalityNext.setOnClickListener {
            if (!validateApprovalAndRemarks(
                    selectedResidintislSupportFacilityApproval,
                    selectedResidintislSupportFacilityApprovalRemark,
                    binding.rfSupportFacilitiesAvailableLayout.etRFResidentialsupportFacalityRemarks
                )
            ) return@setOnClickListener

            // Store remarks if modification selected
            if (selectedResidintislSupportFacilityApproval == "M") {
                selectedResidintislSupportFacilityApprovalRemark = binding.rfSupportFacilitiesAvailableLayout.etRFResidentialsupportFacalityRemarks.text.toString().trim()
            } else {
                selectedResidintislSupportFacilityApprovalRemark = ""
            }

            // Update UI - show verified icon
            binding.rfSupportFacilitiesAvailableLayout.tvRFResidentialSupportFacality.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_signage,
                0,
                R.drawable.ic_verified,
                0
            )

            // Hide all expandable sections, show final submit button
            collapseAllSections()
            binding.btnSubmitFinal.visibility = View.VISIBLE

            scrollToTop()
        }

        binding.btnSubmitFinal.setOnClickListener {
            submitFinalForm()
        }

        // Previous buttons - expand the previous section
        binding.infrastructureDetailsAndCompliancesLayout.btnIDCPrevious.setOnClickListener {
            collapseAllSections()
            binding.residentialfacilityqteamInfoLayout.RFQTInfoExpand.visibility = View.VISIBLE
            binding.tvinfrastructureDetailsAndCompliances.visibility = View.VISIBLE
            scrollToTop()
        }

        binding.livingareainformationLayout.btnLivingAreaInformationPrevious.setOnClickListener {
            collapseAllSections()
            binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand.visibility = View.VISIBLE
            binding.tvinfrastructureDetailsAndCompliances.visibility = View.VISIBLE
            binding.infrastructureDetailsAndCompliancesLayout.viewIDC.visibility = View.VISIBLE
            scrollToTop()
        }

        binding.RFTioletLayout.btnToiletPrevious.setOnClickListener {
            collapseAllSections()
            binding.livingareainformationLayout.LivingAreaInformationExpand.visibility = View.VISIBLE
            binding.tvlivingareainformation.visibility = View.VISIBLE
            binding.livingareainformationLayout.viewLAI.visibility = View.VISIBLE
            scrollToTop()
        }

        binding.RFTioletAdditionalSectionLayout.btnAdditionalSectionPrevious.setOnClickListener {
            collapseAllSections()
            binding.RFTioletLayout.toiletsExpand.visibility = View.VISIBLE
            binding.tvRFTiolet.visibility = View.VISIBLE
            binding.RFTioletLayout.viewToilet.visibility = View.VISIBLE
            scrollToTop()
        }

        binding.RFNonLivingAreaLayout.btnNonLivingAreaInformationPrevious.setOnClickListener {
            collapseAllSections()
            binding.RFTioletAdditionalSectionLayout.AdditionalSectionExpand.visibility = View.VISIBLE
            binding.tvRFtoiletAdditionalSection.visibility = View.VISIBLE
            binding.RFTioletAdditionalSectionLayout.viewToiletAdditionalSection.visibility = View.VISIBLE
            scrollToTop()
        }

        binding.RFIndoorGameLayout.btnIndoorGamePrevious.setOnClickListener {
            collapseAllSections()
            binding.RFNonLivingAreaLayout.NonLivingAreaInfoExpand.visibility = View.VISIBLE
            binding.tvRFConstraintLayoutNonLivingArea.visibility = View.VISIBLE
            binding.RFNonLivingAreaLayout.viewNonLivingAreaInfor.visibility = View.VISIBLE
            scrollToTop()
        }

        binding.RFResidentialFacilitiesAvailable.btnRFResidentialFacalityPrevious.setOnClickListener {
            collapseAllSections()
            binding.RFIndoorGameLayout.IndoorGameExpand.visibility = View.VISIBLE
            binding.tvRFConstraintLayoutIndoorGame.visibility = View.VISIBLE
            binding.RFIndoorGameLayout.viewIndoorGame.visibility = View.VISIBLE
            scrollToTop()
        }

        binding.rfSupportFacilitiesAvailableLayout.btnRFResidentialsupportFacalityPrevious.setOnClickListener {
            collapseAllSections()
            binding.RFResidentialFacilitiesAvailable.RFResidentialFacalityExpand.visibility = View.VISIBLE
            binding.RFResidentialConstraintLayoutFacilitiesAvailable.visibility = View.VISIBLE
            binding.RFResidentialFacilitiesAvailable.viewRFResidentialFacality.visibility = View.VISIBLE
            binding.btnSubmitFinal.visibility = View.GONE
            scrollToTop()
        }
    }

    private fun showAllSectionsAsList() {
        binding.tvinfrastructureDetailsAndCompliances.visibility = View.VISIBLE
        binding.tvlivingareainformation.visibility = View.VISIBLE
        binding.tvRFTiolet.visibility = View.VISIBLE
        binding.tvRFtoiletAdditionalSection.visibility = View.VISIBLE
        binding.tvRFConstraintLayoutNonLivingArea.visibility = View.VISIBLE
        binding.tvRFConstraintLayoutIndoorGame.visibility = View.VISIBLE
        binding.RFResidentialConstraintLayoutFacilitiesAvailable.visibility = View.VISIBLE
        binding.RFRFSupportFacilitiesAvailable.visibility = View.VISIBLE
    }

    private fun collapseAllSections() {
        listOf(
            binding.residentialfacilityqteamInfoLayout.RFQTInfoExpand,
            binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand,
            binding.livingareainformationLayout.LivingAreaInformationExpand,
            binding.RFTioletLayout.toiletsExpand,
            binding.RFTioletAdditionalSectionLayout.AdditionalSectionExpand,
            binding.RFNonLivingAreaLayout.NonLivingAreaInfoExpand,
            binding.RFIndoorGameLayout.IndoorGameExpand,
            binding.RFResidentialFacilitiesAvailable.RFResidentialFacalityExpand,
            binding.rfSupportFacilitiesAvailableLayout.RFResidentialsupportFacalityExpand
        ).forEach { it.visibility = View.GONE }

        listOf(
            binding.residentialfacilityqteamInfoLayout.viewRFQTInfo,
            binding.infrastructureDetailsAndCompliancesLayout.viewIDC,
            binding.livingareainformationLayout.viewLAI,
            binding.RFTioletLayout.viewToilet,
            binding.RFTioletAdditionalSectionLayout.viewToiletAdditionalSection,
            binding.RFNonLivingAreaLayout.viewNonLivingAreaInfor,
            binding.RFIndoorGameLayout.viewIndoorGame,
            binding.RFResidentialFacilitiesAvailable.viewRFResidentialFacality,
            binding.rfSupportFacilitiesAvailableLayout.viewRFResidentialsupportFacality
        ).forEach { it.visibility = View.GONE }

        // Hide submit button
        binding.btnSubmitFinal.visibility = View.GONE
    }
    private fun setupImageClickListeners() {
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
            showBase64ImageDialog(requireContext(), RFWardenCaretakerMaleFile, "Warden Caretaker Male")
        }

        binding.RFResidentialFacilitiesAvailable.HostelsSeparatedFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFHostelsSeparatedFile, "Hostels Separated")
        }

        binding.RFResidentialFacilitiesAvailable.SecurityGuardsFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFSecurityGuardsFile, "Security Guards")
        }

        binding.rfSupportFacilitiesAvailableLayout.SafeDrinikingAavailableFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFsafeDrinkingeFile, "Safe Drinking Water")
        }

        binding.rfSupportFacilitiesAvailableLayout.FirstAidKitFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFfirstAidKitFile, "First Aid Kit")
        }

        binding.rfSupportFacilitiesAvailableLayout.FireFightingEquipmentrFile.setOnClickListener {
            showBase64ImageDialog(requireContext(), RFfireFightingFile, "Fire Fighting Equipment")
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

    private fun setupSpinnerAdapters() {
        BasicInformationAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            approvalList
        )
        binding.residentialfacilityqteamInfoLayout.SpinnerTcInfo.setAdapter(BasicInformationAdapter)

        nfrastructureDetailsAndCompliancesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            approvalList
        )
        binding.infrastructureDetailsAndCompliancesLayout.SpinnerIDC.setAdapter(nfrastructureDetailsAndCompliancesAdapter)

        tvLivingAreaInformationAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            approvalList
        )
        binding.livingareainformationLayout.SpinnerLivingAreaInformation.setAdapter(tvLivingAreaInformationAdapter)

        tvToiletAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            approvalList
        )
        binding.RFTioletLayout.SpinnerToilet.setAdapter(tvToiletAdapter)

        tvToiletAdditionalSectionAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            approvalList
        )
        binding.RFTioletAdditionalSectionLayout.SpinnerAdditionalSection.setAdapter(tvToiletAdditionalSectionAdapter)

        tvNonLivingAreaAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            approvalList
        )
        binding.RFNonLivingAreaLayout.SpinnerNonLivingAreaInformation.setAdapter(tvNonLivingAreaAdapter)

        RFIndoorGameAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            approvalList
        )
        binding.RFIndoorGameLayout.SpinnerIndoorGame.setAdapter(RFIndoorGameAdapter)

        RFResidentialFacilitiesAvailableAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            approvalList
        )
        binding.RFResidentialFacilitiesAvailable.SpinnerRFResidentialFacality.setAdapter(RFResidentialFacilitiesAvailableAdapter)

        RFResidentialSupportFacilitiesAvailableAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            approvalList
        )
        binding.rfSupportFacilitiesAvailableLayout.SpinnerRFResidentialsupportFacality.setAdapter(RFResidentialSupportFacilitiesAvailableAdapter)

        setupSpinnerListeners()
    }

    private fun setupSpinnerListeners() {
        binding.residentialfacilityqteamInfoLayout.SpinnerTcInfo.setOnItemClickListener { parent, _, position, _ ->
            handleSpinnerSelection(
                parent.getItemAtPosition(position).toString(),
                { selectedRFBasicInformationApproval = it },
                binding.residentialfacilityqteamInfoLayout.etRFQTInfoRemarks,
                binding.residentialfacilityqteamInfoLayout.textViewRFQTInfoRemarks
            )
        }

        binding.infrastructureDetailsAndCompliancesLayout.SpinnerIDC.setOnItemClickListener { parent, _, position, _ ->
            handleSpinnerSelection(
                parent.getItemAtPosition(position).toString(),
                { selectedInfrastctureDetailsComplainsApproval = it },
                binding.infrastructureDetailsAndCompliancesLayout.etIDCRemarks,
                binding.infrastructureDetailsAndCompliancesLayout.tvSelectApprovalIDC
            )
        }

        binding.livingareainformationLayout.SpinnerLivingAreaInformation.setOnItemClickListener { parent, _, position, _ ->
            handleSpinnerSelection(
                parent.getItemAtPosition(position).toString(),
                { selectedRFLevingAreaInformationApproval = it },
                binding.livingareainformationLayout.etLivingAreaInformationRemarks,
                binding.livingareainformationLayout.LivingAreaInformationRemarks
            )
        }

        binding.RFTioletLayout.SpinnerToilet.setOnItemClickListener { parent, _, position, _ ->
            handleSpinnerSelection(
                parent.getItemAtPosition(position).toString(),
                { selectedRFToiletApproval = it },
                binding.RFTioletLayout.etToiletRemarks,
                binding.RFTioletLayout.LivingToiletRemarks
            )
        }

        binding.RFTioletAdditionalSectionLayout.SpinnerAdditionalSection.setOnItemClickListener { parent, _, position, _ ->
            handleSpinnerSelection(
                parent.getItemAtPosition(position).toString(),
                { selectedRFToiletAdditionalSanctionApproval = it },
                binding.RFTioletAdditionalSectionLayout.etAdditionalSectionRemarks,
                binding.RFTioletAdditionalSectionLayout.textViewAdditionalSectionRemarks
            )
        }

        binding.RFNonLivingAreaLayout.SpinnerNonLivingAreaInformation.setOnItemClickListener { parent, _, position, _ ->
            handleSpinnerSelection(
                parent.getItemAtPosition(position).toString(),
                { selectedNonAreaInfoApproval = it },
                binding.RFNonLivingAreaLayout.etNonLivingAreaInformationRemarks,
                binding.RFNonLivingAreaLayout.tvNonLivingAreaInformationRemarks
            )
        }

        binding.RFIndoorGameLayout.SpinnerIndoorGame.setOnItemClickListener { parent, _, position, _ ->
            handleSpinnerSelection(
                parent.getItemAtPosition(position).toString(),
                { selectedIndoorGameApproval = it },
                binding.RFIndoorGameLayout.etIndoorGameRemarks,
                binding.RFIndoorGameLayout.IndoorGameRemarks
            )
        }

        binding.RFResidentialFacilitiesAvailable.SpinnerRFResidentialFacality.setOnItemClickListener { parent, _, position, _ ->
            handleSpinnerSelection(
                parent.getItemAtPosition(position).toString(),
                { selectedResidintislFacilityApproval = it },
                binding.RFResidentialFacilitiesAvailable.etRFResidentialFacalityRemarks,
                binding.RFResidentialFacilitiesAvailable.tvRFResidentialFacalityRemarks
            )
        }

        binding.rfSupportFacilitiesAvailableLayout.SpinnerRFResidentialsupportFacality.setOnItemClickListener { parent, _, position, _ ->
            handleSpinnerSelection(
                parent.getItemAtPosition(position).toString(),
                { selectedResidintislSupportFacilityApproval = it },
                binding.rfSupportFacilitiesAvailableLayout.etRFResidentialsupportFacalityRemarks,
                binding.rfSupportFacilitiesAvailableLayout.tvRFResidentialsupportFacalityRemarks
            )
        }
    }

    private fun setupBackNavigation() {
        binding.toolbar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // -----------------------------------------
    // DATA LOADING METHODS
    // -----------------------------------------

    @SuppressLint("SetTextI18n")
    private fun collectTCInfoResponse() {
        viewModel.ResidentialFacilityQTeam.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        hideProgress()
                        it.wrappedList.firstOrNull()?.let { x ->
                            with(binding.residentialfacilityqteamInfoLayout) {
                                ResidentialFacilityName.text = safeText(x.residentialFacilityName)
                                ResidentialFacilityType.text = safeText(x.residentialType)
                                HouseNo.text = safeText(x.houseNo)
                                Street.text = safeText(x.streetNo1)
                                Landmark.text = safeText(x.landmark)
                                StateUtTc.text = safeText(x.stateName)
                                DistrictTc.text = safeText(x.districtName)
                                Block.text = safeText(x.blockName)
                                GramPanchayat.text = safeText(x.gpName)
                                VillageWardNo.text = safeText(x.villageName)
                                PoliceStation.text = x.policeStation
                                LatitudeLongitude.text = safeText(x.latitude)
                                Pincode.text = x.pincode
                                WardenMobileNo.text = safeText(x.geoAddress)
                                Mobile.text = safeText(x.mobile)
                                RFPNoWSC.text = safeText(x.residentialFacilitiesPhNo)
                                Email.text = safeText(x.email)
                                TypeofArea.text = safeText(x.typeOfArea)
                                categoryOfTCLocaXYZanyOtherArea.text = safeText(x.categoryOfTc)
                                ApproximateDistanceFrom.text = safeText(x.distBusStand)
                                DistanceFromTheTrainingCenter.text = safeText(x.distFromTc)
                                AvailabilityOfPick.text = safeText(x.pickUpDrop)
                                DistanceFromRailwayStand.text = safeText(x.distRailStand)
                                DistanceFromAutoTraining.text = safeText(x.distAutoStand)
                                WadrenName.text = safeText(x.wardenName)
                                WardenGender.text = safeText(x.wardgender)
                                WardenAddress.text = safeText(x.wardAddress)
                                WardenEmployeeId.text = safeText(x.wardEmpId)
                                WardenEmailId.text = safeText(x.wardEmail)
                                WardenMobileNo.text = safeText(x.wardMobile)
                            }
                            RFQTresFacilityId = x.resFacilityId.toString()
                            RFQTBasicInfoPdf = x.policeVerfictnImage.toString()
                            RFQTBasicInfoAppointMent = x.empLetterImage.toString()
                        }
                    }
                    202 -> {
                        hideProgress()
                        toast("No data available.")
                    }
                    301 -> {
                        hideProgress()
                        toast("Please upgrade your app.")
                    }
                    401 -> {
                        hideProgress()
                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }
            }
            result.onFailure {
                hideProgress()
                toast("Failed: ${it.message}")
            }
        }
    }

    private fun loadInfrastructureDetails() {
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
        showProgress()
    }

    @SuppressLint("SetTextI18n")
    private fun collectInsfrastructureDetailsAndComplains() {
        viewModel.CompliancesRFQTReqRFQT.removeObservers(viewLifecycleOwner)
        viewModel.CompliancesRFQTReqRFQT.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        hideProgress()
                        it.wrappedList.firstOrNull()?.let { x ->
                            with(binding.infrastructureDetailsAndCompliancesLayout) {
                                onwershipOfBulding.text = x.ownership
                                areaOfTheBuilding.text = x.buildingArea
                                HostelNameBoard.text = x.hostelNameBoard
                                BasicInformationBoard.text = x.basicInformationBoard
                                SecuringWiresDone.text = x.securingWiresDone
                                RoofofBulding.text = x.roof
                                WhetherItIsStructurally.text = x.plastring
                                visibleSignsOfLeakages.text = x.leakage
                                ConformanceToDduGky.text = x.conformanceDdu
                                ProtectionOfStairs.text = x.protectionStairs
                                CirculatingArea.text = x.circulatingArea
                                Corridor.text = x.corridor
                                SwitchBoardsAndPanelBoards.text = x.switchBoardsPanelBoards
                                StudentEntitlement.text = x.studentEntitlementBoard
                                ContactDetailOfImportantPeople.text = x.contactDetailImportantPeople
                                FoodSpecificationBoard.text = x.foodSpecificationBoard
                                Area.text = x.openSpaceArea

                                // Setup image click listeners
                                OnwershipOfBuldingFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.selfDeclaration, "Building Photos")
                                }
                                BuildingAreaSQFPlanFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.buildingPlanFile, "Building Plan")
                                }
                                RoofLavelFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.buildingPhotosFile, "Building Photos")
                                }
                                WhetherItIsStructurallyFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.wallPhotosFile, "Wall Photos")
                                }
                                VisibleSignsLeakagesFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.leakagesProofFile, "Leakages Proof")
                                }
                                ProtectionOfStairsFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.protectionStairsProofFile, "Stairs Protection")
                                }
                                HostelNameBoardFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.hostelNameBoardProofFile, "Hostel Name Board")
                                }
                                StudentEntitlementFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.studentEntitlementBoardProofFile, "Student Entitlement")
                                }
                                ContactDetailOfImportantPeopleFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.contactDetailImportantPeopleproofFile, "Contact Details")
                                }
                                SpecificationBoardFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.foodSpecificationBoardFile, "Food Specification")
                                }
                                BasicInformationBoardFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.basicInformationBoardproofFile, "Basic Information Board")
                                }
                                SecuringWiresDoneFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.securingWiresDoneProofFile, "Securing Wires")
                                }
                                CorridorFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.corridorProofFile, "Corridor")
                                }
                                circulatingAreaProofFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.circulatingAreaProofFile, "Circulating Area")
                                }
                                ConformanceToDduGkyFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.conformanceDduProofFile, "DDU Conformance")
                                }
                                SwitchBoardsAndPanelBoardsFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.switchBoardsPanelBoardsProofFile, "Switch Boards")
                                }
                            }
                        }
                    }
                    202 -> {
                        hideProgress()
                        toast("No data available.")
                    }
                    301 -> {
                        hideProgress()
                        toast("Please upgrade your app.")
                    }
                    401 -> {
                        hideProgress()
                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }
            }
            result.onFailure {
                hideProgress()
                toast("Failed: ${it.message}")
            }
        }
    }

    private fun loadLivingAreaInformation() {
        adapter = LivingAreaInformationAdapter(emptyList()) { center ->
            val requestTcRoomDetails = RfLivingAreaInformationRQ(
                appVersion = BuildConfig.VERSION_NAME,
                tcId = centerId.toInt(),
                sanctionOrder = sanctionOrder,
                roomNo = center.roomNo.toInt(),
                facilityId = facilityId
            )
            viewModel.getRfLivingAreaInformation(requestTcRoomDetails)
            showProgress()

            viewModel.fLivingAreaInformation.removeObservers(viewLifecycleOwner)
            viewModel.fLivingAreaInformation.observe(viewLifecycleOwner) { result ->
                result.onSuccess {
                    hideProgress()
                    when (it.responseCode) {
                        200 -> {
                            it.wrappedList.firstOrNull()?.let { x ->
                                showRoomInformationDialog(x)
                            }
                        }

                        202 -> {
                            hideProgress()
                            toast("No data available.")
                        }

                        301 -> {
                            hideProgress()
                            toast("Please upgrade your app.")
                        }

                        401 -> {
                            hideProgress()
                            AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                        }
                    }
                }
                result.onFailure {
                    hideProgress()
                    toast("Failed: ${it.message}")
                }
            }
        }

        binding.livingareainformationLayout.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.livingareainformationLayout.recyclerView.adapter = adapter

        val livingRoomListViewReq = LivingRoomListViewRQ(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            facilityId = facilityId
        )
        viewModel.getlivingRoomListView(livingRoomListViewReq)
        RoomRecyclerView()
        showProgress()
    }

    private fun RoomRecyclerView() {
        viewModel.livingRoomListView.removeObservers(viewLifecycleOwner)
        viewModel.livingRoomListView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        hideProgress()
                        adapter.updateData(it.wrappedList ?: emptyList())
                    }
                    202 -> {
                        hideProgress()
                        toast("No data available.")
                    }
                    301 -> {
                        hideProgress()
                        toast("Please upgrade your app.")
                    }
                    401 -> {
                        hideProgress()
                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }
            }
            result.onFailure {
                hideProgress()
                toast("Failed: ${it.message}")
            }
        }
    }

    private fun setupToiletRecyclerView() {
        adapterToilet = RFToiletAdapter(emptyList()) { selectedItem ->
            lifecycleScope.launch(Dispatchers.IO) {
                val toiletRoomReq = ToiletRoomReq(
                    appVersion = BuildConfig.VERSION_NAME,
                    rfToiletId = rfToiletId,
                )
                viewModel.getRfToiletRoomInformation(toiletRoomReq)
            }
        }
        binding.RFTioletLayout.recyclerViewToilet.layoutManager =
            LinearLayoutManager(requireContext())
        binding.RFTioletLayout.recyclerViewToilet.adapter = adapterToilet

        val toiletCountListReq = ToiletCountListReq(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            facilityId = facilityId.toString()
        )


        viewModel.getToiletCountList(toiletCountListReq)
        setupToiletCountObservers()
        showProgress()
    }

    private fun setupToiletCountObservers() {
        viewModel.ToiletCountListView.removeObservers(viewLifecycleOwner)
        viewModel.ToiletCountListView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        it.wrappedList.firstOrNull()?.let { x ->
                            setupToiletClickListeners(x)
                        }
                        hideProgress()
                    }
                    202, 301, 401 -> handleErrorResponse(it.responseCode)
                }
            }
            result.onFailure {
                hideProgress()
                toast("Failed: ${it.message}")
            }
        }

        viewModel.ToiletRoomInformationView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        it.wrappedList.firstOrNull()?.let { x ->
                            showToiletInformationDialog(x)
                        }
                        hideProgress()
                    }
                    202, 301, 401 -> handleErrorResponse(it.responseCode)
                }
            }
            result.onFailure {
                hideProgress()
                toast("Failed: ${it.message}")
            }
        }
    }
    //ToiletCountListResponseDetails
    private fun setupToiletClickListeners(toiletData: CountList) {
        with(binding.RFTioletLayout) {
            TvToilet.text = toiletData.toiletCount
            TvBathroom.text = toiletData.washroomCount
            TvToiletBathroom.text = toiletData.toiletWashroomCount

            listOf(TvToilet, TvBathroom, TvToiletBathroom).forEach {
                it.paintFlags = it.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }

            tvToilet.setOnClickListener {
                LinLayoutCardView.visibility = View.VISIBLE
                LinLayoutRecyclerView.visibility = View.GONE
                tvToilet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.toilet, 0, 0, 0)
            }

            linLayoutToilet.setOnClickListener {
                if (toiletData.toiletCount != "0") {
                    ListViewToilet("Toilet")
                    tvToilet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.back_black, 0, 0, 0)
                    LinLayoutCardView.visibility = View.GONE
                    LinLayoutRecyclerView.visibility = View.VISIBLE
                }
            }

            binding.RFTioletLayout.LinLayoutBathroom.setOnClickListener {
                if (toiletData.washroomCount != "0") {
                    tvToilet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.back_black, 0, 0, 0)
                    LinLayoutCardView.visibility = View.GONE
                    LinLayoutRecyclerView.visibility = View.VISIBLE
                    ListViewToilet("Washroom")
                }
            }

            binding.RFTioletLayout.LinLayoutToiletAndBathroom.setOnClickListener {
                if (toiletData.toiletWashroomCount != "0") {
                    ListViewToilet("Toilet Cum Washroom")
                    tvToilet.setCompoundDrawablesWithIntrinsicBounds(R.drawable.back_black, 0, 0, 0)
                    LinLayoutCardView.visibility = View.GONE
                    LinLayoutRecyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun ListViewToilet(toiletType: String) {
        val livingRoomlistViewReq = ToiletRoomInformationReq(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            facilityId = facilityId,
            toiletType = toiletType
        )
        viewModel.getToiletRoomListView(livingRoomlistViewReq)
        showProgress()

        viewModel.ToiletRoomListView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        hideProgress()
                        adapterToilet.updateData(it.wrappedList ?: emptyList())
                        it.wrappedList.firstOrNull()?.let { x ->
                            rfToiletId = x.rfToiletId
                        }
                    }
                    202 -> {
                        adapterToilet.updateData(emptyList())
                        hideProgress()
                        toast("No data available.")
                    }
                    301 -> {
                        adapterToilet.updateData(emptyList())
                        hideProgress()
                        toast("Please upgrade your app.")
                    }
                    401 -> {
                        hideProgress()
                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }
            }
            result.onFailure {
                hideProgress()
                toast("Failed: ${it.message}")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun GetToiletWashbasinDetails() {
        viewModel.getToiletWashbasinDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgress()
                when (it.responseCode) {
                    200 -> {
                        it.wrappedList.firstOrNull()?.let { x ->
                            with(binding.RFTioletAdditionalSectionLayout) {
                                UrinalAdditionalSection.setText(x.urinal)
                                WashbasinsAdditionalSection.setText(x.washbasin)
                                OverHeadTankAdditionalSection.setText(x.overheadTank)

                                ToiletAdditionalSectionFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.urinalFile, "Preview")
                                }
                                WashbasinsAdditionalSectionFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.washbasinFile, "Preview")
                                }
                                OverHeadTankAdditionalSectionFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.overheadTankFile, "Preview")
                                }
                            }
                        }
                    }
                    202 -> toast("No data available.")
                    301 -> toast("Please upgrade your app.")
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgress()
                toast("Failed: ${it.message}")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadNonLivingAreaInformation() {
        val requestLRLVRQ = LivingRoomListViewRQ(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            facilityId = facilityId
        )
        viewModel.getRfNonLivingAreaInformation(requestLRLVRQ)

        viewModel.NonAreaInformationRoom.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        it.wrappedList.firstOrNull()?.let { x ->
                            with(binding.RFNonLivingAreaLayout) {
                                WhetherFoodFor.text = safeText(x.preparedFood)
                                reTheDiningAndRecreationAreaSeparate.text = safeText(x.separateAreas)
                                NoOfStoolsChairsBenches.text = safeText(x.noOfSeats)
                                WashArea.text = safeText(x.washArea)
                                WhetherTv.text = safeText(x.noOfSeats)
                                DiningLength.text = safeText(x.diningLength)
                                DiningWidth.text = safeText(x.diningWidth)
                                DiningArea.text = safeText(x.diningArea)
                                RecreationLength.text = safeText(x.recreationLength)
                                RecreationWidth.text = safeText(x.recreationWidth)
                                RecreationArea.text = safeText(x.recreationArea)
                                ReceptionArea.text = safeText(x.receptionArea)
                                LengthRecreationAndDining.text = safeText(x.diningLength)
                                AreaRecreationAndDining.text = safeText(x.diningArea)
                                WidthRecreationAndDining.text = safeText(x.diningWidth)

                                recreationFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.diningAreaFile, "Preview")
                                }
                                recreationAndDiningFile.setOnClickListener {
                                    showBase64ImageDialog(requireContext(), x.diningRecreationAreaFile, "Preview")
                                }

                                PreparedFoodFile = x.preprationFoodPdf
                                ReceptionAreaPdf = x.receptionAreaPdf.toString()

                                if (x.separateAreas == "Yes") {
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
                    202 -> toast("No data available.")
                    301 -> toast("Please upgrade your app.")
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgress()
                toast("Failed: ${it.message}")
            }
        }
    }

    private fun loadIndoorGames() {
        adapterIndoorGame = IndoorGameRFAdapter(emptyList()) { game ->
            showBase64ImageDialog(requireContext(), game.indoorGamePdf, "Preview")
        }
        binding.RFIndoorGameLayout.recyclerViewInddorGame.adapter = adapterIndoorGame
        binding.RFIndoorGameLayout.recyclerViewInddorGame.layoutManager =
            LinearLayoutManager(requireContext())

        val rfGameRequest = RFGameRequest(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            facilityId = facilityId
        )
        viewModel.getRfIndoorGameDetails(rfGameRequest)
        IndoorGameRecyclerView()
        showProgress()
    }

    private fun IndoorGameRecyclerView() {
        viewModel.RfIndoorGameDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        hideProgress()
                        adapterIndoorGame.updateData(it.wrappedList ?: emptyList())
                    }
                    202 -> {
                        hideProgress()
                        toast("No data available.")
                    }
                    301 -> {
                        hideProgress()
                        toast("Please upgrade your app.")
                    }
                    401 -> {
                        hideProgress()
                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }
                result.onFailure {
                    hideProgress()
                    toast("Failed: ${it.message}")
                }
            }
        }
    }

    private fun loadResidentialFacilities() {
        val requestTcInfo = RfCommonReq(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            facilityId = facilityId
        )
        viewModel.getResidentialFacilitiesAvailable(requestTcInfo)
        ResidentialFacilitiesForm()
        showProgress()
    }

    private fun ResidentialFacilitiesForm() {
        viewModel.RFResidentialFacilitiesAvailable.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        hideProgress()
                        it.wrappedList.firstOrNull()?.let { x ->
                            with(binding.RFResidentialFacilitiesAvailable) {
                                WardenCare.text = (x.wardenCaretakerFemale)
                                MaleDoctor.text = (x.maleDoctor)
                                WardenCaretakerMale.text = (x.wardenCaretakerMale)
                                HostelsSeparated.text = (x.hostelsSeparated)
                                FemaleDoctor.text = (x.femaleDoctor)
                                SecurityGuards.text = (x.securityGuards)
                            }
                            RFWardenCareFile = x.wardenCaretakerFemalePdf
                            RFMaleDoctorFile = x.maleDoctorPdf
                            RFFemaleDoctorFile = x.femaleDoctorPdf
                            RFWardenCaretakerMaleFile = x.wardenCaretakerMalePdf
                            RFHostelsSeparatedFile = x.hostelsSeparatedPdf
                            RFSecurityGuardsFile = x.securityGuardsPdf
                        }
                    }
                    202 -> {
                        hideProgress()
                        toast("No data available.")
                    }
                    301 -> {
                        hideProgress()
                        toast("Please upgrade your app.")
                    }
                    401 -> {
                        hideProgress()
                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }
                result.onFailure {
                    hideProgress()
                    toast("Failed: ${it.message}")
                }
            }
        }
    }

    private fun loadSupportFacilities() {
        val rfGameRequest = RFGameRequest(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            facilityId = facilityId

        )
        viewModel.getRFSupportFacilitiesAvailable(rfGameRequest)
        RFSupportFacilitiesRecyclerView()
        showProgress()
    }

    private fun RFSupportFacilitiesRecyclerView() {
        viewModel.RFSupportFacilitiesAvailable.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        hideProgress()
                        it.wrappedList.firstOrNull()?.let { x ->
                            with(binding.rfSupportFacilitiesAvailableLayout) {
                                SafeDrinikingAavailable.text = (x.safeDrinking)
                                FirstAidKit.text = (x.firstAidKit)
                                FireFightingEquipmentr.text = (x.fireFighting)
                                BiometricDevice.text = (x.biometricDevice)
                                ElectricalPowerBackup.text = (x.powerBackup)
                                GrievanceRegister.text = (x.grievanceRegister)
                            }
                            RFsafeDrinkingeFile = x.safeDrinkingPdf
                            RFfirstAidKitFile = x.firstAidKitPdf
                            RFfireFightingFile = x.fireFightingPdf
                            RFbiometricDeviceFile = x.biometricDevicePdf
                            RFpowerBackupFile = x.powerBackupPdf
                            RFgrievanceRegisterFile = x.grievanceRegisterPdf
                        }
                    }
                    202 -> {
                        hideProgress()
                        toast("No data available.")
                    }
                    301 -> {
                        hideProgress()
                        toast("Please upgrade your app.")
                    }
                    401 -> {
                        hideProgress()
                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }
                }
                result.onFailure {
                    hideProgress()
                    toast("Failed: ${it.message}")
                }
            }
        }
    }

    // -----------------------------------------
    // DIALOG METHODS
    // -----------------------------------------

    private fun showRoomInformationDialog(x: LivingAreaInformation) {
        val dialogBinding = RoominformationPopdialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        dialog.show()

        //val noOfStudentPermitted = x.windowArea?.toDouble()?.div(25.0) ?: 0.0
        val noOfStudentPermitted = x.area?.toDouble()?.div(25.0)!!.roundHalfUp() ?: 0

        with(dialogBinding) {
            laiTypeOfRoof.text = safeText(x.roofType)
            laiFalseCelling.text = safeText(x.falseCeiling)
            laiHeightofCelling.text = safeText(x.ceilingHeight.toString())
            NoOfStudentPermitted.text = noOfStudentPermitted.toString()
            laiLength.text = safeText(x.length.toString())
            laiWidth.text = safeText(x.width.toString())
            laiArea.text = safeText(x.area.toString())
            laiwindowsArea.text = safeText(x.windowArea.toString())
            laiCotInNo.text = safeText(x.cot.toString())
            laiMattersInNo.text = safeText(x.mattress.toString())
            laiBedSheetInNo.text = safeText(x.bedSheet.toString())
            laiAirCondtion.text = safeText(x.airCondtion.toString())
            laiLights.text = safeText(x.lights.toString())
            laiStorage.text = safeText(x.storage.toString())
            LiaBasicInformationBoard.text = safeText(x.infoBoard.toString())

            LiaBasicInformationBoardFile.setOnClickListener {
                showBase64ImageDialog(requireContext(), "", "Room Preview")
            }
            laiTypeOfRoofFile.setOnClickListener {
                showBase64ImageDialog(requireContext(), x.roofTypePdf, "Room Preview")
            }
            laiFalseCellingFile.setOnClickListener {
                showBase64ImageDialog(requireContext(), x.falseCeilingPdf, "False Ceiling Preview")
            }
            laiHeightofCellingFile.setOnClickListener {
                showBase64ImageDialog(requireContext(), x.ceilingHeightPdf, "Ceiling Height Preview")
            }
            laiwindowsAreaFile.setOnClickListener {
                showBase64ImageDialog(requireContext(), x.windowAreaPdf, "Window Area Preview")
            }
            laiCotInNoFile.setOnClickListener {
                showBase64ImageDialog(requireContext(), x.cotPdf, "Cot Preview")
            }
            laiMattersInNoFile.setOnClickListener {
                showBase64ImageDialog(requireContext(), x.mattressPdf, "Mattress Preview")
            }
            laiBedSheetInNoFile.setOnClickListener {
                showBase64ImageDialog(requireContext(), x.bedSheetPdf, "Bed Sheet Preview")
            }
            laiAirCondtionFile.setOnClickListener {
                showBase64ImageDialog(requireContext(), x.airConditionPdf, "AirCondition Preview")
            }
            laiLightsFile.setOnClickListener {
                showBase64ImageDialog(requireContext(), x.lightPdf, "Light Preview")
            }
            laiStorageFile.setOnClickListener {
                showBase64ImageDialog(requireContext(), x.storagePdf, "Storage Preview")
            }
        }

        dialogBinding.backButton.setOnClickListener { dialog.dismiss() }
    }

    private fun showToiletInformationDialog(x: ToiletRoomInformationDataResponse) {
        val binding = TriPopdialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()

        with(binding) {
            triTypeOfFlooring.text = safeText(x.flooring)
            ConnectionToRunningWater.text = safeText(x.runningWater)
            ToiletType.text = safeText(x.type.toString())
            TriLights.text = safeText(x.lights.toString())

            laiTypeOfFlooringFile.setOnClickListener {
                showBase64ImageDialog(requireContext(), x.floorPdf, "Floor Preview")
            }
            TriLightsFile.setOnClickListener {
                showBase64ImageDialog(requireContext(), x.lightPdf, "Light Preview")
            }
            ConnectionToRunningWaterFile.setOnClickListener {
                showBase64ImageDialog(requireContext(), x.runningWaterFile, "Running WaterFile Preview")
            }
        }

        binding.backButton.setOnClickListener { dialog.dismiss() }
    }



    // -----------------------------------------
    // HELPER METHODS
    // -----------------------------------------

    private fun handleSpinnerSelection(
        selectedValue: String,
        setApproval: (String) -> Unit,
        remarksEditText: EditText,
        remarksTextView: TextView
    ) {
        remarksEditText.visibility = View.VISIBLE
        remarksTextView.visibility = View.VISIBLE
        if (selectedValue == "Send for modification") {
            setApproval("M")
            remarksTextView.text=requireContext().getString(R.string.remarkss)
        } else {
            setApproval("A")
            remarksTextView.text=requireContext().getString(R.string.remarkWitouStar)
        }
    }

    private fun validateApprovalAndRemarks(
        approval: String,
        currentRemarks: String,
        remarksEditText: EditText
    ): Boolean {
        if (approval.isEmpty()) {
            toast("Kindly select Approval first")
            return false
        }
        if (approval == "M") {
            val remarksText = remarksEditText.text.toString().trim()
            if (remarksText.isEmpty()) {
                toast("Kindly enter remarks first")
                return false
            }
        }
        return true
    }

    private fun showPreviousSection(
        currentView: View,
        previousSection: View,
        additionalView: View? = null
    ) {
        currentView.visibility = View.GONE
        previousSection.visibility = View.VISIBLE
        additionalView?.visibility = View.VISIBLE
        binding.btnSubmitFinal.visibility = View.GONE
    }

    // Update the hideAll method to be more flexible
    private fun hideAll() {
        listOf(
            binding.residentialfacilityqteamInfoLayout.RFQTInfoExpand,
            binding.infrastructureDetailsAndCompliancesLayout.IDetailsComplainExpand,
            binding.livingareainformationLayout.LivingAreaInformationExpand,
            binding.RFTioletLayout.toiletsExpand,
            binding.RFTioletAdditionalSectionLayout.AdditionalSectionExpand,
            binding.RFNonLivingAreaLayout.NonLivingAreaInfoExpand,
            binding.RFIndoorGameLayout.IndoorGameExpand,
            binding.RFResidentialFacilitiesAvailable.RFResidentialFacalityExpand,
            binding.rfSupportFacilitiesAvailableLayout.RFResidentialsupportFacalityExpand
        ).forEach { it.visibility = View.GONE }

        // Also hide the text view indicators
        listOf(
            binding.tvinfrastructureDetailsAndCompliances,
            binding.tvlivingareainformation,
            binding.tvRFTiolet,
            binding.tvRFtoiletAdditionalSection,
            binding.tvRFConstraintLayoutNonLivingArea,
            binding.tvRFConstraintLayoutIndoorGame,
            binding.RFResidentialConstraintLayoutFacilitiesAvailable,
            binding.RFRFSupportFacilitiesAvailable
        ).forEach { it.visibility = View.GONE }
    }

    private fun showSection(section: View, sectionIndicator: View? = null) {
        hideAll()
        section.visibility = View.VISIBLE
        sectionIndicator?.visibility = View.VISIBLE
    }

    private fun scrollToTop() {
        binding.scroll.post {
            binding.scroll.smoothScrollTo(0, 0)
        }
    }

    private fun handleErrorResponse(responseCode: Int) {
        when (responseCode) {
            202 -> toast("No data available.")
            301 -> toast("Please upgrade your app.")
            401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
        }
    }

    private fun showBase64ImageDialog(context: Context, base64ImageString: String?, title: String = "Image") {
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

        val dialog = ImagePreviewDialogFragment.newInstance(title, bitmap)
        dialog.show(parentFragmentManager, "ImagePreviewDialog")
    }

    private fun openBase64Pdf(context: Context, base64: String) {
        try {
            val cleanBase64 = base64
                .replace("data:application/pdf;base64,", "")
                .trim()
            val pdfBytes = Base64.decode(cleanBase64, Base64.DEFAULT)

            if (pdfBytes.isEmpty() || !String(pdfBytes.copyOfRange(0, 4)).startsWith("%PDF")) {
                toast("Invalid PDF data")
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
                toast("No PDF viewer installed")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast("Failed to open PDF")
        }
    }

    private fun safeText(value: String?): String {
        return if (value.isNullOrBlank() || value.equals("null", ignoreCase = true)) "N/A" else value
    }

     fun showProgress() {
        if (progress?.isShowing == false) progress?.show()
    }

     fun hideProgress() {
        if (progress?.isShowing == true) progress?.dismiss()
    }

    protected fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


    // -----------------------------------------
    // FINAL SUBMIT
    // -----------------------------------------

    open fun submitFinalForm() {}

    open fun collectFinalSubmitData() {}


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}