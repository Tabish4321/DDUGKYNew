package com.deendayalproject.fragments

import SharedViewModel
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.FragmentLoginBinding
import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.util.AppUtil
import java.io.File

class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {

    private lateinit var viewModel: SharedViewModel


    override fun initializeViews() {
        Log.d("FRAGMENT NAME", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━LoginFragmnet━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        // Setup password toggle
        AppUtil.setupPasswordToggle(binding.etPassword)

        // Check for security warnings
        //checkSecurityWarnings()
        checkAutoLogin()
        setupObservers()
        setupClickListeners()
        loadInitialData()
    }

    override fun setupObservers() {
        observeLoginResult()
    }

    override fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            handleLoginClick()
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
        }
    }

    private fun validateInputs(userId: String, password: String): Boolean {
        if (userId.isEmpty()) {
            showToast("Please enter user ID")
            binding.etUserId.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            showToast("Please enter password")
            binding.etPassword.requestFocus()
            return false
        }

        return true
    }

    private fun performLogin(userId: String, password: String) {
        showProgressDialog("Logging in...")

        val request = LoginRequest(
            loginId = userId,
            password = AppUtil.sha512Hash(password),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME
        )

        logNetworkCall("Login API", "POST")
        viewModel.loginUser(request)
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            dismissProgressDialog()

            handleApiResponse(
                responseCode = result.getOrNull()?.responseCode ?: 0,
                data = result.getOrNull(),
                onSuccess = { data ->
                    handleLoginSuccess(data)
                },
                onNoData = {
                    showToast("No data available.")
                },
                onUpgradeRequired = {
                    showToast("Please upgrade your app.")
                },
                onSessionExpired = {
                    handleSessionExpired()
                }
            )
        }

        // Handle loading state if needed
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
               // showProgressDialog("Logging in...")
            } else {
                dismissProgressDialog()
            }
        }
    }

    private fun handleLoginSuccess(data: Any?) {
        val userId = binding.etUserId.text.toString().trim().uppercase()
        val accessToken = (data as? com.deendayalproject.model.response.LoginResponse)?.accessToken ?: ""

        // Save login data
        AppUtil.saveLoginStatus(requireContext(), true)
        AppUtil.saveTokenPreference(requireContext(), accessToken)
        AppUtil.saveLoginIdPreference(requireContext(), userId)

        // Log successful login
        logFragmentEvent("Login_Successful", userId)
        setUserIdentifier(userId)

        // Show success message
        //showSuccessToast("Login Successful")

        // Navigate to home
        findNavController().navigate(R.id.action_fragmentLogin_to_homeFragment)
    }

    private fun handleLoginFailure(exception: Exception) {
        logCrashlyticsError("Login_Failed", exception)
        AppUtil.clearPreferences(requireContext())
        showErrorToast("Login Failed: ${exception.message}")
    }

    private fun checkSecurityWarnings() {
        if (isDeviceRooted() || isRunningOnEmulator()) {
            showSecurityWarning()
        }
    }

    /**
     * Checks if the device is rooted.
     */
    private fun isDeviceRooted(): Boolean {
        val paths = arrayOf(
            "/system/bin/su",
            "/system/xbin/su",
            "/sbin/su",
            "/system/su",
            "/system/bin/.ext/.su",
            "/system/usr/we-need-root/su-backup",
            "/system/xbin/mu"
        )
        return paths.any { File(it).exists() }
    }

    /**
     * Checks if the app is running on an emulator.
     */
    private fun isRunningOnEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.lowercase().contains("emulator")
                || Build.MODEL.lowercase().contains("android sdk built for x86")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("sdk_gphone")
                || Build.BOARD.lowercase().contains("unknown")
                || Build.BRAND.startsWith("generic")
                || Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT)
    }

    /**
     * Shows an alert dialog if the device is rooted or an emulator.
     */
    private fun showSecurityWarning() {
        val message = when {
            isDeviceRooted() && isRunningOnEmulator() -> "Rooted device and Emulator detected! For security reasons, this app cannot run."
            isDeviceRooted() -> "Rooted device detected! For security reasons, this app cannot run."
            isRunningOnEmulator() -> "Emulator detected! This app cannot run on emulators."
            else -> return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Security Warning")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Exit") { _, _ ->
                logFragmentEvent("App_Closed_Security_Warning", message)
                finishAffinity(requireActivity())
            }
            .setOnDismissListener {
                logFragmentEvent("Security_Warning_Dismissed")
            }
            .show()
    }

    // Maintain original method names for compatibility if needed
    fun showProgressBar() {
        showProgressDialog("Loading...")
    }

    fun hideProgressBar() {
        dismissProgressDialog()
    }
}