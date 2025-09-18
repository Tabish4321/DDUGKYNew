package com.deendayalproject.fragments

import SharedViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.adapter.CenterAdapter
import com.deendayalproject.adapter.TrainingQAdapter
import com.deendayalproject.databinding.FragmentQTeamListBinding
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.util.AppUtil


class QTeamListFragment : Fragment() {
    private var _binding: FragmentQTeamListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: TrainingQAdapter




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentQTeamListBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        adapter = TrainingQAdapter(emptyList()) { center ->
            val action = QTeamListFragmentDirections.actionQTeamListFragmentToQTeamFormFragment(
                center.trainingCenterId.toString(),center.trainingCenterName,center.senctionOrder
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
        viewModel.fetchTrainingCenters(request, AppUtil.getSavedTokenPreference(requireContext()))

    }
    private fun observeViewModel() {
        viewModel.trainingCenters.observe(viewLifecycleOwner) { result ->
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