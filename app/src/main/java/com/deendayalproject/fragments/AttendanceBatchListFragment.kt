package com.deendayalproject.fragments

import SharedViewModel
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.adapter.AttendanceBatchAdapter
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentAttendanceBatchListBinding
import com.deendayalproject.model.request.AttendanceBatchListReq
import com.deendayalproject.model.request.WardReq
import com.deendayalproject.model.response.AttendanceBatch
import com.deendayalproject.model.response.AttendanceBatchRes
import com.deendayalproject.model.response.WardItem
import com.deendayalproject.util.AppUtil
import kotlinx.coroutines.launch
import kotlin.collections.forEach
import kotlin.collections.set


class AttendanceBatchListFragment  : BaseFragment<FragmentAttendanceBatchListBinding>(
    FragmentAttendanceBatchListBinding::inflate) {

    private lateinit var viewModel: SharedViewModel

    private lateinit var batchAdapter: AttendanceBatchAdapter
    private var AttendanceBatchList = mutableListOf<AttendanceBatch>()


    override fun initializeViews() {
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        setupRecyclerView()
        collectBatchListRes()


        viewModel.getAttendanceBatchListAPI(
            AttendanceBatchListReq(
                BuildConfig.VERSION_NAME
            ), AppUtil.getSavedTokenPreference(requireContext())
        )

        showProgressDialog("Loading...")





    }

    override fun setupObservers() {
    }

    override fun setupClickListeners() {

        binding.toolbar.btnBack.setOnClickListener {

            findNavController().navigateUp()
        }
        binding.toolbar.tvTitle.text= "Batch List"

    }

    override fun loadInitialData() {
    }


    private fun setupRecyclerView() {
        batchAdapter = AttendanceBatchAdapter(AttendanceBatchList)
        binding.recyclerViewBatches.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewBatches.adapter = batchAdapter

    }

    private fun collectBatchListRes() {
        lifecycleScope.launch {
            viewModel.getAttendanceBatchListAPI.observe(viewLifecycleOwner) { it ->
                it.onSuccess { response ->
                    dismissProgressDialog()

                    when (response.responseCode) {
                        200 -> {

                            AttendanceBatchList.clear()
                            for (x in response.wrappedList)
                            {
                                AttendanceBatchList.add(x)
                            }
                            batchAdapter.notifyDataSetChanged()

                        }

                        //  populateSpinnerVillage((response.wrappedList ?: emptyList()) as ArrayList<VillageModel?>, spinnerSelectULB )

                        202 -> Toast.makeText(
                            requireContext(), "No data available.", Toast.LENGTH_SHORT
                        ).show()

                        301 -> Toast.makeText(
                            requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                        ).show()

                        401 -> AppUtil.showSessionExpiredDialog(
                            findNavController(), requireContext()
                        )
                    }
                }
                it.onFailure {
                    dismissProgressDialog()

                    Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


}