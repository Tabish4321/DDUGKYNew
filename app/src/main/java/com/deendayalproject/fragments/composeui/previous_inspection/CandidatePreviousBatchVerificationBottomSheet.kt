package com.deendayalproject.fragments.composeui.previous_inspection

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.deendayalproject.model.uistate.CandidateVerificationUiState
import com.deendayalproject.model.updateAnswer
import com.deendayalproject.model.updateRemarks
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.CandidateVerificationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProCandidateBottomSheet(
    condidateVerificationViewModel: CandidateVerificationViewModel,
    batchId: Int?,
    candidateData: CandidateListInspectionRes,
    onDismiss: () -> Unit,
    onSubmit: (CandidateVerificationUiState) -> Unit
) {
    val context = LocalContext.current
    val state by condidateVerificationViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    //isError = state.error && state.getAnswer(question.id) == null
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        condidateVerificationViewModel.loadCandidateDetails(
            batchId = batchId!!,
            inspectionId =  AppUtil.getSavedInspectionIdPreference(context).toInt(),
            candidateId = candidateData.candidateId
        )
    }


    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            snackbarHostState.showSnackbar("Verification Saved Successfully")
            condidateVerificationViewModel.clearSaveState()
            onDismiss()
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            condidateVerificationViewModel.clearError()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize().imePadding(),
            containerColor = Color.White,
            contentWindowInsets = WindowInsets(0)
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {

                CandidateHeader(
                    candidateData = candidateData,
                    onCloseClick = onDismiss
                )

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()

                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {

                        items(items = questionList)
                        { question ->

                            val parentValue = question.dependsOn?.let {
                                state.getAnswer(it)
                            }

                            val shouldShow = when {
                                question.dependsOn == null -> true
                                parentValue?.equals("Yes", true) == true -> true
                                else -> false
                            }

                            if (shouldShow) {

                                ComplianceQuestionWithRemarks(
                                    question = question.title,
                                    answer = state.getAnswer(question.id),
                                    remarks = state.getRemarks(question.id),
                                    isError = state.showValidation && state.getAnswer(question.id) == null,
                                    onAnswerChange = { value ->
                                        condidateVerificationViewModel.updateState {
                                            updateAnswer(question.id, value)
                                        }
//
                                    },
                                    onRemarksChange = { value ->
                                        condidateVerificationViewModel.updateState {
                                            updateRemarks(question.id, value)
                                        }
//
                                    }
                                )
                            }
                        }

                        // Salary Field
                        if (state.joinedJob?.equals("Yes", true) == true) {
                            item {
                                NumericTextField(
                                    value = state.salary,
                                    onValueChange = { value ->
                                        condidateVerificationViewModel.updateState {
                                            copy(salary = value)
                                        }
                                    },
                                    label = "If Yes, What is the Salary",
                                    isRequired = true,
                                    isError = state.showValidation && state.salary.isBlank(),
                                    placeholder = "Enter salary amount"
                                )
                            }
                        }

                        // Entitlement Details
                        if (state.ojtEntitlementReceived?.equals("Yes", true) == true) {
                            item {
                                MultiLineEditText(
                                    value = state.ojtEntitlementDetails,
                                    onValueChange = { value ->
                                        condidateVerificationViewModel.updateState {
                                            copy(ojtEntitlementDetails = value)
                                        }
                                    },
                                    label = "If Yes, Details",
                                    isRequired = true,
                                    isError = state.showValidation && state.ojtEntitlementDetails.isBlank()
                                )
                            }
                        }

                        // Replacement Action
                        item {
                            MultiLineEditText(
                                value = state.replacementAction,
                                onValueChange = { value ->
                                    condidateVerificationViewModel.updateState {
                                        copy(replacementAction = value)
                                    }
                                },
                                label = "Action taken by the PIA for replacement",
                                isRequired = true,
                                isError = state.showValidation && state.replacementAction.isBlank()
                            )
                        }
                    


                        item {
                            Button(
                                onClick = {
                                    condidateVerificationViewModel.saveVerification(
                                        batchId = batchId!!,
                                        inspectionId = AppUtil.getSavedInspectionIdPreference(context).toInt(),
                                        candidateId = candidateData.candidateId
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Submit Verification")
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }
            }
        }
    }
}
