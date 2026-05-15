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
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
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
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
//import com.android.volley.NetworkResponse
//import com.android.volley.ParseError
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.databinding.FragmentOnJobTrainingBinding
import com.deendayalproject.model.response.OJTList
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
//import com.iceteck.silicompressorr.SiliCompressor
import java.io.ByteArrayOutputStream

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
// File & Storage



// CameraX Core
import com.google.gson.Gson
//import com.iceteck.silicompressorr.SiliCompressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
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
import kotlin.plus
import kotlin.text.isEmpty
import kotlin.text.isNotEmpty
import kotlin.text.isNullOrBlank
import kotlin.text.trim
import kotlin.toString

// Android Core

import android.widget.VideoView
import android.hardware.Camera
import android.location.Geocoder
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.media.MediaMuxer
import androidx.lifecycle.lifecycleScope


// Coroutines
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Location (agar use kar rahe ho)

// Video Compression Library


import java.io.File
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.addCallback
import androidx.annotation.OptIn
import androidx.annotation.RequiresPermission
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.bumptech.glide.Glide
import com.deendayalproject.databinding.FragmentPreviewScreenBinding
import com.deendayalproject.databinding.FragmentPreviewScreenSrlmBinding
import com.deendayalproject.model.response.ChildSRLM
import com.deendayalproject.model.response.VerificationDetails
import com.deendayalproject.network.SecurePreferenceManager.getToken
import com.deendayalproject.util.AppConstant.OJT_VIDEO_URL
import okhttp3.RequestBody.Companion.toRequestBody
import java.nio.ByteBuffer
//code commit 10/03/2026 Time 16:40 PM OJT Module add in Seoerate OJT Folder
class PreViewlScreenCandidateBottomSRLMDialog(private val detail: List<ChildSRLM>, private val batch: List<ChildSRLM>) :  DialogFragment() {

    private var player: ExoPlayer? = null
//    private var _binding: FragmentPreviewScreenBinding? = null








    private var _binding: FragmentPreviewScreenSrlmBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    //    private var Bindinglatitude = 27.034750
//    private var Bindinglongitutde = 79.487056
//    var videoPath: String? = null
    private var finalVideoPath: String? = ""
    private var latitude = 0.0
    private var longitude = 0.0
    var radius: Float = 100f
    private var isProcessingOJTFullScreenDialog = false
    private var isProfileVisible = false
    private var istrainingDetailsVisible = false
    private var isquestionsDetailsVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
        // 👉 Lock screen in Portrait mode
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requireActivity().onBackPressedDispatcher.addCallback(this) {
//            if (isRecording) stopRecordingManually()
            dismiss()


        }

        }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentPreviewScreenSrlmBinding.inflate(layoutInflater)

        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(true)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())






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


//        setupRadioListeners()
        // make full screen
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.setCanceledOnTouchOutside(true)


        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude


                // Fetch and update address
            } else {
                // If last known location is null, request a fresh location update

            }
        }.addOnFailureListener {

            binding.address.text = getString(R.string.address_not_found)
            Log.e("LocationError", "Failed to get location: ${it.message}")
        }




//        binding.tvPiaName.text = batch[0].piaName
////        binding.tvActivityTitle.text =
////            getString(R.string.what_activity_did_you_do_a_random_day_to_be_chosen_by_the_ytem_from_last_6_days)
//        binding.tvCenterName.text = batch[0].trainingCenterName
////        binding.tvCandidate.text = batch[0].candidateName
//        binding.tvKpid.text = batch[0].candidateId
//        binding.tvStatus.text = batch[0].status
//
//
////        binding.tvStipned.text =
////            getString(R.string.how_much_stipend_are_you_eligible_for) + "\n" + "₹" + batch[0].stipend
//        binding.tvFatherName.text = batch[0].fatherName
//        binding.tvDistrict.text = batch[0].districtName
//        binding.tvTrainingStart.text = batch[0].batchStartDate
//        binding.tvTrainingEnd.text = batch[0].batchEndDate
//        binding.tvOjtStart.text = batch[0].ojtStartDate ?: ""
//        binding.tvOjtEnd.text = batch[0].ojtEndDate ?: ""
//        binding.tvOjtLocation.text = batch[0].workplaceName
//        binding.tvOjtIndustryName.text = batch[0].employeersName
//        binding.tvMobileNo.text = batch[0].mobileNo


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

        }
        binding.radioGroupYesNoNa.setOnCheckedChangeListener(null)

//        if (detail == null) {
//            binding.radioGroupYesNoNa.clearCheck()
//            binding.textRemarkareYouGivenSufficientInstument.visibility = View.GONE
//        } else
//        {
////            val value = "NO"
//            val value = detail[0].candidateAvailable
//
//            when (value) {
//                "Yes" -> {
//                    binding.yesLayout.visibility = View.VISIBLE
//
//                    binding.textInputReon.visibility = View.GONE
//                    binding.radioYes.isChecked = true
//                }
//                "No" -> {
//                    binding.radioNo.isChecked = true
//                    binding.textInputReon.visibility = View.VISIBLE
//
//                    binding.yesLayout.visibility = View.GONE
//                }
//                else -> {
//                    binding.radioGroupYesNoNa.clearCheck()
//                }
//            }
//
//            // ✅ Visibility handling (single source)
//            binding.textRemarkareYouGivenSufficientInstument.visibility =
//                if (value == "No") View.VISIBLE else View.GONE
//        }

//        binding.radioGroupnominatedYesNo.setOnCheckedChangeListener(null)
//
//        if (detail == null) {
//            binding.radioGroupnominatedYesNo.clearCheck()
//            binding.textRemarkareYouGivenSufficientInstument.visibility = View.GONE
//        } else {
//            val value = detail[0].isFieldLevelSupervisorNominated
//
//            when (value) {
//                "Yes" -> {
//                    binding.radionominatedYes.isChecked = true
//                }
//
//                "No" -> {
//                    binding.radionominatedNo.isChecked = true
//                }
//
//                else -> {
//                    binding.radioGroupnominatedYesNo.clearCheck()
//                }
//            }
//
//            // ✅ Visibility control
//            binding.textRemarkisFieldLevelSupervisorNominated.visibility =
//                if (value == "No") View.VISIBLE else View.GONE
//        }


//        binding.radioGroupinstrumentYesNo.setOnCheckedChangeListener(null)
//
//        if (detail == null) {
//            binding.radioGroupinstrumentYesNo.clearCheck()
//            binding.textRemarkareYouGivenSufficientInstument.visibility = View.GONE
//        } else {
//            val value = detail[0].areYouGivenSufficientInstument
//
//            when (value) {
//                "Yes" -> {
//                    binding.radioinstrumentYes.isChecked = true
//                }
//                "No" -> {
//                    binding.radioinstrumentNo.isChecked = true
//                }
//                else -> {
//                    binding.radioGroupnominatedYesNo.clearCheck()
//                }
//            }
//
//            // ✅ Visibility control
//            binding.textRemarkareYouGivenSufficientInstument.visibility =
//                if (value == "No") View.VISIBLE else View.GONE
//        }


//        binding.radioGroupmaterialsYesNo.setOnCheckedChangeListener(null)
//
//        if (detail == null) {
//            binding.radioGroupmaterialsYesNo.clearCheck()
//            binding.textRemarkareYouGivenSufficientInstument.visibility = View.GONE
//        } else {
//            val value = detail[0].areYouGivenSufficientInstument
//
//            when (value) {
//                "Yes" -> {
//                    binding.radiomaterialsYes.isChecked = true
//                }
//                "No" -> {
//                    binding.radiomaterialsNo.isChecked = true
//                }
//
//                else -> {
//                    binding.radioGroupmaterialsYesNo.clearCheck()
//                }
//            }
//            // ✅ Visibility control
//            binding.textRemarkareYouGivenEnoughMaterials.visibility =
//                if (value == "No") View.VISIBLE else View.GONE
//        }


//        binding.radioGroupfacilitiesYesNo.setOnCheckedChangeListener(null)
//
//        if (detail == null) {
//            binding.radioGroupfacilitiesYesNo.clearCheck()
//            binding.textRemarkareYouGivenSufficientInstument.visibility = View.GONE
//        } else {
//            val value = detail[0].eligibleStipend
//
//            when (value) {
//                "Yes" -> {
//                    binding.radiofacilitiesYes.isChecked = true
//                }
//                "No" -> {
//                    binding.radiofacilitiesNo.isChecked = true
//                }
//
//                else -> {
//                    binding.radioGroupfacilitiesYesNo.clearCheck()
//                }
//            }
//            // ✅ Visibility control
//            binding.tvetRemarkbordingAndLoadingFacilities.visibility =
//                if (value == "No") View.VISIBLE else View.GONE
//        }


//        binding.radioGroupAreBoardingAandLoadingFacilitiesProvidedYesNo.setOnCheckedChangeListener(null)
//
//        if (detail == null) {
//            binding.radioGroupAreBoardingAandLoadingFacilitiesProvidedYesNo.clearCheck()
//            binding.textRemarkareYouGivenSufficientInstument.visibility = View.GONE
//        } else {
//            val value = detail[0].bordingAndLoadingFacilities
//
//            when (value) {
//                "Yes" -> {
//                    binding.radioAreBoardingAandLoadingFacilitiesProvidedYes.isChecked = true
//                }
//                "No" -> {
//                    binding.radioAreBoardingAandLoadingFacilitiesProvidedNo.isChecked = true
//                }
//
//                else -> {
//                    binding.radioGroupAreBoardingAandLoadingFacilitiesProvidedYesNo.clearCheck()
//                }
//            }
//            // ✅ Visibility control
//            binding.textRemarkAreBoardingAandLoadingFacilitiesProvided.visibility =
//                if (value == "No") View.VISIBLE else View.GONE
//        }


//        binding.etRemarkAreBoardingAandLoadingFacilitiesProvided.setText(detail[0].bordingAndLoadingFacilitiesRemark)
//        binding.etRemarkAreBoardingAandLoadingFacilitiesProvided.keyListener = null
//
//        binding.etRemarkbordingAndLoadingFacilities.setText(detail[0].stipendRemark)


        binding.etRemarkbordingAndLoadingFacilities.keyListener = null


//        binding.etRemarkareYouGivenEnoughMaterials.setText(detail[0].youGivenEnoughMaterialsRemark)

        binding.etRemarkareYouGivenEnoughMaterials.keyListener = null


//        binding.etRemarkareYouGivenSufficientInstument.setText(detail[0].youGivenSufficientInstumentRemark)
//        binding.etOjtEnterYourGettingFor.setText(detail[0].stipendGetting)


        binding.etOjtEnterYourGettingFor.keyListener = null

        binding.etRemarkareYouGivenSufficientInstument.keyListener = null

//        binding.etOjtEnterDuringTimes.setText(detail[0].supervisorInteractionTimeCount)
        binding.etOjtEnterDuringTimes.keyListener = null
//        binding.etRemarkFieldLevelSupervisorNominated.setText(detail[0].fieldLevelSupervisorNominatedRemark)
        binding.etRemarkFieldLevelSupervisorNominated.keyListener = null
//        binding.tvSelectedDate.setText(detail[0].ojtStartDate)
//        binding.etOjtTrainingCenter.setText(detail[0].todayActivity)
        binding.etOjtTrainingCenter.keyListener = null


//        binding.tvHowMuchSelectedDate.setText(detail[0].previousActivity)

//        val previousActivity = detail[0].previousActivity.orEmpty()
//
//        if (previousActivity.length >= 10) {
//            val datePart = previousActivity.substring(0, 10)
//            val textPart = previousActivity.substring(10).trim()
//
//            binding.tvHowMuchSelectedDate.text = datePart
//            binding.etSelectedRandomDate.setText(textPart)
//        } else {
//            binding.tvHowMuchSelectedDate.text = ""
//            binding.etSelectedRandomDate.setText(previousActivity)
//        }

        binding.tvHowMuchSelectedDate.keyListener = null
        binding.etSelectedRandomDate.keyListener = null

//        playVideo(batch[0].candidateId)

//        finalVideoPath = detail[0].verificationImage
        val bitmap = finalVideoPath?.let { base64ToBitmap(it) }

        if (bitmap != null) {
            binding.imageView.setImageBitmap(bitmap)
        } else {
            binding.imageView.setImageResource(R.drawable.no_data) // fallback
        }


//        val reason = detail[0].reason.orEmpty()
//
//        val visibility = if (reason.isBlank()) View.GONE else View.VISIBLE

//        binding.textRemarkReaon.visibility = visibility
//        binding.etRemarkReaon.visibility = visibility

// optional: set text when available
//        if (visibility == View.VISIBLE) {
//            binding.etRemarkReaon.setText(reason)
//        }
//        binding.etRemarkReaon.setText(detail[0].reason)
//        binding.etRemarkReaon.keyListener = null
//
//    }

    }
    fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    @OptIn(UnstableApi::class) private fun
            playVideo(candidateId: String) {

        val videoUrl =OJT_VIDEO_URL+candidateId


        // ✅ Headers
        val headers = mapOf(
            "ddugkyappauth" to "Bearer "+getToken(requireContext())
        )

        // ✅ DataSource with headers
        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setDefaultRequestProperties(headers)

        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoUrl))

        // ✅ Player
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player

        player?.apply {
            setMediaSource(mediaSource)
            prepare()
            playWhenReady = true
        }
    }


//        player = ExoPlayer.Builder(requireContext()).build()
//        binding.playerView.player = player
//
//        val mediaItem = MediaItem.fromUri(url)
//        player?.setMediaItem(mediaItem)
//
//        player?.prepare()
//        player?.playWhenReady = true
//    }



    override fun onDestroyView() {
        super.onDestroyView()
        // 👉 Orientation unlock when dialog closed
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        _binding = null

        isProcessingOJTFullScreenDialog = false



    }
    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }


















}

