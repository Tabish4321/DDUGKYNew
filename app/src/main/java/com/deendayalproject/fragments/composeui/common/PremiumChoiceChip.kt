package com.deendayalproject.fragments.composeui.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class PremiumOption(
    val title: String,
    val activeColor: Color
)

@Composable
fun PremiumSelector(
    options: List<PremiumOption>,
    selected: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        options.forEach { option ->

            PremiumOptionCard(
                text = option.title,
                selected = selected == option.title,
                activeColor = option.activeColor,
                onClick = { onSelect(option.title) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PremiumOptionCard(
    text: String,
    selected: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val elevation by animateDpAsState(
        targetValue = if (selected) 8.dp else 2.dp,
        label = ""
    )

    val scale by animateFloatAsState(
        targetValue = if (selected) 1.04f else 1f,
        label = ""
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            width = 1.3.dp,
            color = if (selected)
                activeColor
            else
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        ),
        tonalElevation = elevation,
        color = Color.White,
        modifier = modifier.scale(scale)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color =
                    if (selected) activeColor
                    else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Composable
fun PremiumSegmentedSelector(
    selected: String?,
    onSelect: (String) -> Unit
) {

    val options = listOf("Yes", "No")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFE2E8F0),
                shape = RoundedCornerShape(50)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        options.forEach { option ->

            val isSelected = selected == option

            val backgroundColor by animateColorAsState(
                targetValue =
                    if (isSelected) {
                        if (option == "Yes") Color(0xFF16A34A)
                        else Color(0xFFDC2626)
                    } else Color.Transparent,
                label = ""
            )

            val contentColor by animateColorAsState(
                targetValue =
                    if (isSelected) Color.White
                    else Color(0xFF334155),
                label = ""
            )

            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.02f else 1f,
                label = ""
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .scale(scale)
                    .clip(RoundedCornerShape(50))
                    .background(backgroundColor)
                    .clickable { onSelect(option) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor
                )
            }
        }
    }
}