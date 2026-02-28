package com.deendayalproject.fragments.composeui.common

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deendayalproject.model.response.CandidateListInspectionRes
import kotlin.math.absoluteValue

@Composable
fun CandidateHeader(
    candidateData: CandidateListInspectionRes,
    onCloseClick: () -> Unit
) {

    val accentColor = remember(candidateData.name) {
        generateColorFromName(candidateData.name)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        shadowElevation = 6.dp,
        tonalElevation = 2.dp,
        color = Color.White
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.10f),
                            Color.White
                        )
                    )
                )
                .padding(horizontal = 22.dp, vertical = 24.dp)
        ) {

            // 🔹 Top Title Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text(
                        text = "Candidate Verification",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = accentColor
                    )

                    Text(
                        text = "Review and confirm details",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }

                // Premium Close Button
                Surface(
                    onClick = onCloseClick,
                    shape = CircleShape,
                    color = Color(0xFFF1F5F9),
                    tonalElevation = 1.dp,
                    shadowElevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF475569)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // 🔹 Profile Section
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Premium Avatar
                Surface(
                    shape = CircleShape,
                    color = accentColor.copy(alpha = 0.15f),
                    shadowElevation = 4.dp,
                    tonalElevation = 2.dp,
                    modifier = Modifier.size(84.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = candidateData.name
                                .takeIf { it.isNotBlank() }
                                ?.take(1)
                                ?.uppercase() ?: "?",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = candidateData.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    // Metadata grid style
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        InfoRow("Candidate ID", candidateData.candidateId.toString())
                        InfoRow("Roll No", candidateData.rollNumber)
                        InfoRow("Contact", candidateData.contactNumber)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String?) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF64748B)
        )

        Text(
            text = "•",
            color = Color(0xFFCBD5E1)
        )

        Text(
            text = value?.takeIf { it.isNotBlank() } ?: "-",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1E293B)
        )
    }
}

private fun generateColorFromName(name: String): Color {

    val safeName = name.ifBlank { "User" }
    val hash = safeName.hashCode()

    val hue = (hash % 360).absoluteValue.toFloat()
    val saturation = 0.65f
    val lightness = 0.55f

    return Color.hsl(hue, saturation, lightness)
}