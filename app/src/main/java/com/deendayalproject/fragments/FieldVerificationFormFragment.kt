package com.deendayalproject.fragments

import SharedViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.RippleDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.databinding.FragmentFieldVerFormBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import android.widget.EditText
import android.widget.ImageView
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
import android.widget.FrameLayout
import androidx.core.graphics.ColorUtils
import com.deendayalproject.model.response.AnnualTurnover
import com.deendayalproject.model.response.FieldVerificationDetailResponse
import com.deendayalproject.model.response.FinancialDetailsResponse
import com.deendayalproject.model.response.NetWorth
import com.deendayalproject.model.response.RemarkItem
import com.deendayalproject.model.response.TrainingCriteriaItem
import com.deendayalproject.model.response.YearlyFinancialItem
import com.deendayalproject.model.response.YearlyPlacementDetails
import com.deendayalproject.model.response.YearlyTrainingItem
import com.deendayalproject.model.response.toYearlyItem
import com.deendayalproject.model.response.toYearlyTrainingItem
import java.text.NumberFormat

/**
 * FieldVerificationFormFragment
 *
 * Shows a RecyclerView of field verification items using the layout item_field_ver_card.xml
 * This fragment includes a small local adapter so you don't need to change other files.
 *
 * If you already have your own adapter/model, you can replace `LocalFieldVerificationAdapter`
 * and `FieldVerificationItem` with your own implementations.
 */
class FieldVerificationFormFragment : Fragment() {

    private var _binding: FragmentFieldVerFormBinding? = null
    private val binding get() = _binding!!

    // Optional shared VM (attempt to obtain if present)
    private var sharedViewModel: SharedViewModel? = null

    private val viewModel: SharedViewModel by activityViewModels()

    // Recycler + Adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewFin: RecyclerView

    private lateinit var recyclerViewTraining: RecyclerView

    private lateinit var recyclerViewTrainingInfra: RecyclerView

    private lateinit var recyclerViewCert: RecyclerView

    private lateinit var recyclerViewPlacement: RecyclerView

    private lateinit var recyclerViewField: RecyclerView
    //private lateinit var adapter: LocalFieldVerificationAdapter

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

    private lateinit var finAdapter: LocalFieldVerificationAdapter

    private lateinit var trainingAdapter: LocalFieldVerificationAdapter

    private lateinit var trainingInfraAdapter: LocalFieldVerificationAdapter

    private lateinit var certAdapter: LocalFieldVerificationAdapter

    private lateinit var placementAdapter: LocalFieldVerificationAdapter

    private lateinit var fieldAdapter: LocalFieldVerificationAdapter

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





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFieldVerFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        captiveEmpanelmentId = arguments?.getString("captiveEmpanelmentId").toString()
        prnNo = arguments?.getString("prnNo").toString()

        val request = FieldVerificationDetailRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            captiveEmpanelmentId = captiveEmpanelmentId,
            prnNo = prnNo
        )

        viewModel.getFieldVerificationDetail(request)

        viewModel.fieldDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->

                // ✅ Response object: FieldVerificationDetailResponse
                val item = response.wrappedList.firstOrNull()

                // 🎯 Extract fields you need from nested structure
                // 🔴 Assign to global variables (important!)
                apiDateOfIncorporation =
                    item?.organizationDetails?.proofOfIndustryExistence?.dateOfIncorporation

                apiBankName =
                    item?.organizationDetails?.bankDetails?.bankName

                apiManpowerRemarks =
                    item?.organizationDetails?.manpowerAgencyCheck?.remarks

                /* EPFO 6 months Challan */
                apiEpfoExistingStaff =
                    item?.organizationDetails?.epfoChallans?.existingStaffRegisteredInEpfo
                apiEpfoDocumentUrl =
                    item?.organizationDetails?.epfoChallans?.epfoDocument

                // --- Tax Details (from API) ---
                apiGstNumber =
                    item?.organizationDetails?.taxDetails?.gstNumber
                apiTanNumber =
                    item?.organizationDetails?.taxDetails?.tanNumber
                apiTanAttachmentBase64 =
                    item?.organizationDetails?.taxDetails?.tanAttachment

                // --- Bank Details (from API) ---
                apiBankAccountNumber   = item?.organizationDetails?.bankDetails?.bankAccountNumber
                apiBankLetterBase64    = item?.organizationDetails?.bankDetails?.bankLetterDocument
                apiSelfDeclarationBase64 = item?.organizationDetails?.bankDetails?.selfDeclarationDocument

                // --- Industry Registration (from API) ---
                apiEpfoNumber =
                    item?.organizationDetails?.industryRegistration?.epfoNumber

                apiEsicNumber =
                    item?.organizationDetails?.industryRegistration?.esicNumber

                apiFactoryRegNumber    = item?.organizationDetails?.industryRegistration?.factoryRegistrationNumber

                apiEpfoAttachmentBase64    = item?.organizationDetails?.industryRegistration?.epfoAttachment      // Base64 or null
                apiEsicAttachmentBase64    = item?.organizationDetails?.industryRegistration?.esicAttachment      // Base64 or null
                apiFactoryAttachmentBase64 = item?.organizationDetails?.industryRegistration?.factoryRegistrationAttachment // Base64 or null





                Log.d("FIELD_API", "DOI = $apiDateOfIncorporation")
                Log.d("FIELD_API", "EPFO = $apiEpfoNumber")
                Log.d("FIELD_API", "ESIC = $apiEsicNumber")
                Log.d("FIELD_API", "GST = $apiGstNumber")
                Log.d("FIELD_API", "BankName = $apiBankName")



            }.onFailure {
                Toast.makeText(requireContext(),
                    it.message ?: "Failed to fetch details",
                    Toast.LENGTH_LONG).show()
            }
        }

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    Log.d("Camera", "Captured image URI: $photoUri")
                    if (currentUploadPosition >= 0) {
                        val pos = currentUploadPosition

                        when (currentUploadList) {
                            "fin" -> {
                                val existing = finItems.getOrNull(pos)
                                if (existing != null) {
                                    finItems[pos] = existing.copy(
                                        imageUri = photoUri.toString(),
                                        uploadEnabled = true
                                    )
                                    finAdapter.update(finItems)
                                }
                            }
                            "training" -> {
                                val existing = trainingItems.getOrNull(pos)
                                if (existing != null) {
                                    trainingItems[pos] = existing.copy(
                                        imageUri = photoUri.toString(),
                                        uploadEnabled = true
                                    )
                                    trainingAdapter.update(trainingItems)
                                }
                            }
                            "Self Declaration" -> {
                                val existing = trainingInfraItems.getOrNull(pos)
                                if (existing != null) {
                                    trainingInfraItems[pos] = existing.copy(
                                        imageUri = photoUri.toString(),
                                        uploadEnabled = true
                                    )
                                    trainingInfraAdapter.update(trainingInfraItems)
                                }
                            }
                            "Training Centre" -> {
                                val existing = trainingInfraItems.getOrNull(pos)
                                if (existing != null) {
                                    trainingInfraItems[pos] = existing.copy(
                                        imageUri = photoUri.toString(),
                                        uploadEnabled = true
                                    )
                                    trainingInfraAdapter.update(trainingInfraItems)
                                }
                            }
                            else -> {
                                // default: if you still want to support orgItems, add a branch here
                            }
                        }

                        // reset flags
                        currentUploadPosition = -1
                        currentUploadList = ""
                    }
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


        // Try to get shared ViewModel (if your project uses it)
        try {
            sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        } catch (e: Exception) {
            sharedViewModel = null
        }

        // Set up toolbar back button
        binding.backButton.setOnClickListener {

            findNavController().navigateUp()
        }

        // Try common RecyclerView IDs to maximize compatibility with different XML variants
        recyclerView = try {
            binding.recyclerView // if binding generated this id
        } catch (e: Exception) {
            // fallback to a generic id "recyclerView" or find by id
            val rvById = view.findViewById<RecyclerView?>(R.id.recyclerView)
            rvById ?: throw IllegalStateException("RecyclerView not found. Ensure id is rv_field_ver_list or recyclerView.")
        }



        // Populate sample data (replace with your real data from ViewModel or elsewhere)
        orgItems = mutableListOf(
            FieldVerificationItem(
                id = "",
                requirement = resources.getString(R.string.field_ver_industry_existence),
                verificationDoc = resources.getString(R.string.field_ver_valid_govt_note_doc),
                documents = listOf(
                    "Date of Incorporation (PRN)"
                ),
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
        val orgAdapter = LocalFieldVerificationAdapter(
            items = orgItems,
            onViewClick = { pos, doc ->
                Toast.makeText(requireContext(), "Position: $pos Viewing: $doc", Toast.LENGTH_SHORT).show()
                if (pos == 0 && doc=="Date of Incorporation (PRN)"){
                    val dialog = AlertDialog.Builder(requireContext())
                        .setTitle("Date of incorporation")
                        .setMessage(apiDateOfIncorporation ?: "Not Available")
                        .setPositiveButton("OK", null)
                        .create()
                    dialog.show()
                }else if (pos == 1 && doc=="View Registration Document"){
                    val msg = buildString {
                        appendLine("EPFO Number: ${apiEpfoNumber ?: "NA"}")
                        appendLine("ESIC Number: ${apiEsicNumber ?: "NA"}")
                        appendLine("Factory Registration Number: ${apiFactoryRegNumber ?: "NA"}")
                    }.trim()

                    val actions = buildList {
                        if (!apiEpfoAttachmentBase64.isNullOrBlank()) {
                            add(DocAction("View EPFO") { openBase64Pdf(requireContext(),apiEpfoAttachmentBase64!!) })
                        }
                        if (!apiEsicAttachmentBase64.isNullOrBlank()) {
                            add(DocAction("View ESIC") { openBase64Pdf(requireContext(),apiEsicAttachmentBase64!!) })
                        }
                        if (!apiFactoryAttachmentBase64.isNullOrBlank()) {
                            add(DocAction("View Factory") { openBase64Pdf(requireContext(),apiFactoryAttachmentBase64!!) })
                        }
                    }

                    showInfoWithHorizontalButtonsDialog(
                        title = "Industry Registration",
                        message = msg,
                        actions = actions
                    )
                }
                else if (pos == 2 && doc == "EPFO Challan (6 Months)") {
                    val message = "Existing staff registered in EPFO: ${apiEpfoExistingStaff ?: "Not Available"}"

                    val actions = buildList {
                        if (!apiEpfoDocumentUrl.isNullOrBlank()) {
                            add(DocAction("View EPFO Challan") {
                                openBase64Pdf(requireContext(),apiEpfoDocumentUrl!!) // your PDF/image opener
                            })
                        }
                    }

                    showInfoWithHorizontalButtonsDialog(
                        title = "EPFO Challan (Last 6 Months)",
                        message = message,
                        actions = actions
                    )
                }else if (pos == 3 && doc == "View") {
                    val msg = buildString {
                        appendLine("GST number: ${apiGstNumber ?: "—"}")
                        appendLine("TAN number: ${apiTanNumber ?: "—"}")
                    }.trim()

                    val actions = buildList {
                        if (!apiTanAttachmentBase64.isNullOrBlank()) {
                            add(DocAction("View TAN") { openBase64Pdf(requireContext(),apiTanAttachmentBase64!!) })
                        }
                    }

                    showInfoWithHorizontalButtonsDialog(
                        title = "Tax Details",
                        message = msg,
                        actions = actions
                    )
                }else if (pos == 4 && doc == "View Account Details") {
                    val msg = buildString {
                        appendLine("Bank Account Number: ${apiBankAccountNumber ?: "—"}")
                    }.trim()

                    val actions = buildList {
                        if (!apiBankLetterBase64.isNullOrBlank()) {
                            add(DocAction("View Bank Letter") { openBase64Pdf(requireContext(),apiBankLetterBase64!!) })
                        }
                        if (!apiSelfDeclarationBase64.isNullOrBlank()) {
                            add(DocAction("View Self-Declaration") { openBase64Pdf(requireContext(),apiSelfDeclarationBase64!!) })
                        }
                    }

                    showInfoWithHorizontalButtonsDialog(
                        title = "Bank Details",
                        message = msg,
                        actions = actions
                    )

                }
            },
            onUploadClick = { _, _ -> /* not needed here */ },
            showIcons = true
        )
        //binding.recyclerView.adapter = orgAdapter
        // Setup adapter and layout manager

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = orgAdapter


        orgAdapter.update(orgItems)

        //previous button finance
        binding.btnFinPrevious.setOnClickListener {

            binding.verOrg.visibility = View.VISIBLE
            binding.trainingInfraExpand.visibility = View.VISIBLE
            binding.verFin.visibility = View.GONE
            binding.verFinExpand.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        //previous button Training
        binding.btnTrainingPrevious.setOnClickListener {

            binding.verFin.visibility = View.VISIBLE
            binding.verFinExpand.visibility = View.VISIBLE
            binding.verTraining.visibility = View.GONE
            binding.verTrainingExpand.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        //previous button Training Infrastructure
        binding.btnTrainingInfraPrevious.setOnClickListener {

            binding.verTraining.visibility = View.VISIBLE
            binding.verTrainingExpand.visibility = View.VISIBLE
            binding.verTrainingInfra.visibility = View.GONE
            binding.verTrainingInfraExpand.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        //previous button Assessment & Certification
        binding.btnCertPrevious.setOnClickListener {

            binding.verTrainingInfra.visibility = View.VISIBLE
            binding.verTrainingInfraExpand.visibility = View.VISIBLE
            binding.verCert.visibility = View.GONE
            binding.verCertExpand.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        //previous button Placement
        binding.btnPlacementPrevious.setOnClickListener {

            binding.verCert.visibility = View.VISIBLE
            binding.verCertExpand.visibility = View.VISIBLE
            binding.verPlacement.visibility = View.GONE
            binding.verPlacementExpand.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        //previous button Field Visit
        binding.btnFieldPrevious.setOnClickListener {

            binding.verPlacement.visibility = View.VISIBLE
            binding.verPlacementExpand.visibility = View.VISIBLE
            binding.verField.visibility = View.GONE
            binding.verFieldExpand.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }


        // Submit button for Organisation Details : try common ids via binding or findById
        val btn = try {
            binding.btnInfoNext
        } catch (e: Exception) {
            view.findViewById<Button?>(R.id.btnInfoNext) // fallback id name
        }
        btn?.setOnClickListener {
            /*val manpowerRemark = orgItems.firstOrNull { it.allowRemark }?.remarkText?.trim().orEmpty()
            if (manpowerRemark.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter remark for Manpower Agency Check", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }*/
            /*val manpowerRemark = validateRemarkForSection(
                items = orgItems,
                errorMessage = "Please enter remark for Manpower Agency Check"
            ) ?: return@setOnClickListener*/
            val allmanpowerRemarks = validateAllRemarksForSection(orgItems) ?: return@setOnClickListener
            /*val remark = manpowerRemarkLocal?.trim().orEmpty()
            if (remark.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter remark for Manpower Agency Check", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }*/
//            selectedOrganizationInfoRemarks =
//                binding.etInfoRemarks.text.toString()
//            if (selectedOrganizationInfoRemarks.isEmpty()) {
//                Toast.makeText(
//                    requireContext(),
//                    "Kindly enter remarks first",
//                    Toast.LENGTH_SHORT
//                ).show()
//                return@setOnClickListener
//            }
            // Example: collect and show a toast. Replace with actual submit logic.
            //Toast.makeText(requireContext(), "Submit clicked (items: ${orgAdapter.itemCount}) with remark "+selectedOrganizationInfoRemarks, Toast.LENGTH_SHORT).show()

            binding.trainingInfraExpand.visibility = View.GONE
            binding.tvTrainInfra.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )
            binding.verFin.visibility = View.VISIBLE
            binding.verFinExpand.visibility = View.VISIBLE

            viewModel.getFieldVerificationFinDetail(request)
            viewModel.finDetail.removeObservers(viewLifecycleOwner)
            viewModel.finDetail.observe(viewLifecycleOwner) { result ->
                result.onSuccess { response ->
                    try {
                        val item = response.wrappedList?.firstOrNull()

                        // Safety: check the object exists
                        val financialDetails = item?.financialDetails

                        // assign to fragment globals
                        apiAnnualTurnoverList = financialDetails?.annualTurnover
                        apiNetWorthList = financialDetails?.netWorth

                        // now call the function that will convert these lists into finItems (we'll implement next)
                        //populateFinanceFromResponse()
                        Log.d("FieldVerify", "Annual turnover count: ${financialDetails?.annualTurnover?.size}")
                        Log.d("FieldVerify", "Net worth count: ${financialDetails?.netWorth?.size}")


                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Failed handling response: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }.onFailure { throwable ->
                    Toast.makeText(requireContext(), "API error: ${throwable.message ?: "Unknown"}", Toast.LENGTH_LONG).show()
                }
            }

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

            // Check and request permission
            if (hasLocationPermission()) {
                getCurrentLocation()
            } else {
                requestLocationPermission()
            }
        }

        // Submit button for Finance Details

        val btnFin = try {
            binding.btnFinNext
        } catch (e: Exception) {
            view.findViewById<Button?>(R.id.btnFinNext) // fallback id name
        }
        btnFin?.setOnClickListener {
            /*selectedFinanceRemarks =
                binding.etFinRemarks.text.toString()
            // NEW: check required uploads for finance items
            if (!checkUploadsComplete(finItems, "Finance")) {
                // missing required photos -> do not proceed
                return@setOnClickListener
            }
            if (selectedFinanceRemarks.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Kindly enter remarks first",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }*/

            // Example: collect and show a toast. Replace with actual submit logic.
            //Toast.makeText(requireContext(), "Submit clicked (items: ${orgAdapter.itemCount}) with remark "+selectedFinanceRemarks, Toast.LENGTH_SHORT).show()

            binding.verFinExpand.visibility = View.GONE
            binding.tvFinHead.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )
            binding.verTraining.visibility = View.VISIBLE
            binding.verTrainingExpand.visibility = View.VISIBLE

            val request = FieldVerificationDetailRequest(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                captiveEmpanelmentId = captiveEmpanelmentId,
                prnNo = prnNo
            )

            // Call SEPARATE training API (Option B)
            viewModel.getFieldVerificationTrainingDetail(request)

            // Observe the LiveData once (remove previous observers first for safety)
            viewModel.trainingDetail.removeObservers(viewLifecycleOwner)
            viewModel.trainingDetail.observe(viewLifecycleOwner) { result ->
                result.onSuccess { response ->
                    try {
                        val item = response.wrappedList?.firstOrNull()
                        val trainingDetails = item?.trainingDetails

                        // Save fields to fragment globals
                        apiTrainingCriteriaList = trainingDetails?.trainingCriteria
                        apiTotalTrainingHoursRemarks = trainingDetails?.totalTrainingHoursRemarks
                        apiRepetitionClubbingRemarks = trainingDetails?.repetitionClubbingRemarks

                        apiBasicSelfDeclarationBase64 = trainingDetails?.basicTraining?.selfDeclarationTrainingDoc
                        apiCommitmentForm1Base64 = trainingDetails?.commitment?.form1
                        apiCommitmentForm2Base64 = trainingDetails?.commitment?.form2
                        apiTailorTrainingDocBase64 = trainingDetails?.trainingPlacement?.tailorTrainingDoc
                        apiDomainForm1Base64 = trainingDetails?.domainSpecificTraining?.form1
                        apiDomainForm2Base64 = trainingDetails?.domainSpecificTraining?.form2

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Failed processing training response: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }.onFailure { e ->
                    Toast.makeText(requireContext(), "Training API failed: ${e.message ?: "Unknown"}", Toast.LENGTH_LONG).show()
                }
            }


        }

        // Optionally, if you have a SharedViewModel list available, observe it and update adapter:
        // Example (uncomment & adapt if your ViewModel has the LiveData):
        /*
        sharedViewModel?.fieldVerificationItems?.observe(viewLifecycleOwner) { list ->
            if (list != null) adapter.update(list)
        }
        */


        /* for Financial Detail */

        recyclerViewFin = try {
            binding.recyclerViewFin // if binding generated this id
        } catch (e: Exception) {
            // fallback to a generic id "recyclerView" or find by id
            val rvById = view.findViewById<RecyclerView?>(R.id.recyclerViewFin)
            rvById ?: throw IllegalStateException("RecyclerView not found. Ensure id is rv_field_ver_list or recyclerView.")
        }

        finItems = mutableListOf(
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

        finAdapter = LocalFieldVerificationAdapter(
            items = finItems,
            onViewClick = { pos, doc -> /* not used for upload list */

                //Toast.makeText(requireContext(), "Position: $pos Viewing: $doc", Toast.LENGTH_SHORT).show()
                if (pos == 0 && doc==resources.getString(R.string.fin_balance_sheet_button)){
                    val items = apiAnnualTurnoverList?.map { it.toYearlyItem() }
                    showFinancialDialog(resources.getString(R.string.fin_annual_turnover),items)
                }else if (pos == 1 && doc==resources.getString(R.string.fin_turnover_button)){
                    val items = apiNetWorthList?.map { it.toYearlyItem() }
                    showFinancialDialog(resources.getString(R.string.fin_net_worth),items)
                }

                          },
            onUploadClick = { pos, doc ->
                //Toast.makeText(requireContext(), "Upload clicked for item $pos", Toast.LENGTH_SHORT).show()
                // TODO: launch file picker here
                currentUploadPosition = pos
                currentUploadList = "fin"
                currentPhotoTarget = "Turnover"
                checkAndLaunchCamera()
            },
            showIcons = true
        )

        recyclerViewFin.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewFin.adapter = finAdapter
        finAdapter.update(finItems)

        /* for Training Details */

        // Submit button for Training Details

        val btnTraining = try {
            binding.btnTrainingNext
        } catch (e: Exception) {
            view.findViewById<Button?>(R.id.btnTrainingNext) // fallback id name
        }
        btnTraining?.setOnClickListener {
            /*val trainingRemark = validateRemarkForSection(
                items = trainingItems,
                errorMessage = "Please enter remark for Training section"
            ) ?: return@setOnClickListener*/

            val alltrainingRemarks = validateAllRemarksForSection(trainingItems) ?: return@setOnClickListener
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

            // Call SEPARATE training API (Option B)
            viewModel.getFieldVerificationTrainingInfraDetail(request)

            // Observe the LiveData once (remove previous observers first for safety)
            viewModel.trainingInfraDetail.removeObservers(viewLifecycleOwner)
            viewModel.trainingInfraDetail.observe(viewLifecycleOwner) { result ->
                result.onSuccess { response ->
                    try {
                        val item = response.wrappedList?.firstOrNull()
                        // NOTE: your API key is "trainingInfrastrutureDetails" (typo preserved from response)
                        val infra = item?.trainingInfrastrutureDetails
                        val residential = infra?.residentialFacilityDetails

                        // assign to fragment globals
                        apiResidentialFacilityAvailable = residential?.residentialFacilityAvailable
                        apiResidentialFacilityDocumentBase64 = residential?.residentialFacilityDocument

                        Log.d("FieldVerify", "Residential available = $apiResidentialFacilityAvailable, doc present = ${!apiResidentialFacilityDocumentBase64.isNullOrBlank()}")

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Failed processing training infra response: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }.onFailure { e ->
                    Toast.makeText(requireContext(), "Training Infra API failed: ${e.message ?: "Unknown"}", Toast.LENGTH_LONG).show()
                }
            }
        }

        recyclerViewTraining = try {
            binding.recyclerViewTraining // if binding generated this id
        } catch (e: Exception) {
            // fallback to a generic id "recyclerView" or find by id
            val rvById = view.findViewById<RecyclerView?>(R.id.recyclerViewTraining)
            rvById ?: throw IllegalStateException("RecyclerView not found. Ensure id is rv_field_ver_list or recyclerView.")
        }

        trainingItems = mutableListOf(
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
                listOf(resources.getString(R.string.train_commitment1_button), resources.getString(R.string.train_commitment2_button)),
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
                listOf(resources.getString(R.string.train_domain1_button), resources.getString(R.string.train_domain2_button)),
                uploadEnabled = false,
                allowRemark = false
            )
        )

        trainingAdapter = LocalFieldVerificationAdapter(
            items = trainingItems,
            onViewClick = { pos, doc ->
                Toast.makeText(requireContext(), "Position: $pos Viewing: $doc", Toast.LENGTH_SHORT).show()
                if (pos == 0 && doc==resources.getString(R.string.train_target_button)){
                    val items: List<YearlyTrainingItem>? = apiTrainingCriteriaList?.map { it.toYearlyTrainingItem() }
                    showTrainingDialog("Training Details", items)
                }else if (pos == 3 && doc == resources.getString(R.string.train_NSQF_course_button)){
                    if (!apiBasicSelfDeclarationBase64.isNullOrBlank()) {
                        val actions = buildList {

                            add(DocAction(resources.getString(R.string.train_NSQF_course_button)) {
                                openBase64Pdf(
                                    requireContext(),
                                    apiBasicSelfDeclarationBase64!!
                                )
                            })

                        }

                        showInfoWithHorizontalButtonsDialog(
                            title = "Basic Training",
                            message = "",
                            actions = actions
                        )
                    }else{
                        Toast.makeText(requireContext(), "No File to View", Toast.LENGTH_SHORT).show()
                    }
                }else if (pos == 4 && doc == resources.getString(R.string.train_commitment1_button)){
                    if (!apiCommitmentForm1Base64.isNullOrBlank()) {
                        val actions = buildList {
                            add(DocAction(resources.getString(R.string.train_commitment1_button)) {
                                openBase64Pdf(
                                    requireContext(),
                                    apiCommitmentForm1Base64!!
                                )
                            })
                        }
                        showInfoWithHorizontalButtonsDialog(
                            title = "Captive Employers Commitment",
                            message = "",
                            actions = actions
                        )
                    }else{
                        Toast.makeText(requireContext(), "No Form 1 to View", Toast.LENGTH_SHORT).show()
                    }
                }else if (pos == 4 && doc == resources.getString(R.string.train_commitment2_button)){

                    if (!apiCommitmentForm2Base64.isNullOrBlank()) {
                        val actions = buildList {
                            add(DocAction(resources.getString(R.string.train_commitment2_button)) {
                                openBase64Pdf(
                                    requireContext(),
                                    apiCommitmentForm2Base64!!
                                )
                            })
                        }
                        showInfoWithHorizontalButtonsDialog(
                            title = "Captive Employers Commitment",
                            message = "",
                            actions = actions
                        )
                    }else{
                        Toast.makeText(requireContext(), "No Form 2 to View", Toast.LENGTH_SHORT).show()
                    }
                }else if (pos == 5 && doc == resources.getString(R.string.train_tailor_button)){
                    if (!apiTailorTrainingDocBase64.isNullOrBlank()) {
                        val actions = buildList {
                            add(DocAction(resources.getString(R.string.train_tailor_button)) {
                                openBase64Pdf(
                                    requireContext(),
                                    apiTailorTrainingDocBase64!!
                                )
                            })
                        }
                        showInfoWithHorizontalButtonsDialog(
                            title = "Tailor Training Doc",
                            message = "",
                            actions = actions
                        )
                    }else{
                        Toast.makeText(requireContext(), "No Tailor Training Doc", Toast.LENGTH_SHORT).show()
                    }
                }else if (pos == 6 && doc == resources.getString(R.string.train_domain1_button)){
                    if (!apiDomainForm1Base64.isNullOrBlank()) {
                        val actions = buildList {
                            add(DocAction(resources.getString(R.string.train_domain1_button)) {
                                openBase64Pdf(
                                    requireContext(),
                                    apiDomainForm1Base64!!
                                )
                            })
                        }
                        showInfoWithHorizontalButtonsDialog(
                            title = "Domain Specific Training",
                            message = "",
                            actions = actions
                        )
                    }else{
                        Toast.makeText(requireContext(), "No Domain Specific Training Doc", Toast.LENGTH_SHORT).show()
                    }
                }else if (pos == 6 && doc == resources.getString(R.string.train_domain2_button)){
                    if (!apiDomainForm2Base64.isNullOrBlank()) {
                        val actions = buildList {
                            add(DocAction(resources.getString(R.string.train_domain2_button)) {
                                openBase64Pdf(
                                    requireContext(),
                                    apiDomainForm2Base64!!
                                )
                            })
                        }
                        showInfoWithHorizontalButtonsDialog(
                            title = "Domain Specific Training",
                            message = "",
                            actions = actions
                        )
                    }else{
                        Toast.makeText(requireContext(), "No Domain Specific Training Doc", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onUploadClick = { pos, doc ->
                //Toast.makeText(requireContext(), "Upload clicked for item $pos", Toast.LENGTH_SHORT).show()
                // TODO: launch file picker here
                currentUploadPosition = pos
                currentUploadList = "training"
                currentPhotoTarget = "Additional tailor-made training If Yes Upload"
                checkAndLaunchCamera()
            },
            showIcons = true
        )

        recyclerViewTraining.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewTraining.adapter = trainingAdapter
        trainingAdapter.update(trainingItems)

        /* for Training Infrastructure Details */

        // Submit button for Training Infrastructure Details

        val btnTrainingInfra = try {
            binding.btnTrainingInfraNext
        } catch (e: Exception) {
            view.findViewById<Button?>(R.id.btnTrainingInfraNext) // fallback id name
        }
        btnTrainingInfra?.setOnClickListener {
            //selectedTrainingInfraRemarks = binding.etTrainingInfraRemarks.text.toString()


            if (base64TrainingInfraDeclarationFile.isNullOrBlank()) {
                // missing required photos -> do not proceed
                Toast.makeText(requireContext(), "Please Capture Self Declaration", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (base64TrainingInfraCentreFile.isNullOrBlank()) {
                // missing required photos -> do not proceed
                Toast.makeText(requireContext(), "Please Capture Training Centre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Example: collect and show a toast. Replace with actual submit logic.
            //Toast.makeText(requireContext(), "Submit clicked (items: ${orgAdapter.itemCount}) with remark "+selectedTrainingInfraRemarks, Toast.LENGTH_SHORT).show()

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

            // Call SEPARATE training API (Option B)
            viewModel.getFieldVerificationCertificationDetail(request)

            // Observe the LiveData once (remove previous observers first for safety)
            viewModel.certificationDetail.removeObservers(viewLifecycleOwner)
            viewModel.certificationDetail.observe(viewLifecycleOwner) { result ->
                result.onSuccess { response ->
                    try {
                        val item = response.wrappedList?.firstOrNull()
                        // NOTE: your API key is "assessmentCertificationDetails" (typo preserved from response)
                        val infra = item?.assessmentCertificationDetails
                        val commitment = infra?.commitmentLetterDetails

                        // assign to fragment globals
                        apiAwardBodyCommitBase64 = commitment?.awardBodyCommit
                        apiSeventyPctCommitBase64 = commitment?.seventyPctCommit

                        Log.d("assessmentCertificationDetails", "Award body available = $apiAwardBodyCommitBase64, doc present = ${!apiSeventyPctCommitBase64.isNullOrBlank()}")

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Failed processing Assessment & Certification response: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }.onFailure { e ->
                    Toast.makeText(requireContext(), "Assessment & Certification API failed: ${e.message ?: "Unknown"}", Toast.LENGTH_LONG).show()
                }
            }
        }

        recyclerViewTrainingInfra = try {
            binding.recyclerViewTrainingInfra // if binding generated this id
        } catch (e: Exception) {
            // fallback to a generic id "recyclerView" or find by id
            val rvById = view.findViewById<RecyclerView?>(R.id.recyclerViewTrainingInfra)
            rvById ?: throw IllegalStateException("RecyclerView not found. Ensure id is rv_field_ver_list or recyclerView.")
        }

        trainingInfraItems = mutableListOf(
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

        trainingInfraAdapter = LocalFieldVerificationAdapter(
            items = trainingInfraItems,
            onViewClick = { pos, doc ->
                if (pos == 1 && doc == "View Residential Facilities"){
                    val msg = buildString {
                        appendLine("Residential Facility Available: ${apiResidentialFacilityAvailable ?: "—"}")
                    }.trim()

                    if (!apiResidentialFacilityDocumentBase64.isNullOrBlank()) {
                        val actions = buildList {
                            add(DocAction("Residential Facilities") {
                                openBase64Pdf(
                                    requireContext(),
                                    apiResidentialFacilityDocumentBase64!!
                                )
                            })
                        }
                        showInfoWithHorizontalButtonsDialog(
                            title = "Residential Facilities",
                            message = msg,
                            actions = actions
                        )
                    }else{
                        Toast.makeText(requireContext(), "No Residential Facilities to View", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onUploadClick = { pos, doc ->
                //Toast.makeText(requireContext(), "Upload clicked for item $pos", Toast.LENGTH_SHORT).show()
                // TODO: launch file picker here
                currentUploadPosition = pos

                if (pos == 0 && doc == "Self Declaration"){
                    currentPhotoTarget = "Self Declaration"
                    currentUploadList = "Self Declaration"
                }
                else if (pos == 0 && doc == "Training Centre"){
                    currentPhotoTarget = "Training Centre"
                    currentUploadList = "Training Centre"
                }
                checkAndLaunchCamera()
            },
            showIcons = true
        )

        recyclerViewTrainingInfra.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewTrainingInfra.adapter = trainingInfraAdapter
        trainingInfraAdapter.update(trainingInfraItems)


        /* for Assessment & Certification Details */

        // Submit button for Assessment & Certification Details

        val btnCert = try {
            binding.btnCertNext
        } catch (e: Exception) {
            view.findViewById<Button?>(R.id.btnCertNext) // fallback id name
        }
        btnCert?.setOnClickListener {
            /*selectedCertRemarks =
                binding.etCertRemarks.text.toString()
            if (selectedCertRemarks.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Kindly enter remarks first",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }*/
            // Example: collect and show a toast. Replace with actual submit logic.
            //Toast.makeText(requireContext(), "Submit clicked (items: ${certAdapter.itemCount}) with remark "+selectedCertRemarks, Toast.LENGTH_SHORT).show()

            binding.verCertExpand.visibility = View.GONE
            binding.tvCertHead.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )
            binding.verPlacement.visibility = View.VISIBLE
            binding.verPlacementExpand.visibility = View.VISIBLE

            val request = FieldVerificationDetailRequest(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                captiveEmpanelmentId = captiveEmpanelmentId,
                prnNo = prnNo
            )

            // Call SEPARATE training API (Option B)
            viewModel.getFieldVerificationPlacementDetail(request)

            // Observe the LiveData once (remove previous observers first for safety)
            viewModel.placementDetail.removeObservers(viewLifecycleOwner)
            viewModel.placementDetail.observe(viewLifecycleOwner) { result ->
                result.onSuccess { response ->
                    try {
                        val item = response.wrappedList?.firstOrNull()
                        // NOTE: your API key is "assessmentCertificationDetails" (typo preserved from response)
                        val infra = item?.placementDetails

                        // assign to fragment globals
                        apiPlacementList = infra?.yearWisePlacementDetails
                        apiCommitmentSixMonthsBase64 = infra?.commitment?.commitmentSixMonths
                        apiCommitmentLessSixMonthsBase64 = infra?.commitment?.commitmentLessSixMonths
                        apiCommitmentMoreSixMonthsBase64 = infra?.commitment?.commitmentMoreSixMonths

                        Log.d("FIELD_API", "Placement List = $apiPlacementList")
                        Log.d("FIELD_API", "6month = $apiCommitmentSixMonthsBase64")
                        Log.d("FIELD_API", "<6month = $apiCommitmentLessSixMonthsBase64")
                        Log.d("FIELD_API", ">6month = $apiCommitmentMoreSixMonthsBase64")

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Failed processing Placement response: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }.onFailure { e ->
                    Toast.makeText(requireContext(), "Placement API failed: ${e.message ?: "Unknown"}", Toast.LENGTH_LONG).show()
                }
            }


        }

        recyclerViewCert = try {
            binding.recyclerViewCert // if binding generated this id
        } catch (e: Exception) {
            // fallback to a generic id "recyclerView" or find by id
            val rvById = view.findViewById<RecyclerView?>(R.id.recyclerViewCert)
            rvById ?: throw IllegalStateException("RecyclerView not found. Ensure id is rv_field_ver_list or recyclerView.")
        }

        certItems = mutableListOf(
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

        certAdapter = LocalFieldVerificationAdapter(
            items = certItems,
            onViewClick = { pos, doc ->
                if (pos == 0 && doc == "Form 4"){

                    if (!apiAwardBodyCommitBase64.isNullOrBlank()) {
                        val actions = buildList {
                            add(DocAction("View Form 4") {
                                openBase64Pdf(
                                    requireContext(),
                                    apiAwardBodyCommitBase64!!
                                )
                            })
                        }
                        showInfoWithHorizontalButtonsDialog(
                            title = "Awarding Body",
                            message = "",
                            actions = actions
                        )
                    }else{
                        Toast.makeText(requireContext(), "No Award Body to View", Toast.LENGTH_SHORT).show()
                    }
                }else if (pos == 1 && doc == "Form 4"){

                    if (!apiSeventyPctCommitBase64.isNullOrBlank()) {
                        val actions = buildList {
                            add(DocAction("View Form 4") {
                                openBase64Pdf(
                                    requireContext(),
                                    apiSeventyPctCommitBase64!!
                                )
                            })
                        }
                        showInfoWithHorizontalButtonsDialog(
                            title = "Certification for 70% Candidates",
                            message = "",
                            actions = actions
                        )
                    }else{
                        Toast.makeText(requireContext(), "No Commitment to View", Toast.LENGTH_SHORT).show()
                    }
                }

            },
            onUploadClick = { pos, doc -> },
            showIcons = true
        )

        recyclerViewCert.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewCert.adapter = certAdapter
        certAdapter.update(certItems)


        /* for Placement Details */

        // Submit button for Placement Details

        val btnPlacement = try {
            binding.btnPlacementNext
        } catch (e: Exception) {
            view.findViewById<Button?>(R.id.btnPlacementNext) // fallback id name
        }
        btnPlacement?.setOnClickListener {
            /*selectedPlacementRemarks =
                binding.etPlacementRemarks.text.toString()
            if (selectedPlacementRemarks.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Kindly enter remarks first",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }*/
            // Example: collect and show a toast. Replace with actual submit logic.
            //Toast.makeText(requireContext(), "Submit clicked (items: ${placementAdapter.itemCount}) with remark "+selectedPlacementRemarks, Toast.LENGTH_SHORT).show()

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

        recyclerViewPlacement = try {
            binding.recyclerViewPlacement // if binding generated this id
        } catch (e: Exception) {
            // fallback to a generic id "recyclerView" or find by id
            val rvById = view.findViewById<RecyclerView?>(R.id.recyclerViewPlacement)
            rvById ?: throw IllegalStateException("RecyclerView not found. Ensure id is rv_field_ver_list or recyclerView.")
        }

        placementItems = mutableListOf(
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

        placementAdapter = LocalFieldVerificationAdapter(
            items = placementItems,
            onViewClick = { pos, doc ->
                if (pos==0 && doc=="View Employment Details"){

                    val items: List<YearlyPlacementDetails>? = apiPlacementList?.map { it}
                    showPlacementDialog("Placement Details", items)

                }else if (pos==1 && doc=="Form 1"){

                    if (!apiCommitmentSixMonthsBase64.isNullOrBlank()) {
                        val actions = buildList {
                            add(DocAction("View Form 1") {
                                openBase64Pdf(
                                    requireContext(),
                                    apiCommitmentSixMonthsBase64!!
                                )
                            })
                        }
                        showInfoWithHorizontalButtonsDialog(
                            title = "Six Months",
                            message = "",
                            actions = actions
                        )
                    }else{
                        Toast.makeText(requireContext(), "No Commitment to View", Toast.LENGTH_SHORT).show()
                    }

                }else if (pos==2 && doc=="Form 1"){

                    if (!apiCommitmentLessSixMonthsBase64.isNullOrBlank()) {
                        val actions = buildList {
                            add(DocAction("View Form 1") {
                                openBase64Pdf(
                                    requireContext(),
                                    apiCommitmentLessSixMonthsBase64!!
                                )
                            })
                        }
                        showInfoWithHorizontalButtonsDialog(
                            title = "Commitment Less than Six Months",
                            message = "",
                            actions = actions
                        )
                    }else{
                        Toast.makeText(requireContext(), "No Commitment to View", Toast.LENGTH_SHORT).show()
                    }

                }else if (pos==3 && doc=="Form 1"){

                    if (!apiCommitmentMoreSixMonthsBase64.isNullOrBlank()) {
                        val actions = buildList {
                            add(DocAction("View Form 1") {
                                openBase64Pdf(
                                    requireContext(),
                                    apiCommitmentMoreSixMonthsBase64!!
                                )
                            })
                        }
                        showInfoWithHorizontalButtonsDialog(
                            title = "Commitment Greater than Six Months",
                            message = "",
                            actions = actions
                        )
                    }else{
                        Toast.makeText(requireContext(), "No Commitment to View", Toast.LENGTH_SHORT).show()
                    }

                }
            },
            onUploadClick = { pos,doc -> },
            showIcons = true
        )

        recyclerViewPlacement.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewPlacement.adapter = placementAdapter
        placementAdapter.update(placementItems)

        /* for Field Visit Details */

        // Submit button for Field Visit Details

        val btnField = try {
            binding.btnFieldNext
        } catch (e: Exception) {
            view.findViewById<Button?>(R.id.btnFieldNext) // fallback id name
        }
        btnField?.setOnClickListener {
            /*selectedFieldRemarks =
                binding.etFieldRemarks.text.toString()
            if (selectedFieldRemarks.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Kindly enter remarks first",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }*/
            // Example: collect and show a toast. Replace with actual submit logic.
            //Toast.makeText(requireContext(), "Submit clicked (items: ${fieldAdapter.itemCount}) with remark "+selectedFieldRemarks, Toast.LENGTH_SHORT).show()

            binding.verFieldExpand.visibility = View.GONE
            binding.tvFieldHead.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )

            // collect all remarks
            val sectionMap = collectAllRemarksSectionWise()

            // Validate required remarks if backend requires specific ones:
            // e.g., ensure manpower remark present:
            val manpowerFound = sectionMap["Organization"]?.any { it.requirement.contains("Manpower", ignoreCase = true) } ?: false

            Log.d("section Map :: ",sectionMap.toString())

            Log.d("manpowerFound :: ",manpowerFound.toString())
        }

        recyclerViewField = try {
            binding.recyclerViewField // if binding generated this id
        } catch (e: Exception) {
            // fallback to a generic id "recyclerView" or find by id
            val rvById = view.findViewById<RecyclerView?>(R.id.recyclerViewField)
            rvById ?: throw IllegalStateException("RecyclerView not found. Ensure id is rv_field_ver_list or recyclerView.")
        }

        fieldItems = mutableListOf(
            FieldVerificationItem(
                id = "",
                resources.getString(R.string.field_ver_geo_factory_field),
                resources.getString(R.string.field_ver_ctsa_off_note_field),
                listOf("Lat: ${latitude}","Long: ${longitude}"),
                uploadEnabled = false,
                allowRemark = false
            )
        )

        fieldAdapter = LocalFieldVerificationAdapter(
            items = fieldItems,
            onViewClick = { _, _ -> /* not used for upload list */ },
            onUploadClick = { pos,doc -> },
            showIcons = false
        )

        recyclerViewField.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewField.adapter = fieldAdapter
        fieldAdapter.update(fieldItems)

    }

    private fun hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocation = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
        return fineLocation == PackageManager.PERMISSION_GRANTED ||
                coarseLocation == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted || coarseLocationGranted) {
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
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
        // Uses high accuracy priority for precise location
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    /*Toast.makeText(
                        requireContext(),
                        "Lat: ${location.latitude}, Lng: ${location.longitude}",
                        Toast.LENGTH_LONG
                    ).show()*/

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
                        fieldAdapter.update(fieldItems)
                    }
                } else {
                    Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get location: ${it.message}", Toast.LENGTH_SHORT).show()
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

    private fun checkUploadsComplete(items: List<FieldVerificationItem>, sectionName: String = "this section"): Boolean {
        // Find items that require upload but do not have an image
        val missing = items.filter { it.uploadEnabled && it.imageUri.isNullOrBlank() }

        if (missing.isNotEmpty()) {
            // Build friendly message describing missing uploads
            val names = missing.joinToString(separator = ", ") { it.requirement.take(40) } // limit length
            val message = "Please upload photos for the items in $sectionName: $names"
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ---------------------------
    // Local adapter + model below
    // ---------------------------

    // Simple data model used by the adapter. If you already have a model class, feel free to use it instead.
    data class FieldVerificationItem(
        val id: String,
        val requirement: String,
        val verificationDoc: String,
        val documents: List<String>,
        val uploadEnabled: Boolean = false,
        val imageUri: String? = null,
        val allowRemark: Boolean = false,
        var remarkText: String? = null
    )

    private inner class LocalFieldVerificationAdapter(
        private var items: List<FieldVerificationItem> = emptyList(),
        private val onViewClick: (position: Int, doc: String) -> Unit,
        private val onUploadClick: (position: Int, doc: String) -> Unit,
        private val showIcons: Boolean = true

    ) : RecyclerView.Adapter<LocalFieldVerificationAdapter.VH>() {

        fun update(newItems: List<FieldVerificationItem>) {
            items = newItems
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_field_ver_card, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = items[position]
            holder.tvReqTitle.text = item.requirement
            holder.tvVerification.text = item.verificationDoc

            // Clear chips then add
            holder.chipGroup.removeAllViews()
            for (doc in item.documents) {
                val chip = Chip(holder.itemView.context).apply {
                    text = doc
                    isClickable = true
                    isCheckable = false
                    if (showIcons){
                        // Show icon based on uploadEnabled flag
                        val iconRes = if (item.uploadEnabled) R.drawable.file else R.drawable.ic_up
                        closeIcon = context.getDrawable(iconRes) // 👁️ view icon
                        closeIconTint = context.getColorStateList(android.R.color.darker_gray)
                        isCloseIconVisible = true
                        iconEndPadding = 8f
                        textEndPadding = 16f
                    }else{
                        isCloseIconVisible = false
                    }

                    setOnClickListener {
                        if (item.uploadEnabled) {
                            onUploadClick(position, doc)
                        } else {
                            onViewClick(position, doc)
                        }

                    }
                }
                holder.chipGroup.addView(chip)
            }

            if (!item.imageUri.isNullOrBlank()) {
                Log.d("item for upload :: ",item.toString())
                Log.d("currentPhotoTarget :: ",currentPhotoTarget)
                try {

                    if (currentPhotoTarget=="Training Centre"){
                        holder.imageGroup?.visibility = View.VISIBLE
                        holder.ivPreview1?.setImageURI(Uri.parse(item.imageUri))
                        holder.ivPreview1?.visibility = View.VISIBLE
                    }else {
                        holder.imageGroup?.visibility = View.VISIBLE
                        holder.ivPreview?.setImageURI(Uri.parse(item.imageUri))
                        holder.ivPreview?.visibility = View.VISIBLE
                    }



                } catch (e: Exception) {
                    holder.ivPreview?.visibility = View.GONE
                    holder.ivPreview1?.visibility = View.GONE
                    holder.imageGroup?.visibility = View.GONE
                }
            } else {
                holder.ivPreview?.visibility = View.GONE
                holder.ivPreview1?.visibility = View.GONE
                holder.imageGroup?.visibility = View.GONE
            }

            // ---------- remark handling per item (fixed) ----------
            if (item.allowRemark) {
                holder.remarkGroup?.visibility = View.VISIBLE

                // Prefill from item's stored value (if any)
                val current = item.remarkText ?: ""
                if (holder.etSectionRemark?.text?.toString() != current) {
                    holder.etSectionRemark?.setText(current)
                }

                // Remove any previous TextWatcher attached to this holder to avoid duplicates
                holder.currentWatcher?.let { prev -> holder.etSectionRemark?.removeTextChangedListener(prev) }

                // Create & attach a new TextWatcher that updates only this item's remarkText
                val watcher = object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        val t = s?.toString()?.trim()
                        item.remarkText = t  // store per-item
                        // DO NOT set manpowerRemarkLocal here unless you want a fragment-wide copy;
                        // if you still need a fragment-level aggregated value you can compute it separately.
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                }
                holder.etSectionRemark?.addTextChangedListener(watcher)
                holder.currentWatcher = watcher
            } else {
                // hide remark UI for this item
                holder.remarkGroup?.visibility = View.GONE
                // also remove any watcher (just in case)
                holder.currentWatcher?.let { prev -> holder.etSectionRemark?.removeTextChangedListener(prev) }
                holder.currentWatcher = null
            }

            /*holder.itemView.setOnClickListener {
                Toast.makeText(holder.itemView.context, item.requirement, Toast.LENGTH_SHORT).show()
            }*/
        }

        override fun getItemCount(): Int = items.size

        inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvReqTitle: TextView = itemView.findViewById(R.id.tv_req_title)
            val tvVerification: TextView = itemView.findViewById(R.id.tv_verification)
            val chipGroup: ChipGroup = itemView.findViewById(R.id.chipgroup_documents)

            val imageGroup: View? = itemView.findViewById(R.id.imageGroup)
            val ivPreview: ImageView? = itemView.findViewById(R.id.ivPreview)

            val ivPreview1: ImageView? = itemView.findViewById(R.id.ivPreview1)
            // NEW:
            val remarkGroup: View? = itemView.findViewById(R.id.remarkGroup)
            val etSectionRemark: EditText? = itemView.findViewById(R.id.etSectionRemark)

            var currentWatcher: TextWatcher? = null
        }
    }


    private fun openBase64Pdf(context: Context, base64: String) {
        try {
            // 1) Clean Base64 (remove possible data URI prefix)
            val cleanBase64 = base64
                .replace("data:application/pdf;base64,", "", ignoreCase = true)
                .trim()

            // 2) Decode Base64
            val pdfBytes = android.util.Base64.decode(cleanBase64, android.util.Base64.DEFAULT)

            // 3) Verify PDF header using bytes (more robust than String)
            val isPdf = pdfBytes.size >= 4 &&
                    pdfBytes[0] == 0x25.toByte() && // %
                    pdfBytes[1] == 0x50.toByte() && // P
                    pdfBytes[2] == 0x44.toByte() && // D
                    pdfBytes[3] == 0x46.toByte()    // F
            if (!isPdf) {
                Toast.makeText(context, "Invalid PDF data", Toast.LENGTH_SHORT).show()
                return
            }

            // 4) Save temporarily in cache (FileProvider-friendly)
            val pdfFile = File.createTempFile("temp_", ".pdf", context.cacheDir)
            pdfFile.outputStream().use { it.write(pdfBytes) }

            // 5) Build content Uri via FileProvider (authority must match manifest)
            val uri: Uri = androidx.core.content.FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                pdfFile
            )

            // 6) Try external PDF viewer first
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                // If this is NOT an Activity context, we must add NEW_TASK
                if (context !is android.app.Activity) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }

            val pm = context.packageManager
            val handlers = pm.queryIntentActivities(intent, 0)
            if (handlers.isNotEmpty()) {
                context.startActivity(Intent.createChooser(intent, "Open PDF with").apply {
                    // Same flags on chooser (esp. NEW_TASK for app context)
                    if (context !is android.app.Activity) {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                })
                return
            }

            // 7) Fallback: in-app preview (emulator often has no PDF viewer)
            //    Renders page 1 into an ImageView dialog.
            showPdfInApp(context, pdfFile)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to open PDF", Toast.LENGTH_SHORT).show()
        }
    }

    // ---- In-app fallback using PdfRenderer (no extra deps) ----
    fun showPdfInApp(context: Context, file: File) {
        try {
            val pfd = android.os.ParcelFileDescriptor.open(file, android.os.ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = android.graphics.pdf.PdfRenderer(pfd)

            if (renderer.pageCount <= 0) {
                Toast.makeText(context, "Empty PDF", Toast.LENGTH_SHORT).show()
                renderer.close()
                pfd.close()
                return
            }

            val displayMetrics = context.resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels
            val pagerHeight = (screenHeight * 0.75).toInt() // 75% of screen height

            val viewPager = androidx.viewpager2.widget.ViewPager2(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    pagerHeight
                )
                orientation = androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
                offscreenPageLimit = 1
            }

            // Page indicator
            val pageIndicator = android.widget.TextView(context).apply {
                val density = context.resources.displayMetrics.density
                val padding = (12 * density).toInt()
                setPadding(padding, padding / 2, padding, padding / 2)
                text = "Page 1 of ${renderer.pageCount}"
                textSize = 14f
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            // Container (vertical)
            val container = android.widget.LinearLayout(context).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                addView(viewPager, android.widget.LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    pagerHeight
                ))
                addView(pageIndicator, android.widget.LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = android.view.Gravity.CENTER_HORIZONTAL
                    topMargin = (8 * displayMetrics.density).toInt()
                })
                setPadding( // dialog padding
                    (12 * displayMetrics.density).toInt(),
                    (8 * displayMetrics.density).toInt(),
                    (12 * displayMetrics.density).toInt(),
                    (12 * displayMetrics.density).toInt()
                )
            }

            // Adapter - IMPORTANT: itemView MUST be MATCH_PARENT x MATCH_PARENT
            class PdfPagerAdapter(
                private val pdfRenderer: android.graphics.pdf.PdfRenderer,
                private val ctx: Context
            ) : androidx.recyclerview.widget.RecyclerView.Adapter<PdfPagerAdapter.PageViewHolder>() {

                inner class PageViewHolder(val container: android.widget.FrameLayout, val imageView: android.widget.ImageView) :
                    androidx.recyclerview.widget.RecyclerView.ViewHolder(container)

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
                    // Item view must be MATCH_PARENT x MATCH_PARENT
                    val container = android.widget.FrameLayout(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }

                    // ImageView fills the item (match_parent x match_parent)
                    val iv = android.widget.ImageView(ctx).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        adjustViewBounds = false
                        scaleType = android.widget.ImageView.ScaleType.FIT_CENTER // or CENTER_INSIDE
                    }

                    container.addView(iv)
                    return PageViewHolder(container, iv)
                }

                override fun getItemCount(): Int = pdfRenderer.pageCount

                override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
                    // recycle previous bitmap if any
                    val prev = holder.imageView.tag
                    if (prev is android.graphics.Bitmap && !prev.isRecycled) {
                        holder.imageView.setImageDrawable(null)
                        prev.recycle()
                    }

                    val page = pdfRenderer.openPage(position)
                    try {
                        // we'll scale to fit pager width while keeping aspect ratio
                        val targetWidth = displayMetrics.widthPixels - (24 * displayMetrics.density).toInt()
                        val scale = targetWidth.toFloat() / page.width.toFloat()
                        val bmpWidth = (page.width * scale).toInt().coerceAtLeast(1)
                        val bmpHeight = (page.height * scale).toInt().coerceAtLeast(1)

                        val bitmap = android.graphics.Bitmap.createBitmap(
                            bmpWidth,
                            bmpHeight,
                            android.graphics.Bitmap.Config.ARGB_8888
                        )

                        // Use matrix transform mapping page -> bitmap
                        val srcRectF = android.graphics.RectF(0f, 0f, page.width.toFloat(), page.height.toFloat())
                        val dstRectF = android.graphics.RectF(0f, 0f, bmpWidth.toFloat(), bmpHeight.toFloat())
                        val matrix = android.graphics.Matrix().apply {
                            setRectToRect(srcRectF, dstRectF, android.graphics.Matrix.ScaleToFit.FILL)
                        }

                        page.render(bitmap, null, matrix, android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                        holder.imageView.setImageBitmap(bitmap)
                        holder.imageView.tag = bitmap
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    } finally {
                        page.close()
                    }
                }

                override fun onViewRecycled(holder: PageViewHolder) {
                    super.onViewRecycled(holder)
                    val tag = holder.imageView.tag
                    if (tag is android.graphics.Bitmap && !tag.isRecycled) {
                        tag.recycle()
                    }
                    holder.imageView.setImageDrawable(null)
                    holder.imageView.tag = null
                }
            }

            val adapter = PdfPagerAdapter(renderer, context)
            viewPager.adapter = adapter

            viewPager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    pageIndicator.text = "Page ${position + 1} of ${renderer.pageCount}"
                }
            })

            val themedContext = android.view.ContextThemeWrapper(
                context,
                android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
            )

            val dialog = androidx.appcompat.app.AlertDialog.Builder(themedContext)
                .setTitle("PDF Preview")
                .setView(container)
                .setNegativeButton(resources.getString(R.string.close)) { d, _ -> d.dismiss() }
                .create()

            dialog.setOnDismissListener {
                // release resources
                try { viewPager.adapter = null } catch (_: Exception) {}
                try { renderer.close() } catch (_: Exception) {}
                try { pfd.close() } catch (_: Exception) {}
            }

            dialog.show()

            // Ensure dialog window width is reasonable and keep our pagerHeight as set earlier.
            dialog.window?.setLayout(
                (displayMetrics.widthPixels * 0.95).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Unable to preview PDF", Toast.LENGTH_SHORT).show()
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
            .setPositiveButton(resources.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    /** Shows a dialog with:
     *  - title
     *  - multi-line message text
     *  - custom horizontal row of buttons (equal width)
     */
    private fun showInfoWithHorizontalButtonsDialog(
        title: String,
        message: String,
        actions: List<DocAction>
    ) {
        val ctx = requireContext()
        val dp = ctx.resources.displayMetrics.density

        val container = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            // keep some room so stroke isn't clipped by dialog edges
            val pad = (14 * dp).toInt()
            setPadding(pad, pad, pad, (12 * dp).toInt())
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val tv = TextView(ctx).apply {
            text = message
            setTextColor(ContextCompat.getColor(ctx, android.R.color.black))
            textSize = 14f
        }
        container.addView(tv)

        val row = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            weightSum = actions.size.takeIf { it > 0 }?.toFloat() ?: 1f
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = (16 * dp).toInt() }
            clipToPadding = false
        }
        container.addView(row)

        var dialog: AlertDialog? = null

        // Colors (use your project colors where available)
        val strokeColor = try { ContextCompat.getColor(ctx, R.color.color_dark_blue) } catch (_: Exception) { ContextCompat.getColor(ctx, android.R.color.holo_blue_dark) }
        val textColor = strokeColor
        val rippleColor = ColorUtils.setAlphaComponent(strokeColor, 80) // subtle ripple
        val transparentFill = android.graphics.Color.TRANSPARENT

        // Helper that creates a rounded outline drawable + ripple, with a small inset to avoid clipping
        fun makeButtonBackground(): android.graphics.drawable.Drawable {
            val cornerRadius = (10 * dp)
            val strokeWidth = (1.8f * dp).toInt() // slightly thicker so it's visible

            val rounded = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                this.cornerRadius = cornerRadius
                setColor(transparentFill)       // transparent fill so stroke sits above background
                setStroke(strokeWidth, strokeColor)
            }

            // Use the same rounded drawable as mask to avoid ripple clipping
            val mask = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                this.cornerRadius = cornerRadius
                setColor(android.graphics.Color.WHITE)
            }

            // Add a tiny inset so the stroke is not clipped by parent bounds
            val inset = (1.5f * dp).toInt()
            val insetDrawable = InsetDrawable(rounded, inset, inset, inset, inset)

            return RippleDrawable(ColorStateList.valueOf(rippleColor), insetDrawable, mask)
        }

        actions.forEach { action ->
            val btn = AppCompatButton(ctx).apply {
                text = action.label
                isAllCaps = false
                setTextColor(textColor)

                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                ).apply {
                    marginStart = (6 * dp).toInt()
                    marginEnd = (6 * dp).toInt()
                }

                minHeight = (44 * dp).toInt()
                setPadding((10 * dp).toInt(), (10 * dp).toInt(), (10 * dp).toInt(), (10 * dp).toInt())

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

        dialog.show()
    }

    private class YearlyFinancialAdapter(
        private val items: List<YearlyFinancialItem>,
        private val onViewAttachment: (YearlyFinancialItem) -> Unit
    ) : RecyclerView.Adapter<YearlyFinancialAdapter.VH>() {

        inner class VH(view: View) : RecyclerView.ViewHolder(view) {
            val tvYear: TextView = view.findViewById(R.id.tvYear)
            val tvAmount: TextView = view.findViewById(R.id.tvAmount)
            val btnView: Button = view.findViewById(R.id.btnView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_financial_details_card, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = items[position]
            holder.tvYear.text = item.year ?: "-"
            holder.tvAmount.text = formatAmount(item.amount)

            if (!item.attachmentBase64.isNullOrBlank()) {
                holder.btnView.visibility = View.VISIBLE
                holder.btnView.isEnabled = true
                holder.btnView.setOnClickListener { onViewAttachment(item) }
            } else {
                holder.btnView.visibility = View.GONE
                holder.btnView.isEnabled = false
            }
        }

        override fun getItemCount(): Int = items.size

        private fun formatAmount(value: Double?): String {
            if (value == null) return "₹ -"
            val nf = NumberFormat.getInstance(Locale.getDefault())
            nf.maximumFractionDigits = 2
            return "₹ ${nf.format(value)}"
        }
    }

    private fun showFinancialDialog(title: String,list: List<YearlyFinancialItem>?) {
        if (list.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No annual turnover data found", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_fiel_verification, null)
        val rv = dialogView.findViewById<RecyclerView>(R.id.rvAnnualTurnover)
        rv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = YearlyFinancialAdapter(list) { item ->
            val base64 = item.attachmentBase64
            if (!base64.isNullOrBlank()) {
                // reuse your existing PDF opener
                openBase64Pdf(requireContext(), base64)
            } else {
                Toast.makeText(requireContext(), "No attachment for ${item.year}", Toast.LENGTH_SHORT).show()
            }
        }
        rv.adapter = adapter

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(dialogView)
            .setNegativeButton(resources.getString(R.string.close), null)
            .show()
    }

    private class YearlyTrainingAdapter(
        private val items: List<YearlyTrainingItem>,
        private val onViewAttachment: (YearlyTrainingItem) -> Unit
    ) : RecyclerView.Adapter<YearlyTrainingAdapter.VH>() {

        inner class VH(view: View) : RecyclerView.ViewHolder(view) {
            val tvYear: TextView = view.findViewById(R.id.tvYear)
            val tvAllocated: TextView = view.findViewById(R.id.tvAllocated)
            val tvAchieved: TextView = view.findViewById(R.id.tvAchieved)
            val btnView: Button = view.findViewById(R.id.btnView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_yearly_training_row, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = items[position]
            holder.tvYear.text = item.year ?: "-"
            holder.tvAllocated.text = "Allocated: ${formatNumber(item.targetAllocated)}"
            holder.tvAchieved.text = "Achieved: ${formatNumber(item.targetAchieved)}"

            if (!item.attachmentBase64.isNullOrBlank()) {
                holder.btnView.visibility = View.VISIBLE
                holder.btnView.isEnabled = true
                holder.btnView.setOnClickListener { onViewAttachment(item) }
            } else {
                holder.btnView.visibility = View.GONE
                holder.btnView.isEnabled = false
            }
        }

        override fun getItemCount(): Int = items.size

        private fun formatNumber(value: Double?): String {
            if (value == null) return "-"
            val nf = NumberFormat.getInstance(Locale.getDefault())
            nf.maximumFractionDigits = 2
            return nf.format(value)
        }
    }

    private fun showTrainingDialog(title: String, list: List<YearlyTrainingItem>?) {
        if (list.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No $title data found", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_yearly_training_list, null)
        val rv = dialogView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvYearlyTraining)
        rv.layoutManager = LinearLayoutManager(requireContext())

        val adapter = YearlyTrainingAdapter(list) { item ->
            val base64 = item.attachmentBase64
            if (!base64.isNullOrBlank()) {
                // Prefer your existing openBase64Pdf(context, base64) if present:
                try {
                    openBase64Pdf(requireContext(), base64)
                } catch (e: Exception) {
                    // fallback to generic file opener
                    Toast.makeText(requireContext(), "Pdf Error", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "No attachment for ${item.year}", Toast.LENGTH_SHORT).show()
            }
        }

        rv.adapter = adapter

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(dialogView)
            .setNegativeButton("Close", null)
            .show()
    }

    private fun commitFocusedEditText() {
        try {
            view?.findFocus()?.clearFocus()
        } catch (_: Exception) { /* ignore */ }
    }


    private class YearlyPlacementAdapter(
        private val items: List<YearlyPlacementDetails>,
        private val onViewAttachment: (YearlyPlacementDetails) -> Unit
    ) : RecyclerView.Adapter<YearlyPlacementAdapter.VH>() {

        inner class VH(view: View) : RecyclerView.ViewHolder(view) {
            val tvYear: TextView = view.findViewById(R.id.tvYearPlacement)
            val tvCandidatePlaced: TextView = view.findViewById(R.id.tvCandidatePlaced)
            val tvSanctionOrderIdPlacement: TextView = view.findViewById(R.id.tvSanctionOrderIdPlacement)
            val tvEsicNumberPlacement: TextView = view.findViewById(R.id.tvEsicNumberPlacement)
            val tvEpfoNumberPlacement: TextView = view.findViewById(R.id.tvEpfoNumberPlacement)
            val btnViewPlacement: Button = view.findViewById(R.id.btnViewPlacement)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_yearly_placement_row, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = items[position]
            holder.tvYear.text = boldLabel("Year :", item.year?:"-")
            holder.tvCandidatePlaced.text = boldLabel("Candidate's Placed :", item.candidatePlaced.toString())
            holder.tvSanctionOrderIdPlacement.text = boldLabel("Sanction Order :", item.sanctionOrderId.toString())
            holder.tvEsicNumberPlacement.text = boldLabel("ESIC No. :", item.esicNumber.toString())
            holder.tvEpfoNumberPlacement.text = boldLabel("EPFO No. :", item.epfoNumber.toString())

            if (!item.proofDocument.isNullOrBlank()) {
                holder.btnViewPlacement.visibility = View.VISIBLE
                holder.btnViewPlacement.isEnabled = true
                holder.btnViewPlacement.setOnClickListener { onViewAttachment(item) }
            } else {
                holder.btnViewPlacement.visibility = View.GONE
                holder.btnViewPlacement.isEnabled = false
            }
        }

        fun boldLabel(label: String, value: String): SpannableString {
            val fullText = "$label $value"
            val spannable = SpannableString(fullText)
            spannable.setSpan(
                android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0,
                label.length, // bold only the label
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return spannable
        }

        override fun getItemCount(): Int = items.size

        private fun formatAmount(value: Double?): String {
            if (value == null) return "₹ -"
            val nf = NumberFormat.getInstance(Locale.getDefault())
            nf.maximumFractionDigits = 2
            return "₹ ${nf.format(value)}"
        }
    }

    private fun showPlacementDialog(title: String,list: List<YearlyPlacementDetails>?) {
        if (list.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No Placement data found", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_yearly_placement_list, null)
        val rv = dialogView.findViewById<RecyclerView>(R.id.rvYearlyPlacement)
        rv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = YearlyPlacementAdapter(list) { item ->
            val base64 = item.proofDocument
            if (!base64.isNullOrBlank()) {
                // reuse your existing PDF opener
                openBase64Pdf(requireContext(), base64)
            } else {
                Toast.makeText(requireContext(), "No attachment for ${item.year}", Toast.LENGTH_SHORT).show()
            }
        }
        rv.adapter = adapter

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(dialogView)
            .setNegativeButton(resources.getString(R.string.close), null)
            .show()
    }



    private fun validateAllRemarksForSection(
        items: List<FieldVerificationItem>,
        errorMessageForItem: (FieldVerificationItem) -> String = { "Please enter remark for ${it.requirement}" }
    ): Map<String, String>? {
        commitFocusedEditText()

        val remarkItems = items.filter { it.allowRemark }
        if (remarkItems.isEmpty()) return emptyMap() // nothing to validate

        remarkItems.forEach { item ->
            val r = item.remarkText?.trim().orEmpty()
            if (r.isEmpty()) {
                Toast.makeText(requireContext(), errorMessageForItem(item), Toast.LENGTH_SHORT).show()
                return null
            }
        }

        // Build result map where key = requirement (or any id you prefer)
        val result = remarkItems.associate { it.requirement to (it.remarkText!!.trim()) }
        return result
    }

    /**
     * Collect remark items from one list (sectionItems) and return non-empty remarks.
     */
    private fun collectRemarksFromSection(
        sectionName: String,
        sectionItems: List<FieldVerificationItem>
    ): List<RemarkItem> {

        commitFocusedEditText()

        return sectionItems
            .filter { it.allowRemark }
            .mapNotNull { item ->
                val remark = item.remarkText?.trim().orEmpty()
                if (remark.isEmpty()) null
                else RemarkItem(
                    section = sectionName,
                    requirement = item.id,   // <── SEND ID, NOT LONG TEXT
                    remark = remark
                )
            }
    }

    private fun collectAllRemarksSectionWise(): Map<String, List<RemarkItem>> {
        commitFocusedEditText()

        val result = mutableMapOf<String, List<RemarkItem>>()

        result["Organization"] = collectRemarksFromSection("Organization", orgItems)
        result["Finance"] = collectRemarksFromSection("Finance", finItems)
        result["Training"] = collectRemarksFromSection("Training", trainingItems)
        result["TrainingInfra"] = collectRemarksFromSection("TrainingInfra", trainingInfraItems)
        result["Certification"] = collectRemarksFromSection("Certification", certItems)
        result["Placement"] = collectRemarksFromSection("Placement", placementItems)
        result["FieldVisit"] = collectRemarksFromSection("FieldVisit", fieldItems)

        return result
    }


}
