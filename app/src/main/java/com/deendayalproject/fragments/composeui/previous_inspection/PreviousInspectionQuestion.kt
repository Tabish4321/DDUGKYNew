package com.deendayalproject.fragments.composeui.previous_inspection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.MultiLineEditText
import com.deendayalproject.fragments.composeui.common.PremiumChoiceChip

@Composable
fun ComplianceQuestionItem(
    question: String,
    answer: String?,
    remarks: String,
    onAnswerChange: (String) -> Unit,
    onRemarksChange: (String) -> Unit,
) {

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Text(
            text = question,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
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
                label = "Remarks"

                //isError = remarks.isBlank()
            )
        }


    }
}
