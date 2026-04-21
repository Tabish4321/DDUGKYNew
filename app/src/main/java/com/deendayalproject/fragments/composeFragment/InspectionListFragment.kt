package com.deendayalproject.fragments.composeFragment


import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.deendayalproject.fragments.composeui.EmptyScreen
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

    @RequiresApi(Build.VERSION_CODES.O)
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
                       // snackbarHostState.showSnackbar(it)
                        Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
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
                                EmptyScreen({findNavController().navigateUp()},"Training Centers")
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
                                            inspectionId = it.inspectionId,
                                            centerType = it.centerType,
                                            inspectionDate = it.inspectionDate,
                                            trainingCenterCode= it.inspectionCode,
                                            piaName=it.piaName,
                                            trainingCenterName = it.trainingCenterName,
                                            inspectionCode = it.inspectionCode
                                        )
                                    },
                                    isLoading = false,
                                    onBackClick = {
                                        findNavController().navigateUp()
                                    },
                                    onItemClick = { selectedItem ->

                                        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
                                        val currentDate = java.time.LocalDate.now()

                                        val inspectionDate = try {
                                            selectedItem.inspectionDate?.let {
                                                java.time.LocalDate.parse(it, formatter)
                                            } ?: currentDate   //  NULL → current date
                                        } catch (e: Exception) {
                                            currentDate   //  wrong format → current date
                                        }

                                     if (inspectionDate == currentDate) {
                                          //  if (true) {
                                            AppUtil.saveInspectionIdPreference(requireContext(), selectedItem.inspectionId)
                                            AppUtil.saveTrainingCenterIdPreference(requireContext(), selectedItem.id.toString())
                                            AppUtil.saveCenterTypePreference(requireContext(), selectedItem.centerType)
                                            AppUtil.saveSanctionOrderInsPreference(requireContext(), selectedItem.sanctionLetterNo)

                                            findNavController().navigate(
                                                InspectionListFragmentDirections
                                                    .actionInspectionListFragmentToInspectionBasicDetailsFragment(
                                                        selectedItem.prnNumber,
                                                        selectedItem.sanctionLetterNo,
                                                        selectedItem.inspectionType,
                                                        selectedItem.id
                                                    )
                                            )

                                        } else {

                                            android.widget.Toast.makeText(
                                                requireContext(),
                                                "This Inspection will Active on ${selectedItem.inspectionDate ?: "Today"}",
                                                android.widget.Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                )
                            }
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