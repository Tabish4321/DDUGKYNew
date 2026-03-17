package com.deendayalproject.fragments.composeui.previous_inspection

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import com.deendayalproject.model.uistate.CommonInspectionItem
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch

@Composable
fun PremiumInspectionTabsUltra(
    pagerState: PagerState,
    list: List<CommonInspectionItem>
) {

    val tabs = InspectionTab.values()
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

    // ⭐ AUTO SCROLL WHEN PAGE CHANGE
    LaunchedEffect(pagerState.currentPage) {
        listState.animateScrollToItem(pagerState.currentPage)
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        itemsIndexed(tabs) { index, tab ->

            val selected = pagerState.currentPage == index

            val gradient =
                Brush.horizontalGradient(
                    listOf(Color(0xFF2563EB), Color(0xFF1E40AF))
                )

            val borderColor by animateColorAsState(
                if (selected) Color(0xFF1E3A8A) else Color(0xFFE5E7EB)
            )

            val bgColor by animateColorAsState(
                if (selected) Color.Transparent else Color(0xFFF8FAFC)
            )

            val count = list.count { it.sectionName == tab.sectionKey }

            Surface(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, borderColor),
                color = bgColor,
                tonalElevation = if (selected) 6.dp else 0.dp
            ) {

                Box(
                    modifier = Modifier
                        .background(
                            if (selected) gradient
                            else Brush.horizontalGradient(listOf(bgColor, bgColor))
                        )
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(
                            text = tab.title,
                            color = if (selected) Color.White else Color(0xFF111827),
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 2,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (selected)
                                        Color.White.copy(.2f)
                                    else Color.White
                                )
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = count.toString(),
                                color = if (selected) Color.White else Color.Black,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}