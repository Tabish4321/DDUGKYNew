package com.deendayalproject.model.uistate

import PreviousInspectionItemResponse
import com.deendayalproject.model.response.DueDiligenceItemResponse

data class InspectionUiState(
    val isLoading: Boolean = false,
    val previousList: List<PreviousInspectionItemResponse> = emptyList(),
    val dueDiligenceList: List<DueDiligenceItemResponse> = emptyList(),
    val selectedTab: InspectionTab = InspectionTab.PREVIOUS,
    val error: String? = null
)

enum class InspectionTab {
    PREVIOUS,
    DUE_DILIGENCE
}