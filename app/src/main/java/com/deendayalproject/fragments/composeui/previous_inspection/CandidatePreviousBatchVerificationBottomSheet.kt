package com.deendayalproject.fragments.composeui.previous_inspection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.CandidateHeader
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.fragments.composeui.common.MultiLineEditText
import com.deendayalproject.fragments.composeui.common.NumericTextField
import com.deendayalproject.model.getAnswer
import com.deendayalproject.model.getRemarks
import com.deendayalproject.model.questionList
import com.deendayalproject.model.response.CandidateListInspectionRes
import com.deendayalproject.model.updateAnswer
import com.deendayalproject.model.updateRemarks
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.CandidateVerificationViewModel
import kotlinx.coroutines.launch

fun isRemarksRequired(answer: String?, remarks: String): Boolean {
    return answer.equals("No", true) && remarks.isBlank()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProCandidateBottomSheet(
    condidateVerificationViewModel: CandidateVerificationViewModel,
    batchId: Int?,
    candidateData: CandidateListInspectionRes,
    snackbarHostState: SnackbarHostState,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val state by condidateVerificationViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )

    val inspectionId = remember {
        AppUtil.getSavedInspectionIdPreference(context).toInt()
    }


    LaunchedEffect(Unit) {
        sheetState.expand()
    }

    LaunchedEffect(candidateData.candidateId) {
        condidateVerificationViewModel.clearSaveState()
        condidateVerificationViewModel.loadCandidateDetails(
            batchId = batchId!!,
            inspectionId = inspectionId,
            candidateId = candidateData.candidateId

        )
    }

    LaunchedEffect(state) {

        when {

            state.saveSuccess -> {

                condidateVerificationViewModel.clearSaveState()

                onDismiss()

                snackbarHostState.showSnackbar("Verification Saved Successfully")
            }

            state.error != null -> {

                snackbarHostState.showSnackbar(state.error.toString())
                condidateVerificationViewModel.clearError()
            }

            state.showValidation -> {

                snackbarHostState.showSnackbar("Please fill all the mandatory fields")

                condidateVerificationViewModel.clearErrorValidation()
            }

        }
    }

//    val nestedScrollConnection = remember {
//        object : NestedScrollConnection {}
//    }



    ModalBottomSheet(
        onDismissRequest = {  },
        sheetState = sheetState,
//        dragHandle = {
//            Box(
//                modifier = Modifier
//                    .padding(vertical = 8.dp)
//                    .size(width = 40.dp, height = 4.dp)
//                    .background(
//                        Color(0xFFE2E8F0),
//                        RoundedCornerShape(50)
//                    )
//            )
//        },
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = Color(0xFFF8FAFC),
        tonalElevation = 6.dp,
        scrimColor = Color.Black.copy(alpha = 0.35f),
        contentWindowInsets = { WindowInsets(0) }
    ) {

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color(0xFFF8FAFC),
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentWindowInsets = WindowInsets(0),
            bottomBar = {
                Surface(
                    shadowElevation = 8.dp,
                    tonalElevation = 2.dp,
                    color = Color.White
                ) {
                    Button(
                        onClick = {


                            val state = condidateVerificationViewModel.uiState.value

                            if  (
                            /* existing validations */

                                (state.joinedJob?.equals("Yes", true) == true && state.salary.isBlank()) ||

                                (state.ojtEntitlementReceived?.equals("Yes", true) == true &&
                                        state.ojtEntitlementDetails.isBlank()) ||

                                /* status validations */

                                (state.currentStatus == "Working" && state.workingMonths.isBlank()) ||

                                (state.currentStatus == "Not Working" && state.notWorkingReason.isBlank()) ||

                                /* mandatory remarks validations */
                                isRemarksRequired(state.externalAssessment, state.externalAssessmentRemarks) ||
                                isRemarksRequired(state.passedFailed, state.passedFailedRemarks) ||
                                isRemarksRequired(state.certificateReceived, state.certificateReceivedRemarks)||
                                isRemarksRequired(state.ojtJoined, state.ojtJoinedRemarks) ||
                                isRemarksRequired(state.ojtCertificateReceived, state.ojtCertificateReceivedRemarks) ||
                                isRemarksRequired(state.ojtEntitlementReceived, state.ojtEntitlementRemarks) ||
                                isRemarksRequired(state.ojtVerificationDone, state.ojtVerificationRemarks) ||
                                isRemarksRequired(state.offerLetterReceived, state.offerLetterRemarks) ||
                                isRemarksRequired(state.performancePlanFilled, state.performancePlanRemarks) ||
                                isRemarksRequired(state.joinedJob, state.joinedJobRemarks) ||
                                isRemarksRequired(state.minimumWageMatch, state.minimumWageRemarks) ||
                                isRemarksRequired(state.currentStatus, state.currentStatusRemarks) ||
                                isRemarksRequired(state.ppsDisbursed, state.ppsDisbursedRemarks) ||
                                state.replacementAction.isBlank()
                            ) {

                                condidateVerificationViewModel.updateState {
                                    copy(showValidation = true)
                                }
                                return@Button
                            }

                            condidateVerificationViewModel.saveVerification(
                                batchId = batchId!!,
                                inspectionId = AppUtil.getSavedInspectionIdPreference(context).toInt(),
                                candidateId = candidateData.candidateId
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            "Submit Verification",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                CandidateHeader(
                    candidateData = candidateData,
                    onCloseClick = {
                        scope.launch {
                            sheetState.hide()
                            condidateVerificationViewModel.resetForm()
                            onDismiss() // Only close button works
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFE2E8F0)
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (state.isLoading) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 3.dp
                        )
                    }

                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
           //                 .nestedScroll(nestedScrollConnection),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 100.dp),
                    ) {

                        items(items=questionList,
                            key={it.id}
                        ) { question ->

                            val parentValue = question.dependsOn?.let {
                                state.getAnswer(it)
                            }

                            val shouldShow = when {
                                question.dependsOn == null -> true
                                parentValue?.equals("Yes", true) == true -> true
                                else -> false
                            }

                            if (shouldShow) {
                                CandidateDetailsQuestionN(condidateVerificationViewModel = condidateVerificationViewModel,question=question,state=state,   onAnswerChange = { value ->
                                    condidateVerificationViewModel.updateState {
                                        updateAnswer(question.id, value)
                                    }
                                },
                                    onRemarksChange = { value ->
                                        condidateVerificationViewModel.updateState {
                                            updateRemarks(question.id, value)
                                        }
                                    })
                            }
                        }
                        item {
                            MultiLineEditText(

                                value = state.replacementAction,
                                onValueChange = {
                                    condidateVerificationViewModel.updateState {
                                        copy(replacementAction = it)
                                    }
                                },
                                label = "Action taken by the PIA for replacement",
                                isRequired = true,
                                isError = state.showValidation &&
                                        state.replacementAction.isBlank()
                            )
                        }

                    }
                }
            }
        }
    }
}
