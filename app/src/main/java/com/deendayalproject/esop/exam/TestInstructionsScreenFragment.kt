package com.deendayalproject.esop.exam

import SharedViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.SystemClock
import android.util.Base64
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.EsopFragmentBinding
import com.deendayalproject.databinding.EsopTestinstructionscreenfragmentBinding
import com.deendayalproject.databinding.EsopWrongquestionsscreenFragmentBinding
import com.deendayalproject.esop.dashboard.DashboardSection
//import com.deendayalproject.esop.face.FaceVerificationDialog
import com.deendayalproject.esop.profile.CandidateInfoCard
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.model.request.EsopCandidateRequest
import com.deendayalproject.model.response.EsopCandidateRes
import com.deendayalproject.network.SecurePreferenceManager.getToken
import com.deendayalproject.uidai.XstreamCommonMethods
import com.deendayalproject.uidai.XstreamCommonMethods.respDecodedXmlToPojoAuth
import com.deendayalproject.uidai.capture.CaptureResponse
import com.deendayalproject.uidai.ekyc.UidaiKycRequest
import com.deendayalproject.util.AppConstant
import com.deendayalproject.util.AppConstant.LANGUAGE
import com.deendayalproject.util.AppConstant.PRODUCTION
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.decodeBase64
import com.deendayalproject.util.toastLong
import com.deendayalproject.util.toastShort
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.getValue



@AndroidEntryPoint
class TestInstructionsScreenFragment :
    BaseFragment<EsopTestinstructionscreenfragmentBinding>(
        bindingInflater = EsopTestinstructionscreenfragmentBinding::inflate
    ) {

    private val viewModel: SharedViewModel by viewModels()

    private var candidateId = ""
    private var batchId = ""

    private lateinit var navController: NavController

    private var candidateName by mutableStateOf("")
    private var candidateMobileNo by mutableStateOf("")
    private var aadhaarNumber = ""
    private var startTime: Long = 0
    private var userPhotoUIADI: Bitmap? = null



    private var ekycImage: String = ""
    private var userName = ""
    private var selectedCertificateType = ""
    private var selectedDepartment = ""
    private var loginId = ""
    private var mobile = ""
    private var email = ""
    private var name = ""
    private var dob = ""
    private var gender = ""
    private var careOf = ""
    private var state = ""
    private var dist = ""
    private var block = ""
    private var po = ""
    private var pinCode = ""
    private var street = ""
    private var village = ""
    private var photo = ""
    private var facultyName = ""

    private var candidateLoginId by mutableStateOf("")
    private var candidateLoginEmail by mutableStateOf("")
//    private val categoryList = mutableListOf<String>()
    private var categoryList: ArrayList<String> = arrayListOf()

    private var ESOPCandidateList: List<EsopCandidateRes.EsopCandidate> = emptyList()

    private val certificateTypeList = listOf("Professional", "Master")

    // Camera permission launcher
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(requireContext(), "Camera permission required for face verification", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initializeViews() {

        hideStatusBar()

        navController = findNavController()

        // Camera permission yahi maang lete hain shuru me
        if (!hasCameraPermission()) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

         loginId = arguments?.getString("loginId").orEmpty()
         userName = arguments?.getString("userName").orEmpty()
         mobile = arguments?.getString("mobile").orEmpty()
         email = arguments?.getString("email").orEmpty()
         gender = arguments?.getString("gender").orEmpty()
         aadhaarNumber = arguments?.getString("aadhaarNumber").orEmpty()

         categoryList = arguments?.getStringArrayList("categoryList") ?: arrayListOf()

        val token = getToken(requireContext())

        binding.composeESOPinstructionTestScreen.apply {

            setContent {

                Scaffold(

                    topBar = {
                        PremiumTopBar(
                            dynamicTitle = getString(R.string.esop),
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    },

                    containerColor = Color.White

                ) { paddingValues ->

                    TestInstructionsContent(
                        navController = navController,
                        loginId = loginId,
                        userName = userName,
                        mobile = mobile,
                        email = email,
                        gender = gender,
                        categoryList = categoryList,
                        paddingValues = paddingValues,
                        hasCameraPermission = { hasCameraPermission() },
                        onRequestPermission = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                        onVerificationSuccess = {
                            // ---------- Yaha se aap actual test screen pe navigate karenge ----------
                            // navController.navigate(R.id.action_to_testScreen)
                            Toast.makeText(requireContext(), "Verified! Starting test...", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }

    override fun setupObservers() {}
    override fun setupClickListeners() {}
    override fun loadInitialData() {}

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TestInstructionsContent(
        navController: NavController,
        loginId: String,
        userName: String,
        mobile: String,
        email: String,
        gender: String,
        categoryList: List<String>,
        paddingValues: PaddingValues,
        hasCameraPermission: () -> Boolean,
        onRequestPermission: () -> Unit,
        onVerificationSuccess: () -> Unit
    ) {

        var isCertDropdownExpanded by remember { mutableStateOf(false) }
//        var selectedCertificateType by remember { mutableStateOf("") }

        var isDeptDropdownExpanded by remember { mutableStateOf(false) }

//        var selectedDepartment by remember {
//            mutableStateOf(if (categoryList.size == 1) categoryList[0] else "")
        selectedDepartment = if (categoryList.size == 1) categoryList[0] else ""
//        }

        var isAcknowledged by remember { mutableStateOf(false) }

        var totalQuestions by remember { mutableStateOf(0) }
        var timeDuration by remember { mutableStateOf("50 Minutes") }
        var markPerQuestion by remember { mutableStateOf(1) }

        // ---------- Naya state: face verification dialog show karna hai ya nahi ----------
        var showFaceVerification by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    Text(
                        text = "Test Instructions",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    ExposedDropdownMenuBox(
                        expanded = isCertDropdownExpanded,
                        onExpandedChange = { isCertDropdownExpanded = !isCertDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCertificateType.ifEmpty { "Please Select Your Certificate of Type" },
//                            value = selectedCertificateType.ifEmpty { aadhaarNumber },
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = {
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedTextColor = if (selectedCertificateType.isEmpty()) Color.Gray else Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedBorderColor = Color.LightGray,
                                focusedBorderColor = Color(0xFF2962FF)
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = isCertDropdownExpanded,
                            onDismissRequest = { isCertDropdownExpanded = false }
                        ) {
                            certificateTypeList.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        selectedCertificateType = type
                                        isCertDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    InfoCard(icon = Icons.Default.Assignment, label = "Total Questions", value = totalQuestions.toString())
                }

                item {
                    InfoCard(icon = Icons.Default.AccessTime, label = "Time Duration", value = timeDuration)
                }

                item {
                    if (categoryList.size <= 1) {
                        InfoCard(
                            icon = Icons.Default.LocalFireDepartment,
                            label = "Department Selected",
                            value = if (categoryList.isNotEmpty()) categoryList[0] else "Not Available"
                        )
                    } else {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F8)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
                                Text(text = "Department Selected", fontSize = 14.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(6.dp))

                                ExposedDropdownMenuBox(
                                    expanded = isDeptDropdownExpanded,
                                    onExpandedChange = { isDeptDropdownExpanded = !isDeptDropdownExpanded }
                                ) {
                                    OutlinedTextField(
                                        value = selectedDepartment.ifEmpty { "Select Department" },
                                        onValueChange = {},
                                        readOnly = true,
                                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                                        textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                                        trailingIcon = {
                                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                                        },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedTextColor = if (selectedDepartment.isEmpty()) Color.Gray else Color.Black,
                                            focusedTextColor = Color.Black,
                                            unfocusedBorderColor = Color.LightGray,
                                            focusedBorderColor = Color(0xFF2962FF)
                                        )
                                    )

                                    ExposedDropdownMenu(
                                        expanded = isDeptDropdownExpanded,
                                        onDismissRequest = { isDeptDropdownExpanded = false }
                                    ) {
                                        categoryList.forEach { dept ->
                                            DropdownMenuItem(
                                                text = { Text(dept) },
                                                onClick = {
                                                    selectedDepartment = dept
                                                    isDeptDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    InfoCard(icon = Icons.Default.LocalFireDepartment, label = "Mark per Question", value = markPerQuestion.toString())
                }

                item {
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(
                            checked = isAcknowledged,
                            onCheckedChange = { isAcknowledged = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2962FF))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "I acknowledge that my identity will be verified through facial recognition using eye-blink detection before the examination begins. For security and examination integrity purposes, the camera may remain active throughout the test, and my face may be verified periodically until the examination is completed. If face verification fails or an unauthorized person is detected, the examination may be suspended or terminated.",
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }

                // ---------- Start New Test Button ----------
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            when {
                                selectedCertificateType.isEmpty() -> {
                                    Toast.makeText(navController.context, "Please select certificate type", Toast.LENGTH_SHORT).show()
                                }
                                categoryList.size > 1 && selectedDepartment.isEmpty() -> {
                                    Toast.makeText(navController.context, "Please select department", Toast.LENGTH_SHORT).show()
                                }
                                !isAcknowledged -> {
                                    Toast.makeText(navController.context, "Please acknowledge the terms", Toast.LENGTH_SHORT).show()
                                }
                                !hasCameraPermission() -> {
                                    onRequestPermission()
                                }
                                else -> {

                                    val bundle = bundleOf(
                                        "loginId" to loginId,
                                        "userName" to userName,
                                        "mobile" to mobile,
                                        "email" to email,
                                        "gender" to gender,
                                        "candidateLoginId" to candidateLoginId,
                                        "candidateMobileNo" to candidateMobileNo,
                                        "selectedCertificateType" to selectedCertificateType,
                                        "selectedDepartment" to selectedDepartment
                                    )

                                    navController.navigate(R.id.action_esopFragment_to_esopgetTestScreenFragment,bundle)

//                                    invokeCaptureIntent()





                                    // ---------- Sab validation pass -> Face Verification dialog open ----------
                                    showFaceVerification = true
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2962FF))
                    ) {
                        Text(text = "Start New Test", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // ---------- Face Verification Overlay (button click par yahi trigger hota hai) ----------
//            if (showFaceVerification) {
//                FaceVerificationDialog(
//                    onDismiss = { showFaceVerification = false },
//                    onVerified = {
//                        showFaceVerification = false
//                        onVerificationSuccess()
//                    }
//                )
//            }
        }
    }

    @Composable
    fun InfoCard(icon: ImageVector, label: String, value: String) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F8)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF2962FF), modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = label, fontSize = 14.sp, color = Color.Gray)
                    Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }





    @RequiresApi(Build.VERSION_CODES.O)
    private val startUidaiAuthResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data

                    if (intent != null) {
                        val captureResponse =
                            intent.getStringExtra(AppConstant.CAPTURE_INTENT_RESPONSE_DATA)

                        if (!captureResponse.isNullOrEmpty()) {
                            handleCaptureResponse(captureResponse)
                        } else {
                            toastShort(getString(R.string.capture_response_is_empty))
                        }
                    } else {
                        toastShort(getString(R.string.failed_to_get_capture_response_data))
                    }
                } else {
                    toastLong(getString(R.string.failed_to_capture_data))
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
                toastShort(getString(R.string.error_missing_data_in_result))

            } catch (e: Exception) {
                e.printStackTrace()
                toastShort(getString(R.string.an_error_occurred_while_processing_the_result))
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleCaptureResponse(captureResponse: String) {
        try {


            //  toastShort(decryptedAadhaar)
            // Parse the capture response XML to an object
            val response = CaptureResponse.fromXML(captureResponse)

            if (response.isSuccess) {

                showProgressDialog(getString(R.string.please_wait))
                // Process the response to generate the PoiType or other required fields
                val poiType = XstreamCommonMethods.processPidBlockEkyc(
                    response.toXML(),
                    "456718077531",
//                    aadhaarNumber,

                    false,
                    requireContext()
                )


                // Define Pre-Production URL (use a constant or environment configuration in production)
                //  val authURL = "http://10.247.252.95:8080/NicASAServer/ASAMain" //preProd
                val authURL = "http://10.247.252.93:8080/NicASAServer/ASAMain"  //Prod

                // Record the start time for elapsed time computation
                startTime = SystemClock.elapsedRealtime()


                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.postOnAUAFaceAuthNREGA(
                        AppConstant.FACE_AUTH_UIADI,
                        UidaiKycRequest(poiType, authURL))
                }

                collectFaceAuthResponse()
                // Handle Aadhaar authentication or additional processing here if required
            } else {
                dismissProgressDialog()
                toastLong(getString(R.string.failed_attendance))

            }


        } catch (e: SecurityException) {
            // Handle camera permission-related issues
            dismissProgressDialog()
            e.printStackTrace()

            toastShort(getString(R.string.camera_permission_is_required_for_this_feature))
        } catch (e: IllegalArgumentException) {
            // Handle cases where the response parsing might fail
            dismissProgressDialog()
            e.printStackTrace()
            toastShort(getString(R.string.invalid_capture_response_format))
        } catch (e: Exception) {
            // Catch all other exceptions
            dismissProgressDialog()
            e.printStackTrace()
            toastShort(getString(R.string.an_error_occurred_while_processing_the_response))
        }
    }

    private fun getTransactionID(): String {
        val prefix = "DDUGKY"
        val suffix = "AEAD"

        // 12 digit random number
        val random = SecureRandom()
        val n = (100000000000L + (random.nextDouble() * 900000000000L)).toLong()

        val date = Date()

        val yyyy = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
        val mm = SimpleDateFormat("MM", Locale.getDefault()).format(date)
        val dd = SimpleDateFormat("dd", Locale.getDefault()).format(date)

        val hh = SimpleDateFormat("HH", Locale.getDefault()).format(date) // 24-hour format better
        val min = SimpleDateFormat("mm", Locale.getDefault()).format(date)
        val ss = SimpleDateFormat("ss", Locale.getDefault()).format(date)

        val strDate = yyyy + mm + dd
        val strTime = hh + min + ss

        return "$prefix$n$strDate$strTime$suffix"
    }



    private fun createPidOptions(txnId: String, purpose: String): String {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<PidOptions ver=\"1.0\" env=\"$PRODUCTION\">\n" + "   <Opts fCount=\"\" fType=\"\" iCount=\"\" iType=\"\" pCount=\"\" pType=\"\" format=\"\" pidVer=\"2.0\" timeout=\"\" otp=\"\" wadh=\"${AppConstant.WADH_KEY}\" posh=\"\" />\n" + "   <CustOpts>\n" + "      <Param name=\"txnId\" value=\"${txnId}\"/>\n" + "      <Param name=\"purpose\" value=\"$purpose\"/>\n" + "      <Param name=\"language\" value=\"$LANGUAGE}\"/>\n" + "   </CustOpts>\n" + "</PidOptions>"
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun collectFaceAuthResponse() {

        viewModel.postOnAUAFaceAuthNREGA
            .observe(viewLifecycleOwner) { result ->

                when {
                    result.isSuccess -> {
                        dismissProgressDialog()

                        val uidaiData = result.getOrNull()
                        if (uidaiData == null) {
                            toastShort(getString(R.string.server_error_from_uidai))

                            return@observe
                        }

                        try {
                            val kycResp =
                                XstreamCommonMethods.respDecodedXmlToPojoEkyc(
                                    uidaiData.PostOnAUA_Face_authResult
                                )


                            if (kycResp.isSuccess) {

                                // 🔹 Photo decode
                                val bytes = Base64.decode(kycResp.uidData.pht, Base64.DEFAULT)
                                val bitmap =
                                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                                userPhotoUIADI = bitmap
                                ekycImage = kycResp.uidData.pht ?: ""

                                name = kycResp.uidData.poi.name ?: "N/A"
                                photo = kycResp.uidData.pht ?: "N/A"
                                gender = kycResp.uidData.poi.gender ?: "N/A"
                                dob = kycResp.uidData.poi.dob ?: "N/A"
                                careOf = kycResp.uidData.poa.co ?: "N/A"
                                state = kycResp.uidData.poa.state ?: "N/A"
                                dist = kycResp.uidData.poa.dist ?: "N/A"
                                block = kycResp.uidData.poa.subdist ?: "N/A"
                                village = kycResp.uidData.poa.vtc ?: "N/A"
                                street = kycResp.uidData.poa.loc ?: "N/A"
                                po = kycResp.uidData.poa.po ?: "N/A"
                                pinCode = kycResp.uidData.poa.pc ?: "N/A"

//                                Toast.makeText(requireContext(), kycResp.uidData.poi.name ?: getString(R.string.failed),Toast.LENGTH_SHORT)
//                                    .show()

                                val bundle = bundleOf(
                                    "loginId" to loginId,
                                    "userName" to userName,
                                    "mobile" to mobile,
                                    "email" to email,
                                    "gender" to gender,
                                    "candidateLoginId" to candidateLoginId,
                                    "candidateMobileNo" to candidateMobileNo,
                                    "selectedCertificateType" to selectedCertificateType,
                                    "selectedDepartment" to selectedDepartment
                                )

                                navController.navigate(R.id.action_esopFragment_to_esopgetTestScreenFragment,bundle)

//                                collectFacultyInsertAttendance()


                            }
                            else {
                                dismissProgressDialog()
                                val decodedRar = decodeBase64(kycResp.rar)
                                val authRes = decodedRar?.let {
                                    respDecodedXmlToPojoAuth(it)
                                }
                                val errorDesc =
                                    XstreamCommonMethods.getAuthErrorDescription(authRes?.info)
                                toastShort(errorDesc ?: getString(R.string.ekyc_failed))
                            }

                        } catch (e: Exception) {
                            dismissProgressDialog()
                            e.printStackTrace()
                            toastShort(getString(R.string.error_processing_kyc_response))

                        }
                    }

                    result.isFailure -> {
                        dismissProgressDialog()
                        val error = result.exceptionOrNull()
                        toastShort(error?.message ?: getString(R.string.something_went_wrong))
                    }

                    else -> {
                        // Loading case (agar handleApiCall me post karte ho)
                        showProgressDialog()
                    }
                }
            }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun invokeCaptureIntent() {

        try {
            val intent1 = Intent(AppConstant.CAPTURE_INTENT)
            intent1.putExtra(
                AppConstant.CAPTURE_INTENT_REQUEST,
                createPidOptions(getTransactionID(), "auth")
            )
            startUidaiAuthResult.launch(intent1)

        } catch (exp: Exception) {

            toastShort("Failed to open capture app")
        }

    }

    private fun collectFacultyInsertAttendance() {
        lifecycleScope.launch {
            viewModel.insertFacultyAttandance.observe(viewLifecycleOwner) { it ->
                it.onSuccess { response ->
                    dismissProgressDialog()

                    when (response.responseCode) {
                        200 -> {

//                            showBottomSheet(userPhotoUIADI,name,gender,dob,careOf)

                        }

                        //  populateSpinnerVillage((response.wrappedList ?: emptyList()) as ArrayList<VillageModel?>, spinnerSelectULB )

                        202 -> Toast.makeText(
                            requireContext(), getString(R.string.no_data_available), Toast.LENGTH_SHORT
                        ).show()

                        301 -> Toast.makeText(
                            requireContext(), getString(R.string.please_upgrade_your_app), Toast.LENGTH_SHORT
                        ).show()

                        401 -> AppUtil.showSessionExpiredDialog(
                            findNavController(), requireContext()
                        )
                    }
                }
                it.onFailure {
                    dismissProgressDialog()
                    Toast.makeText(requireContext(), it.message ?: getString(R.string.failed),Toast.LENGTH_SHORT)
                        .show()

                }
            }
        }
    }

}


