package com.deendayalproject.fragments

import SharedViewModel
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.base.NoDataConfig
import com.deendayalproject.databinding.FragmentFieldVerificationListBinding
import com.deendayalproject.databinding.ItemFieldVerificationLayoutBinding
import com.deendayalproject.model.request.FieldVerificationListRequest
import com.deendayalproject.model.response.FieldVerificationItem
import com.deendayalproject.util.AppUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class FieldVerificationListFragment : BaseFragment<FragmentFieldVerificationListBinding>(
    FragmentFieldVerificationListBinding::inflate
) {

    private lateinit var viewModel: SharedViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var fieldVerificationList: MutableList<FieldVerificationItem> = mutableListOf()

    private var latitude = 26.2153
    private var longitude = 84.3588
    private var radius = 500000000f

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted → retry geofence check
                logFragmentEvent("Location_Permission_Granted")
            } else {

                showErrorToast( getString(R.string.location_permission_denied))
                logCrashlyticsError("LocationPermission", Exception("Location permission denied by user"))
            }
        }


    override fun initializeViews() {
        Log.d("FRAGMENT NAME", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━FieldVerificationListFragment━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        setupToolbar(
            root = binding.root,
            titleRes = R.string.field_ver_list,
            showBack = true,
            showLang = false,
            showProfile = false,
            backClick = { findNavController().navigateUp()}
        )
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        // Setup back stack observer with original logic
        val navController = findNavController()

        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>("refresh_pia_list")
            ?.observe(viewLifecycleOwner) { shouldRefresh ->
                /*if (shouldRefresh == true) {
                    loadFieldVerificationList()
                    // reset flag
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("refresh_pia_list", false)
                }*/
                findNavController().navigateUp()
            }


        setupRecyclerView()
    }

    override fun setupObservers() {
        observeViewModel()
    }

    override fun setupClickListeners() {
//        binding.backButton.setOnClickListener {
//            findNavController().navigateUp()
//        }
    }

    override fun loadInitialData() {
        val request = FieldVerificationListRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.fetchFieldVerificationList(request, "")
    }

    private fun setupRecyclerView() {
        setupRecyclerView(
            recyclerView = binding.recyclerView,
            items = fieldVerificationList,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = { inflater, parent, _ ->
                ItemFieldVerificationLayoutBinding.inflate(inflater, parent, false)
            },
            onBind = { item, itemBinding, position ->
                itemBinding.piaName.text = "PIA Name: ${item.piaName}"
                itemBinding.prnNo.text = "PRN No: ${item.prnNo}"
                itemBinding.address.text = "PIA Address: ${item.address}"
                itemBinding.districtName.text = "District Name: ${item.districtName}"

                itemBinding.root.setOnClickListener {
                    onItemClick(item)
                }
            },
            noDataConfig = NoDataConfig(
                title = "No Field Verifications",
                description = "No field verification records found",
                iconRes = R.drawable.no_data
            )
        )
    }

    private fun onItemClick(item: FieldVerificationItem) {
        val id = item.captiveEmpanelmentId?.toString() ?: ""
        val action = FieldVerificationListFragmentDirections
            .actionFieldVerificationListFragmentToFieldVerificationFormFragment(
                id,
                item.prnNo ?: ""
            )
        findNavController().navigate(action)

        logFragmentEvent("Field_Verification_Item_Selected", item.prnNo ?: "")
    }

    private fun observeViewModel() {
        viewModel.fieldprnDetails.observe(viewLifecycleOwner) { result ->
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.let {
                        fieldVerificationList.clear()
                        fieldVerificationList.addAll(it)
                        updateRecyclerViewData(binding.recyclerView.id, fieldVerificationList)
                    }
                },
                onNoData = {
                    showToast( getString(R.string.no_data_available))
                    fieldVerificationList.clear()
                    updateRecyclerViewData(binding.recyclerView.id, fieldVerificationList)
                },
                onUpgradeRequired = {
                    showToast( getString(R.string.please_upgrade_your_app))
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    // Maintain all original methods for compatibility
    private fun checkGeofence(
        context: Context,
        latitude: Double,
        longitude: Double,
        radiusInMeters: Float,
        progressBar: android.widget.ProgressBar,
        onResult: (inside: Boolean, location: Location?) -> Unit
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

        progressBar.show()

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                progressBar.hide()
                if (location != null) {
                    val inside = isUserInGeofence(
                        userLat = location.latitude,
                        userLng = location.longitude,
                        centerLat = latitude,
                        centerLng = longitude,
                        radiusInMeters = radiusInMeters
                    )
                    onResult(inside, location)

                    // Log location event
                    logFragmentEvent("Geofence_Check_Result", "Inside: $inside")
                } else {
                    showToast( getString(R.string.location_not_found))
                    onResult(false, null)
                }
            }
            .addOnFailureListener { exception ->
                progressBar.hide()
                showErrorToast(getString(R.string.failed_to_get_location))
                onResult(false, null)
                logCrashlyticsError("checkGeofence", exception)
            }
    }

    // Simple geofence check - kept original method
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


    // Location permission check helper
    fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Request location permission
    fun requestLocationPermission() {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}