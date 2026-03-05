package com.deendayalproject.fragments.composeui.tlm

import android.graphics.Bitmap
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.deendayalproject.model.uistate.TlmQuestion
import com.deendayalproject.util.AppUtil.bitmapToCompressedBase64
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@Composable
fun TlmVerificationSection(
    snackbarHostState: SnackbarHostState,
    onSubmit: (List<TlmQuestion>) -> Unit
) {

    val scope = rememberCoroutineScope()

    var captureIndex by remember { mutableStateOf<Int?>(null) }

    val questions = remember {

        mutableStateListOf(

            TlmQuestion("Received Domain Curriculum"),
            TlmQuestion("Received Bilingual TLM for IT Skills"),
            TlmQuestion("Received Bilingual TLM for Soft Skills"),
            TlmQuestion("Received Bilingual TLM for English Skills"),
            TlmQuestion("Received Training Kit"),
            TlmQuestion("Received Bilingual ID Card"),
            TlmQuestion("Practical Learning Provided"),
            TlmQuestion("Usage of Lab Tools & Equipment"),
            TlmQuestion("Tablets Uploaded with TLM & Info Content"),
            TlmQuestion("IP Enabled Camera Footage for Assessments Available")
        )
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->

            bitmap?.let {

                captureIndex?.let { index ->

                    val base64 = bitmapToCompressedBase64(it)

                    questions[index] =
                        questions[index].copy(
                            imageBitmap = it,
                            imageBase64 = base64
                        )
                }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        questions.forEachIndexed { index, item ->

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = item.question,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Button(
                            onClick = {

                                questions[index] =
                                    item.copy(answer = "Yes")

                                captureIndex = index
                                cameraLauncher.launch(null)
                            }
                        ) {
                            Text("Yes")
                        }

                        Button(
                            onClick = {

                                questions[index] =
                                    item.copy(answer = "No")
                            }
                        ) {
                            Text("No")
                        }
                    }

                    if (item.answer == "No") {

                        OutlinedTextField(
                            value = item.remarks,
                            onValueChange = {

                                questions[index] =
                                    item.copy(remarks = it)
                            },
                            label = { Text("Remarks") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item.imageBitmap?.let { bitmap ->

                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            )

                            IconButton(
                                onClick = {

                                    questions[index] =
                                        questions[index].copy(
                                            imageBitmap = null,
                                            imageBase64 = null
                                        )
                                },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = {

                scope.launch {

                    for (item in questions) {

                        when {

                            item.answer == null -> {

                                snackbarHostState.showSnackbar(
                                    "Please select: ${item.question}"
                                )
                                return@launch
                            }

                            item.answer == "Yes" && item.imageBase64 == null -> {

                                snackbarHostState.showSnackbar(
                                    "Please capture proof for ${item.question}"
                                )
                                return@launch
                            }

                            item.answer == "No" && item.remarks.isBlank() -> {

                                snackbarHostState.showSnackbar(
                                    "Please enter remarks for ${item.question}"
                                )
                                return@launch
                            }
                        }
                    }

                    onSubmit(questions)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Submit")
        }
    }
}