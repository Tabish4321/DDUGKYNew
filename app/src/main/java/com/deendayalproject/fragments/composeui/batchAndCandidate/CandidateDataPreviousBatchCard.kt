package com.deendayalproject.fragments.composeui.batchAndCandidate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.deendayalproject.R
import com.deendayalproject.model.response.CandidateListInspectionRes


@Composable
fun CandidateDataPreviousBatchCard(
    candidate: List<CandidateListInspectionRes>,
    onVerifyCandidateClick: (CandidateListInspectionRes) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        candidate.forEach { item ->

            SingleCandidateDataPreviousBatchCard(
                candidate = CandidateListInspectionRes(
                    candidateId = item.candidateId,
                    name = item.name,
                    rollNumber = item.rollNumber,
                    contactNumber = item.contactNumber,
                    status = item.status
                ),
                onVerifyCandidateClick = {
                    onVerifyCandidateClick(item)
                }
            )
        }
    }
}


@Composable
fun SingleCandidateDataPreviousBatchCard(
    candidate: CandidateListInspectionRes,
    onVerifyCandidateClick: () -> Unit
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorResource(id = R.color.white)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            // 🔹 Top Row (Image + Info)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE3F2FD)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = candidate.name.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF795FDA)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = candidate.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "KP Id: ${candidate.candidateId}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Roll: ${candidate.rollNumber}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Divider()

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Contact + Button Same Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = "Contact Number",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = candidate.contactNumber,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (candidate.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    val isVerified = candidate.status != 0
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isVerified) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        Text(
                            text = if (isVerified) "Verified" else "Verify"
                        )
                    }

//                    OutlinedButton(
//                        onClick = onVerifyCandidateClick,
//                        shape = RoundedCornerShape(8.dp),
//                        colors =  ButtonDefaults.outlinedButtonColors(
//                            containerColor = Color(0xFF795FDA),
//                            contentColor = Color.White
//                        ),
//                        contentPadding = PaddingValues(
//                            horizontal = 14.dp,
//                            vertical = 2.dp
//                        )
//                    ) {
//                        Text(
//                            text = "Verify",
//                            style = MaterialTheme.typography.labelMedium
//                        )
//                    }
                }
            }
        }
    }
}
