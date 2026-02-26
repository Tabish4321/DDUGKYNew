package com.deendayalproject.fragments.composeui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                text = question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (isError)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.onSurface
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                PremiumChoiceChip(
                    text = "Yes",
                    selected = answer == "Yes",
                    selectedColor = Color(0xFF2E7D32),
                    onClick = { onAnswerChange("Yes") },
                    modifier = Modifier.weight(1f)
                )

                PremiumChoiceChip(
                    text = "No",
                    selected = answer == "No",
                    selectedColor = Color(0xFFC62828),
                    onClick = { onAnswerChange("No") },
                    modifier = Modifier.weight(1f)
                )
            }

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