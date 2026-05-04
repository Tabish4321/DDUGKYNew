package com.deendayalproject.fragments.ojt.ojt_SRLM

import SharedViewModel
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.adapter.BatchAdapter
import com.deendayalproject.adapter.OjtListByBatchAdapter
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentOJTSclectionSRLMBinding
import com.deendayalproject.model.request.ModulesCandidateByOjtRequest2
import com.deendayalproject.model.request.ModulesOJTBatchRequest
import com.deendayalproject.model.request.ModulesOJTSanctionOrderRequest
import com.deendayalproject.model.request.ModulesOJTTrainingCenterRequest
import com.deendayalproject.model.response.OJTBatchList
import com.deendayalproject.model.response.OJTSanctionOrderNumber
import com.deendayalproject.model.response.OJTTrainingCenterName
import com.deendayalproject.model.response.OjtListByBatch
import com.deendayalproject.network.SecurePreferenceManager.getToken
import com.deendayalproject.util.AppUtil
import kotlin.collections.forEach


class OJTSclectionSRLMFragment : BaseFragment<FragmentOJTSclectionSRLMBinding>(FragmentOJTSclectionSRLMBinding::inflate) {


    private lateinit var viewModel: SharedViewModel

    // ---------- Sanction Order ----------
    private var OJTSanctionOrderList: List<OJTSanctionOrderNumber> = emptyList()
    private val OJTSanctionOrderNumberList = kotlin.collections.ArrayList<String>()
    private lateinit var OJTSanctionOrderAdapter: ArrayAdapter<String>
    private var selectedOJTSanctionOrder: OJTSanctionOrderNumber? = null

    // ---------- Training Center ----------
    private var OJTTrainingCenterList: List<OJTTrainingCenterName> = emptyList()
    private val OJTTrainingCenterNameList = kotlin.collections.ArrayList<String>()
    private lateinit var OJTTrainingCenterAdapter: ArrayAdapter<String>
    private var selectedOJTTrainingCenter: OJTTrainingCenterName? = null
    private var batchId: String = ""
    private var isProfileVisible = false

    // ---------- Batch ----------
    private var OJTBatchList: List<OJTBatchList> = emptyList()
    private var OJTByBatchList: List<OjtListByBatch> = emptyList()
    private lateinit var batchAdapter: BatchAdapter
    private lateinit var ojtListBatchAdapter: OjtListByBatchAdapter
    private var selectedBatch: OJTBatchList? = null

    override fun initializeViews() {
        setupViewModel()
        setupSanctionOrder()
        setupTrainingCenter()
        setupBatchRecycler()
    }

    override fun loadInitialData() {
        fetchModulesSanction()
    }

    override fun setupObservers() {
        observeViewModel()
    }

    override fun setupClickListeners() {
        binding.toolbar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.toolbar.tvTitle.text = getStrings(R.string.on_job_training)
    }

    private fun setupViewModel() {
        // 👉 Lock screen in Portrait mode
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
    }

    private fun fetchModulesSanction() {
        val token = getToken(requireContext())
        val request = ModulesOJTSanctionOrderRequest(BuildConfig.VERSION_NAME)
        showProgressDialog("Loading...")
        viewModel.fetchOJTSanctionOrderNumber(request, "Bearer $token")
    }

    private fun setupSanctionOrder() {
        OJTSanctionOrderAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            OJTSanctionOrderNumberList
        )

        binding.OjtspinnerSanctionOrder.apply {
            setAdapter(OJTSanctionOrderAdapter)
            keyListener = null
            setOnClickListener { showDropDown() }
            setOnItemClickListener { _, _, position, _ ->
                selectedOJTSanctionOrder = OJTSanctionOrderList[position]
                clearTrainingCenter()
                clearBatchRecycler()
                fetchModulesTrainingCenter(selectedOJTSanctionOrder!!.sanctionOrder)
            }
        }
    }

    private fun fetchModulesTrainingCenter(sanctionOrder: String) {
        val token = getToken(requireContext())
        val request = ModulesOJTTrainingCenterRequest(
            BuildConfig.VERSION_NAME,
            sanctionOrder
        )
        showProgressDialog("Loading...")
        viewModel.fetchOJTTrainingCenter(request, "Bearer $token")
    }

    private fun setupTrainingCenter() {
        OJTTrainingCenterAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            OJTTrainingCenterNameList
        )

        binding.OjtspinnertvTrainingCenter.apply {
            setAdapter(OJTTrainingCenterAdapter)
            keyListener = null
            setOnClickListener { showDropDown() }
            setOnItemClickListener { _, _, position, _ ->
                selectedOJTTrainingCenter = OJTTrainingCenterList[position]
                clearBatchRecycler()
                fetchBatch(selectedOJTTrainingCenter!!.trainingCenterId.toString())
            }
        }
    }

    private fun setupBatchRecycler() {


        batchAdapter = BatchAdapter { batch ->


            isProfileVisible = !isProfileVisible
//
            binding.rvModules2.visibility = if (isProfileVisible) View.VISIBLE else View.GONE

            selectedBatch = batch



            val token = getToken(requireContext())
            val request = ModulesCandidateByOjtRequest2(
                BuildConfig.VERSION_NAME,
//                14.toString()
                batch.batchId.toString()
            )
            showProgressDialog("Loading...")
            viewModel.fetchOJTgetOjtListByBatch(request, "Bearer $token")





        }


        FetchOjtListByBatch()
        binding.rvModules.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = batchAdapter
        }



    }



    private fun FetchOjtListByBatch() {

        ojtListBatchAdapter = OjtListByBatchAdapter { batch ->
            AppUtil.saveOJTBatchIDPreference(requireContext(), batch.ojtplanId.toString())
            findNavController().navigate(R.id.action_fragmentOJTChild_to_OJTChild)

        }
        binding.rvModules2.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ojtListBatchAdapter
        }



    }
    private fun fetchBatch(trainingCenterId: String) {
        val token = getToken(requireContext())
        val request = ModulesOJTBatchRequest(
            BuildConfig.VERSION_NAME,
            trainingCenterId.toInt()
//           75
        )
        showProgressDialog("Loading...")
        viewModel.fetchOJTBatch(request, "Bearer $token")

    }

    private fun clearTrainingCenter() {
        selectedOJTTrainingCenter = null
        OJTTrainingCenterList = emptyList()
        OJTTrainingCenterNameList.clear()
        binding.OjtspinnertvTrainingCenter.setText("", false)
        OJTTrainingCenterAdapter.notifyDataSetChanged()
    }

    private fun clearBatchRecycler() {
        selectedBatch = null
        OJTBatchList = emptyList()
        OJTByBatchList = emptyList()
        ojtListBatchAdapter.setItems(emptyList())
        batchAdapter.setItems(emptyList())
    }

    private fun observeViewModel() {
        viewModel.ojtSanctionNo.observe(viewLifecycleOwner) { response ->
            response.onSuccess {
                dismissProgressDialog()
                OJTSanctionOrderList = it.wrappedList
                OJTSanctionOrderNumberList.clear()
                OJTSanctionOrderList.forEach { so ->
                    OJTSanctionOrderNumberList.add(so.sanctionOrder)
                }
                OJTSanctionOrderAdapter.notifyDataSetChanged()
            }
        }

        viewModel.ojtTrainingCenterRequest.observe(viewLifecycleOwner) { response ->
            response.onSuccess {
                dismissProgressDialog()
                OJTTrainingCenterList = it.wrappedList
                OJTTrainingCenterNameList.clear()
                OJTTrainingCenterList.forEach { tc ->
                    OJTTrainingCenterNameList.add(tc.trainingCenterName)
                }
                OJTTrainingCenterAdapter.notifyDataSetChanged()
            }
        }

        viewModel.BatchRequest.observe(viewLifecycleOwner) { response ->

            response.onSuccess { data ->

                dismissProgressDialog()
                if (data.responseDesc == "No data available.") {

                    Toast.makeText(
                        requireContext(),
                        data.responseDesc,
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    OJTBatchList = data.wrappedList
                    batchAdapter.setItems(OJTBatchList)
                }
                // ✅ Show responseDesc in Toast



            }

            response.onFailure { error ->
                dismissProgressDialog()
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.ListByBatch.observe(viewLifecycleOwner) { response ->
            response.onSuccess { data ->
                dismissProgressDialog()

                if (data.responseDesc == "No data available.") {

                    Toast.makeText(
                        requireContext(),
                        data.responseDesc,
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    OJTByBatchList = data.wrappedList
                    ojtListBatchAdapter.setItems(OJTByBatchList)
                }

            }

            response.onFailure { error ->
                dismissProgressDialog()
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        // 👉 Orientation unlock when dialog closed
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}
