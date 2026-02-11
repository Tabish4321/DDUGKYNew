package com.deendayalproject.fragments

import SharedViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.databinding.RfItemQteamLayoutBinding
import com.deendayalproject.databinding.RfSrlmListFragmentBinding
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.response.RfCenter
import com.deendayalproject.util.AppUtil
import com.deendayalproject.base.BaseFragment
import dagger.hilt.android.ViewModelLifecycle

class RFSrlmListFragment : BaseFragment<RfSrlmListFragmentBinding>(
    RfSrlmListFragmentBinding::inflate
) {
    private lateinit var viewModel: SharedViewModel

    override fun initializeViews() {
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        Log.d("FRAGMENT NAME",  "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━RFSrlmListFragment━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        setupToolbar(
            binding.root,
            titleRes = R.string.residential_facility_srlm,
            backClick = { findNavController().navigateUp() }
        )

        // Setup RecyclerView using BaseFragment's method
        setupRecyclerView(
            recyclerView = binding.recyclerViewSRLM,
            items = emptyList<RfCenter>(),
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = { inflater, parent, attachToParent ->
                RfItemQteamLayoutBinding.inflate(inflater, parent, attachToParent)
            },
            onBind = { center, binding, position ->
                binding.apply {
                    trainingCenterName.text = center.trainingCenterName
                    trainingCenterAddress.text = center.trainingCenterAddress
                    senctionOrder.text =  center.senctionOrder
                    districtName.text =  center.districtName
                    totalCapacity.text = center.finalRfCapacity
                    residenctialType.text=center.residentialType

                }
            },
            onItemClick = { center, position ->
                val action =
                    RFSrlmListFragmentDirections.actionRFSrlmListFragmentToRFSRLMFormFragment(
                        center.trainingCenterId.toString(),
                        center.trainingCenterName,
                        center.senctionOrder,
                        center.facilityId
                    )
                findNavController().navigate(action)
            },
            noDataTitle = "No Training Centers",
            noDataDescription = "No training centers available at the moment",
            noDataIconRes = R.drawable.no_data
        )
    }

    override fun setupObservers() {
        observeViewModel()
    }

    override fun setupClickListeners() {
//        binding.backButton.setOnClickListener {
//            findNavController().navigateUp()
//        }
    }

    override fun loadInitialData() {
        val request = TrainingCenterRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext())
        )

        viewModel.getRFSRLMVerification(request, AppUtil.getSavedTokenPreference(requireContext()))

        viewModel.loading.observe(viewLifecycleOwner){loading ->
            if(loading){
                binding.progressBar.show()
            } else {
                binding.progressBar.hide()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.trainingRfCenters.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                handleApiResponse(
                    responseCode = response.responseCode,
                    data = response.wrappedList,
                    onSuccess = { data ->
                        // Update RecyclerView data using BaseFragment method
                        updateRecyclerViewData(binding.recyclerViewSRLM.id, data ?: emptyList())
                    },
                    onNoData = {
                        updateRecyclerViewData(binding.recyclerViewSRLM.id, emptyList<RfCenter>())
                        showToast("No data available.")
                    },
                    onUpgradeRequired = {
                        showToast("Please upgrade your app.")
                    },
                    onSessionExpired = {
                        handleSessionExpired()
                    }
                )
            }
            result.onFailure { exception ->
                showErrorToast("Failed: ${exception.message}")
                logCrashlyticsError("observeViewModel", exception as Exception)
            }
        }


    }

    // Keep original method names as requested
    fun showProgressBar() {
        showProgressDialog("Loading...")
    }

    fun hideProgressBar() {
        dismissProgressDialog()
    }
}