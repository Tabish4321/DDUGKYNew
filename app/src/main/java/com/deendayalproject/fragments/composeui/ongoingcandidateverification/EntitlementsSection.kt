package com.deendayalproject.fragments.composeui.ongoingcandidateverification


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.deendayalproject.fragments.composeui.common.ComplianceQuestionWithRemarks
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.CandidateAssessmentViewModel
import com.deendayalproject.viewmodel.InspectionViewModel
import kotlinx.coroutines.launch

@Composable
fun EntitlementsSection(
    viewModel: CandidateAssessmentViewModel,
    snackbarHostState: SnackbarHostState,
    batchId: String,
    candidateId: String,
    inspectionId: Int,
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val state by viewModel.entitlementState.collectAsState()

    /* ---------------------- */
    /* PREFILL VALUES */
    /* ---------------------- */

    var trainingFreeAnswer by remember { mutableStateOf<String?>(null) }
    var bankAccountAnswer by remember { mutableStateOf<String?>(null) }
    var residentialAnswer by remember { mutableStateOf<String?>(null) }
    var trainingMaterialAnswer by remember { mutableStateOf<String?>(null) }
    var uniformAnswer by remember { mutableStateOf<String?>(null) }
    var sanitaryAnswer by remember { mutableStateOf<String?>(null) }
    var medicineAnswer by remember { mutableStateOf<String?>(null) }
    var insuranceAnswer by remember { mutableStateOf<String?>(null) }
    var toFroEntitlementAnswer by remember { mutableStateOf<String?>(null) }


    var trainingFreeRemark by remember { mutableStateOf("") }
    var bankAccountRemark by remember { mutableStateOf("") }
    var residentialRemark by remember { mutableStateOf("") }
    var trainingMaterialRemark by remember { mutableStateOf("") }
    var uniformRemark by remember { mutableStateOf("") }
    var sanitaryRemark by remember { mutableStateOf("") }
    var medicineRemark by remember { mutableStateOf("") }
    var insuranceRemark by remember { mutableStateOf("") }
    var toFroEntitlementRemark by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }

    val isNonResidential = AppUtil.getSavedCenterTypePreference(context) != "Residential"
    val isResidential = AppUtil.getSavedCenterTypePreference(context) == "Residential"


    /* ---------------------- */
    /* LOAD API */
    /* ---------------------- */

    LaunchedEffect(candidateId) {

        viewModel.loadEntitlements(
            batchId = batchId.toInt(),
            inspectionId = inspectionId,
            candidateId = candidateId
        )
    }

    /* ---------------------- */
    /* PREFILL DATA */
    /* ---------------------- */

    LaunchedEffect(state) {

        trainingFreeAnswer = state.trainingFree
        bankAccountAnswer = state.bankAccountOpened
        residentialAnswer = state.entitlementsPaid
        trainingMaterialAnswer = state.receivedFreeTrainingMaterial
        uniformAnswer = state.uniformProvidedinFirstMonth
        sanitaryAnswer = state.padsMasksProvided
        medicineAnswer = state.medicineProvided
        insuranceAnswer = state.insuranceBenefitsProvided
        toFroEntitlementAnswer=state.toFroEntitlementPaid

        trainingFreeRemark = state.trainingFreeRemark
        bankAccountRemark = state.bankAccountOpenedRemark
        residentialRemark = state.entitlementsPaidRemark
        trainingMaterialRemark = state.receivedFreeTrainingMaterialRemark
        uniformRemark = state.uniformProvidedinFirstMonthRemark
        sanitaryRemark = state.padsMasksProvidedRemark
        medicineRemark = state.medicineProvidedRemark
        insuranceRemark = state.insuranceBenefitsProvidedRemark
        toFroEntitlementRemark=state.toFroEntitlementPaidRemark
    }

    /* ---------------------- */
    /* LOADER */
    /* ---------------------- */

    if (state.isLoading) {

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    /* ---------------------- */
    /* SNACKBAR ERROR */
    /* ---------------------- */

    LaunchedEffect(state.error) {

        state.error?.let {
            //snackbarHostState.showSnackbar(it)
            Toast.makeText(context,it, Toast.LENGTH_SHORT).show()

            viewModel.clearEntitlementError()
        }
    }

    LaunchedEffect(state.saveSuccess) {
        if( state.saveSuccess){
            Toast.makeText(context,"Entitlements saved successfully", Toast.LENGTH_SHORT).show()
            viewModel.triggerRefresh()
            //snackbarHostState.showSnackbar("Entitlements saved successfully")
            viewModel.clearEntitlementSaveSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        ComplianceQuestionWithRemarks(
            question = "Training free of cost ?",
            answer = trainingFreeAnswer,
            remarks = trainingFreeRemark,
            isError = showError && trainingFreeAnswer == null,
            onAnswerChange = { trainingFreeAnswer = it },
            onRemarksChange = { trainingFreeRemark = it }
        )

        ComplianceQuestionWithRemarks(
            question = "Bank Account Opened as per instructions ?",
            answer = bankAccountAnswer,
            remarks = bankAccountRemark,
            isError = showError && bankAccountAnswer == null,
            onAnswerChange = { bankAccountAnswer = it },
            onRemarksChange = { bankAccountRemark = it }
        )

        if(isResidential){
            ComplianceQuestionWithRemarks(
                question = "Residential Facilities Provided / Entitlements Paid ?",
                answer = residentialAnswer,
                remarks = residentialRemark,
                isError = showError && residentialAnswer == null,
                onAnswerChange = { residentialAnswer = it },
                onRemarksChange = { residentialRemark = it }
            )
        }

        ComplianceQuestionWithRemarks(
            question = "Received Free Training Material ?",
            answer = trainingMaterialAnswer,
            remarks = trainingMaterialRemark,
            isError = showError && trainingMaterialAnswer == null,
            onAnswerChange = { trainingMaterialAnswer = it },
            onRemarksChange = { trainingMaterialRemark = it }
        )

        ComplianceQuestionWithRemarks(
            question = "Uniform Provided in First Month ?",
            answer = uniformAnswer,
            remarks = uniformRemark,
            isError = showError && uniformAnswer == null,
            onAnswerChange = { uniformAnswer = it },
            onRemarksChange = { uniformRemark = it }
        )

        ComplianceQuestionWithRemarks(
            question = "Sanitary Pads/Masks Provided ?",
            answer = sanitaryAnswer,
            remarks = sanitaryRemark,
            isError = showError && sanitaryAnswer == null,
            onAnswerChange = { sanitaryAnswer = it },
            onRemarksChange = { sanitaryRemark = it }
        )

        ComplianceQuestionWithRemarks(
            question = "Medicine Provided if Sick ?",
            answer = medicineAnswer,
            remarks = medicineRemark,
            isError = showError && medicineAnswer == null,
            onAnswerChange = { medicineAnswer = it },
            onRemarksChange = { medicineRemark = it }
        )

        ComplianceQuestionWithRemarks(
            question = "Insurance Benefits Provided ?",
            answer = insuranceAnswer,
            remarks = insuranceRemark,
            isError = showError && insuranceAnswer == null,
            onAnswerChange = { insuranceAnswer = it },
            onRemarksChange = { insuranceRemark = it }
        )

        if(isNonResidential) {
            ComplianceQuestionWithRemarks(
                question = "To & Fro Travel Entitlement Paid ?",
                answer = toFroEntitlementAnswer,
                remarks = toFroEntitlementRemark,
                isError =  showError && toFroEntitlementAnswer == null,
                onAnswerChange = { toFroEntitlementAnswer = it },
                onRemarksChange = { toFroEntitlementRemark = it }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        PremiumSubmitButton {

            showError = true

            scope.launch {

                when {

                    trainingFreeAnswer == null ->
                        snackbarHostState.showSnackbar("Please select: Training free of cost")

                    bankAccountAnswer == null ->
                        snackbarHostState.showSnackbar("Please select: Bank Account Opened")

                    isResidential&& residentialAnswer == null ->
                        snackbarHostState.showSnackbar("Please select: Residential Facilities Provided")

                    trainingMaterialAnswer == null ->
                        snackbarHostState.showSnackbar("Please select: Received Free Training Material")

                    uniformAnswer == null ->
                        snackbarHostState.showSnackbar("Please select: Uniform Provided in First Month")

                    sanitaryAnswer == null ->
                        snackbarHostState.showSnackbar("Please select: Sanitary Pads/Masks Provided")

                    medicineAnswer == null ->
                        snackbarHostState.showSnackbar("Please select: Medicine Provided if Sick")

                    insuranceAnswer == null ->
                        snackbarHostState.showSnackbar("Please select: Insurance Benefits Provided")

                 //   toFroEntitlementAnswer == null -> snackbarHostState.showSnackbar("Please select: Io & Fro Travel Entitlement Paid")
                    isNonResidential && toFroEntitlementAnswer == null ->
                        snackbarHostState.showSnackbar("Please select: To & Fro Travel Entitlement Paid")

                    else -> {

                        viewModel.updateEntitlementState(
                            trainingFreeAnswer!!,
                            bankAccountAnswer!!,
                            residential = if(isResidential) residentialAnswer!! else "",
                            trainingMaterialAnswer!!,
                            uniformAnswer!!,
                            sanitaryAnswer!!,
                            medicineAnswer!!,
                            insuranceAnswer!!,
                            toEntitlementPaid = if (isNonResidential) toFroEntitlementAnswer!! else "",
                            trainingFreeRemark,
                            bankAccountRemark,
                            if(isResidential) residentialRemark else "",
                            trainingMaterialRemark,
                            uniformRemark,
                            sanitaryRemark,
                            medicineRemark,
                            insuranceRemark,
                            if (isNonResidential) toFroEntitlementRemark else ""
                        )

                        viewModel.saveEntitlements(
                            batchId = batchId.toInt(),
                            inspectionId = inspectionId,
                            candidateId = candidateId
                        )
                    }
                }
            }
        }
    }
}