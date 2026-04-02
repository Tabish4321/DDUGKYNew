
package com.deendayalproject.fragments.composeui.trainer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.InspectionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerBottomSheet(
    snackbarHostState:SnackbarHostState,
    viewModel: InspectionViewModel,
    trainerName: String,
    trainerId: String,
    trainerCode: Int,
    onDismiss: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val inspectionId = AppUtil.getSavedInspectionIdPreference(context).toInt()

    val state by viewModel.trainerState.collectAsStateWithLifecycle()

    val answers = remember(trainerCode) { mutableStateMapOf<Int, String?>().apply { clear() } }
    val remarks = remember(trainerCode) { mutableStateMapOf<Int, String>().apply { clear() } }


    var showValidation by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { target ->
            target != SheetValue.Hidden
        }
    )

    /* ----------------------------- */
    /* LOAD API */
    /* ----------------------------- */

    LaunchedEffect(inspectionId) {
        answers.clear()
        remarks.clear()
        viewModel.clearSaveState()
        viewModel.loadTrainerAttendance(
            trainerCode.toInt(),
            inspectionId,
        )
    }

    /* ----------------------------- */
    /* PREFILL */
    /* ----------------------------- */

    LaunchedEffect(state.inspectionId,state.isLoading) {
        if (!state.isLoading) {
            answers[1] = state.trainerAttendanceMatch
            answers[2] = state.trainerCounsellingArranged
            answers[3] = state.trainerEntryExitAclp
            answers[4] = state.replacementArrangement


            remarks[1] = state.trainerAttendanceMatchRemark
            remarks[2] = state.trainerCounsellingArrangedRemark
            remarks[3] = state.trainerEntryExitAclpRemark
            remarks[4] = state.replacementArrangementRemark
        }
    }

    /* ----------------------------- */
    /* ERROR SNACKBAR */
    /* ----------------------------- */

    LaunchedEffect(state.error) {

        state.error?.let {

            snackbarHostState.showSnackbar(it)

            viewModel.clearTrainerError()
        }
    }

    /* ----------------------------- */
    /* SUCCESS SNACKBAR */
    /* ----------------------------- */

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            sheetState.hide()
            onDismiss()
            snackbarHostState.showSnackbar("Verification Submitted")
            viewModel.clearTrainerSuccess()
        }
    }

    LaunchedEffect(trainerCode) {
        if (!sheetState.isVisible) {
            sheetState.show()
        }
    }

//    val nestedScrollConnection = remember {
//        object : NestedScrollConnection {}
//    }

    ModalBottomSheet(
        onDismissRequest = {},
        containerColor = Color.White,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        tonalElevation = 6.dp,
        scrimColor = Color.Black.copy(alpha = 0.35f),
        contentWindowInsets = { WindowInsets(0) }
    ) {

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color(0xFFF8FAFC),
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentWindowInsets = WindowInsets(0),
            bottomBar = {

                Surface(
                    shadowElevation = 8.dp,
                    tonalElevation = 2.dp,
                    color = Color.White,
                    modifier = Modifier.navigationBarsPadding()
                ) {

                    Button(
                        onClick = {

                            showValidation = true

                            trainerQuestionList.forEach { question ->

                                val ans = answers[question.id]

                                if (ans == null) {

                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Please answer: ${question.question}"
                                        )
                                    }

                                    return@Button
                                }

                                if (ans == "No") {

                                    val rem = remarks[question.id] ?: ""

                                    if (rem.isBlank()) {

                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "Please enter remarks for: ${question.question}"
                                            )
                                        }

                                        return@Button
                                    }
                                }
                            }

                            viewModel.updateTrainerState(
                                trainerAttendanceMatch =answers[1],
                                trainerCounsellingArranged = answers[2],
                                trainerEntryExitAclp = answers[3],
                                replacementArrangement =answers[4],
                                trainerAttendanceMatchRemark = remarks[1],
                                trainerCounsellingArrangedRemark = remarks[2],
                                trainerEntryExitAclpRemark = remarks[3],
                                replacementArrangementRemark = remarks[4]

                            )

                            viewModel.saveTrainerAttendance(
                                trainerCode,
                                inspectionId
                            )
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                            .navigationBarsPadding(),   //  IMPORTANT FIX

                        shape = RoundedCornerShape(14.dp)
                    ) {

                        Text("Submit Verification")
                    }
                }
            }

        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {

                TrainerHeader(
                    trainerName = trainerName,
                    trainerId = trainerId,
                    onCloseClick = {
                        scope.launch {
                        sheetState.hide()
                        onDismiss()
                    }}
                )

                Spacer(modifier = Modifier.height(16.dp))

                /* ----------------------------- */
                /* LOADER */
                /* ----------------------------- */

                if (state.isLoading) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator()
                    }

                }else{

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(
                            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 80.dp
                        )
                    ) {

                        items(
                            items = trainerQuestionList,
                            key = { it.id }     // 🔥 REQUIRED
                        ) { question ->

//                            val answer = answers[question.id]
//                            val remark = remarks[question.id] ?: ""

                            val answer by remember { derivedStateOf { answers[question.id] } }
                            val remark by remember { derivedStateOf { remarks[question.id] ?: "" } }

                            ComplianceQuestionWithRemarks(

                                question = question.question,

                                answer = answer,

                                remarks = remark,

                                isError = showValidation && answer == null,

                                onAnswerChange = {

                                    answers[question.id] = it
                                },

                                onRemarksChange = {

                                    remarks[question.id] = it
                                }
                            )
                        }
                    }
                }

            }
        }
    }
}