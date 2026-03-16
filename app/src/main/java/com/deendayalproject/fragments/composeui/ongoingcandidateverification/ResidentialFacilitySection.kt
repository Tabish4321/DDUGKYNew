package com.deendayalproject.fragments.composeui.ongoingcandidateverification


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.fragments.composeui.common.MultiLineEditText
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.CandidateAssessmentViewModel
import com.deendayalproject.viewmodel.InspectionViewModel
import kotlinx.coroutines.launch

@Composable
fun ResidentialFacilitySection(
    viewModel: CandidateAssessmentViewModel,
    snackbarHostState: SnackbarHostState,
    batchId: String,
    candidateId: String
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val inspectionId = AppUtil.getSavedInspectionIdPreference(context).toInt()

    val state by viewModel.residentialState.collectAsState()

    val questions = listOf(
        "Separate Hostels for Males & Females",
        "Hostel Name Board Available",
        "Contact Details Board Available",
        "Entitlement & Responsibilities Board in bilingual",
        "Basic Information Boards Available",
        "Biometric Attendance Captured Daily (6-10 pm)",
        "Pick-up and Drop Facilities",
        "Grievance Register Maintained",
        "Individual Bed/Mat/Bed Sheet",
        "Kitchen & Dining Hygienic",
        "Dining and Recreation Space Adequate",
        "Toilet Signage Available",
        "Food Quality & Hygiene as per SOP",
        "Food Committee Formed",
        "Food As Per Prescribed Menu",
        "Drinking Water Available",
        "Toilet Hygiene Maintained",
        "Overhead Water Tank Cleaned (every 2 months)",
        "Quarterly Health Check-up Conducted",
        "First Aid Kit as per SOP",
        "Male/Female Doctor On Call Available",
        "Security & Warden Present",
        "Genset Used During Power Cuts",
        "TV with Cable/Satellite Available",
        "Indoor Games Equipment as per SOP",
        "Warden’s Police Verification Completed",
        "Grievance Register Easily Accessible",
        "Grievances Addressed Properly",
        "Hostel Maintained in Hygienic Condition"
    )

    val answers = remember { mutableStateListOf<String?>().apply { repeat(questions.size) { add(null) } } }

    val remarks = remember { mutableStateListOf<String>().apply { repeat(questions.size) { add("") } } }

    var washbasinCount by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }

    /* ----------------------------- */
    /* LOAD API */
    /* ----------------------------- */

    LaunchedEffect(candidateId) {
        viewModel.loadResidentialFacility(
            batchId.toInt(),
            inspectionId,
            candidateId
        )
    }



    LaunchedEffect(state) {
        if (state.answers.isNotEmpty()) {

//            answers.clear()
//            answers.addAll(state.answers)
            answers.indices.forEach { i ->
                answers[i] = state.answers.getOrNull(i)
            }

            remarks.clear()
            remarks.addAll(
                state.remarks.map { it ?: "" }
            )

            washbasinCount = state.washbasins ?: ""
        }
    }

    /* ----------------------------- */
    /* ERROR SNACKBAR */
    /* ----------------------------- */

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearResidentialError()
        }
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            viewModel.triggerRefresh()
            snackbarHostState.showSnackbar("Saved successfully")
            viewModel.clearResidentialSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        /* ----------------------------- */
        /* LOADING */
        /* ----------------------------- */

        if (state.isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }

        }else{
            questions.forEachIndexed { index, question ->

                ComplianceQuestionWithRemarks(

                    question = question,

                    answer = answers[index],

                    remarks = remarks[index],

                    isError = showError && answers[index] == null,

                    onAnswerChange = {

                        answers[index] = it

                    },

                    onRemarksChange = {

                        remarks[index] = it

                    }
                )
            }

            MultiLineEditText(

                value = washbasinCount,

                onValueChange = {

                    washbasinCount = it

                },

                label = "Number of Washbasins",

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            PremiumSubmitButton {

                showError = true

                scope.launch {

                    questions.forEachIndexed { index, question ->

                        when {

                            answers[index] == null -> {

                                snackbarHostState.showSnackbar(
                                    "Please select: $question"
                                )

                                return@launch
                            }

                            answers[index] == "No" &&
                                    remarks[index].isBlank() -> {

                                snackbarHostState.showSnackbar(
                                    "Please enter remarks for $question"
                                )

                                return@launch
                            }
                        }
                    }

                    if (washbasinCount.isBlank()) {

                        snackbarHostState.showSnackbar(
                            "Please enter number of washbasins"
                        )

                        return@launch
                    }

                    /* ----------------------------- */
                    /* UPDATE STATE */
                    /* ----------------------------- */

                    viewModel.updateResidentialState(

                        answers.map { it ?: "" },

                        remarks.toList(),

                        washbasinCount
                    )

                    /* ----------------------------- */
                    /* SAVE API */
                    /* ----------------------------- */

                    viewModel.saveResidentialFacility(

                        batchId.toInt(),

                        inspectionId,

                        candidateId
                    )

                }
            }
        }
    }
}