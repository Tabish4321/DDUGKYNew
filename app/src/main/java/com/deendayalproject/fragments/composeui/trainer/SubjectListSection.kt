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
import androidx.compose.ui.unit.dp
import com.deendayalproject.model.request.SubjectItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectListSection(

    subjects: List<String>,
    subjectData: List<SubjectItem>,
    selectedSubject: String,
    onSubjectSelect: (String) -> Unit,
    onAddClick: () -> Unit,
    onDelete: (SubjectItem) -> Unit

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

                        text = { Text(subject) },

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

        /* -------- Section Header -------- */

        Text(
            text = "Added Subjects",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        /* -------- Subject List -------- */

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            subjectData.forEach { subject ->

                ElevatedCard(

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(14.dp),

                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 6.dp
                    ),

                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color.White
                    )

                ) {

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),

                        verticalAlignment = Alignment.CenterVertically,

                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {

                        Text(
                            text = subject.subjectName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )

                        IconButton(
                            onClick = { onDelete(subject) }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Delete,
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