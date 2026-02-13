package com.deendayalproject.fragments.composeui

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


@Composable
fun TrainingCenterDetails( prnNumber: String, sanctionLetter: String,inspectionType: String,trainingCenterId: String) {

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

            InfoRow(
                icon = Icons.Default.ConfirmationNumber,
                label = "PRN Number",
                value = prnNumber
            )

            InfoRow(
                icon = Icons.Default.Description,
                label = "Sanction Letter No.",
                value = sanctionLetter
            )
            InfoRow(
                icon = Icons.Default.Description,
                label = "Inspection type",
                value = inspectionType
            )

            InfoRow(
                icon = Icons.Default.LocationOn,
                label = "Training Centre Name",
                value = "TC Lucknow, Uttar Pradesh"
            )

            InfoRow(
                icon = Icons.Default.Badge,
                label = "Training Centre ID",
                value = trainingCenterId
            )

            InfoRow(
                icon = Icons.Default.Build,
                label = "Approved Trade & Capacity",
                value = "Electrician (40)"
            )


            InfoRow(
                icon = Icons.Default.Person,
                label = "Training Centre In-Charge Name",
                value = "Mr. Rajesh Kumar"
            )

            InfoRow(
                icon = Icons.Default.Phone,
                label = "Mobile Number",
                value = "+91 9876543210"
            )

            InfoRow(
                icon = Icons.Default.Email,
                label = "Email",
                value = "tc.lucknow@email.com"
            )

            InfoRow(
                icon = Icons.Default.LocationOn,
                label = "Coordinates",
                value = "26.8467° N, 80.9462° E"
            )

            InfoRow(
                icon = Icons.Default.DateRange,
                label = "Date of Inspection",
                value = "05 Feb 2026"
            )

            InfoRow(
                icon = Icons.Default.Person,
                label = "Inspector Name",
                value = "Rahul Sharma"
            )

            InfoRow(
                icon = Icons.Default.Badge,
                label = "Inspected by (Role)",
                value = "Q Team / SRLM / CTSA"
            )
        }
    }
}
