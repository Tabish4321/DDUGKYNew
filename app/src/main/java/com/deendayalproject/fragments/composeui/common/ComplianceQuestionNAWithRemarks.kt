package com.deendayalproject.fragments.composeui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ComplianceQuestionNAWithRemarks(
    modifier: Modifier = Modifier,
    question: String,
    answer: String?,
    remarks: String,
    isError: Boolean = false,
    onAnswerChange: (String) -> Unit,
    onRemarksChange: (String) -> Unit
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()


    ElevatedCard(
        modifier = modifier.fillMaxWidth() .bringIntoViewRequester(bringIntoViewRequester),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                text = question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
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

//                AnswerButton(
//                    text = "Not Applicable",
//                    selected = answer == "Not Applicable",
//                    color = Color(0xFFF59E0B),
//                    weight = 1.6f   // 👈 wider button
//                ) { onAnswerChange("Not Applicable") }

            }

          //  if (answer == "No" || answer == "Not Applicable") {
            if (answer == "No") {
                MultiLineEditText(
                    value = remarks,
                    onValueChange = onRemarksChange,
                    label = "Remarks",
                    isRequired = answer == "No",
                    isError = answer == "No" && remarks.isBlank(),
                    modifier = Modifier.onFocusEvent {
                        if (it.isFocused) {
                            scope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    }
                )

            }

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