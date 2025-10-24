package com.deendayalproject.fragments

import CenterAdapter
import SharedViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.adapter.RfAdapterList
import com.deendayalproject.databinding.FragmentCenterBinding
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.util.AppUtil

class RfCenterFragment : Fragment() {

    private var _binding: FragmentCenterBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: RfAdapterList
    private val progress: AlertDialog? by lazy {
        AppUtil.getProgressDialog(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCenterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        adapter = RfAdapterList(emptyList()) { selectedItem ->
            val bundle = Bundle().apply {
                putSerializable("centerItem", selectedItem)
            }

            findNavController().navigate(
                R.id.action_rfcenterFragment_to_fragment_residential_facility,
                bundle
            )
        }




        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        observeViewModel()


        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val request = TrainingCenterRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.fetchRfList(request, AppUtil.getSavedTokenPreference(requireContext()))
    }

    private fun observeViewModel() {
        viewModel.rfTrainingCenters.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> adapter.updateData(it.wrappedList ?: emptyList())
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
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
