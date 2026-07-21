package com.deendayalproject.esop.exam

import SharedViewModel
import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
//import androidx.camera.core.ImageCapture
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
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
import com.deendayalproject.databinding.EsopTestscreenBinding
import com.deendayalproject.esop.dashboard.DashboardSection
import com.deendayalproject.esop.profile.CandidateInfoCard
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.model.request.EsopCandidateRequest
import com.deendayalproject.model.response.EsopCandidateRes
import com.deendayalproject.network.SecurePreferenceManager.getToken
import com.deendayalproject.util.AppUtil
import com.example.esop.AswersOptionSubmit.SubmitAnswer
import com.example.esop.AswersOptionSubmit.SubmitExamRequest
import com.example.esop.quetions_esop.Question
import com.example.esop.quetions_esop.QuestiontReq
import com.example.esop.quetions_esop.SummaryCard
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlin.collections.set
import kotlin.compareTo
import kotlin.getValue
import kotlin.hashCode

@AndroidEntryPoint
class TestScreenFragment :
    BaseFragment<EsopTestscreenBinding>(
        bindingInflater = EsopTestscreenBinding::inflate
    ) {






    private val viewModel: SharedViewModel by viewModels()

    private lateinit var navController: NavController
    private var candidateMobileNo by mutableStateOf("")
    private var loginId by mutableStateOf("")
    private var userName by mutableStateOf("")
    private var mobile by mutableStateOf("")
    private var email by mutableStateOf("")
    private var gender by mutableStateOf("")
    private var selectedCertificateType by mutableStateOf("")
    private var selectedDepartment by mutableStateOf("")
    var submitRequestJson = ""

    private var showReviewScreen = false

    private var questionList: List<Question> = emptyList()
    private var showDialogTime = mutableStateOf(false)
    private val showQuestionPalette = mutableStateOf(false)
    private var showDialog = mutableStateOf(false)

    private val reviewQuestions = mutableStateListOf<Int>()
    private val markedQuestions = mutableStateListOf<Int>()
    private val answeredQuestions = mutableStateMapOf<Int, String>()
    private var totalQuestions by mutableIntStateOf(0)
    private var easyCount by mutableIntStateOf(0)
    private var mediumCount by mutableIntStateOf(0)
    private var hardCount by mutableIntStateOf(0)
    private var numberofAttempt by mutableIntStateOf(0)
    private var easyPercentage by mutableDoubleStateOf(0.0)
    private var mediumPercentage by mutableDoubleStateOf(0.0)
    private var hardPercentage by mutableDoubleStateOf(0.0)

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showExitExamDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            backPressedCallback
        )
    }



    override fun onResume() {
        super.onResume()

        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }


    override fun onDestroyView() {
        super.onDestroyView()

        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun initializeViews() {

        hideStatusBar()

        // Initialize NavController
        navController = findNavController()

        loginId = arguments?.getString("loginId").orEmpty()
        userName = arguments?.getString("userName").orEmpty()
        mobile = arguments?.getString("mobile").orEmpty()
        email = arguments?.getString("email").orEmpty()
        gender = arguments?.getString("gender").orEmpty()
        candidateMobileNo = arguments?.getString("candidateMobileNo").orEmpty()
        selectedCertificateType = arguments?.getString("selectedCertificateType").orEmpty()
        selectedDepartment = arguments?.getString("selectedDepartment").orEmpty()




        val token = getToken(requireContext())
        val request = QuestiontReq(
            BuildConfig.VERSION_NAME,
            selectedDepartment,
            selectedCertificateType,
            ""
        )

        showProgressDialog("Loading...")

        viewModel.QuestiontReq(
            request,
            "Bearer $token"
        )



        viewModel.insertresultsubmit.observe(viewLifecycleOwner) { response ->

            response.onSuccess { data ->

                dismissProgressDialog()



                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Message")
                    .setMessage(data.responseDesc)
                    .setCancelable(false)
                    .setPositiveButton("OK") { dialog, _ ->


                        dialog.dismiss()
                    }
                    .show()
            }

            response.onFailure { error ->

                dismissProgressDialog()

                Toast.makeText(
                    requireContext(),
                    error.message ?: "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.insertsubmit.observe(viewLifecycleOwner) { response ->

            response.onSuccess { data ->

                dismissProgressDialog()

                val item = data.wrappedLista?.firstOrNull()

                item?.let {
                    val resultText = if (it.result == 0) {
                        "No"
                    } else {
                        "Yes"
                    }

                    val request = InsertRequest(
                        BuildConfig.VERSION_NAME,loginId,email,it.totalQuestions,it.wrongAns,numberofAttempt,it.notattempteQuestion,it.scoredPercentage
                        ,it.passingPercentage,it.correctAns,it.result,selectedDepartment,resultText,"Internal","",
                        selectedCertificateType
                    )

                    showProgressDialog("Loading...")

                    viewModel.insertfinalsubmit(
                        request,
                        "Bearer $token"
                    )
                }
            }

            response.onFailure { error ->

                dismissProgressDialog()

                Toast.makeText(
                    requireContext(),
                    error.message ?: "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.QuestiontReq.observe(viewLifecycleOwner) { response ->

            response.onSuccess { data ->

                dismissProgressDialog()

                // Store Question List


                binding.composeESOPTestScreen.apply {

                    val message = data.status.trim()
                    if (message.equals("error"))

                    {


                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Message")
                            .setMessage(data.message)
                            .setCancelable(false)
                            .setPositiveButton("Yes") { dialog, _ ->
                                dialog.dismiss()
                                backPressedCallback.isEnabled = false
                                requireActivity().onBackPressedDispatcher.onBackPressed()
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                                navController.popBackStack()
                            }
                            .show()

                    }


                    else
                    {

                        questionList = data.Questions

                        // Store Summary
                        val summary = data.summary

                        totalQuestions = summary.totalQuestions
                        easyCount = summary.easyCount
                        mediumCount = summary.mediumCount
                        hardCount = summary.hardCount
                        numberofAttempt = summary.numberofAttempt

                        easyPercentage = summary.easyPercentage
                        mediumPercentage = summary.mediumPercentage
                        hardPercentage = summary.hardPercentage
                        setContent {

                            Scaffold(
//                            topBar = {
//                                PremiumTopBar(
//                                    dynamicTitle = getString(R.string.esop),
//                                    onBackClick = {
//                                        navController.popBackStack()
//                                    }
//                                )
//                            },
                                containerColor = Color.White
                            )
                            { paddingValues ->

                                Box(
                                    modifier = Modifier.padding(paddingValues)
                                ) {

                                    ExamScreen(
                                        questionList = questionList
                                    )

                                }
                            }
                        }





                    }












                }

                }

            response.onFailure { error ->

                dismissProgressDialog()

                Toast.makeText(
                    requireContext(),
                    error.message ?: "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }





    }

    private fun showExitExamDialog() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Exit Exam")
            .setMessage("Do you want to exit exam?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()

                backPressedCallback.isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    @Composable
    fun ExamScreen(questionList: List<Question>)
    {
        var currentQuestionIndex by remember {
            mutableIntStateOf(0)
        }

        var selectedAnswer by remember {
            mutableStateOf("")
        }

        val currentQuestion = (questionList ?: emptyList())
            .getOrNull(currentQuestionIndex)
        var showSubmitDialog by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F3FA))
        )


        {

            // ================= HEADER =================


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(

                                Color(0xFF2563EB),
                                Color(0xFFD9CCE9)
                            )
                        )
                    )
                    .padding(16.dp),

                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )


            {

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        Text(
                            text = "Candidate ID :" + loginId,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Text(
                            text = "Candidate Name :" + userName
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.width(24.dp)
                )



                Box(
                    contentAlignment = Alignment.Center
                )
                {

                    val totalTime = 30 * 60 // 60 minutes
//                    val totalTime = 30

                    var timeLeft by remember {
                        mutableStateOf(totalTime)
                    }

                    LaunchedEffect(Unit) {

                        while (timeLeft > 0) {
                            delay(1000)
                            timeLeft--
                        }
                        showDialog.value = false
                        showDialogTime.value = true
                    }

                    CircularProgressIndicator(
                        progress = {
                            timeLeft.toFloat() / totalTime.toFloat()
                        },
                        modifier = Modifier.size(72.dp),
                        strokeWidth = 4.dp,
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.25f)
                    )

                    Text(
                        text = String.format(
                            "%02d:%02d",
                            timeLeft / 60,
                            timeLeft % 60
                        ),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (showDialogTime.value) {

                    AlertDialog(
                        onDismissRequest = { },

                        title = {
                            Text("Time Over")
                        },

                        text = {
                            Text("Time is over. Please complete your test.")
                        },

                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showReviewScreen = true
                                    showDialogTime.value = false

                                    // -----------------------------
                                    // API CALL
                                    // -----------------------------
                                    val submitList = questionList.mapIndexed { index, question ->
                                        SubmitAnswer(
                                            question_id = question.questionId,
                                            answer_given = answeredQuestions[index] ?: "NA"
                                        )
                                    }
                                    val token = getToken(requireContext())
                                    val request = SubmitExamRequest(
                                        appVersion = BuildConfig.VERSION_NAME,
                                        courseType = numberofAttempt,
                                        courseName = selectedDepartment,
                                        certificateType = selectedCertificateType,
                                        email = email,
                                        loginId = loginId,
                                        answers = submitList,
                                        userTypeIe = "Internal",
                                        paaCategory = ""
                                    )
                                    viewModel.insertresultsubmit(
                                        request,
                                        "Bearer $token"
                                    )

                                }
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }

                IconButton(
                    onClick = {}
                ) {
                    IconButton(
                        onClick = {

                            if (answeredQuestions.size == questionList.size) {

                                val remainingQuestions =
                                    questionList.size - answeredQuestions.size

                                Toast.makeText(
                                    context,
                                    "Please attempt all questions. Remaining: $remainingQuestions",
                                    Toast.LENGTH_LONG
                                ).show()


                            }

                            val submitList = answeredQuestions.map { entry ->

                                SubmitAnswer(
                                    question_id =
                                        questionList[entry.key].questionId,

                                    answer_given =
                                        entry.value
                                )
                            }


                            val request = SubmitExamRequest(
                                appVersion = BuildConfig.VERSION_NAME,
                                courseType = numberofAttempt,
                                courseName = selectedDepartment,
                                certificateType = selectedCertificateType,
                                email = email,
                                loginId = loginId,
                                answers = submitList,
                                userTypeIe = "Internal",
                                paaCategory = ""
                            )

                            submitRequestJson = GsonBuilder()
                                .setPrettyPrinting()
                                .create()
                                .toJson(request)
                            println(submitRequestJson)

                            showQuestionPalette.value = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // ================= QUESTION (Fixed) + OPTIONS (Scroll ONLY here) =================
            // Ye poora block weight(1f) leta hai, isliye available vertical space
            // ye fill karega aur neeche wale saare buttons hamesha bottom par
            // fixed rahenge. Question number aur question title FIXED hain
            // (kabhi scroll nahi honge). Sirf options ki list apni khud ki
            // verticalScroll wali Column mein hai, isliye scroll SIRF options
            // mein hoga.

            val scrollState = rememberScrollState()

            currentQuestion?.let { question ->

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            horizontal = 12.dp,
                            vertical = 20.dp
                        )
                ) {

                    // ---- Fixed part: does NOT scroll ----

                    Text(
                        text = "Question ${currentQuestionIndex + 1}/${questionList.size}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    val questionFontSize = 18.sp
                    Text(
                        text = buildAnnotatedString {

                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.Bold)
                            ) {
                                append("Question ")
                            }

                            append("${currentQuestionIndex + 1}. ")
                            append(question.questionTitle)
                        },
                        fontSize = questionFontSize,
                        lineHeight = (questionFontSize.value + 6).sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier.height(28.dp)
                    )

                    // ---- Scrollable part: ONLY options scroll here ----

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(scrollState)
                    ) {

                        question.options.forEach { option ->

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (selectedAnswer == option.option_Key)
                                            Color(0xFFE7D9F8)
                                        else
                                            Color.White
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (selectedAnswer == option.option_Key)
                                            Color(0xFF7B1FA2)
                                        else
                                            Color.LightGray,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        selectedAnswer = option.option_Key
                                    }
                                    .padding(horizontal = 16.dp, vertical = 14.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    RadioButton(
                                        selected = selectedAnswer == option.option_Key,
                                        onClick = {
                                            selectedAnswer = option.option_Key
                                        }
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = option.option_value,
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(
                                28.dp
                            )
                        )
                    }
                }
            }

            // ================= ACTION BUTTONS (Fixed at bottom) =================

            val buttonList = listOf(
                "Save & Next" to Color(0xFF4CAF50),
                "Save & Review" to Color(0xFFFFC107),
                "Mark" to Color(0xFF03A9F4),
                "Clear" to Color(0xFF9E9E9E)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp
                    ),

                horizontalArrangement = Arrangement.spacedBy(
                    12.dp
                )
            )
            {

                buttonList.forEach { (text, color) ->

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(
                                RoundedCornerShape(
                                    16.dp
                                )
                            )
                            .background(color)
                            .clickable {

                                when (text) {

                                    "Save & Next" -> {
                                        if (selectedAnswer.isNotEmpty()) {
                                            answeredQuestions[currentQuestionIndex] = selectedAnswer
                                        }

                                        reviewQuestions.remove(currentQuestionIndex)
                                        markedQuestions.remove(currentQuestionIndex)

                                        if (currentQuestionIndex < questionList.lastIndex) {
                                            currentQuestionIndex++
                                            selectedAnswer =
                                                answeredQuestions[currentQuestionIndex] ?: ""
                                        }
                                    }

                                    "Save & Review" -> {
                                        if (selectedAnswer.isNotEmpty()) {
                                            answeredQuestions[currentQuestionIndex] = selectedAnswer
                                        }

                                        if (!reviewQuestions.contains(currentQuestionIndex)) {
                                            reviewQuestions.add(currentQuestionIndex)
                                        }
                                        markedQuestions.remove(currentQuestionIndex)

                                        if (currentQuestionIndex < questionList.lastIndex) {
                                            currentQuestionIndex++
                                            selectedAnswer =
                                                answeredQuestions[currentQuestionIndex] ?: ""
                                        }
                                    }

                                    "Mark" -> {
                                        if (!markedQuestions.contains(currentQuestionIndex)) {
                                            markedQuestions.add(currentQuestionIndex)
                                        }
                                        reviewQuestions.remove(currentQuestionIndex)

                                        if (selectedAnswer.isNotEmpty()) {
                                            answeredQuestions[currentQuestionIndex] = selectedAnswer
                                        }

                                        // ✅ Mark karne ke baad bhi next question par jaayenge
                                        if (currentQuestionIndex < questionList.lastIndex) {
                                            currentQuestionIndex++
                                            selectedAnswer =
                                                answeredQuestions[currentQuestionIndex] ?: ""
                                        }
                                    }

                                    "Clear" -> {
                                        selectedAnswer = ""
                                        answeredQuestions.remove(currentQuestionIndex)
                                        reviewQuestions.remove(currentQuestionIndex)
                                        markedQuestions.remove(currentQuestionIndex)
                                    }

                                }
                            },

                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = text,

                            color =
                                if (text == "Save & Review")
                                    Color.Black
                                else
                                    Color.White,

                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(
                    28.dp
                )
            )

            // ================= PREVIOUS / NEXT (Small icon buttons, right above Submit) =================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp
                    ),

                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEDE7F6))
                        .border(
                            width = 1.2.dp,
                            color = Color(0xFF2563EB),
                            shape = CircleShape
                        ),

                    onClick = {

                        if (currentQuestionIndex > 0) {

                            currentQuestionIndex--

                            selectedAnswer =
                                answeredQuestions[currentQuestionIndex]
                                    ?: ""
                        }
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous",
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEDE7F6))
                        .border(
                            width = 1.2.dp,
                            color = Color(0xFF2563EB),
                            shape = CircleShape
                        ),

                    onClick = {

                        if (currentQuestionIndex < questionList.lastIndex) {

                            currentQuestionIndex++

                            selectedAnswer =
                                answeredQuestions[currentQuestionIndex]
                                    ?: ""
                        }
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next",
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(
                    8.dp
                )
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp,
                        vertical = 20.dp
                    )
                    .height(60.dp),

                shape = RoundedCornerShape(
                    20.dp
                ),

                onClick = {


//                    if (answeredQuestions.size == questionList.size) {
//
//                        val remainingQuestions =
//                            questionList.size - answeredQuestions.size
//
//                        Toast.makeText(
//                            context,
//                            "Please attempt all questions. Remaining: $remainingQuestions",
//                            Toast.LENGTH_LONG
//                        ).show()
//
//                        return@Button
//                    }

                    val submitList = answeredQuestions.map { entry ->

                        SubmitAnswer(
                            question_id =
                                questionList[entry.key].questionId,

                            answer_given =
                                entry.value
                        )
                    }

                    val request = SubmitExamRequest(

                        appVersion = BuildConfig.VERSION_NAME,
                        courseType = numberofAttempt,
                        courseName = selectedDepartment,
                        certificateType = selectedCertificateType,
                        email = email,
                        loginId = loginId,
                        answers = submitList,
                        userTypeIe = "Internal",
                        paaCategory = ""
                    )

                    submitRequestJson = GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .toJson(request)
                    println(submitRequestJson)

                    showQuestionPalette.value = true

                },

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),

                contentPadding = PaddingValues(0.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF2563EB),
                                    Color(0xFFD9CCE9)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Submit Exam",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        if (showQuestionPalette.value) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .zIndex(10f)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Question Index",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )

                        IconButton(
                            onClick = {
                                showQuestionPalette.value = false
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        LegendItem(
                            Color(0xFF9E9E9E),
                            "Not Answered"
                        )

                        LegendItem(
                            Color(0xFF4CAF50),
                            "Answered"
                        )

                        LegendItem(
                            Color(0xFFFFC107),
                            "Review"
                        )

                        LegendItem(
                            Color(0xFF03A9F4),
                            "Marked"
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        modifier = Modifier.weight(1f)
                    )
                    {

                        items(questionList.size) { index ->

                            val bgColor = when {
                                reviewQuestions.contains(index) -> Color(0xFFFFC107)
                                markedQuestions.contains(index) -> Color(0xFF03A9F4)
                                answeredQuestions.containsKey(index) -> Color(0xFF4CAF50)
                                else -> Color(0xFF9E9E9E)
                            }

                            Box(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(60.dp)
                                    .background(
                                        color = bgColor,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable {

                                        currentQuestionIndex = index

                                        selectedAnswer =
                                            answeredQuestions[index] ?: ""

                                        showQuestionPalette.value = false
                                    },
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = "${index + 1}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {


                        OutlinedButton(
                            modifier = Modifier
                                .width(130.dp)
                                .height(48.dp),
                            onClick = {
                                if (currentQuestionIndex > 0) {
                                    currentQuestionIndex--
                                    showQuestionPalette.value = false
                                }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color(0xFFE57373), // Light Red
                                contentColor = Color.White
                            )
                        ) {
                            Text("Previous")
                        }
                        OutlinedButton(
                            modifier = Modifier
                                .width(130.dp)
                                .height(48.dp),
                            onClick = {
                                showQuestionPalette.value = false

                                showReviewScreen = true

                                if (currentQuestionIndex < questionList.lastIndex) {
                                    currentQuestionIndex++
                                    showQuestionPalette.value = false
                                }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color(0xFF66BB6A), // Light Green
                                contentColor = Color.White
                            )
                        ) {
                            Text("Next")
                        }
//                        OutlinedButton(
//                            modifier = Modifier
//                                .width(130.dp)
//                                .height(48.dp),
//
//                            onClick = {
//                                showQuestionPalette.value = false
//
//                                showReviewScreen = true
//                                if (currentQuestionIndex < questionList.lastIndex) {
//
//                                    currentQuestionIndex++
//                                    showQuestionPalette.value = false
//                                }
//                            }
//                        ) {
//
//                            Text("Next")
//                        }
                    }
                }
            }
        }

        if (showDialog.value) {
//            VibrateWhileDialogVisible(showDialog.value)

            AlertDialog(
                onDismissRequest = {},
                title = {
                    Text("Security Alert")
                },
                text = {
                    Text("Face Not Matched or Object Detected")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog.value = false

                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        if (showReviewScreen) {

            showDialogTime.value = false

            // ===== Counts yahan add kariye =====
            val answeredCount = answeredQuestions.size

            val markedCount = markedQuestions.size + reviewQuestions.size

            val notAnsweredCount = questionList.indices.count { index ->
                !answeredQuestions.containsKey(index) &&
                        !markedQuestions.contains(index) &&
                        !reviewQuestions.contains(index)
            }

            var selectedFilter by remember { mutableStateOf("ALL") }

            // ================================

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .zIndex(20f)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    Text(
                        text = "Review Your Test",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        SummaryCard(
                            "Answered",
                            answeredCount.toString(),
                            Color(0xFF4CAF50),
                        )

                        SummaryCard(
                            "Not Answered",
                            notAnsweredCount.toString(),
                            Color(0xFFF44336),
                        )

                        SummaryCard(
                            "Marked",
                            markedCount.toString(),
                            Color(0xFFFFC107),
                        )

                        SummaryCard(
                            "Total",
                            questionList.size.toString(),
                            Color(0xFF2196F3),
                        )
                    }
                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {

                        items(questionList.size) { index ->

                            val status = when {

                                reviewQuestions.contains(index) ->
                                    "Marked for Review"

                                markedQuestions.contains(index) ->
                                    "Marked"

                                answeredQuestions.containsKey(index) ->
                                    "Answered"

                                else ->
                                    "Not Answered"
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),

                                horizontalArrangement =
                                    Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = "Q. ${index + 1}"
                                )

                                Text(
                                    text = status
                                )
                            }

                            Divider()
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = {

                                showReviewScreen = false
                            }
                        ) {

                            Text("Back to Test")
                        }

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick =
                                { showSubmitDialog = true }
                        ) {
                            Text("Submit Test")
                        }


                    }
                }
            }
        }

        if (showSubmitDialog) {
            AlertDialog(
                onDismissRequest = {
                    showSubmitDialog = false
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(40.dp)
                    )
                },
                title = {
                    Text(
                        text = "Submit Test",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Do you want to submit the questions?"
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSubmitDialog = false
                            // -----------------------------
                            // API CALL
                            // -----------------------------
                            val submitList = questionList.mapIndexed { index, question ->
                                SubmitAnswer(
                                    question_id = question.questionId,
                                    answer_given = answeredQuestions[index] ?: "NA"
                                )
                            }
                            val token = getToken(requireContext())
                            val request = SubmitExamRequest(
                                appVersion = BuildConfig.VERSION_NAME,
                                courseType = numberofAttempt,
                                courseName = selectedDepartment,
                                certificateType = selectedCertificateType,
                                email = email,
                                loginId = loginId,
                                answers = submitList,
                                userTypeIe = "Internal",
                                paaCategory = ""
                            )
                            viewModel.insertresultsubmit(
                                request,
                                "Bearer $token"
                            )
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            showSubmitDialog = false
                        }
                    ) {
                        Text("No")
                    }
                }
            )
        }



    }


    override fun setupObservers() {
    }

    override fun setupClickListeners() {
    }

    override fun loadInitialData() {
    }
}



