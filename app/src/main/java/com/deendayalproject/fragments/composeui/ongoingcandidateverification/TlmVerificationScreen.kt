package com.deendayalproject.fragments.composeui.ongoingcandidateverification

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.model.uistate.TlmQuestion
import com.deendayalproject.util.AppUtil.bitmapToCompressedBase64
import kotlinx.coroutines.launch

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

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                cameraLauncher.launch(null)

            } else {

                scope.launch {

                    snackbarHostState.showSnackbar(
                        "Camera permission required"
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

            Column {

                ComplianceQuestionWithRemarks(

                    question = item.question,
                    answer = item.answer,
                    remarks = item.remarks,

                    onAnswerChange = { answer ->

                        if (answer == "Yes") {

                            questions[index] =
                                item.copy(
                                    answer = "Yes",
                                    remarks = ""
                                )

                            captureIndex = index

                            permissionLauncher.launch(
                                Manifest.permission.CAMERA
                            )

                        } else {

                            questions[index] =
                                item.copy(
                                    answer = "No",
                                    imageBitmap = null,
                                    imageBase64 = null
                                )
                        }
                    },

                    onRemarksChange = {

                        questions[index] =
                            item.copy(remarks = it)
                    }
                )

                if (item.answer == "Yes" && item.imageBitmap != null) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Image(
                            bitmap = item.imageBitmap!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                    }
                }
            }
        }

        Button(
            onClick = {

                scope.launch {

                    val invalidField = questions.firstOrNull {

                        it.answer == null ||
                                (it.answer == "Yes" && it.imageBase64 == null) ||
                                (it.answer == "No" && it.remarks.isBlank())
                    }

                    if (invalidField != null) {

                        val message = when {

                            invalidField.answer == null ->
                                "Please select Yes/No for ${invalidField.question}"

                            invalidField.answer == "Yes" &&
                                    invalidField.imageBase64 == null ->
                                "Please capture image for ${invalidField.question}"

                            invalidField.answer == "No" &&
                                    invalidField.remarks.isBlank() ->
                                "Please enter remarks for ${invalidField.question}"

                            else -> ""
                        }

                        snackbarHostState.showSnackbar(message)

                        return@launch
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