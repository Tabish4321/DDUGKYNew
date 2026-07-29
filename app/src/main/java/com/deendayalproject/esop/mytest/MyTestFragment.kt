package com.deendayalproject.esop.mytest





import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController


import SharedViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.EsopMytestFragmentBinding
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.model.request.EsopCandidateRequest
import com.deendayalproject.model.request.EsopResultRequest
import com.deendayalproject.model.response.EsopCandidateRes
import com.deendayalproject.network.SecurePreferenceManager.getToken
import com.deendayalproject.util.AppUtil
import com.example.esop.mytest.MyTestItem
import kotlin.getValue


import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

// ─────────────────────────────────────────────
// Colors
// ─────────────────────────────────────────────

// ─────────────────────────────────────────────
// Colors
// ─────────────────────────────────────────────
private val BackgroundPage = Color(0xFFF5F5F5)
private val CardWhite = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF1A1A2E)
private val TextMuted = Color(0xFF888888)
private val DividerColor = Color(0xFFF0F0F0)
private val GreenText = Color(0xFF1A9650)
private val GreenBg = Color(0xFFEAFAF1)
private val RedText = Color(0xFFC0392B)
private val RedBg = Color(0xFFFDECEA)
private val BlueLight = Color(0xFFE8F4FD)
private val GreenLight = Color(0xFFEAFAF1)

// ─────────────────────────────────────────────
// Fragment
// ─────────────────────────────────────────────
class MyTestFragment :
    BaseFragment<EsopMytestFragmentBinding>(
        bindingInflater = EsopMytestFragmentBinding::inflate
    ) {

    private val viewModel: SharedViewModel by viewModels()

    // Candidate info
    private var candidateLoginId by mutableStateOf("")
    private var candidateLoginEmail by mutableStateOf("")

    // API result state
    private var ESOPResultList by mutableStateOf<List<MyTestItem>>(emptyList())
    private var isLoading by mutableStateOf(true)
    private var errorMessage by mutableStateOf<String?>(null)

    // UI state (Fragment-level, NOT remember — Fragment isn't a @Composable)
    private var expanded by mutableStateOf(false)
//    private var selectedDepartment by mutableStateOf("Finance")
    private var selectedDepartment by mutableStateOf("-----Select-----")
    private var showEmptyDialog by mutableStateOf(false)

    private val departments = listOf("-----Select-----","Finance", "Operation","PAA")

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.R)
    override fun initializeViews() {

        candidateLoginEmail = arguments?.getString("candidateLoginEmail").orEmpty()
        candidateLoginId = arguments?.getString("candidateLoginId").orEmpty()
        hideStatusBar()

        val token = getToken(requireContext())

        val request = EsopResultRequest(
            BuildConfig.VERSION_NAME,
            candidateLoginId,
            candidateLoginEmail
        )

        showProgressDialog("Loading...")

        viewModel.getresultAll(
            request,
            "Bearer $token"
        )

        viewModel.getresultAll.observe(viewLifecycleOwner) { response ->

            dismissProgressDialog()
            isLoading = false

            response.onSuccess { data ->

                Log.d("GET_RESULT", "Response = $data")

                if (data.responseCode != 200) {
                    errorMessage = data.responseDesc
                    return@onSuccess
                }

                ESOPResultList = data.wrappedList
                showEmptyDialog = ESOPResultList.isEmpty()
                errorMessage = null
            }

            response.onFailure { error ->
                isLoading = false
                errorMessage = error.localizedMessage ?: "Something went wrong"
            }
        }

        binding.composeESOPMytest.setContent {

            if (showEmptyDialog) {
                AlertDialog(
                    onDismissRequest = { showEmptyDialog = false },
                    title = { Text(stringResource(R.string.no_result_available)) },
                    text = { Text(stringResource(R.string.please_conduct_the_exam_first)) },
                    confirmButton = {
                        TextButton(onClick = {
                            showEmptyDialog = false
                            findNavController().popBackStack()
                        }) {
                            Text(stringResource(R.string.ok))
                        }
                    }
                )
            }

            Scaffold(
                topBar = {
                    PremiumTopBar(
                        dynamicTitle = stringResource(R.string.my_test),
                        onBackClick = {
                            findNavController().popBackStack()
                        }
                    )
                },
                containerColor = BackgroundPage
            ) { padding ->

                when {

                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    errorMessage != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = errorMessage!!,
                                color = RedText
                            )
                        }
                    }

                    else -> {

                        // NOTE: departmentCetegory can be null in the API response,
                        // so filtering by it will hide everything until backend sends it.
                        // Change to `ESOPResultList` directly if you want to show all rows for now.
                        val filteredList = ESOPResultList.filter {
                            it.departmentCetegory?.equals(selectedDepartment, ignoreCase = true) == true
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {

                            // ── Dropdown ──────────────────────────
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedDepartment,
                                    onValueChange = {},
                                    readOnly = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                        focusedBorderColor = Color.Gray,
                                        unfocusedBorderColor = Color.LightGray
                                    ),
                                    label = {
                                        Text(text = stringResource(R.string.select_department), color = Color.Black)
                                    },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp, top = 10.dp)
                                        .menuAnchor()
                                )

                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    departments.forEach { department ->
                                        DropdownMenuItem(
                                            text = { Text(text = department, color = Color.Black) },
                                            onClick = {
                                                selectedDepartment = department
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            // ── Cards ─────────────────────────────
                            if (filteredList.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(
                                        horizontal = 16.dp,
                                        vertical = 4.dp
                                    ),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    items(
                                        items = filteredList,
                                        key = { filteredList.indexOf(it).toString() }
                                    ) { item ->
                                        MyTestCard(item)
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = stringResource(R.string.no_result_found),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            text = stringResource(
                                                R.string.no_data_available_for_department,
                                                selectedDepartment
                                            ),
                                            fontSize = 13.sp,
                                            color = TextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun setupObservers() {}
    override fun setupClickListeners() {}
    override fun loadInitialData() {}
}

// ─────────────────────────────────────────────
// Card
// ─────────────────────────────────────────────
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyTestCard(item: MyTestItem) {

    val department = item.departmentCetegory ?: "Unknown"
    // 👇 NAYA — API response se Certificate Type nikal rahe hain (dynamic)
    // ⚠️ Field ka naam apne MyTestItem data class ke hisaab se confirm/adjust kar lein
    // (agar API mein alag naam ho jaise "certificationType", to yahan wahi use karein)
    val certificateType = item.certificateType ?: "Unknown"
    val resultDate = item.resultdate ?: ""
    val totalQ = item.totalQuestion ?: "0"
    val correctA = item.correctAns ?: "0"
    val wrongA = item.wrongAns ?: "0"
    val scoredPer = item.scoredPercentage ?: "0"
    val passingPer = item.passingPercentage ?: "0"
    val finalRes = item.finalResult ?: ""

    val isPassed = finalRes.equals("1", ignoreCase = true) ||
            finalRes.equals("pass", ignoreCase = true)

    val scoreInt = scoredPer.toIntOrNull() ?: 0
    val passInt = passingPer.toIntOrNull() ?: 0

    val deptIconBg = if (department.equals("Finance", ignoreCase = true))
        BlueLight else GreenLight

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            // ── Header ────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(deptIconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (department.equals("Finance", ignoreCase = true)) "💼" else "⚙️",
                        fontSize = 20.sp
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = department,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                    if (resultDate.isNotEmpty()) {
                        Text(
                            text = formatDateTime(resultDate),
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }

                // Pass/Fail badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isPassed) GreenBg else RedBg)
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (isPassed) "PASS" else "FAIL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isPassed) GreenText else RedText
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 14.dp),
                thickness = 1.dp,
                color = DividerColor
            )

            // ── Data rows ─────────────────────────────
            // 👇 NAYA — Certificate Type row add ki, sabse pehle (dynamic value)
            ResultRow(stringResource(R.string.certificate_type), certificateType)
            ResultRow(stringResource(R.string.total_questions), totalQ)
            ResultRow(stringResource(R.string.correct_answers), correctA, valueColor = GreenText)
            ResultRow(stringResource(R.string.wrong_answers), wrongA, valueColor = RedText)
            ResultRow(stringResource(R.string.passing), "$passingPer%")

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                thickness = 1.dp,
                color = DividerColor
            )

            // ── Score + Progress bar ──────────────────
            ResultRow(
                title = stringResource(R.string.score),
                value = "$scoreInt%",
                valueColor = if (scoreInt >= passInt) GreenText else RedText
            )

            Spacer(Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { scoreInt / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = if (scoreInt >= passInt) GreenText else RedText,
                trackColor = DividerColor
            )
        }
    }
}

@Composable
fun ResultRow(
    title: String,
    value: String,
    valueColor: Color = TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            color = TextMuted
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }}


fun formatDateTime(rawDate: String): String {

    if (rawDate.isBlank()) return ""

    // Formats to try, in order — covers the "+00:00" offset style and
    // the more common "Z" / no-millis variants some backends send.
    val inputFormats = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'"
    )

    for (pattern in inputFormats) {
        try {
            val parser = SimpleDateFormat(pattern, Locale.getDefault())
            parser.timeZone = TimeZone.getTimeZone("UTC")

            val date = parser.parse(rawDate) ?: continue

            val outputFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getDefault() // show in device's local time

            return outputFormat.format(date)

        } catch (e: Exception) {
            // try next pattern
        }
    }

    // Nothing matched — return the raw string instead of crashing/blank
    return rawDate
}



































//private val BackgroundPage = Color(0xFFF5F5F5)
//private val CardWhite = Color(0xFFFFFFFF)
//private val TextPrimary = Color(0xFF1A1A2E)
//private val TextMuted = Color(0xFF888888)
//private val DividerColor = Color(0xFFF0F0F0)
//private val GreenText = Color(0xFF1A9650)
//private val GreenBg = Color(0xFFEAFAF1)
//private val RedText = Color(0xFFC0392B)
//private val RedBg = Color(0xFFFDECEA)
//private val BlueLight = Color(0xFFE8F4FD)
//private val GreenLight = Color(0xFFEAFAF1)
//
//// ─────────────────────────────────────────────
//// Fragment
//// ─────────────────────────────────────────────
//class MyTestFragment :
//    BaseFragment<EsopMytestFragmentBinding>(
//        bindingInflater = EsopMytestFragmentBinding::inflate
//    ) {
//
//    private val viewModel: SharedViewModel by viewModels()
//
//    // Candidate info
//    private var candidateLoginId by mutableStateOf("")
//    private var candidateLoginEmail by mutableStateOf("")
//
//    // API result state
//    private var ESOPResultList by mutableStateOf<List<MyTestItem>>(emptyList())
//    private var isLoading by mutableStateOf(true)
//    private var errorMessage by mutableStateOf<String?>(null)
//
//    // UI state (Fragment-level, NOT remember — Fragment isn't a @Composable)
//    private var expanded by mutableStateOf(false)
//    private var selectedDepartment by mutableStateOf("Finance")
//    private var showEmptyDialog by mutableStateOf(false)
//
//    private val departments = listOf("Finance", "Operation","PAA")
//
//    @OptIn(ExperimentalMaterial3Api::class)
//    @RequiresApi(Build.VERSION_CODES.R)
//    override fun initializeViews() {
//
//        candidateLoginEmail = arguments?.getString("candidateLoginEmail").orEmpty()
//        candidateLoginId = arguments?.getString("candidateLoginId").orEmpty()
//
//        Log.d("TAG", candidateLoginEmail)
//        Log.d("TAG", candidateLoginId)
//
//        hideStatusBar()
//
//        val token = getToken(requireContext())
//
//        val request = EsopResultRequest(
//            BuildConfig.VERSION_NAME,
//            candidateLoginId,
//            candidateLoginEmail
//        )
//
//        showProgressDialog("Loading...")
//
//        viewModel.getresultAll(
//            request,
//            "Bearer $token"
//        )
//
//        viewModel.getresultAll.observe(viewLifecycleOwner) { response ->
//
//            dismissProgressDialog()
//            isLoading = false
//
//            response.onSuccess { data ->
//
//                Log.d("GET_RESULT", "Response = $data")
//
//                if (data.responseCode != 200) {
//                    errorMessage = data.responseDesc
//                    return@onSuccess
//                }
//
//                ESOPResultList = data.wrappedList
//                showEmptyDialog = ESOPResultList.isEmpty()
//                errorMessage = null
//            }
//
//            response.onFailure { error ->
//                isLoading = false
//                errorMessage = error.localizedMessage ?: "Something went wrong"
//            }
//        }
//
//        binding.composeESOPMytest.setContent {
//
//            if (showEmptyDialog) {
//                AlertDialog(
//                    onDismissRequest = { showEmptyDialog = false },
//                    title = { Text("No Result Available") },
//                    text = { Text("Please conduct the exam first.") },
//                    confirmButton = {
//                        TextButton(onClick = {
//                            showEmptyDialog = false
//                            findNavController().popBackStack()
//                        }) {
//                            Text("OK")
//                        }
//                    }
//                )
//            }
//
//            Scaffold(
//                topBar = {
//                    PremiumTopBar(
//                        dynamicTitle = stringResource(R.string.my_test),
//                        onBackClick = {
//                            findNavController().popBackStack()
//                        }
//                    )
//                },
//                containerColor = BackgroundPage
//            ) { padding ->
//
//                when {
//
//                    isLoading -> {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            CircularProgressIndicator()
//                        }
//                    }
//
//                    errorMessage != null -> {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = errorMessage!!,
//                                color = RedText
//                            )
//                        }
//                    }
//
//                    else -> {
//
//                        // NOTE: departmentCetegory can be null in the API response,
//                        // so filtering by it will hide everything until backend sends it.
//                        // Change to `ESOPResultList` directly if you want to show all rows for now.
//                        val filteredList = ESOPResultList.filter {
//                            it.departmentCetegory?.equals(selectedDepartment, ignoreCase = true) == true
//                        }
//
//                        Column(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(padding)
//                        ) {
//
//                            // ── Dropdown ──────────────────────────
//                            ExposedDropdownMenuBox(
//                                expanded = expanded,
//                                onExpandedChange = { expanded = !expanded }
//                            ) {
//                                OutlinedTextField(
//                                    value = selectedDepartment,
//                                    onValueChange = {},
//                                    readOnly = true,
//                                    colors = OutlinedTextFieldDefaults.colors(
//                                        focusedContainerColor = Color.White,
//                                        unfocusedContainerColor = Color.White,
//                                        focusedTextColor = Color.Black,
//                                        unfocusedTextColor = Color.Black,
//                                        focusedBorderColor = Color.Gray,
//                                        unfocusedBorderColor = Color.LightGray
//                                    ),
//                                    label = {
//                                        Text(text = "Select Department", color = Color.Black)
//                                    },
//                                    trailingIcon = {
//                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
//                                    },
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(start = 16.dp, end = 16.dp, top = 10.dp)
//                                        .menuAnchor()
//                                )
//
//                                ExposedDropdownMenu(
//                                    expanded = expanded,
//                                    onDismissRequest = { expanded = false }
//                                ) {
//                                    departments.forEach { department ->
//                                        DropdownMenuItem(
//                                            text = { Text(text = department, color = Color.Black) },
//                                            onClick = {
//                                                selectedDepartment = department
//                                                expanded = false
//                                            }
//                                        )
//                                    }
//                                }
//                            }
//
//                            Spacer(Modifier.height(12.dp))
//
//                            // ── Cards ─────────────────────────────
//                            if (filteredList.isNotEmpty()) {
//                                LazyColumn(
//                                    modifier = Modifier.fillMaxSize(),
//                                    contentPadding = PaddingValues(
//                                        horizontal = 16.dp,
//                                        vertical = 4.dp
//                                    ),
//                                    verticalArrangement = Arrangement.spacedBy(14.dp)
//                                ) {
//                                    items(
//                                        items = filteredList,
//                                        key = { filteredList.indexOf(it).toString() }
//                                    ) { item ->
//                                        MyTestCard(item)
//                                    }
//                                }
//                            } else {
//                                Box(
//                                    modifier = Modifier.fillMaxSize(),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Column(
//                                        horizontalAlignment = Alignment.CenterHorizontally
//                                    ) {
//                                        Text(
//                                            text = "No Result Found",
//                                            fontSize = 16.sp,
//                                            fontWeight = FontWeight.Bold,
//                                            color = TextPrimary
//                                        )
//                                        Spacer(Modifier.height(6.dp))
//                                        Text(
//                                            text = "No data available for $selectedDepartment department",
//                                            fontSize = 13.sp,
//                                            color = TextMuted
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    override fun setupObservers() {}
//    override fun setupClickListeners() {}
//    override fun loadInitialData() {}
//}
//
//// ─────────────────────────────────────────────
//// Card
//// ─────────────────────────────────────────────
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun MyTestCard(item: MyTestItem) {
//
//    val department = item.departmentCetegory ?: "Unknown"
//    val resultDate = item.resultdate ?: ""
//    val totalQ = item.totalQuestion ?: "0"
//    val correctA = item.correctAns ?: "0"
//    val wrongA = item.wrongAns ?: "0"
//    val scoredPer = item.scoredPercentage ?: "0"
//    val passingPer = item.passingPercentage ?: "0"
//    val finalRes = item.finalResult ?: ""
//
//    val isPassed = finalRes.equals("1", ignoreCase = true) ||
//            finalRes.equals("pass", ignoreCase = true)
//
//    val scoreInt = scoredPer.toIntOrNull() ?: 0
//    val passInt = passingPer.toIntOrNull() ?: 0
//
//    val deptIconBg = if (department.equals("Finance", ignoreCase = true))
//        BlueLight else GreenLight
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(18.dp),
//        colors = CardDefaults.cardColors(containerColor = CardWhite),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Column(modifier = Modifier.padding(18.dp)) {
//
//            // ── Header ────────────────────────────────
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(44.dp)
//                        .clip(RoundedCornerShape(12.dp))
//                        .background(deptIconBg),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = if (department.equals("Finance", ignoreCase = true)) "💼" else "⚙️",
//                        fontSize = 20.sp
//                    )
//                }
//
//                Spacer(Modifier.width(12.dp))
//
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        text = department,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 16.sp,
//                        color = TextPrimary
//                    )
//                    if (resultDate.isNotEmpty()) {
//                        Text(
//                            text = formatDateTime(resultDate),
//                            fontSize = 12.sp,
//                            color = TextMuted
//                        )
//                    }
//                }
//
//                // Pass/Fail badge
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(20.dp))
//                        .background(if (isPassed) GreenBg else RedBg)
//                        .padding(horizontal = 14.dp, vertical = 5.dp)
//                ) {
//                    Text(
//                        text = if (isPassed) "PASS" else "FAIL",
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = if (isPassed) GreenText else RedText
//                    )
//                }
//            }
//
//            HorizontalDivider(
//                modifier = Modifier.padding(vertical = 14.dp),
//                thickness = 1.dp,
//                color = DividerColor
//            )
//
//            // ── Data rows ─────────────────────────────
//            ResultRow("Total Questions", totalQ)
//            ResultRow("Correct Answers", correctA, valueColor = GreenText)
//            ResultRow("Wrong Answers", wrongA, valueColor = RedText)
//            ResultRow("Passing %", "$passingPer%")
//
//            HorizontalDivider(
//                modifier = Modifier.padding(vertical = 10.dp),
//                thickness = 1.dp,
//                color = DividerColor
//            )
//
//            // ── Score + Progress bar ──────────────────
//            ResultRow(
//                title = "Score",
//                value = "$scoreInt%",
//                valueColor = if (scoreInt >= passInt) GreenText else RedText
//            )
//
//            Spacer(Modifier.height(8.dp))
//
//            LinearProgressIndicator(
//                progress = { scoreInt / 100f },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(8.dp)
//                    .clip(RoundedCornerShape(6.dp)),
//                color = if (scoreInt >= passInt) GreenText else RedText,
//                trackColor = DividerColor
//            )
//        }
//    }
//}
//
//@Composable
//fun ResultRow(
//    title: String,
//    value: String,
//    valueColor: Color = TextPrimary
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 5.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(
//            text = title,
//            fontSize = 13.sp,
//            color = TextMuted
//        )
//        Text(
//            text = value,
//            fontSize = 13.sp,
//            fontWeight = FontWeight.Bold,
//            color = valueColor
//        )
//    }}
//
//
//fun formatDateTime(rawDate: String): String {
//
//    if (rawDate.isBlank()) return ""
//
//    // Formats to try, in order — covers the "+00:00" offset style and
//    // the more common "Z" / no-millis variants some backends send.
//    val inputFormats = listOf(
//        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
//        "yyyy-MM-dd'T'HH:mm:ssXXX",
//        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
//        "yyyy-MM-dd'T'HH:mm:ss'Z'"
//    )
//
//    for (pattern in inputFormats) {
//        try {
//            val parser = SimpleDateFormat(pattern, Locale.getDefault())
//            parser.timeZone = TimeZone.getTimeZone("UTC")
//
//            val date = parser.parse(rawDate) ?: continue
//
//            val outputFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
//            outputFormat.timeZone = TimeZone.getDefault() // show in device's local time
//
//            return outputFormat.format(date)
//
//        } catch (e: Exception) {
//            // try next pattern
//        }
//    }
//
//    // Nothing matched — return the raw string instead of crashing/blank
//    return rawDate
//}




