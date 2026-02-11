package com.deendayalproject.fragments

import SharedViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.base.NoDataConfig
import com.deendayalproject.databinding.RfQteamListFragmentBinding
import com.deendayalproject.databinding.RfItemQteamLayoutBinding
import com.deendayalproject.model.request.ResidentialFacilityQTeamRequest
import com.deendayalproject.model.response.RfCenter
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.ModernProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RFQTeamListFragment : BaseFragment<RfQteamListFragmentBinding>(
    bindingInflater = RfQteamListFragmentBinding::inflate
) {

    // Using viewModels delegate instead of ViewModelProvider
    private val viewModel: SharedViewModel by viewModels()

    private var centerId: String = ""
    private var sanctionOrder: String = ""

    // No need for custom adapter anymore

    override fun initializeViews() {
        Log.d("FRAGMENT NAME", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━RFQTeamListFragment━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        // Get arguments safely
        arguments?.let { bundle ->
            centerId = bundle.getString("centerId").orEmpty()
            sanctionOrder = bundle.getString("sanctionOrder").orEmpty()
        }

        setupToolbar(
            root = binding.root,
            titleRes = R.string.residential_facility_q_team,
            showBack = true,
            showLang = false,
            showProfile = false,
            backClick = {findNavController().navigateUp()}
        )

        // Setup recycler view using BaseFragment's helper
        setupRecyclerView(
            recyclerView = binding.recyclerView,
            items = emptyList<RfCenter>(),
            noDataConfig = NoDataConfig(
                title = getString(R.string.no_data_available),
                description = getString(R.string.no_centers_found),
                iconRes = R.drawable.no_data
            ),
            bindingInflater = { inflater, parent, attachToParent ->
                RfItemQteamLayoutBinding.inflate(inflater, parent, attachToParent)
            },
            onBind = { center, binding, position ->
                bindCenterItem(center, binding)
            },
            onItemClick = { center, position ->
                navigateToForm(center)
            }
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
        // Make API call
        fetchResidentialFacilityQTeamList()
    }

    private fun fetchResidentialFacilityQTeamList() {
        val token = AppUtil.getSavedTokenPreference(requireContext()).orEmpty()
        val loginId = AppUtil.getSavedLoginIdPreference(requireContext()).orEmpty()
        if (loginId.isEmpty()) {
            showErrorToast("Login ID not found")
            handleSessionExpired()
            return
        }
        val request = ResidentialFacilityQTeamRequest(
            loginId = loginId,
            appVersion = BuildConfig.VERSION_NAME,
            imeiNo = AppUtil.getAndroidId(requireContext()).orEmpty()
        )
        viewModel.fetchResidentialFacilityQTeamList(request, token)
    }

    private fun observeViewModel() {
        viewModel.trainingRfCenters.observe(viewLifecycleOwner) { result ->
            lifecycleScope.launch {
                result.onSuccess { response ->
                    handleApiResponse(
                        responseCode = response.responseCode ?: 0,
                        data = response.wrappedList,
                        onSuccess = { centers ->
                            // Update recycler view with new data
                            updateRecyclerViewData(
                                recyclerViewId = binding.recyclerView.id,
                                newItems = centers ?: emptyList()
                            )

                            // Show success message if no data
                            if (centers.isNullOrEmpty()) {
                                showToast("No data available.")
                            }
                        },
                        onNoData = {
                            updateRecyclerViewData(
                                recyclerViewId = binding.recyclerView.id,
                                newItems = emptyList<RfCenter>()
                            )
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

                result.onFailure { throwable ->
                    showErrorToast("Failed: ${throwable.message ?: "Unknown error"}")
                    logCrashlyticsError("observeViewModel", Exception(throwable))
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showProgressDialog("Loading centers...")
            } else {
                dismissProgressDialog()
            }
        }
    }

    private fun bindCenterItem(center: RfCenter, binding: RfItemQteamLayoutBinding) {
        binding.trainingCenterName.text = safeText(center.trainingCenterName)
        binding.trainingCenterAddress.text = safeText(center.trainingCenterAddress)
        binding.senctionOrder.text = safeText(center.senctionOrder)
        binding.districtName.text =safeText(center.districtName)
        binding.totalCapacity.text=center.finalRfCapacity?:"N/A"
        binding.residenctialIcon.setImageResource(
            if (center.residentialType.equals("Male"))
                R.drawable.baseline_boy_24
            else
                R.drawable.baseline_girl_24
        )
        binding.residenctialType.text= center.residentialType
    }
    private fun navigateToForm(center: RfCenter) {
        try {
            val action = RFQTeamListFragmentDirections.actionRFQTeamListFragmentToRFQTeamFormFragment(
                    center.trainingCenterId.toString(),
                    center.trainingCenterName,
                    center.senctionOrder,
                    center.facilityId
                )
            findNavController().navigate(action)
           // findNavController().navigate(action)
        } catch (e: Exception) {
            logCrashlyticsError("navigateToForm", e)
            showErrorToast("Failed to navigate: ${e.message}")
        }
    }

    override fun onDestroyView() {
        clearRecyclerViewData(binding.recyclerView.id)
        super.onDestroyView()
    }
}