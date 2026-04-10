package com.deendayalproject.fragments


import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan

import android.provider.Settings
import SharedViewModel
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentLoginBinding
import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.network.SecurePreferenceManager.saveToken
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.validateDeviceSecurity
//import com.deendayalproject.util.validateDeviceSecurity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {

    //GitTestingComment
    private lateinit var viewModel: SharedViewModel
    private var isProcessingLogin = false

    override fun initializeViews() {
        Log.d("FRAGMENT NAME", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━LoginFragment━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        binding.versionText.text = "v${BuildConfig.VERSION_NAME}"
        hideStatusBar()
        setupKeyboardDismissHandler()
        disableCopyPaste()
        setupPasswordToggle()
        setupEditTextListeners()
        checkAutoLogin()
        //disableScreenshots()
    }


    private fun setupKeyboardDismissHandler() {
        binding.fragmentContainer.setOnClickListener {
            dismissKeyboard()
        }
    }

    private fun disableScreenshots() {
        requireActivity().window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_SECURE,
            android.view.WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    private fun dismissKeyboard(view: View? = null) {
        val imm = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java) as? InputMethodManager ?: return
        val windowToken = view?.windowToken ?: requireActivity().currentFocus?.windowToken ?: return
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun showKeyboard(view: View) {
        val imm = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java) as? InputMethodManager ?: return
        view.requestFocus()
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }


    private fun setupPasswordToggle() {
        val showPasswordDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_passwordon)
        val hidePasswordDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_passwordtoggle)

        // Set initial drawable
        binding.etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, hidePasswordDrawable, null)

        // Set touch listener for toggle
        binding.etPassword.setOnTouchListener { v, event ->
            val drawableRight = binding.etPassword.compoundDrawablesRelative[2]
            if (drawableRight != null && event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.etPassword.right - drawableRight.bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun togglePasswordVisibility() {
        val inputType = binding.etPassword.inputType
        val cursorPosition = binding.etPassword.selectionStart

        if (inputType == android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            // Show password
            binding.etPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            val showPasswordDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_passwordon)
            binding.etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, showPasswordDrawable, null)
        } else {
            // Hide password
            binding.etPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            val hidePasswordDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_passwordtoggle)
            binding.etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, hidePasswordDrawable, null)
        }

        // Restore cursor and move to end
        binding.etPassword.setSelection(cursorPosition)
        binding.etPassword.post {
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }
    }

    private fun setupEditTextListeners() {
        binding.etUserId.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.etPassword.requestFocus()
                showKeyboard(binding.etPassword)
                return@setOnEditorActionListener true
            }
            false
        }

        binding.etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                dismissKeyboard()
                if (!isProcessingLogin) {
                    handleLoginClick()
                }
                return@setOnEditorActionListener true
            }
            false
        }
    }

    override fun setupObservers() {
        observeLoginResult()
    }

    override fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            if (!isProcessingLogin) {
                handleLoginClick()
            }
        }
    }

    override fun loadInitialData() {
        // Nothing to load initially for login
    }

    private fun checkAutoLogin() {
        val loginStatus = AppUtil.getLoginStatus(requireContext())
        if (loginStatus) {
            logFragmentEvent("Auto_Login_Detected")
            findNavController().navigate(R.id.action_fragmentLogin_to_homeFragment)
        }
    }

    private fun handleLoginClick() {

//        if (!validateDeviceSecurity(requireContext())) {
//            resetButtonState()
//            return
//        }

        if (isProcessingLogin) return

        isProcessingLogin = true
        binding.btnLogin.isEnabled = false
        binding.btnLogin.alpha = 0.7f

        // Dismiss keyboard using button's window token to ensure click registers properly
        dismissKeyboard(binding.btnLogin)

        // Set language based on preference
        if (AppUtil.getSavedLanguagePreference(requireContext()).contains("en")) {
            AppUtil.saveLanguagePreference(requireContext(), "en")
        } else {
            AppUtil.changeAppLanguage(
                requireContext(),
                AppUtil.getSavedLanguagePreference(requireContext())
            )
        }

        val userId = binding.etUserId.text.toString().trim().uppercase()
        val password = binding.etPassword.text.toString()

        if (validateInputs(userId, password)) {
            performLogin(userId, password)
        } else {
            resetButtonState()
        }
    }

    private fun resetButtonState() {
        isProcessingLogin = false
        binding.btnLogin.isEnabled = true
        binding.btnLogin.alpha = 1.0f
    }

    private fun validateInputs(userId: String, password: String): Boolean {
        if (userId.isEmpty()) {
            showToast("Please enter user ID")
            binding.etUserId.requestFocus()
            showKeyboard(binding.etUserId)
            return false
        }

        if (password.isEmpty())
        {
            showToast(getString(R.string.please_enter_password))
            binding.etPassword.requestFocus()
            showKeyboard(binding.etPassword)
            return false
        }

        return true
    }

    private fun performLogin(userId: String, password: String) {
        showProgressDialog(getString(R.string.logging_in))

        val request = LoginRequest(
            loginId = userId,
            password = AppUtil.sha512Hash(password),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
        )

        logNetworkCall("Login API", "POST")

        viewModel.loginUser(request)
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            resetButtonState()
            dismissProgressDialog()

            when {
                result.isSuccess -> {
                    val data = result.getOrNull()
                    handleApiResponse(
                        responseCode = data?.responseCode ?: 0,
                        data = data,
                        onSuccess = { responseData ->
                            handleLoginSuccess(responseData)
                        },
                        onNoData = {
                            showToast(getString(R.string.no_data_available))
                        },
                        onUpgradeRequired = {
                            showToast(getString(R.string.please_upgrade_your_app))
                        },
                        onSessionExpired = {
                            showToast(data?.responseDesc.toString())
                            handleSessionExpired()
                        }
                    )
                }
                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    handleLoginFailure(exception as Exception?)
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (!loading) {
                dismissProgressDialog()
            }
        }
    }

    private fun handleLoginSuccess(data: Any?) {
        val userId = binding.etUserId.text.toString().trim().uppercase()
        val accessToken = (data as? com.deendayalproject.model.response.LoginResponse)?.accessToken ?: ""

        AppUtil.saveLoginStatus(requireContext(), true)
       // AppUtil.saveTokenPreference(requireContext(), accessToken)
        AppUtil.saveLoginIdPreference(requireContext(), userId)
        saveToken(requireContext(), accessToken)

        logFragmentEvent("Login_Successful", userId)
        setUserIdentifier(userId)

        // Clear password for security
        binding.etPassword.text?.clear()

        // Dismiss keyboard before navigation
        dismissKeyboard()

        findNavController().navigate(R.id.action_fragmentLogin_to_homeFragment)
    }

    private fun handleLoginFailure(exception: Exception?) {
        logCrashlyticsError("Login_Failed", exception ?: Exception("Unknown error"))
        showErrorToast(getString(R.string.login_failed, exception?.message ?: getString(R.string.unknown_error)))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // Ensure keyboard is dismissed and state reset - safe call without binding access
        dismissKeyboard()
        isProcessingLogin = false
    }

    private fun disableCopyPaste() {

        fun disable(editText: android.widget.EditText) {
            editText.setTextIsSelectable(false)
            editText.customSelectionActionModeCallback =
                object : android.view.ActionMode.Callback {
                    override fun onCreateActionMode(mode: android.view.ActionMode?, menu: android.view.Menu?) = false
                    override fun onPrepareActionMode(mode: android.view.ActionMode?, menu: android.view.Menu?) = false
                    override fun onActionItemClicked(mode: android.view.ActionMode?, item: android.view.MenuItem?) = false
                    override fun onDestroyActionMode(mode: android.view.ActionMode?) {}
                }
            editText.isLongClickable = false
        }

        disable(binding.etUserId)
        disable(binding.etPassword)
    }



}