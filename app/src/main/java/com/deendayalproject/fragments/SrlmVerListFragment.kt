package com.deendayalproject.fragments

import SharedViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.base.NoDataConfig
import com.deendayalproject.databinding.FragmentSrlmListLayoutBinding
import com.deendayalproject.databinding.ItemQteamLayoutBinding
import com.deendayalproject.model.request.TrainingCenter
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.util.AppUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class SrlmVerListFragment : BaseFragment<FragmentSrlmListLayoutBinding>(
    FragmentSrlmListLayoutBinding::inflate
) {

    private lateinit var viewModel: SharedViewModel
    private var trainingCentersList: MutableList<TrainingCenter> = mutableListOf()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    // private var radius = 500000000f
    //private var latitude = 0.0
    //private var longitude = 0.0


    override fun initializeViews() {
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        Log.d("FRAGMENT NAME", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━SrlmVerListFragment━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermission()
        setupToolbar(
            binding.root,
            titleRes = R.string.training_list,
            showBack = true,
            showLang = false,
            showProfile = false,
            backClick = { findNavController().navigateUp()}
        )
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
        showProgressBar()
        val request = TrainingCenterRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.fetchSrlmTeamTrainingList(request, AppUtil.getSavedTokenPreference(requireContext()))
    }

    private fun setupRecyclerView() {
        setupRecyclerView(
            recyclerView = binding.recyclerView,
            items = trainingCentersList,
            layoutManager = LinearLayoutManager(requireContext()),
            bindingInflater = { inflater, parent, _ ->
                ItemQteamLayoutBinding.inflate(inflater, parent, false)
            },
            onBind = { center, itemBinding, position ->
                itemBinding.trainingCenterName.text = "Training Center Name: ${center.trainingCenterName}"
                itemBinding.trainingCenterAddress.text = "Training Center Address: ${center.trainingCenterAddress}"
                itemBinding.senctionOrder.text = "Sanction Order: ${center.senctionOrder}"
                itemBinding.districtName.text = "District Name: ${center.districtName}"
                itemBinding.tcBoyCap.text = "Tc Male Capacity: ${center.tcMaleCapacity}"
                itemBinding.tcFemaleCap.text = "Tc Female Capacity: ${center.tcFemaleCapacity}"
                itemBinding.tcTotalCap.text = "Training Center Total Capacity: ${center.tcCapacity}"

                itemBinding.root.setOnClickListener {
                   // onItemClick(center)
                    handleTrainingCenterClick(center)
                }
            },
            noDataConfig = NoDataConfig(
                title = "No Training Centers",
                description = "No training centers available for verification",
                iconRes = R.drawable.no_data
            )
        )
    }

    private fun onItemClick(center: TrainingCenter) {
        val action = SrlmVerListFragmentDirections.actionSrlmVerListFragmentToSrlmVerificationForm(
            center.trainingCenterId.toString(),
            center.trainingCenterName,
            center.senctionOrder
        )
        findNavController().navigate(action)
        logFragmentEvent("Training_Center_Selected", center.trainingCenterName)
    }

    private fun observeViewModel() {
        viewModel.trainingCenters.observe(viewLifecycleOwner) { result ->
            hideProgressBar()
            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull()?.wrappedList,
                onSuccess = { data ->
                    data?.let {
 //                       Log.d("CHECk LAN ,LAt---->",data.toString())
//                        latitude=data.firstOrNull()?.latitude!!.toDouble()
//                        longitude=data.firstOrNull()?.longitude!!.toDouble()
                        trainingCentersList.clear()
                        trainingCentersList.addAll(it)
                        updateRecyclerViewData(binding.recyclerView.id, trainingCentersList)
                    }
                },
                onNoData = {
                    showToast("No data available.")
                    trainingCentersList.clear()
                    updateRecyclerViewData(binding.recyclerView.id, trainingCentersList)
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
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



    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                logFragmentEvent("Location_Permission_Granted")
            } else {
                showToast(" Location permission denied")
                logFragmentEvent("Location_Permission_Denied")
            }
        }

    private fun handleTrainingCenterClick(center: TrainingCenter) {
        logFragmentEvent("Training_Center_Clicked", center.trainingCenterId.toString())
        //onItemClick(center)
        checkGeofence(center) { inside, location ->
            if (inside) {
                onItemClick(center)
            } else {
                showErrorToast("You are outside the training center area")
            }


        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
        }
    }



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
                        centerLat =  center.letitude!!.toDouble(),
                        centerLng =  center.longitude!!.toDouble(),
                        radiusInMeters = center.radius!!.toFloat()
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



    // Maintain original method names for compatibility
    fun showProgressBar() {
        showProgressDialog("Loading training centers...")
    }

    fun hideProgressBar() {
        dismissProgressDialog()
    }


//    fun updateTrainingCentersData(newList: List<TrainingCenter>) {
//        trainingCentersList.clear()
//        trainingCentersList.addAll(newList)
//        updateRecyclerViewData(binding.recyclerView.id, trainingCentersList)
//    }
//
//    fun getCurrentTrainingCenters(): List<TrainingCenter> {
//        return getRecyclerViewItems(binding.recyclerView.id)
//    }
//
//    fun clearTrainingCenters() {
//        trainingCentersList.clear()
//        updateRecyclerViewData(binding.recyclerView.id, trainingCentersList)
//    }
//
//    fun filterTrainingCenters(query: String) {
//        val filteredList = if (query.isEmpty()) {
//            trainingCentersList
//        } else {
//            trainingCentersList.filter {
//                it.trainingCenterName.contains(query, ignoreCase = true) ||
//                        it.districtName.contains(query, ignoreCase = true) ||
//                        it.senctionOrder.contains(query, ignoreCase = true)
//            }
//        }
//        updateRecyclerViewData(binding.recyclerView.id, filteredList)
//    }
}