package com.deendayalproject.fragments.composeui.ongoingcandidateverification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.fragments.composeui.common.InfoRow
import com.deendayalproject.model.response.AttendanceStatusItem
import com.deendayalproject.viewmodel.InspectionViewModel
import kotlinx.coroutines.launch

@Composable
fun AssessmentSection(
    viewModel: InspectionViewModel,
    snackbarHostState: SnackbarHostState,
    onSubmit: (
        String,
        String,
        String,
        String,
        String,
        String?,
        String?,
        String?,
        String?,
        String?
    ) -> Unit
) {

    val scope = rememberCoroutineScope()

    var cameraAnswer by remember { mutableStateOf<String?>(null) }
    var seriousnessAnswer by remember { mutableStateOf<String?>(null) }
    var malpracticeAnswer by remember { mutableStateOf<String?>(null) }
    var diffRevalAnswer by remember { mutableStateOf<String?>(null) }
    var diffRetestAnswer by remember { mutableStateOf<String?>(null) }

    var cameraRemark by remember { mutableStateOf("") }
    var seriousnessRemark by remember { mutableStateOf("") }
    var malpracticeRemark by remember { mutableStateOf("") }
    var diffRevalRemark by remember { mutableStateOf("") }
    var diffRetestRemark by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // 🔹 Top Status Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {

            InfoRow(
                icon = Icons.Default.Assessment,
                label = "Assessment Status",
                value = "NA"
            )

            Divider()

            InfoRow(
                icon = Icons.Default.EventAvailable,
                label = "Present on Assessment Day",
                value = "N/A"
            )
        }

        // 🔹 Questions
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ComplianceQuestionWithRemarks(
                question = "Verified via IP Enabled Camera",
                answer = cameraAnswer,
                remarks = cameraRemark,
                isError = showError && cameraAnswer == null,
                onAnswerChange = { cameraAnswer = it },
                onRemarksChange = { cameraRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Seriousness during test",
                answer = seriousnessAnswer,
                remarks = seriousnessRemark,
                isError = showError && seriousnessAnswer == null,
                onAnswerChange = { seriousnessAnswer = it },
                onRemarksChange = { seriousnessRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Malpractices observed",
                answer = malpracticeAnswer,
                remarks = malpracticeRemark,
                isError = showError && malpracticeAnswer == null,
                onAnswerChange = { malpracticeAnswer = it },
                onRemarksChange = { malpracticeRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Difference between actual and revaluation marks >10%",
                answer = diffRevalAnswer,
                remarks = diffRevalRemark,
                isError = showError && diffRevalAnswer == null,
                onAnswerChange = { diffRevalAnswer = it },
                onRemarksChange = { diffRevalRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Difference in retest marks >20%",
                answer = diffRetestAnswer,
                remarks = diffRetestRemark,
                isError = showError && diffRetestAnswer == null,
                onAnswerChange = { diffRetestAnswer = it },
                onRemarksChange = { diffRetestRemark = it }
            )
        }

        Spacer(Modifier.height(10.dp))

        // 🔹 Submit Button
        PremiumSubmitButton {

            showError = true

            scope.launch {

                when {

                    cameraAnswer == null ->
                        snackbarHostState.showSnackbar("Please select: Verified via IP Enabled Camera")

                    seriousnessAnswer == null ->
                        snackbarHostState.showSnackbar("Please select: Seriousness during test")

                    malpracticeAnswer == null ->
                        snackbarHostState.showSnackbar("Please select: Malpractices observed")

                    diffRevalAnswer == null ->
                        snackbarHostState.showSnackbar("Please select: Difference between actual and revaluation marks")

                    diffRetestAnswer == null ->
                        snackbarHostState.showSnackbar("Please select: Difference in retest marks")

                    cameraAnswer == "No" && cameraRemark.isBlank() ->
                        snackbarHostState.showSnackbar("Please enter remarks for IP Enabled Camera")

                    seriousnessAnswer == "No" && seriousnessRemark.isBlank() ->
                        snackbarHostState.showSnackbar("Please enter remarks for Seriousness during test")

                    malpracticeAnswer == "No" && malpracticeRemark.isBlank() ->
                        snackbarHostState.showSnackbar("Please enter remarks for Malpractices")

                    else -> {

                        onSubmit(
                            cameraAnswer!!,
                            seriousnessAnswer!!,
                            malpracticeAnswer!!,
                            diffRevalAnswer!!,
                            diffRetestAnswer!!,
                            cameraRemark,
                            seriousnessRemark,
                            malpracticeRemark,
                            diffRevalRemark,
                            diffRetestRemark
                        )
                    }
                }
            }
        }
    }
}