package com.deendayalproject.fragments.ojt

import SharedViewModel
import android.content.pm.ActivityInfo
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.adapter.ChildAdapter
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.ChildFragmentBinding
import com.deendayalproject.model.request.ModulesCandidateByOjtRequest
import com.deendayalproject.model.request.ModulesOJTCompleteOjtRequest
import com.deendayalproject.model.response.OJTList
import com.deendayalproject.model.response.OjtBatchRes

import com.deendayalproject.util.AppUtil
import com.google.gson.Gson
import retrofit2.HttpException
import kotlin.apply
import kotlin.collections.isNullOrEmpty
import kotlin.jvm.java
import kotlin.onFailure
import kotlin.onSuccess

class OJTChildFragment : BaseFragment<ChildFragmentBinding>(ChildFragmentBinding::inflate) {

    private lateinit var viewModel: SharedViewModel
    // ---------- Batch ----------
    private var CompleteOJTList: List<OJTList> = emptyList()

    private var OJTList: List<OjtBatchRes> = emptyList()
    private lateinit var childAdapter: ChildAdapter
//    private var selectedBatch: OJTList? = null
    private var selectedBatch: OjtBatchRes? = null
    var verificationStatus : String? = null



    override fun initializeViews() {
        setupViewModel()

//        setupTrainingCenter()
        setupBatchRecycler()
//        fetchBatch()
    }

    override fun loadInitialData() {
//        fetchModulesSanction()
        fetchBatch()
    }

    override fun setupObservers() {
        observeViewModel()
    }

    override fun setupClickListeners() {
        binding.toolbar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.toolbar.tvTitle.text = getString(R.string.ojt_candidate_list)
    }

    private fun setupViewModel() {
        // 👉 Lock screen in Portrait mode
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
    }









    private fun setupBatchRecycler() {


        childAdapter = ChildAdapter { batch ->
//            selectedBatch = batch

            verificationStatus=batch.verificationStatus



            val token = AppUtil.getSavedTokenPreference(requireContext())
            val request = ModulesOJTCompleteOjtRequest(
                BuildConfig.VERSION_NAME,
                batch.candidateId
            )








            showProgressDialog("Loading...")
            viewModel.fetchOJTgetCompleteOjt(request, "Bearer $token")

        }


//        }

        binding.rvModules.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = childAdapter
        }



    }



//    private fun fetchBatch(trainingCenterId: String) {
    private fun fetchBatch() {
        val token = AppUtil.getSavedTokenPreference(requireContext())
        val request = ModulesCandidateByOjtRequest(
            BuildConfig.VERSION_NAME,
            AppUtil.getSavedOJTBatchIDPreference(requireContext())
        )
        showProgressDialog("Loading...")
        viewModel.fetchCandidateByOjtBy(request, "Bearer $token")
    }




    private fun clearBatchRecycler() {
        selectedBatch = null
        OJTList = emptyList()
        childAdapter.setItems(emptyList())
    }

    private fun observeViewModel() {
        viewModel.CompleteOjt.observe(viewLifecycleOwner) { response ->

            response.onSuccess { result ->

                dismissProgressDialog()

                // ✅ Check Response Code
                if (result.responseCode == 200) {

                    // ✅ Check Data Not Null / Not Empty
                    if (!result.wrappedList.isNullOrEmpty()) {

                        CompleteOJTList = result.wrappedList

                        if (verificationStatus != "Completed") {
                            val dialog = FullScreenDialog(CompleteOJTList)
                            dialog.show(parentFragmentManager, "Full Screen Dialog")
                        }

                    } else {
                        // ❌ No Data Case
                        Toast.makeText(
                            requireContext(),
                            "No Data Found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    // ❌ Response Code Not 200
                    Toast.makeText(
                        requireContext(),
                        result.responseDesc ?: "Server Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            response.onFailure { error ->




                dismissProgressDialog()

                // ✅ Handle 401 (Session Expired)
                if (error is HttpException && error.code() == 401) {
                    AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                    return@onFailure
                }

                // ✅ General Failure
                Toast.makeText(
                    requireContext(),
                    error.message ?: "Something went wrong. Try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.OjtListByBatch.observe(viewLifecycleOwner) { response ->

            response.onSuccess { result ->

                dismissProgressDialog()

                // ✅ Check Response Code
                if (result.responseCode == 200) {

                    // ✅ Check Data Available
                    if (!result.wrappedList.isNullOrEmpty()) {

                        OJTList = result.wrappedList
                        childAdapter.setItems(OJTList)

                        // ✅ Print JSON
                        val gson = Gson()
                        val jsonString = gson.toJson(OJTList)
                        Log.d("JSON_DATA", jsonString)

                    } else {
                        // ❌ No Data
                        childAdapter.setItems(emptyList())

                        Toast.makeText(
                            requireContext(),
                            "No Data Found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    // ❌ API Returned Error Code
                    Toast.makeText(
                        requireContext(),
                        result.responseDesc ?: "Server Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            response.onFailure { error ->

                dismissProgressDialog()

                // ✅ Handle 401 Session Expired
                if (error is HttpException && error.code() == 401) {
                    AppUtil.showSessionExpiredDialog(
                        findNavController(),
                        requireContext()
                    )
                    return@onFailure
                }

                // ❌ Other Errors (Network / Timeout etc.)
                Toast.makeText(
                    requireContext(),
                    error.message ?: "Something went wrong. Try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        // 👉 Orientation unlock when dialog closed
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}
