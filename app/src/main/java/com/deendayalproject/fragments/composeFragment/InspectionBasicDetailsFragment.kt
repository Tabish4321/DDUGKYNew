package com.deendayalproject.fragments.composeFragment

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
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.colorResource
import com.deendayalproject.R
import androidx.navigation.fragment.findNavController
import com.deendayalproject.fragments.composeui.previous_inspection.CandidateVerificationBottomSheet
import com.deendayalproject.fragments.composeui.common.InspectionProgressHeader
import com.deendayalproject.fragments.composeui.previous_inspection.PreviousInspectionSection
import com.deendayalproject.fragments.composeui.trainingCenListAandDetails.TrainingCenterDetails
import com.deendayalproject.fragments.composeui.batchAndCandidate.BatchListSection
import com.deendayalproject.fragments.composeui.batchAndCandidate.CandidateDataPreviousBatchCard
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.fragments.composeui.common.ShimmerTrainingList
import com.deendayalproject.model.response.CandidateListInspectionRes
import com.deendayalproject.model.response.batchListRes
import kotlinx.coroutines.delay


class InspectionBasicDetailsFragment : BaseFragment<InspectionBasicFragmentBinding>(
    bindingInflater = InspectionBasicFragmentBinding::inflate
) {


    private var trainingCenterId = 0
    private var prnNumber = ""
    private var sanctionOrder = ""
    private var inspectionType = ""




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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


                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(1000)
                    isLoading = false
                }




                InspectionModernScreen(
                    prnNumber,
                    sanctionOrder,
                    inspectionType,
                    trainingCenterId.toString(),
                    isLoading = isLoading,
                    onEditClick = { inspectionId ->



                        findNavController().navigate(
                            InspectionBasicDetailsFragmentDirections.actionInspectionBasicDetailsFragmentToPreviousInspectionEditFragment
                                (inspectionId.date,inspectionId.conductedBy)
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
                            isLoading: Boolean, onEditClick: (PreviousInspectionItemResponse) -> Unit ) {

    var currentStep by remember { mutableStateOf(1) }
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current
    var selectedCandidate by remember {
        mutableStateOf<CandidateListInspectionRes?>(null)
    }

    var selectedBatch by remember { mutableStateOf<batchListRes?>(null) }

    val toolbarTitle = when (currentStep) {
        1 -> "Training Center Details"
        2 -> {
            if (selectedBatch != null)
                "Candidate List"
            else
            "Previous Batch List"
        }
        3 -> "Final Review"
        else -> "Inspection"
    }



// Batch data


    val sampleBatchList = listOf(
        batchListRes(1, "BATCH-001"),
        batchListRes(2, "BATCH-002"),
        batchListRes(3, "BATCH-003")
    )


  // candidate data


    val sampleCandidateList = listOf(
        CandidateListInspectionRes(
            candidateId = 123456789,
            name = "Rahul Kumar",
            rollNumber = "RN001",
            contactNumber = "9876543210"
        ),
        CandidateListInspectionRes(
            candidateId = 123456789,
            name = "Amit Sharma",
            rollNumber = "RN002",
            contactNumber = "9123456789"
        ),
        CandidateListInspectionRes(
            candidateId = 123456789,
            name = "Priya Singh",
            rollNumber = "RN003",
            contactNumber = "9988776655"
        ),
        CandidateListInspectionRes(
            candidateId = 123456789,
            name = "Naman Singh",
            rollNumber = "RN004",
            contactNumber = "9988776655"
        ),
        CandidateListInspectionRes(
            candidateId = 123456789,
            name = "Pankaj",
            rollNumber = "RN005",
            contactNumber = "9988776655"
        )
    )



    // inspection data

    val sampleInspectionList = listOf(
        PreviousInspectionItemResponse(
            1,
            "12 Jan 2026",
            "Rahul Sharma",
            "Inspection"
        ),
        PreviousInspectionItemResponse(
            2,
            "05 Feb 2026",
            "Amit Verma",
            "Due Diligence"
        )
    )


    Scaffold(
        topBar = {
            PremiumTopBar(
                toolbarTitle,
                onBackClick = {
                    //backDispatcher?.onBackPressedDispatcher?.onBackPressed()

                    if (currentStep > 1) {


                        // 🔥 IMPORTANT LOGIC HERE
                        if (currentStep == 2 && selectedBatch != null) {
                            selectedBatch = null   // Back to Batch List
                        } else {
                            currentStep--
                        }


                    } else
                        backDispatcher?.onBackPressedDispatcher?.onBackPressed()
                }
            )
        },

        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                color = colorResource(R.color.white)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {

                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = {

                                // 🔥 IMPORTANT LOGIC HERE
                                if (currentStep == 2 && selectedBatch != null) {
                                    selectedBatch = null   // Back to Batch List
                                } else {
                                    currentStep--
                                }

                            },
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





        if (currentStep == 2 && selectedCandidate != null) {

            CandidateVerificationBottomSheet(
                candidateData = selectedCandidate!!,
                onDismiss = { selectedCandidate = null },
                onSubmit = {
                    selectedCandidate = null
                }
            )
        }





        if (isLoading) {

            Box(modifier = Modifier.padding(padding)) {
                ShimmerTrainingList()
            }

        } else {


            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize().background(colorResource(id = R.color.white)),
            )
            {

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
                            item {
                                TrainingCenterDetails(
                                    prnNumber,
                                    sanctionLetter,
                                    inspectionType,
                                    trainingCenterId
                                )


                                PreviousInspectionSection(
                                    items = sampleInspectionList,
                                    onEditClick = { selectedItem ->

                                        onEditClick(selectedItem)

                                    }
                                )


                            }


                        }

                        2 -> {


                            if (selectedBatch == null) {

                                // 🔹 Show Batch List First
                                item {
                                    BatchListSection(
                                        batchList = sampleBatchList,
                                        onBatchClick = { batch ->
                                            selectedBatch = batch   // 🔥 open candidate list
                                        }
                                    )
                                }

                            } else {

                                // 🔹 Show Candidate List After Batch Click
                                item {
                                    CandidateDataPreviousBatchCard(
                                        candidate = sampleCandidateList,
                                        onVerifyCandidateClick = {
                                            selectedCandidate = it
                                        }
                                    )
                                }
                            }



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
}



