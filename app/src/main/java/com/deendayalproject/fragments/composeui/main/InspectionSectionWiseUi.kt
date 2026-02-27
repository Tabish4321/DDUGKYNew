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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ctc.wstx.shaded.msv_core.writer.relaxng.Context
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
import com.deendayalproject.viewmodel.PreviousAndDueViewModel
import com.deendayalproject.model.response.CandidateListInspectionRes
import com.deendayalproject.model.response.PrevBatchItem
import com.deendayalproject.model.response.TrainingInspCenterDetails
import com.deendayalproject.util.AppUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionModernScreen(
    viewModel: PreviousAndDueViewModel,
    prnNumber: String,

    sanctionLetter: String,
    inspectionType: String,
    trainingCenterId: String,
    batchList: List<PrevBatchItem>,
    candidateList: List<CandidateItem>,
    currentStep: Int,
    onBatchSelected: (Int?) -> Unit,
    onStepChange: (Int) -> Unit,
    isLoading: Boolean,
    trainingDetails: TrainingInspCenterDetails?,
    onEditClick: (PreviousInspectionItemResponse) -> Unit
) {

    val backDispatcher = LocalOnBackPressedDispatcherOwner.current
    val context = LocalContext.current
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
                    color = colorResource(R.color.white),
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .imePadding()
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
                                modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
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

                                    Spacer(modifier = Modifier.height(20.dp))

                                    PreviousInspectionSection(
                                        viewModel,
                                        trainingCenterId= AppUtil.getSavedTrainingCenterIdPreference(context),
                                        sanctionOrder = sanctionLetter,
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
                                                        contactNumber = it.mobileNo ?: "",
                                                        status = it.status

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