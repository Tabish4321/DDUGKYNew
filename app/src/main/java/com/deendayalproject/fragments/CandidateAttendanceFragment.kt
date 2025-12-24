package com.deendayalproject.fragments

import SharedViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.util.Base64
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentCandidateAttendanceBinding
import com.deendayalproject.util.toastLong
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.deendayalproject.R
import com.deendayalproject.model.response.AttendanceCheckRes
import com.deendayalproject.model.response.AttendanceData
import com.deendayalproject.uidai.XstreamCommonMethods
import com.deendayalproject.uidai.XstreamCommonMethods.respDecodedXmlToPojoAuth
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.decodeBase64
import com.deendayalproject.util.toastShort


const val CAMERA_REQUEST = 101
class CandidateAttendanceFragment : BaseFragment<FragmentCandidateAttendanceBinding>(
    FragmentCandidateAttendanceBinding::inflate) {

    private lateinit var viewModel: SharedViewModel
    private var name = ""
    private var dob = ""
    private var gender = ""
    private var careOf = ""
    private var state = ""
    private var dist = ""
    private var block = ""
    private var po = ""
    private var pinCode = ""
    private var street = ""
    private var village = ""
    private var photo = ""
    private var candidateId = ""
    private var candidateName = ""
    private var candidateMobile = ""
    private var candidateEmail = ""
    private var candidateGender = ""
    private var candidateDob = ""
    private var candidateDp = ""
    private var batchId = ""
    private var aadhaarNo = ""
    private var candidateRollNo = ""
    private var checkIn = ""
    private var totalHours = ""
    private var checkOut = ""
    private var attendanceFlag = ""
    private var decryptedAadhaar = ""
    private var startTime: Long = 0
    private var userPhotoUIADI: Bitmap? = null
    private var ekycImage: String = ""

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /*  private var latitude: Double = 0.0
      private var longitude: Double = 0.0
      var radius: Float = 100f*/
    private var latitude = 26.2153  // Example geofence latitude
    private var longitude = 84.3588  // Example geofence longitude
    private var radius = 50000000f  // 100 meters radius



    override fun initializeViews() {
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        startClock()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermission()

    }

    override fun setupObservers() {
    }

    override fun setupClickListeners() {

        binding.tvCurrentDate.text= AppUtil.getCurrentDateForAttendance()



    }

    override fun loadInitialData() {




        candidateId = arguments?.getString("candidateId").toString()
        candidateName = arguments?.getString("candidateName").toString()
        candidateMobile = arguments?.getString("candidateMobile").toString()
        candidateEmail = arguments?.getString("candidateEmail").toString()
        candidateGender = arguments?.getString("candidateGender").toString()
        candidateDob = arguments?.getString("candidateDob").toString()
        candidateDp = arguments?.getString("candidateDp").toString()
        batchId = arguments?.getString("batchId").toString()
        candidateRollNo = arguments?.getString("candidateRollNo").toString()
        aadhaarNo = arguments?.getString("aadhaarNo").toString()


        binding.tvAadhaarName.text=candidateName
        binding.tvRollNoValue.text=candidateRollNo
        binding.tvEmailMobile.text=candidateEmail
        binding.tvAaadharMobile.text=candidateMobile
        binding.tvAaadharGender.text=candidateGender
        binding.tvAaadharDob.text=candidateDob


        if (candidateDp != ""){

            loadBase64Image(candidateDp, binding.circleImageView)

        }
        else
            binding.circleImageView.setImageResource(R.drawable.person)


    }

    private fun startClock() {
        lifecycleScope.launch {
            while (isAdded) { // Check if fragment is attached
                val currentTime = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
                binding.currentTime.text = currentTime
                delay(1000) // Update every second
            }
        }
    }




    @SuppressLint("SuspiciousIndentation")
    private fun showBottomSheet(
        image: Bitmap?,
        name: String,
        gender: String,
        dateOfBirth: String,
        careOf: String
    ) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        // Inflate the layout
        val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(view)

        // Prevent closing when tapping outside
        bottomSheetDialog.setCanceledOnTouchOutside(false)

        // Find views
        val imageView = view.findViewById<ImageView>(R.id.circleImageView)
        val nameView = view.findViewById<TextView>(R.id.attendancCandidateName)
        val genderView = view.findViewById<TextView>(R.id.attendancGender)
        val dobView = view.findViewById<TextView>(R.id.attendancCDob)
        val careOfView = view.findViewById<TextView>(R.id.attendancCareOf)
        val okButton = view.findViewById<TextView>(R.id.tvLogin)

        // Set data
        imageView.setImageBitmap(image)
        nameView.text = name
        genderView.text = gender
        dobView.text = dateOfBirth
        careOfView.text = careOf

        // Handle OK button click
        okButton.setOnClickListener {

            bottomSheetDialog.dismiss()
            findNavController().navigateUp()
        }

        // Handle back button press
        bottomSheetDialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                // Show a confirmation dialog before closing
                AlertDialog.Builder(requireContext())
                    .setTitle("Exit")
                    .setMessage("Do you want to close this screen?")
                    .setPositiveButton("Yes") { _, _ ->
                        bottomSheetDialog.dismiss()
                    }
                    .setNegativeButton("No", null)
                    .show()
                return@setOnKeyListener true
            }
            false
        }

        // Show the BottomSheetDialog
        bottomSheetDialog.show()
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

    private fun getCurrentLocation(onLocationResult: (Location?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            toastLong("❌ Location permission not granted")
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            onLocationResult(location)
        }.addOnFailureListener {
            onLocationResult(null)
        }
    }

    private fun isUserInsideGeofence(
        currentLocation: Location,
        lat: Double,
        lng: Double,
        radius: Float
    ): Boolean {
        val targetLocation = Location("").apply {
            latitude = lat
            longitude = lng
        }
        val distance = currentLocation.distanceTo(targetLocation)
        return distance <= radius
    }

    private fun loadBase64Image(base64String: String?, imageView: ImageView) {
        if (base64String.isNullOrEmpty()) {
            return  // Avoid processing if the string is null or empty
        }

        try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            // Set bitmap to ImageView
            imageView.setImageBitmap(bitmap)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    private fun collectFaceAuthResponse() {

        viewModel.postOnAUAFaceAuthNREGA
            .observe(viewLifecycleOwner) { result ->

                when {
                    result.isSuccess -> {
                        dismissProgressDialog()

                        val uidaiData = result.getOrNull()
                        if (uidaiData == null) {
                            toastShort("Server error from UIDAI. Please try again.")
                            return@observe
                        }

                        try {
                            val kycResp =
                                XstreamCommonMethods.respDecodedXmlToPojoEkyc(
                                    uidaiData.PostOnAUA_Face_authResult
                                )


                            if (kycResp.isSuccess) {

                                // 🔹 Photo decode
                                val bytes = Base64.decode(kycResp.uidData.pht, Base64.DEFAULT)
                                val bitmap =
                                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                                userPhotoUIADI = bitmap
                                ekycImage = kycResp.uidData.pht ?: ""

                                name = kycResp.uidData.poi.name ?: "N/A"
                                photo = kycResp.uidData.pht ?: "N/A"
                                gender = kycResp.uidData.poi.gender ?: "N/A"
                                dob = kycResp.uidData.poi.dob ?: "N/A"
                                careOf = kycResp.uidData.poa.co ?: "N/A"
                                state = kycResp.uidData.poa.state ?: "N/A"
                                dist = kycResp.uidData.poa.dist ?: "N/A"
                                block = kycResp.uidData.poa.subdist ?: "N/A"
                                village = kycResp.uidData.poa.vtc ?: "N/A"
                                street = kycResp.uidData.poa.loc ?: "N/A"
                                po = kycResp.uidData.poa.po ?: "N/A"
                                pinCode = kycResp.uidData.poa.pc ?: "N/A"



                                showBottomSheet(userPhotoUIADI,name,gender,dob,careOf)


                            }
                            else {
                                dismissProgressDialog()
                                val decodedRar = decodeBase64(kycResp.rar)
                                val authRes = decodedRar?.let {
                                    respDecodedXmlToPojoAuth(it)
                                }
                                val errorDesc =
                                    XstreamCommonMethods.getAuthErrorDescription(authRes?.info)
                                toastShort(errorDesc ?: "EKYC Failed")
                            }

                        } catch (e: Exception) {
                            dismissProgressDialog()
                            e.printStackTrace()
                            toastShort("Error processing KYC response")
                        }
                    }

                    result.isFailure -> {
                        dismissProgressDialog()
                        val error = result.exceptionOrNull()
                        toastShort(error?.message ?: "Something went wrong")
                    }

                    else -> {
                        // Loading case (agar handleApiCall me post karte ho)
                        showProgressDialog()
                    }
                }
            }
    }





}