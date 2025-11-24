package com.deendayalproject.fragments
import SharedViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.R
import com.deendayalproject.BuildConfig
import com.deendayalproject.adapter.ModuleAdapter
import com.deendayalproject.databinding.FragmentHomeBinding
import com.deendayalproject.databinding.NavigationHeaderBinding
import com.deendayalproject.model.request.ModulesRequest
import com.deendayalproject.model.response.Form
import com.deendayalproject.util.AppUtil


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: ModuleAdapter
    private val progress: AlertDialog? by lazy {
        AppUtil.getProgressDialog(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // First, get the header view using getHeaderView()
        val headerView = binding.navigationView.getHeaderView(0)

        // Now, bind the header layout using the generated ViewBinding for the header
        val headerBinding = NavigationHeaderBinding.bind(headerView)

        // Access the ImageView from the header layout
        headerBinding.circleImageView
        val headerIdView: TextView = headerBinding.loginId

        headerIdView.text = AppUtil.getSavedLoginIdPreference(requireContext())

        binding.profilePic.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

//New Logout (Rohit)
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()

                    // Clear saved login status
                    AppUtil.saveLoginStatus(requireContext(), false)

                    // Navigate back to login fragment and clear back stack
                    findNavController().navigate(
                        R.id.fragmentLogin,
                        null,
                        androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(findNavController().graph.startDestinationId, true)
                            .build()
                    )

                    // Close the drawer
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                else -> false
            }
        }
/*binding.changeLanguage.setOnClickListener {
    findNavController().navigate(R.id.action_homeFragment_to_languageSelectionFragment)
}*/

        // Initialize ViewModel scoped to this Fragment
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        // Initialize adapter with empty list and form click listener lambda
        adapter = ModuleAdapter(emptyList()) { form: Form ->
            // Show Toast with formCd when form clicked
            Log.i("form value formCd :: ",form.formCd)

            if (form.formCd == "TRAINING_CENTER_APP") {
                findNavController().navigate(R.id.action_homeFragment_to_centerFragment)
            }
            if (form.formCd=="RESIDENTIAL_FACILITY_FORM"){
                findNavController().navigate(R.id.action_homeFragment_to_rfCenterFragment)
            }

            if (form.formCd == "TRAINING_CENTER_VERIFICATION") {
                findNavController().navigate(R.id.action_homeFragment_to_QTeamListFragment)
            }

            if (form.formCd == "TRAINING_CENTERS_VERIFICATION_SRLM") {
                findNavController().navigate(R.id.action_homeFragment_to_srlmVerListFragment)
            }


            if (form.formCd == "RESIDENTIAL_FACILITY_FORM_SRLM") {
                findNavController().navigate(R.id.action_homeFragment_to_RFSrlmListFragment)
            }
            if (form.formCd == "RESIDENTIAL_FACILITY_FORM_QTEAM") {
                findNavController().navigate(R.id.action_homeFragment_to_RFQTeamListFragment)
            }

            /* Field Verification */
            if (form.formCd == "FIELD_VERIFICATION_FORM") {
                findNavController().navigate(R.id.action_homeFragment_to_fieldVerificationFragment)
            }

        }

        // Setup RecyclerView with adapter
        binding.rvModules.layoutManager = LinearLayoutManager(requireContext())
        binding.rvModules.adapter = adapter

        observeViewModel()

        val modulesRequest = ModulesRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            appVersion = BuildConfig.VERSION_NAME

        )
        val token = AppUtil.getSavedTokenPreference(requireContext())
        Log.d("HomeFragment", "Using token: $token")

        // Trigger data fetch with token
        viewModel.fetch(
            modulesRequest,
            "Bearer ${AppUtil.getSavedTokenPreference(requireContext())}"
        )
        showProgressBar()
    }
    private fun observeViewModel() {
        viewModel.modules.observe(viewLifecycleOwner) { response ->
            response.onSuccess {
                hideProgressBar()


                when (it.responseCode) {

                    200 -> {
                        // Initialize modules as collapsed (optional)
                        val collapsedModules = it.wrappedList?.map { module ->
                            module.isExpanded = false
                            module
                        } ?: emptyList()
                        adapter.updateData(collapsedModules)

                    }

                    401 -> {
                        AppUtil.showSessionExpiredDialog(findNavController(), requireContext())

                    }
                }

            }
            response.onFailure {

                hideProgressBar()
                if (it is retrofit2.HttpException && it.code() == 401) {
                    AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
                Toast.makeText(
                    requireContext(),
                    "Something went wrong try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }


/*
        viewModel.sessionExpired.observe(viewLifecycleOwner){ expired ->
            if (expired){
                Log.d("homeFragment","sessionexpired")
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
}
