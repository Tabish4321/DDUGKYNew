package com.deendayalproject.fragments.composeui.previous_inspection

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.mapper.mapToCommonList
import com.deendayalproject.model.request.PreviousInsQuesReq
import com.deendayalproject.model.response.InspectionFullDetails
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.InspectionViewModel
import com.deendayalproject.BuildConfig

@Composable
fun PreviousInspectionEditScreen(
    navController: NavController,
    data: InspectionFullDetails?,
    viewModel: InspectionViewModel
) {

    val context = LocalContext.current

    val allList = remember(data) {
        data?.let { mapToCommonList(it) } ?: emptyList()
    }

    val isLoading by viewModel.loading.collectAsState()
    val saveSuccess by viewModel.saveSuccess.collectAsState()

    val tabs = InspectionTab.values()

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )

    // ⭐ SAVE SUCCESS → REFRESH LIST
    LaunchedEffect(saveSuccess) {

        if (saveSuccess) {

            viewModel.getPreviousInsQues(
                PreviousInsQuesReq(
                    appVersion = BuildConfig.VERSION_NAME,
                    inspectionId = AppUtil
                        .getSavedInspectionIdPreference(context)
                        .toInt()
                ),
                AppUtil.getSavedTokenPreference(context)
            )

            viewModel.resetSaveFlag()
        }
    }

    Column {

        PremiumTopBar(
            dynamicTitle = "Due Diligence",
            onBackClick = { navController.popBackStack() }
        )

        PremiumInspectionTabsUltra(
            pagerState = pagerState,
            list = allList
        )

        Box(Modifier.fillMaxSize()) {

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->

                val tab = tabs[page]

                val filtered =
                    allList.filter { it.sectionName == tab.sectionKey }

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(filtered) { item ->

                        CommonInspectionCard(
                            item = item,
                            viewModel = viewModel
                        )
                    }
                }
            }

            // ⭐ GLOBAL LOADER
            if (isLoading) {

                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}