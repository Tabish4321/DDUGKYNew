package com.deendayalproject.fragments.composeFragment

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.PreviousInspectionEditFragmentBinding
import com.deendayalproject.fragments.composeui.previous_inspection.PreviousInspectionEditScreen
import com.deendayalproject.model.request.PreviousInsQuesReq
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.InspectionViewModel

class PreviousInspectionEditFragment :
    BaseFragment<PreviousInspectionEditFragmentBinding>(
        bindingInflater = PreviousInspectionEditFragmentBinding::inflate
    ) {

    private val viewModel: InspectionViewModel by viewModels()

    override fun initializeViews() {

        binding.composeViewPrevious.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        binding.composeViewPrevious.setContent {

            val response by viewModel.getPreviousInsQues.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.getPreviousInsQues(
                    PreviousInsQuesReq(
                        appVersion = BuildConfig.VERSION_NAME,
                        previousInspectionId = AppUtil.getPreviouseSavedInspectionIdPreference(requireContext()).toInt()
                    ),
                    AppUtil.getSavedTokenPreference(requireContext())
                )
            }

            PreviousInspectionEditScreen(
                navController = findNavController(),
                data = response?.wrappedList?.firstOrNull(),
                viewModel = viewModel
            )
        }
    }

    override fun setupObservers() {}
    override fun setupClickListeners() {}
    override fun loadInitialData() {}
}