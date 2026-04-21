package com.deendayalproject.fragments.composeui.main

import PreviousInspectionItemResponse
import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.fragments.composeFragment.InspectionBasicDetailsFragmentDirections
import com.deendayalproject.fragments.composeui.LocationHandle.LocationMismatchDialog
import com.deendayalproject.fragments.composeui.LocationHandle.rememberGeofenceChecker
import com.deendayalproject.fragments.composeui.PreviousObservationScreen
import com.deendayalproject.fragments.composeui.batchAndCandidate.BatchListSection
import com.deendayalproject.fragments.composeui.batchAndCandidate.CandidateDataPreviousBatchCard
import com.deendayalproject.fragments.composeui.common.EmptyStateView
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
import kotlinx.coroutines.launch

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
    var trainerApiCalled by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


    val openDueDiligenceEdit by viewModel.openDueDiligenceEdit.collectAsState(initial = null)
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

    LaunchedEffect(openDueDiligenceEdit) {
        openDueDiligenceEdit?.let {

            findNavigator.navigate(
                R.id.action_inspectionBasicDetailsFragment_to_dueDiligenceEditFragment
            )
        }
    }

    val verificationState by condidateVerificationViewModel.state.collectAsState()
    val finalRemark = condidateVerificationViewModel.finalRemark

    LaunchedEffect(verificationState.submitSuccess) {
        if (verificationState.submitSuccess) {
            Toast.makeText(context, "Inspection submitted successfully",
                Toast.LENGTH_SHORT).show()
            // snackbarHostState.showSnackbar("Inspection submitted successfully")
            findNavigator.previousBackStackEntry
                ?.savedStateHandle
                ?.set("inspection_success", true)
            condidateVerificationViewModel.clearFinalSuccess()
            findNavigator.popBackStack()
        }
    }


    LaunchedEffect(verificationState.error) {
        verificationState.error?.let {
            snackbarHostState.showSnackbar(it)
            condidateVerificationViewModel.clearFinalError()
        }
    }

    var isEligible by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var distance by remember { mutableStateOf<Float?>(null) }

    // "28.6296845 &  77.2189032" jeevan bharti
    trainingDetails?.coordinate?.let { coord ->
        rememberGeofenceChecker(apiCoordinate = coord) { inside, dist ->
            if(BuildConfig.DEBUG){
                isEligible = true
                showDialog = false
                return@rememberGeofenceChecker
            }
            isEligible = inside
            distance = dist

            if (!inside) {
               // showDialog = false
                showDialog = true
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {

        if (showDialog && !isEligible) {
            LocationMismatchDialog(
                distance = distance,
                onOkClick = {
                    showDialog = false
                    backDispatcher?.onBackPressedDispatcher?.onBackPressed()
                }
            )
        }

        if (isEligible) {
            if (showDialog) {
                LocationMismatchDialog(
                    distance = distance,
                    onOkClick = {
                        showDialog = false
                        backDispatcher?.onBackPressedDispatcher?.onBackPressed()
                    }
                )
            }
        }
        if (isEligible) {
            Scaffold(
                contentWindowInsets = WindowInsets(0),
                snackbarHost = {
                    SnackbarHost(
                        snackbarHostState,
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
                            .navigationBarsPadding()
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
                                    if (currentStep == 7) {
                                        if (finalRemark.isBlank()) {
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Please enter final remark")
                                            }
                                        } else {

                                            condidateVerificationViewModel.submitFinal(
                                                AppUtil.getSavedInspectionIdPreference(context)
                                                    .toInt(),
                                                AppUtil.getSavedTrainingCenterIdPreference(context)
                                                    .toInt(),
                                                finalRemark
                                            )

                                        }
                                    } else if (currentStep < 7) {   // 👈 updated max step
                                        onStepChange(currentStep + 1)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(if (currentStep == 7) "Submit" else "Next")
                            }
                        }
                    }
                }
            )
            { padding ->

                if (isLoading) {
                    Box(modifier = Modifier.padding(padding)) {
                        ShimmerTrainingList()
                    }
                } else {

                    if (currentStep == 6) {

                        Column(
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize()
                        ) {

                            InspectionProgressHeader(
                                currentStep,
                                onStepClick = { step ->
                                    onStepChange(step)
                                }
                            )

                            StandardFormComplianceScreen(
                                viewModel = documentMaintainViewModel,
                                snackbarHostState = snackbarHostState
                            )
                        }
                    } else if (currentStep == 7) {
                        Column(
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize()
                        ) {
                            PreviousObservationScreen(
                                viewModel = condidateVerificationViewModel
                            )
                        }

                    } else {
                        Column(
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize()
                                .background(colorResource(id = R.color.white))
                        )
                        {

                            InspectionProgressHeader(
                                currentStep,
                                onStepClick = { step ->
                                    onStepChange(step)
                                })

                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 12.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                            )
                            {


                                when (currentStep) {

                                    /* ---------------- STEP 1 ---------------- */
                                    1 -> {
                                        item {



                                            TrainingCenterDetails(
                                                prnNumber = prnNumber,
                                                sanctionLetter = sanctionLetter,
                                                inspectionType = inspectionType,
                                                trainingCenterId = trainingDetails?.trainingCenterId
                                                    ?: "",
                                                trainingCenterName = trainingDetails?.trainingCenterName
                                                    ?: "",
                                                inchargeName = trainingDetails?.inchargeName ?: "",
                                                mobileNumber = trainingDetails?.mobileNumber ?: "",
                                                email = trainingDetails?.emailId ?: "",
                                                tradeAndCapacity = trainingDetails?.tradeAndCapacity
                                                    ?: "",
                                                coordinate = trainingDetails?.coordinate ?: "",
                                                roleName = trainingDetails?.roleName ?: "",
                                                revisedDoc = trainingDetails?.revisedSanctionOrderDoc
                                                    ?: "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MS9UeXBlL1BhZ2VzL0tpZHNbMTcgMCBSXT4+DWVuZG9iag01IDAgb2JqDTw8L1N1YnR5cGUvWE1ML0xlbmd0aCA0MDMwL1R5cGUvTWV0YWRhdGE+PnN0cmVhbQ0KPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4KPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNC4wLWMzMTYgNDQuMjUzOTIxLCBTdW4gT2N0IDAxIDIwMDYgMTc6MTQ6MzkiPgogICA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgogICAgICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgICAgICAgICB4bWxuczpwZGY9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGRmLzEuMy8iPgogICAgICAgICA8cGRmOlByb2R1Y2VyPkFjcm9iYXQgRGlzdGlsbGVyIDguMS4wIChXaW5kb3dzKTwvcGRmOlByb2R1Y2VyPgogICAgICA8L3JkZjpEZXNjcmlwdGlvbj4KICAgICAgPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIKICAgICAgICAgICAgeG1sbnM6cGRmeD0iaHR0cDovL25zLmFkb2JlLmNvbS9wZGZ4LzEuMy8iPgogICAgICAgICA8cGRmeDpDb21wYW55PlNETCBJbnRlcm5hdGlvbmFsPC9wZGZ4OkNvbXBhbnk+CiAgICAgICAgIDxwZGZ4OlNvdXJjZU1vZGlmaWVkPkQ6MjAwOTA4MjUxMzM0NDM8L3BkZng6U291cmNlTW9kaWZpZWQ+CiAgICAgIDwvcmRmOkRlc2NyaXB0aW9uPgogICAgICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgICAgICAgICB4bWxuczp4YXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iPgogICAgICAgICA8eGFwOkNyZWF0ZURhdGU+MjAwOS0wOC0yNVQxNDozNToxOCswMTowMDwveGFwOkNyZWF0ZURhdGU+CiAgICAgICAgIDx4YXA6Q3JlYXRvclRvb2w+QWNyb2JhdCBQREZNYWtlciA4LjEgZm9yIFdvcmQ8L3hhcDpDcmVhdG9yVG9vbD4KICAgICAgICAgPHhhcDpNb2RpZnlEYXRlPjIwMDktMDgtMjVUMTQ6MzU6MjErMDE6MDA8L3hhcDpNb2RpZnlEYXRlPgogICAgICAgICA8eGFwOk1ldGFkYXRhRGF0ZT4yMDA5LTA4LTI1VDE0OjM1OjIxKzAxOjAwPC94YXA6TWV0YWRhdGFEYXRlPgogICAgICA8L3JkZjpEZXNjcmlwdGlvbj4KICAgICAgPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIKICAgICAgICAgICAgeG1sbnM6eGFwTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iPgogICAgICAgICA8eGFwTU06RG9jdW1lbnRJRD51dWlkOjc5MDgzN2QxLWRjODItNGU1OC1hODk2LTdlMWI2NTllYTdkNjwveGFwTU06RG9jdW1lbnRJRD4KICAgICAgICAgPHhhcE1NOkluc3RhbmNlSUQ+dXVpZDo4NzRkODQyYS03M2IzLTQ4YWQtYWY1ZS00Y2FhNDhiYThmOTA8L3hhcE1NOkluc3RhbmNlSUQ+CiAgICAgICAgIDx4YXBNTTpzdWJqZWN0PgogICAgICAgICAgICA8cmRmOlNlcT4KICAgICAgICAgICAgICAgPHJkZjpsaT4xPC9yZGY6bGk+CiAgICAgICAgICAgIDwvcmRmOlNlcT4KICAgICAgICAgPC94YXBNTTpzdWJqZWN0PgogICAgICA8L3JkZjpEZXNjcmlwdGlvbj4KICAgICAgPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIKICAgICAgICAgICAgeG1sbnM6ZGM9Imh0dHA6Ly9wdXJsLm9yZy9kYy9lbGVtZW50cy8xLjEvIj4KICAgICAgICAgPGRjOmZvcm1hdD5hcHBsaWNhdGlvbi9wZGY8L2RjOmZvcm1hdD4KICAgICAgICAgPGRjOmNyZWF0b3I+CiAgICAgICAgICAgIDxyZGY6U2VxPgogICAgICAgICAgICAgICA8cmRmOmxpPk5ld1VzZXI8L3JkZjpsaT4KICAgICAgICAgICAgPC9yZGY6U2VxPgogICAgICAgICA8L2RjOmNyZWF0b3I+CiAgICAgICAgIDxkYzp0aXRsZT4KICAgICAgICAgICAgPHJkZjpBbHQ+CiAgICAgICAgICAgICAgIDxyZGY6bGkgeG1sOmxhbmc9IngtZGVmYXVsdCI+RHVtbXkgUERGIGRvd25sb2FkPC9yZGY6bGk+CiAgICAgICAgICAgIDwvcmRmOkFsdD4KICAgICAgICAgPC9kYzp0aXRsZT4KICAgICAgPC9yZGY6RGVzY3JpcHRpb24+CiAgIDwvcmRmOlJERj4KPC94OnhtcG1ldGE+CiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAKPD94cGFja2V0IGVuZD0idyI/Pg0KZW5kc3RyZWFtDWVuZG9iag02IDAgb2JqDTw8L0NyZWF0aW9uRGF0ZShEOjIwMDkwODI1MTQzNTE4KzAxJzAwJykvQXV0aG9yKE5ld1VzZXIpL0NyZWF0b3IoQWNyb2JhdCBQREZNYWtlciA4LjEgZm9yIFdvcmQpL1Byb2R1Y2VyKEFjcm9iYXQgRGlzdGlsbGVyIDguMS4wIFwoV2luZG93c1wpKS9Nb2REYXRlKEQ6MjAwOTA4MjUxNDM1MjErMDEnMDAnKS9Db21wYW55KFNETCBJbnRlcm5hdGlvbmFsKS9Tb3VyY2VNb2RpZmllZChEOjIwMDkwODI1MTMzNDQzKS9UaXRsZShEdW1teSBQREYgZG93bmxvYWQpPj4NZW5kb2JqDXhyZWYNCjAgMTUNCjAwMDAwMDAwMDAgNjU1MzUgZg0KMDAwMDAwMjE2MSAwMDAwMCBuDQowMDAwMDAyNjI1IDAwMDAwIG4NCjAwMDAwMDI2NTkgMDAwMDAgbg0KMDAwMDAwMjY4MyAwMDAwMCBuDQowMDAwMDAyNzM1IDAwMDAwIG4NCjAwMDAwMDY4NDIgMDAwMDAgbg0KMDAwMDAwMDAwMCA2NTUzNSBmDQowMDAwMDAwMDAwIDY1NTM1IGYNCjAwMDAwMDAwMDAgNjU1MzUgZg0KMDAwMDAwMDAwMCA2NTUzNSBmDQowMDAwMDAwMDAwIDY1NTM1IGYNCjAwMDAwMDAwMDAgNjU1MzUgZg0KMDAwMDAwMDAwMCA2NTUzNSBmDQowMDAwMDAwMDAwIDY1NTM1IGYNCnRyYWlsZXINCjw8L1NpemUgMTU+Pg0Kc3RhcnR4cmVmDQoxMTYNCiUlRU9GDQo=",
                                                context = context
                                            )

                                            Spacer(modifier = Modifier.height(20.dp))

                                            PreviousInspectionSection(
                                                viewModel,
                                                trainingCenterId = AppUtil.getSavedTrainingCenterIdPreference(
                                                    context
                                                ),
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
                                                    //     Text("No batch available")
                                                    EmptyStateView(
                                                        "No Batch Data available",
                                                        modifier = Modifier.fillMaxSize()
                                                    )

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

                                        } else {

                                            item {

                                                if (candidateList.isEmpty()) {
                                                    //Text("No candidates available")
                                                    EmptyStateView("No candidates available")
                                                } else {

                                                    CandidateDataPreviousBatchCard(
                                                        candidate = candidateList.map {
                                                            CandidateListInspectionRes(
                                                                candidateId = it.candidateId ?: "",
                                                                name = it.candidateName ?: "",
                                                                rollNumber = it.rollNo?.toString()
                                                                    ?: "",
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
//                                                Text("No ongoing batch available")
                                                    EmptyStateView("No ongoing batch available")

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
                                                    //Text("No ongoing candidates available")
                                                    EmptyStateView("No ongoing candidates available")

                                                } else {
                                                    CandidateDataPreviousBatchCard(
                                                        candidate = ongoingCandidateList.map {
                                                            CandidateListInspectionRes(
                                                                candidateId = it.candidateId ?: "",
                                                                name = it.candidateName ?: "",
                                                                rollNumber = it.rollNo?.toString()
                                                                    ?: "",
                                                                contactNumber = it.mobileNo ?: "",
                                                                status = it.status,
                                                                candidateProfilePic = it.candidateProfilePic
                                                                    ?: ""
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
                                                trainerList = trainerList,
                                                viewModel = viewModelInspection,
                                                snackbarHostState = snackbarHostState,
                                                showForm = showTrainingForm,
                                                onShowFormChange = { showTrainingForm = it }
                                            )

                                        }

                                    }

                                    /* 6 -> {

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

                            }*/

//                                7 -> {
//
//                                    item {
//
//                                        Text(
//                                            text = "Final Inspection Summary",
//                                            style = MaterialTheme.typography.titleLarge
//                                        )
//
//                                    }
//
//                                }
                                }
                            }
                        }
                    }

                    /* ---------------- BOTTOM SHEET ---------------- */

                    if (currentStep == 2 && selectedCandidate != null) {
                        previousSelectedBatch?.let { batch ->
                            ProCandidateBottomSheet(
                                condidateVerificationViewModel,
                                batch.batchId,
                                snackbarHostState = snackbarHostState,
                                candidateData = selectedCandidate!!,
                                onDismiss = { selectedCandidate = null }
                            )
                        }
                    }




                    if (currentStep == 3 && ongoingSelectedCandidate != null) {
                        LaunchedEffect(ongoingSelectedCandidate) {
                            ongoingSelectedCandidate?.let { candidate ->

                                findNavigator.navigate(
                                    InspectionBasicDetailsFragmentDirections
                                        .actionInspectionBasicDetailsFragmentToOngoingCandidateFragment(
                                            candidate.candidateId,
                                            batchId = ongoingSelectedBatch?.batchId.toString(),
                                            candidate.name,
                                            candidate.contactNumber,
                                            candidate.rollNumber
                                        )
                                )

                                ongoingSelectedCandidate = null
                            }
                        }
                    }

                    if (currentStep == 4 && showTrainerSheet) {

                        TrainerBottomSheet(
                            snackbarHostState = snackbarHostState,
                            viewModel = viewModelInspection,
                            trainerName = selectedTrainer?.trainerName ?: "NA",
                            trainerId = selectedTrainer?.trainerId ?: "NA",
                            trainerCode = selectedTrainer!!.trainerCode,
                            onDismiss = {
                                showTrainerSheet = false
                            }
                        )
                    }

                    LaunchedEffect(currentStep) {

                        if (currentStep == 4 && !trainerApiCalled) {

                            trainerApiCalled = true

                            viewModelInspection.getTrainersListInspection(
                                TrainerListReq(
                                    appVersion = BuildConfig.VERSION_NAME,
                                    trainingCenterId = trainingCenterId.toIntOrNull() ?: 0
                                )
                            )
                        }

                        if (currentStep != 4) {
                            trainerApiCalled = false
                        }
                    }
                }
            }
        }
    }
}






