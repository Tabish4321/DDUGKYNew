package com.deendayalproject.fragments.composeui.trainingCenListAandDetails


import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.InfoRow
import com.deendayalproject.util.AppUtil.downloadAndOpenPdf
import com.deendayalproject.util.AppUtil.getFileSize
import com.deendayalproject.util.isNull
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    roleName: String,
    revisedDoc: String,
    context: Context,
) {

    var expanded by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    var isDownloading by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var fileSizeText by remember { mutableStateOf("Unknown size") }
    var statusText by remember { mutableStateOf("Not downloaded") }
    val scope = rememberCoroutineScope()



    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(400),
        label = ""
    )



    //  DOWNLOAD CARD
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    )
    {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    "Revised Sanction Order",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    fileSizeText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    statusText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                if (isDownloading) {
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                    )
                }
            }

            IconButton(
                onClick = {
                    if (revisedDoc == ""){

                        Toast.makeText(
                            context,
                            "PDF not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else{
                        isDownloading = true
                        statusText = "Downloading..."

                        scope.launch {
                            for (i in 1..100) {
                                progress = i / 100f
                                delay(15)
                            }

                            fileSizeText = getFileSize(revisedDoc)
                            statusText = "Downloaded"
                            isDownloading = false

                            downloadAndOpenPdf(context, revisedDoc)
                        }
                    }

                },
                enabled = !isDownloading
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }


    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier.fillMaxWidth()
    )
    {

        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                verticalAlignment = Alignment.Top
            ) {


                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
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

                Spacer(modifier = Modifier.width(14.dp))

                // 🔹 TEXT SECTION (better spacing)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 2.dp) //  thoda upar adjust
                ) {

                    Text(
                        "Training Center Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {

                        Text(
                            if (expanded) "Tap to collapse" else "Tap to expand",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = if (expanded)
                                Icons.Default.KeyboardArrowUp
                            else
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.rotate(rotation)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))


                }
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
    val context = LocalContext.current

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
                    roleName = "Center Manager",
                   "",
                    context = context
                )
            }
        }
    }
}