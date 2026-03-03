package com.deendayalproject.fragments.composeui.ongoingcandidateverification

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable

@Composable
fun getSectionIcon(title: String) = when (title) {

    "Basic Records Verification" -> Icons.Default.Folder
    "Validate Attendance" -> Icons.Default.People
    "Assessment" -> Icons.Default.Assignment
    "Distribution of Teaching-Learning Material" -> Icons.Default.MenuBook
    "Entitlements Distribution" -> Icons.Default.CardGiftcard
    "Residential Facility Verification" -> Icons.Default.Home

    else -> Icons.Default.Description
}