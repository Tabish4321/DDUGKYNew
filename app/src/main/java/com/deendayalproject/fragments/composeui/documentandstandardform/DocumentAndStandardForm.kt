package com.deendayalproject.fragments.composeui.documentandstandardform

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionNAWithRemarks
import com.deendayalproject.viewmodel.DocumentMaintainViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.deendayalproject.util.AppUtil

@Composable
fun StandardFormComplianceScreen(
    viewModel: DocumentMaintainViewModel,
    snackbarHostState: SnackbarHostState
) {

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val inspectionId =
        AppUtil.getSavedInspectionIdPreference(context).toInt()

    val state by viewModel.inspectionStandardFormState.collectAsState()

    val questions = remember {
        listOf(
            "Is SF 6.1.2A: Non-Domain Training - English content available?",
            "Is SF 6.1.2B: Non-Domain Training - IT ?",
            "Is SF 6.1.2C: Non-Domain Training - Soft skills content available?",
            "Is document available as per para 6.1.1 for Domain curriculum content and equipment available?",
            "SF 6.1.2 D - Non- Domain Entrepreneurship content",
            "Is SF 4.1C: Activity cum lesson planner available?",
            "Is SF 4.5A: Daily distribution of Tablet computers available?",
            "Is SF 4.2A: Look and feel of a training centre available?",
            "Is SF 4.2B: Training centre name board available?",
            "Is SF 4.2C: Hostel centre name board available?",
            "Is SF 4.2D: Activity summary and achievement board available?",
            "Is SF 4.2E: Contact details of important people available?",
            "Is SF 4.2F: Basic Information Board (Training Centre) available?",
            "Is SF 4.2G: Basic Information Board (Residential Centre) available?",
            "Is SF 4.2H: Academic Information Board available?",
            "Is SF 4.2I: Living Area Information Board available?",
            "Is SF 4.2J: Code of conduct for candidates available?",
            "Is SF 4.2K: Food specifications (residential centre) available?",
            "IS SF 4.2M: Student entitlement board and responsibilities board available?",
            "Is SF 4.2N: Student Attendance Summary Information Board available?",
            "Is SF 4.2S: Welcome kit to trainees candidates available?",
            "Is SF 4.2T: First-aid kit available?",
            "Is SF 4.2O: Due diligence of a training centre available?",
            "Is SF 4.2P: Due diligence for residential facilities available?",
            "Is SF 4.2W: Candidate ID template available?",
            "Is SF 4.2X: Index of individual candidate dossier available?",
            "Is SF 4.2Y: Parents' consent form available?",
            "Is SF 4.1A: Plan of training available?",
            "Is SF 4.2Z: Attendance registers for candidates available?",
            "Is SF 4.2AA: Attendance registers for trainers available?",
            "Is SF 4.2B: Checklist of items given to candidates available?",
            "Is SF 4.2AF: List of equipment in the training centre available?",
            "Is SF 4.2AG: List of equipment available in the accommodation facilities?",
            "Is SF 5.1A: Daily failure items report available?",
            "Is Overview of aptitude test available?",
            "Is SF 4.2V: Trainers' profile available?",
            "Is SF 4.1B: Finishing and work readiness module available?",
            "Is SF 4.2U: Summary of staff deployed available?",
            "Is SF 4.3H: On the job training plan available?",
            "Is SF 4.2AC: TA/DA calculation record available?",
            "Is SF 4.3G: Drop out analysis Form available?",
            "Has capacity building session conducted for Mobiliser?",
            "Has counselling conducted for candidates ?",
            "SF 5.1D Health Standards of Candidates",
            "SF 5.1C Checklist for cleanliness of Training Centre and Hostel"
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadInspectionStandardForm(inspectionId)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


                itemsIndexed(questions) { index, q ->

                    val answer = state.answers.getOrNull(index)
                    val remark = state.remarks.getOrNull(index) ?: ""

                    ComplianceQuestionNAWithRemarks(
                        question = q,
                        answer = answer,
                        remarks = remark,
                        isError = answer == null || (answer == "No" && remark.isBlank()),
                        onAnswerChange = {
                            viewModel.updateStandardAnswer(index, it)
                        },
                        onRemarksChange = {
                            viewModel.updateStandardRemark(index, it)
                        }
                    )
                }

                item {

                    Button(
                        onClick = {

                            focusManager.clearFocus()

                            val answers = state.answers
                            val remarks = state.remarks

                            for (i in questions.indices) {

                                val ans = answers.getOrNull(i)

                                if (ans == null) {

                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Please select: ${questions[i]}"
                                        )
                                    }
                                    return@Button
                                }

                                if (
                                    ans == "No" &&
                                    remarks.getOrNull(i).isNullOrBlank()
                                ) {

                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Remarks required for: ${questions[i]}"
                                        )
                                    }
                                    return@Button
                                }
                            }

                            viewModel.saveInspectionStandardForm(inspectionId)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Submit")
                    }
                }
            }

        }
    }

