package com.deendayalproject.fragments.composeui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumericTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isRequired: Boolean = false,
    isError: Boolean = false,
    placeholder: String = ""
) {

    OutlinedTextField(
        value = value,
        onValueChange = { input ->

            // 🔥 Only digits allowed
            if (input.all { it.isDigit() }) {
                onValueChange(input)
            }
        },
        label = {
            Text(if (isRequired) "$label *" else label)
        },
        placeholder = {
            Text(placeholder)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        singleLine = true,
        isError = isError,
        modifier = Modifier.fillMaxWidth()
    )
}