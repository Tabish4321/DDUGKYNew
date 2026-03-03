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

@Composable
fun BasicRecordsSection(
    imageList: List<CandidateProofItem>?
) {

    var selectedImage by remember { mutableStateOf<String?>(null) }

    val proof = imageList?.firstOrNull()

    val documents = listOf(
        "PMAYG Attachment" to proof?.pmaygAttachment,
        "SHG Image" to proof?.shgImage,
        "RSBY Card" to proof?.rsbyCardPath,
        "Category Certificate" to proof?.categoryCertPath,
        "Minority Certificate" to proof?.minorityCertPath,
        "Ration Card" to proof?.rationCardPath,
        "NREGA Card" to proof?.naregaCardPath,
        "PIP Certificate" to proof?.pipCert,
        "Disability Certificate" to proof?.disablityCertPath
    )

    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {

        documents.forEach { (title, image) ->

            ProofWithQuestion(
                title = title,
                base64Image = image,
                selectedImage = { selectedImage = it }
            )
        }

        selectedImage?.let { base64 ->
            ImagePreviewDialog(
                base64 = base64,
                onClose = { selectedImage = null }
            )
        }
    }
}


@Composable
fun ProofWithQuestion(
    title: String,
    base64Image: String?,
    selectedImage: (String) -> Unit
) {

    var answer by remember { mutableStateOf<String?>(null) }
    var remarks by remember { mutableStateOf("") }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                if (!base64Image.isNullOrBlank()) {
                    IconButton(
                        onClick = { selectedImage(base64Image) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            if (base64Image.isNullOrBlank()) {
                Text(
                    text = "No image uploaded",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            ComplianceQuestionWithRemarks(
                question = "Is document valid?",
                answer = answer,
                remarks = remarks,
                onAnswerChange = { answer = it },
                onRemarksChange = { remarks = it }
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


