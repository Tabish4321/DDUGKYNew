package com.deendayalproject.fragments

import SharedViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.os.SystemClock
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentFacultyAttendanceBinding
import com.deendayalproject.model.request.AttendanceCandidateListReq
import com.deendayalproject.model.request.InsertFacultyAttendance
import com.deendayalproject.model.response.FacultyAttendance
import com.deendayalproject.model.response.FacultyDetailsRes
import com.deendayalproject.uidai.XstreamCommonMethods
import com.deendayalproject.uidai.XstreamCommonMethods.respDecodedXmlToPojoAuth
import com.deendayalproject.uidai.capture.CaptureResponse
import com.deendayalproject.uidai.ekyc.IntentResponse
import com.deendayalproject.uidai.ekyc.UidaiKycRequest
import com.deendayalproject.uidai.ekyc.UidaiResp
import com.deendayalproject.util.AESCryptography
import com.deendayalproject.util.AppConstant
import com.deendayalproject.util.AppConstant.LANGUAGE
import com.deendayalproject.util.AppConstant.PRODUCTION
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.decodeBase64
import com.deendayalproject.util.toastLong
import com.deendayalproject.util.toastShort
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.toString

class FacultyAttendanceFragment : BaseFragment<FragmentFacultyAttendanceBinding>(
    FragmentFacultyAttendanceBinding::inflate) {

    private lateinit var viewModel: SharedViewModel

    private var batchId = 0
    private var checkIn = ""
    private var totalHours = ""
    private var trainerCode = 0
    private var batchRegNo =""
    private var checkOut = ""
    private var attendanceFlag = ""
    private var decryptedAadhaar = ""
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var radius: Float = 10000000000f
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var intentResponse: IntentResponse? = null
    private val neededPermissions = arrayOf(Manifest.permission.CAMERA)
    private var startTime: Long = 0
    private var userPhotoUIADI: Bitmap? = null
    private var ekycImage: String = ""
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
    private var facultyName = ""


    override fun initializeViews() {
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        startClock()
        batchId = arguments?.getInt("batchId",0)!!
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        checkLocationPermission()
        binding.tvCurrentDate.text= AppUtil.getCurrentDateForAttendance()



        viewModel.getFacultyDetails(
            AttendanceCandidateListReq(batchId.toInt(),
                BuildConfig.VERSION_NAME
            ), AppUtil.getSavedTokenPreference(requireContext())
        )

        showProgressDialog("Loading...")

        collectFacultyDetails()


    }

    override fun setupObservers() {
    }

    override fun setupClickListeners() {

        binding.btnCheckIn.setOnClickListener {



            if (attendanceFlag=="checkin"){
                //for audit
                showProgressDialog("Loading...")
                invokeCaptureIntent()
                /* val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                 val currentTime = LocalTime.now()
                 val formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))  // ✅ 24-hour format\
                 val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")




                viewModel.insertFacultyAttandance(
                    InsertFacultyAttendance(
                        BuildConfig.VERSION_NAME,
                        batchId,
                        trainerCode.toString(),
                        batchRegNo,
                        formattedTime,
                        "",
                        currentDate,
                        "",
                        AppUtil.getAndroidId(requireContext())

                    ),AppUtil.getSavedTokenPreference(requireContext()))

                collectFacultyInsertAttendance()*/



            }


            else toastLong("Checkin Already marked")





        }

        binding.btnCheckOut.setOnClickListener {


            if (attendanceFlag=="checkout"){

                //for audit
                showProgressDialog("Loading...")
                invokeCaptureIntent()


              /*   val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                 val currentTime = LocalTime.now()
                 val formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))  // ✅ 24-hour format\
                 val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                 val checkInTime = LocalTime.parse(checkIn, timeFormatter)
                 val checkOutTime = LocalTime.parse(formattedTime, timeFormatter)
                 val duration = Duration.between(checkInTime, checkOutTime)


                 val hours = duration.toHours()
                 val minutes = (duration.toMinutes() % 60)
                 val seconds = (duration.seconds % 60)

                 val totalHoursValue = String.format("%02d:%02d:%02d", hours, minutes, seconds)


                viewModel.insertFacultyAttandance(
                    InsertFacultyAttendance(
                        BuildConfig.VERSION_NAME,
                        batchId,
                        trainerCode.toString(),
                        batchRegNo,
                        "",
                        formattedTime,
                        currentDate,
                        totalHoursValue,
                        AppUtil.getAndroidId(requireContext())

                    ),AppUtil.getSavedTokenPreference(requireContext()))

                collectFacultyInsertAttendance()*/

            }


            else toastLong("Kindly mark checkin First")




        }

    }

    override fun loadInitialData() {
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




    private fun showAlertGeoFancingDialog(context: Context, title: String, message: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            findNavController().navigateUp()
        }

        val dialog = builder.create()
        dialog.setCancelable(false)  // Prevent outside touch dismissal
        dialog.setCanceledOnTouchOutside(false) // Extra safety: disable outside clicks
        dialog.show()
    }




    private val startUidaiAuthResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data

                    if (intent != null) {
                        val captureResponse =
                            intent.getStringExtra(AppConstant.CAPTURE_INTENT_RESPONSE_DATA)

                        if (!captureResponse.isNullOrEmpty()) {
                            handleCaptureResponse(captureResponse)
                        } else {
                            toastShort("Capture response is empty.")
                        }
                    } else {
                        toastShort("Failed to get capture response data.")
                    }
                } else {
                    toastLong("Failed to capture data.")
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
                toastShort("Error: Missing data in result.")

            } catch (e: Exception) {
                e.printStackTrace()
                toastShort("An error occurred while processing the result.")
            }
        }

    private fun handleCaptureResponse(captureResponse: String) {
        try {


          //  toastShort(decryptedAadhaar)
            // Parse the capture response XML to an object
            val response = CaptureResponse.fromXML(captureResponse)

            if (response.isSuccess) {

                showProgressDialog("please Wait")
                // Process the response to generate the PoiType or other required fields
                val poiType = XstreamCommonMethods.processPidBlockEkyc(
                    response.toXML(),
                    decryptedAadhaar,

                    false,
                    requireContext()
                )


                // Define Pre-Production URL (use a constant or environment configuration in production)
                //  val authURL = "http://10.247.252.95:8080/NicASAServer/ASAMain" //preProd
                val authURL = "http://10.247.252.93:8080/NicASAServer/ASAMain"  //Prod

                // Record the start time for elapsed time computation
                startTime = SystemClock.elapsedRealtime()


                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.postOnAUAFaceAuthNREGA(
                        AppConstant.FACE_AUTH_UIADI,
                        UidaiKycRequest(poiType, authURL))
                }

                collectFaceAuthResponse()
                // Handle Aadhaar authentication or additional processing here if required
            } else {
                dismissProgressDialog()
                toastLong(getString(R.string.failed_attendance))

            }


        } catch (e: SecurityException) {
            // Handle camera permission-related issues
            dismissProgressDialog()
            e.printStackTrace()

            toastShort("Camera permission is required for this feature.")
        } catch (e: IllegalArgumentException) {
            // Handle cases where the response parsing might fail
            dismissProgressDialog()
            e.printStackTrace()
            toastShort("Invalid Capture Response format.")
        } catch (e: Exception) {
            // Catch all other exceptions
            dismissProgressDialog()
            e.printStackTrace()
            toastShort("An error occurred while processing the response.")
        }
    }


    private fun getTransactionID(): String {
        val secureRandom = SecureRandom()
        return secureRandom.nextInt(9999).toString()
    }


    private fun invokeCaptureIntent() {

        try {
            val intent1 = Intent(AppConstant.CAPTURE_INTENT)
            intent1.putExtra(
                AppConstant.CAPTURE_INTENT_REQUEST,
                createPidOptions(getTransactionID(), "auth")
            )
            startUidaiAuthResult.launch(intent1)

            // val packageName = "com.example.otherapp" // Replace with the target app's package name
            val intent =
                requireContext().packageManager.getLaunchIntentForPackage(AppConstant.CAPTURE_INTENT)
            intent?.putExtra(
                AppConstant.CAPTURE_INTENT_REQUEST,
                createPidOptions(getTransactionID(), "auth")
            )
            if (intent != null) {
                startActivity(intent)
            }
        } catch (exp: Exception) {
        }

    }

    private fun createPidOptions(txnId: String, purpose: String): String {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<PidOptions ver=\"1.0\" env=\"$PRODUCTION\">\n" + "   <Opts fCount=\"\" fType=\"\" iCount=\"\" iType=\"\" pCount=\"\" pType=\"\" format=\"\" pidVer=\"2.0\" timeout=\"\" otp=\"\" wadh=\"${AppConstant.WADH_KEY}\" posh=\"\" />\n" + "   <CustOpts>\n" + "      <Param name=\"txnId\" value=\"${txnId}\"/>\n" + "      <Param name=\"purpose\" value=\"$purpose\"/>\n" + "      <Param name=\"language\" value=\"$LANGUAGE}\"/>\n" + "   </CustOpts>\n" + "</PidOptions>"
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



                                val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                                val currentTime = LocalTime.now()
                                val formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))  // ✅ 24-hour format\
                                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")


                                if (attendanceFlag== "checkin"){


                                    viewModel.insertFacultyAttandance(
                                        InsertFacultyAttendance(
                                            BuildConfig.VERSION_NAME,
                                            batchId,
                                            trainerCode.toString(),
                                            batchRegNo,
                                            formattedTime,
                                            "",
                                            currentDate,
                                            "",
                                            AppUtil.getAndroidId(requireContext())

                                        ),AppUtil.getSavedTokenPreference(requireContext()))
                                }
                                else{

                                    val checkInTime = LocalTime.parse(checkIn, timeFormatter)
                                    val checkOutTime = LocalTime.parse(formattedTime, timeFormatter)
                                    val duration = Duration.between(checkInTime, checkOutTime)


                                    val hours = duration.toHours()
                                    val minutes = (duration.toMinutes() % 60)
                                    val seconds = (duration.seconds % 60)

                                    val totalHoursValue = String.format("%02d:%02d:%02d", hours, minutes, seconds)


                                    viewModel.insertFacultyAttandance(
                                        InsertFacultyAttendance(
                                            BuildConfig.VERSION_NAME,
                                            batchId,
                                            trainerCode.toString(),
                                            batchRegNo,
                                            "",
                                            formattedTime,
                                            currentDate,
                                            totalHoursValue,
                                            AppUtil.getAndroidId(requireContext())

                                        ),AppUtil.getSavedTokenPreference(requireContext()))


                                }

                                collectFacultyInsertAttendance()


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


    private fun collectFacultyDetails() {
        lifecycleScope.launch {
            viewModel.getFacultyDetails.observe(viewLifecycleOwner) { it ->
                it.onSuccess { response ->
                    dismissProgressDialog()

                    when (response.responseCode) {
                        200 -> {
                            for (x in response.wrappedList) {

                                binding.tvAadhaarName.text= x.facultyName
                                binding.tvRollNoValue.text= x.loginId.toString()
                                binding.tvAaadharMobile.text= x.mobileNo
                                binding.tvEmailMobile.text= x.emailId
                                binding.tvAaadharGender.text= x.gender
                                binding.tvAaadharDob.text= x.dob

                                decryptedAadhaar = AESCryptography.decryptIntoString(x.aadhaarNo,
                                    AppConstant.ENCRYPT_KEY,
                                    AppConstant.ENCRYPT_IV_KEY)

                                latitude= x.latitude.toDouble()
                                longitude= x.langitude.toDouble()
                                //radius= x.radius.toFloat()
                                attendanceFlag = x.attendanceFlag
                                checkIn = x.checkIn//00:00
                                checkOut = x.checkOut
                                totalHours= x.totalHours
                                facultyName= x.facultyName
                                trainerCode= x.loginId
                                batchRegNo= x.batchRegNo




                                binding.tvCheckInValue.text= x.checkIn
                                binding.tvCheckOutValue.text= x.checkOut
                                binding.tvTotalHoursValue.text= x.totalHours


                            }
                            getCurrentLocation { location ->
                                if (location != null) {
                                     val isInside = isUserInsideGeofence(location, latitude, longitude, radius)
                                  //  val isInside = isUserInsideGeofence(location, 26.2153, 84.3588, radius)
                                    if (isInside) {

                                        //    findNavController().navigate(SdrListFragmentDirections.actionSdrListFragmentToSdrVisitReport(formName,instituteName,finYear,instituteId))
                                    } else {
                                        showAlertGeoFancingDialog(requireContext(),"Alert","❌ You are outside the institute area")

                                    }
                                } else {
                                    toastLong("❌ Failed to retrieve current location")
                                    showAlertGeoFancingDialog(requireContext(),"Alert","❌ Failed to retrieve current location Kindly on your gps from settings")
                                }
                            }

                        }

                        //  populateSpinnerVillage((response.wrappedList ?: emptyList()) as ArrayList<VillageModel?>, spinnerSelectULB )

                        202 -> Toast.makeText(
                            requireContext(), "No data available.", Toast.LENGTH_SHORT
                        ).show()

                        301 -> Toast.makeText(
                            requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                        ).show()

                        401 -> AppUtil.showSessionExpiredDialog(
                            findNavController(), requireContext()
                        )
                    }
                }
                it.onFailure {
                    dismissProgressDialog()

                    Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
    private fun collectFacultyInsertAttendance() {
        lifecycleScope.launch {
            viewModel.insertFacultyAttandance.observe(viewLifecycleOwner) { it ->
                it.onSuccess { response ->
                    dismissProgressDialog()

                    when (response.responseCode) {
                        200 -> {

                            showBottomSheet(userPhotoUIADI,name,gender,dob,careOf)

                        }

                        //  populateSpinnerVillage((response.wrappedList ?: emptyList()) as ArrayList<VillageModel?>, spinnerSelectULB )

                        202 -> Toast.makeText(
                            requireContext(), "No data available.", Toast.LENGTH_SHORT
                        ).show()

                        301 -> Toast.makeText(
                            requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                        ).show()

                        401 -> AppUtil.showSessionExpiredDialog(
                            findNavController(), requireContext()
                        )
                    }
                }
                it.onFailure {
                    dismissProgressDialog()

                    Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }



}