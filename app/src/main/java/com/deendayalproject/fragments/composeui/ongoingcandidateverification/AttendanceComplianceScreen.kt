package com.deendayalproject.fragments.composeui.ongoingcandidateverification

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.fragments.composeui.common.InfoRow
import com.deendayalproject.model.request.GetAttendanceDetailsReq
import com.deendayalproject.viewmodel.CandidateAssessmentViewModel
import com.deendayalproject.viewmodel.InspectionViewModel


@Composable
fun AttendanceComplianceScreen(
    viewModel: InspectionViewModel,
    request: GetAttendanceDetailsReq,
    candidateAssessmentViewModel: CandidateAssessmentViewModel,
    snackbarHostState: SnackbarHostState,
    inspectionId: Int

) {

    val scope = rememberCoroutineScope()

    val attendanceState by viewModel.getCandidateTodayAttendanceStatus.collectAsState()

    val state by candidateAssessmentViewModel.candidateAttendanceState.collectAsState()

    /* ----------------------------- */
    /* FORM STATE */
    /* ----------------------------- */

    var attendanceAnswer by remember { mutableStateOf<String?>(null) }
    var counsellingAnswer by remember { mutableStateOf<String?>(null) }
    var regularAttendanceAnswer by remember { mutableStateOf<String?>(null) }

    var attendanceRemark by remember { mutableStateOf("") }
    var counsellingRemark by remember { mutableStateOf("") }
    var regularAttendanceRemark by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }

    /* ----------------------------- */
    /* LOAD STATUS API */
    /* ----------------------------- */

    LaunchedEffect(request.candidateId) {

        if (request.candidateId.isNotEmpty()) {
            viewModel.getCandidateTodayAttendanceStatus(request)

            candidateAssessmentViewModel.loadCandidateAttendance(
                batchId = request.batchId.toInt(),
                inspectionId = inspectionId,
                candidateId = request.candidateId
            )
        }
    }



    /* ----------------------------- */
    /* PREFILL FORM */
    /* ----------------------------- */
    val context = LocalContext.current

    LaunchedEffect(
        state.biomatricAttendance,
        state.attendanceCounselling,
        state.regularlyAttend
    ) {

        attendanceAnswer = state.biomatricAttendance
        counsellingAnswer = state.attendanceCounselling
        regularAttendanceAnswer = state.regularlyAttend

        attendanceRemark = state.biomatricAttendanceRemark
        counsellingRemark = state.attendanceCounsellingRemark
        regularAttendanceRemark = state.regularlyAttendRemark
    }

    /* ----------------------------- */
    /* ERROR */
    /* ----------------------------- */

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
            //snackbarHostState.showSnackbar(it)
            candidateAssessmentViewModel.clearCandidateAttendanceError()

        }
    }

    /* ----------------------------- */
    /* SUCCESS */
    /* ----------------------------- */

    LaunchedEffect(state.saveSuccess) {

        if (state.saveSuccess) {
            candidateAssessmentViewModel.triggerRefresh()
            Toast.makeText(context,"Saved Successfully", Toast.LENGTH_SHORT).show()

            //  snackbarHostState.showSnackbar("Saved Successfully")
            candidateAssessmentViewModel.clearCandidateAttendanceSuccess()
        }
    }

    val item = attendanceState?.wrappedList?.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F7FB))
            .padding(horizontal = 6.dp, vertical = 12.dp),

        verticalArrangement = Arrangement.spacedBy(18.dp)

    ) {

        /* ----------------------------- */
        /* INFO CARD */
        /* ----------------------------- */

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp)

        ) {

            InfoRow(
                icon = Icons.Default.EventAvailable,
                label = "Attendance Status",
                value = item?.attendanceStatus ?: "N/A"
            )

            Divider()


            InfoRow(
                icon = Icons.Default.Percent,
                label = "Attendance Percentage",
                value = item?.attendancePercentage ?: "N/A"
            )

            Divider()


            item?.let { it.attendancePercentage!! }?.let {
                if(it.toInt()<75) {
                    InfoRow(
                        icon = Icons.Default.SupportAgent,
                        label = "Counselling Status",
                        value = item?.counsellingStatus ?: "N/A"
                    )

                    Divider()
                }
            }

            InfoRow(
                icon = Icons.Default.CheckCircle,
                label = "Regular Attendance",
                value = item?.regularAttendance ?: "N/A"
            )
        }

        /* ----------------------------- */
        /* FORM */
        /* ----------------------------- */


        ComplianceQuestionWithRemarks(

            question = "Candidate Attendance matches biometric attendance ?",
            answer = attendanceAnswer,
            remarks = attendanceRemark,
            isError = showError && attendanceAnswer == null,
            onAnswerChange = { attendanceAnswer = it },
            onRemarksChange = { attendanceRemark = it }
        )

        item?.let { it.attendancePercentage!! }?.let {
            if(it.toInt()<75){
                ComplianceQuestionWithRemarks(
                    question = "Counselling arranged if attendance <75% ?",
                    answer = counsellingAnswer,
                    remarks = counsellingRemark,
                    isError = showError && counsellingAnswer == null,
                    onAnswerChange = { counsellingAnswer = it },
                    onRemarksChange = { counsellingRemark = it }
                )
            }
        }


        ComplianceQuestionWithRemarks(
            question = "Candidate attended all classes regularly ?",
            answer = regularAttendanceAnswer,
            remarks = regularAttendanceRemark,
            isError = showError && regularAttendanceAnswer == null,
            onAnswerChange = { regularAttendanceAnswer = it },
            onRemarksChange = { regularAttendanceRemark = it }
        )

        /* ----------------------------- */
        /* SUBMIT */
        /* ----------------------------- */

        PremiumSubmitButton {

            showError = true

            val isValid =
                attendanceAnswer != null &&
                        regularAttendanceAnswer != null &&
                        !(attendanceAnswer == "No" && attendanceRemark.isBlank()) &&
                        !(counsellingAnswer == "No" && counsellingRemark.isBlank()) &&
                        !(regularAttendanceAnswer == "No" && regularAttendanceRemark.isBlank())

            if (isValid) {

                candidateAssessmentViewModel.updateCandidateAttendanceState(
                    attendanceAnswer,
                    counsellingAnswer,
                    regularAttendanceAnswer,
                    attendanceRemark,
                    counsellingRemark,
                    regularAttendanceRemark
                )

                candidateAssessmentViewModel.saveCandidateAttendance(
                    batchId = request.batchId.toInt(),
                    inspectionId = inspectionId,
                    candidateId = request.candidateId
                )
            }
        }
    }
}