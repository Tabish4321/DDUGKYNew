package com.deendayalproject.fragments.composeui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.deendayalproject.model.response.TrainingCenterListInspecRes

@Composable
fun TrainingCenterListScreen(
    items: List<TrainingCenterListInspecRes>,
    isLoading: Boolean,
    onItemClick: (TrainingCenterListInspecRes) -> Unit
) {


    if (isLoading) {
        ShimmerTrainingList()
    } else {

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(
                items,
                key = { it.id }
            ) { item ->

                TrainingCenterCard(
                    item = item,
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}
