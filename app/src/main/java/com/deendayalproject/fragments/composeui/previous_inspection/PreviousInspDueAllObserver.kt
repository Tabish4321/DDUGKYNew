package com.deendayalproject.fragments.composeui.previous_inspection

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deendayalproject.model.response.PreviousObservationRes
import com.deendayalproject.model.response.PreviousObservationUiState
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.deendayalproject.fragments.composeui.common.CommonWebView
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.fragments.composeui.common.EmptyStateView
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.fragments.composeui.common.ShimmerTrainingList
import com.deendayalproject.util.AppUtil.getSavedSanctionOrderInsPreference
import com.deendayalproject.util.AppUtil.getSavedTrainingCenterIdPreference


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
    var showWebView by remember { mutableStateOf(false) }

    val context = LocalContext.current

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
            EmptyStateView("No Due Diligence Data Available")

        }
        else {


            Box(modifier = Modifier.fillMaxSize()
                ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(
                            color =Color.White
                        ),

                ) {

                    //  NOW VISIBLE
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable { showWebView = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Due diligence details",
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )

                            Icon(
                                imageVector = Icons.Default.RemoveRedEye,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
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

                //  WEBVIEW OVERLAY
                if (showWebView) {
                    CommonWebView(
                        url = "https://kaushal.rural.gov.in/#/training-centres-app-details/${getSavedTrainingCenterIdPreference(context)}/${getSavedSanctionOrderInsPreference(context)}/false ",
                        onClose = { showWebView = false }
                    )
                }
            }
        }
        }
    }
