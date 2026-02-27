package com.deendayalproject.fragments.composeui.previous_inspection

import PreviousInspectionItemResponse
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deendayalproject.BuildConfig
import com.deendayalproject.model.request.InspectionRequestBody
import com.deendayalproject.model.response.DueDiligenceItemResponse
import com.deendayalproject.model.uistate.InspectionTab
import com.deendayalproject.viewmodel.PreviousAndDueViewModel


@Composable
fun PreviousInspectionSection(
    viewModel: PreviousAndDueViewModel,
    trainingCenterId: String,
    sanctionOrder: String
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData(
            InspectionRequestBody(
                appVersion = BuildConfig.VERSION_NAME,
                trainingCenterId =trainingCenterId,
                sanctionOrder = sanctionOrder
            )

        )
    }

    Column(modifier = Modifier.fillMaxSize()) {

        TabRow(
            selectedTabIndex = state.selectedTab.ordinal
        ) {

            Tab(
                selected = state.selectedTab == InspectionTab.PREVIOUS,
                onClick = { viewModel.selectTab(InspectionTab.PREVIOUS) },
                text = { Text("Previous Inspection") }
            )

            Tab(
                selected = state.selectedTab == InspectionTab.DUE_DILIGENCE,
                onClick = { viewModel.selectTab(InspectionTab.DUE_DILIGENCE) },
                text = { Text("Due Diligence") }
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Text(
                        text = state.error ?: "Something went wrong",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                else -> {
                    when (state.selectedTab) {

                        InspectionTab.PREVIOUS -> {
                            PreviousInspectionList(
                                items = state.previousList,
                                onEditClick = {

                                }
                            )
                        }

                        InspectionTab.DUE_DILIGENCE -> {
                            DueDiligenceSection(
                                items = state.dueDiligenceList,
                                onEditClick = {

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
    onEditClick: (PreviousInspectionItemResponse) -> Unit
) {

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = items
        ) { item ->

            InspectionInfoCard(
                date = item.inspectionDate,
                titleLabel = "Inspection Conducted By",
                titleValue = item.inspectorName,
                codeLabel = "Inspection Code",
                codeValue = item.inspectionCode,
                showEdit = true,
                onEditClick = { onEditClick(item) }
            )
        }
    }
}



@Composable
fun DueDiligenceSection(
    items: List<DueDiligenceItemResponse>,
    onEditClick: (DueDiligenceItemResponse) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {

        items(
            items = items
        ) { item ->

            InspectionInfoCard(
                date = item.verificationDate,
                titleLabel = "Verified By",
                titleValue = item.verifierName,
                codeLabel = "Training Center Code",
                codeValue = item.trainingCenterCode,
                showEdit = true,
                onEditClick = { onEditClick(item) }
            )
        }
    }
}






