package com.deendayalproject.fragments

import SharedViewModel
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.base.NoDataConfig
import com.deendayalproject.databinding.FragmentSrlmListLayoutBinding
import com.deendayalproject.databinding.ItemQteamLayoutBinding
import com.deendayalproject.model.request.TrainingCenter
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.util.AppUtil

class SrlmVerListFragment : BaseFragment<FragmentSrlmListLayoutBinding>(
    FragmentSrlmListLayoutBinding::inflate
) {

    private lateinit var viewModel: SharedViewModel
    private var trainingCentersList: MutableList<TrainingCenter> = mutableListOf()


    override fun initializeViews() {
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        Log.d("FRAGMENT NAME", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━SrlmVerListFragment━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        setupToolbar(
            binding.root,
            titleRes = R.string.training_list,
            showBack = true,
            showLang = false,
            showProfile = false,
            backClick = { findNavController().navigateUp()}
        )
        setupRecyclerView()
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
        showProgressBar()
        val request = TrainingCenterRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.fetchSrlmTeamTrainingList(request, AppUtil.getSavedTokenPreference(requireContext()))
    }

    private fun setupRecyclerView() {
        setupRecyclerView(
            recyclerView = binding.recyclerView,
            items = trainingCentersList,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = { inflater, parent, _ ->
                ItemQteamLayoutBinding.inflate(inflater, parent, false)
            },
            onBind = { center, itemBinding, position ->
                itemBinding.trainingCenterName.text = "Training Center Name: ${center.trainingCenterName}"
                itemBinding.trainingCenterAddress.text = "Training Center Address: ${center.trainingCenterAddress}"
                itemBinding.senctionOrder.text = "Sanction Order: ${center.senctionOrder}"
                itemBinding.districtName.text = "District Name: ${center.districtName}"
                itemBinding.tcBoyCap.text = "Tc Male Capacity: ${center.tcMaleCapacity}"
                itemBinding.tcFemaleCap.text = "Tc Female Capacity: ${center.tcFemaleCapacity}"
                itemBinding.tcTotalCap.text = "Training Center Total Capacity: ${center.tcCapacity}"

                itemBinding.root.setOnClickListener {
                    onItemClick(center)
                }
            },
            noDataConfig = NoDataConfig(
                title = "No Training Centers",
                description = "No training centers available for verification",
                iconRes = R.drawable.no_data
            )
        )
    }

    private fun onItemClick(center: TrainingCenter) {
        val action = SrlmVerListFragmentDirections.actionSrlmVerListFragmentToSrlmVerificationForm(
            center.trainingCenterId.toString(),
            center.trainingCenterName,
            center.senctionOrder
        )
        findNavController().navigate(action)

        logFragmentEvent("Training_Center_Selected", center.trainingCenterName)
    }

    private fun observeViewModel() {
        viewModel.trainingCenters.observe(viewLifecycleOwner) { result ->
            hideProgressBar()

            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.let {
                        trainingCentersList.clear()
                        trainingCentersList.addAll(it)
                        updateRecyclerViewData(binding.recyclerView.id, trainingCentersList)
                    }
                },
                onNoData = {
                    showToast("No data available.")
                    trainingCentersList.clear()
                    updateRecyclerViewData(binding.recyclerView.id, trainingCentersList)
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    // Maintain original method names for compatibility
    fun showProgressBar() {
        showProgressDialog("Loading training centers...")
    }

    fun hideProgressBar() {
        dismissProgressDialog()
    }

    // Optional helper methods
//    fun updateTrainingCentersData(newList: List<TrainingCenter>) {
//        trainingCentersList.clear()
//        trainingCentersList.addAll(newList)
//        updateRecyclerViewData(binding.recyclerView.id, trainingCentersList)
//    }
//
//    fun getCurrentTrainingCenters(): List<TrainingCenter> {
//        return getRecyclerViewItems(binding.recyclerView.id)
//    }
//
//    fun clearTrainingCenters() {
//        trainingCentersList.clear()
//        updateRecyclerViewData(binding.recyclerView.id, trainingCentersList)
//    }
//
//    fun filterTrainingCenters(query: String) {
//        val filteredList = if (query.isEmpty()) {
//            trainingCentersList
//        } else {
//            trainingCentersList.filter {
//                it.trainingCenterName.contains(query, ignoreCase = true) ||
//                        it.districtName.contains(query, ignoreCase = true) ||
//                        it.senctionOrder.contains(query, ignoreCase = true)
//            }
//        }
//        updateRecyclerViewData(binding.recyclerView.id, filteredList)
//    }
}