package com.deendayalproject.fragments.ojt.ojt_SRLM


import SharedViewModel
import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Base64
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.MediaController
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.AppUtil.hasStoragePermission
import com.deendayalproject.util.ProgressDialogUtil
import com.deendayalproject.util.toastLong
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.io.ByteArrayOutputStream

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject

import java.io.FileOutputStream
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.apply
import kotlin.collections.all
import kotlin.collections.any
import kotlin.collections.isNotEmpty
import kotlin.io.copyTo
import kotlin.io.use
import kotlin.jvm.java
import kotlin.let
import kotlin.onFailure
import kotlin.onSuccess
import kotlin.text.isEmpty
import kotlin.text.isNullOrBlank
import kotlin.text.trim
import kotlin.toString

import android.hardware.Camera
import android.location.Geocoder
import java.io.File
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.addCallback
import androidx.annotation.RequiresPermission
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.deendayalproject.databinding.FragmentOnJobTrainingSrlmBinding
import com.deendayalproject.model.response.ChildSRLM
import com.deendayalproject.network.SecurePreferenceManager.getToken
import com.deendayalproject.util.ProgressDialogUtil.dismissProgressDialog
import com.deendayalproject.util.ProgressDialogUtil.showProgressDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class SRLMBottomDialog( private val batch: List<ChildSRLM>) :  DialogFragment(), SurfaceHolder.Callback {
private var _binding: FragmentOnJobTrainingSrlmBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel

    private var selectedAnswer = ""
    private var selectednominatedAnswer = ""
    private var selectedinstrumentAnswer = ""
    private var selectedmaterialsAnswer = ""
    private var selectedfacilitiesAnswer = ""
    private var selectedstipinedAnswer = ""
    private var AreBoardingAandLoadingFacilitiesProvided = ""
    private var selectedRandomDateStr: String? = null
    private var HowMuchSelectedDate: String? = null
    // Base64 holders

    private lateinit var surfaceHolder: SurfaceHolder

    private var videoPath: String = ""



    private var isPlaying = false
    private var isSelfie = false
    private var camera: Camera? = null
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: File? = null

    private var isRecording = false

    private var currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK

    private var countDownTimer: CountDownTimer? = null

    private val RECORD_TIME = 180000L


    private var remainingRecordingTime = RECORD_TIME

    private var isPaused = false
//    private val RECORD_TIME = 60000L

    private var completed = false

    // playback timer
    private val handler = android.os.Handler(Looper.getMainLooper())
    private var playbackRunnable: Runnable? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    //    private var Bindinglatitude = 27.034750
//    private var Bindinglongitutde = 79.487056
//    var videoPath: String? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private var currentImageView: ImageView? = null
    private var image1Base64 = ""
    var radius: Float = 100f
    private var locationAddress = ""

    //    private var compressDialog: ProgressDialog? = null
    private var isProcessingOJTFullScreenDialog = false
    private lateinit var textToSpeech: TextToSpeech
    var REQUEST_CODE_VIDEO_CAPTURE: Int = 2607
    private val UTTERANCE_ID = "TTS_UTTERANCE"
    private var currentSpeakingView: ImageView? = null
    private var isSpeaking = false


    private var selectedPosition = -1
    private var isProfileVisible = false
    private var istrainingDetailsVisible = false
    private var isquestionsDetailsVisible = false


    private var recordedVideoUri: Uri? = null
    private var finalVideoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
        // 👉 Lock screen in Portrait mode
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requireActivity().onBackPressedDispatcher.addCallback(this) {
//            if (isRecording) stopRecordingManually()
            dismiss()
            deleteVideo()

        }

    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentOnJobTrainingSrlmBinding.inflate(layoutInflater)

        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(true)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())





        initTextToSpeech()
        setupClickListeners()
        setupApiObserver()
//        setupRadioListeners()

        return dialog
    }

    // 🔹 Force full screen size
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation")
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }


        dialog?.setContentView(binding.root)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        initTextToSpeech()
        setupClickListeners()
        setupApiObserver()
//        setupRadioListeners()
        // make full screen
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.setCanceledOnTouchOutside(true)

        initTextToSpeech()
        setupClickListeners()
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude



                // Fetch and update address
                getAddressFromLocation(latitude!!, longitude!!)
            } else {
                // If last known location is null, request a fresh location update

            }
        }.addOnFailureListener {

            binding.address.text = getString(R.string.address_not_found)
            Log.e("LocationError", "Failed to get location: ${it.message}")
        }
        // image / document selection
//        setupVideoCapture()


//        setupDatePicker(batch.ojtStartDate)

        checkAndRequestPermissions()
        startPulseAnimation()
//        code commit 27/02/26
        setupDatePicker(batch[0].ojtStartDate)


        binding.ivRandomCalendar.setOnClickListener {
            val baseDateStr = batch[0].ojtStartDate  // example
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val baseDate = sdf.parse(baseDateStr)

            if (baseDate != null) {
                val calendar = Calendar.getInstance()
                calendar.time = baseDate

                // +6 days range
                val maxCalendar = Calendar.getInstance()
                maxCalendar.time = baseDate
                maxCalendar.add(Calendar.DAY_OF_MONTH, 365)

                showRandomDatePicker(baseDate, maxCalendar.time)
            } else {
                Toast.makeText(requireContext(), "Invalid date", Toast.LENGTH_SHORT).show()
            }


        }
        // set values
//        what_activity_did_you_do_a_random_day_to_be_chosen_by_the_ytem_from_last_6_days
//        binding.tvPiaName.text = batch.piaName
        binding.tvPiaName.text = batch[0].piaName

        binding.tvActivityTitle.text =
//            "4 - What activity did you do  on "+batch.ojtStartDate)"
//            "4 - What activity did you do on ?"
            getString(R.string.what_activity_did_you_do_a_random_day_to_be_chosen_by_the_ytem_from_last_6_days)
        binding.tvCenterName.text = batch[0].trainingCenterName
        binding.tvCandidate.text = batch[0].candidateName
        binding.tvKpid.text = batch[0].candidateId
        binding.tvStatus.text = batch[0].status



        binding.tvStipned.text =
            getString(R.string.how_much_stipend_are_you_eligible_for) + "\n" + "₹" + batch[0].stipend
        binding.tvFatherName.text = batch[0].fatherName
        binding.tvDistrict.text = batch[0].districtName
        binding.tvTrainingStart.text = batch[0].batchStartDate
        binding.tvTrainingEnd.text = batch[0].batchEndDate
        binding.tvOjtStart.text = batch[0].ojtStartDate ?: ""
        binding.tvOjtEnd.text = batch[0].ojtEndDate ?: ""
        binding.tvOjtLocation.text = batch[0].workplaceName
        binding.tvOjtIndustryName.text = batch[0].employeersName
        binding.tvMobileNo.text=batch[0].mobileNo

        binding.ivCalendarHowMuch.setOnClickListener {
            openCalendar()
        }
        var imageData = batch[0].candidateImage




        if (imageData != null) {
            val candidateImage = base64ToBitmap(imageData)
            binding.circleImageView.setImageBitmap(candidateImage)
        }

//        binding.profileImageLayout.visibility= View.VISIBLE
//        binding.profileImageLayout.visibility =
//            if (selectedPosition) View.VISIBLE else View.GONE
//
//        binding.profileImageLayout.setOnClickListener {
//
////                listener(batch)
//
//            selectedPosition = if (selectedPosition == selectedPosition) -1 else selectedPosition


//        }

//        binding.btnRecord.setOnClickListener {
//            dismissKeyboard()
//            checkPermissionAndRecord()
//        }


        binding.imgnavigationttrainingDetails.setOnClickListener {
            istrainingDetailsVisible = !istrainingDetailsVisible

            binding.trainingDetailsLayout.visibility =
                if (istrainingDetailsVisible) View.VISIBLE else View.GONE
            binding.imgnavigationttrainingDetails.setBackgroundResource(R.drawable.baseline_ojt_arrow_down_24)


            binding.imgnavigationttrainingDetails.setBackgroundResource(
                if (istrainingDetailsVisible)
                    R.drawable.outline_ojt_arrow_up_24
                else
                    R.drawable.baseline_ojt_arrow_down_24

            )
        }





        binding.imgnavigationQuestionttrainingDetails.setOnClickListener {

            scrollToView(binding.radioGroupYesNoNa)
            isquestionsDetailsVisible = !isquestionsDetailsVisible

            binding.trainingQuestionDetailsLayout.visibility =
                if (isquestionsDetailsVisible) View.VISIBLE else View.GONE
            binding.imgnavigationQuestionttrainingDetails.setBackgroundResource(R.drawable.baseline_ojt_arrow_down_24)
            binding.imgnavigationQuestionttrainingDetails.setBackgroundResource(
                if (isquestionsDetailsVisible)
                    R.drawable.outline_ojt_arrow_up_24
                else
                    R.drawable.baseline_ojt_arrow_down_24
            )
        }

        binding.imgnavigation.setOnClickListener {

            isProfileVisible = !isProfileVisible

            binding.profileImageLayout.visibility =
                if (isProfileVisible) View.VISIBLE else View.GONE
            binding.imgnavigation.setBackgroundResource(R.drawable.baseline_ojt_arrow_down_24)
            binding.imgnavigation.setBackgroundResource(
                if (isProfileVisible)
                    R.drawable.outline_ojt_arrow_up_24
                else
                    R.drawable.baseline_ojt_arrow_down_24
            )
        }


        binding.btnBack.setOnClickListener {
            dismiss()
            deleteVideo()
//            if (isRecording) stopRecordingManually()
        }




        binding.btnLogin.setOnClickListener {
            binding.trainingQuestionDetailsLayout.visibility = View.VISIBLE
            val token = getToken(requireContext())
//             FIRST — run validation
//            if (!validateInputs()) {
//                return@setOnClickListener
//            }

            if (selectedAnswer == "Yes") {

                if (!validateRequiredField(
                        binding.tvSelectedDate,
                        binding.tvSelectedDate.text.toString(),
                        "Please select OJT start your date"
                    )
                ) return@setOnClickListener


                if (!validateRequiredDoingTodayField(
                        binding.etOjtTrainingCenter,
                        binding.etOjtTrainingCenter.text.toString(),
                        "Please enter Activity are you doing Today"
                    )
                ) return@setOnClickListener




                if (!validateRequiredDidYouDoField(
                        binding.etSelectedRandomDate,
                        binding.etSelectedRandomDate.text.toString(),
                        "Activity did you do on "
                    )
                ) return@setOnClickListener


//            if (!validateRequiredhowManyField(
//                    binding.etOjtEnterDuringTimes,
//                    binding.etOjtEnterDuringTimes.text.toString(),
//                    "How many times during the day did"
//                )) return@setOnClickListener


                // ---------- NOMINATED ----------
                if (!validateYesNoWithRemark(
                        binding.radioGroupnominatedYesNo,
                        binding.radionominatedNo.id,
                        binding.etRemarkFieldLevelSupervisorNominated,
                        "Please enter Supervisor Nominated remark"
                    )
                ) return@setOnClickListener

                if (!validateRequiredhowManyField(
                        binding.etOjtEnterDuringTimes,
                        binding.etOjtEnterDuringTimes.text.toString(),
                        "How many times during the day did"
                    )
                ) return@setOnClickListener
                // ---------- INSTRUMENT ----------
                if (!validateYesNoWithRemark(
                        binding.radioGroupinstrumentYesNo,
                        binding.radioinstrumentNo.id,
                        binding.etRemarkareYouGivenSufficientInstument,
                        "Please enter Instrument remark"
                    )
                ) return@setOnClickListener


                // ---------- MATERIALS ----------
                if (!validateYesNoWithRemark(
                        binding.radioGroupmaterialsYesNo,
                        binding.radiomaterialsNo.id,
                        binding.etRemarkareYouGivenEnoughMaterials,
                        "Please enter Materials remark"
                    )
                ) return@setOnClickListener


//                   if (!validateRequiredhowMuchstipnedeligibleField(
//                           binding.etRemarkbordingAndLoadingFacilities,
//                           binding.etRemarkbordingAndLoadingFacilities.text.toString(),
//                           "Please enter how much stipned eligible"
//                       )) return@setOnClickListener

                if (!validateRequiredhowMuchstipnedegettingField(
                        binding.etOjtEnterYourGettingFor,
                        binding.etOjtEnterYourGettingFor.text.toString(),
                        "Please enter how much stipned getting for"
                    )
                ) return@setOnClickListener

                // ---------- FACILITIES ----------
                if (!validateYesNoWithRemark(
                        binding.radioGroupAreBoardingAandLoadingFacilitiesProvidedYesNo,
                        binding.radioAreBoardingAandLoadingFacilitiesProvidedNo.id,
                        binding.etRemarkAreBoardingAandLoadingFacilitiesProvided,
                        "Please enter Are Boarding and Loading Facilities Provided remark"
                    )
                ) return@setOnClickListener


//                   if (image1Base64.isNullOrBlank()) {
//                       Toast.makeText(
//                           requireContext(),
//                           "Please take required Image from camera",
//                           Toast.LENGTH_SHORT
//                       ).show()
//                       return@setOnClickListener
//                   }

                // Video mandatory
                if (finalVideoPath.isNullOrBlank()) {
                    Toast.makeText(
                        requireContext(),
                        "Please take required Video from camera",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }


            } else if (selectedAnswer == "No") {


                if (!validateRequiredhowMuchstipnedeligibleField(
                        binding.etNoReason,
                        binding.etNoReason.text.toString(),
                        "Please enter reason"
                    )
                ) return@setOnClickListener

//                   if (image1Base64.isNullOrBlank()) {
//                       Toast.makeText(
//                           requireContext(),
//                           "Please take required Image from camera",
//                           Toast.LENGTH_SHORT
//                       ).show()
//                       return@setOnClickListener
//                   }

                if (finalVideoPath.isNullOrBlank()) {
                    Toast.makeText(
                        requireContext(),
                        "Please take required Video from camera",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }


            }

            // ---------- DATE ----------

            else {
                Toast.makeText(
                    requireContext(),
                    "Select the Candidate available on the day of ",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // ✅ All validations passed
//                Toast.makeText(requireContext(), "Form submitted successfully", Toast.LENGTH_SHORT).show()

            // TODO: API call / save / next screen


            // THEN — continue with location check
            getCurrentLocation { location ->
                if (location != null) {
                    val isInside = isUserInsideGeofence(location, latitude, longitude, radius)
                    if (isInside) {
                        uploadCandidateOjtVerification(
                            token,
                            finalVideoPath.toString(),
                            image1Base64
                        )
                    } else {
                        showAlertGeoFancingDialog(
                            requireContext(),
                            "Alert",
                            "❌ You are outside the institute area"
                        )
                    }
                } else {
                    toastLong("❌ Failed to retrieve current location")
                    showAlertGeoFancingDialog(
                        requireContext(),
                        "Alert",
                        "❌ Failed to retrieve current location. Kindly Turn ON GPS from settings"
                    )
                }
            }
        }

        binding.image1.setOnClickListener {
            dismissKeyboard()
            openCamera(binding.image1)
        }







        binding.radioGroupYesNoNa.setOnCheckedChangeListener { _, checkedId ->
            dismissKeyboard()
            selectedAnswer = when (checkedId) {

                binding.radioYes.id -> {
                    binding.yesLayout.visibility = View.VISIBLE
                    binding.ImageLayout.visibility = View.GONE
                    binding.textInputReon.visibility = View.GONE
                    binding.VideoLinlayout.visibility = View.VISIBLE
                    binding.etNoReason.setText("")
                    videoPath= ""
                    image1Base64= ""
                    "Yes"

                }

                binding.radioNo.id -> {
                    binding.textInputReon.visibility = View.VISIBLE
                    binding.VideoLinlayout.visibility = View.VISIBLE
                    binding.ImageLayout.visibility = View.GONE
                    binding.yesLayout.visibility = View.GONE




                    selectedAnswer = ""
                    selectednominatedAnswer = ""
                    selectedinstrumentAnswer = ""
                    selectedmaterialsAnswer = ""
                    selectedfacilitiesAnswer = ""
                    selectedstipinedAnswer = ""
                    AreBoardingAandLoadingFacilitiesProvided = ""
                    selectedRandomDateStr= ""
                    HowMuchSelectedDate= ""



                    binding.tvSelectedDate.setText("")
                    binding.tvHowMuchSelectedDate.setText("")
                    binding.etOjtEnterYourGettingFor.setText("")
                    binding.etOjtEnterDuringTimes.setText("")
                    binding.etSelectedRandomDate.setText("")
                    binding.etOjtTrainingCenter.setText("")
                    binding.etNoReason.setText("")
                    binding.etRemarkbordingAndLoadingFacilities.setText("")
                    binding.etRemarkFieldLevelSupervisorNominated.getText().toString()
                    binding.etRemarkareYouGivenEnoughMaterials.setText("")
                    binding.etRemarkareYouGivenSufficientInstument.setText("")
                    binding.etRemarkAreBoardingAandLoadingFacilitiesProvided.setText("")

                    binding.radioGroupnominatedYesNo.clearCheck()
                    binding.radioGroupinstrumentYesNo.clearCheck()
                    binding.radioGroupinstrumentYesNo.clearCheck()
                    binding.radioGroupmaterialsYesNo.clearCheck()
                    binding.radioGroupStipinedYesNo.clearCheck()
                    binding.radioGroupAreBoardingAandLoadingFacilitiesProvidedYesNo.clearCheck()

                    videoPath= ""
                    image1Base64= ""





                    "No"
                }
//                }

                else -> ""
            }
//            Toast.makeText(requireContext(), selectedAnswer, Toast.LENGTH_SHORT).show()
        }

        binding.radioGroupnominatedYesNo.setOnCheckedChangeListener { _, nominatedAnswer ->

            selectednominatedAnswer = when (nominatedAnswer) {
                binding.radionominatedYes.id -> {
                    binding.textRemarkisFieldLevelSupervisorNominated.visibility = View.GONE


                    "Yes"

                }

                binding.radionominatedNo.id -> {
                    binding.textRemarkisFieldLevelSupervisorNominated.visibility = View.VISIBLE
                    "No"
                }

                else -> ""

            }
        }
        binding.radioGroupinstrumentYesNo.setOnCheckedChangeListener { _, instrumentAnswer ->


            selectedinstrumentAnswer = when (instrumentAnswer) {
                binding.radioinstrumentYes.id -> {
                    binding.textRemarkareYouGivenSufficientInstument.visibility = View.GONE


                    "Yes"

                }

                binding.radioinstrumentNo.id -> {
                    binding.textRemarkareYouGivenSufficientInstument.visibility = View.VISIBLE

                    "No"
                }

                else -> ""

            }
        }
        binding.radioGroupmaterialsYesNo.setOnCheckedChangeListener { _, materialsAnswer ->


            selectedmaterialsAnswer = when (materialsAnswer) {

                binding.radiomaterialsYes.id -> {
                    binding.textRemarkareYouGivenEnoughMaterials.visibility = View.GONE
                    "Yes"
                }

                binding.radiomaterialsNo.id -> {
                    binding.textRemarkareYouGivenEnoughMaterials.visibility = View.VISIBLE
                    "No"
                }

                else -> ""

            }
        }

        binding.radioGroupStipinedYesNo.setOnCheckedChangeListener { _, facilitiesAnswer ->
            selectedstipinedAnswer = when (facilitiesAnswer) {

                binding.radioStipinedYes.id -> {
//                etRemarkbordingAndLoadingFacilities
                    binding.tvetRemarkbordingAndLoadingFacilities.visibility = View.GONE


                    "Yes"

                }

                binding.radioStipinedNo.id -> {
                    binding.tvetRemarkbordingAndLoadingFacilities.visibility = View.VISIBLE

                    "No"
                }

                else -> ""

            }
        }
        binding.radioGroupAreBoardingAandLoadingFacilitiesProvidedYesNo.setOnCheckedChangeListener { _, facilitiesAnswer ->
            AreBoardingAandLoadingFacilitiesProvided = when (facilitiesAnswer) {


                binding.radioAreBoardingAandLoadingFacilitiesProvidedYes.id -> {
//                etRemarkbordingAndLoadingFacilities
                    binding.textRemarkAreBoardingAandLoadingFacilitiesProvided.visibility =
                        View.GONE


                    "Yes"

                }

                binding.radioAreBoardingAandLoadingFacilitiesProvidedNo.id -> {
                    binding.textRemarkAreBoardingAandLoadingFacilitiesProvided.visibility =
                        View.VISIBLE

                    "No"
                }

                else -> ""

            }
        }


        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = sdf.format(Date())
//    bin.text = today
        binding.tvCurrentDate.text = today
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(Date())


        binding.tvTime.text = currentTime
        val monthFull = SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())
        binding.tvMonth.text = monthFull


//         27.034750
//    79.487056

//        28.629660,77.218909
//        latitude = batch.latitude
//        longitude = batch.longitude
//        latitude = 28.629660
//        longitude = 77.218909
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkAndRequestStoragePermissions()






        binding.surfaceView.holder.addCallback(this)

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ),
            101
        )



        binding.btnPause.setOnClickListener {

            if (isRecording) {

                if (!isPaused) {

                    pauseRecording()

                } else {

                    resumeRecording()
                }
            }
        }

        binding.btnRecord.setOnClickListener {
            binding.btnPlayPause.visibility = View.GONE
            binding.btnStop.visibility = View.VISIBLE
            binding.btnSwitch.visibility = View.VISIBLE
            binding.btnStop.visibility = View.VISIBLE
            binding.btnSwitch.visibility = View.GONE

//            binding.videoContainer.visibility = View.VISIBLE

            if (!isRecording) {
                resetUI()
                startRecording()
            }
        }


        binding.btnPlayPause.setOnClickListener {

            if (isPlaying) {
                playVideo()
                binding.videoView.pause()

                binding.btnPlayPause.text = getString(R.string.pause)
//                binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
                isPlaying = false

            } else {

                binding.videoView.start()
                binding.btnPlayPause.text = getString(R.string.playing)
//                binding.btnPlayPause.setImageResource(R.drawable.ic_play)
                isPlaying = true
            }
        }
        binding.btnStop.setOnClickListener {
            binding.btnPlayPause.visibility = View.VISIBLE
            binding.btnStop.visibility = View.GONE
            binding.btnSwitch.visibility = View.GONE
            if (isRecording) stopRecordingManually()
        }

        binding.btnSwitch.setOnClickListener {
            if (!isRecording) switchCamera()


            if (isSelfie) {

                binding.btnSwitch.text = getString(R.string.front)
                isSelfie = false

            } else {


                binding.btnSwitch.text = getString(R.string.back)
                isSelfie = true
            }
        }

        binding.btnDelete.setOnClickListener {
            binding.btnSwitch.visibility = View.VISIBLE
            binding.btnPlayPause.visibility = View.GONE
            deleteVideo()
        }
    }

    // ---------------- Camera ----------------

    private fun openCamera() {

        camera = Camera.open(currentCameraId)

        camera?.setDisplayOrientation(90)

        camera?.setPreviewDisplay(binding.surfaceView.holder)

        camera?.startPreview()
    }

    private fun switchCamera() {

        camera?.release()

        currentCameraId =
            if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
                Camera.CameraInfo.CAMERA_FACING_FRONT
            else
                Camera.CameraInfo.CAMERA_FACING_BACK

        openCamera()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        openCamera()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera?.release()
    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {}
    private fun pauseRecording() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            mediaRecorder?.pause()

            countDownTimer?.cancel()

            isPaused = true

            binding.btnPause.text = "Resume"

            Toast.makeText(
                requireContext(),
                "Recording Paused",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun resumeRecording() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            mediaRecorder?.resume()

            isPaused = false

            binding.btnPause.text = "Pause"

            startTimer()

            Toast.makeText(
                requireContext(),
                "Recording Resumed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    // ---------------- Recording ----------------
    private fun startRecording() {

        completed = false

        remainingRecordingTime = RECORD_TIME

        binding.btnPause.visibility = View.VISIBLE

        outputFile = File(
            requireContext().getExternalFilesDir(null),
            "VIDEO_${System.currentTimeMillis()}.mp4"
        )

        camera?.stopPreview()

        camera?.unlock()

        mediaRecorder = MediaRecorder()

        mediaRecorder?.apply {

            setCamera(camera)

            setAudioSource(MediaRecorder.AudioSource.CAMCORDER)

            setVideoSource(MediaRecorder.VideoSource.CAMERA)

            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)

            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            setVideoEncoder(MediaRecorder.VideoEncoder.H264)

            setVideoSize(640, 480)

            setVideoFrameRate(20)

            setVideoEncodingBitRate(300 * 1000)

            setAudioEncodingBitRate(64 * 1000)

            setOrientationHint(
                if (
                    currentCameraId ==
                    Camera.CameraInfo.CAMERA_FACING_FRONT
                ) 270 else 90
            )

            setOutputFile(outputFile!!.absolutePath)

            setPreviewDisplay(binding.surfaceView.holder.surface)

            prepare()

            start()
        }

        isRecording = true

        isPaused = false

        binding.btnPause.text = "Pause"

        startTimer()
    }
//    private fun startRecording() {
//
//        completed = false
//
//        outputFile = File(
//            requireContext().getExternalFilesDir(null),
//            "VIDEO_${System.currentTimeMillis()}.mp4"
//        )
//
//        camera?.stopPreview()
//        camera?.unlock()
//
//        mediaRecorder = MediaRecorder()
//
//        mediaRecorder?.apply {
//            setCamera(camera)
//
//            setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
//            setVideoSource(MediaRecorder.VideoSource.CAMERA)
//
//            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//
//            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
//            setVideoSize(640, 480)
//            setVideoFrameRate(20)
//
//            setVideoEncodingBitRate(300 * 1000)
//            setAudioEncodingBitRate(64 * 1000)
//
//            setOrientationHint(
//                if (currentCameraId ==
//                    Camera.CameraInfo.CAMERA_FACING_FRONT
//                ) 270 else 90
//            )
//
//            setOutputFile(outputFile!!.absolutePath)
//
//            setPreviewDisplay(binding.surfaceView.holder.surface)
//
//            prepare()
//            start()
//
//        }
//
//        isRecording = true
//        binding.btnSwitch.visibility = View.GONE
//        startTimer()
//    }

    // ---------------- Recording Timer ----------------
    private fun startTimer() {

        countDownTimer = object : CountDownTimer(
            remainingRecordingTime,
            1000
        ) {

            override fun onTick(
                millisUntilFinished: Long
            ) {

                remainingRecordingTime =
                    millisUntilFinished

                val sec =
                    millisUntilFinished / 1000

                binding.tvTimer.text =
                    String.format(
                        "%02d:%02d",
                        sec / 60,
                        sec % 60
                    )
            }

            override fun onFinish() {

                completed = true

                stopAndSave()
            }

        }.start()
    }
//    private fun startTimer() {
//
//        countDownTimer = object : CountDownTimer(RECORD_TIME, 1000) {
//
//            override fun onTick(millisUntilFinished: Long) {
//
//                val sec = millisUntilFinished / 1000
//
//                binding.tvTimer.text =
//                    String.format("%02d:%02d", sec / 60, sec % 60)
//            }
//
//            override fun onFinish() {
//
//                completed = true
//
//                stopAndSave()
//            }
//
//        }.start()
//    }

    // ---------------- Stop Recording ----------------

    private fun stopRecordingManually() {

        countDownTimer?.cancel()

        try {

            mediaRecorder?.stop()

        } catch (e: Exception) {

            e.printStackTrace()
        }

        mediaRecorder?.release()

        mediaRecorder = null

        camera?.lock()

        isRecording = false

        isPaused = false

        binding.btnPause.visibility = View.GONE

        binding.btnStop.visibility = View.GONE

        binding.surfaceView.visibility = View.GONE

        binding.videoView.visibility = View.VISIBLE

        binding.btnDelete.visibility = View.VISIBLE

        binding.btnPlayPause.visibility = View.VISIBLE
    }

    private fun stopAndSave() {

        try {
            mediaRecorder?.stop()
        } catch (e: Exception) {}

        mediaRecorder?.release()
        mediaRecorder = null

        camera?.lock()

        isRecording = false

        binding.btnStop.visibility = View.GONE
        binding.surfaceView.visibility = View.GONE
        binding.videoView.visibility = View.VISIBLE
        binding.btnDelete.visibility = View.VISIBLE
        binding.btnSwitch.visibility = View.GONE
        binding.btnPlayPause.visibility = View.VISIBLE


        outputFile?.let { file ->

            val path = file.absolutePath
            finalVideoPath=path


        }











//        playVideo()
    }

    // ---------------- Play Video ----------------

    private fun playVideo() {

        outputFile?.let { file ->

            val path = file.absolutePath

            binding.videoView.setVideoPath(path)

            binding.videoView.setOnPreparedListener {

                binding.videoView.start()

                startPlaybackTimer()

                val sizeMB =
                    file.length().toDouble() / (1024 * 1024)

//                Toast.makeText(
//                    requireContext(),
//                    "Saved Path:\n$path",
//                    Toast.LENGTH_LONG
//                ).show()

                binding.btnStop.visibility = View.GONE

                finalVideoPath=path

//                android.app.AlertDialog.Builder(requireContext())
//                    .setTitle("Recording Completed")
//                    .setMessage(
//                        "Size: %.2f MB\n\nPath:\n$path".format(sizeMB)
//                    )
//                    .setPositiveButton("OK", null)
//                    .show()
            }
        }
    }

    // ---------------- Playback Timer ----------------

    private fun startPlaybackTimer() {

        playbackRunnable = object : Runnable {

            override fun run() {

                if (binding.videoView.isPlaying) {

                    val current =
                        binding.videoView.currentPosition

                    val duration =
                        binding.videoView.duration

                    val currentSec = current / 1000
                    val totalSec = duration / 1000

                    val currentMin = currentSec / 60
                    val currentSecond = currentSec % 60

                    val totalMin = totalSec / 60
                    val totalSecond = totalSec % 60

                    binding.tvTimer.text =
                        String.format(
                            "%02d:%02d / %02d:%02d",
                            currentMin,
                            currentSecond,
                            totalMin,
                            totalSecond
                        )

                    handler.postDelayed(this, 500)
                }
            }
        }

        handler.post(playbackRunnable!!)
    }

    // ---------------- Delete Video ----------------

    private fun deleteVideo() {

        outputFile?.let {

            if (it.exists()) it.delete()
        }

        binding.videoView.stopPlayback()

        binding.videoView.visibility = View.GONE

        binding.surfaceView.visibility = View.VISIBLE

        binding.btnDelete.visibility = View.GONE

        binding.tvTimer.text = "03:00"

        openCamera()

        Toast.makeText(
            requireContext(),
            "Video Deleted",
            Toast.LENGTH_SHORT
        ).show()
    }

    // ---------------- Reset UI ----------------

    private fun resetUI() {

        binding.surfaceView.visibility = View.VISIBLE

        binding.videoView.visibility = View.GONE

        binding.btnDelete.visibility = View.GONE

        binding.tvTimer.text = "03:00"
    }



    // ===============================
    // STEP 1: CHECK PERMISSION
    // ===============================



    private fun dismissKeyboard(view: View? = null) {
        val imm = ContextCompat.getSystemService(
            requireContext(),
            InputMethodManager::class.java
        ) as? InputMethodManager ?: return
        val windowToken = view?.windowToken ?: requireActivity().currentFocus?.windowToken ?: return
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openCalendar() {

        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->

                val selectedDate = LocalDate.of(
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                if (isDateAllowed(selectedDate)) {

                    HowMuchSelectedDate = selectedDate.format(formatter)
                    binding.tvHowMuchSelectedDate.setText(selectedDate.format(formatter))
                } else {
                    binding.tvHowMuchSelectedDate.setText("")
                    binding.tvHowMuchSelectedDate.error = "Select last 6 working days only"
                }

            },
            year,
            month,
            day
        )

        // 🔹 Set Max Date (Today)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        // 🔹 Set Min Date (Last 6 Working Days)
        val minDate = getMinAllowedDate()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        datePickerDialog.datePicker.minDate = minDate

        datePickerDialog.show()
    }

    //     🔹 Function: Last 6 Working Days (Sunday Skip)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMinAllowedDate(): LocalDate {

        var date = LocalDate.now()
        var workingDays = 0

        while (workingDays < 6) {
            date = date.minusDays(1)

            if (date.dayOfWeek != DayOfWeek.SUNDAY) {
                workingDays++
            }
        }

        return date
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isDateAllowed(date: LocalDate): Boolean {

        val today = LocalDate.now()
        val minDate = getMinAllowedDate()

        return !date.isBefore(minDate) && !date.isAfter(today)
    }


    // ============================
    // Permission Launcher
    // ============================
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED

    // ===============================
    // Start Camera Preview
    // ===============================

    private fun getFileName(uri: Uri): String {

        var name = "Candidate_Registration_${System.currentTimeMillis()}.mp4"

        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst() && index != -1) {
                name = it.getString(index)
            }
        }
        return name
    }
    private fun scrollToView(targetView: View) {
        binding.scrollView.post {
            binding.scrollView.smoothScrollTo(0, targetView.top)
        }
    }

    private fun validateRequiredField(
        view: View,
        value: String,
        message: String
    ): Boolean {

        if (value.trim().isEmpty()) {
            scrollToView(view)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            if (view is EditText) {
                view.error = message
            }
            return false
        }
        return true
    }



    private fun validateRequiredDoingTodayField(
        view: View,
        value: String,
        message: String
    ): Boolean {

        if (value.trim().isEmpty()) {
            scrollToView(view)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            if (view is EditText) {
                view.error = message
            }
            return false
        }
        return true
    }

    private fun validateRequiredDidYouDoField(
        view: View,
        value: String,
        message: String
    ): Boolean {

        if (value.trim().isEmpty()) {
            scrollToView(view)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            if (view is EditText) {
                view.error = message
            }
            return false
        }
        return true
    }



    private fun validateRequiredhowManyField(
        view: View,
        value: String,
        message: String
    ): Boolean {

        if (value.trim().isEmpty()) {
            scrollToView(view)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            if (view is EditText) {
                view.error = message
            }
            return false
        }
        return true
    }

    private fun validateYesNoWithRemark(
        radioGroup: RadioGroup,
        radioNoId: Int,
        remarkEt: EditText,
        emptyMsg: String
    ): Boolean {

        // 1️⃣ Radio selection mandatory
        if (radioGroup.checkedRadioButtonId == -1) {
            scrollToView(radioGroup)
            Toast.makeText(
                requireContext(),
                "Please select Yes or No",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        // 2️⃣ Agar NO select hai → EditText mandatory
        if (radioGroup.checkedRadioButtonId == radioNoId) {
            if (remarkEt.text.toString().trim().isEmpty()) {
                remarkEt.error = emptyMsg
                scrollToView(remarkEt)
                Toast.makeText(
                    requireContext(),
                    emptyMsg,
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }

        // 3️⃣ Agar YES select hai → koi EditText validation nahi
        return true
    }


    private fun validateRequiredhowMuchstipnedeligibleField(
        view: View,
        value: String,
        message: String
    ): Boolean {

        if (value.trim().isEmpty()) {
            scrollToView(view)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            if (view is EditText) {
                view.error = message
            }
            return false
        }
        return true
    }


    private fun validateRequiredhowMuchstipnedegettingField(
        view: View,
        value: String,
        message: String
    ): Boolean {

        if (value.trim().isEmpty()) {
            scrollToView(view)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            if (view is EditText) {
                view.error = message
            }
            return false
        }
        return true
    }
    private fun setupApiObserver() {

        viewModel.SaveCandidateOjtVerification.observe(this) { result ->

            // Always dismiss any progress
            ProgressDialogUtil.dismissProgressDialog()

            result.onSuccess { response ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Success")
                    .setMessage(response.responseDesc)
                    .setPositiveButton("Yes") { dialog, _ ->
                        dialog.dismiss()

                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()

                    }
                    .show()

                dismiss() // close dialog if desired

            }.onFailure { throwable ->

                Toast.makeText(
                    requireContext(),
                    "Submission Failed: ${throwable.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupDatePicker(baseDateStr: String) {
        binding.ivCalendar.setOnClickListener {

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            // parse base date string
            val baseDate = sdf.parse(baseDateStr)

            if (baseDate != null) {

                val calMin = Calendar.getInstance()
                calMin.time = baseDate

                val calMax = Calendar.getInstance()
                calMax.time = baseDate
                calMax.add(Calendar.DAY_OF_MONTH, 365)   // max = base date + 365 days

                // 🔹 Calendar constraints
                val constraints = CalendarConstraints.Builder()
                    .setStart(calMin.timeInMillis)
                    .setEnd(calMax.timeInMillis)
                    .setValidator(
                        CompositeDateValidator.allOf(
                            listOf(
                                DateValidatorPointForward.from(calMin.timeInMillis),
                                DateValidatorPointBackward.before(calMax.timeInMillis + 1)
                            )
                        )
                    )
                    .build()

                // 🔹 Material Date Picker
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date")
                    .setSelection(calMin.timeInMillis) // default selected date
                    .setCalendarConstraints(constraints)
                    .build()

                datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")

                datePicker.addOnPositiveButtonClickListener { selection ->

                    val selectedDate = sdf.format(Date(selection))
                    selectedRandomDateStr = selectedDate
                    binding.tvSelectedDate.text = selectedDate
                }

            } else {
                Toast.makeText(requireContext(), "Invalid base date", Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun showRandomDatePicker(minDate: Date, maxDate: Date) {

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val constraints = CalendarConstraints.Builder()
            .setStart(minDate.time)
            .setEnd(maxDate.time)
            .setValidator(
                CompositeDateValidator.allOf(
                    listOf(
                        DateValidatorPointForward.from(minDate.time),
                        DateValidatorPointBackward.before(maxDate.time + 1)
                    )
                )
            )
            .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(minDate.time) // default selected date
            .setCalendarConstraints(constraints)
            .build()

        datePicker.show(parentFragmentManager, "RANDOM_DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { selection ->

            val selectedDate = sdf.format(Date(selection))
//            binding.tvSelectedRandomDate.text = selectedDate
        }
    }

    private fun initTextToSpeech() {

        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {

                textToSpeech.language = Locale.ENGLISH
                textToSpeech.setSpeechRate(0.75f)

                textToSpeech.setOnUtteranceProgressListener(object :
                    UtteranceProgressListener() {

                    override fun onStart(utteranceId: String?) {
                        requireActivity().runOnUiThread {
                            isSpeaking = true
                            currentSpeakingView?.setImageResource(R.drawable.ic_pause)
                        }
                    }

                    override fun onDone(utteranceId: String?) {
                        requireActivity().runOnUiThread {
                            stopAndReset()
                        }
                    }

                    override fun onError(utteranceId: String?) {
                        requireActivity().runOnUiThread {
                            stopAndReset()
                        }
                    }
                })
            }
        }
    }


    // ===============================
    // STEP 2: PERMISSION RESULT
    // ===============================
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

            val granted = permissions.entries.all { it.value }

            if (granted) {
                openVideoCamera()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    // ===============================
    // STEP 3: OPEN CAMERA
    // ===============================
    private fun openVideoCamera() {

        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 180) // 1 minute
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)

        videoLauncher.launch(intent)
    }

    private val videoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {


                val videoUri = result.data?.data
                videoUri?.let { uri ->

                    videoPath = videoUri.toString()
                    // Immediately show video preview
                    binding.videoView.apply {
                        visibility = View.VISIBLE
                        setVideoURI(uri)
                        requestFocus()
                        start()
                    }

                    // Compress video async


                    recordedVideoUri = result.data?.data


                    if (recordedVideoUri != null) {
                        finalVideoPath = copyVideoToAppStorage(videoUri)

                        playVideo(finalVideoPath!!)
                    }
//
                }
            }
        }

    private fun playVideo(path: String) {

        val controller = MediaController(requireContext())
        controller.setAnchorView(binding.videoView)

        // Forward (Next) 10 seconds
        controller.setPrevNextListeners(
            {
                val pos = binding.videoView.currentPosition
                binding.videoView.seekTo(pos + 10000) // 10 sec forward
            },
            {
                val pos = binding.videoView.currentPosition
                binding.videoView.seekTo(pos - 10000) // 10 sec backward
            }
        )

        binding.videoView.setMediaController(controller)
        binding.videoView.setVideoPath(path)

        binding.videoView.setOnPreparedListener {
            binding.videoView.start()
            controller.show(0) // controller always visible
        }
    }
    private fun copyVideoToAppStorage(uri: Uri): String {

        val fileName = getFileName(uri)

        val file = File(
            requireContext().getExternalFilesDir(null),
            fileName
        )

        requireContext().contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
    }
    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (permissions.any {
                ContextCompat.checkSelfPermission(
                    requireContext(), it
                ) != PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(requireActivity(), permissions, 100)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(requireContext(), "Permissions granted!", Toast.LENGTH_SHORT)
                    .show()
            } else {
//                Toast.makeText(requireContext(), "Permissions denied!", Toast.LENGTH_SHORT)
//                    .show()
            }
        }


    }


    private fun openCamera(imageView: ImageView) {
        checkAndRequestPermissions()

        currentImageView = imageView

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), "Camera permission required!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)  // No extra output, no file
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as? Bitmap
                if (bitmap == null || currentImageView == null) {
                    Toast.makeText(requireContext(), "Image capture failed!", Toast.LENGTH_SHORT)
                        .show()
                    return@registerForActivityResult
                }

                val compressedBitmap = compressBitmap(bitmap)
                currentImageView?.setImageBitmap(compressedBitmap)

                val base64Image = bitmapToBase64(compressedBitmap)

                when (currentImageView?.id) {
                    R.id.image1 -> {
                        image1Base64 = base64Image
                    }
                }
            }
        }

    private fun compressBitmap(bitmap: Bitmap): Bitmap {
        return try {
            val maxSize = 1024 // Resize to max 1024px width/height
            val width = bitmap.width
            val height = bitmap.height
            val scale =
                if (width > height) maxSize.toFloat() / width else maxSize.toFloat() / height

            val newWidth = (width * scale).toInt()
            val newHeight = (height * scale).toInt()

            Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        } catch (e: Exception) {
            e.printStackTrace()
            bitmap // Return the original bitmap if compression fails
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        return try {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                90,
                outputStream
            ) // Increase quality to 90
            val byteArray = outputStream.toByteArray()
            outputStream.close()
            Base64.encodeToString(byteArray, Base64.NO_WRAP) // Use NO_WRAP to avoid line breaks
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }


    private fun showAlertGeoFancingDialog(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
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

    @SuppressLint("MissingPermission")
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
    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        try {
            val geocoder = Geocoder(requireContext(), Locale("en", "IN"))
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val fullAddress = address.getAddressLine(0) ?: "Address not available"
                val city = address.locality ?: "Unknown City"
                val state = address.adminArea ?: "Unknown State"
                val pincode = address.postalCode ?: "No Pincode"
                val country = address.countryName ?: "Unknown Country"

                locationAddress = "$fullAddress, $city, $state, $pincode, $country"
                binding.address.text = locationAddress
            } else {
                binding.address
                    .text = getString(R.string.address_not_found)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            binding.address.text = getString(R.string.error_address)
        }
    }

    private fun startPulseAnimation() {

        val scaleX = ObjectAnimator.ofFloat(binding.rippleContainer, "scaleX", 1f, 1.1f)
        val scaleY = ObjectAnimator.ofFloat(binding.rippleContainer, "scaleY", 1f, 1.1f)

        scaleX.duration = 1200
        scaleY.duration = 1200

        scaleX.repeatCount = ObjectAnimator.INFINITE
        scaleY.repeatCount = ObjectAnimator.INFINITE

        scaleX.repeatMode = ObjectAnimator.REVERSE
        scaleY.repeatMode = ObjectAnimator.REVERSE

        scaleX.interpolator = AccelerateDecelerateInterpolator()
        scaleY.interpolator = AccelerateDecelerateInterpolator()

        scaleX.start()
        scaleY.start()
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

    private fun checkAndRequestStoragePermissions() {
        if (!hasStoragePermission(requireContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                storagePermissionLauncher.launch(AppUtil.storagePermissions)
            } else {
                storagePermissionLauncher.launch(arrayOf(AppUtil.legacyStoragePermission))
            }
        } else {
            // Permissions already granted, continue your logic
        }
    }

    private val storagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
//            Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            // proceed with file/media access
        } else {
//            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    // ================= CLICK HANDLER =================
    private fun handleSpeechClick(view: ImageView, text: String) {

        // Same icon clicked again → STOP
        if (isSpeaking && currentSpeakingView == view) {
            stopAndReset()
            return
        }

        // Another speech already running → stop it
        if (isSpeaking) {
            stopAndReset()
        }

        // Start new speech
        currentSpeakingView = view
        speakText(text)
    }

    // ================= HELPERS =================
    private fun stopAndReset() {
        textToSpeech.stop()
        isSpeaking = false
        currentSpeakingView?.setImageResource(R.drawable.ic_play)
        currentSpeakingView = null
    }

    private fun speakText(text: String) {
        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UTTERANCE_ID)

        textToSpeech.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            params,
            UTTERANCE_ID
        )
    }

    // ================= ALL CLICK LISTENERS =================
    private fun setupClickListeners() {

        binding.imageViewIsCandidate.setOnClickListener {
            handleSpeechClick(
                binding.imageViewIsCandidate,
                getString(R.string.is_the_candidatete_available_on_the_day_of_visit_to_the_ojt_location)
            )
        }

        binding.tvICandidateAvailable.setOnClickListener {
            handleSpeechClick(
                binding.tvICandidateAvailable,
                getString(R.string.when_did_you_start_your_ojt)
            )
        }

        binding.tvwhatActivityDoingare.setOnClickListener {
            handleSpeechClick(
                binding.tvwhatActivityDoingare,
                getString(R.string.what_activity_are_you_doing_today)
            )
        }

        binding.tvwhatActivity.setOnClickListener {
            handleSpeechClick(
                binding.tvwhatActivity,
                getString(R.string.what_activity_did_you_do_a_random_day_to_be_chosen_by_the_ytem_from_last_6_days)
            )
        }

        binding.tvWhatWhetherthe.setOnClickListener {
            handleSpeechClick(
                binding.tvWhatWhetherthe,
                getString(R.string.whether_the_field_level_upervisor_isnominated_for_the_ojt)
            )
        }

        binding.tvHowmanyTimes.setOnClickListener {
            handleSpeechClick(
                binding.tvHowmanyTimes,
                getString(R.string.how_many_times_during_the_day_did_the_supervior_interact_with_you)
            )
        }

        binding.tvAreYouSufficient.setOnClickListener {
            handleSpeechClick(
                binding.tvAreYouSufficient,
                getString(R.string.are_you_given_sufficient_instruments_to_work_during_ojt_tools_machinery_computers_etc)
            )
        }

        binding.tvAreYouGivenEnough.setOnClickListener {
            handleSpeechClick(
                binding.tvAreYouGivenEnough,
                getString(R.string.are_you_given_enough_materials_to_work_upon)
            )
        }

        binding.tvHowMuchtipned.setOnClickListener {
            handleSpeechClick(
                binding.tvHowMuchtipned,
                getString(R.string.how_much_stipend_are_you_eligible_for)
            )
        }

        binding.tvHowMuchtipnedGetting.setOnClickListener {
            handleSpeechClick(
                binding.tvHowMuchtipnedGetting,
                getString(R.string.how_much_stipend_are_you_getting)
            )
        }

        binding.tvAreBoardingandLanding.setOnClickListener {
            handleSpeechClick(
                binding.tvAreBoardingandLanding,
                getString(R.string.are_boarding_and_loading_facilities_provided)
            )
        }
    }


    private fun safe(value: String?): String {
        return value?.trim() ?: ""
    }






    private fun uploadCandidateOjtVerification(
        token: String,
        finalVideoPath: String,
        image1Base64: String
    ) {

        if (!isAdded) return

        val progressDialog = ProgressDialog(requireContext()).apply {
            setMessage("Uploading please wait...")
            setCancelable(false)
            show()
        }

        //------------------------------------
        // ALL TEXT VALUES
        //------------------------------------

        val SelectedDate = safe(binding.tvSelectedDate.text.toString())
        val HowMuchSelectedDate = safe(binding.tvHowMuchSelectedDate.text.toString())
        val EnterYourGettingFor = safe(binding.etOjtEnterYourGettingFor.text.toString())
        val EnterDuringTimes = safe(binding.etOjtEnterDuringTimes.text.toString())
        val PrevousDoingToday = safe(binding.etSelectedRandomDate.text.toString())
        val DoingToday = safe(binding.etOjtTrainingCenter.text.toString())
        val NoReason = safe(binding.etNoReason.text.toString())

        val RemarkbordingAndLoadingFacilities =
            safe(binding.etRemarkbordingAndLoadingFacilities.text.toString())

        val RemarkFieldLevelSupervisorNominated =
            safe(binding.etRemarkFieldLevelSupervisorNominated.text.toString())

        val RemarkareYouGivenEnoughMaterials =
            safe(binding.etRemarkareYouGivenEnoughMaterials.text.toString())

        val RemarkareYouGivenSufficientInstument =
            safe(binding.etRemarkareYouGivenSufficientInstument.text.toString())

        val RemarkAreBoardingAandLoadingFacilitiesProvided =
            safe(binding.etRemarkAreBoardingAandLoadingFacilitiesProvided.text.toString())

        //------------------------------------
        // VIDEO CHECK
        //------------------------------------

        val videoFile = File(finalVideoPath)

        if (!videoFile.exists()) {

            progressDialog.dismiss()

            Toast.makeText(
                requireContext(),
                "Video file not found",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        //------------------------------------
        // OKHTTP CLIENT
        //------------------------------------

        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .build()

        //------------------------------------
        // VIDEO REQUEST BODY
        //------------------------------------

        val videoRequestBody =
            videoFile.asRequestBody("video/mp4".toMediaTypeOrNull())

        //------------------------------------
        // MULTIPART BODY
        //------------------------------------

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("appVersion", safe(BuildConfig.VERSION_NAME))
            .addFormDataPart("ojtPlanId", safe(batch[0].ojtPlanId.toString()))
            .addFormDataPart("ojtPlanVerificationId", safe(batch[0].ojtPlanVerificationId.toString()))
            .addFormDataPart("sanctionOrder", safe(batch[0].sanctionOrder))
            .addFormDataPart("trainingCenterId", safe(batch[0].trainingCenterId.toString()))
            .addFormDataPart("batchId", safe(AppUtil.getSavedOJTBatchIDPreference(requireContext())))
            .addFormDataPart("candidateId", safe(batch[0].candidateId))
            .addFormDataPart("piaCode", safe(batch[0].piaCode))
            .addFormDataPart("employeersId", safe(batch[0].employeersId.toString()))
            .addFormDataPart("month", safe(binding.tvMonth.text.toString()))
            .addFormDataPart("fatherName", safe(batch[0].fatherName))
            .addFormDataPart("districtCode", safe(batch[0].districtCode))
            .addFormDataPart("trainingStartDate", safe(batch[0].batchStartDate))
            .addFormDataPart("trainingEndDate", safe(batch[0].batchEndDate))
            .addFormDataPart("ojtStartDate", safe(selectedRandomDateStr.toString()))
            .addFormDataPart("ojtEndDate", safe(batch[0].ojtEndDate))
            .addFormDataPart("verificationDate", safe(binding.tvCurrentDate.text.toString()))
            .addFormDataPart("verificationTime", safe(binding.tvTime.text.toString()))
            .addFormDataPart("candidateAvailable", safe(selectedAnswer))
            .addFormDataPart("workPlaceId", safe(batch[0].workplaceId))
            .addFormDataPart("reason", NoReason)
            .addFormDataPart("ojtStartByCandidate", SelectedDate)
            .addFormDataPart("todayActivity", DoingToday)
            .addFormDataPart(
                "previousActivity",
                safe(HowMuchSelectedDate + " " + PrevousDoingToday)
            )
            .addFormDataPart(
                "isFieldLevelSupervisorNominated",
                safe(selectednominatedAnswer)
            )
            .addFormDataPart(
                "supervisorInteractionTimeCount",
                EnterDuringTimes
            )
            .addFormDataPart(
                "areYouGivenSufficientInstument",
                safe(selectedinstrumentAnswer)
            )
            .addFormDataPart(
                "areYouGivenEnoughMaterials",
                safe(selectedmaterialsAnswer)
            )
            .addFormDataPart("eligibleStipend", safe(selectedstipinedAnswer))
            .addFormDataPart("stipendGetting", EnterYourGettingFor)
            .addFormDataPart(
                "bordingAndLoadingFacilities",
                safe(AreBoardingAandLoadingFacilitiesProvided)
            )
            .addFormDataPart(
                "candidateRollNo",
                safe(batch[0].rollNo.toString())
            )
            .addFormDataPart("latitude", safe(latitude.toString()))
            .addFormDataPart("longitude", safe(longitude.toString()))

            // IMAGE BASE64
            .addFormDataPart(
                "verificationImage",
                safe(image1Base64)
            )

            // VIDEO
            .addFormDataPart(
                "verificationVideo",
                videoFile.name,
                videoRequestBody
            )

            .addFormDataPart(
                "fieldLevelSupervisorNominatedRemark",
                RemarkFieldLevelSupervisorNominated
            )

            .addFormDataPart(
                "youGivenSufficientInstumentRemark",
                RemarkareYouGivenSufficientInstument
            )

            .addFormDataPart(
                "youGivenEnoughMaterialsRemark",
                RemarkareYouGivenEnoughMaterials
            )

            .addFormDataPart(
                "bordingAndLoadingFacilitiesRemark",
                RemarkAreBoardingAandLoadingFacilitiesProvided
            )

            .addFormDataPart(
                "isStipendSame",
                safe(selectedfacilitiesAnswer)
            )

            .addFormDataPart(
                "stipendRemark",
                RemarkbordingAndLoadingFacilities
            )

            .build()

        //------------------------------------
        // REQUEST
        //------------------------------------

        val request = Request.Builder()
            .url(BuildConfig.BASE_URL + "saveSrlmCandidateOjtVerification")
            .post(body)
            .addHeader("ddugkyappauth", "Bearer $token")
            .build()

        //------------------------------------
        // API CALL
        //------------------------------------

        lifecycleScope.launch(Dispatchers.IO) {

            try {

                Log.d("UPLOAD_API", request.url.toString())
                Log.d("UPLOAD_TOKEN", token)
                Log.d("VIDEO_PATH", videoFile.absolutePath)
                Log.d("VIDEO_SIZE_MB", "${videoFile.length() / (1024 * 1024)} MB")

                val response = client.newCall(request).execute()

                val responseBody = response.body?.string() ?: ""

                Log.d("UPLOAD_RESPONSE_CODE", response.code.toString())
                Log.d("UPLOAD_RESPONSE", responseBody)

                withContext(Dispatchers.Main) {

                    if (!isAdded) return@withContext

                    progressDialog.dismiss()

                    if (response.isSuccessful) {

                        try {

                            val jsonObject = JSONObject(responseBody)

                            val message =
                                jsonObject.optString(
                                    "responseDesc",
                                    "Upload Successful"
                                )

                            AlertDialog.Builder(requireContext())
                                .setTitle("Success")
                                .setMessage(message)
                                .setCancelable(false)
                                .setPositiveButton("OK") { dialog, _ ->

                                    dialog.dismiss()

                                    parentFragmentManager.setFragmentResult(
                                        "REFRESH_DATA",
                                        Bundle()
                                    )
                                    this@SRLMBottomDialog.dismissAllowingStateLoss()
                                    dialog.dismiss()

//                                    dismissAllowingStateLoss()
                                }
                                .show()

                        } catch (e: Exception) {

                            AlertDialog.Builder(requireContext())
                                .setTitle("Success")
                                .setMessage(responseBody)
                                .setPositiveButton("OK") { dialog, _ ->

                                    dialog.dismiss()

                                    parentFragmentManager.setFragmentResult(
                                        "REFRESH_DATA",
                                        Bundle()
                                    )
                                    this@SRLMBottomDialog.dismissAllowingStateLoss()
                                    dialog.dismiss()
                                }
                                .show()
                        }

                    } else {

                        AlertDialog.Builder(requireContext())
                            .setTitle("Server Error")
                            .setMessage(
                                "HTTP ${response.code}\n\n$responseBody"
                            )
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }

            } catch (e: Exception) {

                withContext(Dispatchers.Main) {

                    if (!isAdded) return@withContext

                    progressDialog.dismiss()

                    AlertDialog.Builder(requireContext())
                        .setTitle("Exception")
                        .setMessage(e.localizedMessage ?: "Unknown Error")
                        .setPositiveButton("OK", null)
                        .show()
                }

                Log.e("UPLOAD_EXCEPTION", e.toString())
            }
        }
    }
//    private fun uploadCandidateOjtVerification(image1Base64: String) {
//
//        val token = "Bearer ${getToken(requireContext())}"
//
//        //------------------------------------
//        // VIDEO VALIDATION
//        //------------------------------------
//        if (finalVideoPath.isNullOrEmpty()) {
//            Toast.makeText(requireContext(), "Video path missing", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val videoFile = File(finalVideoPath!!)
//
//        if (!videoFile.exists()) {
//            Toast.makeText(requireContext(), "Video file not found", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        //------------------------------------
//        // VIDEO PART
//        //------------------------------------
//        val videoRequestBody =
//            videoFile.asRequestBody("video/mp4".toMediaTypeOrNull())
//
//        val videoPart =
//            MultipartBody.Part.createFormData(
//                "verificationVideo",
//                videoFile.name,
//                videoRequestBody
//            )
//
//        //------------------------------------
//        // IMAGE PART (🔥 FIXED - NO BASE64)
//        //------------------------------------
//        val imageFile = File(image1Base64) // <-- ensure you have real path
//
//        val imagePart = if (imageFile.exists()) {
//            val imageRequestBody =
//                imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
//
//            MultipartBody.Part.createFormData(
//                "verificationImage",
//                imageFile.name,
//                imageRequestBody
//            )
//        } else null
//
//        //------------------------------------
//        // SAFE REQUEST BODY FUNCTION
//        //------------------------------------
//        fun String?.toBody(): RequestBody =
//            (this ?: "").trim().toRequestBody("text/plain".toMediaTypeOrNull())
//
//        fun Int?.toBody(): RequestBody =
//            (this ?: 0).toString().toBody()
//
//        //------------------------------------
//        // MAP
//        //------------------------------------
//        val map = HashMap<String, RequestBody>()
//
//        map["appVersion"] = BuildConfig.VERSION_NAME.toBody()
//        map["ojtPlanId"] = batch[0].ojtPlanId.toBody()
//        map["sanctionOrder"] = batch[0].sanctionOrder.toBody()
//        map["trainingCenterId"] = batch[0].trainingCenterId.toBody()
//
//        map["batchId"] =
//            AppUtil.getSavedOJTBatchIDPreference(requireContext()).toBody()
//
//        map["candidateId"] = batch[0].candidateId.toBody()
//        map["piaCode"] = batch[0].piaCode.toBody()
//        map["employeersId"] = batch[0].employeersId.toBody()
//
//        map["month"] = binding.tvMonth.text.toString().toBody()
//        map["fatherName"] = batch[0].fatherName.toBody()
//        map["districtCode"] = batch[0].districtCode.toBody()
//
//        map["trainingStartDate"] = batch[0].batchStartDate.toBody()
//        map["trainingEndDate"] = batch[0].batchEndDate.toBody()
//
//        map["ojtStartDate"] = selectedRandomDateStr.toBody()
//        map["ojtEndDate"] = batch[0].ojtEndDate.toBody()
//
//        map["verificationDate"] = binding.tvCurrentDate.text.toString().toBody()
//        map["verificationTime"] = binding.tvTime.text.toString().toBody()
//
//        map["candidateAvailable"] = selectedAnswer.toBody()
//
//        map["reason"] = binding.etNoReason.text.toString().toBody()
//
//        map["latitude"] = latitude.toString().toBody()
//        map["longitude"] = longitude.toString().toBody()
//
//        //------------------------------------
//        // IMPORTANT FIX: DO NOT SEND BASE64
//        //------------------------------------
//        // map["verificationImage"] = ❌ REMOVE THIS
//
//        //------------------------------------
//        // API CALL
//        //------------------------------------
//        showProgressDialog(requireContext(), "Uploading...")
//
//        viewModel.uploadCandidateOjtSRLMVerification(
//            token,
//            map,
//            videoPart,
//            imagePart // 👈 ADD THIS in API
//        )
//    }



    fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // 👉 Orientation unlock when dialog closed
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        _binding = null
        dismissKeyboard()
        isProcessingOJTFullScreenDialog = false
        textToSpeech.stop()
        textToSpeech.shutdown()


        try {

            if (isRecording) {
                if (isRecording) stopRecordingManually()
                deleteVideo()
                mediaRecorder?.stop()
            }

        } catch (e: Exception) {

            e.printStackTrace()
        }

        mediaRecorder?.release()
        mediaRecorder = null

        camera?.release()
        camera = null

    }

}

