package com.deendayalproject.fragments

import SharedViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.R
import com.deendayalproject.BuildConfig
import com.deendayalproject.adapter.ModuleAdapter
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.base.BaseRecyclerAdapter
import com.deendayalproject.databinding.FragmentHomeBinding
import com.deendayalproject.databinding.ItemFormBinding
import com.deendayalproject.databinding.ItemModuleBinding
import com.deendayalproject.databinding.NavigationHeaderBinding
import com.deendayalproject.fragments.ojt.FullScreenDialog
import com.deendayalproject.model.request.ModulesRequest
import com.deendayalproject.model.response.Form
import com.deendayalproject.model.response.Module
import com.deendayalproject.model.response.OJTList
import com.deendayalproject.network.SecurePreferenceManager.clearToken
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.NoDataHelper
import com.deendayalproject.util.ProgressDialogUtil

class HomeFragment : BaseFragment<FragmentHomeBinding>(
bindingInflater = FragmentHomeBinding::inflate
) {

    private lateinit var viewModel: SharedViewModel
   // private lateinit var batchAdapter: BaseRecyclerAdapter<AttendanceBatch, AttendanceBatchLayoutBinding>

    private lateinit var adapter: BaseRecyclerAdapter<Module,ItemModuleBinding>


    // ------------------- UI Setup ------------------------

    private fun setupNavHeader() {
        setupToolbar(
            binding.root,
            getString(R.string.home),
            showBack = false,
            showLang = true,
            showProfile = true,
            profileClick = { binding.drawerLayout.openDrawer(GravityCompat.START) },
            langClick = {findNavController().navigate(HomeFragmentDirections.actionHomeFrahmentToLanguageChangeFragment())
            }

        )

        val headerBinding = NavigationHeaderBinding.bind(binding.navigationView.getHeaderView(0))
        headerBinding.loginId.text = AppUtil.getSavedLoginIdPreference(requireContext())
    }

    private fun setupDrawerClicks() {
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {

                    viewModel.getLogOutAPI( "")
                    viewModel.modules.observe(viewLifecycleOwner) { response ->
                        response.onSuccess { result ->
                            when (result.responseCode) {
                                200 -> {
                                    Toast.makeText(requireContext(),
                                        getString(R.string.logged_out), Toast.LENGTH_SHORT).show()
                                    AppUtil.saveLoginStatus(requireContext(), false)
                                    clearToken(requireContext())
                                    findNavController().navigate(
                                        R.id.fragmentLogin,
                                        null,
                                        androidx.navigation.NavOptions.Builder()
                                            .setPopUpTo(findNavController().graph.startDestinationId, true)
                                            .build()
                                    )
                                }
                                301 ->Toast.makeText(
                                    requireContext(),
                                    getString(R.string.please_upgrade_your_app_first),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        response.onFailure {

                            Toast.makeText(
                                requireContext(),
                                getString(R.string.something_went_wrong_try_again),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
    }
    private var expandedPosition = -1
    private fun updateExpansionUI(binding: ItemModuleBinding, isExpanded: Boolean) {
        binding.rvForms.visibility = if (isExpanded) View.VISIBLE else View.GONE

        binding.ivExpandArrow.animate()
            .rotation(if (isExpanded) 180f else 0f)
            .setDuration(250)
            .start()
    }

    private fun toggleExpand(position: Int) {
        expandedPosition = if (expandedPosition == position) -1 else position
    }

    fun update(newList: List<Module>) {
        adapter.update(newList)
    }

    fun collapseAll() {
        expandedPosition = -1
        adapter.notifyDataSetChanged()
    }
    private fun setupRecycler() {
         adapter = BaseRecyclerAdapter(
            items = emptyList<Module>(),
            bindingInflater = ItemModuleBinding::inflate,
            onBind = { module, binding, position ->

                binding.tvModuleName.text = module.moduleName

              //  var  lists = List(4) { module.forms }.flatten() // for testing

                // Set up nested form adapter
                val formAdapter = BaseRecyclerAdapter(
                    items = module.forms, //lists,
                    bindingInflater = ItemFormBinding::inflate,
                    onBind = { form, formBinding, _ ->
                        formBinding.tvFormName.text = form.formName
                    },
                    onItemClick = { form, _ ->
                        handleFormClick(form)
                    }
                )

                binding.rvForms.apply {
                    layoutManager = LinearLayoutManager(binding.root.context)
                    adapter = formAdapter
                }

                // Apply expand/collapse state
                val isExpanded = position == expandedPosition
                updateExpansionUI(binding, isExpanded)

                // Handle module click
                binding.root.setOnClickListener {
                    toggleExpand(position)
                    adapter.notifyDataSetChanged()
                }
            },
            diffChecker = { old, new -> old.id == new.id },
             recyclerViewParent = binding.container,
             noDataTitle = getString(R.string.no_modules_available_for_your_account),
             noDataDescription = ""
        )
        binding.rvModules.layoutManager = LinearLayoutManager(requireContext())
        binding.rvModules.adapter = adapter

//        adapter = ModuleAdapter(emptyList()) { form: Form ->
//            handleFormClick(form)
//        }
//
//        binding.rvModules.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = this@HomeFragment.adapter
//        }
    }

    private fun handleFormClick(form: Form) {
        when (form.formCd) {
            "TRAINING_CENTER_APP" ->
                navigate(R.id.action_homeFragment_to_centerFragment)

            "RESIDENTIAL_FACILITY_FORM" ->
                navigate(R.id.action_homeFragment_to_rfCenterFragment)

            "TRAINING_CENTER_VERIFICATION" ->
                navigate(R.id.action_homeFragment_to_QTeamListFragment)

            "TRAINING_CENTERS_VERIFICATION_SRLM" ->
                navigate(R.id.action_homeFragment_to_srlmVerListFragment)

            "RESIDENTIAL_FACILITY_FORM_SRLM" ->
                navigate(R.id.action_homeFragment_to_RFSrlmListFragment)

            "RESIDENTIAL_FACILITY_FORM_QTEAM" ->
                navigate(R.id.action_homeFragment_to_RFQTeamListFragment)

            "FIELD_VERIFICATION_FORM" ->
                navigate(R.id.action_homeFragment_to_fieldVerificationFragment)

            "DDUGKY_CANDIDATE_ATTENDANCE_APP" ->
                navigate(R.id.action_homeFragment_to_attendanceBatchListFragment)
            "OJT_VERIFICATION_FORM_APP" ->


//            navigate(R.id.action_homeFragment_to_SelectionSrlm)
                navigate(R.id.action_homeFragment_to_SelectionFragment)

        }
    }

    private fun navigate(id: Int) {
        findNavController().navigate(id)
    }

    // ------------------- API & Observers ------------------------

    private fun fetchModules() {
        val loginId = AppUtil.getSavedLoginIdPreference(requireContext())
//        val token ="" //AppUtil.getSavedTokenPreference(requireContext())
        val token =AppUtil.getSavedTokenPreference(requireContext())

        val request = ModulesRequest(
            loginId = loginId,
            appVersion = BuildConfig.VERSION_NAME
        )

        Log.d("HomeFragment", "Using token: $token")
        //dismissProgressDialog()
        showProgressDialog("Loading....")
        viewModel.fetch(request, "Bearer $token")
    }

    private fun observeViewModel() {
        viewModel.modules.observe(viewLifecycleOwner) { response ->
            response.onSuccess { result ->
                dismissProgressDialog()

                handleApiResponse(responseCode = result.responseCode,
                    result.wrappedList,
                    onSuccess = {
               // val updated = emptyList<Module>() // for testing
                        val updated = result.wrappedList?.map { module ->
                    module.isExpanded = false
                    module
                    } ?: emptyList()
                     adapter.update(updated)
                        adapter.notifyDataSetChanged()

                        if (updated.isEmpty()) {
                            showToast( getString(R.string.no_modules_available_for_your_account))
                        }
                 },
                    onSessionExpired = { AppUtil.showSessionExpiredDialog(findNavController(), requireContext())},
                )
            }

            response.onFailure {
                dismissProgressDialog()
                if (it is retrofit2.HttpException && it.code() == 401) {
                    AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }

                Toast.makeText(
                    requireContext(),
                    getString(R.string.something_went_wrong_try_again),
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
    }

    override fun initializeViews() {
        Log.d("FRAGMENT NAME", "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━HomeFragment━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        setupNavHeader()
        setupViewModel()
        }

    override fun setupObservers() {
        observeViewModel()
    }

    override fun setupClickListeners() {
        setupDrawerClicks()
    }

    override fun loadInitialData() {
        if(AppUtil.getSavedLoginIdPreference(requireContext()) == BuildConfig.USER_NAME_FOR_APP){
            val action = HomeFragmentDirections.actionHomeFragmentToQTeamFormFragment("1", "DDUGKY Training Center", "S2025")
            findNavController().navigate(action)
        }else{
            fetchModules()
            setupRecycler()
        }

    }
}
