package com.deendayalproject.fragments.composeui.common

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

/**
 * Created by Rishi Porwal
 */

@Composable
fun StatusBadge(isVerified: Boolean) {

    val bgColor =
        if (isVerified) Color(0xFFDCFCE7)
        else Color(0xFFFFF7ED)

    val textColor =
        if (isVerified) Color(0xFF16A34A)
        else Color(0xFFEA580C)

    val text =
        if (isVerified) "Verified"
        else "Pending"

    Surface(
        shape = RoundedCornerShape(50),
        color = bgColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}