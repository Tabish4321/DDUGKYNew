package com.deendayalproject.fragments.composeui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun TrainingCenterDetails() {

    ElevatedCard(
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                "Training Center Details",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(12.dp))

            InfoRow(
                icon = Icons.Default.Badge,
                label = "PRN Number",
                value = "2505000007"
            )

            InfoRow(
                icon = Icons.Default.LocationOn,
                label = "Training Center",
                value = "TC Lucknow"
            )

            InfoRow(
                icon = Icons.Default.Person,
                label = "Inspector",
                value = "Rahul Sharma"
            )

        }
    }
}