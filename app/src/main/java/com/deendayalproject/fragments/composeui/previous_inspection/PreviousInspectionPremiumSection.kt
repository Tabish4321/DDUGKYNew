package com.deendayalproject.fragments.composeui.previous_inspection

import PreviousInspectionItemResponse
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deendayalproject.BuildConfig
import com.deendayalproject.model.request.InspectionRequestBody
import com.deendayalproject.model.response.DueDiligenceItemResponse
import com.deendayalproject.model.uistate.InspectionTab
import com.deendayalproject.viewmodel.PreviousAndDueViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


@Composable
fun PreviousInspectionSection(
    viewModel: PreviousAndDueViewModel,
    trainingCenterId: String,
    sanctionOrder: String,
    onEditClick: (PreviousInspectionItemResponse) -> Unit
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData(
            InspectionRequestBody(
                appVersion = BuildConfig.VERSION_NAME,
                trainingCenterId = trainingCenterId,
                sanctionOrder = sanctionOrder
            )
        )
    }

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            Text(
                "Previous Inspection & Due Diligence",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )


            TabRow(
                selectedTabIndex = state.selectedTab.ordinal,
                containerColor = Color.Transparent,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(
                            tabPositions[state.selectedTab.ordinal]
                        ),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                Tab(
                    selected = state.selectedTab == InspectionTab.PREVIOUS,
                    onClick = { viewModel.selectTab(InspectionTab.PREVIOUS) },
                    text = { Text("Previous") }
                )
                Tab(
                    selected = state.selectedTab == InspectionTab.DUE_DILIGENCE,
                    onClick = { viewModel.selectTab(InspectionTab.DUE_DILIGENCE) },
                    text = { Text("Due Diligence") }
                )
            }

            AnimatedContent(targetState = state.selectedTab, label = "") {

                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    state.error != null -> {
                        Text(
                            text = state.error!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    else -> {
                        if (it == InspectionTab.PREVIOUS) {
                            PreviousInspectionList(
                                items = state.previousList,
                                onItemClick = onEditClick
                            )
                        } else {
                            DueDiligenceList(
                                items = state.dueDiligenceList,
                                onEditClick = {
                                    viewModel.openDueDiligenceEdit()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PreviousInspectionList(
    items: List<PreviousInspectionItemResponse>,
    onItemClick: (PreviousInspectionItemResponse) -> Unit
) {

    if (items.isEmpty()) {
        EmptyStateView(
            message = "No Previous Inspection Found"
        )
        return
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        items.forEachIndexed { index, item ->

            val animatedAlpha by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = index * 80
                ),
                label = ""
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { alpha = animatedAlpha }
            ) {

                InspectionInfoCard(
                    date = item.inspectionDate,
                    titleLabel = "Inspection Conducted By",
                    titleValue = item.inspectorName,
                    codeLabel = "Inspection Code",
                    codeValue = item.inspectionCode,
                    showEdit = true,
                    onEditClick = { onItemClick(item) }
                )
            }
        }
    }
}



@Composable
fun DueDiligenceList(
    items: List<DueDiligenceItemResponse>,
    onEditClick: () -> Unit
) {

    if (items.isEmpty()) {
        EmptyStateView(
            message = "No Due Diligence Found"
        )
        return
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        items.forEachIndexed { index, item ->

            val animatedAlpha by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = index * 80
                ),
                label = ""
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { alpha = animatedAlpha }
            ) {

                InspectionInfoCard(
                    date = item.verificationDate,
                    titleLabel = "Verified By",
                    titleValue = item.verifierName,
                    codeLabel = "Training Center Code",
                    codeValue = item.trainingCenterCode,
                    showEdit = true,
                    onEditClick = {
                        onEditClick()
                    }
                )
            }
        }
    }
}


@Composable
fun EmptyStateView(
    message: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Default.AssignmentLate,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }




}




