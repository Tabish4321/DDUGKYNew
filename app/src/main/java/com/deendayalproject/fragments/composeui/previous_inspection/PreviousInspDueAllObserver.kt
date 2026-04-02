package com.deendayalproject.fragments.composeui.previous_inspection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deendayalproject.model.response.PreviousObservationRes
import com.deendayalproject.model.response.PreviousObservationUiState
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.fragments.composeui.common.InfoRow
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.fragments.composeui.common.ShimmerTrainingList


@Composable
fun PreviousInspectionDueAllObserver(
    observationItems: List<PreviousObservationRes>,
    onBackClick: () -> Unit,
    isLoading: Boolean,
    expandedIndex: Int?,
    onExpandChange: (Int?) -> Unit,
    onExpand: (Int) -> Unit,
    onSubmit: (PreviousObservationUiState) -> Unit
) {

    val observationList = observationItems.map {
        PreviousObservationUiState(
            questionId = it.questionId,
            conductedBy = it.conductedBy,
            title = it.title,
            originalRemarks = it.remarks,
            selectionYesNo = it.preAnswer,
            inputRemarks = it.preRemark ?: ""
        )
    }.toMutableStateList()

    Scaffold(
        topBar = {
            PremiumTopBar(
                dynamicTitle = "Due Diligence",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->

        if (isLoading) {
            ShimmerTrainingList()
        } else if(observationList.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Data Available",
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White),

                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                itemsIndexed(observationList) { index, item ->

                    val isExpanded = expandedIndex == index

                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isExpanded) {
                                    onExpandChange(null)
                                } else {
                                    onExpandChange(index)
                                    onExpand(item.questionId)
                                }
                            }
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                                    ComplianceQuestionWithRemarks(
                                        question = item.title,
                                        answer = item.selectionYesNo,
                                        remarks = item.inputRemarks,
                                        onAnswerChange = {
                                            observationList[index] =
                                                item.copy(selectionYesNo = it)
                                        },
                                        onRemarksChange = {
                                            observationList[index] =
                                                item.copy(inputRemarks = it)
                                        }
                                    )
                                    Spacer(Modifier.height(10.dp))

                                    Button(
                                        onClick = { onSubmit(observationList[index]) },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Save")
                                    }
                            }
                        }
                    }
                }
            }
        }
    }
