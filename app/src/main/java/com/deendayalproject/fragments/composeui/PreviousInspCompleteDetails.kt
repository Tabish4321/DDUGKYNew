package com.deendayalproject.fragments.composeui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deendayalproject.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviousInspectionComplete(
    dateOfInspection: String,
    conductedBy: String,
    observation: String,
    actionTaken: String,
    remarks: String,
    onSubmit: (String?, String) -> Unit,
    onBackClick: () -> Unit
) {

    var complianceAnswer by remember { mutableStateOf<String?>(null) }
    var enteredRemarks by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            PremiumTopBar(
                dynamicTitle = "Inspection Details",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.white))
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {

                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.elevatedCardElevation(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {

                        InfoRow(Icons.Default.DateRange, "Date of Inspection", dateOfInspection)
                        InfoRow(Icons.Default.Person, "Inspection Conducted By", conductedBy)
                        InfoRow(Icons.Default.Description, "Observations", observation)
                        InfoRow(Icons.Default.PendingActions, "Action Taken", actionTaken)
                        InfoRow(Icons.Default.Badge, "Remarks", remarks)
                    }
                }
            }

            item {
                Divider()
            }

            item {
                Text(
                    text = "Are previous inspection observations complied?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    PremiumChoiceChip(
                        text = "Yes",
                        selected = complianceAnswer == "Yes",
                        selectedColor = Color(0xFF2E7D32),
                        onClick = { complianceAnswer = "Yes" },
                        modifier = Modifier.weight(1f)
                    )

                    PremiumChoiceChip(
                        text = "No",
                        selected = complianceAnswer == "No",
                        selectedColor = Color(0xFFC62828),
                        onClick = { complianceAnswer = "No" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (complianceAnswer == "No") {
                item {

                    MultiLineEditText(
                        value = enteredRemarks,
                        onValueChange = { enteredRemarks = it },
                        isError = enteredRemarks.isBlank()
                    )
                }
            }

            item {

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (complianceAnswer == null) return@Button
                        if (complianceAnswer == "No" && enteredRemarks.isBlank()) return@Button

                        onSubmit(complianceAnswer, enteredRemarks)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Submit")
                }
            }
        }
    }
}







