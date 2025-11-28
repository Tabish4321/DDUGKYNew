package com.deendayalproject.fragments

import SharedViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.base.NoDataConfig
import com.deendayalproject.databinding.FragmentRfMultipleListBinding
import com.deendayalproject.databinding.ItemModifyListBinding
import com.deendayalproject.model.request.AddNewRFReq
import com.deendayalproject.model.request.ModifyRfList
import com.deendayalproject.model.response.RFModifyListItem

class RfMultipleListFragment : BaseFragment<FragmentRfMultipleListBinding>(
    FragmentRfMultipleListBinding::inflate
) {

    private lateinit var viewModel: SharedViewModel
    private var rfModifyList: MutableList<RFModifyListItem> = mutableListOf()
    var centerId = ""
    var sanctionOrder = ""
    var facilityId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        centerId = arguments?.getString("centerId").toString()
        sanctionOrder = arguments?.getString("sanctionOrder").toString()

        initializeViews()
        setupObservers()
        setupClickListeners()
        loadInitialData()
    }

    override fun initializeViews() {
        setupToolbar(
            binding.root,
            titleRes = R.string.residential_multiple_list,
            showBack = true,
            showLang = false,
            showProfile = false,
            backClick = { findNavController().navigateUp() }
        )
        setupRecyclerView()
    }

    override fun setupObservers() {
        observeViewModel()
        observeViewAddNewRF()
    }

    override fun setupClickListeners() {
        binding.btnAddResidentialFacility.setOnClickListener {
            addNewResidentialFacility()
        }
    }

    override fun loadInitialData() {
        showProgressBar()
        val request = ModifyRfList(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder = sanctionOrder
        )
        viewModel.getResidentialList(request)
    }

    private fun setupRecyclerView() {
        setupRecyclerView(
            recyclerView = binding.recyclerView,
            items = rfModifyList,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = { inflater, parent, _ ->
                ItemModifyListBinding.inflate(inflater, parent, false)
            },
            onBind = { item, itemBinding, position ->
                itemBinding.trainingCenterName.text = "Training Center Name: ${item.trainingCenterName}"
                itemBinding.senctionOrder.text = "Sanction Order: ${item.senctionOrder}"

                itemBinding.root.setOnClickListener {
                    onItemClick(item)
                }
            },
            noDataConfig = NoDataConfig(
                title = "No Residential Facilities",
                description = "No residential facilities found for this training center",
                iconRes = R.drawable.no_data
            )
        )
    }

    private fun onItemClick(selectedItem: RFModifyListItem) {
        val action =
            RfMultipleListFragmentDirections.actionRfMultipleListFragmentToFragmentResidentialFacility(
                selectedItem.trainingCenterId.toString(),
                selectedItem.senctionOrder.toString(),
                selectedItem.facilityId.toString(),
                selectedItem.remarks.toString(),
                selectedItem.status.toString()
            )
        findNavController().navigate(action)
    }

    private fun addNewResidentialFacility() {
        val request = AddNewRFReq(
            appVersion = BuildConfig.VERSION_NAME,
            trainingCentre = centerId,
            sanctionOrder = sanctionOrder
        )
        viewModel.saveInitialResidentialFacility(request)
        showProgressBar()
    }

    private fun observeViewModel() {
        viewModel.getResidentialList.observe(viewLifecycleOwner) { result ->
            hideProgressBar()

            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.let {
                        rfModifyList.clear()
                        rfModifyList.addAll(it)
                        updateRecyclerViewData(binding.recyclerView.id, rfModifyList)
                    }
                },
                onNoData = {
                    showToast("No data available.")
                    rfModifyList.clear()
                    updateRecyclerViewData(binding.recyclerView.id, rfModifyList)
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }
    }

    private fun observeViewAddNewRF() {
        viewModel.saveInitialResidentialFacility.observe(viewLifecycleOwner) { result ->
            hideProgressBar()

            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull(),
                onSuccess = { data ->
                    showSuccessToast("RF Added successfully!")
                    val action =
                        RfMultipleListFragmentDirections.actionRfMultipleListFragmentToFragmentResidentialFacility(
                            centerId,
                            sanctionOrder,
                            data?.facilityId.toString(),
                            "",
                            ""
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
    }

    // Maintain all original method names for compatibility
    fun showProgressBar() {
        showProgressDialog("Loading...")
    }

    fun hideProgressBar() {
        dismissProgressDialog()
    }

    // Optional helper methods if needed elsewhere
    fun updateRfList(newList: List<RFModifyListItem>) {
        rfModifyList.clear()
        rfModifyList.addAll(newList)
        updateRecyclerViewData(binding.recyclerView.id, rfModifyList)
    }

    fun getCurrentRfList(): List<RFModifyListItem> {
        return getRecyclerViewItems(binding.recyclerView.id)
    }

    fun clearRfList() {
        rfModifyList.clear()
        updateRecyclerViewData(binding.recyclerView.id, rfModifyList)
    }
}