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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProCandidateBottomSheet(
    condidateVerificationViewModel: CandidateVerificationViewModel,
    batchId: Int?,
    candidateData: CandidateListInspectionRes,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val state by condidateVerificationViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            // Prevent sheet from hiding by swipe
            newValue != SheetValue.Hidden
        }
    )

    LaunchedEffect(Unit) {
        sheetState.expand()
    }

    LaunchedEffect(Unit) {
        condidateVerificationViewModel.loadCandidateDetails(
            batchId = batchId!!,
            inspectionId = AppUtil.getSavedInspectionIdPreference(context).toInt(),
            candidateId = candidateData.candidateId

        )
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            condidateVerificationViewModel.clearSaveState()
            onDismiss()
            snackbarHostState.showSnackbar("Verification Saved Successfully")
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            condidateVerificationViewModel.clearError()
        }
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {}
    }

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
                        modifier = Modifier.weight(1f)
                            .nestedScroll(nestedScrollConnection),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 100.dp),
                    ) {

                        items(questionList,
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

                                ComplianceQuestionWithRemarks(
                                    question = question.title,
                                    answer = state.getAnswer(question.id),
                                    remarks = state.getRemarks(question.id),
                                    isError = state.showValidation &&
                                            state.getAnswer(question.id) == null,
                                    onAnswerChange = { value ->
                                        condidateVerificationViewModel.updateState {
                                            updateAnswer(question.id, value)
                                        }
                                    },
                                    onRemarksChange = { value ->
                                        condidateVerificationViewModel.updateState {
                                            updateRemarks(question.id, value)
                                        }
                                    }
                                )
                            }
                        }

                        if (state.joinedJob?.equals("Yes", true) == true) {
                            item {
                                NumericTextField(
                                    value = state.salary,
                                    onValueChange = {
                                        condidateVerificationViewModel.updateState {
                                            copy(salary = it)
                                        }
                                    },
                                    label = "If Yes, What is the Salary",
                                    isRequired = true,
                                    isError = state.showValidation &&
                                            state.salary.isBlank(),
                                    placeholder = "Enter salary amount"
                                )
                            }
                        }

                        if (state.ojtEntitlementReceived?.equals("Yes", true) == true) {
                            item {
                                MultiLineEditText(
                                    value = state.ojtEntitlementDetails,
                                    onValueChange = {
                                        condidateVerificationViewModel.updateState {
                                            copy(ojtEntitlementDetails = it)
                                        }
                                    },
                                    label = "If Yes, Details",
                                    isRequired = true,
                                    isError = state.showValidation &&
                                            state.ojtEntitlementDetails.isBlank()
                                )
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
