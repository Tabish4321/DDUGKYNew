package com.deendayalproject.fragments.composeui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.input.KeyboardType
@Composable
fun WorkingStatusQuestion(
    answer: String?,
    workingMonths: String,
    notWorkingReason: String,
    isError: Boolean,
    onAnswerChange: (String) -> Unit,
    onWorkingMonthsChange: (String) -> Unit,
    onNotWorkingReasonChange: (String) -> Unit
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
                text = "What is the current status of the candidate",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (isError)
                    MaterialTheme.colorScheme.error
                else
                    Color(0xFF111827)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {


                PremiumSelector(
                    options = listOf(
                        PremiumOption("Working", Color(0xFF22C55E)),
                        PremiumOption("Not Working", Color(0xFFEF4444))
                    ),
                    selected = answer,
                    onSelect = onAnswerChange
                )






            }

            //  If Working
            if (answer == "Working") {

                OutlinedTextField(
                    value = workingMonths,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            onWorkingMonthsChange(input)
                        }
                    },
                    label = { Text("Number of months working") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = isError && workingMonths.isBlank(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF16A34A),
                        unfocusedBorderColor = Color(0xFFE5E7EB),
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            //  If Not Working
            if (answer == "Not Working") {

                MultiLineEditText(
                    value = notWorkingReason,
                    onValueChange = onNotWorkingReasonChange,
                    label = "Reason for leaving the job",
                    isRequired = true,
                    isError = isError && notWorkingReason.isBlank()
                )
            }
        }
    }
}