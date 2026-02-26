package com.deendayalproject.fragments


import SharedViewModel
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.adapter.AttendanceCandidateAdapter
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentAttendanceCandidateListBinding
import com.deendayalproject.model.request.AttendanceBatchListReq
import com.deendayalproject.model.request.AttendanceCandidateListReq
import com.deendayalproject.model.response.AttendanceCandidateRes
import com.deendayalproject.model.response.Candidate
import com.deendayalproject.util.AppUtil
import kotlinx.coroutines.launch
import kotlin.getValue


class AttendanceCandidateListFragment : BaseFragment<FragmentAttendanceCandidateListBinding>(
        FragmentAttendanceCandidateListBinding::inflate
    ) {
        private lateinit var viewModel: SharedViewModel

        private lateinit var candidateListAdapter: AttendanceCandidateAdapter
        private var AttendanceCandidateList = mutableListOf<Candidate>()

        private var batchId = 0
        private var batchName = ""
        private var batchRegNo = ""



        override fun initializeViews() {
            viewModel = ViewModelProvider(this)[SharedViewModel::class.java]



            batchId = arguments?.getInt("batchId",0)!!
            batchName = arguments?.getString("batchName").toString()
            batchRegNo = arguments?.getString("batchRegNo").toString()

            setupRecyclerView()
            collectCandidateListRes()

            viewModel.getAttendanceCandidateListAPI(
                AttendanceCandidateListReq(batchId,
                    BuildConfig.VERSION_NAME
                ), ""
            )

            showProgressDialog("Loading...")


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

            candidateListAdapter = AttendanceCandidateAdapter(AttendanceCandidateList, candidateRegNo = batchRegNo)
            binding.recyclerViewCandidates.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewCandidates.adapter = candidateListAdapter
        }

    private fun collectCandidateListRes() {
        lifecycleScope.launch {
            viewModel.getAttendanceCandidateListAPI.observe(viewLifecycleOwner) { it ->
                it.onSuccess { response ->
                    dismissProgressDialog()

                    when (response.responseCode) {
                        200 -> {

                            AttendanceCandidateList.clear()

                            for (x in response.wrappedList)
                            {

                                AttendanceCandidateList.add(x)
                            }
                            candidateListAdapter.notifyDataSetChanged()

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