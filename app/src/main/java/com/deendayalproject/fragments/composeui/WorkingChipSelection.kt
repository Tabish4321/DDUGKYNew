package com.deendayalproject.fragments.composeui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WorkingChipSelection(
    selected: String?,
    onSelected: (String) -> Unit
) {

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

        PremiumChoiceChip(
            text = "Working",
            selected = selected == "Working",
            selectedColor = Color(0xFF2E7D32),
            onClick = { onSelected("Working") },
            modifier = Modifier.weight(1f)
        )

        PremiumChoiceChip(
            text = "Not Working",
            selected = selected == "Not Working",
            selectedColor = Color(0xFFC62828),
            onClick = { onSelected("Not Working") },
            modifier = Modifier.weight(1f)
        )
    }
}
