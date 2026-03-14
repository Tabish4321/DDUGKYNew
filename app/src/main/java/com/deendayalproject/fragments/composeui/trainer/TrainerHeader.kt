package com.deendayalproject.fragments.composeui.trainer


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.deendayalproject.fragments.composeui.common.InfoRow
import kotlin.math.absoluteValue

@Composable
fun TrainerHeader(
    trainerName: String,
    trainerId: String,
    onCloseClick: () -> Unit
) {

    val accentColor = remember(trainerName) {
        generateColorFromName(trainerName)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
        shadowElevation = 8.dp,
        tonalElevation = 2.dp,
        color = Color.White
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.08f),
                            Color.White
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 22.dp)
        ) {

            // 🔹 Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Trainer Details",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )

                IconButton(onClick = onCloseClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color(0xFF6B7280)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                // Avatar
                Surface(
                    shape = CircleShape,
                    color = accentColor.copy(alpha = 0.15f),
                    modifier = Modifier
                        .size(80.dp)
                        .shadow(4.dp, CircleShape)
                ) {

                    Box(contentAlignment = Alignment.Center) {

                        Text(
                            text = trainerName.take(1).uppercase(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }
                }

                Spacer(modifier = Modifier.width(18.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Text(
                        text = trainerName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    InfoRow("Trainer ID", trainerId)
                }
            }
        }
    }
}


private fun generateColorFromName(name: String): Color {

    val palette = listOf(
        // Color(0xFF6366F1), // Indigo
        //  Color(0xFF22C55E), // Green
        Color(0xFF0EA5E9), // Sky Blue
        Color(0xFFF59E0B), // Amber
        Color(0xFFEC4899)  // Pink
    )

    val index = name.hashCode().absoluteValue % palette.size
    return palette[index]
}