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
import com.deendayalproject.adapter.RfModifyListAdapter
import com.deendayalproject.databinding.FragmentRfMultipleListBinding
import com.deendayalproject.model.request.AddNewRFReq
import com.deendayalproject.model.request.ModifyRfList
import com.deendayalproject.model.response.TrainingCenterItem
import com.deendayalproject.util.AppUtil

class RfMultipleListFragment : Fragment() {

    private var _binding: FragmentRfMultipleListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: RfModifyListAdapter
    var centerId = ""
    var sanctionOrder = ""
    var facilityId = ""

    private val progress: androidx.appcompat.app.AlertDialog? by lazy {
        AppUtil.getProgressDialog(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRfMultipleListBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]


        centerId = arguments?.getString("centerId").toString()
        sanctionOrder = arguments?.getString("sanctionOrder").toString()

        adapter = RfModifyListAdapter(emptyList()) { selectedItem ->


            val action =
                RfMultipleListFragmentDirections.actionRfMultipleListFragmentToFragmentResidentialFacility(
                    selectedItem.trainingCenterId.toString(),
                    selectedItem.senctionOrder.toString(),
                    selectedItem.facilityId.toString(),
                    selectedItem.remarks.toString(),
                    selectedItem.status.toString()
                )
            findNavController().navigate(action)
        }

        binding.btnAddResidentialFacility.setOnClickListener {
            val request = AddNewRFReq(
                appVersion = BuildConfig.VERSION_NAME,
                trainingCentre = centerId,
                sanctionOrder = sanctionOrder
            )
            viewModel.saveInitialResidentialFacility(request)

            observeViewAddNewRF()
         //   showProgressBar()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        observeViewModel()
      //  showProgressBar()


        binding.toolbar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.tvTitle.setText(R.string.residential_multiple_list)



        val request = ModifyRfList(
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder = sanctionOrder
        )
        viewModel.getResidentialList(request)


    }

    private fun observeViewModel() {
        hideProgressBar()
        viewModel.getResidentialList.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        hideProgressBar()
                        adapter.updateData(emptyList())
                        adapter.updateData(mutableListOf())
                        adapter.updateData(it.wrappedList ?: emptyList())
                    }


                    202 -> {
                        hideProgressBar()

                        Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT)
                            .show()
                    }


                    301 -> {
                        hideProgressBar()

                        Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }


                    401 -> {
                        hideProgressBar()
                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    }


                }
            }
            result.onFailure {

                {
                    hideProgressBar()
                    Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT)
                        .show()


                }

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

    private fun observeViewAddNewRF() {
        viewModel.saveInitialResidentialFacility.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        hideProgressBar()

                        Toast.makeText(requireContext(), "Rf Added.", Toast.LENGTH_SHORT).show()
                        val action =
                            RfMultipleListFragmentDirections.actionRfMultipleListFragmentToFragmentResidentialFacility(
                                centerId, sanctionOrder, it.facilityId.toString(), "", ""
                            )
                        findNavController().navigate(action)
                    }

                    202 -> {
                        hideProgressBar()
                        adapter.updateData(emptyList())
                        adapter.updateData(mutableListOf())
                        Toast.makeText(
                            requireContext(),
                            "No data available.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    301 -> {
                        hideProgressBar()

                        Toast.makeText(
                            requireContext(),
                            "Please upgrade your app.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    401 -> {

                        hideProgressBar()
                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())

                    }


                }
            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }


    }


    fun showProgressBar() {
        if (context != null && isAdded && progress?.isShowing == false) {
            progress?.show()
        }
    }

    //
    fun hideProgressBar() {
        if (progress?.isShowing == true) {
            progress?.dismiss()
        }


    }
}