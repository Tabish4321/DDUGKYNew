package com.deendayalproject.fragments.composeui.previous_inspection

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.deendayalproject.model.response.CandidateListInspectionRes
import com.deendayalproject.model.uistate.CandidateVerificationUiState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.deendayalproject.fragments.composeui.common.InfoRow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateVerificationBottomSheet(
    candidateData: CandidateListInspectionRes,
    onDismiss: () -> Unit,
    onSubmit: (CandidateVerificationUiState) -> Unit
) {

    var state by remember { mutableStateOf(CandidateVerificationUiState()) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    val coroutineScope = rememberCoroutineScope()

    // 🔥 Open Full Screen Initially (Smooth Animation)
    LaunchedEffect(Unit) {
        sheetState.expand()
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        tonalElevation = 8.dp
    ) {

        Scaffold(
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = candidateData.name,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    sheetState.hide()
                                    onDismiss()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { padding ->

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // =============================
                // 🔹 Candidate Info Card
                // =============================

                item {

                    ElevatedCard(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.elevatedCardElevation(4.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            InfoRow(
                                icon = Icons.Default.Badge,
                                label = "KP ID",
                                value = candidateData.candidateId.toString()
                            )

                            InfoRow(
                                icon = Icons.Default.ConfirmationNumber,
                                label = "Roll No",
                                value = candidateData.rollNumber
                            )

                            InfoRow(
                                icon = Icons.Default.Phone,
                                label = "Contact No",
                                value = candidateData.contactNumber
                            )
                        }
                    }
                }

                // =============================
                // 🔹 External Assessment Section
                // =============================

                item {

                    ElevatedCard(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.elevatedCardElevation(3.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {

                            Text(
                                text = "External Assessment",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            // 1️⃣ External Assessment
                            ComplianceQuestionItem(
                                question = "External Assessment completed",
                                answer = state.externalAssessment,
                                remarks = state.externalAssessmentRemarks,
                                onAnswerChange = {
                                    state = state.copy(
                                        externalAssessment = it,
                                        passed = null,
                                        passedRemarks = ""
                                    )
                                },
                                onRemarksChange = {
                                    state = state.copy(
                                        externalAssessmentRemarks = it
                                    )
                                }
                            )

                            // 2️⃣ Passed / Failed
                            if (state.externalAssessment == "Yes") {

                                ComplianceQuestionItem(
                                    question = "If Yes, Passed/Failed",
                                    answer = state.passed,
                                    remarks = state.passedRemarks,
                                    onAnswerChange = {
                                        state = state.copy(passed = it)
                                    },
                                    onRemarksChange = {
                                        state = state.copy(passedRemarks = it)
                                    }
                                )
                            }
                        }
                    }
                }

                // =============================
                // 🔹 Submit Button
                // =============================

                item {

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onSubmit(state) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Submit Verification")
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}