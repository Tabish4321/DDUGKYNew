package com.deendayalproject.fragments

import SharedViewModel
import androidx.lifecycle.ViewModelProvider
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentCandidateAttendanceBinding


class CandidateAttendanceFragment : BaseFragment<FragmentCandidateAttendanceBinding>(
    FragmentCandidateAttendanceBinding::inflate) {

    private lateinit var viewModel: SharedViewModel


    override fun initializeViews() {
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
    }

    override fun setupObservers() {
        TODO("Not yet implemented")
    }

    override fun setupClickListeners() {
        TODO("Not yet implemented")
    }

    override fun loadInitialData() {
        TODO("Not yet implemented")
    }
}