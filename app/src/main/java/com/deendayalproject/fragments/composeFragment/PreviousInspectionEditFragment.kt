package com.deendayalproject.fragments.composeFragment

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.findNavController
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.PreviousInspectionEditFragmentBinding
import com.deendayalproject.fragments.composeui.previous_inspection.PreviousInspectionDueAllObserver
import com.deendayalproject.model.response.PreviousObservationRes
import kotlinx.coroutines.delay

class PreviousInspectionEditFragment : BaseFragment<PreviousInspectionEditFragmentBinding>(
    bindingInflater = PreviousInspectionEditFragmentBinding::inflate
) {


    private var dateOfInspection = ""
    private var conductedBy = ""

    override fun initializeViews() {



        val sampleObservationList = listOf(
            PreviousObservationRes(
                title = "External Assessment completed",
                conductedBy = "Rahul",
                remarks = "Need Improvement"
            ),
            PreviousObservationRes(
                title = "OJT verification done by the PIA Q.Team",
                conductedBy = "Rahul",
                remarks = "Need Improvement"
            ),
            PreviousObservationRes(
                title = "Action taken by the PIA for replacement ",
                conductedBy = "Rahul",
                remarks = "Need Improvement"
            ),
        )




        dateOfInspection = arguments?.getString("dateOfInspection").toString()
        conductedBy = arguments?.getString("conductedBy").toString()



        binding.composeViewPrevious.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                val navController = findNavController()

                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(1000)
                    isLoading = false
                }


                PreviousInspectionDueAllObserver(
                    observationItems = sampleObservationList,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    isLoading,
                    onSubmit = { uiState ->


                        //  Yaha API call karo
                    }
                )





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