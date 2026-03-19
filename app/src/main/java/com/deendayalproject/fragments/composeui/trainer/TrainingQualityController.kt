package com.deendayalproject.fragments.composeui.trainer

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deendayalproject.BuildConfig
import com.deendayalproject.model.request.SubjectDeleteReq
import com.deendayalproject.model.request.SubjectReq
import com.deendayalproject.model.response.SubjectListData
import com.deendayalproject.model.response.TrainerData
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.InspectionViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingQualityController(
    trainerList: List<TrainerData>,
    viewModel: InspectionViewModel,
    snackbarHostState: SnackbarHostState,
    showForm: Boolean,
    onShowFormChange: (Boolean) -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val subjectResponse by viewModel.getSubjectList.collectAsState()
    val deleteResponse by viewModel.deleteSubjectItem.collectAsState()

    val inspectionId = remember {
        AppUtil.getSavedInspectionIdPreference(context)
    }

//    val subjects = remember {
//        listOf("IT", "Soft Skills", "English", "Domain", "Entrepreneurship")
//    }

    var selectedTrainer by remember { mutableStateOf<TrainerData?>(null) }
    var deletingSubjectId by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val addedSubjects by remember(subjectResponse) {
        derivedStateOf { subjectResponse?.wrappedList ?: emptyList() }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )

    /* ---------------- API REFRESH ---------------- */

    fun refreshSubjects() {

        isLoading = true

        viewModel.getSubjectList(
            SubjectReq(
                appVersion = BuildConfig.VERSION_NAME,
                inspectionId = inspectionId
            ),
            "Bearer token"
        )
    }

    /* ---------------- INITIAL LOAD ---------------- */

    LaunchedEffect(Unit) {

        if (subjectResponse == null) {
            refreshSubjects()
        }
    }

    /* ---------------- STOP LOADING ---------------- */

    LaunchedEffect(subjectResponse) {
        if (isLoading) isLoading = false
    }

    /* ---------------- DELETE RESPONSE ---------------- */

    LaunchedEffect(deleteResponse?.responseCode) {

        if (deleteResponse?.responseCode == 200) {

            deletingSubjectId = null
            Toast.makeText(
                context,
                "Subject Deleted",
                Toast.LENGTH_SHORT
            ).show()

            refreshSubjects()

            viewModel.clearDeleteSubjectResponse()
        }
    }

    /* ---------------- BACK HANDLER ---------------- */

    BackHandler(enabled = showForm) {
        onShowFormChange(false)
    }

    /* ---------------- MAIN UI ---------------- */

    Box {

        if (isLoading && addedSubjects.isEmpty()) {
           CircularProgressIndicator()

        } else {
            SubjectListSection(
                subjects = trainerList,
                subjectData = addedSubjects,
                selectedSubject = selectedTrainer?.trainerWithSubject ?: "",
                deletingSubjectId = deletingSubjectId,
                onSubjectSelect = { trainer ->
                    selectedTrainer = trainer
                },

                onAddClick = {

                    if (selectedTrainer == null) {
                        Toast.makeText(context, "Please select trainer", Toast.LENGTH_SHORT).show()
                    } else {
                        onShowFormChange(true)
                    }
                },

                onDelete = { subject ->
                    deletingSubjectId = subject.subjectId
                    isLoading = true

                    viewModel.deleteSubjectItem(
                        SubjectDeleteReq(
                            appVersion = BuildConfig.VERSION_NAME,
                            subjectId = subject.subjectId
                        ),
                        "Bearer token"
                    )
                }
            )
        }

        /* ---------------- LOADING OVERLAY ---------------- */

        if (isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        }
    }

    /* ---------------- BOTTOM SHEET ---------------- */

    if (showForm) {

        LaunchedEffect(showForm) {
            sheetState.expand()
        }

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {},
            containerColor = Color.White,
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = true,
                shouldDismissOnClickOutside = false
            ),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            tonalElevation = 6.dp,
            scrimColor = Color.Black.copy(alpha = 0.35f),
            contentWindowInsets = { WindowInsets(0) }

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
                            viewModel = viewModel,
                            snackbarHostState = snackbarHostState,
                            inspectionId = inspectionId.toInt(),
                            trainerData = selectedTrainer!!, // ✅ full object

                            onClose = { msg ->

                                Toast.makeText(
                                    context,
                                    msg,
                                    Toast.LENGTH_SHORT
                                ).show()


                                scope.launch {

                                    onShowFormChange(false)

                                    sheetState.hide()

                                    refreshSubjects()

                                }

                            }
                        )

                    }

                }

            }

        }

    }

}


@Preview(showBackground = true)
@Composable
fun TrainingQualityControllerStaticPreview() {

    val dummySubjects = listOf(
        SubjectListData("1", "IT","Risisi"),
        SubjectListData("2", "English","porwal")
    )

    MaterialTheme {
        SubjectListSection(
            subjects = emptyList(),
            subjectData = dummySubjects,
            selectedSubject = "IT",
            deletingSubjectId = null,
            onSubjectSelect = {},
            onAddClick = {},
            onDelete = {}
        )
    }
}