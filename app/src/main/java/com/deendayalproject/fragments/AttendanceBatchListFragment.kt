package com.deendayalproject.fragments

import SharedViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.adapter.AttendanceBatchAdapter
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentAttendanceBatchListBinding
import com.deendayalproject.model.response.AttendanceBatch
import com.deendayalproject.model.response.AttendanceBatchRes


class AttendanceBatchListFragment  : BaseFragment<FragmentAttendanceBatchListBinding>(
    FragmentAttendanceBatchListBinding::inflate) {

    private lateinit var viewModel: SharedViewModel

    private lateinit var batchAdapter: AttendanceBatchAdapter
    private var AttendanceBatchList = mutableListOf<AttendanceBatch>()


    override fun initializeViews() {
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        setupRecyclerView()

        val response = AttendanceBatchDummy.response
        AttendanceBatchList.clear()
        for (x in response.wrappedList)
        {

            AttendanceBatchList.add(x)
        }






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
    object AttendanceBatchDummy {

        val response = AttendanceBatchRes(
            wrappedList = listOf(
                AttendanceBatch("Batch A", "REG001", 101),
                AttendanceBatch("Batch B", "REG002", 102)
            ),
            responseCode = 200,
            responseDesc = "Success",
            responseMsg = "Static data loaded",
            appCode = null
        )
    }
}