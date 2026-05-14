package com.deendayalproject.fragments.ojt.ojt_SRLM

import SharedViewModel
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
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
import com.deendayalproject.adapter.ChildAdapter
import com.deendayalproject.adapter.ChildSRLMAdapter
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.ChildFragmentBinding
import com.deendayalproject.databinding.ChildFragmentBinding.inflate
import com.deendayalproject.databinding.FragmentOJTChildSRLMBinding
import com.deendayalproject.fragments.ojt.FullScreenDialog
import com.deendayalproject.fragments.ojt.PreViewlScreenCandidateBottomDialog
import com.deendayalproject.model.request.ModulesCandidateByOjtRequest
import com.deendayalproject.model.request.ModulesOJTCompleteOjtRequest
import com.deendayalproject.model.response.ChildSRLM
import com.deendayalproject.model.response.OJTList
import com.deendayalproject.model.response.OjtBatchRes
import com.deendayalproject.model.response.OjtListChildSRLMRes
import com.deendayalproject.model.response.VerificationDetails
import com.deendayalproject.network.SecurePreferenceManager.getToken
import com.deendayalproject.util.AppUtil
import com.google.gson.Gson
import retrofit2.HttpException

class OJTChildSRLMFragment : BaseFragment<FragmentOJTChildSRLMBinding>(FragmentOJTChildSRLMBinding::inflate) {

    private lateinit var viewModel: SharedViewModel
    // ---------- Batch ----------
    private var CompleteOJTList: List<ChildSRLM> = emptyList()
    private var VerificationDetails: List<ChildSRLM> = emptyList()
//    private var VerificationDetails: List<VerificationDetails> = emptyList()

    private var OJTList: List<ChildSRLM> = emptyList()
    private lateinit var childAdapter: ChildSRLMAdapter
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
        binding.toolbar.tvTitle.text = getString(R.string.ojt_candidate_srlm_list)
    }

    private fun setupViewModel() {
        // 👉 Lock screen in Portrait mode
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
    }









    private fun setupBatchRecycler() {


        childAdapter = ChildSRLMAdapter { batch ->

            verificationStatus=batch.trainingCenterName

             batch.trainingCenterName


//            VerificationDetails = result.wrappedList
//            CompleteOJTList = result.wrappedList
            if (verificationStatus != "Completed") {
                CompleteOJTList = arrayListOf(batch)
                val dialog = SRLMBottomDialog(CompleteOJTList)
                dialog.show(parentFragmentManager, "PreViewlScreenCandidateBottomDialog")

            }

        }

//        }

        binding.rvModules.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = childAdapter
        }



    }



    //    private fun fetchBatch(trainingCenterId: String) {
    private fun fetchBatch() {



        val token = getToken(requireContext())
        val request = ModulesOJTCompleteOjtRequest(
//            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_NAME,2508460350.toString()
//            AppUtil.getSavedOJTBatchIDPreference(requireContext())
        )
        showProgressDialog("Loading...")
        viewModel.getVerifiedCompleteOjt(request, "Bearer $token")








//        showProgressDialog("Loading...")
//            viewModel.fetchOJTgetCompleteOjt(request, "Bearer $token")
//        viewModel.getVerifiedCompleteOjt(request, "Bearer $token")
//        val token = getToken(requireContext())
//        val request = ModulesCandidateByOjtRequest(
//            BuildConfig.VERSION_NAME,
//            AppUtil.getSavedOJTBatchIDPreference(requireContext())
//        )
//        showProgressDialog("Loading...")
//        viewModel.fetchCandidateByOjtBy(request, "Bearer $token")
    }




    private fun clearBatchRecycler() {
        selectedBatch = null
        OJTList = emptyList()
        childAdapter.setItems(emptyList())
    }

    private fun observeViewModel() {
        viewModel.VerifiedCompleteChildOjt.observe(viewLifecycleOwner) { response ->


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








//            response.onSuccess { result ->
//
//                dismissProgressDialog()
//
//                // ✅ Check Response Code
//                if (result.responseCode == 200) {
//
//                    // ✅ Check Data Not Null / Not Empty
//                    if (!result.wrappedList.isNullOrEmpty()) {
//
//                        CompleteOJTList = result.wrappedList
//                        val candidateId = CompleteOJTList.firstOrNull()?.candidateId
//                        if (verificationStatus != "Completed") {
////                            val dialog = FullScreenDialog(CompleteOJTList)
////                            dialog.show(parentFragmentManager, "Full Screen Dialog")
//                        }
//                        else{
//
//
//
//
//                            val token = getToken(requireContext())
//                            val request = ModulesOJTCompleteOjtRequest(
//                                BuildConfig.VERSION_NAME,
//                                candidateId.toString()
//                            )
//
//                            showProgressDialog("Loading...")
//                            viewModel.fetchgetCandidateOjtVerification(request, "Bearer $token")
//
//
//
////                            val dialog = PreViewlScreenCandidateBottomDialog(VerificationDetails)
////                            dialog.show(parentFragmentManager, "PreViewlScreenCandidateBottomDialog")
//                        }
//                    } else {
//                        // ❌ No Data Case
//                        Toast.makeText(
//                            requireContext(),
//                            "No Data Found",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                } else {
//                    // ❌ Response Code Not 200
//                    Toast.makeText(
//                        requireContext(),
//                        result.responseDesc ?: "Server Error",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//
//            response.onFailure { error ->
//
//
//
//
//                dismissProgressDialog()
//
//                // ✅ Handle 401 (Session Expired)
//                if (error is HttpException && error.code() == 401) {
//                    AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
//                    return@onFailure
//                }
//
//                // ✅ General Failure
//                Toast.makeText(
//                    requireContext(),
//                    error.message ?: "Something went wrong. Try again.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
        }








//        viewModel.OjtListByBatch.observe(viewLifecycleOwner) { response ->
//
//            response.onSuccess { result ->
//
//                dismissProgressDialog()
//
//                // ✅ Check Response Code
//                if (result.responseCode == 200) {
//
//                    // ✅ Check Data Available
//                    if (!result.wrappedList.isNullOrEmpty()) {
//
//                        OJTList = result.wrappedList
//                        childAdapter.setItems(OJTList)
//
//                        // ✅ Print JSON
//                        val gson = Gson()
//                        val jsonString = gson.toJson(OJTList)
//                        Log.d("JSON_DATA", jsonString)
//
//                    } else {
//                        // ❌ No Data
//                        childAdapter.setItems(emptyList())
//
//                        Toast.makeText(
//                            requireContext(),
//                            "No Data Found",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                } else {
//                    // ❌ API Returned Error Code
//                    Toast.makeText(
//                        requireContext(),
//                        result.responseDesc ?: "Server Error",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//
//            response.onFailure { error ->
//
//                dismissProgressDialog()
//
//                // ✅ Handle 401 Session Expired
//                if (error is HttpException && error.code() == 401) {
//                    AppUtil.showSessionExpiredDialog(
//                        findNavController(),
//                        requireContext()
//                    )
//                    return@onFailure
//                }
//
//                // ❌ Other Errors (Network / Timeout etc.)
//                Toast.makeText(
//                    requireContext(),
//                    error.message ?: "Something went wrong. Try again.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//
//        viewModel.CandidateOjtVerificationDetails.observe(viewLifecycleOwner) { response ->
//
//            response.onSuccess { result ->
//
//                dismissProgressDialog()
//
//                // ✅ Check Response Code
//                if (result.responseCode == 200) {
//
//                    // ✅ Check Data Not Null / Not Empty
//                    if (!result.wrappedList.isNullOrEmpty()) {
//
//                        VerificationDetails = result.wrappedList
//
//                        val dialog = PreViewlScreenCandidateBottomDialog(VerificationDetails,CompleteOJTList)
//                        dialog.show(parentFragmentManager, "PreViewlScreenCandidateBottomDialog")
//                    } else {
//                        // ❌ No Data Case
//                        Toast.makeText(
//                            requireContext(),
//                            "No Data Found",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                } else {
//                    // ❌ Response Code Not 200
//                    Toast.makeText(
//                        requireContext(),
//                        result.responseDesc ?: "Server Error",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//
//            response.onFailure { error ->
//
//
//
//
//                dismissProgressDialog()
//
//                // ✅ Handle 401 (Session Expired)
//                if (error is HttpException && error.code() == 401) {
//                    AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
//                    return@onFailure
//                }
//
//                // ✅ General Failure
//                Toast.makeText(
//                    requireContext(),
//                    error.message ?: "Something went wrong. Try again.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 👉 Orientation unlock when dialog closed
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}
