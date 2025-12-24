package com.deendayalproject.fragments


import SharedViewModel
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.adapter.AttendanceCandidateAdapter
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentAttendanceCandidateListBinding
import com.deendayalproject.model.response.AttendanceCandidateRes
import com.deendayalproject.model.response.Candidate
import kotlin.getValue


class AttendanceCandidateListFragment : BaseFragment<FragmentAttendanceCandidateListBinding>(
        FragmentAttendanceCandidateListBinding::inflate
    ) {
        private lateinit var viewModel: SharedViewModel

        private lateinit var candidateListAdapter: AttendanceCandidateAdapter
        private var AttendanceCandidateList = mutableListOf<Candidate>()

        private var batchId = ""
        private var batchName = ""



        override fun initializeViews() {
            viewModel = ViewModelProvider(this)[SharedViewModel::class.java]


            batchId = arguments?.getString("batchId").toString()
            batchName = arguments?.getString("batchName").toString()
            val response = AttendanceCandidateDummy.response

            setupRecyclerView()

            AttendanceCandidateList.clear()

            for (x in response.wrappedList)
            {

                AttendanceCandidateList.add(x)
            }

        }

        override fun setupObservers() {
        }

        override fun setupClickListeners() {

            binding.toolbar.btnBack.setOnClickListener {

                findNavController().navigateUp()
            }
            binding.toolbar.tvTitle.text= "Candidate List"
        }

        override fun loadInitialData() {
        }

        private fun setupRecyclerView() {

            candidateListAdapter = AttendanceCandidateAdapter(AttendanceCandidateList)
            binding.recyclerViewCandidates.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewCandidates.adapter = candidateListAdapter
        }

    object AttendanceCandidateDummy {

        val response = AttendanceCandidateRes(
            wrappedList = listOf(
                Candidate(
                    "Rahul Kumar",
                    101,
                    "9876543210",
                    1,
                    "CAND001",
                    "Patna, Bihar",
                    "1998-05-12",
                    "rahul@gmail.com",
                    "Male",
                    "XXXX-XXXX-1234",
                    ""
                ),
                Candidate(
                    "Anita Devi",
                    102,
                    "9123456780",
                    1,
                    "CAND002",
                    "Gaya, Bihar",
                    "1999-09-20",
                    "anita@gmail.com",
                    "Female",
                    "XXXX-XXXX-5678",
                    ""
                )
            ),
            responseCode = 200,
            responseDesc = "Success",
            responseMsg = "Static data",
            appCode = null
        )
    }

}