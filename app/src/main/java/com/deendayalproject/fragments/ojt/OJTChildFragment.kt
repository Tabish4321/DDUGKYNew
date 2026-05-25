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
import com.deendayalproject.model.response.CandidateOjtVerificationDetails
import com.deendayalproject.model.response.OJTList
import com.deendayalproject.model.response.OjtBatchRes
import com.deendayalproject.model.response.VerificationDetails
import com.deendayalproject.network.SecurePreferenceManager.getToken

import com.deendayalproject.util.AppUtil
import com.google.gson.Gson
import retrofit2.HttpException
import kotlin.apply
import kotlin.collections.isNullOrEmpty
import kotlin.jvm.java
import kotlin.onFailure
import kotlin.onSuccess

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch

//mac commit in use 20/03/2026




class OJTChildFragment :
    BaseFragment<ChildFragmentBinding>(ChildFragmentBinding::inflate) {

    private lateinit var viewModel: SharedViewModel
    private var isBottomSheetOpened = false
    // =========================================================
    // LISTS
    // =========================================================

    private var CompleteOJTList: List<OJTList> = emptyList()

    private var VerificationDetails: List<VerificationDetails> =
        emptyList()

    private var OJTList: List<OjtBatchRes> = emptyList()

    private lateinit var childAdapter: ChildAdapter

    private var selectedBatch: OjtBatchRes? = null

    var verificationStatus: String? = null

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

                // ✅ All Permission Granted
                openVerificationScreen()

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

        setupBatchRecycler()
    }

    override fun loadInitialData() {

        fetchBatch()
    }

    override fun setupObservers() {

        observeViewModel()
    }

    override fun setupClickListeners() {

        binding.toolbar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.tvTitle.text =
            getString(R.string.ojt_candidate_list)
    }

    // =========================================================
    // VIEW MODEL
    // =========================================================

    private fun setupViewModel() {

        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        viewModel =
            ViewModelProvider(this)[SharedViewModel::class.java]
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
    }

    // =========================================================
    // RECYCLER
    // =========================================================

    private fun setupBatchRecycler() {

        childAdapter = ChildAdapter { batch ->

            selectedBatch = batch

            verificationStatus =
                batch.verificationStatus

            val token =
                getToken(requireContext())

            val request =
                ModulesOJTCompleteOjtRequest(
                    BuildConfig.VERSION_NAME,
                    batch.candidateId
                )

            showProgressDialog("Loading...")

            viewModel.fetchOJTgetCompleteOjt(
                request,
                "Bearer $token"
            )
        }

        binding.rvModules.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            adapter = childAdapter
        }
    }

    // =========================================================
    // FETCH BATCH
    // =========================================================

    private fun fetchBatch() {

        val token =
            getToken(requireContext())

        val request = ModulesCandidateByOjtRequest(
                BuildConfig.VERSION_NAME,
                AppUtil.getSavedOJTBatchIDPreference(
                    requireContext()))

        showProgressDialog("Loading...")

        viewModel.fetchCandidateByOjtBy(
            request,
            "Bearer $token"
        )
    }

    // =========================================================
    // CHECK & REQUEST PERMISSION
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

            // ✅ Already Granted
            openVerificationScreen()

        } else {

            // ✅ Ask Permission One Time
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
    // OPEN SCREEN AFTER PERMISSION
    // =========================================================

    private fun openVerificationScreen() {

        if (verificationStatus != "Completed") {

            val dialog =
                FullScreenDialog(CompleteOJTList)

            dialog.show(
                parentFragmentManager,
                "Full Screen Dialog"
            )

        } else {

            val candidateId =
                CompleteOJTList.firstOrNull()?.candidateId

            val token =
                getToken(requireContext())

            val request =
                ModulesOJTCompleteOjtRequest(
                    BuildConfig.VERSION_NAME,
                    candidateId.toString()
                )

            showProgressDialog("Loading...")

            viewModel.fetchgetCandidateOjtVerification(
                request,
                "Bearer $token"
            )
        }
    }



    private fun observeViewModel() {

        // =====================================================
        // COMPLETE OJT
        // =====================================================

        viewModel.CompleteOjt.observe(
            viewLifecycleOwner
        ) { response ->

            response.onSuccess { result ->

                dismissProgressDialog()

                if (result.responseCode == 200) {

                    if (!result.wrappedList.isNullOrEmpty()) {

                        CompleteOJTList =
                            result.wrappedList

                        // ✅ CHECK PERMISSION HERE
                        checkAndRequestPermissions()

                    } else {

                        Toast.makeText(
                            requireContext(),
                            "No Data Found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {

                    Toast.makeText(
                        requireContext(),
                        result.responseDesc
                            ?: "Server Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            response.onFailure { error ->

                dismissProgressDialog()

                if (error is HttpException &&
                    error.code() == 401
                ) {

                    AppUtil.showSessionExpiredDialog(
                        findNavController(),
                        requireContext()
                    )

                    return@onFailure
                }

                Toast.makeText(
                    requireContext(),
                    error.message
                        ?: "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // =====================================================
        // OJT LIST
        // =====================================================




        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.OjtListByBatch.observe(
                    viewLifecycleOwner
                ) { response ->

                    response.onSuccess { result ->
                        if (isBottomSheetOpened) return@onSuccess
                        dismissProgressDialog()

                        if (result.responseCode == 200) {

                            if (!result.wrappedList.isNullOrEmpty()) {

                                OJTList =
                                    result.wrappedList

                                childAdapter.setItems(OJTList)

                                val gson = Gson()

                                val json =
                                    gson.toJson(OJTList)

                                Log.d("JSON_DATA", json)
                                isBottomSheetOpened = true
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
                                result.responseDesc
                                    ?: "Server Error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    response.onFailure { error ->

                        dismissProgressDialog()

                        if (error is HttpException &&
                            error.code() == 401
                        ) {

                            AppUtil.showSessionExpiredDialog(
                                findNavController(),
                                requireContext()
                            )

                            return@onFailure
                        }

                        Toast.makeText(
                            requireContext(),
                            error.message
                                ?: "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }


        viewModel.CandidateOjtVerificationDetails.observe(
            viewLifecycleOwner
        ) { response ->

            response.onSuccess { result ->

                dismissProgressDialog()

                if (result.responseCode == 200) {

                    if (!result.wrappedList.isNullOrEmpty()) {

                        VerificationDetails =
                            result.wrappedList

                        val dialog =
                            PreViewlScreenCandidateBottomDialog(
                                VerificationDetails,
                                CompleteOJTList
                            )

                        dialog.show(
                            parentFragmentManager,
                            "PreViewlScreenCandidateBottomDialog"
                        )

                    } else {

                        Toast.makeText(
                            requireContext(),
                            "No Data Found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {

                    Toast.makeText(
                        requireContext(),
                        result.responseDesc
                            ?: "Server Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            response.onFailure { error ->

                dismissProgressDialog()

                if (error is HttpException &&
                    error.code() == 401
                ) {

                    AppUtil.showSessionExpiredDialog(
                        findNavController(),
                        requireContext()
                    )

                    return@onFailure
                }

                Toast.makeText(
                    requireContext(),
                    error.message
                        ?: "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
