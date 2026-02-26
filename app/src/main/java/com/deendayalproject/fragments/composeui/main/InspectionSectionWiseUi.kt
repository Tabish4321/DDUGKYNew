package com.deendayalproject.fragments.composeui.main

import PreviousInspectionItemResponse
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.deendayalproject.model.response.CandidateListInspectionRes
import com.deendayalproject.model.response.TrainingInspCenterDetails
import com.deendayalproject.model.response.batchListRes

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionModernScreen(
    prnNumber: String,
    sanctionLetter: String,
    inspectionType: String,
    trainingCenterId: String,
    isLoading: Boolean,
    trainingDetails: TrainingInspCenterDetails?,
    onEditClick: (PreviousInspectionItemResponse) -> Unit
) {

    var currentStep by remember { mutableStateOf(1) }
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current

    var selectedCandidate by remember {
        mutableStateOf<CandidateListInspectionRes?>(null)
    }

    var selectedBatch by remember { mutableStateOf<batchListRes?>(null) }

    val toolbarTitle = when (currentStep) {
        1 -> "Training Center Details"
        2 -> if (selectedBatch != null) "Candidate List"
        else "Previous Batch List"
        3 -> "Final Review"
        else -> "Inspection"
    }

    // 🔹 Dummy batch list (same as before)
    val sampleBatchList = listOf(
        batchListRes(1, "BATCH-001"),
        batchListRes(2, "BATCH-002"),
        batchListRes(3, "BATCH-003")
    )

    // 🔹 Dummy candidate list (same as before)
    val sampleCandidateList = listOf(
        CandidateListInspectionRes(1, "Rahul Kumar", "RN001", "9876543210"),
        CandidateListInspectionRes(2, "Amit Sharma", "RN002", "9123456789"),
        CandidateListInspectionRes(3, "Priya Singh", "RN003", "9988776655"),
        CandidateListInspectionRes(4, "Naman Singh", "RN004", "9988776655"),
        CandidateListInspectionRes(5, "Pankaj", "RN005", "9988776655")
    )

    // 🔹 Dummy previous inspection list
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
                    if (currentStep > 1) {
                        if (currentStep == 2 && selectedBatch != null) {
                            selectedBatch = null
                        } else {
                            currentStep--
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

            ProCandidateBottomSheet(
                candidateData = selectedCandidate!!,
                onDismiss = { selectedCandidate = null },
                onSubmit = { selectedCandidate = null }
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
                    .fillMaxSize()
                    .background(colorResource(id = R.color.white))
            ) {

                // 🔹 Header
                InspectionProgressHeader(currentStep)

                // 🔹 Scrollable content
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    when (currentStep) {

                        // 🔥 STEP 1

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
                                    onEditClick = { selectedItem ->
                                        onEditClick(selectedItem)
                                    }
                                )
                            }
                        }

                        // 🔥 STEP 2
                        2 -> {

                            if (selectedBatch == null) {
                                item {
                                    BatchListSection(
                                        batchList = sampleBatchList,
                                        onBatchClick = { batch ->
                                            selectedBatch = batch
                                        }
                                    )
                                }
                            } else {
                                item {
                                    CandidateDataPreviousBatchCard(
                                        candidate = sampleCandidateList,
                                        onVerifyCandidateClick = {
                                            selectedCandidate = it
                                        }
                                    )
                                }
                            }

                            item {}
                        }

                        // 🔥 STEP 3
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
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionModernScreen(
    prnNumber: String,
    sanctionLetter: String,
    inspectionType: String,
    trainingCenterId: String,
    isLoading: Boolean,
    trainingDetails: TrainingInspCenterDetails?,
    onEditClick: (PreviousInspectionItemResponse) -> Unit
) {

    var currentStep by remember { mutableStateOf(1) }
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current

    var selectedCandidate by remember { mutableStateOf<CandidateListInspectionRes?>(null) }
    var selectedBatch by remember { mutableStateOf<batchListRes?>(null) }

    val toolbarTitle = when (currentStep) {
        1 -> "Training Center Details"
        2 -> if (selectedBatch != null) "Candidate List" else "Previous Batch List"
        3 -> "Final Review"
        else -> "Inspection"
    }

    val sampleBatchList = listOf(
        batchListRes(1, "BATCH-001"),
        batchListRes(2, "BATCH-002"),
        batchListRes(3, "BATCH-003")
    )

    val sampleCandidateList = listOf(
        CandidateListInspectionRes(1, "Rahul Kumar", "RN001", "9876543210"),
        CandidateListInspectionRes(2, "Amit Sharma", "RN002", "9123456789"),
        CandidateListInspectionRes(3, "Priya Singh", "RN003", "9988776655"),
        CandidateListInspectionRes(4, "Naman Singh", "RN004", "9988776655"),
        CandidateListInspectionRes(5, "Pankaj", "RN005", "9988776655")
    )

    val sampleInspectionList = listOf(
        PreviousInspectionItemResponse(1, "12 Jan 2026", "Rahul Sharma", "Inspection"),
        PreviousInspectionItemResponse(2, "05 Feb 2026", "Amit Verma", "Due Diligence")
    )

    // 🔥 ROOT BOX (IMPORTANT FOR OVERLAY)
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
                                currentStep--
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

                            2 -> {

                                if (selectedBatch == null) {
                                    item {
                                        BatchListSection(
                                            batchList = sampleBatchList,
                                            onBatchClick = { selectedBatch = it }
                                        )
                                    }
                                } else {
                                    item {
                                        CandidateDataPreviousBatchCard(
                                            candidate = sampleCandidateList,
                                            onVerifyCandidateClick = {
                                                selectedCandidate = it   // 🔥 THIS TRIGGERS SHEET
                                            }
                                        )
                                    }
                                }

                                item {}
                            }

                            3 -> {
                                item { Text("Final Review Screen") }
                            }
                        }
                    }
                }
            }
        }

        //  SHEET MUST BE OUTSIDE SCAFFOLD
        if (currentStep == 2 && selectedCandidate != null) {
            ProCandidateBottomSheet(
                candidateData = selectedCandidate!!,
                onDismiss = { selectedCandidate = null },
                onSubmit = { selectedCandidate = null }
            )
        }
    }
}
