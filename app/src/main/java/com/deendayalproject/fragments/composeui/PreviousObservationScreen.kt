package com.deendayalproject.fragments.composeui

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.MultiLineEditText
import com.deendayalproject.fragments.composeui.common.ShimmerTrainingList
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.AppUtil.bitmapToCompressedBase64
import com.deendayalproject.viewmodel.CandidateVerificationViewModel
import kotlinx.coroutines.launch
import org.slf4j.helpers.Util


@Composable
fun PreviousObservationScreen(
    viewModel: CandidateVerificationViewModel,
) {

    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    var fileError by remember {
        mutableStateOf(false)
    }

    val previouseInspectionId = remember {
        AppUtil.getPreviouseSavedInspectionIdPreference(context)
    }



    // =========================
    // LOAD OBSERVATION
    // =========================

    LaunchedEffect(Unit) {

        val id =
            previouseInspectionId.toIntOrNull()

        if (id != null) {

            viewModel.loadObservation(id)
        }
    }



    // =========================
    // FILE PICKER
    // =========================

    val filePickerLauncher =
        rememberLauncherForActivityResult(

            contract =
                ActivityResultContracts.OpenDocument()

        ) { uri ->

            uri ?: return@rememberLauncherForActivityResult

            try {

                val fileSize =
                    context.contentResolver
                        .openFileDescriptor(uri, "r")
                        ?.statSize ?: 0L

                val fileSizeInMB =
                    fileSize / (1024 * 1024)



                // =====================
                // MAX 10 MB CHECK
                // =====================

                if (fileSizeInMB > 5) {

                    fileError = true

                    Toast.makeText(
                        context,
                        "File size must be less than 5 MB",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@rememberLauncherForActivityResult
                }



                // =====================
                // MIME TYPE CHECK
                // =====================

                val mimeType =
                    context.contentResolver
                        .getType(uri)

                val allowedTypes = listOf(

                    "application/pdf",

                    "image/jpeg",

                    "image/jpg",

                    "image/png"
                )

                if (mimeType !in allowedTypes) {

                    fileError = true

                    Toast.makeText(
                        context,
                        "Only PDF and JPEG allowed",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@rememberLauncherForActivityResult
                }



                // =====================
                // FILE NAME
                // =====================

                val fileName = getFileName(context, uri)
                val base64 = AppUtil.uriToBase64(context,uri)?:""

//                // =====================
//                // BASE64
//                // =====================
//
//                val inputStream = context.contentResolver.openInputStream(uri)
//
//                val bytes = inputStream?.readBytes()
//                val base64 = Base64.encodeToString(
//                        bytes,
//                        Base64.DEFAULT
//                    )


                // =====================
                // VIEWMODEL UPDATE
                // =====================

                viewModel.updateFinalAttachment(

                    base64 = base64,

                    fileName = fileName
                )

                fileError = false

            } catch (e: Exception) {

                fileError = true

                Toast.makeText(
                    context,
                    "Unable to select file",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }



    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if (state.isLoading) {

            ShimmerTrainingList()

        } else {

            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),

                verticalArrangement =
                    Arrangement.spacedBy(14.dp)

            ) {



                /* ---------------- NORMAL SECTIONS ---------------- */

                state.sections.filterKeys { it != "OngoingBatchCandidate" }
                    .forEach { (_, itemsList) ->

                        items(itemsList) { item ->

                            state.answers[item.questionId ?: 0]

                            val remark = state.remarks[item.questionId ?: 0] ?: item.remark ?: ""

                            Card(
                                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(14.dp),
                                elevation = CardDefaults.elevatedCardElevation(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp)
                                ) {

                                    // Section Name
                                    Text(
                                        text = item.sactionName ?: "",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    // Question
                                    Text(
                                        text = item.question ?: "",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(Modifier.height(10.dp))
                                    Text(
                                        text = "Remarks:",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Bold
                                    )

                                    // Remark Box
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                Color.White, RoundedCornerShape(10.dp)
                                            )
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            text = remark.ifEmpty { "No remarks added" },
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }

                /* ---------------- ONGOING SECTION ---------------- */

                val ongoing = state.sections["OngoingBatchCandidate"] ?: emptyList()

                val grouped = ongoing.groupBy {
                    it.sactionType ?: "Other"
                }

                grouped.forEach { (type, list) ->

                    item {
                        Text(
                            text = type,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(list) { item ->

                        state.answers[item.questionId ?: 0]

                        val remark = state.remarks[item.questionId ?: 0] ?: item.remark ?: ""

                        Card(
                            colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(14.dp),
                            elevation = CardDefaults.elevatedCardElevation(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {

                                Text(
                                    text = item.sactionName ?: "",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(Modifier.height(6.dp))

                                Text(
                                    text = item.question ?: "",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(Modifier.height(10.dp))
                                Text(
                                    text = "Remarks:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Bold
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color.White, RoundedCornerShape(10.dp)
                                        )
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = remark.ifEmpty { "No remarks added" },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }


                /* ---------------- FINAL SUBMIT ---------------- */

                item {

                    Spacer(
                        Modifier.height(10.dp)
                    )

                    MultiLineEditText(

                        value =
                            viewModel.finalRemark,

                        onValueChange = {

                            viewModel.updateFinalRemark(it)
                        },

                        label =
                            "Enter final inspection remark",

                        isError =
                            viewModel.finalRemark.isBlank(),

                        maxLength = 2000
                    )
                }



                // =====================================
                // ATTACHMENT
                // =====================================

                item {

                    Spacer(
                        Modifier.height(12.dp)
                    )

                    OutlinedButton(

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),

                        shape =
                            RoundedCornerShape(14.dp),

                        onClick = {

                            filePickerLauncher.launch(

                                arrayOf(

                                    "application/pdf",

                                    "image/jpeg",

                                    "image/png"
                                )
                            )
                        }

                    ) {

                        Text(

                            text = if (

                                viewModel
                                    .finalAttachmentName
                                    .isBlank()

                            ) {

                                "Add Attachment (PDF/JPEG)"

                            } else {

                                viewModel
                                    .finalAttachmentName
                            }
                        )
                    }



                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(

                        text =
                            "Only PDF/JPEG allowed • Max 10 MB",

                        style =
                            MaterialTheme.typography.bodySmall,

                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurfaceVariant
                    )



                    // =====================
                    // ERROR
                    // =====================

                    if (

                        fileError ||

                        viewModel
                            .finalAttachmentBase64
                            .isBlank()

                    ) {

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(

                            text =
                                "Valid attachment required",

                            color = Color.Red,

                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall
                        )
                    }
                }
            }
        }
    }
}



// =========================
// FILE NAME
// =========================

fun getFileName(
    context: Context,
    uri: Uri
): String {

    var result = "Attachment"

    val cursor =
        context.contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )

    cursor?.use {

        if (it.moveToFirst()) {

            val index =
                it.getColumnIndex(
                    OpenableColumns.DISPLAY_NAME
                )

            if (index != -1) {

                result = it.getString(index)
            }
        }
    }

    return result
}


