package com.deendayalproject.fragments.composeui.trainingCenListAandDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Build
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.res.colorResource
import com.deendayalproject.R
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

    ElevatedCard(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorResource(id = R.color.white)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                "Training Center Details",
                style = MaterialTheme.typography.titleMedium
            )

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