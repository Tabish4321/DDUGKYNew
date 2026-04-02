package com.deendayalproject.fragments.composeui.trainer


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deendayalproject.R
import com.deendayalproject.model.response.TrainerData

@Composable
fun TrainerDataCard(
    trainer: List<TrainerData>,
    onVerifyTrainerClick: (TrainerData) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        if(trainer.isNullOrEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(24.dp)
                ) {

                    Text(
                        text = "No Data Available",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF444444),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "There is currently no data to display.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
            }
        else{
            trainer.forEach { item ->
                SingleTrainerCard(
                    trainer = item,
                    onVerifyTrainerClick = {
                        onVerifyTrainerClick(item)
                    }
                )
            }
        }
    }
}


@Composable
fun SingleTrainerCard(
    trainer: TrainerData,
    onVerifyTrainerClick: () -> Unit
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
                        text = trainer.trainerName.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF795FDA)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = trainer.trainerName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Trainer Id: ${trainer.trainerId}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Designation: ${trainer.trainerDesignation}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Divider()

            Spacer(modifier = Modifier.height(8.dp))

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
                        text = trainer.contactNumber,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = onVerifyTrainerClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFF795FDA),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 14.dp,
                        vertical = 2.dp
                    )
                ) {

                    Text(
                        text = "Verify",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrainerCardPreview() {

    val dummyList = listOf(
        TrainerData(
            trainerName = "Rishi Kumar",
            trainerId = "TR123",
            trainerDesignation = "Senior Trainer",
            contactNumber = "9876543210",
            trainerCode = 0,
            trainerWithSubject = ""
        ),
        TrainerData(
            trainerName = "Ankit Sharma",
            trainerId = "TR456",
            trainerDesignation = "Assistant Trainer",
            contactNumber = "9123456780",
            trainerCode = 0,
            trainerWithSubject = ""
        )
    )

    MaterialTheme {
        Surface {
            TrainerDataCard(
                trainer = dummyList,
                onVerifyTrainerClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingleTrainerCardPreview() {

    val trainer = TrainerData(
        trainerName = "Rishi Kumar",
        trainerId = "TR123",
        trainerDesignation = "Senior Trainer",
        contactNumber = "9876543210",
        trainerCode = 0,
        trainerWithSubject = ""
    )

    MaterialTheme {
        Surface {
            SingleTrainerCard(
                trainer = trainer,
                onVerifyTrainerClick = {}
            )
        }
    }
}