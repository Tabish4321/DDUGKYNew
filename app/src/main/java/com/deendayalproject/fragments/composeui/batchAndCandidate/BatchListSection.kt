package com.deendayalproject.fragments.composeui.batchAndCandidate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.deendayalproject.R
import com.deendayalproject.model.response.batchListRes

@Composable
fun BatchListSection(
    batchList: List<batchListRes>,
    onBatchClick: (batchListRes) -> Unit
) {

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        batchList.forEach { batch ->

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onBatchClick(batch) },

                shape = RoundedCornerShape(12.dp),

                colors = CardDefaults.elevatedCardColors(
                    containerColor = colorResource(id = R.color.white)
                ),

                elevation = CardDefaults.elevatedCardElevation(4.dp)
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Batch ID: ${batch.batchId}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Reg No: ${batch.batchRegNo}"
                    )
                }
            }
        }
    }
}