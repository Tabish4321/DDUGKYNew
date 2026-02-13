package com.deendayalproject.fragments.composeui

import PreviousInspectionItemResponse
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.InspectionBasicFragmentBinding
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.ui.res.colorResource
import androidx.core.os.bundleOf
import com.deendayalproject.R
import androidx.navigation.fragment.findNavController
import com.deendayalproject.fragments.InspectionListFragmentDirections


class InspectionBasicDetailsFragment : BaseFragment<InspectionBasicFragmentBinding>(
    bindingInflater = InspectionBasicFragmentBinding::inflate
) {


    private var trainingCenterId = 0
    private var prnNumber = ""
    private var sanctionOrder = ""
    private var inspectionType = ""




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideStatusBar()

    }



    override fun initializeViews() {


        trainingCenterId = arguments?.getInt("trainingCenterId",0)!!
        prnNumber = arguments?.getString("prnNumber").toString()
        sanctionOrder = arguments?.getString("sanctionOrder").toString()
        inspectionType = arguments?.getString("inspectionType").toString()


        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                InspectionModernScreen(
                    prnNumber,
                    sanctionOrder,
                    inspectionType,
                    trainingCenterId.toString(),
                    onEditClick = { inspectionId ->



                        findNavController().navigate(
                            InspectionBasicDetailsFragmentDirections.actionInspectionBasicDetailsFragmentToPreviousInspectionEditFragment
                                (inspectionId.date,inspectionId.conductedBy,inspectionId.observations,inspectionId.actionTaken,inspectionId.remarks)
                        )

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionModernScreen( prnNumber: String, sanctionLetter: String,inspectionType: String,trainingCenterId: String,
                            onEditClick: (PreviousInspectionItemResponse) -> Unit ) {

    var currentStep by remember { mutableStateOf(1) }
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current

    val sampleInspectionList = listOf(
        PreviousInspectionItemResponse(
            1,
            "12 Jan 2026",
            "Rahul Sharma",
            "Lab setup completed",
            "Minor corrections",
            "All good",
            "Complied"
        ),
        PreviousInspectionItemResponse(
            2,
            "05 Feb 2026",
            "Amit Verma",
            "Safety issue found",
            "Pending action",
            "Need improvement",
            "Not Complied"
        )
    )


    Scaffold(
        topBar = {
            PremiumTopBar(
                "Training Center Details",
                onBackClick = {
                    backDispatcher?.onBackPressedDispatcher?.onBackPressed()
                }
            )
        },

        bottomBar = {
            Surface(tonalElevation = 8.dp,
                color = colorResource(R.color.white)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {

                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = { currentStep-- },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Previous")
                        }
                    }

                    Button(
                        onClick = {
                            if (currentStep < 3) currentStep++
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(if (currentStep < 3) "Continue" else "Submit")
                    }
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize().background(colorResource(id = R.color.white)),
        ) {

            // 🔹 FIXED HEADER (Not Scrollable)
            InspectionProgressHeader(currentStep)

            // 🔹 SCROLLABLE CONTENT
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),


            ) {

                when (currentStep) {

                    1 -> {
                        item { TrainingCenterDetails(prnNumber,sanctionLetter,inspectionType,trainingCenterId)


                            PreviousInspectionSection(
                                items = sampleInspectionList,
                                onEditClick = { selectedItem ->

                                        onEditClick(selectedItem)

                                }
                            )


                        }


                    }

                    2 -> {
                        item { }

                    }

                    3 -> {
                        item { Text("Final Review Screen") }
                    }
                }
            }
        }
    }
}



