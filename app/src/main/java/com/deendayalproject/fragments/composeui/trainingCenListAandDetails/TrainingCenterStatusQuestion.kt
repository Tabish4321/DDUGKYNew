import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.PremiumOption
import com.deendayalproject.fragments.composeui.common.PremiumSelector


@Composable
fun TrainingCenterStatusQuestion(

    answer: String?,

    remarks: String,

    imageBase64: String?,

    remarksError: Boolean,

    imageError: Boolean,

    onAnswerChange: (String) -> Unit,

    onRemarksChange: (String) -> Unit,

    onCaptureImage: () -> Unit,

    onPreviewImage: () -> Unit

) {

//    ElevatedCard(
//
//        modifier = Modifier.fillMaxWidth(),
//
//        shape = RoundedCornerShape(24.dp),
//
//        colors = CardDefaults.elevatedCardColors(
//            containerColor = Color.White
//        ),
//
//        elevation = CardDefaults.elevatedCardElevation(
//            defaultElevation = 6.dp
//        )
//
//    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)

        ) {



            // =========================
            // TITLE
            // =========================

            Text(

                text = "Training Center Status",

                modifier = Modifier.fillMaxWidth(),

                textAlign = TextAlign.Center,

                style = MaterialTheme.typography.titleLarge,

                fontWeight = FontWeight.SemiBold,

                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )



            // =========================
            // QUESTION
            // =========================

            Text(

                text = "Is the Training Center Open Today?",

                style = MaterialTheme.typography.titleMedium,

                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )



            // =========================
            // YES / NO PREMIUM SELECTOR
            // =========================

            PremiumSelector(

                options = listOf(

                    PremiumOption(
                        title = "YES",
                        activeColor = Color(0xFF2E7D32)
                    ),

                    PremiumOption(
                        title = "NO",
                        activeColor = Color(0xFFD32F2F)
                    )
                ),

                selected = answer,

                onSelect = onAnswerChange
            )



            // =========================
            // NO SECTION
            // =========================

            if (answer.equals("NO", true)) {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )



                // =====================
                // REMARKS
                // =====================

                OutlinedTextField(

                    value = remarks,

                    onValueChange = onRemarksChange,

                    modifier = Modifier.fillMaxWidth(),

                    label = {

                        Text("Enter Remarks")
                    },

                    shape = RoundedCornerShape(14.dp),

                    isError = remarksError,

                    minLines = 4
                )

                if (remarksError) {

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(

                        text = "Remarks required",

                        color = MaterialTheme.colorScheme.error,

                        style =
                            MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )



                // =====================
                // IMAGE BUTTON
                // =====================

                OutlinedButton(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),

                    shape = RoundedCornerShape(14.dp),

                    onClick = {

                        onCaptureImage()
                    }

                ) {

                    Text(

                        text = if (
                            imageBase64.isNullOrEmpty()
                        ) {

                            "Capture Attachment"

                        } else {

                            "Attachment Added ✓"
                        },

                        fontWeight = FontWeight.Medium
                    )
                }



                // =====================
                // IMAGE ERROR
                // =====================

                if (imageError) {

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(

                        text = "Attachment required",

                        color = MaterialTheme.colorScheme.error,

                        style =
                            MaterialTheme.typography.bodySmall
                    )
                }



                // =====================
                // PREVIEW BUTTON
                // =====================

                if (!imageBase64.isNullOrEmpty()) {

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    TextButton(

                        modifier = Modifier.align(
                            Alignment.End
                        ),

                        onClick = {

                            onPreviewImage()
                        }

                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Visibility,

                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(

                            text = "Preview",

                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
//}

