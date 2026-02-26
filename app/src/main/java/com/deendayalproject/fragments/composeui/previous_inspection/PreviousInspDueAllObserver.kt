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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.deendayalproject.R
import com.deendayalproject.model.response.PreviousObservationRes
import com.deendayalproject.model.response.PreviousObservationUiState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.draw.rotate
import androidx.compose.material.icons.filled.KeyboardArrowDown
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.fragments.composeui.common.InfoRow
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.fragments.composeui.common.ShimmerTrainingList


@Composable
fun PreviousInspectionDueAllObserver(
    observationItems: List<PreviousObservationRes>,
    onBackClick: () -> Unit,
    isLoading: Boolean,
    onSubmit: (PreviousObservationUiState) -> Unit
)
{

    val observationList = remember {
        mutableStateListOf<PreviousObservationUiState>().apply {
            addAll(
                observationItems.map {
                    PreviousObservationUiState(
                        conductedBy = it.conductedBy,
                        title = it.title,
                        originalRemarks = it.remarks
                    )
                }
            )
        }
    }

    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            PremiumTopBar(
                dynamicTitle = "Inspection/Due Diligence",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->



        if (isLoading) {

            Box(modifier = Modifier.padding(innerPadding)) {
                ShimmerTrainingList()
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(colorResource(R.color.white)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            )
            {

                itemsIndexed(observationList) { index, item ->

                    val isExpanded = expandedIndex == index
                    val rotation by animateFloatAsState(
                        targetValue = if (isExpanded) 180f else 0f,
                        label = ""
                    )

                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedIndex =
                                    if (isExpanded) null else index
                            },
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = colorResource(id = R.color.white)
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            // 🔹 Header Row (Title + Arrow)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    modifier = Modifier.rotate(rotation)
                                )
                            }

                            InfoRow(
                                icon = Icons.Default.CheckCircle,
                                label = "Conducted By",
                                value = item.conductedBy
                            )

                            InfoRow(
                                icon = Icons.Default.PendingActions,
                                label = "Observation",
                                value = item.originalRemarks
                            )

                            AnimatedVisibility(visible = isExpanded) {

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {

                                    ComplianceQuestionWithRemarks(
                                        question = "Are previous inspection observations complied?",
                                        answer = item.selectionYesNo,
                                        remarks = item.inputRemarks,
                                        onAnswerChange = { newAnswer ->
                                            observationList[index] =
                                                item.copy(selectionYesNo = newAnswer)
                                        },
                                        onRemarksChange = { newRemarks ->
                                            observationList[index] =
                                                item.copy(inputRemarks = newRemarks)
                                        }
                                    )

                                    Button(
                                        onClick = {

                                            val currentItem = observationList[index]

                                            if (currentItem.selectionYesNo.isNullOrBlank())
                                                return@Button

                                            if (currentItem.selectionYesNo == "No" &&
                                                currentItem.inputRemarks.isBlank()
                                            ) return@Button

                                            onSubmit(currentItem)

                                            expandedIndex = null
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Submit")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
