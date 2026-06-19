package com.deendayalproject.esop

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.EsopFragmentBinding
import com.deendayalproject.databinding.OngoingCandidateFragmentBinding
import com.deendayalproject.esop.dashboard.DashboardSection
import com.deendayalproject.esop.exam.StartTestCard
import com.deendayalproject.esop.profile.CandidateInfoCard
import com.deendayalproject.fragments.composeui.common.ExpandableComplianceCard
import com.deendayalproject.fragments.composeui.common.PremiumCandidateHeader
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.fragments.composeui.ongoingcandidateverification.AssessmentSection
import com.deendayalproject.fragments.composeui.ongoingcandidateverification.AttendanceComplianceScreen
import com.deendayalproject.fragments.composeui.ongoingcandidateverification.BasicRecordsSection
import com.deendayalproject.fragments.composeui.ongoingcandidateverification.DistributedLearningSection
import com.deendayalproject.fragments.composeui.ongoingcandidateverification.EntitlementsSection
import com.deendayalproject.fragments.composeui.ongoingcandidateverification.OngoingCandidateSectionScreen
import com.deendayalproject.fragments.composeui.ongoingcandidateverification.ResidentialFacilitySection
import com.deendayalproject.fragments.composeui.ongoingcandidateverification.SectionContent
import com.deendayalproject.fragments.composeui.ongoingcandidateverification.getSectionIcon
import com.deendayalproject.fragments.composeui.ongoingcandidateverification.getSectionStatus
import com.deendayalproject.model.request.GetAttendanceDetailsReq
import com.deendayalproject.model.request.GetImageListReq
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.CandidateAssessmentViewModel
import com.deendayalproject.viewmodel.CandidateVerificationViewModel
import com.deendayalproject.viewmodel.InspectionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.set
import kotlin.getValue

//code commit and update 18.06.2026 15.48PM
class EsopFragment :
    BaseFragment<EsopFragmentBinding>(
        bindingInflater = EsopFragmentBinding::inflate
    )  {

    private val viewModel: InspectionViewModel by viewModels()


    private var candidateId = ""
    private var batchId = ""
      private var candidateName = ""
      private var candidateMobileNo = ""
      private var candidateRollNo = ""
      private var candidateDp = ""



    @RequiresApi(Build.VERSION_CODES.R)
    override fun initializeViews() {
        hideStatusBar()
        // API CALL HERE
        viewModel.getCandidateImageRecords(
            GetImageListReq(
                appVersion = BuildConfig.VERSION_NAME,
                candidateId = candidateId
            ),
            AppUtil.getSavedTokenPreference(requireContext())
        )

        val candidateVerificationViewModel: CandidateAssessmentViewModel by viewModels()


        binding.composeESOP.apply {

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )


            setContent {

                val imageResponse by viewModel
                    .getCandidateImageRecords
                    .collectAsState()

                Scaffold(
                    topBar = {

                        PremiumTopBar(
                            dynamicTitle = context.getString(R.string.esop),
                            onBackClick = {
                                findNavController().popBackStack()
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
                                name = candidateName,
                                age = "30",
                                designation = "Developer",
                                mobileNo = candidateMobileNo,
                                gender = "Male",
                                imageUrl = ""
                            )


                            StartTestCard()

                            Spacer(modifier = Modifier.height(30.dp))
                            DashboardSection()
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


  }