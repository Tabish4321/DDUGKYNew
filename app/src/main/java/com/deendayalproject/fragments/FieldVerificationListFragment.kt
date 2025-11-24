package com.deendayalproject.fragments

import SharedViewModel
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.adapter.FieldVerificationAdapter
import com.deendayalproject.adapter.TrainingQAdapter
import com.deendayalproject.databinding.FragmentFieldVerificationListBinding
import com.deendayalproject.model.request.FieldVerificationListRequest
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.util.AppUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


class FieldVerificationListFragment : Fragment() {
    private var _binding: FragmentFieldVerificationListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: FieldVerificationAdapter

    private var latitude = 26.2153
    private var longitude = 84.3588
    private var radius = 500000000f

    private lateinit var fusedLocationClient: FusedLocationProviderClient





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFieldVerificationListBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())



        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        adapter = FieldVerificationAdapter(emptyList()) { item ->
            // navigate using captiveEmpanelmentId (or other fields)
            val id = item.captiveEmpanelmentId?.toString() ?: ""
            val action = FieldVerificationListFragmentDirections
                .actionFieldVerificationListFragmentToFieldVerificationFormFragment(
                    id,
                    item.prnNo ?: ""
                )
            findNavController().navigate(action)
        }

        binding.backButton.setOnClickListener {

            findNavController().navigateUp()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        observeViewModel()

        val request = FieldVerificationListRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.fetchFieldVerificationList(request, AppUtil.getSavedTokenPreference(requireContext()))

    }
    private fun observeViewModel() {
        viewModel.fieldprnDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 ->{
                        adapter.updateData(it.wrappedList ?: emptyList())
                        adapter.notifyDataSetChanged()

                    }
                    202 -> {
                        adapter.updateData(it.wrappedList ?: emptyList())
                        adapter.notifyDataSetChanged()
                        Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    }
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


    private fun checkGeofence(
        context: Context,
        latitude: Double,
        longitude: Double,
        radiusInMeters: Float,
        progressBar: ProgressBar,
        onResult: (inside: Boolean, location: Location?) -> Unit
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 👉 Ask user for permission
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

        // Show progress bar
        progressBar.visibility = View.VISIBLE

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                progressBar.visibility = View.GONE
                if (location != null) {
                    val inside = isUserInGeofence(
                        userLat = location.latitude,
                        userLng = location.longitude,
                        centerLat = latitude,
                        centerLng = longitude,
                        radiusInMeters = radiusInMeters
                    )
                    onResult(inside, location)
                } else {
                    Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()
                    onResult(false, null)
                }
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Failed to get location", Toast.LENGTH_SHORT).show()
                onResult(false, null)
            }
    }


    // Simple geofence check
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
                // Permission granted → retry geofence check

            } else {
                Toast.makeText(requireContext(), "❌ Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }


}