package com.deendayalproject.fragments.composeui.ongoingcandidateverification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SectionContent(title: String) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text("Section: $title", fontWeight = FontWeight.SemiBold)

        Divider()

        Text("• Records verified")
        Text("• Documents uploaded")
        Text("• Geo-location captured")
        Text("• Remarks recorded")
        Text("• Digital confirmation completed")
    }
}