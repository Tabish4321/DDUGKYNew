package com.deendayalproject.fragments.composeui.ongoingcandidateverification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.ComplianceStatus
import com.deendayalproject.fragments.composeui.common.ExpandableComplianceCard
import com.deendayalproject.fragments.composeui.common.PremiumCandidateHeader
import com.deendayalproject.fragments.composeui.common.PremiumTopBar
import com.deendayalproject.model.response.CandidateProofItem
import com.deendayalproject.model.response.ExpandableSectionName

@Composable
fun OngoingCandidateSectionScreen(
    candidateId: String,
    candidateName: String,
    candidateMobileNo: String,
    candidateRollNo: String,
    imageList: List<CandidateProofItem>?,
    onBackClick: () -> Unit
) {

    val sections = listOf(
        ExpandableSectionName("Basic Records Verification", ComplianceStatus.NotCOMPLETE),
        ExpandableSectionName("Validate Attendance", ComplianceStatus.COMPLETE),
        ExpandableSectionName("Assessment", ComplianceStatus.NotCOMPLETE),
        ExpandableSectionName("Distribution of Teaching-Learning Material", ComplianceStatus.COMPLETE),
        ExpandableSectionName("Entitlements Distribution", ComplianceStatus.COMPLETE),
        ExpandableSectionName("Residential Facility Verification", ComplianceStatus.NotCOMPLETE)
    )

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            PremiumTopBar(
                dynamicTitle = "Candidate Details",
                onBackClick = onBackClick
            )
        },
        containerColor = Color.White
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {

            // 🔹 Fixed Candidate Header
            PremiumCandidateHeader(
                candidateId = candidateId,
                candidateName = candidateName,
                candidateMobileNo = candidateMobileNo,
                candidateRollNo = candidateRollNo
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🔽 Scrollable Expandable Sections
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {

                items(sections) { section ->

                    ExpandableComplianceCard(
                        title = section.title,
                        status = section.status,
                        leftIcon = {
                            Icon(
                                imageVector = getSectionIcon(section.title),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    ) {
                        if (section.title == "Basic Records Verification") {
                            BasicRecordsSection(imageList)
                        } else {
                            SectionContent(section.title)
                        }                    }
                }
            }
        }
    }
}