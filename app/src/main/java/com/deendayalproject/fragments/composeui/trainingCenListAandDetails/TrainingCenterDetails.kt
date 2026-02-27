package com.deendayalproject.fragments.composeui.trainingCenListAandDetails


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.InfoRow

@Composable
fun TrainingCenterDetails(
    prnNumber: String,
    sanctionLetter: String,
    inspectionType: String,
    trainingCenterId: String,
    trainingCenterName: String,
    inchargeName: String,
    mobileNumber: String,
    email: String,
    tradeAndCapacity: String,
    coordinate: String,
    roleName: String
) {

    var expanded by remember { mutableStateOf(true) }
    val haptic = LocalHapticFeedback.current

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(400),
        label = ""
    )

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier.fillMaxWidth()
    ){

        Column(modifier = Modifier.padding(22.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(0.15f),
                                    MaterialTheme.colorScheme.primary.copy(0.05f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Business,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Training Center Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (expanded) "Tap to collapse" else "Tap to expand",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {

                Column {

                    Spacer(modifier = Modifier.height(20.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))

                    InfoRow(Icons.Default.ConfirmationNumber, "PRN Number", prnNumber)
                    InfoRow(Icons.Default.Description, "Sanction Letter No.", sanctionLetter)
                    InfoRow(Icons.Default.Description, "Inspection Type", inspectionType)
                    InfoRow(Icons.Default.LocationOn, "Training Centre Name", trainingCenterName)
                    InfoRow(Icons.Default.Badge, "Training Centre ID", trainingCenterId)
                    InfoRow(Icons.Default.Build, "Trade & Capacity", tradeAndCapacity)
                    InfoRow(Icons.Default.Person, "Training Centre Incharge", inchargeName)
                    InfoRow(Icons.Default.Phone, "Mobile Number", mobileNumber)
                    InfoRow(Icons.Default.Email, "Email", email)
                    InfoRow(Icons.Default.LocationOn, "Coordinates", coordinate)
                    InfoRow(Icons.Default.Badge, "Role", roleName)
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    name = "Training Center - Light"
)
@Composable
fun TrainingCenterDetailsPreview() {
    MaterialTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {

                TrainingCenterDetails(
                    prnNumber = "PRN123456",
                    sanctionLetter = "SO25D020028",
                    inspectionType = "Physical Inspection",
                    trainingCenterId = "TC25D280042",
                    trainingCenterName = "Skill Development Center Delhi",
                    inchargeName = "Mr. Ramesh Kumar",
                    mobileNumber = "9876543210",
                    email = "trainingcenter@gov.in",
                    tradeAndCapacity = "Electrician - 30 Seats",
                    coordinate = "28.6139° N, 77.2090° E",
                    roleName = "Center Manager"
                )
            }
        }
    }
}