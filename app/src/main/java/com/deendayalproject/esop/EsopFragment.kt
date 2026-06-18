package com.deendayalproject.esop

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.EsopFragmentBinding
import com.deendayalproject.databinding.OngoingCandidateFragmentBinding
import com.deendayalproject.fragments.composeui.ongoingcandidateverification.OngoingCandidateSectionScreen
import com.deendayalproject.model.request.GetImageListReq
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.CandidateAssessmentViewModel
import com.deendayalproject.viewmodel.CandidateVerificationViewModel
import com.deendayalproject.viewmodel.InspectionViewModel
import kotlin.getValue


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
//        candidateId = arguments?.getString("candidateId") ?: ""
//        batchId = arguments?.getString("batchId") ?: ""
//        candidateName = arguments?.getString("candidateName") ?: ""
//        candidateMobileNo = arguments?.getString("mobileNumber") ?: ""
//        candidateRollNo = arguments?.getString("rollNumber") ?: ""
//        candidateDp = arguments?.getString("candidateProfilePic") ?: ""

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

//                OngoingCandidateSectionScreen(
//                    context = requireContext(),
//                    candidateVerificationViewModel,
//                    viewModel,
//                    candidateId = candidateId,
//                    batchId = batchId,
//                    candidateName = candidateName,
//                    candidateMobileNo = candidateMobileNo,
//                    candidateRollNo = candidateRollNo,
//                    imageList = imageResponse?.wrappedList,
//                    onBackClick = { findNavController().popBackStack() }
//                )
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