package com.deendayalproject.fragments.composeui.trainer

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deendayalproject.fragments.composeui.TrainerInfoCard
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.fragments.composeui.ongoingcandidateverification.PremiumSubmitButton
import com.deendayalproject.model.response.TrainerData
import com.deendayalproject.viewmodel.InspectionViewModel
import kotlinx.coroutines.launch

@Composable
fun TrainingQualitySection(
    viewModel: InspectionViewModel,
    snackbarHostState: SnackbarHostState,
    inspectionId: Int,
    trainerData: TrainerData,
    onClose: (String) -> Unit

) {

    val scope = rememberCoroutineScope()
    val context= LocalContext.current

    val state by viewModel
        .trainerClassObservationState
        .collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.clearTrainerClassObservationSuccess()
    }
    /* ----------------------------- */
    /* Answers */
    /* ----------------------------- */

    var facingClass by remember { mutableStateOf<String?>(null) }
    var addressingAllCandidates by remember { mutableStateOf<String?>(null) }
    var lessonPlanCovered by remember { mutableStateOf<String?>(null) }
    var maintainsDiscipline by remember { mutableStateOf<String?>(null) }
    var confidentCommunication by remember { mutableStateOf<String?>(null) }
    var teachesWithoutMaterial by remember { mutableStateOf<String?>(null) }
    var usesAudioVisualAids by remember { mutableStateOf<String?>(null) }
    var sessionInteractive by remember { mutableStateOf<String?>(null) }
    var encouragesQuestions by remember { mutableStateOf<String?>(null) }
    var answersQueriesClearly by remember { mutableStateOf<String?>(null) }
    var usesStoriesExamples by remember { mutableStateOf<String?>(null) }
    var conductsInternalAssessments by remember { mutableStateOf<String?>(null) }
    var evaluatesPerformance by remember { mutableStateOf<String?>(null) }
    var guidesCareerProgression by remember { mutableStateOf<String?>(null) }

    /* ----------------------------- */
    /* Remarks */
    /* ----------------------------- */

    var facingClassRemark by remember { mutableStateOf("") }
    var addressingAllCandidatesRemark by remember { mutableStateOf("") }
    var lessonPlanCoveredRemark by remember { mutableStateOf("") }
    var maintainsDisciplineRemark by remember { mutableStateOf("") }
    var confidentCommunicationRemark by remember { mutableStateOf("") }
    var teachesWithoutMaterialRemark by remember { mutableStateOf("") }
    var usesAudioVisualAidsRemark by remember { mutableStateOf("") }
    var sessionInteractiveRemark by remember { mutableStateOf("") }
    var encouragesQuestionsRemark by remember { mutableStateOf("") }
    var answersQueriesClearlyRemark by remember { mutableStateOf("") }
    var usesStoriesExamplesRemark by remember { mutableStateOf("") }
    var conductsInternalAssessmentsRemark by remember { mutableStateOf("") }
    var evaluatesPerformanceRemark by remember { mutableStateOf("") }
    var guidesCareerProgressionRemark by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }

    /* ----------------------------- */
    /* ERROR SNACKBAR */
    /* ----------------------------- */

    LaunchedEffect(state.error) {

        state.error?.let {

            //snackbarHostState.showSnackbar(it)
            Toast.makeText(
                context,
                it,
                Toast.LENGTH_SHORT
            ).show()

            viewModel.clearTrainerClassObservationError()
        }
    }



    /* ----------------------------- */
    /* SUCCESS */
    /* ----------------------------- */

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            val message = state.error ?: "Saved Successfully"
            viewModel.clearTrainerClassObservationSuccess()
            onClose(message)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .navigationBarsPadding()
            .padding(horizontal = 6.dp, vertical = 8.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {

        /* ----------------------------- */
        /* LOADER */
        /* ----------------------------- */

        if (state.isLoading) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }


        } else {

           // TrainerInfoCard(trainerData = trainerData)

            /* ----------------------------- */
            /* QUESTIONS */
            /* ----------------------------- */


            ComplianceQuestionWithRemarks(
                question = "Trainer facing class ?",
                answer = facingClass,
                remarks = facingClassRemark,
                isError = showError && facingClass == null,
                onAnswerChange = { facingClass = it },
                onRemarksChange = { facingClassRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Trainer addressing all candidates ?",
                answer = addressingAllCandidates,
                remarks = addressingAllCandidatesRemark,
                isError = showError && addressingAllCandidates == null,
                onAnswerChange = { addressingAllCandidates = it },
                onRemarksChange = { addressingAllCandidatesRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Session covers plan as per lesson planner ?",
                answer = lessonPlanCovered,
                remarks = lessonPlanCoveredRemark,
                isError = showError && lessonPlanCovered == null,
                onAnswerChange = { lessonPlanCovered = it },
                onRemarksChange = { lessonPlanCoveredRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Maintains class discipline ?",
                answer = maintainsDiscipline,
                remarks = maintainsDisciplineRemark,
                isError = showError && maintainsDiscipline == null,
                onAnswerChange = { maintainsDiscipline = it },
                onRemarksChange = { maintainsDisciplineRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Trainer confident in communication ?",
                answer = confidentCommunication,
                remarks = confidentCommunicationRemark,
                isError = showError && confidentCommunication == null,
                onAnswerChange = { confidentCommunication = it },
                onRemarksChange = { confidentCommunicationRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Teaches without constantly referring materials ?",
                answer = teachesWithoutMaterial,
                remarks = teachesWithoutMaterialRemark,
                isError = showError && teachesWithoutMaterial == null,
                onAnswerChange = { teachesWithoutMaterial = it },
                onRemarksChange = { teachesWithoutMaterialRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Uses audiovisual aids in ≥50% classes ?",
                answer = usesAudioVisualAids,
                remarks = usesAudioVisualAidsRemark,
                isError = showError && usesAudioVisualAids == null,
                onAnswerChange = { usesAudioVisualAids = it },
                onRemarksChange = { usesAudioVisualAidsRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Session interactive / participatory ?",
                answer = sessionInteractive,
                remarks = sessionInteractiveRemark,
                isError = showError && sessionInteractive == null,
                onAnswerChange = { sessionInteractive = it },
                onRemarksChange = { sessionInteractiveRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Encourages candidate questions ?",
                answer = encouragesQuestions,
                remarks = encouragesQuestionsRemark,
                isError = showError && encouragesQuestions == null,
                onAnswerChange = { encouragesQuestions = it },
                onRemarksChange = { encouragesQuestionsRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Answers queries clearly ?",
                answer = answersQueriesClearly,
                remarks = answersQueriesClearlyRemark,
                isError = showError && answersQueriesClearly == null,
                onAnswerChange = { answersQueriesClearly = it },
                onRemarksChange = { answersQueriesClearlyRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Uses stories, pictures, role plays, examples ?",
                answer = usesStoriesExamples,
                remarks = usesStoriesExamplesRemark,
                isError = showError && usesStoriesExamples == null,
                onAnswerChange = { usesStoriesExamples = it },
                onRemarksChange = { usesStoriesExamplesRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Conducts internal assessments on schedule ?",
                answer = conductsInternalAssessments,
                remarks = conductsInternalAssessmentsRemark,
                isError = showError && conductsInternalAssessments == null,
                onAnswerChange = { conductsInternalAssessments = it },
                onRemarksChange = { conductsInternalAssessmentsRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Evaluates performance and provides feedback ?",
                answer = evaluatesPerformance,
                remarks = evaluatesPerformanceRemark,
                isError = showError && evaluatesPerformance == null,
                onAnswerChange = { evaluatesPerformance = it },
                onRemarksChange = { evaluatesPerformanceRemark = it }
            )

            ComplianceQuestionWithRemarks(
                question = "Guides on job readiness & career progression ?",
                answer = guidesCareerProgression,
                remarks = guidesCareerProgressionRemark,
                isError = showError && guidesCareerProgression == null,
                onAnswerChange = { guidesCareerProgression = it },
                onRemarksChange = { guidesCareerProgressionRemark = it }
            )

            Spacer(Modifier.height(10.dp))

            /* ----------------------------- */
            /* SUBMIT BUTTON */
            /* ----------------------------- */

            PremiumSubmitButton {

                showError = true

                scope.launch {

                    val answers = listOf(
                        facingClass,
                        addressingAllCandidates,
                        lessonPlanCovered,
                        maintainsDiscipline,
                        confidentCommunication,
                        teachesWithoutMaterial,
                        usesAudioVisualAids,
                        sessionInteractive,
                        encouragesQuestions,
                        answersQueriesClearly,
                        usesStoriesExamples,
                        conductsInternalAssessments,
                        evaluatesPerformance,
                        guidesCareerProgression
                    )

                    val remarks = listOf(
                        facingClassRemark,
                        addressingAllCandidatesRemark,
                        lessonPlanCoveredRemark,
                        maintainsDisciplineRemark,
                        confidentCommunicationRemark,
                        teachesWithoutMaterialRemark,
                        usesAudioVisualAidsRemark,
                        sessionInteractiveRemark,
                        encouragesQuestionsRemark,
                        answersQueriesClearlyRemark,
                        usesStoriesExamplesRemark,
                        conductsInternalAssessmentsRemark,
                        evaluatesPerformanceRemark,
                        guidesCareerProgressionRemark
                    )

                    val questions = listOf(
                        "Trainer facing class ?",
                        "Trainer addressing all candidates ?",
                        "Session covers plan as per lesson planner ?",
                        "Maintains class discipline ?",
                        "Trainer confident in communication ?",
                        "Teaches without constantly referring materials ?",
                        "Uses audiovisual aids in ≥50% classes ?",
                        "Session interactive ?",
                        "Encourages candidate questions ?",
                        "Answers queries clearly ?",
                        "Uses stories/examples ?",
                        "Conducts internal assessments ?",
                        "Evaluates performance ?",
                        "Guides on career progression ?"
                    )

                    answers.forEachIndexed { index, answer ->

                        if (answer == null) {
                            snackbarHostState.showSnackbar(
                                "Please select: ${questions[index]}"
                            )
                            return@launch
                        }

                        if (answer == "No" && remarks[index].isBlank()) {
                            snackbarHostState.showSnackbar(
                                "Please enter remarks for: ${questions[index]}"
                            )
                            return@launch
                        }
                    }

                    /* ----------------------------- */
                    /* UPDATE STATE */
                    /* ----------------------------- */

                    viewModel.updateTrainerClassObservationState(
                        answers,
                        remarks,
                        trainerData.trainerCode
                    )

                    /* ----------------------------- */
                    /* SAVE API */
                    /* ----------------------------- */

                    viewModel.saveTrainerClassObservation(
                        inspectionId
                    )
                }
            }

            Spacer(modifier = Modifier.height(120.dp))

        }

    }


}