package com.deendayalproject.esop.result
import SharedViewModel
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.network.SecurePreferenceManager.getToken
// TODO: fix this import to wherever your AppPreferences class actually lives
//import com.example.esop.utils.AppPreferences


import com.deendayalproject.databinding.EsopWrongquestionsscreenFragmentBinding



class EsopGetWrongQuestionsScreenFragment :
    BaseFragment<EsopWrongquestionsscreenFragmentBinding>(
        bindingInflater = EsopWrongquestionsscreenFragmentBinding::inflate
    ) {

    private val viewModel: SharedViewModel by viewModels()
    private lateinit var navController: NavController

    // Candidate info
    private var candidateLoginId by mutableStateOf("")
    private var numberofAttempt by mutableStateOf("")
    private var departmentCetegory by mutableStateOf("")
    private var certificateType by mutableStateOf("")
    private var candidateLoginEmail by mutableStateOf("")

    // API result state
    private var ESOPResultList by mutableStateOf<List<ResultItem>>(emptyList())
    private var isLoading by mutableStateOf(true)
    private var errorMessage by mutableStateOf<String?>(null)
    private var showEmptyDialog by mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.R)
    override fun initializeViews() {

        navController = findNavController()
//
        candidateLoginEmail = arguments?.getString("candidateLoginEmail").orEmpty()
        candidateLoginId = arguments?.getString("candidateLoginId").orEmpty()
        numberofAttempt = arguments?.getString("numberofAttempt").orEmpty()
        departmentCetegory = arguments?.getString("departmentCetegory").orEmpty()
        certificateType = arguments?.getString("certificateType").orEmpty()


        hideStatusBar()

        val token = getToken(requireContext())

        val request = GetResultViewRequest(
            BuildConfig.VERSION_NAME,
            candidateLoginId,
            numberofAttempt,
            certificateType,
            departmentCetegory
        )

        showProgressDialog("Loading...")

        viewModel.getResultView(
            request,
            "Bearer $token"
        )

        viewModel.getResultView.observe(viewLifecycleOwner) { response ->

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

        binding.composeESOPwrongQuestion.setContent {
            WrongQuestionsScreen(
                resultList = ESOPResultList,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onBackClick = { navController.popBackStack() }
            )
        }
    }

    override fun setupObservers() {}
    override fun setupClickListeners() {}
    override fun loadInitialData() {}

    // ---------------------------------------------------------
    // ---------------- COMPOSABLE UI BELOW ---------------------
    // ---------------------------------------------------------

    @Composable
    private fun WrongQuestionsScreen(
        resultList: List<ResultItem>,
        isLoading: Boolean,
        errorMessage: String?,
        onBackClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // ---------- Header ----------
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5EEFB)) // light lavender bg
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { onBackClick() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Wrong Questions",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // ---------- Body ----------
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
                        Text(text = errorMessage, color = Color.Red, fontSize = 16.sp)
                    }
                }

                resultList.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No wrong questions found", fontSize = 16.sp, color = Color.Gray)
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(resultList) { index, item ->
                            QuestionCard(index = index + 1, item = item)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun QuestionCard(index: Int, item: ResultItem) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Question title
                Text(
                    text = "Q$index. ${item.question_title.orEmpty()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

//                Spacer(modifier = Modifier.height(12.dp))
//                AnswerBox(
//                    label = "Your Give Options",
//                    answer = item.answer_given?.takeIf { it.isNotBlank() } ?: "NA",
//                    labelColor = Color(0xFFB3261E),
//                    borderColor = Color(0xFFB3261E),
//                    backgroundColor = Color(0xFFFDECEA)
//                )

                Spacer(modifier = Modifier.height(12.dp))
                // Your Answer box (red)
                AnswerBox(
                    label = "Your Answer",
                    answer = "${item.answer_given ?: " "} ${item.AnswerGiventext?.takeIf { it.isNotBlank() } ?: ""}".trim()
                        .ifEmpty { "NA" },
                    labelColor = Color(0xFFB3261E),
                    borderColor = Color(0xFFB3261E),
                    backgroundColor = Color(0xFFFDECEA)
                )






                Spacer(modifier = Modifier.height(12.dp))

                // Correct Answer box (green)


                AnswerBox(
                    label = "Your Answer",
                    answer = "${item.correctAnswer ?: " "} ${item.correctAnswertext?.takeIf { it.isNotBlank() } ?: ""}".trim()
                        .ifEmpty { "NA" },
                    labelColor = Color(0xFF2E7D32),
                    borderColor = Color(0xFF2E7D32),
                    backgroundColor = Color(0xFFFDECEA)
                )






            }
        }
    }

    @Composable
    private fun AnswerBox(
        label: String,
        answer: String,
        labelColor: Color,
        borderColor: Color,
        backgroundColor: Color
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = labelColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = answer,
                fontSize = 15.sp,
                color = Color.Black
            )
        }
    }
}
//class EsopGetWrongQuestionsScreenFragment :
//    BaseFragment<EsopWrongquestionsscreenFragmentBinding>(
//        bindingInflater = EsopWrongquestionsscreenFragmentBinding::inflate
//    ) {
//
//    private val viewModel: SharedViewModel by viewModels()
//    private lateinit var navController: NavController
//
//    // Candidate info
//    private var candidateLoginId by mutableStateOf("")
//    private var numberofAttempt by mutableStateOf("")
//    private var departmentCetegory by mutableStateOf("")
//    private var certificateType by mutableStateOf("")
//    private var candidateLoginEmail by mutableStateOf("")
//
//    // API result state
//    private var ESOPResultList by mutableStateOf<List<ResultItem>>(emptyList())
//    private var isLoading by mutableStateOf(true)
//    private var errorMessage by mutableStateOf<String?>(null)
//    private var showEmptyDialog by mutableStateOf(false)
//
//    @OptIn(ExperimentalMaterial3Api::class)
//    @RequiresApi(Build.VERSION_CODES.R)
//    override fun initializeViews() {
//
//        navController = findNavController()
//
//        candidateLoginEmail = arguments?.getString("candidateLoginEmail").orEmpty()
//        candidateLoginId = arguments?.getString("candidateLoginId").orEmpty()
//        numberofAttempt = arguments?.getString("numberofAttempt").orEmpty()
//        departmentCetegory = arguments?.getString("departmentCetegory").orEmpty()
//        certificateType = arguments?.getString("certificateType").orEmpty()
//
//
//
//
//        Log.d("TAG", candidateLoginEmail)
//        Log.d("TAG", candidateLoginId)
//
//        hideStatusBar()
//
//
//        val token = getToken(requireContext())
//
//        val request = GetResultViewRequest(
//            BuildConfig.VERSION_NAME,
//            "MoRD6264180120",
//            "0",
//            "Master",
//            "Operations"
//        )
//
//        showProgressDialog("Loading...")
//
//        viewModel.getResultView(
//            request,
//            "Bearer $token"
//        )
//
//        viewModel.getResultView.observe(viewLifecycleOwner) { response ->
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
//        binding.composeESOPwrongQuestion.setContent {
////            ResultScreen()
//        }
//    }
//
//
//    override fun setupObservers() {}
//    override fun setupClickListeners() {}
//    override fun loadInitialData() {}
//}
