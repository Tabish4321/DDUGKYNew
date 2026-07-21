package com.deendayalproject.esop.exam

import SharedViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
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
import com.deendayalproject.util.AppUtil
import dagger.hilt.android.AndroidEntryPoint
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
    private var candidateGender by mutableStateOf("")
    private var candidateLoginId by mutableStateOf("")
    private var candidateLoginEmail by mutableStateOf("")
    private val categoryList = mutableListOf<String>()

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

        val loginId = arguments?.getString("loginId").orEmpty()
        val userName = arguments?.getString("userName").orEmpty()
        val mobile = arguments?.getString("mobile").orEmpty()
        val email = arguments?.getString("email").orEmpty()
        val gender = arguments?.getString("gender").orEmpty()

        val categoryList: ArrayList<String> =
            arguments?.getStringArrayList("categoryList") ?: arrayListOf()

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
        var selectedCertificateType by remember { mutableStateOf("") }

        var isDeptDropdownExpanded by remember { mutableStateOf(false) }

        var selectedDepartment by remember {
            mutableStateOf(if (categoryList.size == 1) categoryList[0] else "")
        }

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
}


//@AndroidEntryPoint
//class TestInstructionsScreenFragment :
//    BaseFragment<EsopTestinstructionscreenfragmentBinding>(
//        bindingInflater = EsopTestinstructionscreenfragmentBinding::inflate
//    ) {
//
//    private val viewModel: SharedViewModel by viewModels()
//
//    private var candidateId = ""
//    private var batchId = ""
//
//    private lateinit var navController: NavController
//
//    private var candidateName by mutableStateOf("")
//    private var candidateMobileNo by mutableStateOf("")
//    private var candidateGender by mutableStateOf("")
//    private var candidateLoginId by mutableStateOf("")
//    private var candidateLoginEmail by mutableStateOf("")
//    private val categoryList = mutableListOf<String>()
//
//    private var ESOPCandidateList: List<EsopCandidateRes.EsopCandidate> = emptyList()
//
//    // Static list for top dropdown
//    private val certificateTypeList = listOf("Professional", "Master")
//
//    @RequiresApi(Build.VERSION_CODES.R)
//    override fun initializeViews() {
//
//        hideStatusBar()
//
//        // Initialize NavController
//        navController = findNavController()
//
//        val loginId = arguments?.getString("loginId").orEmpty()
//        val userName = arguments?.getString("userName").orEmpty()
//        val mobile = arguments?.getString("mobile").orEmpty()
//        val email = arguments?.getString("email").orEmpty()
//        val gender = arguments?.getString("gender").orEmpty()
//
//        val categoryList: ArrayList<String> =
//            arguments?.getStringArrayList("categoryList") ?: arrayListOf()
//
//        val token = getToken(requireContext())
//
//        binding.composeESOPinstructionTestScreen.apply {
//
//            setContent {
//
//                Scaffold(
//
//                    topBar = {
//
//                        PremiumTopBar(
//                            dynamicTitle = getString(R.string.esop),
//                            onBackClick = {
//                                navController.popBackStack()
//                            }
//                        )
//
//                    },
//
//                    containerColor = Color.White
//
//                ) { paddingValues ->
//
//                    TestInstructionsContent(
//                        navController = navController,
//                        loginId = loginId,
//                        userName = userName,
//                        mobile = mobile,
//                        email = email,
//                        gender = gender,
//                        categoryList = categoryList,
//                        paddingValues = paddingValues
//                    )
//                }
//            }
//        }
//    }
//
//    override fun setupObservers() {
//    }
//
//    override fun setupClickListeners() {
//    }
//
//    override fun loadInitialData() {
//    }
//
//    @OptIn(ExperimentalMaterial3Api::class)
//    @Composable
//    fun TestInstructionsContent(
//        navController: NavController,
//        loginId: String,
//        userName: String,
//        mobile: String,
//        email: String,
//        gender: String,
//        categoryList: List<String>,
//        paddingValues: PaddingValues
//    ) {
//
//        var isCertDropdownExpanded by remember { mutableStateOf(false) }
//        var selectedCertificateType by remember { mutableStateOf("") }
//
//        var isDeptDropdownExpanded by remember { mutableStateOf(false) }
//
//        // Agar categoryList me sirf 1 item hai to wahi auto-selected rahega
//        var selectedDepartment by remember {
//            mutableStateOf(if (categoryList.size == 1) categoryList[0] else "")
//        }
//
//        var isAcknowledged by remember { mutableStateOf(false) }
//
//        // Yeh values API se bhi aa sakti hain, abhi static rakhi hain
//        var totalQuestions by remember { mutableStateOf(0) }
//        var timeDuration by remember { mutableStateOf("50 Minutes") }
//        var markPerQuestion by remember { mutableStateOf(1) }
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(horizontal = 16.dp),
//            contentPadding = PaddingValues(vertical = 12.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//
//            // ---------- Title (ab top pe) ----------
//            item {
//                Text(
//                    text = "Test Instructions",
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//
//            // ---------- Certificate Type Dropdown (Static: Professional / Master) ----------
//            item {
//                ExposedDropdownMenuBox(
//                    expanded = isCertDropdownExpanded,
//                    onExpandedChange = { isCertDropdownExpanded = !isCertDropdownExpanded }
//                ) {
//                    OutlinedTextField(
//                        value = selectedCertificateType.ifEmpty { "Please Select Your Certificate of Type" },
//                        onValueChange = {},
//                        readOnly = true,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .menuAnchor(),
//                        trailingIcon = {
//                            Icon(
//                                imageVector = Icons.Default.ArrowDropDown,
//                                contentDescription = null
//                            )
//                        },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            unfocusedTextColor = if (selectedCertificateType.isEmpty()) Color.Gray else Color.Black,
//                            focusedTextColor = Color.Black,
//                            unfocusedBorderColor = Color.LightGray,
//                            focusedBorderColor = Color(0xFF2962FF)
//                        )
//                    )
//
//                    ExposedDropdownMenu(
//                        expanded = isCertDropdownExpanded,
//                        onDismissRequest = { isCertDropdownExpanded = false }
//                    ) {
//                        certificateTypeList.forEach { type ->
//                            DropdownMenuItem(
//                                text = { Text(type) },
//                                onClick = {
//                                    selectedCertificateType = type
//                                    isCertDropdownExpanded = false
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//
//            // ---------- Total Questions ----------
//            item {
//                InfoCard(
//                    icon = Icons.Default.Assignment,
//                    label = "Total Questions",
//                    value = totalQuestions.toString()
//                )
//            }
//
//            // ---------- Time Duration ----------
//            item {
//                InfoCard(
//                    icon = Icons.Default.AccessTime,
//                    label = "Time Duration",
//                    value = timeDuration
//                )
//            }
//
//            // ---------- Department Selected ----------
//            // Agar categoryList single hai -> sirf value dikhao (no dropdown)
//            // Agar categoryList multiple hai -> dropdown dikhao select karne ke liye
//            item {
//                if (categoryList.size <= 1) {
//
//                    InfoCard(
//                        icon = Icons.Default.LocalFireDepartment,
//                        label = "Department Selected",
//                        value = if (categoryList.isNotEmpty()) categoryList[0] else "Not Available"
//                    )
//
//                } else {
//
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(14.dp),
//                        colors = CardDefaults.cardColors(
//                            containerColor = Color(0xFFF3F4F8)
//                        ),
//                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
//                    ) {
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp, vertical = 12.dp)
//                        ) {
//                            Text(
//                                text = "Department Selected",
//                                fontSize = 14.sp,
//                                color = Color.Gray
//                            )
//
//                            Spacer(modifier = Modifier.height(6.dp))
//
//                            ExposedDropdownMenuBox(
//                                expanded = isDeptDropdownExpanded,
//                                onExpandedChange = { isDeptDropdownExpanded = !isDeptDropdownExpanded }
//                            ) {
//                                OutlinedTextField(
//                                    value = selectedDepartment.ifEmpty { "Select Department" },
//                                    onValueChange = {},
//                                    readOnly = true,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .menuAnchor(),
//                                    textStyle = LocalTextStyle.current.copy(
//                                        fontWeight = FontWeight.Bold,
//                                        fontSize = 16.sp
//                                    ),
//                                    trailingIcon = {
//                                        Icon(
//                                            imageVector = Icons.Default.ArrowDropDown,
//                                            contentDescription = null
//                                        )
//                                    },
//                                    colors = OutlinedTextFieldDefaults.colors(
//                                        unfocusedTextColor = if (selectedDepartment.isEmpty()) Color.Gray else Color.Black,
//                                        focusedTextColor = Color.Black,
//                                        unfocusedBorderColor = Color.LightGray,
//                                        focusedBorderColor = Color(0xFF2962FF)
//                                    )
//                                )
//
//                                ExposedDropdownMenu(
//                                    expanded = isDeptDropdownExpanded,
//                                    onDismissRequest = { isDeptDropdownExpanded = false }
//                                ) {
//                                    categoryList.forEach { dept ->
//                                        DropdownMenuItem(
//                                            text = { Text(dept) },
//                                            onClick = {
//                                                selectedDepartment = dept
//                                                isDeptDropdownExpanded = false
//                                            }
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            // ---------- Mark per Question ----------
//            item {
//                InfoCard(
//                    icon = Icons.Default.LocalFireDepartment,
//                    label = "Mark per Question",
//                    value = markPerQuestion.toString()
//                )
//            }
//
//            // ---------- Acknowledgment Checkbox ----------
//            item {
//                Row(
//                    verticalAlignment = Alignment.Top,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Checkbox(
//                        checked = isAcknowledged,
//                        onCheckedChange = { isAcknowledged = it },
//                        colors = CheckboxDefaults.colors(
//                            checkedColor = Color(0xFF2962FF)
//                        )
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = "I acknowledge that my identity will be verified through facial recognition using eye-blink detection before the examination begins. For security and examination integrity purposes, the camera may remain active throughout the test, and my face may be verified periodically until the examination is completed. If face verification fails or an unauthorized person is detected, the examination may be suspended or terminated.",
//                        fontSize = 13.sp,
//                        color = Color.DarkGray,
//                        lineHeight = 18.sp,
//                        modifier = Modifier.padding(top = 12.dp)
//                    )
//                }
//            }
//
//            // ---------- Start New Test Button ----------
//            item {
//                Spacer(modifier = Modifier.height(8.dp))
//                Button(
//                    onClick = {
//                        if (selectedCertificateType.isEmpty()) {
//                            Toast.makeText(
//                                navController.context,
//                                "Please select certificate type",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else if (categoryList.size > 1 && selectedDepartment.isEmpty()) {
//                            Toast.makeText(
//                                navController.context,
//                                "Please select department",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else if (!isAcknowledged) {
//                            Toast.makeText(
//                                navController.context,
//                                "Please acknowledge the terms",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else {
//                            // TODO: navigate / start test
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF2962FF)
//                    )
//                ) {
//                    Text(
//                        text = "Start New Test",
//                        color = Color.White,
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }
//    }
//
//    @Composable
//    fun InfoCard(
//        icon: ImageVector,
//        label: String,
//        value: String
//    ) {
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(14.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = Color(0xFFF3F4F8)
//            ),
//            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp, vertical = 14.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = icon,
//                    contentDescription = null,
//                    tint = Color(0xFF2962FF),
//                    modifier = Modifier.size(28.dp)
//                )
//                Spacer(modifier = Modifier.width(16.dp))
//                Column {
//                    Text(
//                        text = label,
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//                    Text(
//                        text = value,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.Black
//                    )
//                }
//            }
//        }
//    }
//}

