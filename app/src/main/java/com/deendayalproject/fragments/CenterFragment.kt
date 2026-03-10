package com.deendayalproject.fragments

import SharedViewModel
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.databinding.FragmentCenterBinding
import com.deendayalproject.databinding.ItemTrainingCenterBinding
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.request.TrainingCenter
import com.deendayalproject.util.AppUtil
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.util.ProgressDialogUtil
import com.deendayalproject.util.gone
import java.lang.System.exit

class CenterFragment : BaseFragment<FragmentCenterBinding>(
    FragmentCenterBinding::inflate
) {
    private lateinit var viewModel: SharedViewModel

    override fun initializeViews() {
        Log.d("FRAGMENT NAME", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━CenterFragment━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        setUpAdapter()
    }

    override fun setupObservers() {
        observeViewModel()
    }

    override fun setupClickListeners() {
//        binding.backButton.setOnClickListener {
//            findNavController().navigateUp()
//        }
    }

    fun setUpAdapter(){
        setupToolbar(
            root = binding.root,
            titleRes = R.string.training_center,
            showBack = true,
            showLang = false,
            showProfile = false,
            backClick = {findNavController().navigateUp()}
        )

        setupRecyclerView(
            recyclerView = binding.recyclerView,
            items = emptyList<TrainingCenter>(),
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = { inflater, parent, attachToParent ->
                ItemTrainingCenterBinding.inflate(inflater, parent, attachToParent)
            },
            onBind = { center, binding, position ->
                binding.trainingCenterName.text = " ${center.trainingCenterName}"
                binding.trainingCenterAddress.text = "${center.trainingCenterAddress}"
                binding.senctionOrder.text = " ${center.senctionOrder}"
                binding.districtName.text = " ${center.districtName}"
                binding.maleCapacityContainer.gone()
                binding.femaleCapacityContainer.gone()
                binding.totalCapacityContainer.gone()
            },
            onItemClick = { center, position ->
                ProgressDialogUtil.showProgressDialog(requireContext(),
                    getString(R.string.please_wait))

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
                ProgressDialogUtil.dismissProgressDialog()

            },
            noDataTitle = getString(R.string.no_training_centers_available),
            noDataDescription = getString(R.string.no_training_centers_available_at_the_moment),
        )
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
                        showToast(getString(R.string.no_data_available))
                    },
                    onUpgradeRequired = {
                        showToast(getString(R.string.please_upgrade_your_app))
                    },
                    onSessionExpired = {
                        handleSessionExpired()
                    }
                )
            }
            result.onFailure { exception ->

                showErrorToast(": ${exception.message ?: getString(R.string.failed)}")

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