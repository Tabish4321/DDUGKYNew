package com.deendayalproject.fragments

import SharedViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentQTeamListBinding
import com.deendayalproject.databinding.ItemTrainingCenterBinding
import com.deendayalproject.model.request.TrainingCenter
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.util.AppUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class QTeamListFragment : BaseFragment<FragmentQTeamListBinding>(
    bindingInflater = FragmentQTeamListBinding::inflate
) {

    private lateinit var viewModel: SharedViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var latitude = 26.2153
    private var longitude = 84.3588
    private var radius = 500000000f


    // Location permission launcher
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                logFragmentEvent("Location_Permission_Granted")
            } else {
                showToast("❌ Location permission denied")
                logFragmentEvent("Location_Permission_Denied")
            }
        }

    override fun initializeViews() {
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        setupLocationClient()

    }

    override fun setupObservers() {

        observeViewModel()
    }

    override fun setupClickListeners() {
        binding.toolbar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun loadInitialData() {
        fetchTrainingCenters()
    }

    // ------------------- UI Setup ------------------------

    private fun setupToolbar() {
        setupToolbar(
            binding.root,
            "Training Centers",
            showBack = true,
            showLang = false,
            showProfile = false,
            backClick = { findNavController().navigateUp() }
        )
    }

    private fun setupRecyclerView() {
        // Using BaseFragment's recyclerView setup
        setupRecyclerView(
            recyclerView = binding.recyclerView,
            items = emptyList<TrainingCenter>(),
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = ItemTrainingCenterBinding::inflate,
            onBind = { center, binding, position ->
                bindTrainingCenterItem(center, binding, position)
            },
            onItemClick = { center, position ->
                handleTrainingCenterClick(center)
            },
            noDataTitle = "No Training Centers",
            noDataDescription = "No training centers available for verification",
            noDataIconRes = R.drawable.no_data
        )
    }

    private fun setupLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun bindTrainingCenterItem(center: TrainingCenter, binding: ItemTrainingCenterBinding, position: Int) {
        binding.trainingCenterName.text = center.trainingCenterName ?: "Unnamed Center"
        binding.trainingCenterAddress.text = center.trainingCenterAddress ?: "N/A"
        binding.senctionOrder.text = center.senctionOrder ?: "N/A"
        binding.districtName.text=center.districtName?:"N/A"
        binding.totalCapacity.text=center.tcCapacity?:"N/A"
        binding.femaleCapacity.text=center.tcFemaleCapacity?:"N/A"
        binding.maleCapacity.text=center.tcMaleCapacity?:"N/A"
    }

    private fun handleTrainingCenterClick(center: TrainingCenter) {
        logFragmentEvent("Training_Center_Clicked", center.trainingCenterId.toString())
        navigateToForm(center)

        /* Uncomment if geofencing is needed:
        checkGeofence(center) { inside, location ->
            if (inside) {
                navigateToForm(center)
            } else {
                showErrorToast("You are outside the training center area")
            }
        }
        */
    }

    private fun navigateToForm(center: TrainingCenter) {
        val action = QTeamListFragmentDirections.actionQTeamListFragmentToQTeamFormFragment(
            center.trainingCenterId.toString(),
            center.trainingCenterName ?: "",
            center.senctionOrder ?: ""
        )
        findNavController().navigate(action)
    }

    // ------------------- API & Observers ------------------------

    private fun fetchTrainingCenters() {
        val loginId = AppUtil.getSavedLoginIdPreference(requireContext())
        val token = AppUtil.getSavedTokenPreference(requireContext())
        val imeiNo = AppUtil.getAndroidId(requireContext())

        val request = TrainingCenterRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = loginId,
            imeiNo = imeiNo
        )

        logFragmentEvent("Fetch_Training_Centers_Started")
        setCustomKey("user_login_id", loginId)

        showProgressDialog("Loading training centers...")
        viewModel.fetchQTeamTrainingList(request, "Bearer $token")
    }

    private fun observeViewModel() {
        viewModel.trainingCenters.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                dismissProgressDialog()

                handleApiResponse(
                    responseCode = response.responseCode,
                    data = response.wrappedList,
                    onSuccess = { centers ->
                        updateRecyclerViewData(binding.recyclerView.id, centers ?: emptyList())
                        logFragmentEvent("Training_Centers_Loaded", "Count: ${centers?.size ?: 0}")
                        if (centers.isNullOrEmpty()) {
                            showToast("No training centers available")
                        }
                    },
                    onNoData = {
                        showToast("No training centers available")
                        clearRecyclerViewData(binding.recyclerView.id)
                    },
                    onSessionExpired = {
                        handleSessionExpired()
                    },
                    onUpgradeRequired = {
                        showToast("Please upgrade your app")
                    }
                )
            }

            result.onFailure { throwable ->
                dismissProgressDialog()
                logCrashlyticsError("fetchTrainingCenters", Exception(throwable))
                showErrorToast("Failed to load training centers. Please try again.")
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading && !isProgressShowing()) {
                showProgressDialog()
            } else if (!isLoading) {
                dismissProgressDialog()
            }
        }
    }

    // ------------------- Location & Geofencing ------------------------

    private fun checkGeofence(center: TrainingCenter, onResult: (Boolean, Location?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }
        showProgressDialog("Checking location...")

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                dismissProgressDialog()
                if (location != null) {
                    val inside = isUserInGeofence(
                        userLat = location.latitude,
                        userLng = location.longitude,
                        centerLat = latitude, // Use center's actual lat/lng if available
                        centerLng = longitude,
                        radiusInMeters = radius
                    )
                    onResult(inside, location)
                    logFragmentEvent("Geofence_Check", "Inside: $inside")
                } else {
                    showToast("Location not available")
                    onResult(false, null)
                }
            }
            .addOnFailureListener { exception ->
                dismissProgressDialog()
                logCrashlyticsError("checkGeofence", exception)
                showErrorToast("Failed to get location")
                onResult(false, null)
            }
    }

    private fun isUserInGeofence(
        userLat: Double,
        userLng: Double,
        centerLat: Double,
        centerLng: Double,
        radiusInMeters: Float
    ): Boolean {
        val results = FloatArray(1)
        Location.distanceBetween(userLat, userLng, centerLat, centerLng, results)
        return results[0] <= radiusInMeters
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // BaseFragment handles cleanup
    }
}