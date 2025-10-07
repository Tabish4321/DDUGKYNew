package com.deendayalproject.fragments

import SharedViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.adapter.RfSrlmAdapter
import com.deendayalproject.databinding.RfSrlmListFragmentBinding
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.util.AppUtil

class RFSrlmListFragment : Fragment() {
    private var _binding: RfSrlmListFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: RfSrlmAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = RfSrlmListFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        adapter = RfSrlmAdapter(emptyList()) { center ->
            val action =
                RFSrlmListFragmentDirections.actionRFSrlmListFragmentToRFSRLMFormFragment(
                    center.trainingCenterId.toString(),
                    center.trainingCenterName,
                    center.senctionOrder
                )
            findNavController().navigate(action)
        }

        binding.backButton.setOnClickListener {

            findNavController().navigateUp()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        observeViewModel()

        val request = TrainingCenterRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.fetchSrlmTeamTrainingList(
            request,
            AppUtil.getSavedTokenPreference(requireContext())
        )

    }

    private fun observeViewModel() {
        viewModel.trainingCenters.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> adapter.updateData(it.wrappedList ?: emptyList())
                    202 -> Toast.makeText(
                        requireContext(),
                        "No data available.",
                        Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(),
                        "Please upgrade your app.",
                        Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}