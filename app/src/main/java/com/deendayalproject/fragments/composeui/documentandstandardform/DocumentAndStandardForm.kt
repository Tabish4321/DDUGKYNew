package com.deendayalproject.fragments.composeui.documentandstandardform

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionNAWithRemarks
import kotlinx.coroutines.launch

@Composable
fun StandardFormComplianceScreen(
    onSubmit: (Map<String, Pair<String, String>>) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val questions = listOf(

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
        "IS SF 4.2M: Student entitlement board and responsibilities board available? (In Bilingual)",
        "Is SF 4.2N: Student Attendance and To and Fro Entitlement Summary Information Board available?",
        "Is SF 4.2S: Welcome kit to trainees candidates available?",
        "Is SF 4.2T: First-aid kit available?",
        "Is SF 4.2O: Due diligence of a training centre (excluding residential facilities) available?",
        "Is SF 4.2P: Due diligence for residential facilities available?",
        "Is SF 4.2W: Candidate ID template available? (In Bilingual)",
        "Is SF 4.2X: Index of individual candidate dossier available?",
        "Is SF 4.2Y: Parents' consent form available?",
        "Is SF 4.1A: Plan of training available?",
        "Is SF 4.2Z: Attendance registers for candidates (as per biometric device) available?",
        "Is SF 4.2AA: Attendance registers for trainers (as per biometric device) available?",
        "Is SF 4.2B: Checklist of items given to candidates available?",
        "Is SF 4.2AF: List of equipment in the training centre available?",
        "Is SF 4.2AG: List of equipment available in the trainee's accommodation facilities (applicable for residential training only)?",
        "Is SF 5.1A: Daily failure items report available?",
        "Is Overview of aptitude test available (as per Chapter 3 / Kaushal Apti) ?",
        "Is SF 4.2V: Trainers' profile available?",
        "Is SF 4.1B: Finishing and work readiness module available?",
        "Is SF 4.2U: Summary of staff deployed at the training centre available?",
        "Is SF 4.3H: On the job training plan for the batch available?",
        "Is SF 4.2AC: TA/DA calculation record (batch wise) available? ( To be linked with biometric attendance)",
        "Is SF 4.3G: Drop out analysis Form available?",
        "Has capacity building session conducted for Mobiliser by PIA Management?",
        "Has counselling conducted for candidates ?",
        "SF 5.1D Health Standards of Candidates",
        "SF 5.1C Checklist for cleanliness of Training Centre and Hostel"

    )


    val answers = remember { mutableStateMapOf<String, String>() }
    val remarks = remember { mutableStateMapOf<String, String>() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    ) {

        questions.forEach { q ->

            ComplianceQuestionNAWithRemarks(
                question = q,
                answer = answers[q],
                remarks = remarks[q] ?: "",
                isError = answers[q] == null ||
                        (answers[q] == "No" && remarks[q].isNullOrBlank()),
                onAnswerChange = { answers[q] = it },
                onRemarksChange = { remarks[q] = it }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {

                focusManager.clearFocus()

                for (q in questions) {

                    val ans = answers[q]

                    if (ans == null) {

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Please select: $q"
                            )
                        }

                        return@Button
                    }

                    if (ans == "No" && remarks[q].isNullOrBlank()) {

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Remarks required for: $q"
                            )
                        }

                        return@Button
                    }
                }

                val data = questions.associateWith {
                    Pair(
                        answers[it] ?: "",
                        remarks[it] ?: ""
                    )
                }

                onSubmit(data)

            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Submit")

        }

        Spacer(modifier = Modifier.height(30.dp))

        SnackbarHost(hostState = snackbarHostState)

    }
}

