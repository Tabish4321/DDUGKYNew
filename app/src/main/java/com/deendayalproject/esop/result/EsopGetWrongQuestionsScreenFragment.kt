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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.deendayalproject.databinding.EsopWrongquestionsscreenFragmentBinding

//import com.example.esop.databinding.EsopResultFragmentBinding

// ─────────────────────────────────────────────
// Colors (file-scoped, won't clash with other files' private vals)
// ─────────────────────────────────────────────
private val GradientTop = Color(0xFF062C63)
private val GradientBottom = Color(0xFF031A3F)
private val ProgressTrack = Color(0xFF1BB2AA)
private val ProgressFill = Color(0xFF95DD31)

class MyResultFragment :
    BaseFragment<EsopWrongquestionsscreenFragmentBinding>(
        bindingInflater = EsopWrongquestionsscreenFragmentBinding::inflate
    ) {

    private val viewModel: SharedViewModel by viewModels()
    private lateinit var navController: NavController

    // Candidate info
    private var candidateLoginId by mutableStateOf("")
    private var candidateLoginEmail by mutableStateOf("")

    // API result state
    private var ESOPResultList by mutableStateOf<List<GetResultItem>>(emptyList())
    private var isLoading by mutableStateOf(true)
    private var errorMessage by mutableStateOf<String?>(null)
    private var showEmptyDialog by mutableStateOf(false)

    // UI state (Fragment-level, NOT remember — Fragment isn't a @Composable)
    private var expanded by mutableStateOf(false)
    private var expandedCertificate by mutableStateOf(false)
    private var selectedDepartment by mutableStateOf("Select")
    private var selectedCertificate by mutableStateOf("Select")

    private val departments = listOf("Select", "Finance", "Operations")
    private val certificateTypeOf = listOf("Select", "Master", "Professional")

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.R)
    override fun initializeViews() {

        navController = findNavController()

        candidateLoginEmail = arguments?.getString("candidateLoginEmail").orEmpty()
        candidateLoginId = arguments?.getString("candidateLoginId").orEmpty()

        Log.d("TAG", candidateLoginEmail)
        Log.d("TAG", candidateLoginId)

        hideStatusBar()


//        //    suspend fun getResultView(request: GetResultViewRequest, token: String): Result<GetResultItem> =
////        safeApiCallWithToken(token) {
////            apiService.getResultView(request)
////        }



        val token = getToken(requireContext())

        val request = GetResultViewRequest(
            "2.6.9",
            "MoRD6264180120",
            "0",
            "Master",
            "Operations"
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

                if (data. != 200) {
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
//            ResultScreen()
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

