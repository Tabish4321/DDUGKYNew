package com.deendayalproject.fragments
import SharedViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.databinding.FragmentLoginBinding
import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.util.AppUtil

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel

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
                //AppUtil.clearPreferences(requireContext())
                Toast.makeText(requireContext(), "Login Failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


/*
    private fun observeLoginResult() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                val token = "Bearer " + it.accessToken
                AppUtil.saveTokenPreference(requireContext(), token)
                AppUtil.saveLoginIdPreference(requireContext(), binding.etUserId.text.toString().trim().uppercase())
                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_fragmentLogin_to_homeFragment)
            }
            result.onFailure {
                AppUtil.clearPreferences(requireContext())
                Toast.makeText(requireContext(), "Session expired: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
