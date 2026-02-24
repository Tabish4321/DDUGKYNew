package com.deendayalproject.fragments.composeFragment


import androidx.compose.ui.platform.ViewCompositionStrategy
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.CandidatePreviousbatchInspectionBinding

class CandidatePreviousBatchInspection : BaseFragment<CandidatePreviousbatchInspectionBinding>(
    bindingInflater = CandidatePreviousbatchInspectionBinding::inflate
){
    override fun initializeViews() {


        binding.candidatePreviousBatchCompose.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {



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