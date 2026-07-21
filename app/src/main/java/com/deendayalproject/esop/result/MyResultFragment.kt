package com.deendayalproject.esop.result





import SharedViewModel
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.EsopResultFragmentBinding
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.model.request.EsopResultRequest
import com.deendayalproject.network.SecurePreferenceManager.getToken
import com.example.esop.mytest.MyTestItem
// TODO: fix this import to wherever your AppPreferences class actually lives
//import com.example.esop.utils.AppPreferences
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone




import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig

//import com.example.esop.databinding.EsopResultFragmentBinding

// ─────────────────────────────────────────────
// Colors (file-scoped, won't clash with other files' private vals)
// ─────────────────────────────────────────────
private val GradientTop = Color(0xFF062C63)
private val GradientBottom = Color(0xFF031A3F)
private val ProgressTrack = Color(0xFF1BB2AA)
private val ProgressFill = Color(0xFF95DD31)

class MyResultFragment :
    BaseFragment<EsopResultFragmentBinding>(
        bindingInflater = EsopResultFragmentBinding::inflate
    ) {

    private val viewModel: SharedViewModel by viewModels()
    private lateinit var navController: NavController


    // Candidate info
    private var candidateLoginId by mutableStateOf("")
    private var candidateLoginEmail by mutableStateOf("")
    private var candidateName by mutableStateOf("")
    private var candidateMobileNo by mutableStateOf("")




    // API result state
    private var ESOPResultList by mutableStateOf<List<MyTestItem>>(emptyList())
    private var isLoading by mutableStateOf(true)
    private var errorMessage by mutableStateOf<String?>(null)
    private var showEmptyDialog by mutableStateOf(false)
    private var numberofAttempt by mutableStateOf("")
    private var departmentCetegorys by mutableStateOf("")
    private var certificateTypes by mutableStateOf("")

    // UI state (Fragment-level, NOT remember — Fragment isn't a @Composable)
    private var expanded by mutableStateOf(false)
    private var expandedCertificate by mutableStateOf(false)
    private var selectedDepartment by mutableStateOf("Select")
    private var selectedCertificate by mutableStateOf("Select")

    private val departments = listOf("Select", "Finance", "Operation","PAA")

    private val certificateTypeOf = listOf("Select", "Master", "Professional")

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.R)
    override fun initializeViews() {

        navController = findNavController()

        candidateLoginEmail = arguments?.getString("candidateLoginEmail").orEmpty()
        candidateLoginId = arguments?.getString("candidateLoginId").orEmpty()


        candidateName = arguments?.getString("candidateName").orEmpty()
        candidateMobileNo = arguments?.getString("candidateMobileNo").orEmpty()



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

                // pehle item se values set karna (agar list me single result expect ho)
                ESOPResultList.firstOrNull()?.let { item ->
                }

                errorMessage = null
            }
            response.onFailure { error ->
                isLoading = false
                errorMessage = error.localizedMessage ?: "Something went wrong"
            }
        }

        binding.composeESOPResult.setContent {
            ResultScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun ResultScreen() {

        if (showEmptyDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("No Result is Available") },
                text = { Text("Please Conduct Exam") },
                confirmButton = {
                    TextButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Text("OK")
                    }
                }
            )
        }

        val financeLast = remember(ESOPResultList, selectedCertificate) {
            ESOPResultList.filter {
                it.departmentCetegory.equals("Finance", ignoreCase = true) &&
                        it.certificateType.equals(selectedCertificate.trim(), ignoreCase = true)
            }.lastOrNull()
        }

        val operationLast = remember(ESOPResultList, selectedCertificate) {
            ESOPResultList.filter {
                it.departmentCetegory.equals("Operation", ignoreCase = true) &&
                        it.certificateType.equals(selectedCertificate.trim(), ignoreCase = true)
            }.lastOrNull()
        }

        val paaLast = remember(ESOPResultList, selectedCertificate) {
            ESOPResultList.filter {
                it.departmentCetegory.equals("PAA", ignoreCase = true) &&
                        it.certificateType.equals(selectedCertificate.trim(), ignoreCase = true)
            }.lastOrNull()
        }

        val filteredResult: MyTestItem? = when {
            selectedDepartment.equals("Finance", ignoreCase = true) -> financeLast
            selectedDepartment.equals("Operation", ignoreCase = true) -> operationLast
            selectedDepartment.equals("PAA", ignoreCase = true) -> paaLast
            else -> null
        }
//        val financeLast = remember(ESOPResultList, selectedCertificate) {
//            ESOPResultList.filter {
//                it.departmentCetegory.equals("Finance", ignoreCase = true) &&
//                        it.certificateType.equals(selectedCertificate.trim(), ignoreCase = true)
//            }.lastOrNull()
//        }
//
//        val operationLast = remember(ESOPResultList, selectedCertificate) {
//            ESOPResultList.filter {
//                it.departmentCetegory.equals("Operation", ignoreCase = true) &&
//                        it.certificateType.equals(selectedCertificate.trim(), ignoreCase = true)
//            }.lastOrNull()
//        }
//
//        val filteredResult: MyTestItem? = when {
//            selectedDepartment.equals("Finance", ignoreCase = true) -> financeLast
//            selectedDepartment.equals("Operation", ignoreCase = true) -> operationLast
//            else -> null
//        }

        val notattempteQuestionValue = when {
            selectedDepartment.equals("Finance", ignoreCase = true) -> financeLast?.notattempteQuestion.orEmpty()
            selectedDepartment.equals("Operation", ignoreCase = true) -> operationLast?.notattempteQuestion.orEmpty()
            else -> ""
        }

        val resultdate = filteredResult?.resultdate ?: ""
        val totalQuestions = filteredResult?.totalQuestion?.toIntOrNull() ?: 0
        val correctAns = filteredResult?.correctAns?.toIntOrNull() ?: 0
        val wrongAns = filteredResult?.wrongAns?.toIntOrNull() ?: 0
        val numberofAttempt = filteredResult?.numberofAttempt
        val id = filteredResult?.id
        val percentage = filteredResult?.scoredPercentage?.toIntOrNull() ?: 0
        val result = filteredResult?.finalResult?.toIntOrNull() ?: 0
        val notattempteQuestion = filteredResult?.notattempteQuestion
        val departmentCetegory = filteredResult?.departmentCetegory ?: selectedDepartment
        val certificateType = filteredResult?.certificateType ?: selectedCertificate

        val score = "$correctAns / $totalQuestions"
        val resultText = when {
            showEmptyDialog || filteredResult == null -> "Not Attempted"
            result == 0 -> "Failed"
            else -> "Passed"
        }
        val resultColor = if (result == 0) Color.Red else ProgressFill

        Scaffold(
            topBar = {
                PremiumTopBar(
                    dynamicTitle = "Result",
                    onBackClick = { navController.popBackStack() }
                )
            },
            containerColor = Color.White,
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 26.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (resultText == "Passed") {
                        Button(
                            onClick = {


//                                totalQuestions = totalQuestions,
//                                    wrongAns = wrongAns,
//                                    notAttempted = 0,
//                                    percentage = percentage,
//                                    correctAns = correctAns,
//                                    result = result



                                val bundle = bundleOf(
                                    "wrongAns" to wrongAns,
                                    "numberofAttempt" to numberofAttempt,
                                    "percentage" to percentage,
                                    "correctAns" to correctAns,
                                    "result" to result,
                                    "departmentCetegory" to departmentCetegory,
                                    "certificateType" to certificateType,
                                    "candidateName" to candidateName,
                                    "candidateMobileNo" to candidateMobileNo,
                                    "totalQuestions" to totalQuestions,
                                    "resultText" to resultText,
                                    "id" to id,
                                )
                               navController.navigate(R.id.action_esopFragment_to_CertificateScreenFragment,bundle)



                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF075CE8))
                        ) {
                            Text(
                                text = "View Certificate",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null && ESOPResultList.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = errorMessage!!, color = Color.Red)
                    }
                }

                else -> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .background(Color.White)
                    ) {

                        // ---------- Department Dropdown ----------
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
                                label = { Text(text = "Select Department", color = Color.Black) },
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

                        Spacer(modifier = Modifier.height(10.dp))

                        // ---------- Certificate Dropdown ----------
                        ExposedDropdownMenuBox(
                            expanded = expandedCertificate,
                            onExpandedChange = { expandedCertificate = !expandedCertificate }
                        ) {
                            OutlinedTextField(
                                value = selectedCertificate,
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
                                label = { Text(text = "Select Certificate", color = Color.Black) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCertificate)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, top = 10.dp)
                                    .menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expandedCertificate,
                                onDismissRequest = { expandedCertificate = false }
                            ) {
                                certificateTypeOf.forEach { certificate ->
                                    DropdownMenuItem(
                                        text = { Text(text = certificate, color = Color.Black) },
                                        onClick = {
                                            selectedCertificate = certificate
                                            expandedCertificate = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // ---------- Result Content ----------
                        Box(modifier = Modifier.fillMaxSize()) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(430.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(GradientTop, GradientBottom)
                                        )
                                    )
                            ) {
                                Text(
                                    text = "Your Result",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .padding(top = 20.dp)
                                )

                                Column(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(bottom = 60.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    ResultProgress(
                                        percentage = percentage,
                                        score = score,
                                        modifier = Modifier.size(160.dp)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (result == 0) "Sorry! You " else "Congratulations! You ",
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = resultText,
                                            color = resultColor,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (result != 0) {
                                            Text(text = " \uD83C\uDF89", color = Color.White, fontSize = 20.sp)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = if (result == 0)
                                            "Please try again to improve your score."
                                        else
                                            "Well done! You have successfully",
                                        color = Color.White,
                                        fontSize = 13.sp
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = formatDateTime(resultdate),
                                        color = Color.White,
                                        fontSize = 13.sp
                                    )

                                    if (result != 0) {
                                        Text(
                                            text = "cleared the test.",
                                            color = Color.White,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            ResultStatsCard(
                                correct = correctAns.toString(),
                                incorrect = wrongAns.toString(),
                                score = score,
                                rank = when {
                                    showEmptyDialog || filteredResult == null -> "NA"
                                    result == 0 -> "Fail"
                                    else -> "Pass"
                                },
                                onIncorrectClick = {
                                    // TODO: replace with your real nav-graph action id / route
                                    // Original intent:
                                    // navController.navigate("GetWrongQuestionScreen/$loginId/$notattempteQuestion/$certificateType/$departmentCetegory")


                                    val bundle = bundleOf(
                                        "numberofAttempt" to numberofAttempt,
                                        "candidateLoginEmail" to candidateLoginEmail,
                                           "candidateLoginId" to candidateLoginId,

                                           "departmentCetegory" to departmentCetegory,
                                           "certificateType" to certificateType
                                    )



                                    navController.navigate(R.id.action_esopFragment_to_EsopWrongFragment,bundle)




//                                    Toast.makeText(
//                                        requireContext(),
//                                        "Incorrect",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                    Log.d(
//                                        "WRONG_Q_NAV",
//                                        "loginId=$candidateLoginId, notAttempted=$notattempteQuestionValue, " +
//                                                "certificateType=$certificateType, department=$departmentCetegory"
//                                    )
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(start = 25.dp, end = 25.dp, bottom = 10.dp)
                            )
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
// Circular score ring
// ─────────────────────────────────────────────
@Composable
private fun ResultProgress(
    percentage: Int,
    score: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 14.dp.toPx()
            val startAngle = 140f
            val sweepAngle = 260f

            drawArc(
                color = ProgressTrack,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                color = ProgressFill,
                startAngle = startAngle,
                sweepAngle = sweepAngle * (percentage / 100f),
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$percentage%",
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = score,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ─────────────────────────────────────────────
// Bottom stats card
// ─────────────────────────────────────────────
@Composable
private fun ResultStatsCard(
    correct: String,
    incorrect: String,
    score: String,
    rank: String,
    onIncorrectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ResultStatItem(title = "Correct", value = correct, valueColor = Color(0xFF19A64A))
            ResultStatItem(
                title = "Incorrect",
                value = incorrect,
                valueColor = Color(0xFFE53935),
                onClick = onIncorrectClick
            )
            ResultStatItem(title = "Score", value = score, valueColor = Color(0xFF1D9BF0))
            ResultStatItem(title = "Result", value = rank, valueColor = Color(0xFF111827))
        }
    }
}

@Composable
private fun ResultStatItem(
    title: String,
    value: String,
    valueColor: Color,
    onClick: (() -> Unit)? = null
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = title,
            color = Color(0xFF4B5563),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            color = valueColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textDecoration = if (onClick != null) TextDecoration.Underline else TextDecoration.None,
            modifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier
        )
    }
}




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











//class MyResultFragment :
//    BaseFragment<EsopResultFragmentBinding>(
//        bindingInflater = EsopResultFragmentBinding::inflate
//    ) {
//    private val viewModel: SharedViewModel by viewModels()
//    private lateinit var navController: NavController
//
//    // Candidate info
//    private var candidateLoginId by mutableStateOf("")
//    private var selectedCertificate by mutableStateOf("")
//    private var candidateLoginEmail by mutableStateOf("")
//
//    // API result state
//    private var ESOPResultList by mutableStateOf<List<MyTestItem>>(emptyList())
//    private var isLoading by mutableStateOf(true)
//    private var errorMessage by mutableStateOf<String?>(null)
//    private var showEmptyDialog by mutableStateOf(false)
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
//            "2.6.9",
//            "MoRD6264180120",
//            "rahuldoc1993@gmail.com"
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
//
//                val selectedCertType = selectedCertificate.trim()
//            }
//
//            response.onFailure { error ->
//                isLoading = false
//                errorMessage = error.localizedMessage ?: "Something went wrong"
//            }
//        }
//
//        binding.composeESOPResult.setContent
//
//
//
//
//
//
//
//    }
//
//    override fun setupObservers() {}
//    override fun setupClickListeners() {}
//    override fun loadInitialData() {}
//}

