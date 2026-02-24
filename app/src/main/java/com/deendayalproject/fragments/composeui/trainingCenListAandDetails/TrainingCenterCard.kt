package com.deendayalproject.fragments.composeui.trainingCenListAandDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Difference
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.deendayalproject.R
import com.deendayalproject.fragments.composeui.common.InfoRow
import com.deendayalproject.model.response.TrainingCenterListInspecRes

@Composable
fun TrainingCenterCard(
    item: TrainingCenterListInspecRes,
    onClick: () -> Unit
) {

    ElevatedCard(
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorResource(id = R.color.white)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 14.dp
        ),
        modifier = Modifier
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {


                InfoRow(
                    icon = Icons.Default.Badge,
                    label = "PRN Number",
                    value = item.prnNumber
                )


                InfoRow(
                    icon = Icons.Default.Description,
                    label = "Sanction Letter No.",
                    value = item.sanctionLetterNo
                )

                InfoRow(
                    icon = Icons.Default.Difference,
                    label = "Inspection Type",
                    value = item.inspectionType
                )

            }
        }
    }
}
