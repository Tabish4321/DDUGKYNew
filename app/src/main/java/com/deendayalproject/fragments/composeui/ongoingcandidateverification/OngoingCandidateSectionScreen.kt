package com.deendayalproject.fragments.composeui.ongoingcandidateverification

import android.content.Context
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deendayalproject.BuildConfig
import com.deendayalproject.fragments.composeui.common.ComplianceStatus
import com.deendayalproject.fragments.composeui.common.ExpandableComplianceCard
import com.deendayalproject.fragments.composeui.common.PremiumCandidateHeader
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.model.request.GetAttendanceDetailsReq
import com.deendayalproject.model.request.OngoingSubmitBasicRecordsReq
import com.deendayalproject.model.response.CandidateProofItem
import com.deendayalproject.model.response.ExpandableSectionName
import com.deendayalproject.model.uistate.InspectionSectionStatusUiState
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.CandidateAssessmentViewModel
import com.deendayalproject.viewmodel.InspectionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun OngoingCandidateSectionScreen(
    context: Context,
    candidateVerificationViewModel:CandidateAssessmentViewModel,
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

    val sectionStatus by candidateVerificationViewModel.uiSectionStatus.collectAsState()



    var showUpdateDialog by remember { mutableStateOf(false) }
    var pendingSection by remember { mutableStateOf<String?>(null) }

    val sections = remember {

        val list = mutableListOf(

            ExpandableSectionName("Basic Records Verification", ComplianceStatus.NotCOMPLETE),
            ExpandableSectionName("Validate Attendance", ComplianceStatus.COMPLETE),
            ExpandableSectionName("Assessment", ComplianceStatus.NotCOMPLETE),
            ExpandableSectionName("Distribution of Teaching-Learning Material", ComplianceStatus.COMPLETE),
            ExpandableSectionName("Entitlements Distribution", ComplianceStatus.COMPLETE)

        )

        if (AppUtil.getSavedCenterTypePreference(context) == "Residential") {

            list.add(
                ExpandableSectionName(
                    "Residential Facility Verification",
                    ComplianceStatus.NotCOMPLETE
                )
            )
        }

        list
    }

    val expandedSections = remember {
        mutableStateMapOf<String, Boolean>()
    }

    LaunchedEffect(candidateId) {

        candidateVerificationViewModel.loadInspectionSectionStatus(
            inspectionId = AppUtil.getSavedInspectionIdPreference(context).toInt(),
            candidateId = candidateId
        )
    }




    val scope = rememberCoroutineScope()

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

                        status = getSectionStatus(section.title,sectionStatus),

                        expanded = expandedSections[section.title] ?: false,

                        onExpandedChange = { expand ->

                            val isCompleted = when (section.title) {
                                "Basic Records Verification" ->
                                    sectionStatus.recordStatus == 1

                                "Validate Attendance" ->
                                    sectionStatus.attendanceStatus == 1

                                "Assessment" ->
                                    sectionStatus.assessmentStatus == 1

                                "Distribution of Teaching-Learning Material" ->
                                    sectionStatus.learningMaterialStatus == 1

                                "Entitlements Distribution" ->
                                    sectionStatus.entitlementsDistributionStatus == 1

                                "Residential Facility Verification" ->
                                    sectionStatus.rfVerificationStatus == 1

                                else -> false
                            }

                            if (expand && isCompleted) {

                                pendingSection = section.title
                                showUpdateDialog = true

                            } else {
                                expandedSections[section.title] = expand
                            }
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
                                    candidateVerificationViewModel,
                                    imageList = imageList,
                                    candidateId = candidateId,
                                    batchId = batchId,
                                    inspectionId = AppUtil.getSavedInspectionIdPreference(context).toInt(),
                                    showMessage = {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            snackbarHostState.showSnackbar(it)
                                        }
                                    }


//                                    onSubmit = { documents ->
//
//                                        val req = OngoingSubmitBasicRecordsReq(
//                                            appVersion = BuildConfig.VERSION_NAME,
//                                            candidateId = candidateId,
//                                            inspectionId = 3,
//                                            batchId = 6,
//
//                                            povertyProofQid = documents[0].qid,
//                                            povertyProof = documents[0].answer ?: "",
//                                            povertyProofRemark = documents[0].remarks,
//
//                                            categoryProofQid = documents[1].qid,
//                                            categoryProof = documents[1].answer ?: "",
//                                            categoryProofRemark = documents[1].remarks,
//
//                                            minorityProofQid = documents[2].qid,
//                                            minorityProof = documents[2].answer ?: "",
//                                            minorityProofRemark = documents[2].remarks,
//
//                                            educationProofQid = documents[3].qid,
//                                            educationProof = documents[3].answer ?: "",
//                                            educationProofRemark = documents[3].remarks,
//
//                                            pwdProofQid = documents[4].qid,
//                                            pwdProof = documents[4].answer ?: "",
//                                            pwdProofRemark = documents[4].remarks
//                                        )
//                                        candidateVerificationViewModel.saveCandidateBasicRecords(req)
//                                        //viewModel.submitBasicRecords(req, "")
//
//                                    }
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
                                    candidateAssesmentViewModel = candidateVerificationViewModel,
                                    snackbarHostState = snackbarHostState,
                                    batchId = batchId,
                                    candidateId=candidateId,
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

                                    }
                                )
                            }



                            "Distribution of Teaching-Learning Material" ->{



                              //  val snackbarHostState = remember { SnackbarHostState() }

                                DistributedLearningSection(
                                    viewModel=candidateVerificationViewModel,
                                    snackbarHostState = snackbarHostState,
                                    batchId = batchId,
                                    candidateId=candidateId
                                )
                                { questions ->
                                    candidateVerificationViewModel.updateDistributedLearningState(questions)

                                    /* -------- SAVE API -------- */
                                    scope.launch {
                                        candidateVerificationViewModel.saveDistributedInspection(
                                            batchId = batchId.toInt(),
                                            inspectionId = AppUtil.getSavedInspectionIdPreference(context).toInt(),
                                            candidateId = candidateId
                                        )
                                        snackbarHostState.showSnackbar("Saved successfully")
                                    }

                                }
                            }


                            "Entitlements Distribution" -> {

                                EntitlementsSection(
                                    viewModel=candidateVerificationViewModel,
                                    snackbarHostState = snackbarHostState,
                                    batchId = batchId,
                                    inspectionId = AppUtil.getSavedInspectionIdPreference(context).toInt(),
                                    candidateId=candidateId,
                                    onSubmit = {
                                            trainingFree,
                                            bankAccount,
                                            residential,
                                            trainingMaterial,
                                            uniform,
                                            sanitary,
                                            medicine,
                                            insurance,
                                            trainingFreeRemark,
                                            bankAccountRemark,
                                            residentialRemark,
                                            trainingMaterialRemark,
                                            uniformRemark,
                                            sanitaryRemark,
                                            medicineRemark,
                                            insuranceRemark ->



                                    }
                                )
                            }


                            "Residential Facility Verification" -> {

                                    ResidentialFacilitySection(
                                        viewModel=candidateVerificationViewModel,
                                        snackbarHostState = snackbarHostState,
                                        batchId = batchId,
                                        candidateId=candidateId,
                                    )

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
    if (showUpdateDialog) {

        AlertDialog(

            onDismissRequest = {
                showUpdateDialog = false
            },

            title = {
                Text("Already Verified")
            },

            text = {
                Text("This section is already verified. Do you want to update it again?")
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        pendingSection?.let {
                            expandedSections[it] = true
                        }

                        showUpdateDialog = false
                    }

                ) {
                    Text("Update")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = { showUpdateDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }


}

fun getSectionStatus(sectionTitle: String,sectionStatus: InspectionSectionStatusUiState): ComplianceStatus {

    val completed = when (sectionTitle) {

        "Basic Records Verification" -> sectionStatus.recordStatus
        "Validate Attendance" -> sectionStatus.attendanceStatus
        "Assessment" -> sectionStatus.assessmentStatus
        "Distribution of Teaching-Learning Material" -> sectionStatus.learningMaterialStatus
        "Entitlements Distribution" -> sectionStatus.entitlementsDistributionStatus
        "Residential Facility Verification" -> sectionStatus.rfVerificationStatus

        else -> 0
    }

    return if (completed == 1)
        ComplianceStatus.COMPLETE
    else
        ComplianceStatus.NotCOMPLETE
}