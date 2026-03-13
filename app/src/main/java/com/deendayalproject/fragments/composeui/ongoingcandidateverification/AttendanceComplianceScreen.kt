package com.deendayalproject.fragments.composeui.ongoingcandidateverification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.fragments.composeui.common.InfoRow
import com.deendayalproject.model.request.GetAttendanceDetailsReq
import com.deendayalproject.viewmodel.InspectionViewModel


@Composable
fun AttendanceComplianceScreen(
    viewModel: InspectionViewModel,
    request: GetAttendanceDetailsReq,
    onSubmitClick: (String, String, String, String?, String?, String?) -> Unit
) {

    val attendanceState by viewModel.getCandidateTodayAttendanceStatus.collectAsState()

    var attendanceAnswer by remember { mutableStateOf<String?>(null) }
    var counsellingAnswer by remember { mutableStateOf<String?>(null) }
    var regularAttendanceAnswer by remember { mutableStateOf<String?>(null) }


    var attendanceRemark by remember { mutableStateOf("") }
    var counsellingRemark by remember { mutableStateOf("") }
    var regularAttendanceRemark by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getCandidateTodayAttendanceStatus(request)
    }

    val item = attendanceState?.wrappedList?.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F7FB))
            .padding(horizontal = 6.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

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
                icon = Icons.Default.SupportAgent,
                label = "Attendance Percentage",
                value = item?.attendancePercentage ?: "N/A"
            )

            Divider()




            InfoRow(
                icon = Icons.Default.SupportAgent,
                label = "Counselling Status",
                value = item?.counsellingStatus ?: "N/A"
            )

            Divider()

            InfoRow(
                icon = Icons.Default.CheckCircle,
                label = "Regular Attendance",
                value = item?.regularAttendance ?: "N/A"
            )
        }

        ComplianceQuestionWithRemarks(
            question = "Is candidate present today?",
            answer = attendanceAnswer,
            remarks = attendanceRemark,
            isError = showError && attendanceAnswer == null,
            onAnswerChange = { attendanceAnswer = it },
            onRemarksChange = { attendanceRemark = it }
        )



        ComplianceQuestionWithRemarks(
            question = "Counselling completed?",
            answer = counsellingAnswer,
            remarks = counsellingRemark,
            isError = showError && counsellingAnswer == null,
            onAnswerChange = { counsellingAnswer = it },
            onRemarksChange = { counsellingRemark = it }
        )



        ComplianceQuestionWithRemarks(
            question = "Candidate attending regularly?",
            answer = regularAttendanceAnswer,
            remarks = regularAttendanceRemark,
            isError = showError && regularAttendanceAnswer == null,
            onAnswerChange = { regularAttendanceAnswer = it },
            onRemarksChange = { regularAttendanceRemark = it }
        )

        PremiumSubmitButton {

            showError = true

            val isValid =
                attendanceAnswer != null &&
                        counsellingAnswer != null &&
                        regularAttendanceAnswer != null &&
                        !(attendanceAnswer == "No" && attendanceRemark.isBlank()) &&
                        !(counsellingAnswer == "No" && counsellingRemark.isBlank()) &&
                        !(regularAttendanceAnswer == "No" && regularAttendanceRemark.isBlank())

            if (isValid) {

                onSubmitClick(
                    attendanceAnswer!!,
                    counsellingAnswer!!,
                    regularAttendanceAnswer!!,
                    attendanceRemark,
                    counsellingRemark,
                    regularAttendanceRemark
                )
            }
        }
    }
}