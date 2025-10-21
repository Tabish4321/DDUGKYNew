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
import com.deendayalproject.adapter.RfQAdapter
import com.deendayalproject.adapter.RfSrlmAdapter
import com.deendayalproject.databinding.RfQteamListFragmentBinding
import com.deendayalproject.model.request.ResidentialFacilityQTeamRequest
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.util.AppUtil

class RFQTeamListFragment : Fragment() {
    private var _binding: RfQteamListFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: RfQAdapter


    private var centerId: String = ""
    private var sanctionOrder: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = RfQteamListFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        adapter = RfQAdapter(emptyList()) { center ->
            val action =
                RFQTeamListFragmentDirections.actionRFQTeamListFragmentToRFQTeamFormFragment(
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
        centerId = arguments?.getString("centerId").toString()
        sanctionOrder = arguments?.getString("sanctionOrder").toString()
        observeViewModel()

        val request = ResidentialFacilityQTeamRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.fetchResidentialFacilityQTeamList(request,AppUtil.getSavedTokenPreference(requireContext())
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