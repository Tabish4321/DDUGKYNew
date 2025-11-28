package com.deendayalproject.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentLaguageChangeBinding
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.gone
import com.deendayalproject.util.visible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class LanguageChangeFragment : BaseFragment<FragmentLaguageChangeBinding>(
    FragmentLaguageChangeBinding::inflate
) {
    private lateinit var languageIconMap: Map<String, View>

    private val languageViewMap by lazy {
        mapOf(
            binding.languageEng to "en",
            binding.languageHindi to "hi",
            binding.languageTamil to "ta",
            binding.languageAssamese to "as",
            binding.languageBengali to "bn",
            binding.languageGujarati to "gu",
            binding.languageKannada to "kn",
            binding.languageMalayalam to "ml",
            binding.languageOdia to "or",
            binding.languageMarathi to "mr",
            binding.languagePunjabi to "pa",
            binding.languageTelugu to "te",
            binding.languageUrdu to "ur"
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initializeViews() {
        setupLanguageIconMap()
        showSelectedLanguageIcon(AppUtil.getSavedLanguagePreference(requireContext()))
        setupClickListeners()
        binding.progressBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun setupObservers() {}

    override fun loadInitialData() {}

    private fun setupLanguageIconMap() {
        languageIconMap = mapOf(
            "en" to binding.checkEnglishIcon,
            "hi" to binding.checkIconHindi,
            "ta" to binding.checkTamilIcon,
            "as" to binding.checkAssameseIcon,
            "bn" to binding.checkBengaliIcon,
            "gu" to binding.checkGujaratiIcon,
            "kn" to binding.checkKannadaIcon,
            "ml" to binding.checkMalayalamIcon,
            "or" to binding.checkOdiaIcon,
            "mr" to binding.checkMarathiIcon,
            "pa" to binding.checkPunjabiIcon,
            "te" to binding.checkTeluguIcon,
            "ur" to binding.checkUrduIcon
        )
    }

    private fun showSelectedLanguageIcon(languageCode: String) {
        // Hide all icons first
        languageIconMap.values.forEach { it.gone() }
        // Show selected language icon
        languageIconMap[languageCode]?.visible()
    }

    override fun setupClickListeners() {
        languageViewMap.forEach { (view, languageCode) ->
            view.setOnClickListener { confirmLanguageChange(languageCode) }
        }
    }

    private fun confirmLanguageChange(languageCode: String) {
        showYesNoDialog(
            context = requireContext(),
            title = "Confirmation",
            message = "Do you want to change language?",
            onYesClicked = {
                lifecycleScope.launch {
                    AppUtil.changeAppLanguage(requireContext(), languageCode)
                    AppUtil.saveLanguagePreference(requireContext(), languageCode)
                    showSelectedLanguageIcon(languageCode)
                    findNavController().navigateUp()
                }
            },
            onNoClicked = {
                // No action needed
            }
        )
    }

    private fun showYesNoDialog(
        context: Context,
        title: String,
        message: String,
        onYesClicked: () -> Unit,
        onNoClicked: () -> Unit
    ) {
        val dialog = MaterialAlertDialogBuilder(
            context, R.style.ModernDialogTheme
        ).apply {
            setTitle(title)
            setMessage(message)
            setBackground(ContextCompat.getDrawable(context, R.drawable.dialog_background))

            // Positive button (Yes) - Primary color
            setPositiveButton("Yes") { dialog, _ ->
                onYesClicked()
                dialog.dismiss()
            }

            // Negative button (No) - Outline style
            setNegativeButton("No") { dialog, _ ->
                onNoClicked()
                dialog.dismiss()
            }
        }.create()

        // Custom button styling
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            // Style positive button
            positiveButton.setTextColor(ContextCompat.getColor(context, R.color.button_start_color))
            positiveButton.background = ContextCompat.getDrawable(context, R.drawable.button_primary)

            // Style negative button
            negativeButton.setTextColor(ContextCompat.getColor(context, R.color.black))
            negativeButton.background = ContextCompat.getDrawable(context, R.drawable.login_screen_bg)

            // Add modern typography
            positiveButton.typeface = ResourcesCompat.getFont(context, R.font.avenir_next_semi_bold)
            negativeButton.typeface = ResourcesCompat.getFont(context, R.font.avenir_next_medium)
        }

        dialog.show()

        dialog.window?.apply {
            setBackgroundDrawableResource(R.drawable.dialog_background)
            setDimAmount(0.6f)
        }

    }
}