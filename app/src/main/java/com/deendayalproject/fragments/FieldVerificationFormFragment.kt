package com.deendayalproject.fragments

import SharedViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.RippleDrawable
import android.net.Uri
import android.os.Environment
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.base.BaseRecyclerAdapter
import com.deendayalproject.base.NoDataConfig
import com.deendayalproject.databinding.FragmentFieldVerFormBinding
import com.deendayalproject.databinding.ItemFieldVerCardBinding
import com.deendayalproject.databinding.ItemFinancialRowBinding
import com.deendayalproject.databinding.ItemPlacementRowBinding
import com.deendayalproject.databinding.ItemTrainingRowBinding
import com.deendayalproject.model.request.CaptivePiaOfficerSelfieRequest
import com.deendayalproject.model.request.FieldVerificationDetailRequest
import com.deendayalproject.model.request.FieldVerificationFinalSubmit
import com.deendayalproject.model.request.FieldVerificationFinalSubmitNEW
import com.deendayalproject.model.response.AnnualTurnover
import com.deendayalproject.model.response.AttachmentItem
import com.deendayalproject.model.response.FieldVerificationDetailResponse
import com.deendayalproject.model.response.FieldVerificationDetailResponseNEW
import com.deendayalproject.model.response.NetWorth
import com.deendayalproject.model.response.RemarkItem
import com.deendayalproject.model.response.TotalTrainingHoursRemark
import com.deendayalproject.model.response.TrainingCriteriaItem
import com.deendayalproject.model.response.YearlyFinancialItem
import com.deendayalproject.model.response.YearlyPlacementDetails
import com.deendayalproject.model.response.YearlyTrainingItem
import com.deendayalproject.model.response.toYearlyItem
import com.deendayalproject.model.response.toYearlyTrainingItem
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.toastShort
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.chip.Chip
import com.google.gson.GsonBuilder
import java.io.File
import java.io.IOException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



// ─────────────────────────────────────────────────────────────
//  Fragment
// ─────────────────────────────────────────────────────────────

class FieldVerificationFormFragment : BaseFragment<FragmentFieldVerFormBinding>(
    FragmentFieldVerFormBinding::inflate
) {

    // ── ViewModel ────────────────────────────────────────────

    private val viewModel: SharedViewModel by activityViewModels()

    // ── Navigation args ──────────────────────────────────────

    private var captiveEmpanelmentId = ""
    private var prnNo = ""

    // ── Officer verification state ───────────────────────────

    private var officerSelfieBase64: String? = null
    private var isSelfieVerificationDone = false

    // ── Location state ───────────────────────────────────────

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: String = ""
    private var longitude: String = ""
    private var tcLatitude: String = ""
    private var tcLongitude: String = ""
    private var rfLatitude: String = ""
    private var rfLongitude: String = ""
    private var tcRfDistance: Float = 0f
    private var isOfficerWithinRange = false

    private companion object {
        const val MAX_ALLOWED_DISTANCE = 500f
        const val TAG = "FieldVerificationForm"
    }

    // ── Camera / permission launchers ────────────────────────

    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var photoUri: Uri
    private var currentPhotoTarget: String = ""
    private var currentUploadPosition: Int = -1
    private var currentUploadList: String = ""

    // ── Section item lists ───────────────────────────────────

    private var orgItems: MutableList<FieldVerificationItem> = mutableListOf()
    private var finItems: MutableList<FieldVerificationItem> = mutableListOf()
    private var trainingItems: MutableList<FieldVerificationItem> = mutableListOf()
    private var trainingInfraItems: MutableList<FieldVerificationItem> = mutableListOf()
    private var residentialFacilityItems: MutableList<FieldVerificationItem> = mutableListOf()
    private var certItems: MutableList<FieldVerificationItem> = mutableListOf()
    private var placementItems: MutableList<FieldVerificationItem> = mutableListOf()
    private var fieldItems: MutableList<FieldVerificationItem> = mutableListOf()

    // ── RecyclerView adapters ─────────────────────────────────

    private lateinit var orgAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>
    private lateinit var finAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>
    private lateinit var trainingAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>
    private lateinit var trainingInfraAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>
    private lateinit var residentialFacilityAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>
    private lateinit var certAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>
    private lateinit var placementAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>
    private lateinit var fieldAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>

    // ── API response cache — Organisation ────────────────────

    private var apiDateOfIncorporation: String? = null
    private var durationOfOrg: String? = null
    private var factoryRegistrationAttachment: String? = null
    private var apiBankName: String? = null
    private var apiEpfoExistingStaff: String? = null
    private var apiEpfoDocumentUrl: String? = ""
    private var apiGstNumber: String? = null
    private var apiTanNumber: String? = null
    private var apiTanAttachmentBase64: String? = null
    private var apiBankAccountNumber: String? = null
    private var apiBankLetterBase64: String? = null
    private var apiBankAccountPassbook: String? = null
    private var ifscCode: String? = null
    private var apiSelfDeclarationBase64: String? = null
    private var apiEpfoNumber: String? = null
    private var apiEsicNumber: String? = null
    private var apiFactoryRegNumber: String? = null
    private var apiEpfoAttachmentBase64: String? = null
    private var apiEsicAttachmentBase64: String? = null
    private var apiFactoryAttachmentBase64: String? = null

    // ── API response cache — Finance ─────────────────────────

    private var apiAnnualTurnoverList: List<AnnualTurnover>? = null
    private var apiNetWorthList: List<NetWorth>? = null

    // ── API response cache — Training ────────────────────────

    private var apiTrainingCriteriaList: List<TrainingCriteriaItem>? = null
    private var apiTotalTrainingHoursRemarks: List<TotalTrainingHoursRemark>? = null
    private var repetitionClubbingIfraNsqf: String? = null
    private var apiBasicSelfDeclarationBase64: String? = null
    private var apiCommitmentForm1Base64: String? = null
    private var apiCommitmentForm2Base64: String? = null
    private var apiTailorTrainingDocBase64: String? = null
    private var apiDomainForm1Base64: String? = null
    private var apiDomainForm2Base64: String? = null

    // ── API response cache — Training Infra ──────────────────

    private var apiResidentialFacilityAvailable: String? = null
    private var apiResidentialFacilityDocumentBase64: String? = null

    // ── API response cache — Certification ───────────────────

    private var apiAwardBodyCommitBase64: String? = null
    private var apiSeventyPctCommitBase64: String? = null

    // ── API response cache — Placement ───────────────────────

    private var apiPlacementList: List<YearlyPlacementDetails>? = null
    private var apiCommitmentSixMonthsBase64: String? = null
    private var apiCommitmentLessSixMonthsBase64: String? = null
    private var apiCommitmentMoreSixMonthsBase64: String? = null

    // ─────────────────────────────────────────────────────────
    //  BaseFragment lifecycle hooks
    // ─────────────────────────────────────────────────────────

    override fun initializeViews() {
        Log.d(TAG, "initializeViews()")
        captiveEmpanelmentId = arguments?.getString("captiveEmpanelmentId").orEmpty()
        prnNo = arguments?.getString("prnNo").orEmpty()

        setupCameraLauncher()
        setupRecyclerViews()
        setupLocationClient()
        hideAllSections()
        showOfficerVerificationDialog()
    }

    override fun setupObservers() {
        observeOrgDetails()
    }

    override fun setupClickListeners() {
        setupToolbar()
        setupNavigationButtons()
        setupNextButtons()
    }

    override fun loadInitialData() {
        viewModel.getFieldVerificationDetail(buildDetailRequest())
    }

    // ─────────────────────────────────────────────────────────
    //  Toolbar
    // ─────────────────────────────────────────────────────────

    private fun setupToolbar() {
        setupToolbar(
            root = binding.root,
            titleRes = R.string.field_ver_head,
            showBack = true,
            showLang = false,
            showProfile = false,
            backClick = { findNavController().navigateUp() })
    }

    // ─────────────────────────────────────────────────────────
    //  Section visibility helpers
    // ─────────────────────────────────────────────────────────

    private fun hideAllSections() {
        binding.verOrg.visibility = View.GONE
        binding.verFin.visibility = View.GONE
        binding.verTraining.visibility = View.GONE
        binding.verTrainingInfra.visibility = View.GONE
        binding.verCert.visibility = View.GONE
        binding.verPlacement.visibility = View.GONE
        binding.verField.visibility = View.GONE
    }

    private fun hideAllExpandLayouts() {
        binding.trainingInfraExpand.visibility = View.GONE
        binding.verFinExpand.visibility = View.GONE
        binding.verTrainingExpand.visibility = View.GONE
        binding.verTrainingInfraExpand.visibility = View.GONE
        binding.verCertExpand.visibility = View.GONE
        binding.verPlacementExpand.visibility = View.GONE
        binding.verFieldExpand.visibility = View.GONE
        binding.verResidentialFacilityExpand.visibility = View.GONE
    }

    private fun scrollToTop() {
        binding.scroll.post { binding.scroll.smoothScrollTo(0, 0) }
    }

    // ─────────────────────────────────────────────────────────
    //  Navigation buttons (Previous)
    // ─────────────────────────────────────────────────────────

    private fun setupNavigationButtons() {
        binding.btnFinPrevious.setOnClickListener { navigateBack(SectionTag.FIN) }
        binding.btnTrainingPrevious.setOnClickListener { navigateBack(SectionTag.TRAINING) }
        binding.btnTrainingInfraPrevious.setOnClickListener { navigateBack(SectionTag.TRAINING_INFRA) }
        binding.btnResidentialFacilityPrevious.setOnClickListener { navigateBack(SectionTag.RESIDENTIAL) }
        binding.btnCertPrevious.setOnClickListener { navigateBack(SectionTag.CERT) }
        binding.btnPlacementPrevious.setOnClickListener { navigateBack(SectionTag.PLACEMENT) }
        binding.btnFieldPrevious.setOnClickListener { navigateBack(SectionTag.FIELD) }
    }

    private fun navigateBack(currentSection: String) {
        hideAllExpandLayouts()
        when (currentSection) {
            SectionTag.FIN -> {
                binding.verOrg.visibility = View.VISIBLE
                binding.trainingInfraExpand.visibility = View.VISIBLE
            }

            SectionTag.TRAINING -> {
                binding.verFin.visibility = View.VISIBLE
                binding.verFinExpand.visibility = View.VISIBLE
            }

            SectionTag.TRAINING_INFRA -> {
                binding.verTraining.visibility = View.VISIBLE
                binding.verTrainingExpand.visibility = View.VISIBLE
            }

            SectionTag.RESIDENTIAL -> {
                binding.verTrainingInfra.visibility = View.VISIBLE
                binding.verTrainingInfraExpand.visibility = View.VISIBLE
            }

            SectionTag.CERT -> {
                binding.verResidentialFacility.visibility = View.VISIBLE
                binding.verResidentialFacilityExpand.visibility = View.VISIBLE
            }

            SectionTag.PLACEMENT -> {
                binding.verCert.visibility = View.VISIBLE
                binding.verCertExpand.visibility = View.VISIBLE
            }

            SectionTag.FIELD -> {
                binding.verPlacement.visibility = View.VISIBLE
                binding.verPlacementExpand.visibility = View.VISIBLE
            }
        }
        scrollToTop()
    }

    // ─────────────────────────────────────────────────────────
    //  Next / Submit buttons
    // ─────────────────────────────────────────────────────────

    private fun setupNextButtons() {
        binding.btnInfoNext.setOnClickListener {
            onOrgNext()
            logSectionGson(Requirement.ORG, orgItems)
        }
        binding.btnFinNext.setOnClickListener { onFinNext()
            logSectionGson(Requirement.FIN, finItems)}
        binding.btnTrainingNext.setOnClickListener { onTrainingNext()
            captureTrainingCentreLocation { updateFieldDistance() }
            logSectionGson(Requirement.TRAINING, trainingItems)}
        binding.btnTrainingInfraNext.setOnClickListener { onTrainingInfraNext()
            logSectionGson(Requirement.INFRA, trainingInfraItems)
        }
        binding.btnResidentialFacilityNext.setOnClickListener {
            captureResidentialFacilityLocation { updateFieldDistance() }
            onResidentialNext()
            logSectionGson(Requirement.INFRA, residentialFacilityItems)}
        binding.btnCertNext.setOnClickListener { onCertNext()
            logSectionGson(Requirement.CERT, certItems)}
        binding.btnPlacementNext.setOnClickListener { onPlacementNext()
            logSectionGson(Requirement.PLACEMENT, placementItems)
        }
        binding.btnFieldNext.setOnClickListener { onFieldFinalSubmit()
            logSectionGson(Requirement.FIELD, fieldItems)}
    }

    // ── Organisation ─────────────────────────────────────────

    private fun onOrgNext() {
        if (!isSelfieVerificationDone) {
            showToast("Please complete officer verification first")
            return
        }
        if (!validateSectionItems(binding.recyclerView, orgItems)) return

        markSectionComplete(
            headerView = binding.tvTrainInfra,
            expandToHide = binding.trainingInfraExpand,
            nextSection = binding.verFin,
            nextExpand = binding.verFinExpand
        )

        val request = FieldVerificationFinalSubmitNEW(

            appVersion = BuildConfig.VERSION_NAME,

            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),

            captiveEmpanelmentId = captiveEmpanelmentId,

            prnNo = prnNo,

            data = collectSectionRemarksNew("ORGANIZATION", orgItems)
        )

        submitSection(request = request,

            headerView = binding.tvTrainInfra,

            expandToHide = binding.trainingInfraExpand,

            nextSection = binding.verFin,

            nextExpand = binding.verFinExpand,

            onSuccess = {

                viewModel.getFieldVerificationFinDetail(
                    buildDetailRequest()
                )

                observeFinDetails()

                if (hasLocationPermission()) {

                    getCurrentLocation()

                } else {

                    requestLocationPermission()
                }
            })

        //collectSectionRemarksNew("ORGANIZATION", orgItems)
        val gson = GsonBuilder().setPrettyPrinting().create()
        Log.d("ORGANIZATION NEW ─────────────> Data", gson.toJson(request))


        viewModel.getFieldVerificationFinDetail(buildDetailRequest())
        observeFinDetails()

        if (hasLocationPermission()) getCurrentLocation() else requestLocationPermission()
    }



    // ── Finance ──────────────────────────────────────────────

    private fun onFinNext() {
        if (!validateSectionItems(binding.recyclerViewFin, finItems)) return
        markSectionComplete(
            headerView = binding.tvFinHead,
            expandToHide = binding.verFinExpand,
            nextSection = binding.verTraining,
            nextExpand = binding.verTrainingExpand
        )

        viewModel.getFieldVerificationTrainingDetail(buildDetailRequest())
        observeTrainingDetails()
    }

    // ── Training ─────────────────────────────────────────────

    private fun onTrainingNext() {
        commitFocusedEditText()
        logSectionGson("Finance", finItems)
        if (!validateSectionItems(binding.recyclerViewTraining, trainingItems)) return
        markSectionComplete(
            headerView = binding.tvTrainingHead,
            expandToHide = binding.verTrainingExpand,
            nextSection = binding.verTrainingInfra,
            nextExpand = binding.verTrainingInfraExpand
        )

        viewModel.getFieldVerificationTrainingInfraDetail(buildDetailRequest())
        observeTrainingInfraDetails()
    }

    // ── Training Infra ───────────────────────────────────────

    private fun onTrainingInfraNext() {
        if (!validateSectionItems(binding.recyclerViewTrainingInfra, trainingInfraItems)) return
        logSectionGson("Residential", residentialFacilityItems)

        markSectionComplete(
            headerView = binding.tvTrainingInfraHead,
            expandToHide = binding.verTrainingInfraExpand,
            nextSection = binding.verResidentialFacility,
            nextExpand = binding.verResidentialFacilityExpand
        )
    }

    // ── Residential Facility ─────────────────────────────────

    private fun onResidentialNext() {
        commitFocusedEditText()
        if (!validateSectionItems(binding.recyclerViewResidentialFacility, residentialFacilityItems)) return        markSectionComplete(
            headerView = binding.tvResidentialFacilityHead,
            expandToHide = binding.verResidentialFacilityExpand,
            nextSection = binding.verCert,
            nextExpand = binding.verCertExpand
        )

        viewModel.getFieldVerificationCertificationDetail(buildDetailRequest())
        observeCertificationDetails()
    }

    // ── Certification ────────────────────────────────────────

    private fun onCertNext() {
        if (!validateSectionItems(binding.recyclerViewCert, certItems)) return
        markSectionComplete(
            headerView = binding.tvCertHead,
            expandToHide = binding.verCertExpand,
            nextSection = binding.verPlacement,
            nextExpand = binding.verPlacementExpand
        )
        viewModel.getFieldVerificationPlacementDetail(buildDetailRequest())
        observePlacementDetails()
    }

    // ── Placement ────────────────────────────────────────────

    private fun onPlacementNext() {
        if (!validateSectionItems(binding.recyclerViewPlacement, placementItems)) return
        markSectionComplete(
            headerView = binding.tvPlacementHead,
            expandToHide = binding.verPlacementExpand,
            nextSection = binding.verField,
            nextExpand = binding.verFieldExpand
        )
    }

    // ── Field Visit (Final Submit) ────────────────────────────
    private fun onFieldFinalSubmit() {
        if (officerSelfieBase64.isNullOrBlank()) {
            showToast("Please capture officer selfie")
            return
        }
        binding.verFieldExpand.visibility = View.GONE
        binding.tvFieldHead.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
        val finalPayload = buildFinalSubmitPayload()
        logJson("FINAL_SUBMIT_JSON", finalPayload)
        showProgressBar()
        viewModel.submitFieldVerificationDetails.removeObservers(viewLifecycleOwner)
        viewModel.submitFieldVerification(finalPayload)
        viewModel.submitFieldVerificationDetails.observe(viewLifecycleOwner) { result ->
            hideProgressBar()
            result.onSuccess { handleFinalSubmitSuccess(it) }
                .onFailure { handleFinalSubmitFailure(it) }
        }
    }

    private fun submitSection(
        request: FieldVerificationFinalSubmitNEW,

        expandView: View,

        headerView: TextView,

        nextSectionView: View? = null,

        nextExpandView: View? = null

    ) {

        showProgressBar()

        viewModel.submitFieldVerificationDetailsNEW
            .removeObservers(viewLifecycleOwner)

        viewModel.submitFieldVerificationNew(request)

        viewModel.submitFieldVerificationDetailsNEW
            .observe(viewLifecycleOwner) { result ->

                hideProgressBar()

                result.onSuccess { response ->

                    handleSectionSuccess(

                        response = response,

                        expandView = expandView,

                        headerView = headerView,

                        nextSectionView = nextSectionView,

                        nextExpandView = nextExpandView
                    )
                }.onFailure {

                    handleSectionFailure(it)
                }
            }
    }
//
    private fun handleSectionSuccess(

        response: FieldVerificationDetailResponseNEW,

        expandView: View,

        headerView: TextView,

        nextSectionView: View?,

        nextExpandView: View?

    ) {

        val item = response.wrappedList.firstOrNull()

        if (

            response.responseCode == 200 &&
            item?.completed == true

        ) {

            toastShort(
                response.responseDesc
                    ?: "Section submitted successfully"
            )

            // Hide current section
            expandView.visibility = View.GONE

            // Show verified icon
            headerView.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )

            // Open next section
            nextSectionView?.visibility = View.VISIBLE

            nextExpandView?.visibility = View.VISIBLE

        } else {

            val pendingMessage = if (
                item?.pendingFields?.isNotEmpty() == true
            ) {

                "\nPending : ${
                    item.pendingFields.joinToString()
                }"

            } else {
                ""
            }

            showToast(
                response.responseDesc.orEmpty() + pendingMessage
            )
        }
    }
//
    private fun handleSectionFailure(
        throwable: Throwable
    ) {

        hideProgressBar()

        showToast(
            throwable.message
                ?: "Something went wrong"
        )
    }

    private fun handleFinalSubmitSuccess(response: FieldVerificationDetailResponse?) {
        // Assuming response has responseCode and responseDesc (adjust type as needed)
        try {
            val r = response ?: throw Exception("Response is null")
            if (r.responseCode.toInt() == 200) {
                toastShort(r.responseDesc ?: "Field Verification submitted successfully")
                Log.d(TAG, "Submit success: ${r.responseDesc}")
                val nav = findNavController()
                nav.previousBackStackEntry?.savedStateHandle?.set("refresh_pia_list", true)
                nav.navigateUp()
            } else {
                binding.verFieldExpand.visibility = View.VISIBLE
                toastShort(r.responseDesc ?: "Failed to submit field verification")
                Log.e(TAG, "Submit failed: ${r.responseCode}")
            }
        } catch (e: Exception) {
            binding.verFieldExpand.visibility = View.VISIBLE
            e.printStackTrace()
            toastShort("Something went wrong while processing response")
            Log.e(TAG, "Exception: ${e.message}")
        }
    }

    private fun handleFinalSubmitFailure(error: Throwable) {
        hideProgressBar()
        binding.verFieldExpand.visibility = View.VISIBLE
        toastShort(error.message ?: "Unable to submit field verification")
        Log.e(TAG, "API Error: ${error.message}")
    }

    // ─────────────────────────────────────────────────────────
    //  Reusable section-advance helper
    // ─────────────────────────────────────────────────────────

    private fun markSectionComplete(
        headerView: TextView, expandToHide: View, nextSection: View, nextExpand: View
    ) {
        headerView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
        expandToHide.visibility = View.GONE
        nextSection.visibility = View.VISIBLE
        nextExpand.visibility = View.VISIBLE
        scrollToTop()
    }

    // ─────────────────────────────────────────────────────────
    //  RecyclerView setup
    // ─────────────────────────────────────────────────────────

    private fun setupRecyclerViews() {
        orgItems = buildOrgItems()
        finItems = buildFinItems()
        trainingItems = buildTrainingItems()
        trainingInfraItems = buildTrainingInfraItems()
        residentialFacilityItems = buildResidentialFacilityItems()
        certItems = buildCertItems()
        placementItems = buildPlacementItems()
        fieldItems = buildFieldItems()

        orgAdapter = buildSectionAdapter(binding.recyclerView, orgItems, SectionTag.ORG)
        finAdapter = buildSectionAdapter(binding.recyclerViewFin, finItems, SectionTag.FIN)
        trainingAdapter =
            buildSectionAdapter(binding.recyclerViewTraining, trainingItems, SectionTag.TRAINING)
        trainingInfraAdapter = buildSectionAdapter(
            binding.recyclerViewTrainingInfra,
            trainingInfraItems,
            SectionTag.TRAINING_INFRA
        )
        residentialFacilityAdapter = buildSectionAdapter(
            binding.recyclerViewResidentialFacility,
            residentialFacilityItems,
            SectionTag.RESIDENTIAL
        )
        certAdapter = buildSectionAdapter(binding.recyclerViewCert, certItems, SectionTag.CERT)
        placementAdapter =
            buildSectionAdapter(binding.recyclerViewPlacement, placementItems, SectionTag.PLACEMENT)
        fieldAdapter = buildSectionAdapter(
            binding.recyclerViewField,
            fieldItems,
            SectionTag.FIELD,
            showIcons = false
        )
    }

    private fun buildSectionAdapter(
        recyclerView: RecyclerView,
        items: MutableList<FieldVerificationItem>,
        section: String,
        showIcons: Boolean = true
    ): BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding> {
        return setupRecyclerView(
            recyclerView = recyclerView,
            items = items,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = ItemFieldVerCardBinding::inflate,
            onBind = { item, b, pos -> bindCardItem(item, b, pos, section, showIcons) },
            onItemClick = { _, _ -> },
            noDataConfig = NoDataConfig(
                title = "No items", description = "No $section verification items to display"
            )
        )
    }

    // ─────────────────────────────────────────────────────────
    //  Card binding
    // ─────────────────────────────────────────────────────────

    private fun bindCardItem(
        item: FieldVerificationItem,
        b: ItemFieldVerCardBinding,
        position: Int,
        section: String,
        showIcons: Boolean
    ) {
        b.tvReqTitle.text = item.requirement
        b.tvVerification.text = item.verificationDoc

        b.chipgroupDocuments.removeAllViews()
        item.documents.forEach { doc ->
            b.chipgroupDocuments.addView(buildDocChip(item, doc, section, position, showIcons))
        }

        bindImagePreview(item, b)
        bindRemarkInput(item, b)
    }

    private fun buildDocChip(
        item: FieldVerificationItem, doc: String, section: String, position: Int, showIcons: Boolean
    ): Chip {
        return Chip(requireContext()).apply {
            text = doc
            isClickable = true
            isCheckable = false

            if (showIcons) {
                val iconRes = if (item.uploadEnabled) R.drawable.file else R.drawable.ic_up
                closeIcon = context.getDrawable(iconRes)
                closeIconTint = context.getColorStateList(android.R.color.darker_gray)
                isCloseIconVisible = true
                iconEndPadding = 8f
                textEndPadding = 16f
            } else {
                isCloseIconVisible = false
            }

            setOnClickListener {
                if (item.uploadEnabled) onUploadChipClick(section, position, doc)
                else onViewChipClick(section, position, doc)
            }
        }
    }

    private fun bindImagePreview(item: FieldVerificationItem, b: ItemFieldVerCardBinding) {
        if (!item.imageUri.isNullOrBlank()) {
            try {
                b.imageGroup.visibility = View.VISIBLE
                val uri = Uri.parse(item.imageUri)
                if (currentPhotoTarget == "Training Centre") {
                    b.ivPreview1.setImageURI(uri); b.ivPreview1.visibility = View.VISIBLE
                    b.ivPreview.visibility = View.GONE
                } else {
                    b.ivPreview.setImageURI(uri); b.ivPreview.visibility = View.VISIBLE
                    b.ivPreview1.visibility = View.GONE
                }
            } catch (e: Exception) {
                b.ivPreview.visibility = View.GONE
                b.ivPreview1.visibility = View.GONE
                b.imageGroup.visibility = View.GONE
            }
        } else {
            b.ivPreview.visibility = View.GONE
            b.ivPreview1.visibility = View.GONE
            b.imageGroup.visibility = View.GONE
        }
    }

    private fun bindRemarkInput(item: FieldVerificationItem, b: ItemFieldVerCardBinding) {
        if (!item.allowRemark) {
            b.remarkGroup.visibility = View.GONE
            removeExistingWatcher(b)
            return
        }

        b.remarkGroup.visibility = View.VISIBLE
        val current = item.remarkText.orEmpty()
        if (b.etSectionRemark.text?.toString() != current) b.etSectionRemark.setText(current)

        removeExistingWatcher(b)

        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                item.remarkText = s?.toString()?.trim()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        b.etSectionRemark.addTextChangedListener(watcher)
        b.etSectionRemark.tag = watcher
    }

    private fun removeExistingWatcher(b: ItemFieldVerCardBinding) {
        (b.etSectionRemark.tag as? TextWatcher)?.let {
            b.etSectionRemark.removeTextChangedListener(it)
        }
        b.etSectionRemark.tag = null
    }

    // ─────────────────────────────────────────────────────────
    //  Chip click dispatch
    // ─────────────────────────────────────────────────────────

    private fun onUploadChipClick(section: String, position: Int, doc: String) {
        currentUploadPosition = position
        currentUploadList = section
        currentPhotoTarget = resolvePhotoTarget(section, doc)
        checkAndLaunchCamera()
    }

    private fun resolvePhotoTarget(section: String, doc: String): String {
        return when {
            section == SectionTag.FIN && doc == resources.getString(R.string.fin_turnover_button) -> "Turnover"
            section == SectionTag.TRAINING && doc == resources.getString(R.string.train_tailor_button) -> "Additional tailor-made training If Yes Upload"
            section == SectionTag.TRAINING_INFRA && doc == "Self Declaration" -> "Self Declaration"
            section == SectionTag.TRAINING_INFRA && doc == "Training Centre" -> "Training Centre"
            section == SectionTag.FIELD && doc == "Capture Selfie" -> SectionTag.OFFICER_SELFIE
            else -> doc
        }
    }

    private fun onViewChipClick(section: String, position: Int, doc: String) {
        when (section) {
            SectionTag.ORG -> handleOrgView(position, doc)
            SectionTag.FIN -> handleFinView(position, doc)
            SectionTag.TRAINING -> handleTrainingView(position, doc)
            SectionTag.TRAINING_INFRA -> handleTrainingInfraView(position, doc)
            SectionTag.CERT -> handleCertView(position, doc)
            SectionTag.PLACEMENT -> handlePlacementView(position, doc)
        }
    }

    // ─────────────────────────────────────────────────────────
    //  View-chip handlers per section
    // ─────────────────────────────────────────────────────────

    private fun handleOrgView(position: Int, doc: String) {
        when {
            position == 0 && doc == "Date of Incorporation (PRN)" -> showIndustryIncorporationDialog()
            position == 1 && doc == "View Registration Document" -> showIndustryRegistrationDialog()
            position == 2 && doc == "EPFO Challan (6 Months)" -> showEpfoChallanDialog()
            position == 3 && doc == "View" -> showTaxDetailsDialog()
            position == 4 && doc == "View Account Details" -> showBankDetailsDialog()
        }
    }

    private fun handleFinView(position: Int, doc: String) {
        when {
            position == 0 && doc == resources.getString(R.string.fin_balance_sheet_button) -> showFinancialDialog(
                resources.getString(R.string.fin_annual_turnover),
                apiAnnualTurnoverList?.map { it.toYearlyItem() })

            position == 1 && doc == resources.getString(R.string.fin_turnover_button) -> showFinancialDialog(
                resources.getString(R.string.fin_net_worth),
                apiNetWorthList?.map { it.toYearlyItem() })
        }
    }

    private fun handleTrainingView(position: Int, doc: String) {
        when {
            position == 0 && doc == resources.getString(R.string.train_target_button) -> showTrainingDialog(
                "Training Details",
                apiTrainingCriteriaList?.map { it.toYearlyTrainingItem() })

            position == 1 && doc == resources.getString(R.string.train_hour_button) -> showTrainingHoursDialog(
                "Training Hours",
                apiTotalTrainingHoursRemarks
            )

            position == 3 && doc == resources.getString(R.string.train_NSQF_course_button) -> showDocumentDialog(
                "Basic Training",
                apiBasicSelfDeclarationBase64,
                doc
            )

            position == 4 && doc == resources.getString(R.string.train_commitment1_button) -> showDocumentDialog(
                "Captive Employers Commitment",
                apiCommitmentForm1Base64,
                doc
            )

            position == 4 && doc == resources.getString(R.string.train_commitment2_button) -> showDocumentDialog(
                "Captive Employers Commitment",
                apiCommitmentForm2Base64,
                doc
            )

            position == 5 && doc == resources.getString(R.string.train_tailor_button) -> showDocumentDialog(
                "Tailor Training Doc",
                apiTailorTrainingDocBase64,
                doc
            )

            position == 6 && doc == resources.getString(R.string.train_domain1_button) -> showDocumentDialog(
                "Domain Specific Training",
                apiDomainForm1Base64,
                doc
            )

            position == 6 && doc == resources.getString(R.string.train_domain2_button) -> showDocumentDialog(
                "Domain Specific Training",
                apiDomainForm2Base64,
                doc
            )
        }
    }

    private fun handleTrainingInfraView(position: Int, doc: String) {
        if (position == 1 && doc == "View Residential Facilities") showResidentialFacilitiesDialog()
    }

    private fun handleCertView(position: Int, doc: String) {
        when {
            position == 0 && doc == "Form 4" -> showDocumentDialog(
                "Awarding Body",
                apiAwardBodyCommitBase64,
                "View Form 4"
            )

            position == 1 && doc == "Form 4" -> showDocumentDialog(
                "Certification for 70% Candidates",
                apiSeventyPctCommitBase64,
                "View Form 4"
            )
        }
    }

    private fun handlePlacementView(position: Int, doc: String) {
        when {
            position == 0 && doc == "View Employment Details" -> showPlacementDialog(
                "Placement Details",
                apiPlacementList
            )

            position == 1 && doc == "Form 1" -> showDocumentDialog(
                "Six Months",
                apiCommitmentSixMonthsBase64,
                "View Form 1"
            )

            position == 2 && doc == "Form 1" -> showDocumentDialog(
                "Commitment Less than Six Months",
                apiCommitmentLessSixMonthsBase64,
                "View Form 1"
            )

            position == 3 && doc == "Form 1" -> showDocumentDialog(
                "Commitment Greater than Six Months",
                apiCommitmentMoreSixMonthsBase64,
                "View Form 1"
            )
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Observers
    // ─────────────────────────────────────────────────────────

    private fun observeOrgDetails() {
        viewModel.fieldDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                val item = response.wrappedList.firstOrNull()
                val org = item?.organizationDetails
                durationOfOrg = org?.proofOfIndustryExistence?.durationOfOrg
                factoryRegistrationAttachment =
                    org?.proofOfIndustryExistence?.factoryRegistrationAttachment
                apiDateOfIncorporation = org?.proofOfIndustryExistence?.dateOfIncorporation
                apiBankName = org?.bankDetails?.bankName
                apiEpfoExistingStaff = org?.epfoChallans?.existingStaffRegisteredInEpfo
                apiEpfoDocumentUrl = org?.epfoChallans?.epfoDocument
                apiGstNumber = org?.taxDetails?.gstNumber
                apiTanNumber = org?.taxDetails?.tanNumber
                apiTanAttachmentBase64 = org?.taxDetails?.tanAttachment
                apiBankAccountNumber = org?.bankDetails?.bankAccountNumber
                apiBankLetterBase64 = org?.bankDetails?.bankLetterDocument
                apiBankAccountPassbook = org?.bankDetails?.bankAccountPassbook
                ifscCode = org?.bankDetails?.ifscCode
                apiSelfDeclarationBase64 = org?.bankDetails?.selfDeclarationDocument
                apiEpfoNumber = org?.industryRegistration?.epfoNumber
                apiEsicNumber = org?.industryRegistration?.esicNumber
                apiFactoryRegNumber = org?.industryRegistration?.factoryRegistrationNumber
                apiEpfoAttachmentBase64 = org?.industryRegistration?.epfoAttachment
                apiEsicAttachmentBase64 = org?.industryRegistration?.esicAttachment
                apiFactoryAttachmentBase64 =
                    org?.industryRegistration?.factoryRegistrationAttachment
                Log.d(TAG, "Org loaded — DOI=$apiDateOfIncorporation  EPFO=$apiEpfoNumber")
            }.onFailure {
                showErrorToast(it.message ?: getString(R.string.failed_to_fetch_details))
            }
        }
    }

    private fun observeFinDetails() {
        viewModel.finDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                try {
                    val fin = response.wrappedList?.firstOrNull()?.financialDetails
                    apiAnnualTurnoverList = fin?.annualTurnover
                    apiNetWorthList = fin?.netWorth
                    Log.d(TAG, "Finance loaded — turnover count: ${fin?.annualTurnover?.size}")
                } catch (e: Exception) {
                    showErrorToast(getString(R.string.failed_handling_response, e.message))
                }
            }.onFailure {
                showErrorToast(getString(R.string.api_error, it.message ?: "Unknown"))
            }
        }
    }

    private fun observeTrainingDetails() {
        viewModel.trainingDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                try {
                    val training = response.wrappedList?.firstOrNull()?.trainingDetails
                    apiTrainingCriteriaList = training?.trainingCriteria
                    apiTotalTrainingHoursRemarks = training?.totalTrainingHoursRemarks
                    repetitionClubbingIfraNsqf = training?.repetitionClubbingIfraNsqf
                    apiBasicSelfDeclarationBase64 =
                        training?.basicTraining?.selfDeclarationTrainingDoc
                    apiCommitmentForm1Base64 = training?.commitment?.form1
                    apiCommitmentForm2Base64 = training?.commitment?.form2
                    apiTailorTrainingDocBase64 = training?.trainingPlacement?.tailorTrainingDoc
                    apiDomainForm1Base64 = training?.domainSpecificTraining?.form1
                    apiDomainForm2Base64 = training?.domainSpecificTraining?.form2
                    updateRecyclerViewData(binding.recyclerViewTraining.id, trainingItems)
                } catch (e: Exception) {
                    showErrorToast(
                        getString(
                            R.string.failed_processing_training_response,
                            e.message
                        )
                    )
                }
            }.onFailure {
                showErrorToast(
                    getString(
                        R.string.training_api_failed,
                        it.message ?: getString(R.string.unknown)
                    )
                )
            }
        }
    }

    private fun observeTrainingInfraDetails() {
        viewModel.trainingInfraDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                try {
                    val residential =
                        response.wrappedList?.firstOrNull()?.trainingInfrastrutureDetails?.residentialFacilityDetails
                    apiResidentialFacilityAvailable = residential?.residentialFacilityAvailable
                    apiResidentialFacilityDocumentBase64 = residential?.residentialFacilityDocument
                    Log.d(TAG, "Infra loaded — residential=${apiResidentialFacilityAvailable}")
                } catch (e: Exception) {
                    showErrorToast(
                        getString(
                            R.string.failed_processing_training_infra_response,
                            e.message
                        )
                    )
                }
            }.onFailure {
                showErrorToast(
                    getString(
                        R.string.training_infra_api_failed,
                        it.message ?: getString(R.string.unknown)
                    )
                )
            }
        }
    }

    private fun observeCertificationDetails() {
        viewModel.certificationDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                try {
                    val commitment =
                        response.wrappedList?.firstOrNull()?.assessmentCertificationDetails?.commitmentLetterDetails
                    apiAwardBodyCommitBase64 = commitment?.awardBodyCommit
                    apiSeventyPctCommitBase64 = commitment?.seventyPctCommit
                    Log.d(
                        TAG,
                        "Cert loaded — awardBody=${!apiAwardBodyCommitBase64.isNullOrBlank()}"
                    )
                } catch (e: Exception) {
                    showErrorToast(
                        getString(
                            R.string.failed_processing_assessment_certification_response,
                            e.message
                        )
                    )
                }
            }.onFailure {
                showErrorToast(
                    getString(
                        R.string.assessment_certification_api_failed,
                        it.message ?: getString(R.string.unknown)
                    )
                )
            }
        }
    }

    private fun observePlacementDetails() {
        viewModel.placementDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                try {
                    val placement = response.wrappedList?.firstOrNull()?.placementDetails
                    apiPlacementList = placement?.yearWisePlacementDetails
                    apiCommitmentSixMonthsBase64 = placement?.commitment?.commitmentSixMonths
                    apiCommitmentLessSixMonthsBase64 =
                        placement?.commitment?.commitmentLessSixMonths
                    apiCommitmentMoreSixMonthsBase64 =
                        placement?.commitment?.commitmentMoreSixMonths
                    Log.d(TAG, "Placement loaded — list size=${apiPlacementList?.size}")
                } catch (e: Exception) {
                    showErrorToast(
                        getString(
                            R.string.failed_processing_placement_response,
                            e.message
                        )
                    )
                }
            }.onFailure {
                showErrorToast(
                    getString(
                        R.string.placement_api_failed,
                        it.message ?: getString(R.string.unknown)
                    )
                )
            }
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Camera
    // ─────────────────────────────────────────────────────────

    private fun setupCameraLauncher() {
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    Log.d(TAG, "Camera captured: $photoUri")
                    onCameraSuccess()
                }
            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) launchCamera()
                else showToast(getString(R.string.camera_permission_is_required))
            }
    }

    private fun checkAndLaunchCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            launchCamera()
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun launchCamera() {
        val file = createImageFile() ?: run { showToast("Failed to create image file"); return }
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            file
        )
        cameraLauncher.launch(photoUri)
    }

    private fun createImageFile(): File? {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return try {
            File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
        } catch (e: IOException) {
            e.printStackTrace(); null
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Camera success handler
    // ─────────────────────────────────────────────────────────

    private fun onCameraSuccess() {
        // Capture geo coordinates when relevant
        when (currentPhotoTarget) {
            "Training Centre" -> captureTrainingCentreLocation { updateFieldDistance() }
            "Residential Facilities" -> captureResidentialFacilityLocation { updateFieldDistance() }
        }

        // Save photo into the correct section list
        if (currentUploadPosition >= 0) {
            val pos = currentUploadPosition
            when (currentUploadList) {
                SectionTag.ORG -> savePhotoToSection(
                    orgItems,
                    pos,
                    binding.recyclerView.id,
                    ::resolveOrgLabel
                )

                SectionTag.FIN -> savePhotoToSection(
                    finItems,
                    pos,
                    binding.recyclerViewFin.id,
                    ::resolveFinLabel
                )

                SectionTag.TRAINING -> savePhotoToSection(
                    trainingItems,
                    pos,
                    binding.recyclerViewTraining.id,
                    ::resolveTrainingLabel
                )

                SectionTag.TRAINING_INFRA -> savePhotoToSection(
                    trainingInfraItems,
                    pos,
                    binding.recyclerViewTrainingInfra.id,
                    ::resolveTrainingInfraLabel,
                    defaultRemark = "Document verified"
                )

                SectionTag.RESIDENTIAL -> savePhotoToSection(
                    residentialFacilityItems,
                    pos,
                    binding.recyclerViewResidentialFacility.id,
                    ::resolveResidentialLabel,
                    defaultRemark = "Document verified"
                )

                SectionTag.CERT -> savePhotoToSection(
                    certItems,
                    pos,
                    binding.recyclerViewCert.id,
                    ::resolveCertLabel,
                    defaultRemark = "Certification verified"
                )

                SectionTag.PLACEMENT -> savePhotoToSection(
                    placementItems,
                    pos,
                    binding.recyclerViewPlacement.id,
                    ::resolvePlacementLabel,
                    defaultRemark = "Document verified"
                )

                SectionTag.OFFICER_SELFIE -> {
                    Log.d(TAG, "Officer selfie captured")
                    officerSelfieBase64 = AppUtil.imageUriToBase64(requireContext(), photoUri)
                    startOfficerVerificationFlow()
                }
            }
            currentUploadPosition = -1
            currentUploadList = ""
        }
    }

    // ── Generic: save a photo into any section list ───────────

    private fun savePhotoToSection(
        items: MutableList<FieldVerificationItem>,
        position: Int,
        recyclerViewId: Int,
        resolveLabel: (String) -> String,
        defaultRemark: String = ""
    ) {
        val existing = items.getOrNull(position) ?: return

        val base64 =
            AppUtil.imageUriToBase64(requireContext(), photoUri) ?: return

        val remark =
            existing.remarkText
                ?.trim()
                ?.ifEmpty { defaultRemark }
                ?: defaultRemark

        val label = resolveLabel(existing.id)

        val updatedAttachments =
            existing.attachments.toMutableList()

        val existingAttachment =
            updatedAttachments.find { it.label == label }

        if (existingAttachment != null) {

            val mergedImages =
                existingAttachment.value.toMutableList()

            mergedImages.add(base64)

            updatedAttachments.remove(existingAttachment)

            updatedAttachments.add(
                existingAttachment.copy(
                    value = mergedImages,
                    remark = remark
                )
            )

        } else {

            updatedAttachments.add(
                AttachmentItem(
                    label = label,
                    value = mutableListOf(base64),
                    remark = remark
                )
            )
        }

        items[position] = existing.copy(
            imageUri = photoUri.toString(),
            uploadEnabled = true,
            attachments = updatedAttachments,
            remarkText = existing.remarkText
        )

        updateRecyclerViewData(recyclerViewId, items)
    }

    private fun syncRecyclerRemarks(
        recyclerView: RecyclerView,
        items: MutableList<FieldVerificationItem>
    ) {
        for (i in items.indices) {
            val holder = recyclerView.findViewHolderForAdapterPosition(i) as? BaseRecyclerAdapter<*, *>.BaseViewHolder
            val binding = holder?.binding as? ItemFieldVerCardBinding

            val latestRemark =
                binding?.etSectionRemark
                    ?.text
                    ?.toString()
                    ?.trim()

            if (!latestRemark.isNullOrEmpty()) {

                items[i].remarkText = latestRemark
            }
        }
    }



    // ── Label resolvers per section ──────────────────────────

    private fun resolveOrgLabel(id: String) = when (id) {
        AttachmentLabel.ORG_EXISTENCE -> AttachmentLabel.ORG_EXISTENCE
        AttachmentLabel.ORG_REGISTRATION -> AttachmentLabel.ORG_REGISTRATION
        AttachmentLabel.ORG_EPFO -> AttachmentLabel.ORG_EPFO
        AttachmentLabel.ORG_TAX -> AttachmentLabel.ORG_TAX
        AttachmentLabel.ORG_BANK -> AttachmentLabel.ORG_BANK
        AttachmentLabel.ORG_MANPOWER -> AttachmentLabel.ORG_MANPOWER
        else -> id
    }

    private fun resolveFinLabel(id: String) = when (id) {
        AttachmentLabel.FIN_TURNOVER -> AttachmentLabel.FIN_TURNOVER
        AttachmentLabel.FIN_NETWORTH -> AttachmentLabel.FIN_NETWORTH
        else -> id
    }

    private fun resolveTrainingLabel(id: String) = when (id) {
        AttachmentLabel.TRAIN_CRITERIA -> AttachmentLabel.TRAIN_CRITERIA
        AttachmentLabel.TRAIN_HOURS -> AttachmentLabel.TRAIN_HOURS
        AttachmentLabel.TRAIN_NSQF -> AttachmentLabel.TRAIN_NSQF
        AttachmentLabel.TRAIN_BASIC -> AttachmentLabel.TRAIN_BASIC
        AttachmentLabel.TRAIN_COMMITMENT -> AttachmentLabel.TRAIN_COMMITMENT
        AttachmentLabel.TRAIN_PLACEMENT -> AttachmentLabel.TRAIN_PLACEMENT
        AttachmentLabel.TRAIN_DOMAIN -> AttachmentLabel.TRAIN_DOMAIN
        else -> id
    }

    private fun resolveTrainingInfraLabel(id: String) = when (id) {
        AttachmentLabel.INFRA_DECLARATION -> AttachmentLabel.INFRA_DECLARATION
        AttachmentLabel.INFRA_CENTRE -> AttachmentLabel.INFRA_CENTRE
        AttachmentLabel.INFRA_CLASSROOM -> AttachmentLabel.INFRA_CLASSROOM
        AttachmentLabel.INFRA_TOILET -> AttachmentLabel.INFRA_TOILET
        AttachmentLabel.INFRA_BUILDING -> AttachmentLabel.INFRA_BUILDING
        AttachmentLabel.INFRA_TABLES -> AttachmentLabel.INFRA_TABLES
        AttachmentLabel.INFRA_LIGHTING -> AttachmentLabel.INFRA_LIGHTING
        else -> id
    }

    private fun resolveResidentialLabel(id: String) = when (id) {
        AttachmentLabel.RES_DECLARATION -> AttachmentLabel.RES_DECLARATION
        AttachmentLabel.RES_BUILDING -> AttachmentLabel.RES_BUILDING
        AttachmentLabel.RES_SAFETY -> AttachmentLabel.RES_SAFETY
        AttachmentLabel.RES_CANTEEN -> AttachmentLabel.RES_CANTEEN
        AttachmentLabel.RES_BED_WATER -> AttachmentLabel.RES_BED_WATER
        else -> id
    }

    private fun resolveCertLabel(id: String) = when (id) {
        AttachmentLabel.CERT_DECLARATION -> AttachmentLabel.CERT_DECLARATION
        else -> id
    }

    private fun resolvePlacementLabel(id: String) = when (id) {
        AttachmentLabel.PLACEMENT_DETAILS -> AttachmentLabel.PLACEMENT_DETAILS
        AttachmentLabel.PLACEMENT_COMMITMENT -> AttachmentLabel.PLACEMENT_COMMITMENT
        else -> id
    }

    // ─────────────────────────────────────────────────────────
    //  Officer verification flow
    // ─────────────────────────────────────────────────────────

    private fun showOfficerVerificationDialog() {
        AlertDialog.Builder(requireContext()).setTitle("Officer Verification")
            .setMessage("Please capture selfie and verify current location before starting field verification.")
            .setCancelable(false).setPositiveButton("Capture Selfie") { _, _ ->
                currentUploadList = SectionTag.OFFICER_SELFIE
                currentUploadPosition = 0
                currentPhotoTarget = SectionTag.OFFICER_SELFIE
                checkAndLaunchCamera()
            }.show()
    }

    private fun startOfficerVerificationFlow() {
        if (hasLocationPermission()) getOfficerCurrentLocation() else requestLocationPermission()
    }

    @SuppressLint("MissingPermission")
    private fun getOfficerCurrentLocation() {
        showProgressBar()
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                hideProgressBar()
                if (location != null) {
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()
                    Log.d(TAG, "Officer location — $latitude, $longitude")
                    callOfficerSelfieApi()
                } else {
                    showToast("Unable to fetch current location")
                }
            }.addOnFailureListener { hideProgressBar(); showToast("Failed to fetch location") }
    }

    private fun callOfficerSelfieApi() {
        val request = CaptivePiaOfficerSelfieRequest(
            appVersion = BuildConfig.VERSION_NAME,
            comment = "Officer Photo captured now",
            officerLatitude = latitude,
            officerLongitude = longitude,
            officerPhoto = officerSelfieBase64 ?: "",
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            createdBy = AppUtil.getSavedLoginIdPreference(requireContext())
        )

        showProgressBar()
        viewModel.getCaptivePiaOfficerSelfie(request, "")
        viewModel.officerSelfieApi.removeObservers(viewLifecycleOwner)
        viewModel.officerSelfieApi.observe(viewLifecycleOwner) { result ->
            hideProgressBar()
            result.onSuccess { response ->
                try {
                    if (response.responseCode == 200) {
                        Log.d(TAG, "Selfie API success")
                        isSelfieVerificationDone = true
                        showToast(response.responseDesc ?: "Officer verification successful")
                        revealOrgSection()
                    } else {
                        showToast(response.responseDesc ?: "Verification failed")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    showToast("Failed to process verification response")
                }
            }.onFailure {
                hideProgressBar()
                Log.e(TAG, "Selfie API failed: ${it.message}")
                showToast(it.message ?: "Officer verification failed")
            }
        }
    }

    private fun revealOrgSection() {
        binding.verOrg.visibility = View.VISIBLE
        binding.trainingInfraExpand.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.VISIBLE
        binding.btnInfoNext.visibility = View.VISIBLE
        scrollToTop()
    }

    // ─────────────────────────────────────────────────────────
    //  Location
    // ─────────────────────────────────────────────────────────

    private fun setupLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarse = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            if (granted) {
                if (!isSelfieVerificationDone) getOfficerCurrentLocation() else getCurrentLocation()
            } else {
                showToast(getString(R.string.location_permission_denied))
            }
        }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()

                    val currentLat = latitude.toDoubleOrNull()
                    val currentLng = longitude.toDoubleOrNull()

                    val tcLat = tcLatitude.toDoubleOrNull()
                    val tcLng = tcLongitude.toDoubleOrNull()

                    if (currentLat != null && currentLng != null && tcLat != null && tcLng != null) {
                        val officerDistance = calculateDistance(
                            currentLat, currentLng, tcLat, tcLng
                        )

                        isOfficerWithinRange = officerDistance <= MAX_ALLOWED_DISTANCE
                    }


                    val rfLat = rfLatitude.toDoubleOrNull()
                    val rfLng = rfLongitude.toDoubleOrNull()

                    if (tcLat != null && tcLng != null && rfLat != null && rfLng != null) {
                        tcRfDistance = calculateDistance(
                            tcLat, tcLng, rfLat, rfLng
                        )

                    }


                    if (::fieldAdapter.isInitialized) refreshFieldItems()
                } else {
                    showToast("Unable to get location")
                }
            }.addOnFailureListener {
                toastShort(getString(R.string.failed_to_get_location, it.message ?: ""))
            }
    }

    @SuppressLint("MissingPermission")
    private fun captureTrainingCentreLocation(onCaptured: () -> Unit) {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    tcLatitude = location.latitude.toString()
                    tcLongitude = location.longitude.toString()
                    Log.d(TAG, "TC location: $tcLatitude, $tcLongitude")
                    showToast("Training Centre location captured successfully")
                    onCaptured()
                } else {
                    showToast("Unable to capture Training Centre location")
                }
            }.addOnFailureListener { showToast("Failed to capture Training Centre location") }
    }

    @SuppressLint("MissingPermission")
    private fun captureResidentialFacilityLocation(onCaptured: () -> Unit) {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    rfLatitude = location.latitude.toString()
                    rfLongitude = location.longitude.toString()
                    Log.d(TAG, "RF location: $rfLatitude, $rfLongitude")
                    showToast("Residential Facility location captured successfully")
                    onCaptured()
                } else {
                    showToast("Unable to capture Residential Facility location")
                }
            }.addOnFailureListener { showToast("Failed to capture Residential Facility location") }
    }

    private fun updateFieldDistance() {
        if (tcLatitude.isNotBlank() && tcLongitude.isNotBlank() && rfLatitude.isNotBlank() && rfLongitude.isNotBlank()) {
            tcRfDistance = calculateDistance(
                tcLatitude.toDouble(),
                tcLongitude.toDouble(),
                rfLatitude.toDouble(),
                rfLongitude.toDouble()
            )
            Log.d(TAG, "TC-RF distance: ${tcRfDistance / 1000} KM")
            //showToast("Distance calculated successfully")
            refreshFieldItems()
        }
    }

    private fun refreshFieldItems() {
        fieldItems = mutableListOf(
//            FieldVerificationItem(
//                id = "",
//                requirement = resources.getString(R.string.field_ver_geo_factory_field),
//                verificationDoc = resources.getString(R.string.field_ver_ctsa_off_note_field),
//                documents = listOf(
//                    "Current Latitude:: $latitude",
//                    "Current Longitude: : $longitude"
//                ),
//                uploadEnabled = false,
//                allowRemark = false
//            ),
            FieldVerificationItem(
                id = "officer_location",
                requirement = "Officer Current Location",
                verificationDoc = "Officer current geo location verification",
                documents = listOf("Latitude : $latitude", "Longitude : $longitude"),
                uploadEnabled = false,
                allowRemark = false
            ), FieldVerificationItem(
                id = "tc_location",
                requirement = "Training Centre Location",
                verificationDoc = "Training Centre geo coordinates",
                documents = listOf("TC Latitude : $tcLatitude", "TC Longitude : $tcLongitude"),
                uploadEnabled = false,
                allowRemark = false
            ), FieldVerificationItem(
                id = "rf_location",
                requirement = "Residential Facility Location",
                verificationDoc = "Residential Facility geo coordinates",
                documents = listOf("RF Latitude : $rfLatitude", "RF Longitude : $rfLongitude"),
                uploadEnabled = false,
                allowRemark = false
            ), FieldVerificationItem(
                id = "distance",
                requirement = "Distance Between TC & RF",
                verificationDoc = "Calculated distance between TC and RF",
                documents = listOf("${tcRfDistance / 1000} KM"),
                uploadEnabled = false,
                allowRemark = false
            )
        )
        updateRecyclerViewData(binding.recyclerViewField.id, fieldItems)
    }

    private fun calculateDistance(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double
    ): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(startLat, startLng, endLat, endLng, results)
        return results[0]
    }

    // ─────────────────────────────────────────────────────────
    //  Validation helpers
    // ─────────────────────────────────────────────────────────

//    private fun validateAllRemarksForSection(
//        items: List<FieldVerificationItem>,
//        errorMessage: (FieldVerificationItem) -> String = {
//            getString(
//                R.string.please_enter_remark_for,
//                it.requirement
//            )
//        }
//    ): Map<String, String>? {
//        commitFocusedEditText()
//        val remarkItems = items.filter { it.allowRemark }
//        if (remarkItems.isEmpty()) return emptyMap()
//        remarkItems.forEach { item ->
//            if (item.remarkText?.trim().isNullOrEmpty()) {
//                showToast(errorMessage(item))
//                return null
//            }
//        }
//        return remarkItems.associate { it.requirement to it.remarkText!!.trim() }
//    }

//    private fun validateSectionItems(
//        items: List<FieldVerificationItem>
//    ): Boolean {
//        commitFocusedEditText()
//
//        val uploadItems = items.filter { it.uploadEnabled }
//
//        uploadItems.forEach { item ->
//
//            // Upload validation
//            if (
//                item.isAttachmentMandatory &&
//                item.attachments.isEmpty()
//            ) {
//                showToast(
//                    "Please upload document for ${item.requirement}"
//                )
//                return false
//            }
//
//            // Remark validation
//            if (item.allowRemark && item.remarkText.isNullOrBlank()) {
//                showToast(
//                    "Please enter remark for ${item.requirement}"
//                )
//                return false
//            }
//        }
//
//        return true
//
//    }

    private fun validateSectionItems(
        recyclerView: RecyclerView,
        items: MutableList<FieldVerificationItem>
    ): Boolean {
        commitFocusedEditText()

        syncRecyclerRemarks(recyclerView, items)

        val uploadItems =
            items.filter { it.uploadEnabled }

        uploadItems.forEach { item ->

            if (
                item.isAttachmentMandatory &&
                item.attachments.isEmpty()
            ) {

                showToast(
                    "Please upload document for ${item.requirement}"
                )

                return false
            }

            if (
                item.allowRemark &&
                item.remarkText?.trim().isNullOrEmpty()
            ) {

                showToast(
                    "Please enter remark for ${item.requirement}"
                )

                return false
            }
        }

        return true

    }





    private fun commitFocusedEditText() {
        try {
            view?.findFocus()?.clearFocus()
        } catch (_: Exception) {
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Payload builders
    // ─────────────────────────────────────────────────────────

    private fun buildDetailRequest() = FieldVerificationDetailRequest(
        appVersion = BuildConfig.VERSION_NAME,
        loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
        captiveEmpanelmentId = captiveEmpanelmentId,
        prnNo = prnNo
    )


    private fun collectSectionRemarksNew(

        sectionName: String,

        sectionItems: List<FieldVerificationItem>

    ): RemarkItem {

        commitFocusedEditText()

        val requirementKey = when (sectionName.uppercase()) {

            "ORGANIZATION" -> Requirement.ORG

            "FINANCE" -> Requirement.FIN

            "TRAINING" -> Requirement.TRAINING

            "TRAININGINFRA" -> Requirement.INFRA

            "RESIDENTIALFACILITY" -> Requirement.RESIDENTIAL

            "CERTIFICATION" -> Requirement.CERT

            "PLACEMENT" -> Requirement.PLACEMENT

            "FIELDVISIT" -> Requirement.FIELD

            else -> sectionName.uppercase()
        }

        val allAttachments = sectionItems.flatMap {

            it.attachments
        }

        return RemarkItem(

            section = sectionName,

            requirement = requirementKey,

            attachments = allAttachments
        )
    }


    private fun collectSectionRemarks(
        sectionName: String, sectionItems: List<FieldVerificationItem>
    ): List<RemarkItem> {
        commitFocusedEditText()

        val requirementKey = when (sectionName.uppercase()) {
            "ORGANIZATION" -> Requirement.ORG
            "FINANCE" -> Requirement.FIN
            "TRAINING" -> Requirement.TRAINING
            "TRAININGINFRA" -> Requirement.INFRA
            "RESIDENTIALFACILITY" -> Requirement.RESIDENTIAL
            "CERTIFICATION" -> Requirement.CERT
            "PLACEMENT" -> Requirement.PLACEMENT
            "FIELDVISIT" -> Requirement.FIELD
            else -> sectionName.uppercase()
        }

        val allAttachments = sectionItems.flatMap { it.attachments }

        return if (allAttachments.isNotEmpty()) {
            listOf(
                RemarkItem(
                    section = sectionName,
                    requirement = requirementKey,
                    attachments = allAttachments
                )
            )
        } else {
            sectionItems.filter { it.allowRemark }.mapNotNull { item ->
                    val r = item.remarkText?.trim().orEmpty()
                    if (r.isEmpty()) null else RemarkItem(
                        section = sectionName,
                        requirement = item.id
                    )
                }
        }
    }

    private fun buildFinalSubmitPayload(): FieldVerificationFinalSubmit {
        commitFocusedEditText()

        val fieldVisitAttachments = mutableListOf<AttachmentItem>().apply {
            officerSelfieBase64?.let {
                add(
                    AttachmentItem(
                        label = AttachmentLabel.FIELD_SELFIE,
                        value = listOf(it),
                        remark = "Officer selfie captured at login with geolocation"
                    )
                )
            }
            add(
                AttachmentItem(
                    label = AttachmentLabel.FIELD_GEO_INITIATION,
                    value = listOf("Completed"),
                    remark = "Officer found within 500 meters during verification initiation"
                )
            )
            add(
                AttachmentItem(
                    label = AttachmentLabel.FIELD_GEO_FINAL,
                    value = listOf("Completed"),
                    remark = "Officer found within 500 meters during final submission"
                )
            )
            if (tcLatitude.isNotBlank()) add(
                AttachmentItem(
                    label = AttachmentLabel.FIELD_TC_LAT,
                    value = listOf(tcLatitude),
                    remark = "Training Centre latitude captured during field visit"
                )
            )
            if (tcLongitude.isNotBlank()) add(
                AttachmentItem(
                    label = AttachmentLabel.FIELD_TC_LNG,
                    value = listOf(tcLongitude),
                    remark = "Training Centre longitude captured during field visit"
                )
            )
            if (rfLatitude.isNotBlank()) add(
                AttachmentItem(
                    label = AttachmentLabel.FIELD_RF_LAT,
                    value = listOf(rfLatitude),
                    remark = "Residential Facility latitude captured during field visit"
                )
            )
            if (rfLongitude.isNotBlank()) add(
                AttachmentItem(
                    label = AttachmentLabel.FIELD_RF_LNG,
                    value = listOf(rfLongitude),
                    remark = "Residential Facility longitude captured during field visit"
                )
            )
            if (tcRfDistance > 0f) add(
                AttachmentItem(
                    label = AttachmentLabel.FIELD_DISTANCE,
                    value = listOf(String.format("%.2f KM", tcRfDistance / 1000)),
                    remark = "Distance between Training Centre and Residential Facility captured"
                )
            )
        }

        return FieldVerificationFinalSubmit(
            Organization = collectSectionRemarks("Organization", orgItems),
            Finance = collectSectionRemarks("Finance", finItems),
            Training = collectSectionRemarks("Training", trainingItems),
            TrainingInfra = collectSectionRemarks("TrainingInfra", trainingInfraItems),
            ResidentialFacility = collectSectionRemarks(
                "ResidentialFacility",
                residentialFacilityItems
            ),
            Certification = collectSectionRemarks("Certification", certItems),
            Placement = collectSectionRemarks("Placement", placementItems),
            FieldVisit = listOf(
                RemarkItem(
                    section = "FieldVisit",
                    requirement = Requirement.FIELD,
                    attachments = fieldVisitAttachments
                )
            ),
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            captiveEmpanelmentId = captiveEmpanelmentId,
            prnNo = prnNo,
        )
    }

    // ─────────────────────────────────────────────────────────
    //  Item list builders
    // ─────────────────────────────────────────────────────────

    private fun buildOrgItems(): MutableList<FieldVerificationItem> = mutableListOf(
        viewItem(
            resources.getString(R.string.field_ver_industry_existence),
            resources.getString(R.string.field_ver_valid_govt_note_doc),
            listOf("Date of Incorporation (PRN)")
        ),
        viewItem(
            resources.getString(R.string.field_ver_valid_epfo_esic_doc),
            resources.getString(R.string.field_ver_valid_epfo_esic_note_doc),
            listOf("View Registration Document")
        ),
        viewItem(
            resources.getString(R.string.field_ver_epfo_challan_doc),
            resources.getString(R.string.field_ver_valid_epfo_challan_note_doc),
            listOf("EPFO Challan (6 Months)")
        ),
        viewItem(
            resources.getString(R.string.field_ver_valid_industry_doc),
            resources.getString(R.string.field_ver_valid_industry_note_doc),
            listOf("View")
        ),
        viewItem(
            resources.getString(R.string.field_ver_valid_bank_doc),
            resources.getString(R.string.field_ver_valid_bank_note_doc),
            listOf("View Account Details")
        ),
        uploadItem(
            AttachmentLabel.ORG_EXISTENCE,
            "Industry Existence Verification",
            "Upload proof of industry existence verification document.",
            "Upload Industry Existence Document",
            "Organization"
        ),
        uploadItem(
            AttachmentLabel.ORG_REGISTRATION,
            "Industry Registration Verification",
            "Upload industry registration verification document.",
            "Upload Industry Registration Document",
            "Organization"
        ),
        uploadItem(
            AttachmentLabel.ORG_EPFO,
            "EPFO Challan Verification",
            "Upload EPFO challan verification document.",
            "Upload EPFO Challan",
            "Organization"
        ),
        uploadItem(
            AttachmentLabel.ORG_TAX,
            "Tax Details Verification",
            "Upload GST and TAN verification document.",
            "Upload Tax Verification Document",
            "Organization"
        ),
        uploadItem(
            AttachmentLabel.ORG_BANK,
            "Bank Details Verification",
            "Upload bank details verification document.",
            "Upload Bank Verification Document",
            "Organization"
        ),
        uploadItem(
            AttachmentLabel.ORG_MANPOWER,
            "Manpower Agency Check Verification",
            "Upload manpower agency declaration verification document.",
            "Upload Manpower Agency Declaration",
            "Organization"
        ),
    )

    private fun buildFinItems(): MutableList<FieldVerificationItem> = mutableListOf(
        viewItem(
            resources.getString(R.string.field_ver_industry_turnover_fin),
            resources.getString(R.string.field_ver_industry_turnover_note_fin),
            listOf(resources.getString(R.string.fin_balance_sheet_button))
        ),
        viewItem(
            resources.getString(R.string.field_ver_industry_networth_fin),
            resources.getString(R.string.field_ver_industry_networth_note_fin),
            listOf(resources.getString(R.string.fin_turnover_button))
        ),
        uploadItem(
            AttachmentLabel.FIN_TURNOVER,
            "Annual Turnover Self Declaration",
            "Verify the submitted self declaration document for annual turnover details.",
            "upload Annual Turnover Self Declaration",
            "Finance"
        ),
        uploadItem(
            AttachmentLabel.FIN_NETWORTH,
            "Net Worth Self Declaration",
            "Verify the submitted self declaration document for net worth details.",
            "upload Net Worth Self Declaration",
            "Finance"
        ),
    )

    private fun buildTrainingItems(): MutableList<FieldVerificationItem> = mutableListOf(
        viewItem(
            resources.getString(R.string.field_ver_exp_training),
            resources.getString(R.string.field_ver_exp_note_training),
            listOf(resources.getString(R.string.train_target_button))
        ),
        FieldVerificationItem(
            id = "FIN_TURNOVER",
            requirement = resources.getString(R.string.field_ver_hrs_training),
            verificationDoc = resources.getString(R.string.field_ver_hrs_note_training),
            documents = listOf(resources.getString(R.string.train_hour_button)),
            uploadEnabled = false,
            allowRemark = false
        ),
        FieldVerificationItem(
            id = "FIN_NETWORTH",
            requirement = resources.getString(R.string.field_ver_course_content_training),
            verificationDoc = if (repetitionClubbingIfraNsqf.equals(
                    "Y",
                    ignoreCase = true
                )
            ) "YES" else "NO",
            documents = listOf(),
            uploadEnabled = false,
            allowRemark = false
        ),
        viewItem(
            resources.getString(R.string.field_ver_nsqf_courses_training),
            resources.getString(R.string.field_ver_nsqf_courses_note_training),
            listOf(resources.getString(R.string.train_NSQF_course_button))
        ),
        viewItem(
            resources.getString(R.string.field_ver_500_cand_training),
            resources.getString(R.string.field_ver_500_cand_note_training),
            listOf(
                resources.getString(R.string.train_commitment1_button),
                resources.getString(R.string.train_commitment2_button)
            )
        ),
        viewItem(
            resources.getString(R.string.field_ver_job_training),
            resources.getString(R.string.field_ver_job_note_training),
            listOf(resources.getString(R.string.train_tailor_button))
        ),
        viewItem(
            resources.getString(R.string.field_ver_domain_training),
            resources.getString(R.string.field_ver_domain_note_training),
            listOf(
                resources.getString(R.string.train_domain1_button),
                resources.getString(R.string.train_domain2_button)
            )
        ),
        uploadItem(
            AttachmentLabel.TRAIN_CRITERIA,
            "Training Criteria Verification",
            "Verify the submitted self declaration document for training criteria details.",
            "Upload Training Criteria Self Declaration",
            "Training"
        ),
        uploadItem(
            AttachmentLabel.TRAIN_HOURS,
            "Total Training Hours Verification",
            "Verify the submitted self declaration document for total training hours.",
            "Upload Total Training Hours Self Declaration",
            "Training"
        ),
        uploadItem(
            AttachmentLabel.TRAIN_NSQF,
            "Repetition Clubbing and NSQF Verification",
            "Verify the submitted self declaration document for repetition clubbing and NSQF compliance.",
            "Upload Repetition Clubbing NSQF Self Declaration",
            "Training"
        ),
        uploadItem(
            AttachmentLabel.TRAIN_BASIC,
            "Basic Training Verification",
            "Verify the submitted self declaration document for basic training details.",
            "Upload Basic Training Self Declaration",
            "Training"
        ),
        uploadItem(
            AttachmentLabel.TRAIN_COMMITMENT,
            "Commitment Verification",
            "Verify the submitted self declaration document for commitment details.",
            "Upload Commitment Self Declaration",
            "Training"
        ),
        uploadItem(
            AttachmentLabel.TRAIN_PLACEMENT,
            "Training and Placement Verification",
            "Verify the submitted self declaration document for training and placement records.",
            "Upload Training Placement Self Declaration",
            "Training"
        ),
        uploadItem(
            AttachmentLabel.TRAIN_DOMAIN,
            "Domain Specific Training Verification",
            "Verify the submitted self declaration document for domain specific training details.",
            "Upload Domain Specific Training Self Declaration",
            "Training"
        ),
    )



    private fun buildCertItems(): MutableList<FieldVerificationItem> = mutableListOf(
        viewItem(
            resources.getString(R.string.field_ver_provide_cert),
            resources.getString(R.string.field_ver_provide_note_cert),
            listOf("Form 4")
        ),
        viewItem(
            resources.getString(R.string.field_ver_res_conduct_cert),
            resources.getString(R.string.field_ver_conduct_note_cert),
            listOf("Form 4")
        ),
        uploadItem(
            AttachmentLabel.CERT_DECLARATION,
            "Assessment and Certification Verification",
            "Verify the submitted self declaration document for assessment and certification.",
            "Upload Certification Self Declaration",
            "Certification"
        ),
    )

    private fun buildPlacementItems(): MutableList<FieldVerificationItem> = mutableListOf(
        viewItem(
            resources.getString(R.string.field_ver_500_empl_placement),
            resources.getString(R.string.field_ver_empl_note_placement),
            listOf("View Employment Details")
        ),
        viewItem(
            resources.getString(R.string.field_ver_70_per_cand_placement),
            resources.getString(R.string.field_ver_empl_off_letter_note_placement),
            listOf("Form 1")
        ),
        viewItem(
            resources.getString(R.string.field_ver_70_per_less_cand_coursewise_placement),
            resources.getString(R.string.field_ver_empl_off_letter_coursewise_less_note_placement),
            listOf("Form 1")
        ),
        viewItem(
            resources.getString(R.string.field_ver_70_per_more_cand_coursewise_placement),
            resources.getString(R.string.field_ver_empl_off_letter_coursewise_more_note_placement),
            listOf("Form 1")
        ),
        uploadItem(
            AttachmentLabel.PLACEMENT_DETAILS,
            "Placement Details Verification",
            "Verify the submitted self declaration document for placement details.",
            "Upload Placement Details Self Declaration",
            "Placement"
        ),
        uploadItem(
            AttachmentLabel.PLACEMENT_COMMITMENT,
            "Placement Commitment Verification",
            "Verify the submitted self declaration document for placement commitment.",
            "Upload Commitment Self Declaration",
            "Placement"
        ),
    )

    private fun buildFieldItems(): MutableList<FieldVerificationItem> = mutableListOf(
        viewItem(
            resources.getString(R.string.field_ver_geo_factory_field),
            resources.getString(R.string.field_ver_ctsa_off_note_field),
            listOf("Lat: $latitude", "Long: $longitude")
        )
    )

    fun buildTrainingInfraItems(): MutableList<FieldVerificationItem> = mutableListOf(
        uploadItem(
            AttachmentLabel.INFRA_DECLARATION,
            "Training Infrastructure Verification",
            "Verify the submitted self declaration document for training infrastructure.",
            "Upload Training Infrastructure Self Declaration",
            "TrainingInfra"
        ),
        uploadItem(
            AttachmentLabel.INFRA_CENTRE,
            "Training Centre Verification",
            "Verify the training centre infrastructure and captured photos.",
            "Upload Training Centre Photo",
            "TrainingInfra"
        ),
        uploadItem(
            AttachmentLabel.INFRA_CLASSROOM,
            "Classroom Infrastructure Verification",
            "Verify the classroom infrastructure and captured photos.",
            "Upload Classroom Photo",
            "TrainingInfra"
        ),
        uploadItem(
            AttachmentLabel.INFRA_TOILET,
            "Toilet Facility Verification",
            "Verify the toilet facilities and captured photos.",
            "Upload Toilet Facility Photo",
            "TrainingInfra"
        ),
        uploadItem(
            AttachmentLabel.INFRA_BUILDING,
            "Building Infrastructure Verification",
            "Verify the building infrastructure and captured photos.",
            "Upload Building Photo",
            "TrainingInfra"
        ),
        uploadItem(
            AttachmentLabel.INFRA_TABLES,
            "Tables and Chairs Verification",
            "Verify the tables and chairs arrangement and captured photos.",
            "Upload Tables and Chairs Photo",
            "TrainingInfra"
        ),
        uploadItem(
            AttachmentLabel.INFRA_LIGHTING,
            "Lighting and Safety Verification",
            "Verify the lighting and safety measures and captured photos.",
            "Upload Lighting and Safety Photo",
            "TrainingInfra"
        ),
    )

    fun buildResidentialFacilityItems(): MutableList<FieldVerificationItem> = mutableListOf(
        uploadItem(
            AttachmentLabel.RES_DECLARATION,
            "Residential Facility Verification",
            "Verify the submitted self declaration document for residential facilities.",
            "Upload Residential Facility Self Declaration",
            "ResidentialFacility"
        ),
        uploadItem(
            AttachmentLabel.RES_BUILDING,
            "Residential Building Verification",
            "Verify the residential building infrastructure and captured photos.",
            "Upload Residential Building Photo",
            "ResidentialFacility"
        ),
        uploadItem(
            AttachmentLabel.RES_SAFETY,
            "Residential Safety Measures Verification",
            "Verify the residential safety measures and captured photos.",
            "Upload Residential Safety Measures Photo",
            "ResidentialFacility"
        ),
        uploadItem(
            AttachmentLabel.RES_CANTEEN,
            "Residential Canteen Verification",
            "Verify the residential canteen facilities and captured photos.",
            "Upload Residential Canteen Photo",
            "ResidentialFacility"
        ),
        uploadItem(
            AttachmentLabel.RES_BED_WATER,
            "Bed and Drinking Water Verification",
            "Verify the bed and drinking water facilities and captured photos.",
            "Upload Residential Bed and Drinking Water Photo",
            "ResidentialFacility"
        ),
    )



    // ─────────────────────────────────────────────────────────
    //  Dialogs
    // ─────────────────────────────────────────────────────────

    private fun showIndustryIncorporationDialog() {
        val message = buildString {
            appendLine("📅 Date of Incorporation")
            appendLine("      ${apiDateOfIncorporation ?: "Not Available"}")
            appendLine()
            appendLine("⏳ Duration of Organization")
            appendLine("     ${durationOfOrg ?: "Not Available"}")
        }.trim()

        val actions = buildList {
            if (!factoryRegistrationAttachment.isNullOrBlank()) add(DialogAction("View Factory Registration") {
                openBase64Pdf(
                    apiEpfoAttachmentBase64!!
                )
            })
        }
        showInfoDialog("Industry Incorporation", message, actions)
    }

    private fun showIndustryRegistrationDialog() {
        val message = buildString {
            appendLine("EPFO Number: ${apiEpfoNumber ?: "NA"}")
            appendLine("ESIC Number: ${apiEsicNumber ?: "NA"}")
            appendLine("Factory Registration Number: ${apiFactoryRegNumber ?: "NA"}")
        }.trim()

        val actions = buildList {
            if (!apiEpfoAttachmentBase64.isNullOrBlank()) add(DialogAction("View EPFO") {
                openBase64Pdf(
                    apiEpfoAttachmentBase64!!
                )
            })
            if (!apiEsicAttachmentBase64.isNullOrBlank()) add(DialogAction("View ESIC") {
                openBase64Pdf(
                    apiEsicAttachmentBase64!!
                )
            })
            if (!apiFactoryAttachmentBase64.isNullOrBlank()) add(DialogAction("View Factory") {
                openBase64Pdf(
                    apiFactoryAttachmentBase64!!
                )
            })
        }
        showInfoDialog("Industry Registration", message, actions)
    }

    private fun showEpfoChallanDialog() {
        val message =
            "Existing staff registered in EPFO: ${apiEpfoExistingStaff ?: "Not Available"}"
        val actions = buildList {
            if (!apiEpfoDocumentUrl.isNullOrBlank()) add(DialogAction("View EPFO Challan") {
                openBase64Pdf(
                    apiEpfoDocumentUrl!!
                )
            })
        }
        showInfoDialog("EPFO Challan (Last 6 Months)", message, actions)
    }

    private fun showTaxDetailsDialog() {
        val message = buildString {
            appendLine("GST number: ${apiGstNumber ?: "—"}")
            appendLine("TAN number: ${apiTanNumber ?: "—"}")
        }.trim()
        val actions = buildList {
            if (!apiTanAttachmentBase64.isNullOrBlank()) add(DialogAction("View TAN") {
                openBase64Pdf(
                    apiTanAttachmentBase64!!
                )
            })
        }
        showInfoDialog("Tax Details", message, actions)
    }

    private fun showBankDetailsDialog() {
        val message = buildString {
            appendLine("🏛️ Bank Name"); appendLine("     ${apiBankName ?: "Not Available"}"); appendLine()
            appendLine("💳 Account Number"); appendLine("     ${apiBankAccountNumber ?: "Not Available"}")
        }.trim()
        val actions = buildList {
            if (!apiBankLetterBase64.isNullOrBlank()) add(DialogAction("View BankLetter") {
                openBase64Pdf(
                    apiBankLetterBase64!!
                )
            })
            if (!apiSelfDeclarationBase64.isNullOrBlank()) add(DialogAction("View SelfDeclaration") {
                openBase64Pdf(
                    apiSelfDeclarationBase64!!
                )
            })
            if (!apiBankAccountPassbook.isNullOrBlank()) add(DialogAction("View Passbook") {
                openBase64Pdf(
                    apiBankAccountPassbook!!
                )
            })
        }
        showInfoDialog("Bank Details", message, actions)
    }

    private fun showResidentialFacilitiesDialog() {
        val message = "Residential Facility Available: ${apiResidentialFacilityAvailable ?: "—"}"
        if (!apiResidentialFacilityDocumentBase64.isNullOrBlank()) {
            val actions = listOf(DialogAction("Residential Facilities") {
                openBase64Pdf(apiResidentialFacilityDocumentBase64!!)
            })
            showInfoDialog("Residential Facilities", message, actions)
        } else {
            showToast("No Residential Facilities to View")
        }
    }

    private fun showDocumentDialog(title: String, base64: String?, buttonText: String) {
        if (!base64.isNullOrBlank()) {
            showInfoDialog(title, "", listOf(DialogAction(buttonText) { openBase64Pdf(base64) }))
        } else {
            showToast(getString(R.string.no_file_to_view))
        }
    }

    private fun showFinancialDialog(title: String, list: List<YearlyFinancialItem>?) {
        if (list.isNullOrEmpty()) {
            showToast("No $title data found"); return
        }
        val dialogView = layoutInflater.inflate(R.layout.dialog_simple_list, null)
        dialogView.findViewById<TextView>(R.id.tvDialogTitle).text = title
        val rv = dialogView.findViewById<RecyclerView>(R.id.rvDialogList)
        setupRecyclerView(
            recyclerView = rv,
            items = list,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = ItemFinancialRowBinding::inflate,
            onBind = { item, b, _ ->
                b.tvYear.text = item.year ?: "-"
                b.tvAmount.text = formatAmount(item.amount)
                b.btnView.visibility =
                    if (!item.attachmentBase64.isNullOrBlank()) View.VISIBLE else View.GONE
                b.btnView.setOnClickListener { openBase64Pdf(item.attachmentBase64!!) }
            },
            noDataConfig = NoDataConfig("No Data Available", "No financial records found")
        )
        AlertDialog.Builder(requireContext()).setView(dialogView)
            .setNegativeButton(resources.getString(R.string.close), null).show()
    }

    private fun showTrainingDialog(title: String, list: List<YearlyTrainingItem>?) {
        if (list.isNullOrEmpty()) {
            showToast("No $title data found"); return
        }
        val dialogView = layoutInflater.inflate(R.layout.dialog_simple_list, null)
        dialogView.findViewById<TextView>(R.id.tvDialogTitle).text = title
        val rv = dialogView.findViewById<RecyclerView>(R.id.rvDialogList)
        setupRecyclerView(
            recyclerView = rv,
            items = list,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = { inflater, parent, _ ->
                ItemTrainingRowBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            },
            onBind = { item, b, _ ->
                b.tvYear.text = item.year ?: "-"
                b.tvAllocated.text = "Allocated: ${formatNumber(item.targetAllocated)}"
                b.tvAchieved.text = "Achieved: ${formatNumber(item.targetAchieved)}"
                b.btnView.visibility =
                    if (!item.attachmentBase64.isNullOrBlank()) View.VISIBLE else View.GONE
                b.btnView.setOnClickListener { openBase64Pdf(item.attachmentBase64!!) }
            },
            noDataConfig = NoDataConfig("No Data Available", "No training records found")
        )
        AlertDialog.Builder(requireContext()).setView(dialogView).setNegativeButton("Close", null)
            .show()
    }

    private fun showTrainingHoursDialog(title: String, items: List<TotalTrainingHoursRemark>?) {
        if (items.isNullOrEmpty()) {
            showInfoDialog(title, "No Training Details Available", emptyList()); return
        }
        val message = buildString {
            items.forEachIndexed { _, item ->
                appendLine(" Year"); appendLine("   ${item.year ?: "Not Available"}"); appendLine()
                appendLine("Trade Name"); appendLine("   ${item.trade_name ?: "Not Available"}"); appendLine()
                appendLine(" Training Duration"); appendLine("   ${item.trade_duration ?: 0} Hours"); appendLine()
                appendLine("📄 Commencement Certificate- ${if (item.commencement_certificate.isNullOrBlank()) "Not Uploaded" else "Available"}")
            }
        }.trim()
        val actions = buildList {
            items.forEachIndexed { index, item ->
                if (!item.commencement_certificate.isNullOrBlank()) add(DialogAction("📄 View Certificate ${index + 1}") {
                    openBase64Pdf(
                        item.commencement_certificate
                    )
                })
            }
        }
        showInfoDialog(title, message, actions)
    }

    private fun showPlacementDialog(title: String, list: List<YearlyPlacementDetails>?) {
        if (list.isNullOrEmpty()) {
            showToast("No Placement data found"); return
        }
        val dialogView = layoutInflater.inflate(R.layout.dialog_simple_list, null)
        dialogView.findViewById<TextView>(R.id.tvDialogTitle).text = title
        val rv = dialogView.findViewById<RecyclerView>(R.id.rvDialogList)
        setupRecyclerView(
            recyclerView = rv,
            items = list,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = { inflater, parent, _ ->
                ItemPlacementRowBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            },
            onBind = { item, b, _ ->
                b.tvYear.text = boldLabel("Year:", item.year ?: "-")
                b.tvCandidatePlaced.text =
                    boldLabel("Candidates Placed:", item.candidatePlaced.toString())
                b.tvSanctionOrder.text =
                    boldLabel("Sanction Order:", item.sanctionOrderId.toString())
                b.tvEsicNumber.text = boldLabel("ESIC No:", item.esicNumber.toString())
                b.tvEpfoNumber.text = boldLabel("EPFO No:", item.epfoNumber.toString())
                b.btnView.visibility =
                    if (!item.proofDocument.isNullOrBlank()) View.VISIBLE else View.GONE
                b.btnView.setOnClickListener { openBase64Pdf(item.proofDocument!!) }
            },
            noDataConfig = NoDataConfig("No Data Available", "No placement records found")
        )
        AlertDialog.Builder(requireContext()).setView(dialogView)
            .setNegativeButton(resources.getString(R.string.close), null).show()
    }

    /** Generic info dialog with horizontal action buttons */
    private fun showInfoDialog(title: String, message: String, actions: List<DialogAction>) {
        val ctx = requireContext()
        val dp = ctx.resources.displayMetrics.density

        val container = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            val pad = (14 * dp).toInt()
            setPadding(pad, pad, pad, (12 * dp).toInt())
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        if (message.isNotEmpty()) {
            container.addView(TextView(ctx).apply {
                text = message
                setTextColor(ContextCompat.getColor(ctx, android.R.color.black))
                textSize = 14f
            })
        }

        if (actions.isNotEmpty()) {
            val strokeColor = runCatching {
                ContextCompat.getColor(
                    ctx,
                    R.color.color_dark_blue
                )
            }.getOrElse { ContextCompat.getColor(ctx, android.R.color.holo_blue_dark) }
            val rippleColor = ColorUtils.setAlphaComponent(strokeColor, 80)

            val row = LinearLayout(ctx).apply {
                orientation = LinearLayout.HORIZONTAL
                weightSum = actions.size.toFloat()
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply { topMargin = (16 * dp).toInt() }
            }

            var dialog: AlertDialog? = null

            actions.forEach { action ->
                row.addView(AppCompatButton(ctx).apply {
                    text = action.label
                    isAllCaps = false
                    setTextColor(strokeColor)
                    layoutParams =
                        LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                            .apply {
                                marginStart = (6 * dp).toInt(); marginEnd = (6 * dp).toInt()
                            }
                    minHeight = (44 * dp).toInt()
                    val pad = (10 * dp).toInt()
                    setPadding(pad, pad, pad, pad)
                    background = buildButtonBackground(dp, strokeColor, rippleColor)
                    stateListAnimator = null
                    setOnClickListener { dialog?.dismiss(); action.onClick() }
                })
            }
            container.addView(row)

            dialog = AlertDialog.Builder(ctx).setTitle(title).setView(container)
                .setNegativeButton(resources.getString(R.string.close), null).create()
                .also { it.window?.setBackgroundDrawableResource(R.drawable.dialog_background) }
            dialog.show()
        } else {
            AlertDialog.Builder(ctx).setTitle(title).setView(container)
                .setNegativeButton(resources.getString(R.string.close), null).create()
                .also { it.window?.setBackgroundDrawableResource(R.drawable.dialog_background) }
                .show()
        }
    }



    // ─────────────────────────────────────────────────────────
    //  Debug utilities
    // ─────────────────────────────────────────────────────────

    private fun logSectionGson(sectionName: String, list: MutableList<FieldVerificationItem>) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        Log.d(
            "$sectionName ─────────────> Data",
            gson.toJson(collectSectionRemarks(sectionName, list))
        )
    }

}