package com.deendayalproject.fragments.composeui.trainer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deendayalproject.R
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
        modifier = Modifier.fillMaxWidth().background(Color.White).padding(1.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 1.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.Absolute.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {

                // 🔹 Dropdown (takes remaining space)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1f).padding(1.dp)
                ) {

                    OutlinedTextField(
                        value = if (selectedSubject.isNullOrBlank()) {
                            "Please Select"
                        } else {
                            selectedSubject
                        },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Subject") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .background(Color.White)
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.White).padding(4.dp)
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

                Spacer(modifier = Modifier.width(10.dp))

                // 🔹 Add Button (clean + compact)
                FloatingActionButton(
                    onClick = onAddClick,
                    shape = RoundedCornerShape(10.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 8.dp
                    ),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowCircleRight,
                        contentDescription = "Add",
                        modifier = Modifier.size(22.dp)
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
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.elevatedCardElevation(8.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = colorResource(id = R.color.white)
                    ),
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "${subject.trainerName} • ${subject.subject}",
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )

                        if (deletingSubjectId == subject.subjectId) {

                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )

                        } else {


                            IconButton(
                                onClick = { onDelete(subject) },
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
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