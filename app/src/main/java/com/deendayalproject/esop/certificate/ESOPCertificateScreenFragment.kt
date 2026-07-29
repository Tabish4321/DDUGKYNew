package com.deendayalproject.esop.certificate





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
import androidx.core.os.bundleOf
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.EsopCertificatescreenBinding
import com.deendayalproject.databinding.EsopMytestFragmentBinding
import com.deendayalproject.model.request.InsertRequest
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


class ESOPCertificateScreenFragment :
    BaseFragment<EsopCertificatescreenBinding>(
        bindingInflater = EsopCertificatescreenBinding::inflate
    ) {

    // Candidate info
    private var wrongAns by mutableStateOf("")
    private val viewModel: SharedViewModel by viewModels()
    private var numberofAttempt by mutableStateOf("")
    private var id: Int = 0
    private var percentage by mutableStateOf("")
    private var departmentCetegory by mutableStateOf("")
    private var certificateType by mutableStateOf("")
    private var candidateLoginId by mutableStateOf("")
    private var totalQuestions by mutableStateOf("")
    private var resultText by mutableStateOf("")
    private var correctAns by mutableStateOf("")
    private var result by mutableStateOf("")

    private var candidateName by mutableStateOf("")
    private var candidateMobileNo by mutableStateOf("")

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.R)
    override fun initializeViews() {

        wrongAns = arguments?.getString("wrongAns").orEmpty()
        numberofAttempt = arguments?.getString("numberofAttempt").orEmpty()
         id = arguments?.getInt("id") ?: 0
        percentage = (arguments?.getInt("percentage") ?: 0).toString()

        correctAns = arguments?.getString("correctAns").orEmpty()
        result = arguments?.getString("result").orEmpty()
        candidateName = arguments?.getString("candidateName").orEmpty()
        candidateMobileNo = arguments?.getString("candidateMobileNo").orEmpty()

        candidateLoginId = arguments?.getString("candidateLoginId").orEmpty()
        certificateType = arguments?.getString("certificateType").orEmpty()
        totalQuestions = (arguments?.getInt("totalQuestions") ?: 0).toString()
        resultText = arguments?.getString("resultText").orEmpty()
        departmentCetegory = arguments?.getString("departmentCetegory").orEmpty()


        hideStatusBar()





        binding.composeESOPCertificateScreen.setContent {
            ESOPCertificateScreen(
                percentage = percentage,
                candidateName = candidateName,
                departmentCetegory = departmentCetegory,
                certificationType = certificateType,
                totalQuestions=totalQuestions,
                resultText=resultText,
                candidateLoginId=candidateLoginId,
                onBackClick = { findNavController().popBackStack() }
            )
        }
    }

    override fun setupObservers() {}
    override fun setupClickListeners() {}
    override fun loadInitialData() {}
}
