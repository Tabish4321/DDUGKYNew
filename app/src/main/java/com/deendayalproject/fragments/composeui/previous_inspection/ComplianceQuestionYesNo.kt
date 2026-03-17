package com.deendayalproject.fragments.composeui.previous_inspection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.MultiLineEditText

@Composable
fun ComplianceQuestionYesNoOnly(
    answer: String?,
    remarks: String,
    onAnswerChange: (String) -> Unit,
    onRemarksChange: (String) -> Unit
) {

    Column {

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            AnswerButton(
                text = "Yes",
                selected = answer == "Yes",
                color = Color(0xFF22C55E),
                weight = 1f
            ) { onAnswerChange("Yes") }

            AnswerButton(
                text = "No",
                selected = answer == "No",
                color = Color(0xFFEF4444),
                weight = 1f
            ) { onAnswerChange("No") }
        }

        if (answer == "No") {

            MultiLineEditText(
                value = remarks,
                onValueChange = onRemarksChange,
                label = "Remarks",
                isRequired = true,
                isError = remarks.isBlank()
            )
        }
    }
}


@Composable
private fun RowScope.AnswerButton(
    text: String,
    selected: Boolean,
    color: Color,
    weight: Float,
    onClick: () -> Unit
) {

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .weight(weight)
            .heightIn(min = 40.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) color.copy(alpha = 0.15f) else Color.Transparent,
            contentColor = color
        ),
        border = ButtonDefaults.outlinedButtonBorder
    ) {

        Text(
            text = text,
            textAlign = TextAlign.Center,
            maxLines = 2,
            style = MaterialTheme.typography.labelMedium
        )

    }

}