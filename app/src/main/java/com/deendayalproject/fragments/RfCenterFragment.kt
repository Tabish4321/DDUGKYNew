package com.deendayalproject.fragments

import SharedViewModel
import android.util.Log
import androidx.fragment.app.FragmentManager.TAG
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.databinding.FragmentRfCenterBinding
import com.deendayalproject.databinding.ItemTrainingCenterBinding
import com.deendayalproject.model.request.AddNewRFReq
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.response.TrainingCenterItem
import com.deendayalproject.util.AppUtil
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.util.gone

class RfCenterFragment : BaseFragment<FragmentRfCenterBinding>(
    FragmentRfCenterBinding::inflate
) {
    private lateinit var viewModel: SharedViewModel
    private var centerId = ""
    private var sanctionOrder = ""

    override fun initializeViews() {
        Log.d("FRAGMENT NAME", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━RfCenterFragment━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        setupToolbar(
            binding.root,
            titleRes = R.string.residential_facility,
            showBack = true,
            showLang = false,
            showProfile = false,
            backClick = { findNavController().navigateUp()}
        )
        // Setup RecyclerView using BaseFragment's method
        setupRecyclerView(
            recyclerView = binding.recyclerView,
            items = emptyList<TrainingCenterItem>(),
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = { inflater, parent, attachToParent ->
                ItemTrainingCenterBinding.inflate(inflater, parent, attachToParent)
            },
            onBind = { center, binding, position ->
                binding.trainingCenterName.text = " ${center.trainingCenterName}"
                binding.trainingCenterAddress.text = " ${center.trainingCenterAddress}"
                binding.senctionOrder.text = " ${center.senctionOrder}"
                binding.districtName.text = "${center.districtName}"
                binding.maleCapacityContainer.gone()
                binding.femaleCapacityContainer.gone()
                binding.totalCapacityContainer.gone()
            },
            onItemClick = { center, position ->
                handleItemClick(center)
            },
            noDataTitle = "No Training Centers",
            noDataDescription = "No training centers available at the moment",
            noDataIconRes = R.drawable.no_data
        )
    }

    override fun setupObservers() {
        observeViewModel()
        observeAddNewRF()
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
        viewModel.fetchRfList(request, AppUtil.getSavedTokenPreference(requireContext()))
    }

    private fun observeViewModel() {
        viewModel.rfTrainingCenters.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                handleApiResponse(
                    responseCode = response.responseCode,
                    data = response.wrappedList,
                    onSuccess = { data ->
                        updateRecyclerViewData(binding.recyclerView.id, data ?: emptyList())
                    },
                    onNoData = {
                        updateRecyclerViewData(binding.recyclerView.id, emptyList<TrainingCenterItem>())
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

    private fun observeAddNewRF() {
        viewModel.saveInitialResidentialFacility.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                handleApiResponse(
                    responseCode = response.responseCode,
                    data = response,
                    onSuccess = { data ->
                        val facilityId = response.facilityId.toString()
                        showSuccessToast("RF Added successfully")

                        val action = RfCenterFragmentDirections.actionRfcenterFragmentToFragmentResidentialFacility(
                            centerId,
                            sanctionOrder,
                            facilityId
                        )
                        findNavController().navigate(action)
                    },
                    onNoData = {
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
                logCrashlyticsError("observeAddNewRF", exception as Exception)
            }
        }
    }

    private fun handleItemClick(center: TrainingCenterItem) {
        centerId = center.trainingCenterId.toString()
        sanctionOrder = center.senctionOrder
        AppUtil.savesanctionOrderPreference(requireContext(), center.senctionOrder)

        if (center.facilityStatus == "N") {
            // Create new RF
            val request = AddNewRFReq(
                appVersion = BuildConfig.VERSION_NAME,
                trainingCentre = centerId,
                sanctionOrder = sanctionOrder
            )
            viewModel.saveInitialResidentialFacility(request)
        } else {
            // Navigate to existing RF
            val action = RfCenterFragmentDirections.actionRfCenterFragmentToRfMultipleListFragment(
                centerId,
                sanctionOrder
            )
            findNavController().navigate(action)
        }
    }
}