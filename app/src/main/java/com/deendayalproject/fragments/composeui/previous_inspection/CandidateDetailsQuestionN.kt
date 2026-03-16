package com.deendayalproject.fragments.composeui.previous_inspection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.MultiLineEditText
import com.deendayalproject.fragments.composeui.common.NumericTextField
import com.deendayalproject.fragments.composeui.common.PremiumOption
import com.deendayalproject.fragments.composeui.common.PremiumSelector
import com.deendayalproject.model.QuestionConfig
import com.deendayalproject.model.getAnswer
import com.deendayalproject.model.getRemarks
import com.deendayalproject.model.uistate.CandidateVerificationUiState
import com.deendayalproject.viewmodel.CandidateVerificationViewModel

@Composable
fun CandidateDetailsQuestionN(
    condidateVerificationViewModel: CandidateVerificationViewModel,
    question: QuestionConfig,
    state: CandidateVerificationUiState,
    onAnswerChange: (String) -> Unit,
    onRemarksChange: (String) -> Unit
) {

    val answer = state.getAnswer(question.id)
    val remarks = state.getRemarks(question.id)

    val isError = state.showValidation && answer == null

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                text = question.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (isError)
                    MaterialTheme.colorScheme.error
                else
                    Color(0xFF111827)
            )

            /* ------------------ ANSWER SELECTOR ------------------ */

            if (question.id == "candidateStatus") {

                PremiumSelector(
                    options = listOf(
                        PremiumOption("Working", Color(0xFF22C55E)),
                        PremiumOption("Not Working", Color(0xFFEF4444))
                    ),
                    selected = answer,
                    onSelect = onAnswerChange
                )

            } else {

                PremiumSelector(
                    options = listOf(
                        PremiumOption("Yes", Color(0xFF22C55E)),
                        PremiumOption("No", Color(0xFFEF4444))
                    ),
                    selected = answer,
                    onSelect = onAnswerChange
                )
            }

            /* ------------------ REMARKS ------------------ */

            if (answer == "No") {

                MultiLineEditText(
                    value = remarks,
                    onValueChange = onRemarksChange,
                    label = "Remarks",
                    isRequired = true,
                    isError = isError && remarks.isBlank()
                )
            }

            /* ------------------ SALARY FIELD ------------------ */

            if (question.id == "joinedJob" && answer == "Yes" && state.joinedJob?.equals("Yes", true) == true) {

                NumericTextField(
                    value = state.salary,
                    onValueChange = {
                        condidateVerificationViewModel.updateState {
                            copy(salary = it)
                        }
                    },
                    label = "If Yes, What is the Salary",
                    isRequired = true,
                    isError = state.showValidation &&
                            state.salary.isBlank(),
                    placeholder = "Enter salary amount"
                )
            }

            /* ------------------ OJT DETAILS ------------------ */

            if (question.id == "ojtEntitlementReceived" && answer == "Yes" && state.ojtEntitlementReceived?.equals("Yes", true) == true) {

                MultiLineEditText(
                    value = state.ojtEntitlementDetails,
                    onValueChange = {
                        condidateVerificationViewModel.updateState {
                            copy(ojtEntitlementDetails = it)
                        }
                    },
                    label = "If Yes, Details",
                    isRequired = true,
                    isError = state.showValidation &&
                            state.ojtEntitlementDetails.isBlank()
                )
            }


                /* -------- WORKING MONTHS -------- */

                if (question.id == "candidateStatus" && answer == "Working") {

                    NumericTextField(
                        value = state.workingMonths,
                        onValueChange = {
                            condidateVerificationViewModel.updateState {
                                copy(workingMonths = it)
                            }
                        },
                        label = "Number of months working",
                        isRequired = true,
                        isError = state.showValidation &&
                                state.workingMonths.isBlank(),
                        placeholder = "Enter months"
                    )
                }

                /* -------- NOT WORKING REASON -------- */

                if (question.id == "candidateStatus" && answer == "Not Working") {

                    MultiLineEditText(
                        value = state.notWorkingReason,
                        onValueChange = {
                            condidateVerificationViewModel.updateState {
                                copy(notWorkingReason = it)
                            }
                        },
                        label = "Reason for leaving job",
                        isRequired = true,
                        isError = state.showValidation &&
                                state.notWorkingReason.isBlank()
                    )
                }
            }

        }
  //  }
}