package com.deendayalproject.fragments
import SharedViewModel
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.databinding.FragmentLoginBinding
import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.util.AppUtil
import java.io.File

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel

    private val progress: AlertDialog? by lazy {
        AppUtil.getProgressDialog(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val logginStatus= AppUtil.getLoginStatus(requireContext())
        if (logginStatus){
            findNavController().navigate(R.id.action_fragmentLogin_to_homeFragment)
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[SharedViewModel::class.java]
        setupListeners()
        observeLoginResult()
    }
    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            if (AppUtil.getSavedLanguagePreference(requireContext()).contains("en")) {

                AppUtil.saveLanguagePreference(requireContext(), "en")

            } else
                AppUtil.changeAppLanguage(
                    requireContext(),
                    AppUtil.getSavedLanguagePreference(requireContext())
                )
            showProgressBar()
            val request = LoginRequest(
                loginId = binding.etUserId.text.toString().trim().uppercase(),
                password = AppUtil.sha512Hash(binding.etPassword.text.toString()),
                imeiNo = AppUtil.getAndroidId(requireContext()),
                appVersion = BuildConfig.VERSION_NAME
            )
                 viewModel.loginUser(request)
        }
        AppUtil.setupPasswordToggle(binding.etPassword)
    }
    private fun observeLoginResult() {

        viewModel.loginResult.observe(viewLifecycleOwner) { result ->

            result.onSuccess {
                hideProgressBar()


                when (it.responseCode) {
                    200 ->  {
                        AppUtil.saveLoginStatus(requireContext(), true)
                        AppUtil.saveTokenPreference( requireContext(),it.accessToken)
                        AppUtil.saveLoginIdPreference( requireContext(),binding.etUserId.text.toString().trim().uppercase())
                        Log.d(requireContext().toString(), "token:Bearer + ${it.accessToken}")
                    Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_fragmentLogin_to_homeFragment)
                    }

                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }

            result.onFailure {
                hideProgressBar()

                AppUtil.clearPreferences(requireContext())
                Toast.makeText(requireContext(), "Login Failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
/*
        viewModel.sessionExpired.observe(viewLifecycleOwner){ expired->
        if (expired){
            AppUtil.showSessionExpiredDialog(findNavController(),requireContext())
        }

        }
*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    fun showProgressBar() {
        if (context != null && isAdded && progress?.isShowing == false) {
            progress?.show()
        }
    }

    fun hideProgressBar() {
        if (progress?.isShowing == true) {
            progress?.dismiss()
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

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Security Warning")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Exit") { _, _ -> finishAffinity(requireActivity()) }
            .show()
    }



}
