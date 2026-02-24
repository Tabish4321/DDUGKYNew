package com.deendayalproject.fragments.composeui.trainingCenListAandDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.deendayalproject.R
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.fragments.composeui.common.ShimmerTrainingList
import com.deendayalproject.model.response.TrainingCenterListInspecRes

@Composable
fun TrainingCenterListScreen(
    items: List<TrainingCenterListInspecRes>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onItemClick: (TrainingCenterListInspecRes) -> Unit
) {

    Scaffold(
        topBar = {
            PremiumTopBar(
                dynamicTitle = "Training Centers",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->

        if (isLoading) {

            Box(modifier = Modifier.padding(innerPadding)) {
                ShimmerTrainingList()
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(colorResource(R.color.white)),
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
}
