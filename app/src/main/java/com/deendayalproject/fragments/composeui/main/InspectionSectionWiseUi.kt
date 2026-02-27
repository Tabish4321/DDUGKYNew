package com.deendayalproject.fragments.composeui.main

import PreviousInspectionItemResponse
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.deendayalproject.R
import com.deendayalproject.fragments.composeui.batchAndCandidate.BatchListSection
import com.deendayalproject.fragments.composeui.batchAndCandidate.CandidateDataPreviousBatchCard
import com.deendayalproject.fragments.composeui.common.InspectionProgressHeader
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.fragments.composeui.common.ShimmerTrainingList
import com.deendayalproject.fragments.composeui.previous_inspection.PreviousInspectionSection
import com.deendayalproject.fragments.composeui.previous_inspection.ProCandidateBottomSheet
import com.deendayalproject.fragments.composeui.trainingCenListAandDetails.TrainingCenterDetails
import com.deendayalproject.model.response.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionModernScreen(
    prnNumber: String,
    sanctionLetter: String,
    inspectionType: String,
    trainingCenterId: String,
    batchList: List<PrevBatchItem>,
    candidateList: List<CandidateItem>,      // 🔥 API DATA
    currentStep: Int,
    onBatchSelected: (Int?) -> Unit,         // 🔥 API CALL TRIGGER
    onStepChange: (Int) -> Unit,
    isLoading: Boolean,
    trainingDetails: TrainingInspCenterDetails?,
    onEditClick: (PreviousInspectionItemResponse) -> Unit
) {

    val backDispatcher = LocalOnBackPressedDispatcherOwner.current

    var selectedCandidate by remember {
        mutableStateOf<CandidateListInspectionRes?>(null)
    }

    var selectedBatch by remember {
        mutableStateOf<PrevBatchItem?>(null)
    }

    val toolbarTitle = when (currentStep) {
        1 -> "Training Center Details"
        2 -> if (selectedBatch != null) "Candidate List" else "Previous Batch List"
        3 -> "Final Review"
        else -> "Inspection"
    }

    val sampleInspectionList = listOf(
        PreviousInspectionItemResponse(1, "12 Jan 2026", "Rahul Sharma", "Inspection"),
        PreviousInspectionItemResponse(2, "05 Feb 2026", "Amit Verma", "Due Diligence")
    )

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            topBar = {
                PremiumTopBar(
                    toolbarTitle,
                    onBackClick = {
                        if (currentStep > 1) {
                            if (currentStep == 2 && selectedBatch != null) {
                                selectedBatch = null
                            } else {
                                onStepChange(currentStep - 1)
                            }
                        } else {
                            backDispatcher?.onBackPressedDispatcher?.onBackPressed()
                        }
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
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        if (currentStep > 1) {
                            OutlinedButton(
                                onClick = {
                                    if (currentStep == 2 && selectedBatch != null) {
                                        selectedBatch = null
                                    } else {
                                        onStepChange(currentStep - 1)
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
                                if (currentStep < 3)
                                    onStepChange(currentStep + 1)
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

            if (isLoading) {
                Box(modifier = Modifier.padding(padding)) {
                    ShimmerTrainingList()
                }
            } else {

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(colorResource(id = R.color.white))
                ) {

                    InspectionProgressHeader(currentStep)

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        when (currentStep) {

                            /* ---------------- STEP 1 ---------------- */
                            1 -> {
                                item {

                                    TrainingCenterDetails(
                                        prnNumber = prnNumber,
                                        sanctionLetter = sanctionLetter,
                                        inspectionType = inspectionType,
                                        trainingCenterId = trainingDetails?.trainingCenterId ?: "",
                                        trainingCenterName = trainingDetails?.trainingCenterName ?: "",
                                        inchargeName = trainingDetails?.inchargeName ?: "",
                                        mobileNumber = trainingDetails?.mobileNumber ?: "",
                                        email = trainingDetails?.emailId ?: "",
                                        tradeAndCapacity = trainingDetails?.tradeAndCapacity ?: "",
                                        coordinate = trainingDetails?.coordinate ?: "",
                                        roleName = trainingDetails?.roleName ?: ""
                                    )

                                    PreviousInspectionSection(
                                        items = sampleInspectionList,
                                        onEditClick = { onEditClick(it) }
                                    )
                                }
                            }

                            /* ---------------- STEP 2 ---------------- */
                            2 -> {

                                if (selectedBatch == null) {

                                    item {
                                        if (batchList.isEmpty()) {
                                            Text("No batch available")
                                        } else {
                                            BatchListSection(
                                                batchList = batchList,
                                                onBatchClick = {
                                                    selectedBatch = it
                                                    onBatchSelected(it.batchId)   // 🔥 CALL API
                                                }
                                            )
                                        }
                                    }

                                } else {

                                    item {

                                        if (candidateList.isEmpty()) {

                                            Text("No candidates available")

                                        } else {

                                            CandidateDataPreviousBatchCard(
                                                candidate = candidateList.map {
                                                    CandidateListInspectionRes(
                                                        candidateId = it.candidateId ?: "",
                                                        name = it.candidateName ?: "",
                                                        rollNumber = it.rollNo?.toString() ?: "",
                                                        contactNumber = it.mobileNo ?: ""
                                                    )
                                                },
                                                onVerifyCandidateClick = {
                                                    selectedCandidate = it
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            /* ---------------- STEP 3 ---------------- */
                            3 -> {
                                item {
                                    Text("Final Review Screen")
                                }
                            }
                        }
                    }
                }
            }
        }

        /* ---------------- BOTTOM SHEET ---------------- */
        if (currentStep == 2 && selectedCandidate != null) {
            ProCandidateBottomSheet(
                candidateData = selectedCandidate!!,
                onDismiss = { selectedCandidate = null },
                onSubmit = { selectedCandidate = null }
            )
        }
    }
}