package com.deendayalproject.fragments.composeui.previous_inspection


import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import com.deendayalproject.BuildConfig
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionNAWithRemarks
import com.deendayalproject.fragments.composeui.common.InfoRow
import com.deendayalproject.model.request.savePreviousInspectionQuesReq
import com.deendayalproject.model.uistate.CommonInspectionItem
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.AppUtil.showBase64Dialog
import com.deendayalproject.util.AppUtil.showNoImageDialog
import com.deendayalproject.viewmodel.InspectionViewModel
import java.io.ByteArrayOutputStream


@Composable
fun CommonInspectionCard(
    item: CommonInspectionItem,
    viewModel: InspectionViewModel
) {

    val context = LocalContext.current
    val saveSuccess by viewModel.saveSuccess.collectAsState()
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val openCameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->

            bitmap?.let {

                capturedBitmap = it

                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 80, stream)

             var  attachmentBase64 =
                    Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
            }
        }

    var expanded by remember { mutableStateOf(false) }
    var answer by remember { mutableStateOf<String?>(null) }
    var remark by remember { mutableStateOf("") }
    var attachmentBase64 by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(answer) {

        if (
            item.sectionType == "LearningMaterialVerification"
            && answer == "Yes"
        ) {
            openCameraLauncher.launch(null)
        }
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },   //  FULL CARD CLICK
        colors = CardDefaults.elevatedCardColors(Color.White)
    ) {

        Column(Modifier.padding(14.dp)) {

            InfoRow(Icons.Default.Info, "Section Name", item.sectionName)

            item.sectionType?.let {
                InfoRow(Icons.Default.Info, "Section Type", it)
            }

            item.subject?.let {
                InfoRow(Icons.Default.Info, "Subject", it)
            }

            item.trainerName?.let {
                InfoRow(Icons.Default.Person, "Trainer", it)
            }

            item.candidateName?.let {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(modifier = Modifier.weight(1f)) {
                        InfoRow(Icons.Default.Person, "Candidate", it)
                    }

                    //  SHOW ONLY FOR CandidateRecordVerification
                    if (
                        item.sectionName == "OngoingBatchCandidateVerification" &&
                        item.sectionType == "CandidateRecordVerification"
                    ) {

                        IconButton(
                            onClick = {

                                if (!item.baseImage.isNullOrBlank()) {

                                    showBase64Dialog(context, item.baseImage)

                                } else {

                                    showNoImageDialog(context)
                                }
                            }
                        ) {
                            Icon(Icons.Default.Visibility, contentDescription = null)
                        }
                    }
                }
            }
            InfoRow(Icons.Default.Note, "Previous Remark", item.previousRemark ?: "-")

            Text(
                item.question,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 6.dp)
            )

            if (expanded) {

                Spacer(Modifier.height(10.dp))

                if (item.sectionName == "DocumentsStandardFormsAvailability") {

                    ComplianceQuestionNAWithRemarks(
                        question = "",
                        answer = answer,
                        remarks = remark,
                        onAnswerChange = { answer = it },
                        onRemarksChange = { remark = it }
                    )

                } else {

                    ComplianceQuestionYesNoOnly(   //  new UI
                        answer = answer,
                        remarks = remark,
                        onAnswerChange = { answer = it },
                        onRemarksChange = { remark = it }
                    )
                }

         /*       //  Learning Material → Camera
                if (item.sectionType == "LearningMaterialVerification" && answer == "Yes") {

                    CameraCapture(
                        onImageCaptured = {
                            attachmentBase64 = it
                        }
                    )
                }*/

                if (
                    item.sectionType == "LearningMaterialVerification"
                    && capturedBitmap != null
                ) {

                    Spacer(Modifier.height(10.dp))

                    Image(
                        bitmap = capturedBitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }



                Spacer(Modifier.height(10.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {

                        if (answer == null) {
                            Toast.makeText(context, "Please select value first", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (answer == "No" && remark.isBlank()) {
                            Toast.makeText(context, "Remark required", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        //  FINAL LEARNING MATERIAL VALIDATION
                        if (
                            item.sectionType == "LearningMaterialVerification"
                            && answer == "Yes"
                            && attachmentBase64.isNullOrEmpty()
                        ) {
                            Toast.makeText(context, "Please capture image first", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        viewModel.savePreviousInspectionObservation(
                            savePreviousInspectionQuesReq(
                                appVersion = BuildConfig.VERSION_NAME,
                                inspectionId = item.inspectionId,
                                candidateId = item.candidateId ?: "",
                                batchId = item.batchId ?: 0,
                                questionId = item.questionId,
                                answer = answer!!,
                                remark = remark,
                                sactionName = item.sectionName,
                                sactionType = item.sectionType ?: "",
                                attachment = attachmentBase64 ?: "",
                                trainerCode = item.trainerCode?:0,
                                subject = item.subject?:""
                            ),
                            AppUtil.getSavedTokenPreference(context)
                        )
                    }
                ) {
                    Text("Submit")
                }
            }
        }
    }
}