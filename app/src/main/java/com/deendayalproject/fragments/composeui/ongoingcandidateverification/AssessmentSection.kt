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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.fragments.composeui.common.InfoRow
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.CandidateAssessmentViewModel
import kotlinx.coroutines.launch

@Composable
fun AssessmentSection(
    candidateAssesmentViewModel: CandidateAssessmentViewModel,
    snackbarHostState: SnackbarHostState,
    batchId:String,
    candidateId:String,

) {

    val scope = rememberCoroutineScope()
    val context= LocalContext.current

    val state by candidateAssesmentViewModel.uiState.collectAsState()
    val statusState by candidateAssesmentViewModel.uiStatusState.collectAsState()

    var presentDuringAssesment by remember { mutableStateOf<String?>(null) }
    var cameraAnswer by remember { mutableStateOf<String?>(null) }
    var seriousnessAnswer by remember { mutableStateOf<String?>(null) }
    var malpracticeAnswer by remember { mutableStateOf<String?>(null) }
    var diffRevalAnswer by remember { mutableStateOf<String?>(null) }
    var diffRetestAnswer by remember { mutableStateOf<String?>(null) }

    var presentDuringAssesmentRemarks by remember { mutableStateOf("") }
    var cameraRemark by remember { mutableStateOf("") }
    var seriousnessRemark by remember { mutableStateOf("") }
    var malpracticeRemark by remember { mutableStateOf("") }
    var diffRevalRemark by remember { mutableStateOf("") }
    var diffRetestRemark by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }

    /* -------------------- */
    /* Prefill API Data */
    /* -------------------- */

    LaunchedEffect(
        state.presentAssessment,
        state.cameraVerified,
        state.seriousness,
        state.malpracticesObserved,
        state.actualAndRevaluationMarks,
        state.retestMarksDifference
    ) {
        presentDuringAssesment=state.presentAssessment
        cameraAnswer = state.cameraVerified
        seriousnessAnswer = state.seriousness
        malpracticeAnswer = state.malpracticesObserved
        diffRevalAnswer = state.actualAndRevaluationMarks
        diffRetestAnswer = state.retestMarksDifference

        presentDuringAssesmentRemarks=state.presentAssessmentRemark
        cameraRemark = state.cameraVerifiedRemark
        seriousnessRemark = state.seriousnessRemark
        malpracticeRemark = state.malpracticesObservedRemark
        diffRevalRemark = state.actualAndRevaluationMarksRemark
        diffRetestRemark = state.retestMarksDifferenceRemark
    }

    LaunchedEffect(candidateId) {

        if (candidateId.isNotEmpty()) {

            candidateAssesmentViewModel.loadAssessmentStatus(
                batchId = batchId.toInt(),
                inspectionId = AppUtil.getSavedInspectionIdPreference(context).toInt(),
                candidateId = candidateId
            )

            candidateAssesmentViewModel.loadAssessmentDetails(
                batchId = batchId.toInt(),
                inspectionId = AppUtil.getSavedInspectionIdPreference(context).toInt(),
                candidateId = candidateId
            )
        }
    }


    /* -------------------- */
    /* Save Success */
    /* -------------------- */

    LaunchedEffect(state.saveSuccess) {

        if (state.saveSuccess) {
            candidateAssesmentViewModel.triggerRefresh()
            snackbarHostState.showSnackbar("Assessment saved successfully")
            candidateAssesmentViewModel.clearSaveState()
        }
    }

    /* -------------------- */
    /* Error */
    /* -------------------- */

    LaunchedEffect(state.error) {

        state.error?.let {
            snackbarHostState.showSnackbar(it)
            candidateAssesmentViewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        /* -------------------- */
        /* Status Card */
        /* -------------------- */

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {

            InfoRow(
                icon = Icons.Default.Assessment,
                label = "Assessment Status",
                value = statusState.assessmentStatus ?: "N/A"
            )

            Divider()

            InfoRow(
                icon = Icons.Default.EventAvailable,
                label = "Present on Assessment Day",
                value = statusState.isPresent ?: "N/A"
            )

            Divider()

            InfoRow(
                icon = Icons.Default.EventAvailable,
                label = "Date of Assessment",
                value = statusState.assessmentDate ?: "N/A"
            )
        }

        /* -------------------- */
        /* Questions */
        /* -------------------- */

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {


            ComplianceQuestionWithRemarks(
                question = "Present during the assessment day",
                answer = presentDuringAssesment,
                remarks = presentDuringAssesmentRemarks,
                isError = showError && presentDuringAssesment == null,
                onAnswerChange = { presentDuringAssesment = it },
                onRemarksChange = { presentDuringAssesmentRemarks = it }
            )

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

        /* -------------------- */
        /* Submit Button */
        /* -------------------- */

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

//                        onSubmit(
//                            cameraAnswer!!,
//                            seriousnessAnswer!!,
//                            malpracticeAnswer!!,
//                            diffRevalAnswer!!,
//                            diffRetestAnswer!!,
//                            cameraRemark,
//                            seriousnessRemark,
//                            malpracticeRemark,
//                            diffRevalRemark,
//                            diffRetestRemark
//                        )

                        candidateAssesmentViewModel.updateState {

                            copy(
                                presentAssessment=presentDuringAssesment,
                                cameraVerified = cameraAnswer,
                                seriousness = seriousnessAnswer,
                                malpracticesObserved = malpracticeAnswer,
                                actualAndRevaluationMarks = diffRevalAnswer,
                                retestMarksDifference = diffRetestAnswer,

                                presentAssessmentRemark = presentDuringAssesmentRemarks,
                                cameraVerifiedRemark = cameraRemark,
                                seriousnessRemark = seriousnessRemark,
                                malpracticesObservedRemark = malpracticeRemark,
                                actualAndRevaluationMarksRemark = diffRevalRemark,
                                retestMarksDifferenceRemark = diffRetestRemark
                            )
                        }

                        candidateAssesmentViewModel.saveAssessment(
                            batchId = batchId.toInt(),
                            inspectionId = AppUtil.getSavedInspectionIdPreference(context = context).toInt(),
                            candidateId = candidateId
                        )
                    }
                }
            }
        }
    }
}