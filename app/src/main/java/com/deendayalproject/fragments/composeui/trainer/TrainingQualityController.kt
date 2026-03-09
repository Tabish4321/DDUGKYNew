package com.deendayalproject.fragments.composeui.trainer

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deendayalproject.model.request.SubjectItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingQualityController(
    snackbarHostState: SnackbarHostState,
    showForm: Boolean,
    onShowFormChange: (Boolean) -> Unit
) {


    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { sheetValue ->
            sheetValue != SheetValue.Hidden
        }
    )


    val context = LocalContext.current

    val subjects = listOf(
        "IT",
        "Soft Skills",
        "English",
        "Domain",
        "Entrepreneurship"
    )

    val addedSubjects = remember {
        mutableStateListOf(
            SubjectItem("IT"),
            SubjectItem("Soft Skills")
        )
    }

    var selectedSubject by remember { mutableStateOf("") }

    /* Back press when bottomsheet open */
    BackHandler(enabled = showForm) {
        onShowFormChange(false)
    }

    /* ---------------- MAIN SUBJECT UI ---------------- */

    SubjectListSection(

        subjects = subjects,

        subjectData = addedSubjects,

        selectedSubject = selectedSubject,

        onSubjectSelect = {
            selectedSubject = it
        },

        onAddClick = {

            if (selectedSubject.isBlank()) {

                Toast.makeText(
                    context,
                    "Please select subject",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                onShowFormChange(true)

            }

        },

        onDelete = {
            addedSubjects.remove(it)
        }
    )

    /* ---------------- BOTTOM SHEET ---------------- */

    if (showForm) {

        ModalBottomSheet(

            sheetState = sheetState,

            onDismissRequest = {
                // Outside click disable
            },

            containerColor = Color.White,

            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = true,
                shouldDismissOnClickOutside = false
            ),

            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    color = Color.LightGray
                )
            }

        ) {

            Column {

                /* -------- Close Button -------- */

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {

                    IconButton(
                        onClick = {
                            onShowFormChange(false)
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Gray
                        )

                    }

                }

                /* -------- Scroll Content -------- */

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),

                    contentPadding = PaddingValues(bottom = 40.dp)

                ) {

                    item {

                        TrainingQualitySection(
                            snackbarHostState = snackbarHostState,

                            onSubmit = { facingClass,
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
                                         guidesCareerProgression,

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
                                         guidesCareerProgressionRemark ->

                                if (addedSubjects.none { it.subjectName == selectedSubject }) {

                                    addedSubjects.add(
                                        SubjectItem(selectedSubject)
                                    )
                                }

                                onShowFormChange(false)
                                selectedSubject = ""
                            }
                        )

                    }

                }

            }

        }
    }
}