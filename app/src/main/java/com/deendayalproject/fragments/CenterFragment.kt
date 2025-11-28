package com.deendayalproject.fragments

import SharedViewModel
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.databinding.FragmentCenterBinding
import com.deendayalproject.databinding.ItemTrainingCenterBinding
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.request.TrainingCenter
import com.deendayalproject.util.AppUtil
import com.deendayalproject.base.BaseFragment

class CenterFragment : BaseFragment<FragmentCenterBinding>(
    FragmentCenterBinding::inflate
) {
    private lateinit var viewModel: SharedViewModel

    override fun initializeViews() {
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        setupRecyclerView(
            recyclerView = binding.recyclerView,
            items = emptyList<TrainingCenter>(),
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = { inflater, parent, attachToParent ->
                ItemTrainingCenterBinding.inflate(inflater, parent, attachToParent)
            },
            onBind = { center, binding, position ->
                binding.trainingCenterName.text = "Training Center Name: ${center.trainingCenterName}"
                binding.trainingCenterAddress.text = "Training Center Address: ${center.trainingCenterAddress}"
                binding.senctionOrder.text = "Sanction Order: ${center.senctionOrder}"
                binding.districtName.text = "District Name: ${center.districtName}"
            },
            onItemClick = { center, position ->
                AppUtil.savesanctionOrderPreference(requireContext(), center.senctionOrder)
                AppUtil.savecenterIdPreference(requireContext(), center.trainingCenterId.toString())

                val action = CenterFragmentDirections.actionCenterFragmentToFragmentTraining(
                    center.trainingCenterId.toString(),
                    center.senctionOrder,
                    center.status,
                    center.remarks,
                    center.trainingCenterName
                )
                findNavController().navigate(action)
            },
            noDataTitle = "No Training Centers",
            noDataDescription = "No training centers available at the moment",
        )
    }

    override fun setupObservers() {
        observeViewModel()
    }

    override fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun loadInitialData() {
        val request = TrainingCenterRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.fetchTrainingCenters(request, AppUtil.getSavedTokenPreference(requireContext()))
    }

    private fun observeViewModel() {
        viewModel.trainingCenters.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                handleApiResponse(
                    responseCode = response.responseCode,
                    data = response.wrappedList,
                    onSuccess = { data ->
                        // Update RecyclerView data using BaseFragment method
                        updateRecyclerViewData(binding.recyclerView.id, data ?: emptyList())
                    },
                    onNoData = {
                        updateRecyclerViewData(binding.recyclerView.id, emptyList<TrainingCenter>())
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

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.progressBar.show()
            } else {
                binding.progressBar.hide()
            }
        }
    }
}