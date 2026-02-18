package com.deendayalproject.fragments.composeui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.deendayalproject.model.response.CandidateListInspectionRes
import com.deendayalproject.model.uistate.CandidateVerificationUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateVerificationBottomSheet(
    candidateData: CandidateListInspectionRes,
    onDismiss: () -> Unit,
    onSubmit: (CandidateVerificationUiState) -> Unit
) {

    var state by remember { mutableStateOf(CandidateVerificationUiState()) }
    var showError by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                Text(
                    text = "Verify Candidate - ${candidateData.name}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // 🔹 External Assessment Section
            item {
                VerificationSection("Assessment Details") {

                    ComplianceQuestionItem(
                        "External Assessment completed",
                        state.externalAssessment,
                        state.externalAssessmentRemarks,
                        { state = state.copy(externalAssessment = it) },
                        { state = state.copy(externalAssessmentRemarks = it) }
                    )

                    if (state.externalAssessment == "Yes") {
                        ComplianceQuestionItem(
                            "If Yes, Passed/Failed",
                            state.passed,
                            state.passedRemarks,
                            { state = state.copy(passed = it) },
                            { state = state.copy(passedRemarks = it) }
                        )
                    }

                    ComplianceQuestionItem(
                        "Received certificate (if eligible)",
                        state.certificateReceived,
                        state.certificateRemarks,
                        { state = state.copy(certificateReceived = it) },
                        { state = state.copy(certificateRemarks = it) }
                    )
                }
            }

            // 🔹 OJT Section
            item {
                VerificationSection("OJT Details") {

                    ComplianceQuestionItem(
                        "OJT Joined",
                        state.ojtJoined,
                        state.ojtJoinedRemarks,
                        { state = state.copy(ojtJoined = it) },
                        { state = state.copy(ojtJoinedRemarks = it) }
                    )

                    ComplianceQuestionItem(
                        "OJT certificate received (4.6B & 4.6C)",
                        state.ojtCertificateReceived,
                        state.ojtCertificateRemarks,
                        { state = state.copy(ojtCertificateReceived = it) },
                        { state = state.copy(ojtCertificateRemarks = it) }
                    )

                    ComplianceQuestionItem(
                        "OJT Entitlement received",
                        state.ojtEntitlementReceived,
                        state.ojtEntitlementRemarks,
                        { state = state.copy(ojtEntitlementReceived = it) },
                        { state = state.copy(ojtEntitlementRemarks = it) }
                    )

                    if (state.ojtEntitlementReceived == "Yes") {
                        MultiLineEditText(
                            value = state.ojtDetails,
                            onValueChange = {
                                state = state.copy(ojtDetails = it)
                            },
                            isError = false
                        )
                    }
                }
            }

            // 🔹 Verification Section
            item {
                VerificationSection("Verification & Employment") {

                    ComplianceQuestionItem(
                        "OJT verification done by PIA Q.Team",
                        state.ojtVerificationDone,
                        state.ojtVerificationRemarks,
                        { state = state.copy(ojtVerificationDone = it) },
                        { state = state.copy(ojtVerificationRemarks = it) }
                    )

                    ComplianceQuestionItem(
                        "100% candidate verification (Video & 4.3K)",
                        state.videoVerification,
                        state.videoVerificationRemarks,
                        { state = state.copy(videoVerification = it) },
                        { state = state.copy(videoVerificationRemarks = it) }
                    )

                    ComplianceQuestionItem(
                        "Performance Evaluation Plan filled",
                        state.performancePlanFilled,
                        state.performancePlanRemarks,
                        { state = state.copy(performancePlanFilled = it) },
                        { state = state.copy(performancePlanRemarks = it) }
                    )

                    ComplianceQuestionItem(
                        "Got offer letter",
                        state.offerLetterReceived,
                        state.offerLetterRemarks,
                        { state = state.copy(offerLetterReceived = it) },
                        { state = state.copy(offerLetterRemarks = it) }
                    )

                    ComplianceQuestionItem(
                        "Joined the job",
                        state.joinedJob,
                        state.joinedJobRemarks,
                        { state = state.copy(joinedJob = it) },
                        { state = state.copy(joinedJobRemarks = it) }
                    )

                    if (state.joinedJob == "Yes") {

                        OutlinedTextField(
                            value = state.salary,
                            onValueChange = {
                                state = state.copy(salary = it)
                            },
                            label = { Text("Salary") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        ComplianceQuestionItem(
                            "Is it match with Minimum wages?",
                            state.minimumWageMatch,
                            state.minimumWageRemarks,
                            { state = state.copy(minimumWageMatch = it) },
                            { state = state.copy(minimumWageRemarks = it) }
                        )
                    }
                }
            }

            // 🔹 Working Status Section
            item {
                VerificationSection("Working Status") {

                    WorkingChipSelection(
                        selected = state.workingStatus,
                        onSelected = {
                            state = state.copy(workingStatus = it)
                        }
                    )

                    if (state.workingStatus == "Working") {
                        OutlinedTextField(
                            value = state.monthsWorking,
                            onValueChange = {
                                state = state.copy(monthsWorking = it)
                            },
                            label = { Text("Number of months working") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (state.workingStatus == "Not Working") {
                        MultiLineEditText(
                            value = state.notWorkingReason,
                            onValueChange = {
                                state = state.copy(notWorkingReason = it)
                            },
                            isError = false
                        )
                    }

                    ComplianceQuestionItem(
                        "PPS Amount disbursed",
                        state.ppsDisbursed,
                        state.ppsRemarks,
                        { state = state.copy(ppsDisbursed = it) },
                        { state = state.copy(ppsRemarks = it) }
                    )

                    MultiLineEditText(
                        value = state.actionTaken,
                        onValueChange = {
                            state = state.copy(actionTaken = it)
                        },
                        isError = false
                    )
                }
            }

            item {

                if (showError) {
                    Text(
                        "Please fill all required fields",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = {

                        val valid =
                            !state.externalAssessment.isNullOrBlank() &&
                                    !state.joinedJob.isNullOrBlank() &&
                                    !(state.joinedJob == "Yes" && state.salary.isBlank())

                        if (valid) {
                            onSubmit(state)
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Submit Verification")
                }
            }
        }
    }
}
