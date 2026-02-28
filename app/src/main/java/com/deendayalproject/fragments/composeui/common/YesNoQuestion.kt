package com.deendayalproject.fragments.composeui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ComplianceQuestionWithRemarks(
    question: String,
    answer: String?,
    remarks: String,
    isError: Boolean = false,
    onAnswerChange: (String) -> Unit,
    onRemarksChange: (String) -> Unit
) {

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFFF8FAFC)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            Text(
                text = question,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = if (isError)
                    MaterialTheme.colorScheme.error
                else
                    Color(0xFF0F172A)
            )

            PremiumSegmentedSelector(
                selected = answer,
                onSelect = onAnswerChange
            )

            AnimatedVisibility(visible = answer == "No") {
                MultiLineEditText(
                    value = remarks,
                    onValueChange = onRemarksChange,
                    label = "Remarks",
                    isRequired = true,
                    isError = isError && remarks.isBlank()
                )
            }
        }
    }
}