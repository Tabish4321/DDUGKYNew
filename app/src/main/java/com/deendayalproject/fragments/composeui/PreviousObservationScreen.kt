package com.deendayalproject.fragments.composeui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionNAWithRemarks
import com.deendayalproject.fragments.composeui.common.ShimmerTrainingList
import com.deendayalproject.viewmodel.CandidateVerificationViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.deendayalproject.fragments.composeui.common.MultiLineEditText
import com.deendayalproject.util.AppUtil


@Composable
fun PreviousObservationScreen(

    viewModel: CandidateVerificationViewModel,

    snackbarHostState: SnackbarHostState,
    onFinalSubmit: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    val scope = rememberCoroutineScope()
    val context=LocalContext.current

    val inspectionId = remember {
        AppUtil.getSavedInspectionIdPreference(context)
    }

    val trainingCenterId = remember {
        AppUtil.getSavedTrainingCenterIdPreference(context).toInt()
    }


    LaunchedEffect(Unit) {

        viewModel.loadObservation(inspectionId.toInt())

    }

//    LaunchedEffect(state.submitSuccess) {
//
//        if (state.submitSuccess) {
//
//            scope.launch {
//                snackbarHostState.showSnackbar("Inspection details saved successfully")
//            }
//
//            viewModel.clearFinalSuccess()
//        }
//
//    }
//
//    LaunchedEffect(state.error) {
//
//        state.error?.let {
//
//            scope.launch {
//                snackbarHostState.showSnackbar(it)
//            }
//
//            viewModel.clearFinalError()
//
//        }
//
//    }



    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if (state.isLoading) {

            ShimmerTrainingList()

        } else {

            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),

                verticalArrangement = Arrangement.spacedBy(14.dp)

            ) {

                /* ---------------- NORMAL SECTIONS ---------------- */

                state.sections
                    .filterKeys { it != "OngoingBatchCandidate" }
                    .forEach { (_, itemsList) ->

                        items(
                            itemsList,
                        ) { item ->

                            val answer = state.answers[item.questionId ?: 0]

                            val remark = state.remarks[item.questionId ?: 0]
                                    ?: item.remark
                                    ?: ""

                            Column {

                                Text(
                                    text = item.sactionName ?: "",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(10.dp))

                                ComplianceQuestionWithRemarksOnly(
                                    modifier = Modifier,
                                    question = item.question ?: "",
                                    remarks = remark,

                                )

                            }

                        }

                    }

                /* ---------------- ONGOING SECTION ---------------- */

                val ongoing =
                    state.sections["OngoingBatchCandidate"]
                        ?: emptyList()

                val grouped =
                    ongoing.groupBy {
                        it.sactionType ?: "Other"
                    }

                grouped.forEach { (type, list) ->

                    item {

                        Text(
                            text = type,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(10.dp))

                    }

                    items(
                        list
                    ) { item ->

                        val answer = state.answers[item.questionId ?: 0]

                        val remark =
                            state.remarks[item.questionId ?: 0]
                                ?: item.remark
                                ?: ""

                        Column {

                            Text(
                                text = item.sactionName ?: "",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(10.dp))
                            ComplianceQuestionWithRemarksOnly(
                                question = item.question ?: "",
                                remarks = remark,
                            )

                        }

                    }

                }

                /* ---------------- FINAL SUBMIT ---------------- */

                item {

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "Final Remark *",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(8.dp))

                    MultiLineEditText(
                        value = viewModel.finalRemark,
                        onValueChange = { viewModel.updateFinalRemark(it) },
                        label = "Enter final inspection remark",
                        isError = viewModel.finalRemark.isBlank()
                    )

//                    if (viewModel.finalRemark.isBlank()) {
//                        Text(
//                            text = "Final remark is mandatory",
//                            color = Color.Red,
//                            style = MaterialTheme.typography.bodySmall
//                        )
//                    }
                }

            }

        }

    }

}