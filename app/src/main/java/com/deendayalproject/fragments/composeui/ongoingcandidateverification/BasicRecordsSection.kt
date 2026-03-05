package com.deendayalproject.fragments.composeui.ongoingcandidateverification


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.model.response.CandidateProofItem
import com.deendayalproject.model.uistate.DocumentVerificationState
@Composable
fun BasicRecordsSection(
    imageList: List<CandidateProofItem>?,
    isLoading: Boolean,
    showMessage: (String) -> Unit,
    onSubmit: (List<DocumentVerificationState>) -> Unit
) {

    var selectedImage by remember { mutableStateOf<String?>(null) }

    val proof = imageList?.firstOrNull()

    val documents = remember {

        mutableStateListOf(

            DocumentVerificationState("Poverty Proof",1,proof?.pmaygAttachment),
            DocumentVerificationState("Category Proof",2,proof?.categoryCertPath),
            DocumentVerificationState("Minority Proof",3,proof?.minorityCertPath),
            DocumentVerificationState("Education Proof",4,proof?.pipCert),
            DocumentVerificationState("Disability Proof",5,proof?.disablityCertPath)

        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {

        documents.forEachIndexed { index, doc ->

            ProofWithQuestion(

                title = doc.title,
                base64Image = doc.image,
                answer = doc.answer,
                remarks = doc.remarks,
                isError = false,

                onAnswerChange = {

                    documents[index] =
                        documents[index].copy(answer = it)

                },

                onRemarksChange = {

                    documents[index] =
                        documents[index].copy(remarks = it)

                },

                selectedImage = {

                    selectedImage = it

                }
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,

            onClick = {

                val invalid = documents.find {

                    it.answer == null ||
                            (it.answer == "No" && it.remarks.isBlank())

                }

                if (invalid != null) {

                    showMessage("${invalid.title} is incomplete")

                } else {

                    onSubmit(documents)

                }
            }


        ) {

            if (isLoading) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )

                    Spacer(Modifier.width(8.dp))

                    Text("Submitting...")
                }

            } else {

                Text("Submit Basic Records")
            }
        }

        //  IMAGE PREVIEW (THIS WAS MISSING)

        selectedImage?.let {

            ImagePreviewDialog(
                base64 = it,
                onClose = { selectedImage = null }
            )

        }
    }
}

@Composable
fun ProofWithQuestion(

    title: String,
    base64Image: String?,
    answer: String?,
    remarks: String,
    isError: Boolean,

    onAnswerChange: (String) -> Unit,
    onRemarksChange: (String) -> Unit,

    selectedImage: (String) -> Unit
) {

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text(title)

                if (!base64Image.isNullOrBlank()) {

                    IconButton(
                        onClick = {
                            selectedImage(base64Image)
                        }
                    ) {

                        Icon(
                            Icons.Default.Visibility,
                            null
                        )
                    }
                }
            }

            ComplianceQuestionWithRemarks(

                question = "Is document valid?",

                answer = answer,

                remarks = remarks,

                isError = isError,

                onAnswerChange = onAnswerChange,

                onRemarksChange = onRemarksChange
            )
        }
    }
}

@Composable
fun ImagePreviewDialog(
    base64: String,
    onClose: () -> Unit
) {

    val bitmap = remember(base64) {
        try {
            val cleanBase64 = base64
                .substringAfter("base64,")
                .replace("\n", "")
                .replace("\r", "")
                .replace(" ", "")

            val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)

            BitmapFactory.decodeByteArray(
                decodedBytes,
                0,
                decodedBytes.size
            )
        } catch (e: Exception) {
            null
        }
    }

    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Dialog(
        onDismissRequest = onClose,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {

            bitmap?.let {

                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize() // 🔥 FULL SCREEN
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        )
                        .pointerInput(Unit) {

                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(1f, 5f)
                                offsetX += pan.x
                                offsetY += pan.y
                            }
                        }
                        .pointerInput(Unit) {

                            detectTapGestures(
                                onDoubleTap = {
                                    if (scale > 1f) {
                                        scale = 1f
                                        offsetX = 0f
                                        offsetY = 0f
                                    } else {
                                        scale = 3f
                                    }
                                }
                            )
                        }
                )
            }

            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}


