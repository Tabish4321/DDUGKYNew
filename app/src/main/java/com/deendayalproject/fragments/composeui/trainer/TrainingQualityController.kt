package com.deendayalproject.fragments.composeui.trainer

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deendayalproject.BuildConfig
import com.deendayalproject.model.request.SubjectDeleteReq
import com.deendayalproject.model.request.SubjectReq
import com.deendayalproject.model.response.SubjectListData
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.InspectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingQualityController(
    viewModel: InspectionViewModel,
    snackbarHostState: SnackbarHostState,
    showForm: Boolean,
    onShowFormChange: (Boolean) -> Unit
) {

    val context = LocalContext.current

    val subjectResponse by viewModel.getSubjectList.collectAsState()
    val deleteResponse by viewModel.deleteSubjectItem.collectAsState()

    val addedSubjects = subjectResponse?.wrappedList ?: emptyList()

    val subjects = listOf(
        "IT",
        "Soft Skills",
        "English",
        "Domain",
        "Entrepreneurship"
    )

    var selectedSubject by remember { mutableStateOf("") }

    var deletingSubjectId by remember { mutableStateOf<String?>(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    /* -------- API CALL -------- */

    LaunchedEffect(true) {

        if (subjectResponse == null) {

            viewModel.getSubjectList(
                SubjectReq(
                    appVersion = BuildConfig.VERSION_NAME,
                    inspectionId = AppUtil.getSavedInspectionIdPreference(context)
                ),
                "Bearer token"
            )
        }
    }

    /* -------- DELETE RESPONSE -------- */

    LaunchedEffect(deleteResponse?.responseCode) {

        if (deleteResponse?.responseCode == 200) {

            snackbarHostState.showSnackbar("Subject Deleted")

            deletingSubjectId = null

            viewModel.getSubjectList(
                SubjectReq(
                    appVersion = BuildConfig.VERSION_NAME,
                    inspectionId = AppUtil.getSavedInspectionIdPreference(context)
                ),
                "Bearer token"
            )

            viewModel.clearDeleteSubjectResponse()
        }
    }

    BackHandler(enabled = showForm) {
        onShowFormChange(false)
    }

    /* -------- SUBJECT SECTION -------- */

    SubjectListSection(

        subjects = subjects,

        subjectData = addedSubjects,

        selectedSubject = selectedSubject,

        deletingSubjectId = deletingSubjectId,

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

        onDelete = { subject ->

            deletingSubjectId = subject.subjectId

            viewModel.deleteSubjectItem(
                SubjectDeleteReq(
                    appVersion = BuildConfig.VERSION_NAME,
                    subjectId = subject.subjectId
                ),
                "Bearer token"
            )
        }
    )

    /* -------- BOTTOM SHEET -------- */

    if (showForm) {

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {},
            containerColor = Color.White,
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = true,
                shouldDismissOnClickOutside = false
            )
        ) {

            Column {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {

                    IconButton(
                        onClick = { onShowFormChange(false) }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )

                    }

                }

                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 40.dp)
                ) {

                    item {

                        TrainingQualitySection(
                            snackbarHostState = snackbarHostState,
                            onSubmit = { _,_,_,_,_,_,_,_,_,_,_,_,_,_,
                                         _,_,_,_,_,_,_,_,_,_,_,_,_,_ ->

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