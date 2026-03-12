/*
package com.deendayalproject.fragments.composeFragment

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.PreviousInspectionEditFragmentBinding
import com.deendayalproject.fragments.composeui.previous_inspection.PreviousInspectionDueAllObserver
import com.deendayalproject.model.response.PreviousObservationRes
import com.deendayalproject.viewmodel.InspectionViewModel
import kotlinx.coroutines.delay

class PreviousInspectionEditFragment :
    BaseFragment<PreviousInspectionEditFragmentBinding>(
        bindingInflater = PreviousInspectionEditFragmentBinding::inflate
    ) {

    private val viewModel: InspectionViewModel by viewModels()

    private var dateOfInspection = ""
    private var conductedBy = ""

    override fun initializeViews() {

        dateOfInspection =
            arguments?.getString("dateOfInspection").orEmpty()

        conductedBy =
            arguments?.getString("conductedBy").orEmpty()

        binding.composeViewPrevious.apply {

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {

                val navController = findNavController()

                val questionRes by viewModel
                    .getPreviousInspectionObservation
                    .collectAsState()

                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {

                    viewModel.getPreviousInspectionObservation(
                        dateOfInspection,
                        conductedBy
                    )

                    delay(600)
                    isLoading = false
                }

                val observationList =
                    questionRes?.wrappedList?.map {

                        PreviousObservationRes(
                            title = it.observationTitle,
                            conductedBy = it.conductedBy,
                            remarks = it.observationRemark,
                            questionId = it.questionId
                        )
                    } ?: emptyList()

                PreviousInspectionDueAllObserver(
                    observationItems = observationList,
                    onBackClick = { navController.popBackStack() },
                    isLoading = isLoading,
                    onSubmit = { uiState ->

                        viewModel.savePreviousInspectionObservation(
                            uiState
                        )
                    }
                )
            }
        }
    }

    override fun setupObservers() {}
    override fun setupClickListeners() {}
    override fun loadInitialData() {}
}*/
