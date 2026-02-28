package com.deendayalproject.fragments.composeui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)@Composable
fun MultiLineEditText(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isRequired: Boolean = false,
    isError: Boolean = false,
    maxLength: Int = 300,
    placeholder: String = "Write detailed remarks..."
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = if (isRequired) "$label *" else label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = if (isError)
                MaterialTheme.colorScheme.error
            else
                Color(0xFF334155)
        )

        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.length <= maxLength) onValueChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp),

            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF94A3B8)
                )
            },

            shape = RoundedCornerShape(16.dp),
            isError = isError,
            minLines = 4,
            maxLines = 6,

            supportingText = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    if (isError) {
                        Text(
                            text = "Remarks are required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Text(
                        text = "${value.length}/$maxLength",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF64748B)
                    )
                }
            },

            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color(0xFFE2E8F0),
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )
    }
}