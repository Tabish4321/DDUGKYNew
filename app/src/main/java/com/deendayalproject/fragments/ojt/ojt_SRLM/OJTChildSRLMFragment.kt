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

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.deendayalproject.model.request.ModulesCandidateByOjtRequest2
import com.deendayalproject.model.response.ListByBatchSRLM
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException








class OJTChildSRLMFragment :
    BaseFragment<FragmentOJTChildSRLMBinding>(
        FragmentOJTChildSRLMBinding::inflate
    ) {

    private lateinit var viewModel: SharedViewModel
    private var isBottomSheetOpened = false
    private var CompleteOJTList: List<ChildSRLM> = emptyList()
    private var VerificationSRLMDetails: List<ChildSRLM> = emptyList()

    private lateinit var childAdapter: ChildSRLMAdapter

    private var batchId: String = ""
    private var verificationStatus: String? = null

    // ✅ Selected Candidate Id
    private var selectedCandidateId: String = ""

    // =========================================================
    // PERMISSION LAUNCHER
    // =========================================================

    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val cameraGranted =
                permissions[Manifest.permission.CAMERA] == true

            val microphoneGranted =
                permissions[Manifest.permission.RECORD_AUDIO] == true

            val fineLocationGranted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

            val coarseLocationGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (cameraGranted &&
                microphoneGranted &&
                (fineLocationGranted || coarseLocationGranted)
            ) {

                // ✅ Permission Granted
                callCompleteOjtApi()

            } else {

                Toast.makeText(
                    requireContext(),
                    "Camera, Microphone & Location Permission Required",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    // =========================================================
    // INITIALIZE
    // =========================================================

    override fun initializeViews() {

        setupViewModel()
        setupRecyclerView()

        arguments?.let { bundle ->
            batchId = bundle.getString("batchId").orEmpty()
        }

        val token = getToken(requireContext())

        val request = ModulesCandidateByOjtRequest2(
            BuildConfig.VERSION_NAME,
            batchId
        )

        showProgressDialog("Loading...")

        viewModel.fetchgetVerifiedBatchCandidateList(
            request,
            "Bearer $token"
        )
    }










//     private val _esoprolecategory =
//        MutableLiveData<Result<EsopCandidateRes>>()
//
//    val esoprolecategory: LiveData<Result<EsopCandidateRes>> =
//        _esoprolecategory
//
//    fun esoprolecategory(request: EsopCandidateRequest, token: String) {
//        handleApiCall(
//            apiCall = {
//                repositoryManager.esop.esoprolecategory(
//                    request,
//                    token
//                )
//            },
//            resultLiveData = _esoprolecategory
//        )
//    }





    override fun setupObservers() {
        observeViewModel()
    }

    override fun loadInitialData() {}

    override fun setupClickListeners() {

        binding.toolbar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.tvTitle.text =
            getString(R.string.ojt_candidate_srlm_list)
    }

    // =========================================================
    // VIEWMODEL
    // =========================================================

    private fun setupViewModel() {

        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        viewModel =
            ViewModelProvider(this)[SharedViewModel::class.java]
    }

    // =========================================================
    // RECYCLER VIEW
    // =========================================================

    private fun setupRecyclerView() {
        parentFragmentManager.setFragmentResultListener(
            "BOTTOM_SHEET_DISMISSED",
            viewLifecycleOwner
        ) { _, _ ->
            isBottomSheetOpened = false
        }
        // ✅ NEW LISTENER (IMPORTANT)
        parentFragmentManager.setFragmentResultListener(
            "REFRESH_DATA",
            viewLifecycleOwner
        ) { _, _ ->
            observeViewModel()

        }
        observeViewModel()
        childAdapter = ChildSRLMAdapter { batch ->

            // ✅ Save Status & Candidate Id
            verificationStatus = batch.verificationStatus
            selectedCandidateId = batch.candidateId

            // ✅ FIRST CHECK PERMISSION
            checkAndRequestPermissions()
        }

        binding.rvModules.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            adapter = childAdapter
        }
    }

    // =========================================================
    // CHECK PERMISSION
    // =========================================================

    private fun checkAndRequestPermissions() {

        val cameraPermission =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

        val microphonePermission =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

        val fineLocationPermission =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationPermission =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (cameraPermission &&
            microphonePermission &&
            (fineLocationPermission || coarseLocationPermission)
        ) {

            // ✅ Permission Already Granted
            callCompleteOjtApi()

        } else {

            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // =========================================================
    // CALL API
    // =========================================================

    private fun callCompleteOjtApi() {

        val token = getToken(requireContext())

        val request = ModulesOJTCompleteOjtRequest(
            BuildConfig.VERSION_NAME,
            selectedCandidateId
        )

        showProgressDialog("Loading...")

        viewModel.getVerifiedCompleteOjt(
            request,
            "Bearer $token"
        )
    }

    // =========================================================
    // OBSERVE VIEWMODEL
    // =========================================================

    @SuppressLint("NotifyDataSetChanged")
    private fun observeViewModel() {

        // =====================================================
        // CANDIDATE LIST
        // =====================================================

        viewModel.VerifiedBatchCandidateSRLMCandidateList.observe(
            viewLifecycleOwner
        ) { response ->

            response.onSuccess { result ->
                if (isBottomSheetOpened) return@onSuccess
                dismissProgressDialog()

                if (result.responseCode == 200) {

                    if (!result.wrappedList.isNullOrEmpty()) {

                        childAdapter.setItems(result.wrappedList)

                    } else {

                        childAdapter.setItems(emptyList())

                        Toast.makeText(
                            requireContext(),
                            "No Data Found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {

                    Toast.makeText(
                        requireContext(),
                        result.responseDesc ?: "Server Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                isBottomSheetOpened = true
            }

            response.onFailure { error ->

                dismissProgressDialog()

                Toast.makeText(
                    requireContext(),
                    error.message ?: "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // =====================================================
        // COMPLETE OJT API
        // =====================================================

        viewModel.VerifiedCompleteChildOjt.observe(
            viewLifecycleOwner
        ) { response ->

            response.onSuccess { result ->

                dismissProgressDialog()

                if (result.responseCode == 200) {

                    if (!result.wrappedList.isNullOrEmpty()) {

                        // ✅ SET DATA
                        CompleteOJTList = result.wrappedList
                        VerificationSRLMDetails = result.wrappedList

                        // ✅ NOW OPEN DIALOG
                        openVerificationScreen()

                    } else {

                        // ✅ EMPTY LIST
                        Toast.makeText(
                            requireContext(),
                            "Data is not available",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } else {

                    Toast.makeText(
                        requireContext(),
                        result.responseDesc ?: "Server Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            response.onFailure { error ->

                dismissProgressDialog()

                Toast.makeText(
                    requireContext(),
                    error.message ?: "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // =========================================================
    // OPEN SCREEN
    // =========================================================

    private fun openVerificationScreen() {

        if (CompleteOJTList.isEmpty()) {

            Toast.makeText(
                requireContext(),
                "Data is not available",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        // ✅ OPEN DIALOG
        if (verificationStatus != "Completed") {

            val dialog = SRLMBottomDialog(
                ArrayList(CompleteOJTList)
            )

            dialog.show(
                parentFragmentManager,
                "Full Screen Dialog"
            )

        } else {

            val dialog =
                PreViewlScreenCandidateBottomSRLMDialog(
                    VerificationSRLMDetails,
                    ArrayList(CompleteOJTList)
                )

            dialog.show(
                parentFragmentManager,
                "PreViewlScreenCandidateBottomDialog"
            )
        }
    }

    // =========================================================
    // DESTROY
    // =========================================================

    override fun onDestroy() {
        super.onDestroy()

        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}







//class OJTChildSRLMFragment : BaseFragment<FragmentOJTChildSRLMBinding>(FragmentOJTChildSRLMBinding::inflate) {
//
//    private lateinit var viewModel: SharedViewModel
//    private var isBottomSheetOpened = false
//    // ---------- Batch ----------
//    private var CompleteOJTList: List<ChildSRLM> = emptyList()
//    private var VerificationSRLMDetails: List<ChildSRLM> = emptyList()
//
//
//    private var OJTList: List<ListByBatchSRLM> = emptyList()
//    private lateinit var childAdapter: ChildSRLMAdapter
//    private var batchId: String = ""
//    //    private var selectedBatch: OJTList? = null
//    private var selectedBatch: OjtBatchRes? = null
//    var verificationStatus : String? = null
//
//    private val permissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//
//            val cameraGranted =
//                permissions[Manifest.permission.CAMERA] == true
//
//            val microphoneGranted =
//                permissions[Manifest.permission.RECORD_AUDIO] == true
//
//            val fineLocationGranted =
//                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
//
//            val coarseLocationGranted =
//                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
//
//            if (cameraGranted &&
//                microphoneGranted &&
//                (fineLocationGranted || coarseLocationGranted)
//            ) {
//
//                // ✅ All Permission Granted
//                openVerificationScreen()
//
//            } else {
//
//                Toast.makeText(
//                    requireContext(),
//                    "Camera, Microphone & Location Permission Required",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
//
//    override fun initializeViews() {
//        setupViewModel()
//
////        setupTrainingCenter()
//        setupBatchRecycler()
//        arguments?.let { bundle ->
//            batchId = bundle.getString("batchId").orEmpty()
//        }
//
//
//        val token = getToken(requireContext())
//        val request = ModulesCandidateByOjtRequest2(
//            BuildConfig.VERSION_NAME,
//            batchId.toString()
//        )
//
//        showProgressDialog("Loading...")
//        viewModel.fetchgetVerifiedBatchCandidateList(request, "Bearer $token")
//
//
//
////        fetchBatch()
//    }
//
//    override fun loadInitialData() {
////        fetchModulesSanction()
//
//    }
//
//    override fun setupObservers() {
//        observeViewModel()
//    }
//
//    override fun setupClickListeners() {
//        binding.toolbar.btnBack.setOnClickListener {
//            findNavController().navigateUp()
//        }
//        binding.toolbar.tvTitle.text = getString(R.string.ojt_candidate_srlm_list)
//    }
//
//    private fun setupViewModel() {
//        // 👉 Lock screen in Portrait mode
//        requireActivity().requestedOrientation =
//            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
//    }
//
//
//
//
//
//
//
//
//
//    private fun setupBatchRecycler() {
//
//
//        childAdapter = ChildSRLMAdapter { batch ->
//            AppUtil.saveOJTBatchIDPreference(requireContext(), batchId.toString())
//            verificationStatus=batch.verificationStatus
//
////             batch.candidateName
//            checkAndRequestPermissions()
//            fetchBatch()
//
//            val token = getToken(requireContext())
//            val request = ModulesOJTCompleteOjtRequest(
////            BuildConfig.VERSION_NAME,
////                BuildConfig.VERSION_NAME,2508460350.toString()
//                BuildConfig.VERSION_NAME,batch.candidateId
////            AppUtil.getSavedOJTBatchIDPreference(requireContext())
//            )
//            showProgressDialog("Loading...")
//            viewModel.getVerifiedCompleteOjt(request, "Bearer $token")
//
//
//            parentFragmentManager.setFragmentResultListener(
//                "BOTTOM_SHEET_DISMISSED",
//                viewLifecycleOwner
//            ) { _, _ ->
//                isBottomSheetOpened = false
//            }
//            // ✅ NEW LISTENER (IMPORTANT)
//            parentFragmentManager.setFragmentResultListener(
//                "REFRESH_DATA",
//                viewLifecycleOwner
//            ) { _, _ ->
//                observeViewModel()
//
//            }
//            observeViewModel()
//
//
//
//
//        }
//
////        }
//
//        binding.rvModules.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = childAdapter
//        }
//
//
//
//    }
//
//
//
//    //    private fun fetchBatch(trainingCenterId: String) {
//    private fun fetchBatch() {
//
//
//
//        val token = getToken(requireContext())
//        val request = ModulesOJTCompleteOjtRequest(
////            BuildConfig.VERSION_NAME,
//            BuildConfig.VERSION_NAME,
//            AppUtil.getSavedOJTBatchIDPreference(requireContext())
//        )
////        2508460350.toString()
//        showProgressDialog("Loading...")
//        viewModel.getVerifiedCompleteOjt(request, "Bearer $token")
//
//    }
//
//
//    private fun checkAndRequestPermissions() {
//
//        val cameraPermission =
//            ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED
//
//        val microphonePermission =
//            ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.RECORD_AUDIO
//            ) == PackageManager.PERMISSION_GRANTED
//
//        val fineLocationPermission =
//            ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//
//        val coarseLocationPermission =
//            ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//
//        if (cameraPermission &&
//            microphonePermission &&
//            (fineLocationPermission || coarseLocationPermission)
//        ) {
//
//            // ✅ Already Granted
//            openVerificationScreen()
//
//        } else {
//
//            // ✅ Ask Permission One Time
//            permissionLauncher.launch(
//                arrayOf(
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.RECORD_AUDIO,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
//        }
//    }
//
//    // =========================================================
//    // OPEN SCREEN AFTER PERMISSION
//    // =========================================================
//
//    private fun openVerificationScreen() {
//
//
//
//        // Check list empty or not
//        if (CompleteOJTList.isEmpty()) {
//
//            return@openVerificationScreen
//        }
//
//        // If data available
//        if (verificationStatus != "Completed") {
//
//            val dialog = SRLMBottomDialog(
//                ArrayList(CompleteOJTList)
//            )
//
//            dialog.show(
//                parentFragmentManager,
//                "Full Screen Dialog"
//            )
//
//        } else {
//
//            val dialog = PreViewlScreenCandidateBottomSRLMDialog(
//                VerificationSRLMDetails,
//                ArrayList(CompleteOJTList)
//            )
//
//            dialog.show(
//                parentFragmentManager,
//                "PreViewlScreenCandidateBottomDialog"
//            )
//        }
//    }
//
//    private fun clearBatchRecycler() {
//        selectedBatch = null
//        OJTList = emptyList()
//        childAdapter.setItems(emptyList())
//    }
//
//
//
//    @SuppressLint("RepeatOnLifecycleWrongUsage")
//    private fun observeViewModel() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//
//                viewModel.VerifiedBatchCandidateSRLMCandidateList.observe(viewLifecycleOwner) { response ->
//
//
//                    response.onSuccess { result ->
//                        if (isBottomSheetOpened) return@onSuccess
////                if (isBottomSheetOpened) return@observe
//                        dismissProgressDialog()
//
//                        // ✅ Check Response Code
//                        if (result.responseCode == 200) {
//
//                            // ✅ Check Data Available
//                            if (!result.wrappedList.isNullOrEmpty()) {
//
//                                OJTList = result.wrappedList
//                                childAdapter.setItems(OJTList)
//
//                                // ✅ Print JSON
//                                val gson = Gson()
//                                val jsonString = gson.toJson(OJTList)
//                                Log.d("JSON_DATA", jsonString)
//
//
//                                // ✅ CHECK PERMISSION HERE
//
//
//
//
//                            } else {
//                                // ❌ No Data
//                                childAdapter.setItems(emptyList())
//                                isBottomSheetOpened = true
//                                Toast.makeText(
//                                    requireContext(),
//                                    "No Data Found",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//
//                        } else {
//                            // ❌ API Returned Error Code
//                            Toast.makeText(
//                                requireContext(),
//                                result.responseDesc ?: "Server Error",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//
//                    response.onFailure { error ->
//
//                        dismissProgressDialog()
//
//                        // ✅ Handle 401 Session Expired
//                        if (error is HttpException && error.code() == 401) {
//                            AppUtil.showSessionExpiredDialog(
//                                findNavController(),
//                                requireContext()
//                            )
//                            return@onFailure
//                        }
//
//                        // ❌ Other Errors (Network / Timeout etc.)
//                        Toast.makeText(
//                            requireContext(),
//                            error.message ?: "Something went wrong. Try again.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//
//
//
//                }
//
//            }
//        }
//
//        viewModel.VerifiedCompleteChildOjt.observe(
//            viewLifecycleOwner
//        ) { response ->
//            response.onSuccess { result ->
//
//                dismissProgressDialog()
//
//                if (result.responseCode == 200) {
//
//                    if (!result.wrappedList.isNullOrEmpty()) {
//
//                        VerificationSRLMDetails = result.wrappedList
//
//
//                        CompleteOJTList = result.wrappedList
//
//                } else {
//
//                    Toast.makeText(
//                        requireContext(),
//                        result.responseDesc
//                            ?: "Server Error",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//               else if (result.responseCode == 202) {
//                    CompleteOJTList
//
////                    Toast.makeText(
////                        requireContext(),
////                        result.responseDesc,
////                        Toast.LENGTH_SHORT
////                    ).show()
//               }
//
//            response.onFailure { error ->
//
//                dismissProgressDialog()
//
//                if (error is HttpException &&
//                    error.code() == 401
//                ) {
//
//                    AppUtil.showSessionExpiredDialog(
//                        findNavController(),
//                        requireContext()
//                    )
//
//                    return@onFailure
//                }
//
//                Toast.makeText(
//                    requireContext(),
//                    error.message
//                        ?: "Something went wrong",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//        }
//
//
//
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        // 👉 Orientation unlock when dialog closed
//        requireActivity().requestedOrientation =
//            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
//    }
//}
