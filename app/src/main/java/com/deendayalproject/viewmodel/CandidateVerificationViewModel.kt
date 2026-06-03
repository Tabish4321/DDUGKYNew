package com.deendayalproject.viewmodel


import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deendayalproject.BuildConfig
import com.deendayalproject.model.request.PreviousInsQuesReqN
import com.deendayalproject.model.request.SaveBatchVerificationRequest
import com.deendayalproject.model.request.assesmentInspection.InsertInspectionFinalDetailsRequest
import com.deendayalproject.model.request.assesmentInspection.PreviousInspectionObservationRequest
import com.deendayalproject.model.response.CandidateInspectionDetails
import com.deendayalproject.model.uistate.*
import com.deendayalproject.repository.CandidateerificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Rishi Porwal
 */



class CandidateVerificationViewModel(
    application: Application
) : AndroidViewModel(application){

    private val repository = CandidateerificationRepository(application)

    private val _uiState = MutableStateFlow(CandidateVerificationUiState())
    val uiState: StateFlow<CandidateVerificationUiState> = _uiState.asStateFlow()

    /* ------------------------------------------------ */
    /* 🔹 LOAD CANDIDATE DETAILS */
    /* ------------------------------------------------ */

    fun loadCandidateDetails(
        batchId: Int,
        inspectionId: Int,
        candidateId: String
    ) {
        viewModelScope.launch {
            _uiState.value = CandidateVerificationUiState()

            _uiState.value = _uiState.value.copy(
                showValidation = false,
                isLoading = true,
                error = null,
                saveSuccess = false

            )

            val result = repository.getCandidateDetails(
                GetCandidateInspectionRequest(
                    appVersion = BuildConfig.VERSION_NAME,
                    batchId = batchId,
                    inspectionId = inspectionId,
                    candidateId = candidateId
                )
            )

            result.onSuccess { list ->

                val dto = list.firstOrNull()

                if (dto != null) {
                    _uiState.value = mapDtoToUiState(dto)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No data available",
                                showValidation = false
                    )
                }

            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Something went wrong",
                    showValidation = false
                )
            }
        }
    }

    /* ------------------------------------------------ */
    /* 🔹 SAVE VERIFICATION */
    /* ------------------------------------------------ */

    fun saveVerification(
        batchId: Int,
        inspectionId: Int,
        candidateId: String
    ) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                saveSuccess = false
            )

            val request = mapUiStateToSaveRequest(
                batchId,
                inspectionId,
                candidateId
            )

            val result = repository.saveVerification(request)

            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    saveSuccess = true
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Save failed"
                )
            }
        }
    }

    /* ------------------------------------------------ */
    /* 🔹 UPDATE FIELD GENERIC */
    /* ------------------------------------------------ */

    fun updateState(update: CandidateVerificationUiState.() -> CandidateVerificationUiState) {
        _uiState.update { it.update() }
    }
    /* ------------------------------------------------ */
    /* 🔹 CLEAR STATES */
    /* ------------------------------------------------ */

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearErrorValidation() {
        _uiState.value = _uiState.value.copy(showValidation = false)
    }

    fun clearSaveState() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }

    fun resetForm() {
        _uiState.value = CandidateVerificationUiState()
    }

    /* ------------------------------------------------ */
    /* 🔹 DTO → UI STATE */
    /* ------------------------------------------------ */

    private fun mapDtoToUiState(dto: CandidateInspectionDetails): CandidateVerificationUiState {
        return CandidateVerificationUiState(
            isLoading = false,

            externalAssessment = dto.externalAssessment,
            externalAssessmentRemarks = dto.externalAssessmentRemark ?: "",

            passedFailed = dto.passedFailed,
            passedFailedRemarks = dto.passedFailedRemark ?: "",

            certificateReceived = dto.receivedCertificate,
            certificateReceivedRemarks = dto.receivedCertificateRemark ?: "",

            ojtJoined = dto.ojtJoined,
            ojtJoinedRemarks = dto.ojtJoinedRemark ?: "",

            ojtCertificateReceived = dto.ojtCertificateReceived,
            ojtCertificateReceivedRemarks = dto.ojtCertificateReceivedRemark ?: "",

            ojtEntitlementReceived = dto.ojtEntitlementReceived,
            ojtEntitlementRemarks = dto.ojtEntitlementReceivedRemark ?: "",

            ojtEntitlementDetails = dto.entitlementDetails ?: "",

            ojtVerificationDone = dto.ojtVerification,
            ojtVerificationRemarks = dto.ojtVerificationRemark ?: "",

            performancePlanFilled = dto.performanceEvaluationPlan,
            performancePlanRemarks = dto.performanceEvaluationPlanRemark ?: "",

            offerLetterReceived = dto.gotOfferLetter,
            offerLetterRemarks = dto.gotOfferLetterRemark ?: "",

            joinedJob = dto.jobJoined,
            joinedJobRemarks = dto.jobJoinedRemark ?: "",

            salary = dto.salary?.toString() ?: "",
           // salaryRemark = dto.salaryRemark ?: "",

            minimumWageMatch = dto.matchWithMinimumWagesOfTheState,
            minimumWageRemarks = dto.matchWithMinimumWagesOfTheStateRemark ?: "",

            currentStatus = dto.candidateCurrentStatus,
            currentStatusRemarks = dto.candidateCurrentStatusRemark ?: "",

            workingMonths = dto.workingMonthNumbers?.toString() ?: "",
            notWorkingReason = dto.reasonForNotWorking ?: "",

            ppsDisbursed = dto.pssAmountDisbursedToTheCandidate,
            ppsDisbursedRemarks = dto.pssAmountDisbursedToTheCandidateRemark ?: "",

            replacementAction = dto.replacementActionByPia ?: ""
        )
    }

    /* ------------------------------------------------ */
    /* 🔹 UI STATE → SAVE REQUEST */
    /* ------------------------------------------------ */

    private fun mapUiStateToSaveRequest(
        batchId: Int,
        inspectionId: Int,
        candidateId: String
    ): SaveBatchVerificationRequest {

        val state = _uiState.value

        return SaveBatchVerificationRequest(
            appVersion = BuildConfig.VERSION_NAME,
            batchId = batchId,
            inspectionId = inspectionId,
            candidateId = candidateId,

            externalAssessment = state.externalAssessment,
            externalAssessmentRemark = state.externalAssessmentRemarks,

            passedFailed = state.passedFailed,
            passedFailedRemark = state.passedFailedRemarks,

            receivedCertificate = state.certificateReceived,
            receivedCertificateRemark = state.certificateReceivedRemarks,

            ojtJoined = state.ojtJoined,
            ojtJoinedRemark = state.ojtJoinedRemarks,

            ojtCertificateReceived = state.ojtCertificateReceived,
            ojtCertificateReceivedRemark = state.ojtCertificateReceivedRemarks,

            ojtEntitlementReceived = state.ojtEntitlementReceived,
            ojtEntitlementReceivedRemark = state.ojtEntitlementRemarks,

            entitlementDetails = state.ojtEntitlementDetails,

            ojtVerification = state.ojtVerificationDone,
            ojtVerificationRemark = state.ojtVerificationRemarks,

            performanceEvaluationPlan = state.performancePlanFilled,
            performanceEvaluationPlanRemark = state.performancePlanRemarks,

            gotOfferLetter = state.offerLetterReceived,
            gotOfferLetterRemark = state.offerLetterRemarks,

            jobJoined = state.joinedJob,
            jobJoinedRemark = state.joinedJobRemarks,

            salary = state.salary,
            salaryRemark = "",

            matchWithMinimumWagesOfTheState = state.minimumWageMatch,
            matchWithMinimumWagesOfTheStateRemark = state.minimumWageRemarks,

            candidateCurrentStatus = state.currentStatus,
            candidateCurrentStatusRemark = state.currentStatusRemarks,

            workingMonthNumbers = state.workingMonths.toIntOrNull(),
            reasonForNotWorking = state.notWorkingReason,

            pssAmountDisbursedToTheCandidate = state.ppsDisbursed,
            pssAmountDisbursedToTheCandidateRemark = state.ppsDisbursedRemarks,

            replacementActionByPia = state.replacementAction
        )
    }

    /*Final Sumbit Observation data*/



    private val _statefinal =
        MutableStateFlow(PreviousInspectionObservationUiState())

    val state = _statefinal.asStateFlow()


    fun loadObservation(
        inspectionId: Int
    ) {

        viewModelScope.launch {

            _statefinal.update {
                it.copy(isLoading = true)
            }

            val result =
                repository.getPreviousInspectionObservation(

                    PreviousInsQuesReqN(
                        BuildConfig.VERSION_NAME,
                        inspectionId
                    )
                )

            result.onSuccess { list ->

                val dto = list.firstOrNull()

                if (dto != null) {

                    val sections = buildMap {

                        dto.DocumentsStandardFormsAvailabilitySaction?.let {
                            put("Documents", it)
                        }

                        dto.TrainingQualitySection?.let {
                            put("TrainingQuality", it)
                        }

                        dto.ValidateTrainerAttendanceSaction?.let {
                            put("TrainerAttendance", it)
                        }

                        dto.PreviousBatchDataVerificationSaction?.let {
                            put("PreviousBatch", it)
                        }

                        dto.OngoingBatchCandidateSection?.let {
                            put("OngoingBatchCandidate", it)
                        }

                    }

                    _statefinal.update {

                        it.copy(
                            isLoading = false,
                            sections = sections
                        )
                    }

                }

            }.onFailure {

                _statefinal.update {
                    it.copy(
                        isLoading = false,
                        error = it.error
                    )
                }

            }

        }

    }

    fun updateAnswer(
        questionId: Int,
        answer: String
    ) {

        _statefinal.update {

            it.copy(
                answers = it.answers + (questionId to answer)
            )

        }

    }

    fun updateRemark(
        questionId: Int,
        remark: String
    ) {

        _statefinal.update {

            it.copy(
                remarks = it.remarks + (questionId to remark)
            )

        }

    }

    var finalRemark by mutableStateOf("")
        private set

    fun updateFinalRemark(value: String) {
        finalRemark = value
    }

    var finalAttachmentBase64 by mutableStateOf("")
        private set

    var finalAttachmentName by mutableStateOf("")
        private set


    fun updateFinalAttachment( base64: String, fileName: String ) {
        finalAttachmentBase64 = base64
        finalAttachmentName = fileName
    }






    fun submitFinal(
        inspectionId: Int,
        trainingCenterId: Int,
        remark: String,
        finalRemarkAttachment:String

    ) {
        viewModelScope.launch {

            _statefinal.update {
                it.copy(isLoading = true)
            }

            val result =
                repository.insertInspectionFinalDetails(
                    InsertInspectionFinalDetailsRequest(
                        BuildConfig.VERSION_NAME,
                        inspectionId,
                        trainingCenterId,
                        remark,
                        finalRemarkAttachment
                    )
                )

            result.onSuccess { response ->

                if (response.responseCode == 200) {

                    _statefinal.update {

                        it.copy(
                            isLoading = false,
                            submitSuccess = true
                        )

                    }

                } else {

                    _statefinal.update {

                        it.copy(
                            isLoading = false,
                            error = response.responseDesc
                        )

                    }

                }

            }.onFailure { error ->

                _statefinal.update {

                    it.copy(
                        isLoading = false,
                        error = error.message ?: "Something went wrong"
                    )

                }

            }

        }

    }


    fun clearFinalError() {

        _statefinal.update {
            it.copy(error = null)
        }

    }

    fun clearFinalSuccess() {

        _statefinal.update {
            it.copy(submitSuccess = false)
        }

    }






}