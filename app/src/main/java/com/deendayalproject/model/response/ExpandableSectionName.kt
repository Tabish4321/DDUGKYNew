package com.deendayalproject.model.response

import com.deendayalproject.fragments.composeui.common.ComplianceStatus

data class ExpandableSectionName(
    val title: String,
    val status: ComplianceStatus
)
