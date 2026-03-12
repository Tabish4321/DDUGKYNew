package com.deendayalproject.fragments.composeFragment

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.PreviousInspectionEditFragmentBinding
import com.deendayalproject.fragments.composeui.previous_inspection.DueDiligenceEditScreen
import com.deendayalproject.viewmodel.InspectionViewModel
import com.deendayalproject.viewmodel.PreviousAndDueViewModel

class DueDiligenceEditFragment :
    BaseFragment<PreviousInspectionEditFragmentBinding>(
        bindingInflater = PreviousInspectionEditFragmentBinding::inflate
    ) {

    private val viewModel: InspectionViewModel by viewModels()

    override fun initializeViews() {

        binding.composeViewPrevious.apply {

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {

                DueDiligenceEditScreen(
                    viewModel = viewModel,
                    navController = findNavController()
                )

            }
        }
    }

    override fun setupObservers() {}
    override fun setupClickListeners() {}
    override fun loadInitialData() {}
}