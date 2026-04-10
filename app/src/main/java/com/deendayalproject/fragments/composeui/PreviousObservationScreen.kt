package com.deendayalproject.fragments.composeui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

    val previouseInspectionId = remember {
        AppUtil.getPreviouseSavedInspectionIdPreference(context)
    }

    val trainingCenterId = remember {
        AppUtil.getSavedTrainingCenterIdPreference(context).toInt()
    }


    LaunchedEffect(Unit) {
        val id = previouseInspectionId.toIntOrNull()
        if (id != null) {
            viewModel.loadObservation(id)
        } else {
            Toast.makeText(context, "Invalid ID:${previouseInspectionId}", Toast.LENGTH_SHORT).show()
            //Log.e("PreviousObservation", "Invalid ID: $previouseInspectionId")
        }
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

                        items(itemsList) { item ->

                            val answer = state.answers[item.questionId ?: 0]

                            val remark = state.remarks[item.questionId ?: 0]
                                ?: item.remark
                                ?: ""

                            Card(
                                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(14.dp),
                                elevation = CardDefaults.elevatedCardElevation(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp)
                                ) {

                                    // Section Name
                                    Text(
                                        text = item.sactionName ?: "",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    // Question
                                    Text(
                                        text = item.question ?: "",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(Modifier.height(10.dp))
                                    Text(
                                        text = "Remarks:" ,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Bold
                                    )

                                    // Remark Box
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                Color.White,
                                                RoundedCornerShape(10.dp)
                                            )
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            text = remark.ifEmpty { "No remarks added" },
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
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
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(list) { item ->

                        val answer = state.answers[item.questionId ?: 0]

                        val remark =
                            state.remarks[item.questionId ?: 0]
                                ?: item.remark
                                ?: ""

                        Card(
                            colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(14.dp),
                            elevation = CardDefaults.elevatedCardElevation(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {

                                Text(
                                    text = item.sactionName ?: "",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(Modifier.height(6.dp))

                                Text(
                                    text = item.question ?: "",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(Modifier.height(10.dp))
                                Text(
                                    text = "Remarks:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Bold
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color.White,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text =  remark.ifEmpty { "No remarks added" },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                /* ---------------- FINAL SUBMIT ---------------- */

                item {
                    Spacer(Modifier.height(10.dp))
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