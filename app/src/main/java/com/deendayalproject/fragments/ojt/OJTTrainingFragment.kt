package com.deendayalproject.fragments.ojt

import android.app.DatePickerDialog
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentOnJobTrainingBinding
import com.deendayalproject.fragments.HomeFragmentDirections
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


// Ajit Ranjan create 27/Jan/2026 OJT Implimentation
 class OJTTrainingFragment : BaseFragment<FragmentOnJobTrainingBinding>(
    FragmentOnJobTrainingBinding::inflate
) {

    private var isProcessingOJTTrainingFragment = false
    private var selectedDateStr: String? = null
    private var selectedRandomDateStr: String? = null

    private var selectedAnswer: String = ""
    private var selectednominatedAnswer: String = ""
    private var selectedinstrumentAnswer: String = ""
    private var selectedmaterialsAnswer: String = ""
    private var selectedfacilitiesAnswer: String = ""

    private lateinit var textToSpeech: TextToSpeech
    private var isSpeaking = false

    // ------------------- UI Setup ------------------------

    private fun setupNavHeader() {
        setupToolbar(
            binding.root,
            "HOME",
            showBack = false,
            showLang = true,
            showProfile = true,
            profileClick = { "" },
            langClick = {
                findNavController().navigate(HomeFragmentDirections.actionHomeFrahmentToLanguageChangeFragment())
            }

        )


    }





    override fun initializeViews() {

        setupNavHeader()
        Log.d(
            "FRAGMENT NAME",
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━OJTTrainingFragment━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        )
    }

    override fun setupObservers() {
        // TODO: add your observers
    }

    override fun setupClickListeners() {

        // —— Yes/No/NA RadioGroup Listener ——
        binding.radioGroupYesNoNa.setOnCheckedChangeListener { _, checkedId ->
            selectedAnswer = when (checkedId) {
                binding.radioYes.id -> {
                    binding.yesLayout.visibility = View.VISIBLE
                    binding.etNoReason.visibility = View.GONE
                    "Yes"
                }
                binding.radioNo.id -> {
                    binding.etNoReason.visibility = View.VISIBLE
                    binding.yesLayout.visibility = View.GONE
                    "No"
                }
//                binding.radioNA.id -> {
//                    binding.etNoReason.visibility = View.GONE
//                    binding.yesLayout.visibility = View.GONE
//                    "NA"
//                }
                else -> {
                    binding.etNoReason.visibility = View.GONE
                    binding.yesLayout.visibility = View.GONE
                    ""
                }
            }

            Toast.makeText(requireContext(), selectedAnswer, Toast.LENGTH_SHORT).show()
            Log.d("RadioSelection", "Selected: $selectedAnswer")
        }

        // —— nominated Yes/No ——
        binding.radioGroupnominatedYesNo.setOnCheckedChangeListener { _, checkedId ->
            selectednominatedAnswer = when (checkedId) {
                binding.radionominatedYes.id -> "Yes"
                binding.radionominatedNo.id -> "No"
                else -> ""
            }
            Toast.makeText(requireContext(), selectednominatedAnswer, Toast.LENGTH_SHORT).show()
        }

        // —— instrument Yes/No ——
        binding.radioGroupinstrumentYesNo.setOnCheckedChangeListener { _, checkedId ->
            selectedinstrumentAnswer = when (checkedId) {
                binding.radioinstrumentYes.id -> "Yes"
                binding.radioinstrumentNo.id -> "No"
                else -> ""
            }
            Toast.makeText(requireContext(), selectedinstrumentAnswer, Toast.LENGTH_SHORT).show()
        }

        // —— materials Yes/No ——
        binding.radioGroupmaterialsYesNo.setOnCheckedChangeListener { _, checkedId ->
            selectedmaterialsAnswer = when (checkedId) {
                binding.radiomaterialsYes.id -> "Yes"
                binding.radiomaterialsNo.id -> "No"
                else -> ""
            }
            Toast.makeText(requireContext(), selectedmaterialsAnswer, Toast.LENGTH_SHORT).show()
        }

        // —— facilities Yes/No ——
        binding.radioGroupfacilitiesYesNo.setOnCheckedChangeListener { _, checkedId ->
            selectedfacilitiesAnswer = when (checkedId) {
                binding.radiofacilitiesYes.id -> "Yes"
                binding.radiofacilitiesNo.id -> "No"
                else -> ""
            }
            Toast.makeText(requireContext(), selectedfacilitiesAnswer, Toast.LENGTH_SHORT).show()
        }

        // —— Date Pickers ——
        binding.ivCalendar.setOnClickListener {

            showDatePicker()
        }
        binding.tvAreBoardingandLanding.setOnClickListener {
            val textToRead = requireContext().getString(R.string.are_boarding_and_loading_facilities_provided)
            speakText(textToRead)
        }
        binding.tvHowMuchtipnedGetting.setOnClickListener {
            val textToRead = requireContext().getString(R.string.how_much_stipend_are_you_getting)
            speakText(textToRead)
        }
        binding.tvHowMuchtipned.setOnClickListener {
            val textToRead = requireContext().getString(R.string.how_much_stipend_are_you_eligible_for)
            speakText(textToRead)
        }
        binding.tvAreYouGivenEnough.setOnClickListener {
            val textToRead = requireContext().getString(R.string.are_you_given_enough_materials_to_work_upon)
            speakText(textToRead)

        }
        binding.tvAreYouSufficient.setOnClickListener {
            val textToRead = requireContext().getString(R.string.are_you_given_sufficient_instruments_to_work_during_ojt_tools_machinery_computers_etc)
            speakText(textToRead)
        }
        binding.tvHowmanyTimes.setOnClickListener {
            val textToRead = requireContext().getString(R.string.how_many_times_during_the_day_did_the_supervior_interact_with_you)
            speakText(textToRead)
        }
        binding.tvWhatWhetherthe.setOnClickListener {
            val textToRead = requireContext().getString(R.string.whether_the_field_level_upervisor_isnominated_for_the_ojt)
            speakText(textToRead)
        }
        binding.tvHowmanyTimes.setOnClickListener {

            val textToRead = requireContext().getString(R.string.what_activity_did_you_do_a_random_day_to_be_chosen_by_the_ytem_from_last_6_days)
            speakText(textToRead)
        }





//        binding.tvICandidateAvailable.setOnClickListener {
//
////            val textToRead = getString(R.string.audio_text)
//            val textToRead = requireContext().getString(R.string.is_the_candidatete_available_on_the_day_of_visit_to_the_ojt_location)
//            speakText(textToRead)
//        }


        // Initialize TTS
//        textToSpeech = TextToSpeech(requireContext()) { status ->
//            if (status == TextToSpeech.SUCCESS) {
//                textToSpeech.language = Locale.ENGLISH
//                textToSpeech.setSpeechRate(1.0f)
//
//                textToSpeech.setOnUtteranceProgressListener(object :
//                    UtteranceProgressListener() {
//
//                    override fun onStart(utteranceId: String?) {
//                        requireActivity().runOnUiThread {
//                            isSpeaking = true
//                            binding.imgVoice.setImageResource(R.drawable.ic_pause)
//                        }
//                    }
//                    override fun onDone(utteranceId: String?) {
//                        requireActivity().runOnUiThread {
//                            isSpeaking = false
//                            binding.imgVoice.setImageResource(R.drawable.ic_play)
//                        }
//                    }
//                    override fun onError(utteranceId: String?) {
//                        requireActivity().runOnUiThread {
//                            isSpeaking = false
//                            binding.imgVoice.setImageResource(R.drawable.ic_play)
//                        }
//                    }
//                })
//            }
//        }


        binding.ivRandomCalendar.setOnClickListener {
            val vadDate = "06-01-2026"  // your reference date
            val compareDate = "01-01-2026"
//            val compareDate = selectedDateStr

            // parse dates to compare
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val vadDateObj = sdf.parse(vadDate)
            val compareDateObj = sdf.parse(compareDate)

            // if vadDate is >= compareDate then show range
            if (vadDateObj != null && compareDateObj != null && vadDateObj >= compareDateObj) {
                showRandomDatePicker(compareDateObj, vadDateObj)
            } else {
                Toast.makeText(requireContext(), "Invalid date range", Toast.LENGTH_SHORT).show()
            }
        }
//            showRandomDatePicker()

    }
    private fun speakText(text: String) {
        textToSpeech.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
    }
//    override fun onDestroyView() {
//        super.onDestroyView()
//        isProcessingOJTTrainingFragment = false
//    }




//    private fun stopSpeech() {
//        textToSpeech.stop()
//        isSpeaking = false
//        binding.imageViewIsCandidate.setImageResource(R.drawable.ic_play)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        isProcessingOJTTrainingFragment = false
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    override fun loadInitialData() {
        // TODO: add initial data loading
    }



    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                cal.set(year, month, day)
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                selectedDateStr = sdf.format(cal.time)
                binding.tvSelectedDate.text = selectedDateStr
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
    private fun showRandomDatePicker(minDate: Date, maxDate: Date) {
        val cal = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                cal.set(year, month, day)
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                selectedRandomDateStr = sdf.format(cal.time)
//                binding.tvSelectedRandomDate.text = selectedRandomDateStr
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        // — Set min & max date
        val minCalendar = Calendar.getInstance().apply { time = minDate }
        val maxCalendar = Calendar.getInstance().apply { time = maxDate }

        datePicker.datePicker.minDate = minCalendar.timeInMillis
        datePicker.datePicker.maxDate = maxCalendar.timeInMillis

        datePicker.show()
    }



    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

//    private fun showRandomDatePicker() {
//        val cal = Calendar.getInstance()
//        val datePicker = DatePickerDialog(
//            requireContext(),
//            { _, year, month, day ->
//                cal.set(year, month, day)
//                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//                selectedRandomDateStr = sdf.format(cal.time)
//                binding.tvSelectedRandomDate.text = selectedRandomDateStr
//            },
//            cal.get(Calendar.YEAR),
//            cal.get(Calendar.MONTH),
//            cal.get(Calendar.DAY_OF_MONTH)
//        )
//        datePicker.show()
//    }
}