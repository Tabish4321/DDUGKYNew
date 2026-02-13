package com.deendayalproject.fragments

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.InspectionListFragmentBinding
import com.deendayalproject.fragments.composeui.TrainingCenterListScreen
import com.deendayalproject.model.response.TrainingCenterListInspecRes
import kotlinx.coroutines.delay

class InspectionListFragment : BaseFragment<InspectionListFragmentBinding>(
    bindingInflater = InspectionListFragmentBinding::inflate
) {






    private val sampleList = listOf(
        TrainingCenterListInspecRes(1, "2505000007", "SL-001/2026", "TC Lucknow, Uttar Pradesh", "Surprised"),
        TrainingCenterListInspecRes(2,"2505000008","SL-002/2026","TC Delhi, New Delhi","Planned"),
        TrainingCenterListInspecRes(3,"2505000009","SL-003/2026","TC Patna, Bihar","Planned"),
        TrainingCenterListInspecRes(4,"2505000010","SL-004/2026","TC Jaipur, Rajasthan","Surprised")
    )

    override fun initializeViews() {





        binding.composeInspectionListView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {

                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(1000)
                    isLoading = false
                }

                TrainingCenterListScreen(
                    items = sampleList,
                    isLoading = isLoading
                ) { selectedItem ->

                    findNavController().navigate(
                        InspectionListFragmentDirections.actionInspectionListFragmentToInspectionBasicDetailsFragment(selectedItem.prnNumber,selectedItem.sanctionLetterNo,selectedItem.inspectionType,selectedItem.id)
                    )
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