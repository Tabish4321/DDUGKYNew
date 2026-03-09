
package com.deendayalproject.fragments.composeui.trainer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerBottomSheet(

    trainerName: String,
    trainerId: String,
    onDismiss: () -> Unit

) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val answers = remember { mutableStateMapOf<Int, String?>() }
    val remarks = remember { mutableStateMapOf<Int, String>() }

    var showValidation by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {

                TrainerHeader(
                    trainerName = trainerName,
                    trainerId = trainerId.toString(),
                    onCloseClick = onDismiss
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    items(trainerQuestionList) { question ->

                        val answer = answers[question.id]
                        val remark = remarks[question.id] ?: ""

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

                    item {

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

                                scope.launch {
                                    snackbarHostState.showSnackbar("Verification Submitted")
                                }

                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {

                            Text("Submit Verification")

                        }

                    }

                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                    }

                }
            }
        }
    }
}