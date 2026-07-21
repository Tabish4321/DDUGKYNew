package com.deendayalproject.esop

import SharedViewModel
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.deendayalproject.esop.dashboard.DashboardSection
import com.deendayalproject.esop.profile.CandidateInfoCard
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.model.request.EsopCandidateRequest
import com.deendayalproject.model.request.InsertFacultyAttendance
import com.deendayalproject.model.response.EsopCandidateRes
import com.deendayalproject.network.SecurePreferenceManager.getToken
import com.deendayalproject.uidai.XstreamCommonMethods
import com.deendayalproject.uidai.XstreamCommonMethods.respDecodedXmlToPojoAuth
import com.deendayalproject.uidai.capture.CaptureResponse
import com.deendayalproject.uidai.ekyc.UidaiKycRequest
import com.deendayalproject.util.AESCryptography
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
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.getValue

//code commit and update 18.06.2026 15.48PM



@AndroidEntryPoint
class EsopFragment :
    BaseFragment<EsopFragmentBinding>(
        bindingInflater = EsopFragmentBinding::inflate
    ) {

    private val viewModel: SharedViewModel by viewModels()

    private var candidateId = ""
    private var batchId = ""

    private lateinit var navController: NavController

    private var candidateName by mutableStateOf("")
    private var candidateMobileNo by mutableStateOf("")
    private var candidateGender by mutableStateOf("")
    private var candidateLoginId by mutableStateOf("")
    private var aadhaarNumber by mutableStateOf("")
    private var decryptedAadhaar = ""
    private var candidateLoginEmail by mutableStateOf("")
    private val categoryList = mutableListOf<String>()



    private var ESOPCandidateList: List<EsopCandidateRes.EsopCandidate> = emptyList()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initializeViews() {

        hideStatusBar()

        // Initialize NavController
        navController = findNavController()





        val token = getToken(requireContext())

        val request = EsopCandidateRequest(
            BuildConfig.VERSION_NAME,
            AppUtil.getSavedLoginIdPreference(requireContext())
        )

        showProgressDialog("Loading...")

        viewModel.esoprolecategory(
            request,
            "Bearer $token"
        )

        viewModel.esoprolecategory.observe(viewLifecycleOwner) { response ->

            response.onSuccess { data ->

                dismissProgressDialog()

                if (data.responseDesc == "No data available.") {

//                    Toast.makeText(
//                        requireContext(),
//                        data.responseDesc,
//                        Toast.LENGTH_SHORT
//                    ).show()

                } else {

                    ESOPCandidateList = data.wrappedList

                    if (ESOPCandidateList.isNotEmpty()) {

                        val candidate = ESOPCandidateList.first()

                        candidateName = candidate.userName
                        candidateLoginEmail = candidate.emailId
                        candidateMobileNo = candidate.mobile
                        candidateGender = candidate.gender ?: ""
                        candidateLoginId = candidate.loginId

                        aadhaarNumber =AESCryptography. decryptIntoStringEsop(
                            candidate.aadhaarNumber,
                            AppConstant.ENCRYPT_IV_KEYESOP,
                            AppConstant.ENCRYPT_KEYESOP
                        )


                        categoryList.clear()

                        // Saari categories add karo
                        categoryList.addAll(candidate.categories.map { it.category })

//                        Toast.makeText(
//                            requireContext(),
//                            decryptedAadhaar,
//                            Toast.LENGTH_LONG
//                        ).show()
                    }


                }
            }

            response.onFailure { error ->

                dismissProgressDialog()

                Toast.makeText(
                    requireContext(),
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.composeESOP.apply {





            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

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

                    LazyColumn(

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),

                        contentPadding = PaddingValues(12.dp)

                    ) {

                        item {

                            CandidateInfoCard(
                                loginId = candidateLoginId,
                                userName = candidateName,
                                mobile = candidateMobileNo,
                                email = candidateLoginEmail,
                                gender = candidateGender
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            StartTestCard(
                                navController = navController,
                                loginId = candidateLoginId,
                                userName = candidateName,
                                mobile = candidateMobileNo,
                                email = candidateLoginEmail,
                                gender = candidateGender,
                                aadhaarNumber = aadhaarNumber,
                                categoryList = categoryList
                            )


                            Spacer(modifier = Modifier.height(12.dp))

                            DashboardSection(
                                navController = navController,
                                candidateLoginEmail,
                                candidateLoginId,
                                candidateName,
                                candidateMobileNo
                            )
                        }
                    }
                }
            }
        }
    }

    override fun setupObservers() {
    }

    override fun setupClickListeners() {
    }

    override fun loadInitialData() {
    }


    @Composable
    fun StartTestCard(
        navController: NavController,
        loginId: String,
        userName: String,
        mobile: String,
        email: String,
        gender: String,
        aadhaarNumber: String,
        categoryList: List<String>,
        modifier: Modifier = Modifier
    ) {

        val context = LocalContext.current

        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable {

                    val categories = if (categoryList.isNotEmpty()) {
                        categoryList.joinToString(", ")
                    } else {
                        "No Category Available"
                    }

                    val bundle = bundleOf(
                        "loginId" to loginId,
                        "userName" to userName,
                        "mobile" to mobile,
                        "email" to email,
                        "gender" to gender,
                        "candidateLoginId" to candidateLoginId,
                        "candidateMobileNo" to candidateMobileNo,
                        "aadhaarNumber" to aadhaarNumber,
                        "categoryList" to ArrayList(categoryList)
                    )

                    navController.navigate(R.id.action_esopFragment_to_TestInstructionsFragment,bundle)




//                    Toast.makeText(
//                        context,
//                        categories,
//                        Toast.LENGTH_LONG
//                    ).show()


                },

            shape = RoundedCornerShape(16.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2962FF)
            ),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Start Test Now",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Attempt a new eSOP exam",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color(0xFF2962FF)
                    )
                }
            }
        }
    }











}





