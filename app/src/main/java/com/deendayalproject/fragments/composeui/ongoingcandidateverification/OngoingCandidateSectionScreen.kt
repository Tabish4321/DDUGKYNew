package com.deendayalproject.fragments.composeui.ongoingcandidateverification

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deendayalproject.BuildConfig
import com.deendayalproject.fragments.composeui.common.ComplianceStatus
import com.deendayalproject.fragments.composeui.common.ExpandableComplianceCard
import com.deendayalproject.fragments.composeui.common.PremiumCandidateHeader
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.fragments.composeui.tlm.TlmVerificationSection
import com.deendayalproject.model.request.GetAttendanceDetailsReq
import com.deendayalproject.model.request.OngoingSubmitBasicRecordsReq
import com.deendayalproject.model.response.CandidateProofItem
import com.deendayalproject.model.response.ExpandableSectionName
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.InspectionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun OngoingCandidateSectionScreen(
    context: Context,
    viewModel : InspectionViewModel,
    candidateId: String,
    candidateName: String,
    batchId: String,
    candidateMobileNo: String,
    candidateRollNo: String,
    imageList: List<CandidateProofItem>?,
    onBackClick: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val isLoading by viewModel
        .isSubmittingBasicRecord
        .collectAsState()

    val response by viewModel
        .submitBasicRecordResponse
        .collectAsState()

    val sections = listOf(
        ExpandableSectionName("Basic Records Verification", ComplianceStatus.NotCOMPLETE),
        ExpandableSectionName("Validate Attendance", ComplianceStatus.COMPLETE),
        ExpandableSectionName("Assessment", ComplianceStatus.NotCOMPLETE),
        ExpandableSectionName("Distribution of Teaching-Learning Material", ComplianceStatus.COMPLETE),
        ExpandableSectionName("Entitlements Distribution", ComplianceStatus.COMPLETE),
        ExpandableSectionName("Residential Facility Verification", ComplianceStatus.NotCOMPLETE)
    )

    val expandedSections = remember {
        mutableStateMapOf<String, Boolean>()
    }


    LaunchedEffect(response?.responseCode) {

        if (response?.responseCode == 200) {

            snackbarHostState.showSnackbar(
                response?.responseDesc ?: "Saved Successfully"
            )

            expandedSections["Basic Records Verification"] = false

            viewModel.clearSubmitResponse()
        }
    }

    val listState = rememberLazyListState()

    Scaffold(

        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },

        topBar = {

            PremiumTopBar(
                dynamicTitle = "Candidate Details",
                onBackClick = onBackClick
            )
        },

        containerColor = Color.White

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            PremiumCandidateHeader(
                candidateId = candidateId,
                candidateName = candidateName,
                candidateMobileNo = candidateMobileNo,
                candidateRollNo = candidateRollNo
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(sections) { section ->

                    ExpandableComplianceCard(

                        title = section.title,

                        status = section.status,

                        expanded = expandedSections[section.title] ?: false,

                        onExpandedChange = {

                            expandedSections[section.title] = it

                        },

                        leftIcon = {

                            Icon(
                                imageVector = getSectionIcon(section.title),
                                contentDescription = null
                            )
                        }

                    ) {

                        when (section.title) {
                            "Basic Records Verification" -> {

                                BasicRecordsSection(

                                    imageList = imageList,

                                    isLoading = isLoading,

                                    showMessage = {

                                        CoroutineScope(Dispatchers.Main).launch {
                                            snackbarHostState.showSnackbar(it)
                                        }

                                    },

                                    onSubmit = { documents ->

                                        val req = OngoingSubmitBasicRecordsReq(
                                            appVersion = BuildConfig.VERSION_NAME,
                                            candidateId = candidateId,
                                            inspectionId = 3,
                                            batchId = 6,

                                            povertyProofQid = documents[0].qid,
                                            povertyProof = documents[0].answer ?: "",
                                            povertyProofRemark = documents[0].remarks,

                                            categoryProofQid = documents[1].qid,
                                            categoryProof = documents[1].answer ?: "",
                                            categoryProofRemark = documents[1].remarks,

                                            minorityProofQid = documents[2].qid,
                                            minorityProof = documents[2].answer ?: "",
                                            minorityProofRemark = documents[2].remarks,

                                            educationProofQid = documents[3].qid,
                                            educationProof = documents[3].answer ?: "",
                                            educationProofRemark = documents[3].remarks,

                                            pwdProofQid = documents[4].qid,
                                            pwdProof = documents[4].answer ?: "",
                                            pwdProofRemark = documents[4].remarks
                                        )

                                        viewModel.submitBasicRecords(req, "")

                                    }
                                )

                            }

                            "Validate Attendance" -> {

                                AttendanceComplianceScreen(

                                    viewModel = viewModel,

                                    request = GetAttendanceDetailsReq(
                                        candidateId = candidateId,
                                        batchId = batchId,
                                        appVersion = BuildConfig.VERSION_NAME
                                    ),

                                    onSubmitClick = { attendance,
                                                      counselling,
                                                      regularAttendance,
                                                      attendanceRemark,
                                                      counsellingRemark,
                                                      regularRemark ->


                                    }
                                )
                            }


                            "Assessment" -> {

                                AssessmentSection(
                                    viewModel = viewModel,
                                    snackbarHostState = snackbarHostState,
                                    onSubmit = { camera,
                                                 seriousness,
                                                 malpractice,
                                                 reval,
                                                 retest,
                                                 camRemark,
                                                 serRemark,
                                                 malRemark,
                                                 revalRemark,
                                                 retestRemark ->

                                        // API call here
                                    }
                                )
                            }



                            "Distribution of Teaching-Learning Material" ->{



                                val snackbarHostState = remember { SnackbarHostState() }

                                TlmVerificationSection(

                                    snackbarHostState = snackbarHostState

                                ) { questions ->


                                    // Api hit submit
                                  /*  val request = SubmitTlmInspectionReq(

                                        domainCurriculum = questions[0].answer,
                                        domainCurriculumProof = questions[0].imageBase64

                                    )

                                    viewModel.submitTlmInspection(request)*/
                                }



                            }


                            else -> {

                                SectionContent(section.title)

                            }
                        }
                    }
                }
            }
        }
    }
}