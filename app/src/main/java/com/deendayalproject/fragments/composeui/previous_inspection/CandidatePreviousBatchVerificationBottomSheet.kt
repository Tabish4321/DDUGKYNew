package com.deendayalproject.fragments.composeui.previous_inspection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.deendayalproject.fragments.composeui.common.CandidateHeader
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.fragments.composeui.common.MultiLineEditText
import com.deendayalproject.fragments.composeui.common.WorkingStatusQuestion
import com.deendayalproject.model.response.CandidateListInspectionRes
import com.deendayalproject.model.uistate.CandidateVerificationUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProCandidateBottomSheet(
    candidateData: CandidateListInspectionRes,
    onDismiss: () -> Unit,
    onSubmit: (CandidateVerificationUiState) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )

    var formState by remember { mutableStateOf(CandidateVerificationUiState()) }
    var showError by remember { mutableStateOf(false) }

    // 🔥 Snackbar Setup
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        dragHandle = null,
        containerColor = Color.White
    ) {

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            containerColor = Color.White
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {

                CandidateHeader(
                    candidateData = candidateData,
                    onCloseClick = onDismiss
                )

                Spacer(modifier = Modifier.height(10.dp))

                HorizontalDivider()

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {

                    item {
                        ComplianceQuestionWithRemarks(
                            question = "External Assessment Completed",
                            answer = formState.externalAssessment,
                            remarks = formState.externalAssessmentRemarks,
                            isError = showError && formState.externalAssessment == null,
                            onAnswerChange = {
                                formState = formState.copy(
                                    externalAssessment = it,
                                    externalAssessmentRemarks = ""
                                )
                            },
                            onRemarksChange = {
                                formState = formState.copy(
                                    externalAssessmentRemarks = it
                                )
                            }
                        )
                    }

                    item {
                        if (formState.externalAssessment == "Yes") {
                            ComplianceQuestionWithRemarks(
                                question = "If Yes, Passed / Failed",
                                answer = formState.passedFailed,
                                remarks = formState.passedFailedRemarks,
                                isError = showError && formState.passedFailed == null,
                                onAnswerChange = {
                                    formState = formState.copy(
                                        passedFailed = it,
                                        passedFailedRemarks = ""
                                    )
                                },
                                onRemarksChange = {
                                    formState = formState.copy(
                                        passedFailedRemarks = it
                                    )
                                }
                            )
                        }
                    }

                    item {
                        ComplianceQuestionWithRemarks(
                            question = "Received certificate (if eligible)",
                            answer = formState.certificateReceived,
                            remarks = formState.certificateReceivedRemarks,
                            isError = showError && formState.certificateReceived == null,
                            onAnswerChange = {
                                formState = formState.copy(
                                    certificateReceived = it,
                                    certificateReceivedRemarks = ""
                                )
                            },
                            onRemarksChange = {
                                formState = formState.copy(
                                    certificateReceivedRemarks = it
                                )
                            }
                        )
                    }

                    item {
                        ComplianceQuestionWithRemarks(
                            question = "OJT Joined",
                            answer = formState.ojtJoined,
                            remarks = formState.ojtJoinedRemarks,
                            isError = showError && formState.ojtJoined == null,
                            onAnswerChange = {
                                formState = formState.copy(
                                    ojtJoined = it,
                                    ojtJoinedRemarks = ""
                                )
                            },
                            onRemarksChange = {
                                formState = formState.copy(
                                    ojtJoinedRemarks = it
                                )
                            }
                        )
                    }

                    item {
                        ComplianceQuestionWithRemarks(
                            question = "OJT certificate received (4.6B & 4.6C)",
                            answer = formState.ojtCertificateReceived,
                            remarks = formState.ojtCertificateReceivedRemarks,
                            isError = showError && formState.ojtCertificateReceived == null,
                            onAnswerChange = {
                                formState = formState.copy(
                                    ojtCertificateReceived = it,
                                    ojtCertificateReceivedRemarks = ""
                                )
                            },
                            onRemarksChange = {
                                formState = formState.copy(
                                    ojtCertificateReceivedRemarks = it
                                )
                            }
                        )
                    }

                    item {
                        ComplianceQuestionWithRemarks(
                            question = "OJT Entitlement received",
                            answer = formState.ojtEntitlementReceived,
                            remarks = formState.ojtEntitlementRemarks,
                            isError = showError && formState.ojtEntitlementReceived == null,
                            onAnswerChange = {
                                formState = formState.copy(
                                    ojtEntitlementReceived = it,
                                    ojtEntitlementRemarks = ""
                                )
                            },
                            onRemarksChange = {
                                formState = formState.copy(
                                    ojtEntitlementRemarks = it
                                )
                            }
                        )
                    }

                    item {
                        if (formState.ojtEntitlementReceived == "Yes") {

                            MultiLineEditText(
                                value = formState.ojtEntitlementDetails,
                                onValueChange = {
                                    formState = formState.copy(
                                        ojtEntitlementDetails = it
                                    )
                                },
                                label = "If Yes, Details",
                                isRequired = true,
                                isError = showError && formState.ojtEntitlementDetails.isBlank()
                            )
                        }
                    }

                    item {
                        ComplianceQuestionWithRemarks(
                            question = "OJT verification done by the PIA Q.Team",
                            answer = formState.ojtVerificationDone,
                            remarks = formState.ojtVerificationRemarks,
                            isError = showError && formState.ojtVerificationDone == null,
                            onAnswerChange = {
                                formState = formState.copy(
                                    ojtVerificationDone = it,
                                    ojtVerificationRemarks = ""
                                )
                            },
                            onRemarksChange = {
                                formState = formState.copy(
                                    ojtVerificationRemarks = it
                                )
                            }
                        )
                    }

                    item {
                        ComplianceQuestionWithRemarks(
                            question = "(Video & 4.3K available or not) - 100% candidate verification",
                            answer = formState.videoVerification,
                            remarks = formState.videoVerificationRemarks,
                            isError = showError && formState.videoVerification == null,
                            onAnswerChange = {
                                formState = formState.copy(
                                    videoVerification = it,
                                    videoVerificationRemarks = ""
                                )
                            },
                            onRemarksChange = {
                                formState = formState.copy(
                                    videoVerificationRemarks = it
                                )
                            }
                        )
                    }

                    item {
                        ComplianceQuestionWithRemarks(
                            question = "Got offer letter",
                            answer = formState.offerLetterReceived,
                            remarks = formState.offerLetterRemarks,
                            isError = showError && formState.offerLetterReceived == null,
                            onAnswerChange = {
                                formState = formState.copy(
                                    offerLetterReceived = it,
                                    offerLetterRemarks = ""
                                )
                            },
                            onRemarksChange = {
                                formState = formState.copy(
                                    offerLetterRemarks = it
                                )
                            }
                        )
                    }

                    item {
                        ComplianceQuestionWithRemarks(
                            question = "Performance Evaluation Plan (SF 4.3N) filled or not",
                            answer = formState.performancePlanFilled,
                            remarks = formState.performancePlanRemarks,
                            isError = showError && formState.performancePlanFilled == null,
                            onAnswerChange = {
                                formState = formState.copy(
                                    performancePlanFilled = it,
                                    performancePlanRemarks = ""
                                )
                            },
                            onRemarksChange = {
                                formState = formState.copy(
                                    performancePlanRemarks = it
                                )
                            }
                        )
                    }

                    item {
                        ComplianceQuestionWithRemarks(
                            question = "Joined the job",
                            answer = formState.joinedJob,
                            remarks = formState.joinedJobRemarks,
                            isError = showError && formState.joinedJob == null,
                            onAnswerChange = {
                                formState = formState.copy(
                                    joinedJob = it,
                                    joinedJobRemarks = ""
                                )
                            },
                            onRemarksChange = {
                                formState = formState.copy(
                                    joinedJobRemarks = it
                                )
                            }
                        )
                    }

                    item {
                        if (formState.joinedJob == "Yes") {

                            MultiLineEditText(
                                value = formState.salary,
                                onValueChange = {
                                    formState = formState.copy(salary = it)
                                },
                                label = "If Yes, What is the Salary",
                                isRequired = true,
                                isError = showError && formState.salary.isBlank(),
                                placeholder = "Enter salary amount"
                            )
                        }
                    }

                    item {
                        ComplianceQuestionWithRemarks(
                            question = "Is it match with the Minimum wages of the state",
                            answer = formState.minimumWageMatch,
                            remarks = formState.minimumWageRemarks,
                            isError = showError && formState.minimumWageMatch == null,
                            onAnswerChange = {
                                formState = formState.copy(
                                    minimumWageMatch = it,
                                    minimumWageRemarks = ""
                                )
                            },
                            onRemarksChange = {
                                formState = formState.copy(
                                    minimumWageRemarks = it
                                )
                            }
                        )
                    }

                    item {
                        WorkingStatusQuestion(
                            answer = formState.currentStatus,
                            workingMonths = formState.workingMonths,
                            notWorkingReason = formState.notWorkingReason,
                            isError = showError && formState.currentStatus == null,
                            onAnswerChange = {
                                formState = formState.copy(
                                    currentStatus = it,
                                    workingMonths = "",
                                    notWorkingReason = ""
                                )
                            },
                            onWorkingMonthsChange = {
                                formState = formState.copy(workingMonths = it)
                            },
                            onNotWorkingReasonChange = {
                                formState = formState.copy(notWorkingReason = it)
                            }
                        )
                    }

                    item {
                        ComplianceQuestionWithRemarks(
                            question = "PPS Amount disbursed to the candidates (as per the eligibility)",
                            answer = formState.ppsDisbursed,
                            remarks = formState.ppsDisbursedRemarks,
                            isError = showError && formState.ppsDisbursed == null,
                            onAnswerChange = {
                                formState = formState.copy(
                                    ppsDisbursed = it,
                                    ppsDisbursedRemarks = ""
                                )
                            },
                            onRemarksChange = {
                                formState = formState.copy(
                                    ppsDisbursedRemarks = it
                                )
                            }
                        )
                    }

                    item {
                        MultiLineEditText(
                            value = formState.replacementAction,
                            onValueChange = {
                                formState = formState.copy(
                                    replacementAction = it
                                )
                            },
                            label = "Action taken by the PIA for replacement",
                            isRequired = true,
                            isError = showError && formState.replacementAction.isBlank()
                        )
                    }

                    // button

                    item {
                        Button(
                            onClick = {

                                showError = true

                                val errorMessage = when {

                                    formState.externalAssessment == null ->
                                        "Please select External Assessment"

                                    formState.externalAssessment == "No" &&
                                            formState.externalAssessmentRemarks.isBlank() ->
                                        "Please enter remarks for External Assessment"

                                    formState.externalAssessment == "Yes" &&
                                            formState.passedFailed == null ->
                                        "Please select Passed / Failed"

                                    formState.passedFailed == "No" &&
                                            formState.passedFailedRemarks.isBlank() ->
                                        "Please enter remarks for Passed / Failed"

                                    formState.certificateReceived == null ->
                                        "Please select Received certificate"

                                    formState.certificateReceived == "No" &&
                                            formState.certificateReceivedRemarks.isBlank() ->
                                        "Please enter remarks for Certificate"

                                    formState.ojtJoined == null ->
                                        "Please select OJT Joined"

                                    formState.ojtJoined == "No" &&
                                            formState.ojtJoinedRemarks.isBlank() ->
                                        "Please enter remarks for OJT Joined"

                                    formState.ojtCertificateReceived == null ->
                                        "Please select OJT Certificate Received"

                                    formState.ojtCertificateReceived == "No" &&
                                            formState.ojtCertificateReceivedRemarks.isBlank() ->
                                        "Please enter remarks for OJT Certificate"

                                    formState.ojtEntitlementReceived == null ->
                                        "Please select OJT Entitlement Received"

                                    formState.ojtEntitlementReceived == "No" &&
                                            formState.ojtEntitlementRemarks.isBlank() ->
                                        "Please enter remarks for OJT Entitlement"

                                    formState.ojtEntitlementReceived == "Yes" &&
                                            formState.ojtEntitlementDetails.isBlank() ->
                                        "Please enter details for OJT Entitlement"

                                    formState.ojtVerificationDone == null ->
                                        "Please select OJT Verification status"

                                    formState.ojtVerificationDone == "No" &&
                                            formState.ojtVerificationRemarks.isBlank() ->
                                        "Please enter remarks for OJT Verification"

                                    formState.videoVerification == null ->
                                        "Please select Video Verification status"

                                    formState.videoVerification == "No" &&
                                            formState.videoVerificationRemarks.isBlank() ->
                                        "Please enter remarks for Video Verification"

                                    formState.performancePlanFilled == null ->
                                        "Please select Performance Evaluation status"

                                    formState.performancePlanFilled == "No" &&
                                            formState.performancePlanRemarks.isBlank() ->
                                        "Please enter remarks for Performance Plan"

                                    formState.offerLetterReceived == null ->
                                        "Please select Offer Letter status"

                                    formState.offerLetterReceived == "No" &&
                                            formState.offerLetterRemarks.isBlank() ->
                                        "Please enter remarks for Offer Letter"

                                    formState.joinedJob == null ->
                                        "Please select Joined the job"

                                    formState.joinedJob == "No" &&
                                            formState.joinedJobRemarks.isBlank() ->
                                        "Please enter remarks for Joined the job"

                                    formState.joinedJob == "Yes" &&
                                            formState.salary.isBlank() ->
                                        "Please enter Salary"

                                    formState.minimumWageMatch == null ->
                                        "Please select Minimum wage match"

                                    formState.minimumWageMatch == "No" &&
                                            formState.minimumWageRemarks.isBlank() ->
                                        "Please enter remarks for Minimum wage"

                                    formState.currentStatus == null ->
                                        "Please select Current Status"

                                    formState.currentStatus == "Not Working" &&
                                            formState.currentStatusRemarks.isBlank() ->
                                        "Please enter remarks for Current Status"


                                    formState.joinedJob == null ->
                                        "Please select Joined the job"

                                    formState.joinedJob == "No" &&
                                            formState.joinedJobRemarks.isBlank() ->
                                        "Please enter remarks for Joined the job"

                                    formState.joinedJob == "Yes" &&
                                            formState.salary.isBlank() ->
                                        "Please enter Salary"

                                    formState.minimumWageMatch == null ->
                                        "Please select Minimum wage match"

                                    formState.minimumWageMatch == "No" &&
                                            formState.minimumWageRemarks.isBlank() ->
                                        "Please enter remarks for Minimum wage"


                                    formState.currentStatus == null ->
                                        "Please select Current Status"

                                    formState.currentStatus == "Working" &&
                                            formState.workingMonths.isBlank() ->
                                        "Please enter number of months working"

                                    formState.currentStatus == "Not Working" &&
                                            formState.notWorkingReason.isBlank() ->
                                        "Please enter reason for leaving the job"

                                    formState.ppsDisbursed == null ->
                                        "Please select PPS Amount Disbursed"

                                    formState.ppsDisbursed == "No" &&
                                            formState.ppsDisbursedRemarks.isBlank() ->
                                        "Please enter remarks for PPS Disbursed"

                                    formState.replacementAction.isBlank() ->
                                        "Please enter Action taken by the PIA"




                                    else -> null
                                }
                                if (errorMessage != null) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = errorMessage,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                } else {
                                    onSubmit(formState) // 🔥 API CALL
                                }
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