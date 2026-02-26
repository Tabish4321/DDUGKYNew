package com.deendayalproject.fragments.composeFragment

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.InspectionBasicFragmentBinding
import com.deendayalproject.fragments.composeui.main.InspectionModernScreen
import com.deendayalproject.model.request.InspectionPreviousBatchList
import com.deendayalproject.model.request.InspectionTcDetailsReq
import com.deendayalproject.model.response.PrevBatchItem
import com.deendayalproject.model.response.TrainingInspCenterDetails
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.InspectionViewModel

class InspectionBasicDetailsFragment :
    BaseFragment<InspectionBasicFragmentBinding>(
        bindingInflater = InspectionBasicFragmentBinding::inflate
    ) {

    private val viewModel: InspectionViewModel by viewModels()

    private var trainingCenterId = 0
    private var prnNumber = ""
    private var sanctionOrder = ""
    private var inspectionType = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initializeViews() {


        //  Get arguments
        trainingCenterId = arguments?.getInt("trainingCenterId", 0) ?: 0
        prnNumber = arguments?.getString("prnNumber") ?: ""
        sanctionOrder = arguments?.getString("sanctionOrder") ?: ""
        inspectionType = arguments?.getString("inspectionType") ?: ""

        binding.composeView.apply {

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )



            setContent {

                val batchResponse by viewModel.previousBatchList.collectAsState()
                val tcResponse by viewModel.tcDetails.collectAsState()
                val isLoading by viewModel.loading.collectAsState()

                val snackbarHostState = remember { SnackbarHostState() }

                var currentStep by remember { mutableStateOf(1) }

                // TC DETAILS API CALL
                LaunchedEffect(trainingCenterId) {

                    if (trainingCenterId != 0) {

                        viewModel.getDueDiligenceTcDetails(
                            InspectionTcDetailsReq(
                                BuildConfig.VERSION_NAME,
                                trainingCenterId.toString(),
                                sanctionOrder
                            ),
                            AppUtil.getSavedTokenPreference(requireContext())
                        )
                    }
                }

                //  STEP 2 → BATCH API CALL
                LaunchedEffect(currentStep) {

                    if (currentStep == 2 && batchResponse == null) {

                        viewModel.getInspectionPreviousBatchList(
                            InspectionPreviousBatchList(
                                BuildConfig.VERSION_NAME,
                                trainingCenterId.toString(),
                                sanctionOrder
                            ),
                            AppUtil.getSavedTokenPreference(requireContext())
                        )
                    }
                }

                //  Error Snackbar
                LaunchedEffect(Unit) {
                    viewModel.errorMessage.collect {
                        snackbarHostState.showSnackbar(it)
                    }
                }

                //  Map API Response
                val batchList: List<PrevBatchItem> =
                    batchResponse?.wrappedList ?: emptyList()

                val tcDetails: TrainingInspCenterDetails? =
                    tcResponse?.wrappedList?.firstOrNull()

               Scaffold(
                    contentWindowInsets = WindowInsets(0),
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ) { padding ->

                    Box(modifier = Modifier.padding(padding)) {

                        InspectionModernScreen(
                            prnNumber = prnNumber,
                            sanctionLetter = sanctionOrder,
                            inspectionType = inspectionType,
                            trainingCenterId = trainingCenterId.toString(),
                            batchList = batchList,
                            currentStep = currentStep,
                            onStepChange = { currentStep = it },
                            isLoading = isLoading,
                            trainingDetails = tcDetails,
                            onEditClick = { inspectionId ->
                                findNavController().navigate(
                                    InspectionBasicDetailsFragmentDirections
                                        .actionInspectionBasicDetailsFragmentToPreviousInspectionEditFragment(
                                            inspectionId.date,
                                            inspectionId.conductedBy
                                        )
                                )
                            }
                        )
                    }
                }
            }

        }
    }

    override fun setupObservers() {}
    override fun setupClickListeners() {}
    override fun loadInitialData() {}
}