package com.deendayalproject.fragments

import SharedViewModel
import android.Manifest
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
import com.deendayalproject.util.AppUtil
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
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
                        base64ConformanceFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
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
                        base64SwitchBoardImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }
                    "WireSecurity" -> {
                        ivWireSecurityPreview.setImageURI(photoUri)
                        ivWireSecurityPreview.visibility = View.VISIBLE
                        base64WireSecurityImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }
                    "leakage" -> {
                        ivLeakagePreview.setImageURI(photoUri)
                        ivLeakagePreview.visibility = View.VISIBLE
                        base64LeakageImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }
                    "stairs" -> {
                        ivStairsPreview.setImageURI(photoUri)
                        ivStairsPreview.visibility = View.VISIBLE
                        base64StairsImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Photo capture failed", Toast.LENGTH_SHORT).show()
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) launchCamera()
            else Toast.makeText(requireContext(), "Camera permission is required.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_training, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupExpandableSections(view)

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

        // Setup Yes/No adapter
        val yesNoAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf("--Select--", "Yes", "No")).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Setup CCTV & Electrical Spinners
        val spinners = listOf(
            R.id.spinnerMonitorAccessible, R.id.spinnerConformance, R.id.spinnerStorage,
            R.id.spinnerDVRStaticIP, R.id.spinnerIPEnabled, R.id.spinnerResolution,
            R.id.spinnerVideoStream, R.id.spinnerRemoteAccessBrowser, R.id.spinnerSimultaneousAccess,
            R.id.spinnerSupportedProtocols, R.id.spinnerColorVideoAudio, R.id.spinnerStorageFacility,
            R.id.spinnerSwitchBoards, R.id.spinnerSecuringWires
        )
        spinners.forEach {
            view.findViewById<Spinner>(it).adapter = yesNoAdapter
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

        // Submit buttons
        view.findViewById<Button>(R.id.btnSubmitCCTVComplianceDetails).setOnClickListener {
            if (validateCCTVForm(view)) submitCCTVData(view)
            else Toast.makeText(requireContext(), "Complete all CCTV fields and photos.", Toast.LENGTH_LONG).show()
        }

        view.findViewById<Button>(R.id.btnSubmitElectricalDetails)?.setOnClickListener {
            if (validateElectricalForm(view)) submitElectricalData(view)
            else Toast.makeText(requireContext(), "Complete all electrical fields and photos.", Toast.LENGTH_LONG).show()
        }

        view.findViewById<Button>(R.id.btnSubmitGeneralDetails).setOnClickListener {
            if (validateGeneralDetailsForm()) submitGeneralDetails()
            else Toast.makeText(requireContext(), "Please complete all fields and photos for General Details.", Toast.LENGTH_LONG).show()
        }

        // Observers
        viewModel.insertCCTVdata.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "CCTV data submitted successfully!", Toast.LENGTH_SHORT).show()
            }
            result.onFailure {
                Toast.makeText(requireContext(), "CCTV submission failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.insertIpenabledata.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Electrical data submitted successfully!", Toast.LENGTH_SHORT).show()
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Electrical submission failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.insertGeneralDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "General details submitted successfully!", Toast.LENGTH_SHORT).show()
            }
            result.onFailure {
                Toast.makeText(requireContext(), "General details submission failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupExpandableSections(view: View) {
        val sections = listOf(
            Triple(R.id.headerTCBasicInfo, R.id.layoutTCBasicInfoContent, R.id.ivToggleTCBasicInfo),
            Triple(R.id.headerInfrastructure, R.id.layoutInfrastructureContent, R.id.ivToggleInfrastructure),
            Triple(R.id.headerCCTVCompliance, R.id.layoutCCTVComplianceContent, R.id.ivToggleCCTVCompliance),
            Triple(R.id.headerElectricalWiring, R.id.layoutElectricalWiringContent, R.id.ivToggleElectricalWiring),
            Triple(R.id.headerGeneralDetails, R.id.layoutGeneralDetailsContent, R.id.ivToggleGeneralDetails),
            Triple(R.id.headerCommonEquipment, R.id.layoutCommonEquipmentContent, R.id.ivToggleCommonEquipment),
            Triple(R.id.headerSignagesInfoBoards, R.id.layoutSignagesInfoBoardsContent, R.id.ivToggleSignagesInfoBoards),
            Triple(R.id.headerAvailableTrainers, R.id.layoutAvailableTrainersContent, R.id.ivToggleAvailableTrainers),
            Triple(R.id.headerTeachingLearningMaterials, R.id.layoutTeachingLearningMaterialsContent, R.id.ivToggleTeachingLearningMaterials),
            Triple(R.id.headerDescriptionOtherAreas, R.id.layoutDescriptionOtherAreasContent, R.id.ivToggleDescriptionOtherAreas),
            Triple(R.id.headerToiletsWashBasins, R.id.layoutToiletsWashBasinsContent, R.id.ivToggleToiletsWashBasins),

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
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            launchCamera()
        else permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun launchCamera() {
        val photoFile = createImageFile()
        if (photoFile == null) {
            Toast.makeText(requireContext(), "Failed to create image file", Toast.LENGTH_SHORT).show()
            return
        }
        photoUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", photoFile)
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
            R.id.spinnerMonitorAccessible, R.id.spinnerConformance, R.id.spinnerStorage,
            R.id.spinnerDVRStaticIP, R.id.spinnerIPEnabled, R.id.spinnerResolution,
            R.id.spinnerVideoStream, R.id.spinnerRemoteAccessBrowser, R.id.spinnerSimultaneousAccess,
            R.id.spinnerSupportedProtocols, R.id.spinnerColorVideoAudio, R.id.spinnerStorageFacility
        ).all {
            view.findViewById<Spinner>(it).selectedItem != "--Select--"
        }

        val photosOk = base64MonitorFile != null && base64ConformanceFile != null &&
                base64StorageFile != null && base64DVRFile != null

        return spinnerOk && photosOk
    }

    private fun validateElectricalForm(view: View): Boolean {
        val switchSelected = view.findViewById<Spinner>(R.id.spinnerSwitchBoards).selectedItem != "--Select--"
        val wireSelected = view.findViewById<Spinner>(R.id.spinnerSecuringWires).selectedItem != "--Select--"
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
        viewModel.submitCCTVDataToServer(request,token)
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
            tcId = AppUtil.getSavedTcId(requireContext()),
            sanctionOrder = AppUtil.getSavedSanctionOrder(requireContext())
        )
        viewModel.submitElectricalData(request,token)
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
            tcId = AppUtil.getSavedTcId(requireContext()),
            sanctionOrder = AppUtil.getSavedSanctionOrder(requireContext())
            )
        viewModel.submitGeneralDetails(request,token)

  }
}
