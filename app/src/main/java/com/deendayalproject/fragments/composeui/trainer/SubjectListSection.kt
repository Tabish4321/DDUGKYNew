package com.deendayalproject.fragments.composeui.trainer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deendayalproject.model.response.SubjectListData
import com.deendayalproject.model.response.TrainerData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectListSection(
    subjects: List<TrainerData>,
    subjectData: List<SubjectListData>,
    selectedSubject: String,
    deletingSubjectId: String?,
    onSubjectSelect: ( TrainerData?) -> Unit,
    onAddClick: () -> Unit,
    onDelete: (SubjectListData) -> Unit

) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        /* -------- Dropdown -------- */

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                value = selectedSubject,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Subject") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                subjects.forEach { subject ->

                    DropdownMenuItem(
                        text = { Text(subject.trainerWithSubject) },
                        onClick = {
                            onSubjectSelect(subject)
                            expanded = false
                        }
                    )
                }
            }
        }

        /* -------- Add Button -------- */

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(52.dp)
            ) {

                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )

            }

        }

        Text(
            text = "Added Subjects",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            subjectData.forEach { subject ->

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.elevatedCardElevation(6.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            "${subject.trainerName} • ${subject.subject}" ,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        if (deletingSubjectId == subject.subjectId) {

                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )

                        } else {

                            IconButton(
                                onClick = { onDelete(subject) }
                            ) {

                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )

                            }

                        }

                    }

                }

            }

        }

    }

}


@Preview(showBackground = true)
@Composable
fun SubjectListSectionPreview() {

    val subjects = listOf(
        "IT",
        "Soft Skills",
        "English",
        "Domain",
        "Entrepreneurship"
    )

    val dummyData = listOf(
        SubjectListData(
            subjectId = "1",
            subject = "IT",
            "rishi"
        ),
        SubjectListData(
            subjectId = "2",
            subject = "English",
            "Porwal"
        )
    )

    var selectedSubject by remember { mutableStateOf("IT") }

    MaterialTheme {
        Surface(modifier = Modifier.padding(16.dp)) {

            SubjectListSection(
                subjects = emptyList(),
                subjectData = dummyData,
                selectedSubject = selectedSubject,
                deletingSubjectId = null,

                onSubjectSelect = {  it },
                onAddClick = {},
                onDelete = {}
            )
        }
    }
}