package com.deendayalproject.fragments.composeui.main

import PreviousInspectionItemResponse
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.fragments.composeFragment.InspectionBasicDetailsFragmentDirections
import com.deendayalproject.fragments.composeui.batchAndCandidate.BatchListSection
import com.deendayalproject.fragments.composeui.batchAndCandidate.CandidateDataPreviousBatchCard
import com.deendayalproject.fragments.composeui.common.InspectionProgressHeader
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.fragments.composeui.common.ShimmerTrainingList
import com.deendayalproject.fragments.composeui.documentandstandardform.StandardFormComplianceScreen
import com.deendayalproject.fragments.composeui.previous_inspection.PreviousInspectionSection
import com.deendayalproject.fragments.composeui.previous_inspection.ProCandidateBottomSheet
import com.deendayalproject.fragments.composeui.trainer.TrainerBottomSheet
import com.deendayalproject.fragments.composeui.trainer.TrainerDataCard
import com.deendayalproject.fragments.composeui.trainer.TrainingQualityController
import com.deendayalproject.fragments.composeui.trainingCenListAandDetails.TrainingCenterDetails
import com.deendayalproject.model.request.TrainerListReq
import com.deendayalproject.model.response.*
import com.deendayalproject.viewmodel.PreviousAndDueViewModel
import com.deendayalproject.model.response.CandidateListInspectionRes
import com.deendayalproject.model.response.PrevBatchItem
import com.deendayalproject.model.response.TrainingInspCenterDetails
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.CandidateVerificationViewModel
import com.deendayalproject.viewmodel.DocumentMaintainViewModel
import com.deendayalproject.viewmodel.InspectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionStepModernScreen(
    findNavigator: NavController,
    documentMaintainViewModel: DocumentMaintainViewModel,
    condidateVerificationViewModel: CandidateVerificationViewModel,
    viewModel: PreviousAndDueViewModel,
    viewModelInspection: InspectionViewModel,
    prnNumber: String,
    sanctionLetter: String,
    inspectionType: String,
    trainingCenterId: String,
    batchList: List<PrevBatchItem>,
    candidateList: List<CandidateItem>,
    trainerList: List<TrainerListInspectionRes>,
    ongoingBatchList: List<PrevBatchItem>,
    ongoingCandidateList: List<CandidateItem>,
    currentStep: Int,
    onBatchSelected: (Int?) -> Unit,
    onOngoingBatchSelected: (Int?) -> Unit,
    onStepChange: (Int) -> Unit,
    isLoading: Boolean,
    trainingDetails: TrainingInspCenterDetails?,
    onEditClick: (PreviousInspectionItemResponse) -> Unit
) {

    val backDispatcher = LocalOnBackPressedDispatcherOwner.current
    val context = LocalContext.current

    var ongoingSelectedBatch by remember { mutableStateOf<PrevBatchItem?>(null) }
    var ongoingSelectedCandidate by remember { mutableStateOf<CandidateListInspectionRes?>(null) }

    var showTrainingForm by remember { mutableStateOf(false) }

    var selectedTrainer by remember { mutableStateOf<TrainerData?>(null) }
    var showTrainerSheet by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }


    var selectedCandidate by remember {
        mutableStateOf<CandidateListInspectionRes?>(null)
    }

    var previousSelectedBatch by remember {
        mutableStateOf<PrevBatchItem?>(null)
    }

    val toolbarTitle = when (currentStep) {
        1 -> "Training Center Details"
        2 -> if (previousSelectedBatch != null) "Previous Candidate List" else "Previous Batch List"
        3 -> if (selectedCandidate != null) "Ongoing Candidate List" else "Ongoing Batch List"
        4 -> "Trainers Attendance Verify"
        5 -> "Training Quality"
        6 -> "Documents maintained at the center"
        7 -> "Final Inspection"
        else -> "Inspection"
    }


    val trainerResponse by viewModelInspection
        .getTrainersListInspection
        .collectAsState()

    val trainerList = trainerResponse?.wrappedList ?: emptyList()

    LaunchedEffect(currentStep) {
        previousSelectedBatch = null
        selectedCandidate = null
        ongoingSelectedBatch = null
        ongoingSelectedCandidate = null
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(snackbarHostState,
                        modifier = Modifier
                        .align(Alignment.TopCenter)
                    .padding(top = 12.dp)

        )
            },
            topBar = {
                PremiumTopBar(
                    toolbarTitle,
                    onBackClick = {

                        when (currentStep) {

                            2 -> {
                                if (previousSelectedBatch != null) {
                                    previousSelectedBatch = null
                                } else {
                                    onStepChange(currentStep - 1)
                                }
                            }

                            3 -> {
                                if (ongoingSelectedBatch != null) {
                                    ongoingSelectedBatch = null
                                    ongoingSelectedCandidate = null
                                } else {
                                    onStepChange(currentStep - 1)
                                }
                            }

                            5 -> {

                                if (showTrainingForm) {

                                    showTrainingForm = false

                                } else {

                                    onStepChange(currentStep - 1)

                                }

                            }

                            6 -> {
                                onStepChange(currentStep - 1)
                            }

                            7 -> {
                                onStepChange(currentStep - 1)
                            }

                            else -> {
                                if (currentStep > 1) {
                                    onStepChange(currentStep - 1)
                                } else {
                                    backDispatcher?.onBackPressedDispatcher?.onBackPressed()
                                }
                            }
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
                                    if (currentStep > 1) {
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
                                if (currentStep < 7) {   // 👈 updated max step
                                    onStepChange(currentStep + 1)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(if (currentStep < 7) "Next" else "Submit")
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
                        verticalArrangement = Arrangement.spacedBy(16.dp),

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

                                if (previousSelectedBatch == null) {

                                    item {
                                        if (batchList.isEmpty()) {
                                            Text("No batch available")
                                        } else {
                                            BatchListSection(
                                                batchList = batchList,
                                                onBatchClick = {
                                                    previousSelectedBatch = it
                                                    onBatchSelected(it.batchId)   //  CALL API
                                                }
                                            )
                                        }
                                    }

                                }
                                else {

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

                                if (ongoingSelectedBatch == null) {

                                    item {

                                        if (ongoingBatchList.isEmpty()) {
                                            Text("No ongoing batch available")
                                        } else {
                                            BatchListSection(
                                                batchList = ongoingBatchList,
                                                onBatchClick = {
                                                    ongoingSelectedBatch = it
                                                    onOngoingBatchSelected(it.batchId)
                                                }
                                            )
                                        }
                                    }

                                } else {

                                    item {

                                        if (ongoingCandidateList.isEmpty()) {
                                            Text("No ongoing candidates available")
                                        } else {

                                            CandidateDataPreviousBatchCard(
                                                candidate = ongoingCandidateList.map {
                                                    CandidateListInspectionRes(
                                                        candidateId = it.candidateId ?: "",
                                                        name = it.candidateName ?: "",
                                                        rollNumber = it.rollNo?.toString() ?: "",
                                                        contactNumber = it.mobileNo ?: "",
                                                        status = it.status
                                                    )
                                                },
                                                onVerifyCandidateClick = {
                                                    ongoingSelectedCandidate = it
                                                }
                                            )
                                        }
                                    }



                                }
                            }

                            4 -> {

                                item {

                                    TrainerDataCard(
                                        trainer = trainerList,
                                        onVerifyTrainerClick = { trainer ->
                                            selectedTrainer = trainer
                                            showTrainerSheet = true
                                        }
                                    )
                                }
                            }

                            5 -> {

                                item {

                                    TrainingQualityController(
                                        viewModel = viewModelInspection,
                                        snackbarHostState = snackbarHostState,
                                        showForm = showTrainingForm,
                                        onShowFormChange = { showTrainingForm = it }
                                    )

                                }

                            }

                            6 -> {

                                item {

                                    var showScreen by remember { mutableStateOf(false) }

                                    LaunchedEffect(Unit) {
                                        kotlinx.coroutines.delay(200)
                                        showScreen = true
                                    }

                                    if (!showScreen) {

                                        ShimmerTrainingList()

                                    } else {

                                        StandardFormComplianceScreen(
                                            documentMaintainViewModel,
                                            snackbarHostState = snackbarHostState,
                                            )

                                    }
                                }

                            }

                            7 -> {

                                item {

                                    Text(
                                        text = "Final Inspection Summary",
                                        style = MaterialTheme.typography.titleLarge
                                    )

                                }

                            }
                        }
                }
            }
        }

        /* ---------------- BOTTOM SHEET ---------------- */

        if (currentStep == 2 && selectedCandidate != null) {
            ProCandidateBottomSheet(
                condidateVerificationViewModel,
                previousSelectedBatch!!.batchId,
                candidateData = selectedCandidate!!,
                onDismiss = { selectedCandidate = null },
               // onSubmit = { selectedCandidate = null }
            )
        }




        if (currentStep == 3 && ongoingSelectedCandidate != null) {

            findNavigator.navigate(
                InspectionBasicDetailsFragmentDirections
                    .actionInspectionBasicDetailsFragmentToOngoingCandidateFragment(
                        ongoingSelectedCandidate!!.candidateId,
                        batchId = ongoingSelectedBatch!!.batchId.toString(),
                        ongoingSelectedCandidate!!.name,
                        ongoingSelectedCandidate!!.contactNumber,
                        ongoingSelectedCandidate!!.rollNumber
                    )
            )
            ongoingSelectedCandidate = null


        }

        if (currentStep == 4 && showTrainerSheet) {

            TrainerBottomSheet(
                viewModel=viewModelInspection,
                trainerName = selectedTrainer?.trainerName ?: "NA",
                trainerId = selectedTrainer?.trainerId ?: "NA",
                trainerCode =selectedTrainer!!.trainerCode,
                onDismiss = {
                    showTrainerSheet = false
                }
            )
        }

            LaunchedEffect(currentStep) {
                if (currentStep == 4) {
                    viewModelInspection.getTrainersListInspection(
                        TrainerListReq(
                            appVersion = BuildConfig.VERSION_NAME,
                            trainingCenterId = trainingCenterId.toInt()
                        )
                    )
                }
            }
    }
}

}