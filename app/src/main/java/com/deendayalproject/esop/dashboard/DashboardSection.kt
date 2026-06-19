package com.deendayalproject.esop.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardSection() {

    Column {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            DashboardCard(
                modifier = Modifier.weight(1f),
                title = "My Tests",
                subtitle = "View your tests",
                icon = Icons.Default.Assignment
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            DashboardCard(
                modifier = Modifier.weight(1f),
                title = "Results",
                subtitle = "View your results",
                icon = Icons.Default.BarChart
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            DashboardCard(
                modifier = Modifier.weight(1f),
                title = "Certificate",
                subtitle = "View & Download",
                icon = Icons.Default.CardMembership
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            DashboardCard(
                modifier = Modifier.weight(1f),
                title = "Profile",
                subtitle = "View & Edit",
                icon = Icons.Default.Person
            )

        }
    }
}