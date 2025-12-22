package com.deendayalproject.fragments

import SharedViewModel
import androidx.lifecycle.ViewModelProvider
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentAttendanceBatchListBinding


class AttendanceBatchListFragment  : BaseFragment<FragmentAttendanceBatchListBinding>(
    FragmentAttendanceBatchListBinding::inflate) {

    private lateinit var viewModel: SharedViewModel


    override fun initializeViews() {
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
    }

    override fun setupObservers() {
    }

    override fun setupClickListeners() {
    }

    override fun loadInitialData() {
    }
}