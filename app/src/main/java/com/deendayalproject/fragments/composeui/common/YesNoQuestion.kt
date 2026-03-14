package com.deendayalproject.fragments.composeui.common

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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                text = question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (isError)
                    MaterialTheme.colorScheme.error
                else
                    Color(0xFF111827)
            )

            PremiumSelector(
                options = listOf(
                    PremiumOption("Yes", Color(0xFF22C55E)),
                    PremiumOption("No", Color(0xFFEF4444))
                ),
                selected = answer,
                onSelect = onAnswerChange
            )

            if (answer == "No") {
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