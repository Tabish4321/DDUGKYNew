package com.deendayalproject.fragments.composeui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.deendayalproject.R
import com.deendayalproject.fragments.composeui.common.CandidateHeader
import com.deendayalproject.model.response.CandidateListInspectionRes
import com.deendayalproject.model.uistate.CandidateVerificationUiState
import com.deendayalproject.util.AppUtil.decodeBase64ToBitmap
import com.deendayalproject.viewmodel.InspectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OngoingCandidateBottomSheet(
    viewModel: InspectionViewModel,
    candidateData: CandidateListInspectionRes,
    onDismiss: () -> Unit,
  //  onSubmit: (CandidateVerificationUiState) -> Unit

) {

    val imageResponse by viewModel.getCandidateImageRecords.collectAsState()
    val proofItem = imageResponse?.wrappedList?.firstOrNull()

    fun String?.isValidImage(): Boolean {
        return !this.isNullOrBlank() && this != "N/A"
    }

    val proofList = proofItem?.let {
        listOf(
            "Proof of Poverty" to listOfNotNull(
                it.rationCardPath.takeIf { path -> path.isValidImage() },
                it.naregaCardPath.takeIf { path -> path.isValidImage() },
                it.pmaygAttachment.takeIf { path -> path.isValidImage() }
            ),
            "Category Proof (SC/ST)" to listOfNotNull(
                it.categoryCertPath.takeIf { path -> path.isValidImage() }
            ),
            "Minority Proof" to listOfNotNull(
                it.minorityCertPath.takeIf { path -> path.isValidImage() }
            ),
            "Education Proof" to listOfNotNull(
                it.pipCert.takeIf { path -> path.isValidImage() }
            ),
            "PWD Proof" to listOfNotNull(
                it.disablityCertPath.takeIf { path -> path.isValidImage() }
            )
        )
    } ?: emptyList()

    var answers by remember { mutableStateOf(mutableMapOf<String, String>()) }
    var remarks by remember { mutableStateOf(mutableMapOf<String, String>()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Text(
                text = "Ongoing Candidate Verification",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            CandidateHeader(
                candidateData = candidateData,
                onCloseClick = onDismiss
            )

            Spacer(modifier = Modifier.height(16.dp))

            proofList.forEach { (title, images) ->

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        YesNoSelector(
                            selected = answers[title] ?: "",
                            onSelect = { answers[title] = it }
                        )

                        if (answers[title] == "NO") {
                            OutlinedTextField(
                                value = remarks[title] ?: "",
                                onValueChange = { remarks[title] = it },
                                label = { Text("Enter Remark") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        ImageThumbnailList(images)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { /* Submit logic */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Submit Verification")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun YesNoSelector(
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

        FilterChip(
            selected = selected == "YES",
            onClick = { onSelect("YES") },
            label = { Text("Yes") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFFDCFCE7),
                selectedLabelColor = Color(0xFF166534)
            )
        )

        FilterChip(
            selected = selected == "NO",
            onClick = { onSelect("NO") },
            label = { Text("No") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFFFEE2E2),
                selectedLabelColor = Color(0xFF991B1B)
            )
        )
    }
}

@Composable
fun ImageThumbnailList(images: List<String>) {

    var previewBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

        if (images.isEmpty()) {

            Image(
                painter = painterResource(id = R.drawable.ic_no_image),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

        } else {

            images.forEach { base64 ->

                val bitmap = remember(base64) {
                    decodeBase64ToBitmap(base64)
                }

                bitmap?.let {

                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { previewBitmap = it },
                        contentScale = ContentScale.Crop
                    )
                } ?: run {

                    Image(
                        painter = painterResource(id = R.drawable.ic_no_image),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }

    previewBitmap?.let { bmp ->
        Dialog(onDismissRequest = { previewBitmap = null }) {
            Box(Modifier.fillMaxSize()) {

                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                IconButton(
                    onClick = { previewBitmap = null },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }
        }
    }
}