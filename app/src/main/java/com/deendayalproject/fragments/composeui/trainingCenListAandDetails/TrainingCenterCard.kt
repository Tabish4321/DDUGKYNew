package com.deendayalproject.fragments.composeui.trainingCenListAandDetails

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deendayalproject.fragments.composeui.common.InfoRow
import com.deendayalproject.model.response.TrainingCenterListInspecRes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrainingCenterCard(
    item: TrainingCenterListInspecRes,
    onClick: () -> Unit
) {
    //Testing Comment
    val formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val currentDate = java.time.LocalDate.now()

    val inspectionDate = try {
        item.inspectionDate?.let {
            java.time.LocalDate.parse(it, formatter)
        } ?: currentDate
    } catch (e: Exception) {
        currentDate
    }

    val isActive = inspectionDate == currentDate

    ElevatedCard(
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp), //  slightly smoother
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Column {

            // 🔷 Status Header (Refined)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(
                        if (isActive)
                            Color(0xFF2E7D32).copy(alpha = 0.95f)
                        else
                            Color(0xFFFF6F00).copy(alpha = 0.95f)
                    )
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isActive) "ACTIVE" else "UPCOMING",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp), // tighter rhythm
                    modifier = Modifier.fillMaxWidth()
                ) {

                    InfoRow(
                        icon = Icons.Default.Business,
                        label = "PIA Name",
                        value = item.piaName
                    )

                    DividerLight()

                    InfoRow(
                        icon = Icons.Default.AccountBalance,
                        label = "TC Name",
                        value = item.trainingCenterName
                    )

                    DividerLight()

                    InfoRow(
                        icon = Icons.Default.QrCode,
                        label = "TC Code",
                        value = item.trainingCenterCode
                    )

                    DividerLight()

                    InfoRow(
                        icon = Icons.Default.School,
                        label = "Center Type",
                        value = item.centerType
                    )
                }
            }
        }
    }
}

@Composable
fun DividerLight() {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.06f),
        thickness = 0.8.dp
    )
}