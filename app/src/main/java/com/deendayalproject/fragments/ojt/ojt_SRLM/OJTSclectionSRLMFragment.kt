package com.deendayalproject.fragments.ojt.ojt_SRLM

import SharedViewModel
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.adapter.BatchAdapter
import com.deendayalproject.adapter.BatchSRLMAdapter
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
import com.deendayalproject.model.response.OJT_OjtVerifiedTrainingCenter_Res
import com.deendayalproject.model.response.OJT_VerifiedBatchListSRLM_Res
import com.deendayalproject.model.response.OjtListByBatch
import com.deendayalproject.model.response.OjtSRLMRes
import com.deendayalproject.model.response.OjtVerifiedTrainingCenter
import com.deendayalproject.model.response.SRLMRes
import com.deendayalproject.model.response.VerifiedBatchListSRLM
import com.deendayalproject.network.SecurePreferenceManager.getToken
import com.deendayalproject.util.AppUtil
import kotlin.collections.forEach


class OJTSclectionSRLMFragment : BaseFragment<FragmentOJTSclectionSRLMBinding>(FragmentOJTSclectionSRLMBinding::inflate) {


    private lateinit var viewModel: SharedViewModel

    // ---------- Sanction Order ----------
    private var SanctionOrderListOjt: List<SRLMRes> = emptyList()
    private lateinit var OJTSanctionOrderListAdapter: ArrayAdapter<String>
    private val OJTSanctionOrderNumberList = kotlin.collections.ArrayList<String>()
    private var selectedOJTSanctionOrder: SRLMRes? = null

    // ---------- Training Center ----------


    private var OJTTrainingCenterList: List<OjtVerifiedTrainingCenter> = emptyList()
    private val OJTTrainingCenterNameList = kotlin.collections.ArrayList<String>()
    private lateinit var OJTTrainingCenterAdapter: ArrayAdapter<String>
    private var selectedOJTTrainingCenter: OjtVerifiedTrainingCenter? = null
    private var isProfileVisible = false

    // ---------- Batch ----------
    private var OJTBatchList: List<VerifiedBatchListSRLM> = emptyList()
    private var OJTByBatchList: List<OjtListByBatch> = emptyList()
    private lateinit var batchAdapter: BatchSRLMAdapter
    private lateinit var ojtListBatchAdapter: OjtListByBatchAdapter
    private var selectedBatch: VerifiedBatchListSRLM? = null

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
        binding.toolbar.tvTitle.text = getStrings(R.string.srlm_on_job_training)
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
        viewModel.fetchgetSanctionOrderListOjt(request, "Bearer $token")

    }






    private fun fetchModulesTrainingCenters(sanctionOrder: String) {
        val token = getToken(requireContext())
        val request = ModulesOJTTrainingCenterRequest(
            BuildConfig.VERSION_NAME,
            sanctionOrder
        )
        showProgressDialog("Loading...")
        viewModel.fetchgetCompOjtTrainingCenter(request, "Bearer $token")
//        viewModel.fetchOJTTrainingCenter(request, "Bearer $token")









    }

    private fun setupSanctionOrder() {



        OJTSanctionOrderListAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            OJTSanctionOrderNumberList
        )

        binding.OjtspinnerPiaName.apply {
            setAdapter(OJTSanctionOrderListAdapter)
            keyListener = null

            setOnClickListener {
                if (OJTSanctionOrderListAdapter.count > 0) {
                    showDropDown()
                } else {
                    Toast.makeText(context, "No data available", Toast.LENGTH_SHORT).show()
                }
            }

            setOnItemClickListener { parent, _, position, _ ->

                val selectedPiaName = parent.getItemAtPosition(position).toString()

                val item = SanctionOrderListOjt.find {
                    it.piaName.trim() == selectedPiaName.trim()
                }

                if (item != null) {

                    selectedOJTSanctionOrder = item

                    clearTrainingCenter()
                    clearBatchRecycler()



                    fetchModulesTrainingCenters(item.sanctionOrder)

                } else {
                    Toast.makeText(context, "Data mismatch!", Toast.LENGTH_SHORT).show()
                }
            }
        }


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


            setOnClickListener {
                if (OJTTrainingCenterAdapter.count > 0) {
                    showDropDown()
                } else {
                    Toast.makeText(context, "No data available", Toast.LENGTH_SHORT).show()
                }
            }

            setOnItemClickListener { parent, _, position, _ ->
                val selectedtrainingCenterName = parent.getItemAtPosition(position).toString()




                val item = OJTTrainingCenterList.find {
                    it.traininrCenterName.trim() == selectedtrainingCenterName.trim()
                }
//
                if (item != null) {
                    clearBatchRecycler()
                    fetchBatch(item.trainingCenterId.toString())
                }
               else {
                }
            }
        }
    }

    private fun setupBatchRecycler() {


        batchAdapter = BatchSRLMAdapter { batch ->


            isProfileVisible = !isProfileVisible
//
            binding.rvModules2.visibility = if (isProfileVisible) View.VISIBLE else View.GONE

            selectedBatch = batch



            val token = getToken(requireContext())
            val request = ModulesCandidateByOjtRequest2(
                BuildConfig.VERSION_NAME,
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
        viewModel.fetchgetVerifiedBatchList(request, "Bearer $token")
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


        viewModel.SanctionOrderListOjt.observe(viewLifecycleOwner) { response ->

            response.onSuccess { res ->

                dismissProgressDialog()

                if (res.responseCode == 200) {

                    SanctionOrderListOjt = res.wrappedList

                    val piaList = SanctionOrderListOjt.map { it.piaName }

                    OJTSanctionOrderListAdapter.clear()
                    OJTSanctionOrderListAdapter.addAll(piaList)
                    OJTSanctionOrderListAdapter.notifyDataSetChanged()

                    // 🔥 Debug
                    Log.d("DEBUG", "List Size: ${piaList.size}")

                } else {
                    Toast.makeText(requireContext(), res.responseDesc, Toast.LENGTH_SHORT).show()
                }
            }

            response.onFailure {
                dismissProgressDialog()
                Toast.makeText(requireContext(), it.message ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }


        viewModel.CompOjtTrainingCenter.observe(viewLifecycleOwner) { response ->


            response.onSuccess { res ->
                dismissProgressDialog()
                if (res.responseCode == 200) {
                    OJTTrainingCenterList = res.wrappedList

                    val traininrCenterName = OJTTrainingCenterList.map { it.traininrCenterName }
                    OJTTrainingCenterAdapter.clear()
                    OJTTrainingCenterAdapter.addAll(traininrCenterName)
                    OJTTrainingCenterAdapter.notifyDataSetChanged()
                } else {
                    // 🔥 Dynamic message from API
                    res.responseDesc?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }

                    // List clear (optional)
                    OJTTrainingCenterNameList.clear()
                    OJTTrainingCenterAdapter.notifyDataSetChanged()
                }
            }
            response.onFailure {
                dismissProgressDialog()

                // अगर failure में भी dynamic message है
                val errorMsg = it.message ?: "Error"
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.VerifiedBatchList.observe(viewLifecycleOwner) { response ->

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
