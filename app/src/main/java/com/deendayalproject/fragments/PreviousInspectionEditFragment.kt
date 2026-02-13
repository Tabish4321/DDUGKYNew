package com.deendayalproject.fragments


import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.findNavController
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.PreviousInspectionEditFragmentBinding
import com.deendayalproject.fragments.composeui.PreviousInspectionComplete

class PreviousInspectionEditFragment : BaseFragment<PreviousInspectionEditFragmentBinding>(
    bindingInflater = PreviousInspectionEditFragmentBinding::inflate
) {


    private var dateOfInspection = ""
    private var conductedBy = ""
    private var observation = ""
    private var actionTaken = ""
    private var remarks = ""

    override fun initializeViews() {




        dateOfInspection = arguments?.getString("dateOfInspection").toString()
        conductedBy = arguments?.getString("conductedBy").toString()
        observation = arguments?.getString("observation").toString()
        actionTaken = arguments?.getString("actionTaken").toString()
        remarks = arguments?.getString("remarks").toString()



        binding.composeViewPrevious.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {

                PreviousInspectionComplete(
                    dateOfInspection,
                    conductedBy,
                    observation,
                    actionTaken,
                    remarks,
                    onSubmit = { answer, remarks ->

                        // 🔥 Call API here


                      /*  viewModel.submitCompliance(
                            inspectionId = inspectionItem?.id ?: 0,
                            status = answer,
                            remarks = remarks
                        )*/
                    },

                    onBackClick = {
                        findNavController().popBackStack()
                    }
                )




            }

        }

    }

    override fun setupObservers() {
    }

    override fun setupClickListeners() {
    }

    override fun loadInitialData() {
    }


}
