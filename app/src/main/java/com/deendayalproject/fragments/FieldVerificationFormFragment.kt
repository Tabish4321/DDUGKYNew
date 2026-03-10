package com.deendayalproject.fragments

import SharedViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.RippleDrawable
import android.net.Uri
import android.os.Environment
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.base.BaseRecyclerAdapter
import com.deendayalproject.base.NoDataConfig
import com.deendayalproject.databinding.FragmentFieldVerFormBinding
import com.deendayalproject.databinding.ItemFieldVerCardBinding
import com.google.android.material.chip.Chip
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.model.request.FieldVerificationDetailRequest
import com.deendayalproject.util.AppUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.graphics.ColorUtils
import com.deendayalproject.databinding.ItemFinancialRowBinding
import com.deendayalproject.databinding.ItemPlacementRowBinding
import com.deendayalproject.databinding.ItemTrainingRowBinding
import com.deendayalproject.model.request.FieldVerificationFinalSubmit
import com.deendayalproject.model.response.AnnualTurnover
import com.deendayalproject.model.response.AttachmentItem
import com.deendayalproject.model.response.NetWorth
import com.deendayalproject.model.response.RemarkItem
import com.deendayalproject.model.response.TrainingCriteriaItem
import com.deendayalproject.model.response.YearlyFinancialItem
import com.deendayalproject.model.response.YearlyPlacementDetails
import com.deendayalproject.model.response.YearlyTrainingItem
import com.deendayalproject.model.response.toYearlyItem
import com.deendayalproject.model.response.toYearlyTrainingItem
import java.text.NumberFormat

class FieldVerificationFormFragment : BaseFragment<FragmentFieldVerFormBinding>(
    FragmentFieldVerFormBinding::inflate
) {

    // Optional shared VM (attempt to obtain if present)
    private var sharedViewModel: SharedViewModel? = null

    private val viewModel: SharedViewModel by activityViewModels()

    // RecyclerView adapters
    private lateinit var orgAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>
    private lateinit var finAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>
    private lateinit var trainingAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>
    private lateinit var trainingInfraAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>
    private lateinit var certAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>
    private lateinit var placementAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>
    private lateinit var fieldAdapter: BaseRecyclerAdapter<FieldVerificationItem, ItemFieldVerCardBinding>

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private lateinit var photoUri: Uri

    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>

    private var currentPhotoTarget: String = ""

    private var base64FinanceFile: String? = null

    private var base64TrainingFile: String? = null

    private var base64TrainingInfraDeclarationFile: String? = null

    private var base64TrainingInfraCentreFile: String? = null

    private var base64TrainingResFile: String? = null

    private var selectedOrganizationInfoRemarks = ""

    private var selectedFinanceRemarks = ""

    private var selectedTrainingRemarks = ""

    private var selectedTrainingInfraRemarks = ""

    private var selectedCertRemarks = ""

    private var selectedPlacementRemarks = ""

    private var selectedFieldRemarks = ""

    private var manpowerRemarkLocal: String? = null

    private var orgItems: MutableList<FieldVerificationItem> = mutableListOf()
    private var finItems: MutableList<FieldVerificationItem> = mutableListOf()

    private var trainingItems: MutableList<FieldVerificationItem> = mutableListOf()

    private var trainingInfraItems: MutableList<FieldVerificationItem> = mutableListOf()

    private var certItems: MutableList<FieldVerificationItem> = mutableListOf()

    private var placementItems: MutableList<FieldVerificationItem> = mutableListOf()

    private var fieldItems: MutableList<FieldVerificationItem> = mutableListOf()

    private var currentUploadPosition: Int = -1

    private var currentUploadList: String = ""

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var latitude: String = ""

    private var longitude: String = ""

    private var captiveEmpanelmentId = ""
    private var prnNo = ""

    /* to show the response fetched in the form variables use for Organisation Details */
    private var apiDateOfIncorporation: String? = null

    private var apiBankName: String? = null
    private var apiManpowerRemarks: String? = null

    /* EPFO 6 months challan */
    private var apiEpfoExistingStaff: String? = null
    private var apiEpfoDocumentUrl: String? = ""

    // --- Tax Details (from API) ---
    private var apiGstNumber: String? = null
    private var apiTanNumber: String? = null
    private var apiTanAttachmentBase64: String? = null

    // --- Bank Details (from API) ---
    private var apiBankAccountNumber: String? = null
    private var apiBankLetterBase64: String? = null
    private var apiSelfDeclarationBase64: String? = null

    // --- Industry Registration (from API) ---
    private var apiEpfoNumber: String? = null
    private var apiEsicNumber: String? = null
    private var apiFactoryRegNumber: String? = null

    private var apiEpfoAttachmentBase64: String? = null
    private var apiEsicAttachmentBase64: String? = null
    private var apiFactoryAttachmentBase64: String? = null

    private var apiAnnualTurnoverList: List<AnnualTurnover>? = null
    private var apiNetWorthList: List<NetWorth>? = null

    private data class DocAction(val label: String, val onClick: () -> Unit)

    // --- Training response holders ---
    private var apiTrainingCriteriaList: List<TrainingCriteriaItem>? = null
    private var apiTotalTrainingHoursRemarks: String? = null
    private var apiRepetitionClubbingRemarks: String? = null

    // Attachments (may be base64 or null)
    private var apiBasicSelfDeclarationBase64: String? = null
    private var apiCommitmentForm1Base64: String? = null
    private var apiCommitmentForm2Base64: String? = null
    private var apiTailorTrainingDocBase64: String? = null
    private var apiDomainForm1Base64: String? = null
    private var apiDomainForm2Base64: String? = null

    // --- Training Infra (from API) ---
    private var apiResidentialFacilityAvailable: String? = null
    private var apiResidentialFacilityDocumentBase64: String? = null

    // Assessment & Certification (API-provided base64)
    private var apiAwardBodyCommitBase64: String? = null
    private var apiSeventyPctCommitBase64: String? = null

    // Placement (API- provided base64)
    private var apiPlacementList: List<YearlyPlacementDetails>? = null
    private var apiCommitmentSixMonthsBase64: String? = null
    private var apiCommitmentLessSixMonthsBase64: String? = null
    private var apiCommitmentMoreSixMonthsBase64: String? = null

    override fun initializeViews() {
        Log.d("FRAGMENT NAME", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━FieldVerificationFragment━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        captiveEmpanelmentId = arguments?.getString("captiveEmpanelmentId").toString()
        prnNo = arguments?.getString("prnNo").toString()

        setupCameraLauncher()
        setupRecyclerViews()
        setupLocationClient()
    }

    override fun setupObservers() {
        observeFieldDetails()
    }

    override fun setupClickListeners() {
        setupToolbar()
        setupNavigationButtons()
        setupSubmitButtons()
    }

    override fun loadInitialData() {
        val request = FieldVerificationDetailRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            captiveEmpanelmentId = captiveEmpanelmentId,
            prnNo = prnNo
        )

        viewModel.getFieldVerificationDetail(request)
    }

    private fun setupCameraLauncher() {
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    Log.d("Camera", "Captured image URI: $photoUri")
                    handleCameraSuccess()
                }
            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) launchCamera()
                else showToast(getString(R.string.camera_permission_is_required))
            }
    }

    private fun setupRecyclerViews() {
        setupOrgRecyclerView()
        setupFinRecyclerView()
        setupTrainingRecyclerView()
        setupTrainingInfraRecyclerView()
        setupCertRecyclerView()
        setupPlacementRecyclerView()
        setupFieldRecyclerView()
    }

    private fun setupOrgRecyclerView() {
        orgItems = getOrgItems()

        orgAdapter = setupRecyclerView(
            recyclerView = binding.recyclerView,
            items = orgItems,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = ItemFieldVerCardBinding::inflate,
            onBind = { item, binding, position ->
                bindFieldVerificationItem(item, binding, position, "org")
            },
            onItemClick = { item, position ->
                // Handle item click if needed
            },
            noDataConfig = NoDataConfig(
                title = "No Organization Items",
                description = "There are no organization verification items to display"
            )
        )
    }

    private fun setupFinRecyclerView() {
        finItems = getFinItems()

        finAdapter = setupRecyclerView(
            recyclerView = binding.recyclerViewFin,
            items = finItems,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = ItemFieldVerCardBinding::inflate,
            onBind = { item, binding, position ->
                bindFieldVerificationItem(item, binding, position, "fin")
            },
            noDataConfig = NoDataConfig(
                title = "No Financial Items",
                description = "There are no financial verification items to display"
            )
        )
    }

    private fun setupTrainingRecyclerView() {
        trainingItems = getTrainingItems()

        trainingAdapter = setupRecyclerView(
            recyclerView = binding.recyclerViewTraining,
            items = trainingItems,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = ItemFieldVerCardBinding::inflate,
            onBind = { item, binding, position ->
                bindFieldVerificationItem(item, binding, position, "training")
            },
            noDataConfig = NoDataConfig(
                title = "No Training Items",
                description = "There are no training verification items to display"
            )
        )
    }

    private fun setupTrainingInfraRecyclerView() {
        trainingInfraItems = getTrainingInfraItems()

        trainingInfraAdapter = setupRecyclerView(
            recyclerView = binding.recyclerViewTrainingInfra,
            items = trainingInfraItems,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = ItemFieldVerCardBinding::inflate,
            onBind = { item, binding, position ->
                bindFieldVerificationItem(item, binding, position, "trainingInfra")
            },
            noDataConfig = NoDataConfig(
                title = "No Training Infrastructure Items",
                description = "There are no training infrastructure verification items to display"
            )
        )
    }

    private fun setupCertRecyclerView() {
        certItems = getCertItems()

        certAdapter = setupRecyclerView(
            recyclerView = binding.recyclerViewCert,
            items = certItems,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = ItemFieldVerCardBinding::inflate,
            onBind = { item, binding, position ->
                bindFieldVerificationItem(item, binding, position, "cert")
            },
            noDataConfig = NoDataConfig(
                title = "No Certification Items",
                description = "There are no certification verification items to display"
            )
        )
    }

    private fun setupPlacementRecyclerView() {
        placementItems = getPlacementItems()

        placementAdapter = setupRecyclerView(
            recyclerView = binding.recyclerViewPlacement,
            items = placementItems,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = ItemFieldVerCardBinding::inflate,
            onBind = { item, binding, position ->
                bindFieldVerificationItem(item, binding, position, "placement")
            },
            noDataConfig = NoDataConfig(
                title = "No Placement Items",
                description = "There are no placement verification items to display"
            )
        )
    }

    private fun setupFieldRecyclerView() {
        fieldItems = getFieldItems()

        fieldAdapter = setupRecyclerView(
            recyclerView = binding.recyclerViewField,
            items = fieldItems,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = ItemFieldVerCardBinding::inflate,
            onBind = { item, binding, position ->
                bindFieldVerificationItem(item, binding, position, "field", showIcons = false)
            },
            noDataConfig = NoDataConfig(
                title = "No Field Visit Items",
                description = "There are no field visit verification items to display"
            )
        )
    }

    private fun bindFieldVerificationItem(
        item: FieldVerificationItem,
        binding: ItemFieldVerCardBinding,
        position: Int,
        section: String,
        showIcons: Boolean = true
    ) {
        binding.tvReqTitle.text = item.requirement
        binding.tvVerification.text = item.verificationDoc

        // Clear chips then add
        binding.chipgroupDocuments.removeAllViews()
        for (doc in item.documents) {
            val chip = Chip(binding.root.context).apply {
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
                    if (item.uploadEnabled) {
                        handleUploadClick(section, position, doc)
                    } else {
                        handleViewClick(section, position, doc)
                    }
                }
            }
            binding.chipgroupDocuments.addView(chip)
        }

        // Handle image preview
        handleImagePreview(item, binding)

        // Handle remarks
        handleRemarks(item, binding, position)
    }

    private fun handleImagePreview(item: FieldVerificationItem, binding: ItemFieldVerCardBinding) {
        if (!item.imageUri.isNullOrBlank()) {
            try {
                binding.imageGroup.visibility = View.VISIBLE
                if (currentPhotoTarget == "Training Centre") {
                    binding.ivPreview1.setImageURI(Uri.parse(item.imageUri))
                    binding.ivPreview1.visibility = View.VISIBLE
                    binding.ivPreview.visibility = View.GONE
                } else {
                    binding.ivPreview.setImageURI(Uri.parse(item.imageUri))
                    binding.ivPreview.visibility = View.VISIBLE
                    binding.ivPreview1.visibility = View.GONE
                }
            } catch (e: Exception) {
                binding.ivPreview.visibility = View.GONE
                binding.ivPreview1.visibility = View.GONE
                binding.imageGroup.visibility = View.GONE
            }
        } else {
            binding.ivPreview.visibility = View.GONE
            binding.ivPreview1.visibility = View.GONE
            binding.imageGroup.visibility = View.GONE
        }
    }

    private fun handleRemarks(
        item: FieldVerificationItem,
        binding: ItemFieldVerCardBinding,
        position: Int
    ) {
        if (item.allowRemark) {
            binding.remarkGroup.visibility = View.VISIBLE

            // Prefill from item's stored value
            val current = item.remarkText ?: ""
            if (binding.etSectionRemark.text?.toString() != current) {
                binding.etSectionRemark.setText(current)
            }

            // Remove previous watcher to avoid duplicates
            binding.etSectionRemark.tag?.let { prev ->
                if (prev is TextWatcher) {
                    binding.etSectionRemark.removeTextChangedListener(prev)
                }
            }

            val watcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    item.remarkText = s?.toString()?.trim()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }

            binding.etSectionRemark.addTextChangedListener(watcher)
            binding.etSectionRemark.tag = watcher
        } else {
            binding.remarkGroup.visibility = View.GONE
            binding.etSectionRemark.tag?.let { prev ->
                if (prev is TextWatcher) {
                    binding.etSectionRemark.removeTextChangedListener(prev)
                }
            }
            binding.etSectionRemark.tag = null
        }
    }

    private fun handleUploadClick(section: String, position: Int, doc: String) {
        currentUploadPosition = position
        currentUploadList = section

        currentPhotoTarget = when {
            section == "fin" && doc == resources.getString(R.string.fin_turnover_button) -> "Turnover"
            section == "training" && doc == resources.getString(R.string.train_tailor_button) -> "Additional tailor-made training If Yes Upload"
            section == "trainingInfra" && doc == "Self Declaration" -> "Self Declaration"
            section == "trainingInfra" && doc == "Training Centre" -> "Training Centre"
            else -> doc
        }

        checkAndLaunchCamera()
    }

    private fun handleViewClick(section: String, position: Int, doc: String) {
        when (section) {
            "org" -> handleOrgViewClick(position, doc)
            "fin" -> handleFinViewClick(position, doc)
            "training" -> handleTrainingViewClick(position, doc)
            "trainingInfra" -> handleTrainingInfraViewClick(position, doc)
            "cert" -> handleCertViewClick(position, doc)
            "placement" -> handlePlacementViewClick(position, doc)
        }
    }

    private fun handleOrgViewClick(position: Int, doc: String) {
        when {
            position == 0 && doc == "Date of Incorporation (PRN)" -> {
                showSimpleDialog("Date of incorporation", apiDateOfIncorporation ?: "Not Available")
            }

            position == 1 && doc == "View Registration Document" -> {
                showIndustryRegistrationDialog()
            }

            position == 2 && doc == "EPFO Challan (6 Months)" -> {
                showEpfoChallanDialog()
            }

            position == 3 && doc == "View" -> {
                showTaxDetailsDialog()
            }

            position == 4 && doc == "View Account Details" -> {
                showBankDetailsDialog()
            }
        }
    }

    private fun handleFinViewClick(position: Int, doc: String) {
        when {
            position == 0 && doc == resources.getString(R.string.fin_balance_sheet_button) -> {
                val items = apiAnnualTurnoverList?.map { it.toYearlyItem() }
                showFinancialDialog(resources.getString(R.string.fin_annual_turnover), items)
            }

            position == 1 && doc == resources.getString(R.string.fin_turnover_button) -> {
                val items = apiNetWorthList?.map { it.toYearlyItem() }
                showFinancialDialog(resources.getString(R.string.fin_net_worth), items)
            }
        }
    }

    private fun handleTrainingViewClick(position: Int, doc: String) {
        when {
            position == 0 && doc == resources.getString(R.string.train_target_button) -> {
                val items = apiTrainingCriteriaList?.map { it.toYearlyTrainingItem() }
                showTrainingDialog("Training Details", items)
            }

            position == 3 && doc == resources.getString(R.string.train_NSQF_course_button) -> {
                showDocumentDialog("Basic Training", apiBasicSelfDeclarationBase64, doc)
            }

            position == 4 && doc == resources.getString(R.string.train_commitment1_button) -> {
                showDocumentDialog("Captive Employers Commitment", apiCommitmentForm1Base64, doc)
            }

            position == 4 && doc == resources.getString(R.string.train_commitment2_button) -> {
                showDocumentDialog("Captive Employers Commitment", apiCommitmentForm2Base64, doc)
            }

            position == 5 && doc == resources.getString(R.string.train_tailor_button) -> {
                showDocumentDialog("Tailor Training Doc", apiTailorTrainingDocBase64, doc)
            }

            position == 6 && doc == resources.getString(R.string.train_domain1_button) -> {
                showDocumentDialog("Domain Specific Training", apiDomainForm1Base64, doc)
            }

            position == 6 && doc == resources.getString(R.string.train_domain2_button) -> {
                showDocumentDialog("Domain Specific Training", apiDomainForm2Base64, doc)
            }
        }
    }

    private fun handleTrainingInfraViewClick(position: Int, doc: String) {
        if (position == 1 && doc == "View Residential Facilities") {
            showResidentialFacilitiesDialog()
        }
    }

    private fun handleCertViewClick(position: Int, doc: String) {
        when {
            position == 0 && doc == "Form 4" -> {
                showDocumentDialog("Awarding Body", apiAwardBodyCommitBase64, "View Form 4")
            }

            position == 1 && doc == "Form 4" -> {
                showDocumentDialog(
                    "Certification for 70% Candidates",
                    apiSeventyPctCommitBase64,
                    "View Form 4"
                )
            }
        }
    }

    private fun handlePlacementViewClick(position: Int, doc: String) {
        when {
            position == 0 && doc == "View Employment Details" -> {
                showPlacementDialog("Placement Details", apiPlacementList)
            }

            position == 1 && doc == "Form 1" -> {
                showDocumentDialog("Six Months", apiCommitmentSixMonthsBase64, "View Form 1")
            }

            position == 2 && doc == "Form 1" -> {
                showDocumentDialog(
                    "Commitment Less than Six Months",
                    apiCommitmentLessSixMonthsBase64,
                    "View Form 1"
                )
            }

            position == 3 && doc == "Form 1" -> {
                showDocumentDialog(
                    "Commitment Greater than Six Months",
                    apiCommitmentMoreSixMonthsBase64,
                    "View Form 1"
                )
            }
        }
    }

    private fun setupToolbar() {
        setupToolbar(
            root = binding.root,
            titleRes = R.string.field_ver_head,
            showBack = true,
            showLang = false,
            showProfile = false,
            backClick = { findNavController().navigateUp()}
        )
//        binding.backButton.setOnClickListener {
//            findNavController().navigateUp()
//        }
    }

    private fun setupNavigationButtons() {
        binding.btnFinPrevious.setOnClickListener { navigateToPreviousSection("fin") }
        binding.btnTrainingPrevious.setOnClickListener { navigateToPreviousSection("training") }
        binding.btnTrainingInfraPrevious.setOnClickListener { navigateToPreviousSection("trainingInfra") }
        binding.btnCertPrevious.setOnClickListener { navigateToPreviousSection("cert") }
        binding.btnPlacementPrevious.setOnClickListener { navigateToPreviousSection("placement") }
        binding.btnFieldPrevious.setOnClickListener { navigateToPreviousSection("field") }
    }

    private fun setupSubmitButtons() {
        binding.btnInfoNext.setOnClickListener { handleOrgSubmit() }
        binding.btnFinNext.setOnClickListener { handleFinSubmit() }
        binding.btnTrainingNext.setOnClickListener { handleTrainingSubmit() }
        binding.btnTrainingInfraNext.setOnClickListener { handleTrainingInfraSubmit() }
        binding.btnCertNext.setOnClickListener { handleCertSubmit() }
        binding.btnPlacementNext.setOnClickListener { handlePlacementSubmit() }
        binding.btnFieldNext.setOnClickListener { handleFieldSubmit() }
    }

    private fun navigateToPreviousSection(currentSection: String) {
        val sectionMap = mapOf(
            "fin" to Pair(binding.verOrg, binding.verFin),
            "training" to Pair(binding.verFin, binding.verTraining),
            "trainingInfra" to Pair(binding.verTraining, binding.verTrainingInfra),
            "cert" to Pair(binding.verTrainingInfra, binding.verCert),
            "placement" to Pair(binding.verCert, binding.verPlacement),
            "field" to Pair(binding.verPlacement, binding.verField)
        )

        sectionMap[currentSection]?.let { (showSection, hideSection) ->
            showSection.visibility = View.VISIBLE
            hideSection.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }
        }
    }

    // ==================== SUBMIT HANDLERS ====================

    private fun handleOrgSubmit() {
        val allManpowerRemarks = validateAllRemarksForSection(orgItems) ?: return
        binding.trainingInfraExpand.visibility = View.GONE
        binding.tvTrainInfra.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_verified,
            0
        )
        binding.verFin.visibility = View.VISIBLE
        binding.verFinExpand.visibility = View.VISIBLE

        val request = FieldVerificationDetailRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            captiveEmpanelmentId = captiveEmpanelmentId,
            prnNo = prnNo
        )

        viewModel.getFieldVerificationFinDetail(request)
        observeFinDetails()

        if (hasLocationPermission()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun handleFinSubmit() {
        binding.verFinExpand.visibility = View.GONE
        binding.tvFinHead.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
        binding.verTraining.visibility = View.VISIBLE
        binding.verTrainingExpand.visibility = View.VISIBLE

        val request = FieldVerificationDetailRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            captiveEmpanelmentId = captiveEmpanelmentId,
            prnNo = prnNo
        )

        viewModel.getFieldVerificationTrainingDetail(request)
        observeTrainingDetails()
    }

    private fun handleTrainingSubmit() {
        val allTrainingRemarks = validateAllRemarksForSection(trainingItems) ?: return
        binding.verTrainingExpand.visibility = View.GONE
        binding.tvTrainingHead.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_verified,
            0
        )
        binding.verTrainingInfra.visibility = View.VISIBLE
        binding.verTrainingInfraExpand.visibility = View.VISIBLE

        val request = FieldVerificationDetailRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            captiveEmpanelmentId = captiveEmpanelmentId,
            prnNo = prnNo
        )

        viewModel.getFieldVerificationTrainingInfraDetail(request)
        observeTrainingInfraDetails()
    }

    private fun handleTrainingInfraSubmit() {
        if (base64TrainingInfraDeclarationFile.isNullOrBlank()) {
            showToast("Please Capture Self Declaration")
            return
        }
        if (base64TrainingInfraCentreFile.isNullOrBlank()) {
            showToast("Please Capture Training Centre")
            return
        }

        binding.verTrainingInfraExpand.visibility = View.GONE
        binding.tvTrainingInfraHead.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_verified,
            0
        )
        binding.verCert.visibility = View.VISIBLE
        binding.verCertExpand.visibility = View.VISIBLE

        val request = FieldVerificationDetailRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            captiveEmpanelmentId = captiveEmpanelmentId,
            prnNo = prnNo
        )

        viewModel.getFieldVerificationCertificationDetail(request)
        observeCertificationDetails()
    }

    private fun handleCertSubmit() {
        binding.verCertExpand.visibility = View.GONE
        binding.tvCertHead.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
        binding.verPlacement.visibility = View.VISIBLE
        binding.verPlacementExpand.visibility = View.VISIBLE

        val request = FieldVerificationDetailRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            captiveEmpanelmentId = captiveEmpanelmentId,
            prnNo = prnNo
        )

        viewModel.getFieldVerificationPlacementDetail(request)
        observePlacementDetails()
    }

    private fun handlePlacementSubmit() {
        binding.verPlacementExpand.visibility = View.GONE
        binding.tvPlacementHead.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_verified,
            0
        )
        binding.verField.visibility = View.VISIBLE
        binding.verFieldExpand.visibility = View.VISIBLE
    }

//    private fun handleFieldSubmit() {
//        binding.verFieldExpand.visibility = View.GONE
//        binding.tvFieldHead.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
//
//        val sectionMap = collectAllRemarksSectionWise()
//        val manpowerFound = sectionMap["Organization"]?.any {
//            it.requirement.contains(
//                "Manpower",
//                ignoreCase = true
//            )
//        } ?: false
//
//        Log.d("section Map :: ", sectionMap.toString())
//        Log.d("manpowerFound :: ", manpowerFound.toString())
//    }


    private fun handleFieldSubmit() {
        binding.verFieldExpand.visibility = View.GONE
        binding.tvFieldHead.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)

        // collect all remarks
        val finalSubmitData = collectAllRemarksSectionWise()

        // Call submit API
        viewModel.submitFieldVerification(finalSubmitData)

        // Observe the LiveData once (remove previous observers first for safety)
        viewModel.submitFieldVerificationDetails.removeObservers(viewLifecycleOwner)
        viewModel.submitFieldVerificationDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                try {
                    val item = response.responseCode.toInt()
                    if (item == 200){
                        Log.d("Field Verification Submit if", item.toString())
                        val navController = findNavController()

                        // Signal to the list that it should refresh
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("refresh_pia_list", true)

                        navController.navigateUp()
                    } else {
                        Log.d("Field Verification Submit else", item.toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    showErrorToast("Failed processing submitFieldVerification response: ${e.message}")
                }
            }.onFailure { e ->
                showErrorToast("SubmitFieldVerification API failed: ${e.message ?: "Unknown"}")
            }
        }
    }

    // ==================== OBSERVERS ====================

    private fun observeFieldDetails() {
        viewModel.fieldDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                val item = response.wrappedList.firstOrNull()
                apiDateOfIncorporation =
                    item?.organizationDetails?.proofOfIndustryExistence?.dateOfIncorporation
                apiBankName = item?.organizationDetails?.bankDetails?.bankName
                apiManpowerRemarks = item?.organizationDetails?.manpowerAgencyCheck?.remarks
                apiEpfoExistingStaff =
                    item?.organizationDetails?.epfoChallans?.existingStaffRegisteredInEpfo
                apiEpfoDocumentUrl = item?.organizationDetails?.epfoChallans?.epfoDocument
                apiGstNumber = item?.organizationDetails?.taxDetails?.gstNumber
                apiTanNumber = item?.organizationDetails?.taxDetails?.tanNumber
                apiTanAttachmentBase64 = item?.organizationDetails?.taxDetails?.tanAttachment
                apiBankAccountNumber = item?.organizationDetails?.bankDetails?.bankAccountNumber
                apiBankLetterBase64 = item?.organizationDetails?.bankDetails?.bankLetterDocument
                apiSelfDeclarationBase64 =
                    item?.organizationDetails?.bankDetails?.selfDeclarationDocument
                apiEpfoNumber = item?.organizationDetails?.industryRegistration?.epfoNumber
                apiEsicNumber = item?.organizationDetails?.industryRegistration?.esicNumber
                apiFactoryRegNumber =
                    item?.organizationDetails?.industryRegistration?.factoryRegistrationNumber
                apiEpfoAttachmentBase64 =
                    item?.organizationDetails?.industryRegistration?.epfoAttachment
                apiEsicAttachmentBase64 =
                    item?.organizationDetails?.industryRegistration?.esicAttachment
                apiFactoryAttachmentBase64 =
                    item?.organizationDetails?.industryRegistration?.factoryRegistrationAttachment

                Log.d("FIELD_API", "DOI = $apiDateOfIncorporation")
                Log.d("FIELD_API", "EPFO = $apiEpfoNumber")
            }.onFailure {

                showErrorToast(it.message ?: getString(R.string.failed_to_fetch_details))
            }
        }
    }

    private fun observeFinDetails() {
        viewModel.finDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                try {
                    val item = response.wrappedList?.firstOrNull()
                    val financialDetails = item?.financialDetails
                    apiAnnualTurnoverList = financialDetails?.annualTurnover
                    apiNetWorthList = financialDetails?.netWorth

                    Log.d(
                        "FieldVerify",
                        "Annual turnover count: ${financialDetails?.annualTurnover?.size}"
                    )
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
                    val item = response.wrappedList?.firstOrNull()
                    val trainingDetails = item?.trainingDetails
                    apiTrainingCriteriaList = trainingDetails?.trainingCriteria
                    apiTotalTrainingHoursRemarks = trainingDetails?.totalTrainingHoursRemarks
                    apiRepetitionClubbingRemarks = trainingDetails?.repetitionClubbingRemarks
                    apiBasicSelfDeclarationBase64 =
                        trainingDetails?.basicTraining?.selfDeclarationTrainingDoc
                    apiCommitmentForm1Base64 = trainingDetails?.commitment?.form1
                    apiCommitmentForm2Base64 = trainingDetails?.commitment?.form2
                    apiTailorTrainingDocBase64 =
                        trainingDetails?.trainingPlacement?.tailorTrainingDoc
                    apiDomainForm1Base64 = trainingDetails?.domainSpecificTraining?.form1
                    apiDomainForm2Base64 = trainingDetails?.domainSpecificTraining?.form2
                } catch (e: Exception) {
                    showErrorToast(
                        getString(
                            R.string.failed_processing_training_response,
                            e.message
                        ))
                }
            }.onFailure {
                showErrorToast(getString(R.string.training_api_failed, it.message ?: getString(R.string.unknown)))
            }
        }
    }

    private fun observeTrainingInfraDetails() {
        viewModel.trainingInfraDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                try {
                    val item = response.wrappedList?.firstOrNull()
                    val infra = item?.trainingInfrastrutureDetails
                    val residential = infra?.residentialFacilityDetails
                    apiResidentialFacilityAvailable = residential?.residentialFacilityAvailable
                    apiResidentialFacilityDocumentBase64 = residential?.residentialFacilityDocument

                    Log.d("FieldVerify", "Residential available = $apiResidentialFacilityAvailable")
                } catch (e: Exception) {
                    showErrorToast(
                        getString(
                            R.string.failed_processing_training_infra_response,
                            e.message
                        ))
                }
            }.onFailure {
                showErrorToast(
                    getString(
                        R.string.training_infra_api_failed,
                        it.message ?:  getString(R.string.unknown)
                    ))
            }
        }
    }

    private fun observeCertificationDetails() {
        viewModel.certificationDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                try {
                    val item = response.wrappedList?.firstOrNull()
                    val infra = item?.assessmentCertificationDetails
                    val commitment = infra?.commitmentLetterDetails
                    apiAwardBodyCommitBase64 = commitment?.awardBodyCommit
                    apiSeventyPctCommitBase64 = commitment?.seventyPctCommit

                    Log.d(
                        "assessmentCertificationDetails",
                        "Award body available = ${!apiAwardBodyCommitBase64.isNullOrBlank()}"
                    )
                } catch (e: Exception) {
                    showErrorToast(
                        getString(
                            R.string.failed_processing_assessment_certification_response,
                            e.message
                        ))
                }
            }.onFailure {
                showErrorToast(
                    getString(
                        R.string.assessment_certification_api_failed,
                        it.message ?: getString(R.string.unknown)
                    ))
            }
        }
    }

    private fun observePlacementDetails() {
        viewModel.placementDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                try {
                    val item = response.wrappedList?.firstOrNull()
                    val infra = item?.placementDetails
                    apiPlacementList = infra?.yearWisePlacementDetails
                    apiCommitmentSixMonthsBase64 = infra?.commitment?.commitmentSixMonths
                    apiCommitmentLessSixMonthsBase64 = infra?.commitment?.commitmentLessSixMonths
                    apiCommitmentMoreSixMonthsBase64 = infra?.commitment?.commitmentMoreSixMonths

                    Log.d("FIELD_API", "Placement List = $apiPlacementList")
                } catch (e: Exception) {
                    showErrorToast(
                        getString(
                            R.string.failed_processing_placement_response,
                            e.message
                        ))
                }
            }.onFailure {
                showErrorToast(getString(R.string.placement_api_failed, it.message ?: getString(R.string.unknown)))
            }
        }
    }

    // ==================== CAMERA & PERMISSION METHODS ====================

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
        val photoFile = createImageFile()
        if (photoFile == null) {
            showToast("Failed to create image file")
            return
        }
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
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

    private fun handleCameraSuccess() {
        if (currentUploadPosition >= 0) {
            val pos = currentUploadPosition

            when (currentUploadList) {
                "fin" -> {
                    val existing = finItems.getOrNull(pos)
                    if (existing != null) {
                        finItems[pos] = existing.copy(
                            imageUri = photoUri.toString(),
                            uploadEnabled = true,
                            attachments = listOf(
                                AttachmentItem(
                                    label = "Turnover",
                                    value = base64FinanceFile ?: ""
                                )
                            )
                        )
                        updateRecyclerViewData(binding.recyclerViewFin.id, finItems)
                    }
                }
                "training" -> {
                    val existing = trainingItems.getOrNull(pos)
                    if (existing != null) {
                        trainingItems[pos] = existing.copy(
                            imageUri = photoUri.toString(),
                            uploadEnabled = true,
                            attachments = listOf(
                                AttachmentItem(
                                    label = "Additional Training",
                                    value = base64TrainingFile ?: ""
                                )
                            )
                        )
                        updateRecyclerViewData(binding.recyclerViewTraining.id, trainingItems)
                    }
                }
                "Self Declaration" -> {
                    val existing = trainingInfraItems.getOrNull(pos)
                    if (existing != null) {
                        trainingInfraItems[pos] = existing.copy(
                            imageUri = photoUri.toString(),
                            uploadEnabled = true
                        )
                        base64TrainingInfraDeclarationFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        updateRecyclerViewData(binding.recyclerViewTrainingInfra.id, trainingInfraItems)
                    }
                }
                "Training Centre" -> {
                    val existing = trainingInfraItems.getOrNull(pos)
                    if (existing != null) {
                        trainingInfraItems[pos] = existing.copy(
                            imageUri = photoUri.toString(),
                            uploadEnabled = true
                        )
                        base64TrainingInfraCentreFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        updateRecyclerViewData(binding.recyclerViewTrainingInfra.id, trainingInfraItems)
                    }
                }
            }

            // reset flags
            currentUploadPosition = -1
            currentUploadList = ""
        }

        // Store base64 files for later submission
        when (currentPhotoTarget) {
            "Turnover" -> {
                base64FinanceFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
            }
            "Additional tailor-made training If Yes Upload" -> {
                base64TrainingFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
            }
            "Self Declaration" -> {
                base64TrainingInfraDeclarationFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
            }
            "Training Centre" -> {
                base64TrainingInfraCentreFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
            }
            "Residential Facilities" -> {
                base64TrainingResFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
            }
        }
    }
    // ==================== LOCATION METHODS ====================

    private fun setupLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocation = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return fineLocation == PackageManager.PERMISSION_GRANTED || coarseLocation == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted || coarseLocationGranted) {
                getCurrentLocation()
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

                    if (::fieldAdapter.isInitialized) {
                        fieldItems = mutableListOf(
                            FieldVerificationItem(
                                id = "",
                                resources.getString(R.string.field_ver_geo_factory_field),
                                resources.getString(R.string.field_ver_ctsa_off_note_field),
                                listOf("Lat: $latitude", "Long: $longitude"),
                                uploadEnabled = false,
                                allowRemark = false
                            )
                        )
                        updateRecyclerViewData(binding.recyclerViewField.id, fieldItems)
                    }
                } else {
                    showToast("Unable to get location")
                }
            }
            .addOnFailureListener {
                showToast(getString(R.string.failed_to_get_location, it.message ?: ""))
            }
    }

    // ==================== DIALOG METHODS ====================

    private fun showSimpleDialog(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .create()
            .show()
    }

    private fun showIndustryRegistrationDialog() {
        val msg = buildString {
            appendLine("EPFO Number: ${apiEpfoNumber ?: "NA"}")
            appendLine("ESIC Number: ${apiEsicNumber ?: "NA"}")
            appendLine("Factory Registration Number: ${apiFactoryRegNumber ?: "NA"}")
        }.trim()

        val actions = buildList {
            if (!apiEpfoAttachmentBase64.isNullOrBlank()) {
                add(DocAction("View EPFO") { openBase64Pdf(apiEpfoAttachmentBase64!!) })
            }
            if (!apiEsicAttachmentBase64.isNullOrBlank()) {
                add(DocAction("View ESIC") { openBase64Pdf(apiEsicAttachmentBase64!!) })
            }
            if (!apiFactoryAttachmentBase64.isNullOrBlank()) {
                add(DocAction("View Factory") { openBase64Pdf(apiFactoryAttachmentBase64!!) })
            }
        }

        showInfoWithHorizontalButtonsDialog("Industry Registration", msg, actions)
    }

    private fun showEpfoChallanDialog() {
        val message =
            "Existing staff registered in EPFO: ${apiEpfoExistingStaff ?: "Not Available"}"
        val actions = buildList {
            if (!apiEpfoDocumentUrl.isNullOrBlank()) {
                add(DocAction("View EPFO Challan") { openBase64Pdf(apiEpfoDocumentUrl!!) })
            }
        }
        showInfoWithHorizontalButtonsDialog("EPFO Challan (Last 6 Months)", message, actions)
    }

    private fun showTaxDetailsDialog() {
        val msg = buildString {
            appendLine("GST number: ${apiGstNumber ?: "—"}")
            appendLine("TAN number: ${apiTanNumber ?: "—"}")
        }.trim()
        val actions = buildList {
            if (!apiTanAttachmentBase64.isNullOrBlank()) {
                add(DocAction("View TAN") { openBase64Pdf(apiTanAttachmentBase64!!) })
            }
        }
        showInfoWithHorizontalButtonsDialog("Tax Details", msg, actions)
    }

    private fun showBankDetailsDialog() {
        val msg = buildString {
            appendLine("Bank Account Number: ${apiBankAccountNumber ?: "—"}")
        }.trim()
        val actions = buildList {
            if (!apiBankLetterBase64.isNullOrBlank()) {
                add(DocAction("View Bank Letter") { openBase64Pdf(apiBankLetterBase64!!) })
            }
            if (!apiSelfDeclarationBase64.isNullOrBlank()) {
                add(DocAction("View Self-Declaration") { openBase64Pdf(apiSelfDeclarationBase64!!) })
            }
        }
        showInfoWithHorizontalButtonsDialog("Bank Details", msg, actions)
    }

    private fun showResidentialFacilitiesDialog() {
        val msg = buildString {
            appendLine("Residential Facility Available: ${apiResidentialFacilityAvailable ?: "—"}")
        }.trim()
        if (!apiResidentialFacilityDocumentBase64.isNullOrBlank()) {
            val actions = buildList {
                add(DocAction("Residential Facilities") {
                    openBase64Pdf(
                        apiResidentialFacilityDocumentBase64!!
                    )
                })
            }
            showInfoWithHorizontalButtonsDialog("Residential Facilities", msg, actions)
        } else {
            showToast(getString(R.string.no_residential_facilities_to_view))
        }
    }

    private fun showDocumentDialog(title: String, base64: String?, buttonText: String) {
        if (!base64.isNullOrBlank()) {
            val actions = buildList {
                add(DocAction(buttonText) { openBase64Pdf(base64) })
            }
            showInfoWithHorizontalButtonsDialog(title, "", actions)
        } else {
            showToast(getString(R.string.no_file_to_view))
        }
    }

    private fun showInfoWithHorizontalButtonsDialog(
        title: String,
        message: String,
        actions: List<DocAction>
    ) {
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
            val tv = TextView(ctx).apply {
                text = message
                setTextColor(ContextCompat.getColor(ctx, android.R.color.black))
                textSize = 14f
            }
            container.addView(tv)
        }

        val row = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            weightSum = actions.size.takeIf { it > 0 }?.toFloat() ?: 1f
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = (16 * dp).toInt()
            }
            clipToPadding = false
        }
        container.addView(row)

        var dialog: AlertDialog? = null

        val strokeColor = try {
            ContextCompat.getColor(ctx, R.color.color_dark_blue)
        } catch (_: Exception) {
            ContextCompat.getColor(ctx, android.R.color.holo_blue_dark)
        }
        val textColor = strokeColor
        val rippleColor = ColorUtils.setAlphaComponent(strokeColor, 80)
        val transparentFill = android.graphics.Color.TRANSPARENT

        fun makeButtonBackground(): android.graphics.drawable.Drawable {
            val cornerRadius = (10 * dp)
            val strokeWidth = (1.8f * dp).toInt()

            val rounded = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                this.cornerRadius = cornerRadius
                setColor(transparentFill)
                setStroke(strokeWidth, strokeColor)
            }

            val mask = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                this.cornerRadius = cornerRadius
                setColor(android.graphics.Color.WHITE)
            }

            val inset = (1.5f * dp).toInt()
            val insetDrawable = InsetDrawable(rounded, inset, inset, inset, inset)

            return RippleDrawable(ColorStateList.valueOf(rippleColor), insetDrawable, mask)
        }

        actions.forEach { action ->
            val btn = AppCompatButton(ctx).apply {
                text = action.label
                isAllCaps = false
                setTextColor(textColor)
                layoutParams =
                    LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
                        marginStart = (6 * dp).toInt()
                        marginEnd = (6 * dp).toInt()
                    }
                minHeight = (44 * dp).toInt()
                setPadding(
                    (10 * dp).toInt(),
                    (10 * dp).toInt(),
                    (10 * dp).toInt(),
                    (10 * dp).toInt()
                )
                background = makeButtonBackground()
                stateListAnimator = null
                setOnClickListener {
                    dialog?.dismiss()
                    action.onClick()
                }
            }
            row.addView(btn)
        }

        dialog = AlertDialog.Builder(ctx)
            .setTitle(title)
            .setView(container)
            .setNegativeButton(resources.getString(R.string.close), null)
            .create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        dialog.show()
    }

//    var titleView = TextView(ctx).apply {
//        text = title
//        textSize = 18f
//        setTextColor(ContextCompat.getColor(ctx, android.R.color.black))
//        gravity = Gravity.CENTER
//        setPadding(0, (12 * dp).toInt(), 0, (12 * dp).toInt())
//    }

    // ==================== VALIDATION & UTILITY METHODS ====================

    private fun validateAllRemarksForSection(
        items: List<FieldVerificationItem>,
        errorMessageForItem: (FieldVerificationItem) -> String = {
            getString(
                R.string.please_enter_remark_for,
                it.requirement
            ) }
    ): Map<String, String>? {
        commitFocusedEditText()

        val remarkItems = items.filter { it.allowRemark }
        if (remarkItems.isEmpty()) return emptyMap()

        remarkItems.forEach { item ->
            val r = item.remarkText?.trim().orEmpty()
            if (r.isEmpty()) {
                showToast(errorMessageForItem(item))
                return null
            }
        }

        return remarkItems.associate { it.requirement to (it.remarkText!!.trim()) }
    }

//    private fun collectRemarksFromSection(
//        sectionName: String,
//        sectionItems: List<FieldVerificationItem>
//    ): List<RemarkItem> {
//        commitFocusedEditText()
//        return sectionItems
//            .filter { it.allowRemark }
//            .mapNotNull { item ->
//                val remark = item.remarkText?.trim().orEmpty()
//                if (remark.isEmpty()) null
//                else RemarkItem(section = sectionName, requirement = item.id, remark = remark)
//            }
//    }

    private fun collectRemarksFromSection(
        sectionName: String,
        sectionItems: List<FieldVerificationItem>
    ): List<RemarkItem> {
        commitFocusedEditText()

        // collect normal per-item remarks (only items with allowRemark)
        val remarkList = sectionItems
            .filter { it.allowRemark }
            .mapNotNull { item ->
                val r = item.remarkText?.trim().orEmpty()
                if (r.isEmpty()) null
                else RemarkItem(section = sectionName, requirement = item.id, remark = r)
            }
            .toMutableList()

        // Special case: attach captured base64 files into TrainingInfra section
        if (sectionName.equals("TrainingInfra", ignoreCase = true)) {
            val attachments = mutableListOf<AttachmentItem>()

            if (!base64TrainingInfraDeclarationFile.isNullOrBlank()) {
                attachments.add(
                    AttachmentItem(
                        label = "Self Declaration",
                        value = base64TrainingInfraDeclarationFile!!
                    )
                )
            }
            if (!base64TrainingInfraCentreFile.isNullOrBlank()) {
                attachments.add(
                    AttachmentItem(
                        label = "Training Centre File",
                        value = base64TrainingInfraCentreFile!!
                    )
                )
            }
            if (attachments.isNotEmpty()) {
                // Add a single RemarkItem carrying the attachments map
                remarkList.add(
                    RemarkItem(
                        section = sectionName,
                        requirement = "TRAIN_INFRA_ATTACHMENTS",
                        remark = "",
                        attachments = attachments
                    )
                )
            }
        }

        // --- Add Field Visit coordinates to collected remarks ---
        if (sectionName.equals("FieldVisit", ignoreCase = true)) {
            val attachList = mutableListOf<AttachmentItem>()

            if (!latitude.isNullOrBlank()) {
                attachList.add(AttachmentItem(label = "latitude", value = latitude))
            }
            if (!longitude.isNullOrBlank()) {
                attachList.add(AttachmentItem(label = "longitude", value = longitude))
            }

            if (attachList.isNotEmpty()) {
                remarkList.add(
                    RemarkItem(
                        section = sectionName,
                        requirement = "FIELD_VISIT_COORDINATES",
                        remark = "",
                        attachments = attachList
                    )
                )
            }
        }
        return remarkList
    }

    // Alternative if API expects different structure
//    data class FieldVerificationFinalSubmit(
//        val appVersion: String,
//        val loginId: String,
//        val captiveEmpanelmentId: String,
//        val prnNo: String,
//        val remarks: List<RemarkItem> // All remarks flattened
//    )

    // Then modify collectAllRemarksSectionWise to flatten all remarks:
    private fun collectAllRemarksSectionWise(): FieldVerificationFinalSubmit {
        commitFocusedEditText()

        val allRemarks = mutableListOf<RemarkItem>().apply {
            addAll(collectRemarksFromSection("Organization", orgItems))
            addAll(collectRemarksFromSection("Finance", finItems))
            addAll(collectRemarksFromSection("Training", trainingItems))
            addAll(collectRemarksFromSection("TrainingInfra", trainingInfraItems))
            addAll(collectRemarksFromSection("Certification", certItems))
            addAll(collectRemarksFromSection("Placement", placementItems))
            addAll(collectRemarksFromSection("FieldVisit", fieldItems))
        }

        return FieldVerificationFinalSubmit(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            captiveEmpanelmentId = captiveEmpanelmentId,
            prnNo = prnNo,
            remarks = allRemarks
        )
    }


//    private fun collectAllRemarksSectionWise(): Map<String, List<RemarkItem>> {
//        commitFocusedEditText()
//        val result = FieldVerificationDetailRequest
//        return mapOf(
//            "Organization" to collectRemarksFromSection("Organization", orgItems),
//            "Finance" to collectRemarksFromSection("Finance", finItems),
//            "Training" to collectRemarksFromSection("Training", trainingItems),
//            "TrainingInfra" to collectRemarksFromSection("TrainingInfra", trainingInfraItems),
//            "Certification" to collectRemarksFromSection("Certification", certItems),
//            "Placement" to collectRemarksFromSection("Placement", placementItems),
//            "FieldVisit" to collectRemarksFromSection("FieldVisit", fieldItems)
//        )
//    }

    private fun commitFocusedEditText() {
        try {
            view?.findFocus()?.clearFocus()
        } catch (_: Exception) { /* ignore */
        }
    }

    // ==================== DATA INITIALIZATION METHODS ====================

    private fun getOrgItems(): MutableList<FieldVerificationItem> {
        return mutableListOf(
            FieldVerificationItem(
                id = "",
                requirement = resources.getString(R.string.field_ver_industry_existence),
                verificationDoc = resources.getString(R.string.field_ver_valid_govt_note_doc),
                documents = listOf("Date of Incorporation (PRN)"),
                uploadEnabled = false,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "",
                requirement = resources.getString(R.string.field_ver_valid_epfo_esic_doc),
                verificationDoc = resources.getString(R.string.field_ver_valid_epfo_esic_note_doc),
                documents = listOf("View Registration Document"),
                uploadEnabled = false,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "",
                requirement = resources.getString(R.string.field_ver_epfo_challan_doc),
                verificationDoc = resources.getString(R.string.field_ver_valid_epfo_challan_note_doc),
                documents = listOf("EPFO Challan (6 Months)"),
                uploadEnabled = false,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "",
                requirement = resources.getString(R.string.field_ver_valid_industry_doc),
                verificationDoc = resources.getString(R.string.field_ver_valid_industry_note_doc),
                documents = listOf("View"),
                uploadEnabled = false,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "",
                requirement = resources.getString(R.string.field_ver_valid_bank_doc),
                verificationDoc = resources.getString(R.string.field_ver_valid_bank_note_doc),
                documents = listOf("View Account Details"),
                uploadEnabled = false,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "ORG_MANPOWER",
                requirement = resources.getString(R.string.field_ver_valid_manpower_doc),
                verificationDoc = resources.getString(R.string.field_ver_valid_manpower_note_doc),
                documents = emptyList(),
                uploadEnabled = false,
                allowRemark = true
            )
        )
    }

    private fun getFinItems(): MutableList<FieldVerificationItem> {
        return mutableListOf(
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_industry_turnover_fin),
                resources.getString(R.string.field_ver_industry_turnover_note_fin),
                listOf(resources.getString(R.string.fin_balance_sheet_button)),
                uploadEnabled = false,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_industry_networth_fin),
                resources.getString(R.string.field_ver_industry_networth_note_fin),
                listOf(resources.getString(R.string.fin_turnover_button)),
                uploadEnabled = false,
                allowRemark = false
            )
        )
    }

    private fun getTrainingItems(): MutableList<FieldVerificationItem> {
        return mutableListOf(
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_exp_training),
                resources.getString(R.string.field_ver_exp_note_training),
                listOf(resources.getString(R.string.train_target_button)),
                uploadEnabled = false,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "FIN_TURNOVER",
                resources.getString(R.string.field_ver_hrs_training),
                resources.getString(R.string.field_ver_hrs_note_training),
                listOf(),
                uploadEnabled = false,
                allowRemark = true
            ),
            FieldVerificationItem(
                id = "FIN_NETWORTH",
                resources.getString(R.string.field_ver_course_content_training),
                resources.getString(R.string.field_ver_course_content_note_training),
                listOf(),
                uploadEnabled = false,
                allowRemark = true
            ),
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_nsqf_courses_training),
                resources.getString(R.string.field_ver_nsqf_courses_note_training),
                listOf(resources.getString(R.string.train_NSQF_course_button)),
                uploadEnabled = false,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_500_cand_training),
                resources.getString(R.string.field_ver_500_cand_note_training),
                listOf(
                    resources.getString(R.string.train_commitment1_button),
                    resources.getString(R.string.train_commitment2_button)
                ),
                uploadEnabled = false,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_job_training),
                resources.getString(R.string.field_ver_job_note_training),
                listOf(resources.getString(R.string.train_tailor_button)),
                uploadEnabled = false,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_domain_training),
                resources.getString(R.string.field_ver_domain_note_training),
                listOf(
                    resources.getString(R.string.train_domain1_button),
                    resources.getString(R.string.train_domain2_button)
                ),
                uploadEnabled = false,
                allowRemark = false
            )
        )
    }

    private fun getTrainingInfraItems(): MutableList<FieldVerificationItem> {
        return mutableListOf(
            FieldVerificationItem(
                id = "TRAIN_INFRA_NSQF",
                resources.getString(R.string.field_ver_nsqf_training_infra),
                resources.getString(R.string.field_ver_nsqf_note_training_infra),
                listOf("Self Declaration", "Training Centre"),
                uploadEnabled = true,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "TRAIN_INFRA_RES",
                resources.getString(R.string.field_ver_res_training_infra),
                resources.getString(R.string.field_ver_res_note_training_infra),
                documents = listOf("View Residential Facilities"),
                uploadEnabled = false,
                allowRemark = false
            )
        )
    }

    private fun getCertItems(): MutableList<FieldVerificationItem> {
        return mutableListOf(
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_provide_cert),
                resources.getString(R.string.field_ver_provide_note_cert),
                listOf("Form 4"),
                uploadEnabled = false,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_res_conduct_cert),
                resources.getString(R.string.field_ver_conduct_note_cert),
                listOf("Form 4"),
                uploadEnabled = false,
                allowRemark = false
            )
        )
    }

    private fun getPlacementItems(): MutableList<FieldVerificationItem> {
        return mutableListOf(
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_500_empl_placement),
                resources.getString(R.string.field_ver_empl_note_placement),
                listOf("View Employment Details"),
                uploadEnabled = false,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_70_per_cand_placement),
                resources.getString(R.string.field_ver_empl_off_letter_note_placement),
                listOf("Form 1"),
                uploadEnabled = false,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_70_per_less_cand_coursewise_placement),
                resources.getString(R.string.field_ver_empl_off_letter_coursewise_less_note_placement),
                listOf("Form 1"),
                uploadEnabled = false,
                allowRemark = false
            ),
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_70_per_more_cand_coursewise_placement),
                resources.getString(R.string.field_ver_empl_off_letter_coursewise_more_note_placement),
                listOf("Form 1"),
                uploadEnabled = false,
                allowRemark = false
            )
        )
    }

    private fun getFieldItems(): MutableList<FieldVerificationItem> {
        return mutableListOf(
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_geo_factory_field),
                resources.getString(R.string.field_ver_ctsa_off_note_field),
                listOf("Lat: ${latitude}", "Long: ${longitude}"),
                uploadEnabled = false,
                allowRemark = false
            )
        )
    }


    private fun showFinancialDialog(title: String, list: List<YearlyFinancialItem>?) {
        if (list.isNullOrEmpty()) {
            showToast("No $title data found")
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_simple_list, null)
        val rv = dialogView.findViewById<RecyclerView>(R.id.rvDialogList)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)

        tvTitle.text = title
        rv.layoutManager = LinearLayoutManager(requireContext())

        // Use BaseFragment's setupRecyclerView for dialogs too
        setupRecyclerView(
            recyclerView = rv,
            items = list,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = ItemFinancialRowBinding::inflate,
            onBind = { item, binding, _ ->
                binding.tvYear.text = item.year ?: "-"
                binding.tvAmount.text = formatAmount(item.amount)

                binding.btnView.apply {
                    if (!item.attachmentBase64.isNullOrBlank()) {
                        visibility = View.VISIBLE
                        setOnClickListener { openBase64Pdf(item.attachmentBase64!!) }
                    } else {
                        visibility = View.GONE
                    }
                }
            },
            noDataConfig = NoDataConfig(
                title = "No Data Available",
                description = "No financial records found"
            )
        )

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setNegativeButton(resources.getString(R.string.close), null)
            .show()
    }

    private fun showTrainingDialog(title: String, list: List<YearlyTrainingItem>?) {
        if (list.isNullOrEmpty()) {
            showToast("No $title data found")
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_simple_list, null)
        val rv = dialogView.findViewById<RecyclerView>(R.id.rvDialogList)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)

        tvTitle.text = title
        rv.layoutManager = LinearLayoutManager(requireContext())

        setupRecyclerView(
            recyclerView = rv,
            items = list,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = { inflater, parent, _ ->
                ItemTrainingRowBinding.inflate(inflater, parent, false)
            },
            onBind = { item, binding, _ ->
                binding.tvYear.text = item.year ?: "-"
                binding.tvAllocated.text = "Allocated: ${formatNumber(item.targetAllocated)}"
                binding.tvAchieved.text = "Achieved: ${formatNumber(item.targetAchieved)}"

                binding.btnView.apply {
                    if (!item.attachmentBase64.isNullOrBlank()) {
                        visibility = View.VISIBLE
                        setOnClickListener { openBase64Pdf(item.attachmentBase64!!) }
                    } else {
                        visibility = View.GONE
                    }
                }
            },
            noDataConfig = NoDataConfig(
                title = "No Data Available",
                description = "No training records found"
            )
        )

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showPlacementDialog(title: String, list: List<YearlyPlacementDetails>?) {
        if (list.isNullOrEmpty()) {
            showToast("No Placement data found")
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_simple_list, null)
        val rv = dialogView.findViewById<RecyclerView>(R.id.rvDialogList)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)

        tvTitle.text = title
        rv.layoutManager = LinearLayoutManager(requireContext())

        setupRecyclerView(
            recyclerView = rv,
            items = list,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = { inflater, parent, _ ->
                ItemPlacementRowBinding.inflate(inflater, parent, false)
            },
            onBind = { item, binding, _ ->
                binding.tvYear.text = boldLabel("Year:", item.year ?: "-")
                binding.tvCandidatePlaced.text =
                    boldLabel("Candidates Placed:", item.candidatePlaced.toString())
                binding.tvSanctionOrder.text =
                    boldLabel("Sanction Order:", item.sanctionOrderId.toString())
                binding.tvEsicNumber.text = boldLabel("ESIC No:", item.esicNumber.toString())
                binding.tvEpfoNumber.text = boldLabel("EPFO No:", item.epfoNumber.toString())

                binding.btnView.apply {
                    if (!item.proofDocument.isNullOrBlank()) {
                        visibility = View.VISIBLE
                        setOnClickListener { openBase64Pdf(item.proofDocument!!) }
                    } else {
                        visibility = View.GONE
                    }
                }
            },
            noDataConfig = NoDataConfig(
                title = "No Data Available",
                description = "No placement records found"
            )
        )

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setNegativeButton(resources.getString(R.string.close), null)
            .show()

    }


    fun formatAmount(value: Double?): String {
        if (value == null) return "₹ -"
        val nf = NumberFormat.getInstance(Locale.getDefault())
        nf.maximumFractionDigits = 2
        return "₹ ${nf.format(value)}"
    }

    private fun formatNumber(value: Double?): String {
        if (value == null) return "-"
        val nf = NumberFormat.getInstance(Locale.getDefault())
        nf.maximumFractionDigits = 2
        return nf.format(value)
    }

    private fun boldLabel(label: String, value: String): SpannableString {
        val fullText = "$label $value"
        val spannable = SpannableString(fullText)
        spannable.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            0,
            label.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

}



    // Simple data model


// Update your FieldVerificationItem data class to include attachments
data class FieldVerificationItem(
    val id: String,
    val requirement: String,
    val verificationDoc: String,
    val documents: List<String>,
    val uploadEnabled: Boolean = false,
    val imageUri: String? = null,
    val allowRemark: Boolean = false,
    var remarkText: String? = null,
    var attachments: List<AttachmentItem> = emptyList() // NEW: For storing captured files
)
