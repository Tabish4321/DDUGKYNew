package com.deendayalproject.fragments.composeFragment


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.InspectionListFragmentBinding
import com.deendayalproject.fragments.composeui.common.ErrorScreen
import com.deendayalproject.fragments.composeui.common.ShimmerTrainingList
import com.deendayalproject.fragments.composeui.trainingCenListAandDetails.TrainingCenterListScreen
import com.deendayalproject.model.request.GetTcInspectionList
import com.deendayalproject.model.response.TrainingCenterListInspecRes
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.InspectionViewModel

class InspectionListFragment :
    BaseFragment<InspectionListFragmentBinding>(
        bindingInflater = InspectionListFragmentBinding::inflate
    ) {

    private val viewModel: InspectionViewModel by viewModels()

    override fun initializeViews() {

        binding.composeInspectionListView.apply {

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {


                val snackbarHostState = remember { SnackbarHostState() }

                val dueDiligenceListResponse by viewModel.dueDiligenceList.collectAsState()
                val isLoading by viewModel.loading.collectAsState()

                //  API Call on first load
                LaunchedEffect(Unit) {
                    viewModel.getDueDiligenceDetails(
                        GetTcInspectionList(BuildConfig.VERSION_NAME),
                        AppUtil.getSavedTokenPreference(requireContext())
                    )
                }

                //  Error Snackbar Collector
                LaunchedEffect(Unit) {
                    viewModel.errorMessage.collect {
                        snackbarHostState.showSnackbar(it)
                    }
                }

                //  Session Expired Collector
                LaunchedEffect(Unit) {
                    viewModel.sessionExpired.collect {
                        snackbarHostState.showSnackbar("Session Expired")

                    }
                }

                Scaffold(
                    contentWindowInsets = WindowInsets(0),
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ) { padding ->

                    Box(modifier = Modifier.padding(padding)) {

                        when {

                            isLoading -> {
                                ShimmerTrainingList()
                            }

                            dueDiligenceListResponse == null -> {
                                // Optional empty state
                            }

                            else -> {

                                val data = dueDiligenceListResponse?.wrappedList ?: emptyList()

                                TrainingCenterListScreen(
                                    items = data.map {
                                        TrainingCenterListInspecRes(
                                            id = it.trainingCenterId,
                                            prnNumber = it.prnRegistrationNo,
                                            sanctionLetterNo = it.sanctionOrder,
                                            inspectionType = it.inspectionType,
                                            inspectionId = it.inspectionId
                                        )
                                    },
                                    isLoading = false,
                                    onBackClick = {
                                        findNavController().navigateUp()
                                    },
                                    onItemClick = { selectedItem ->


                                        AppUtil.saveInspectionIdPreference(requireContext(), selectedItem.inspectionId)


                                        findNavController().navigate(
                                            InspectionListFragmentDirections
                                                .actionInspectionListFragmentToInspectionBasicDetailsFragment(
                                                    selectedItem.prnNumber,
                                                    selectedItem.sanctionLetterNo,
                                                    selectedItem.inspectionType,
                                                    selectedItem.id
                                                )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }        }
    }

    override fun setupObservers() {}
    override fun setupClickListeners() {}
    override fun loadInitialData() {}
}