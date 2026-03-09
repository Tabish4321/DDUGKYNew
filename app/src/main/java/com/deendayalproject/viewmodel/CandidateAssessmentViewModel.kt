package com.deendayalproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.deendayalproject.BuildConfig
import com.deendayalproject.model.request.assesmentInspection.AssessmentStatusInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.GetCandidateAssessmentInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.GetCandidateRecordsVerificationRequest
import com.deendayalproject.model.request.assesmentInspection.GetDistributedLearningMaterialInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.GetEntitlementsDistributionInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.GetInspectionSectionStatusRequest
import com.deendayalproject.model.request.assesmentInspection.GetResidentialFacilityVerificationRequest
import com.deendayalproject.model.request.assesmentInspection.SaveCandidateAssessmentInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.SaveDistributedLearningMaterialInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.SaveEntitlementsDistributionInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.SaveResidentialFacilityVerificationRequest
import com.deendayalproject.model.response.CandidateAssessmentResponse.CandidateAssessmentInspectionDetails
import com.deendayalproject.model.response.CandidateAssessmentResponse.CandidateRecordsVerificationDetails
import com.deendayalproject.model.response.CandidateAssessmentResponse.DistributedLearningMaterialInspectionResponse
import com.deendayalproject.model.uistate.CandidateAssessmentStatusUiState
import com.deendayalproject.model.uistate.CandidateAssessmentUiState
import com.deendayalproject.model.uistate.CandidateRecordsVerificationUiState
import com.deendayalproject.model.uistate.DistributedLearningMaterialUiState
import com.deendayalproject.model.uistate.EntitlementsDistributionUiState
import com.deendayalproject.model.uistate.InspectionSectionStatusUiState
import com.deendayalproject.model.uistate.ResidentialFacilityUiState
import com.deendayalproject.model.uistate.TlmQuestion
import com.deendayalproject.repository.CandidateAssessmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Rishi Porwal
 */
class CandidateAssessmentViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = CandidateAssessmentRepository(application)

    /* Inspection Section Status */
    private val _uiSectionStatus = MutableStateFlow(InspectionSectionStatusUiState())

    val uiSectionStatus: StateFlow<InspectionSectionStatusUiState> = _uiSectionStatus.asStateFlow()

    fun loadInspectionSectionStatus(
        inspectionId: Int,
        candidateId: String
    ) {

        viewModelScope.launch {
            _uiSectionStatus.value =
                _uiSectionStatus.value.copy(isLoading = true, error = null)

            val result = repository.getInspectionSectionStatus(
                GetInspectionSectionStatusRequest(
                    appVersion = BuildConfig.VERSION_NAME,
                    candidateId = candidateId,
                    inspectionId = inspectionId
                )
            )

            result.onSuccess { list ->

                val dto = list.firstOrNull()

                if (dto != null) {

                    _uiSectionStatus.value =
                        _uiSectionStatus.value.copy(

                            recordStatus = dto.recordStatus,
                            attendanceStatus = dto.attendanceStatus,
                            assessmentStatus = dto.assessmentStatus,
                            learningMaterialStatus = dto.learningMaterialStatus,
                            entitlementsDistributionStatus = dto.entitlementsDistributionStatus,
                            rfVerificationStatus = dto.rfVerificationStatus,

                            isLoading = false
                        )

                } else {

                    _uiSectionStatus.value =
                        _uiSectionStatus.value.copy(
                            isLoading = false,
                            error = "No data available"
                        )
                }

            }.onFailure { e ->

                _uiSectionStatus.value =
                    _uiSectionStatus.value.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong"
                    )
            }
        }
    }


    private val _uiRecordState = MutableStateFlow(CandidateRecordsVerificationUiState())
    val uiRecordState: StateFlow<CandidateRecordsVerificationUiState> = _uiRecordState.asStateFlow()


    /* ------------------------------ */
    /* LOAD RECORD VERIFICATION */
    /* ------------------------------ */

    fun loadRecordsVerification(
        batchId: Int,
        inspectionId: Int,
        candidateId: String
    ) {

        viewModelScope.launch {

            _uiRecordState.value = _uiRecordState.value.copy(
                isLoading = true,
                error = null
            )

            val result = repository.getCandidateRecordsVerification(
                GetCandidateRecordsVerificationRequest(
                    appVersion = BuildConfig.VERSION_NAME,
                    candidateId = candidateId,
                    inspectionId = inspectionId,
                    batchId = batchId
                )
            )

            result.onSuccess { list ->

                val dto = list.firstOrNull()

                if (dto != null) {
                    _uiRecordState.value = mapDtoToRecordUiState(dto)
                } else {
                    _uiRecordState.value = _uiRecordState.value.copy(
                        isLoading = false,
                        error = "No data available"
                    )
                }

            }.onFailure { e ->

                _uiRecordState.value = _uiRecordState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Something went wrong"
                )

            }
        }
    }


    private fun mapDtoToRecordUiState(
        dto: CandidateRecordsVerificationDetails
    ): CandidateRecordsVerificationUiState {

        return _uiRecordState.value.copy(

            inspectionId = dto.inspectionId,
            batchId = dto.batchId,
            candidateId = dto.candidateId,

            povertyProof = dto.povertyProof,
            povertyProofRemark = dto.povertyProofRemark ?: "",

            categoryProof = dto.categoryProof,
            categoryProofRemark = dto.categoryProofRemark ?: "",

            minorityProof = dto.minorityProof,
            minorityProofRemark = dto.minorityProofRemark ?: "",

            educationProof = dto.educationProof,
            educationProofRemark = dto.educationProofRemark ?: "",

            pwdProof = dto.pwdProof,
            pwdProofRemark = dto.pwdProofRemark ?: "",

            isLoading = false
        )
    }

    fun updateRecordState(
        update: CandidateRecordsVerificationUiState.() -> CandidateRecordsVerificationUiState
    ) {
        _uiRecordState.update { it.update() }
    }


    private val _uiState = MutableStateFlow(CandidateAssessmentUiState())
    val uiState: StateFlow<CandidateAssessmentUiState> = _uiState.asStateFlow()

    private val _uiStatusState = MutableStateFlow(CandidateAssessmentStatusUiState())
    val uiStatusState: StateFlow<CandidateAssessmentStatusUiState> = _uiStatusState.asStateFlow()


    /* ------------------------------------ */
    /* LOAD ASSESSMENT STATUS */
    /* ------------------------------------ */
    fun loadAssessmentStatus(
        batchId: Int,
        inspectionId: Int,
        candidateId: String
    ) {

        viewModelScope.launch {

            val result = repository.getAssessmentStatus(
                AssessmentStatusInspectionRequest(
                    appVersion = BuildConfig.VERSION_NAME,
                    candidateId = candidateId,
                    batchId = batchId,
                    inspectionId = inspectionId
                )
            )

            result.onSuccess { list ->

                val status = list.firstOrNull()

                if (status != null) {

                    _uiStatusState.value = _uiStatusState.value.copy(
                        assessmentStatus = status.assessmentStatus,
                        isPresent = status.isPresent,
                        assessmentDate = status.assessmentDate
                    )

                }

            }.onFailure { e ->

                _uiStatusState.value = _uiStatusState.value.copy(
                    error = e.message ?: "Status load failed"
                )

            }
        }
    }


    /* ------------------------------------ */
    /* LOAD ASSESSMENT DETAILS */
    /* ------------------------------------ */
    fun loadAssessmentDetails(
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

            val result = repository.getAssessmentInspection(
                GetCandidateAssessmentInspectionRequest(
                    appVersion = BuildConfig.VERSION_NAME,
                    batchId = batchId,
                    inspectionId = inspectionId,
                    candidateId = candidateId
                )
            )

            result.onSuccess { list ->
                val dto = list.firstOrNull()
                if (dto != null) {
                    _uiState.value = mapDtoToSaveUiState(dto)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No data available"
                    )
                }

            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Something went wrong"
                )
            }
        }
    }

    /* ------------------------------------ */
    /* SAVE */
    /* ------------------------------------ */
    fun saveAssessment(
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

            val result = repository.saveAssessmentInspection(request)

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

    fun updateState(
        update: CandidateAssessmentUiState.() -> CandidateAssessmentUiState
    ) {
        _uiState.update { it.update() }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearSaveState() {
        _uiState.update { it.copy(saveSuccess = false) }
    }

    private fun mapUiStateToSaveRequest(
        batchId: Int,
        inspectionId: Int,
        candidateId: String
    ): SaveCandidateAssessmentInspectionRequest {

        val state = _uiState.value

        return SaveCandidateAssessmentInspectionRequest(

            appVersion = BuildConfig.VERSION_NAME,
            candidateId = candidateId,
            batchId = batchId,
            inspectionId = inspectionId,

            presentAssessmentQid = 1,
            presentAssessment = state.presentAssessment ?: "",
            presentAssessmentRemark = state.presentAssessmentRemark,

            cameraVerifiedQid = 2,
            cameraVerified = state.cameraVerified ?: "",
            cameraVerifiedRemark = state.cameraVerifiedRemark,

            seriousnessQid = 3,
            seriousness = state.seriousness ?: "",
            seriousnessRemark = state.seriousnessRemark,

            malpracticesObservedQid = 4,
            malpracticesObserved = state.malpracticesObserved ?: "",
            malpracticesObservedRemark = state.malpracticesObservedRemark,

            actualAndRevaluationMarksQid = 5,
            actualAndRevaluationMarks = state.actualAndRevaluationMarks ?: "",
            actualAndRevaluationMarksRemark = state.actualAndRevaluationMarksRemark,

            retestMarksDifferenceQid = 6,
            retestMarksDifference = state.retestMarksDifference ?: "",
            retestMarksDifferenceRemark = state.retestMarksDifferenceRemark
        )
    }


    private fun mapDtoToSaveUiState(
        dto: CandidateAssessmentInspectionDetails
    ): CandidateAssessmentUiState {

        val current = _uiState.value

        return current.copy(

//            inspectionId = dto.inspectionId,
//            batchId = dto.batchId,
//            candidateId = dto.candidateId,

            /* ----------------------------- */
            /* Q1 PRESENT DURING ASSESSMENT */
            /* ----------------------------- */

            presentAssessmentQid = dto.presentAssessmentQid,
            presentAssessment = dto.presentAssessment,
            presentAssessmentRemark = dto.presentAssessmentRemark ?: "",

            /* ----------------------------- */
            /* Q2 CAMERA VERIFIED */
            /* ----------------------------- */

            cameraVerifiedQid = dto.cameraVerifiedQid,
            cameraVerified = dto.cameraVerified,
            cameraVerifiedRemark = dto.cameraVerifiedRemark ?: "",

            /* ----------------------------- */
            /* Q3 SERIOUSNESS */
            /* ----------------------------- */

            seriousnessQid = dto.seriousnessQid,
            seriousness = dto.seriousness,
            seriousnessRemark = dto.seriousnessRemark ?: "",

            /* ----------------------------- */
            /* Q4 MALPRACTICE OBSERVED */
            /* ----------------------------- */

            malpracticesObservedQid = dto.malpracticesObservedQid,
            malpracticesObserved = dto.malpracticesObserved,
            malpracticesObservedRemark = dto.malpracticesObservedRemark ?: "",

            /* ----------------------------- */
            /* Q5 ACTUAL VS REVALUATION */
            /* ----------------------------- */

            actualAndRevaluationMarksQid = dto.actualAndRevaluationMarksQid,
            actualAndRevaluationMarks = dto.actualAndRevaluationMarks,
            actualAndRevaluationMarksRemark = dto.actualAndRevaluationMarksRemark ?: "",

            /* ----------------------------- */
            /* Q6 RETEST DIFFERENCE */
            /* ----------------------------- */

            retestMarksDifferenceQid = dto.retestMarksDifferenceQid,
            retestMarksDifference = dto.retestMarksDifference,
            retestMarksDifferenceRemark = dto.retestMarksDifferenceRemark ?: "",

            /* ----------------------------- */
            /* UI STATES */
            /* ----------------------------- */

            isLoading = false,
            error = null
        )
    }


    /*-------Distributed Learning--------------*/
    private val _distributedLerningState = MutableStateFlow(DistributedLearningMaterialUiState())
    val distributedLerningState: StateFlow<DistributedLearningMaterialUiState> =
        _distributedLerningState.asStateFlow()


    fun loadDistributedInspection(batchId: Int, inspectionId: Int, candidateId: String) {

        viewModelScope.launch {

            _distributedLerningState.update {
                it.copy(isLoading = true)
            }

            val result = repository.getDistributedLearningMaterialInspection(
                GetDistributedLearningMaterialInspectionRequest(
                    BuildConfig.VERSION_NAME,
                    candidateId,
                    batchId,
                    inspectionId
                )
            )

            result.onSuccess {
                val dto = it.firstOrNull()

                if (dto != null) {
                    _distributedLerningState.value = mapTlmDtoToUiState(dto)
                } else {
                    _distributedLerningState.update {
                        it.copy(
                            isLoading = false,
                            error = "No data available"
                        )
                    }
                }
            }

            result.onFailure {

                _distributedLerningState.update {
                    it.copy(
                        isLoading = false,
                        error = it.error
                    )
                }
            }
        }
    }

    private fun mapTlmDtoToUiState(
        dto: DistributedLearningMaterialInspectionResponse
    ): DistributedLearningMaterialUiState {

        return DistributedLearningMaterialUiState(

            inspectionId = dto.inspectionId,
            batchId = dto.batchId,
            candidateId = dto.candidateId,

            domainCurriculum = dto.domainCurriculum,
            domainCurriculumRemark = dto.domainCurriculumRemark ?: "",
            domainCurriculumImage = dto.domainCurriculumImage,

            bilingualTlmItSkills = dto.bilingualTlmItSkills,
            bilingualTlmItSkillsRemark = dto.bilingualTlmItSkillsRemark ?: "",
            bilingualTlmItSkillsImage = dto.bilingualTlmItSkillsImage,

            bilingualTlmSoftSkills = dto.bilingualTlmSoftSkills,
            bilingualTlmSoftSkillsRemark = dto.bilingualTlmSoftSkillsRemark ?: "",
            bilingualTlmSoftSkillsImage = dto.bilingualTlmSoftSkillsImage,

            bilingualTlmEnglishSkills = dto.bilingualTlmEnglishSkills,
            bilingualTlmEnglishSkillsRemark = dto.bilingualTlmEnglishSkillsRemark ?: "",
            bilingualTlmEnglishSkillsImage = dto.bilingualTlmEnglishSkillsImage,

            trainingKit = dto.trainingKit,
            trainingKitRemark = dto.trainingKitRemark ?: "",
            trainingKitImage = dto.trainingKitImage,

            bilingualIDCard = dto.bilingualIDCard,
            bilingualIDCardRemark = dto.bilingualIDCardRemark ?: "",
            bilingualIDCardImage = dto.bilingualIDCardImage,

            practicalLearningProvided = dto.practicalLearningProvided,
            practicalLearningProvidedRemark = dto.practicalLearningProvidedRemark ?: "",
            practicalLearningProvidedImage = dto.practicalLearningProvidedImage,

            usageLabEquipment = dto.usageLabEquipment,
            usageLabEquipmentRemark = dto.usageLabEquipmentRemark ?: "",
            usageLabEquipmentImage = dto.usageLabEquipmentImage,

            tabletsUploaded = dto.tabletsUploaded,
            tabletsUploadedRemark = dto.tabletsUploadedRemark ?: "",
            tabletsUploadedImage = dto.tabletsUploadedImage,

            ipEnabledCameraFootageAssessments = dto.ipEnabledCameraFootageAssessments,
            ipEnabledCameraFootageAssessmentsRemark = dto.ipEnabledCameraFootageAssessmentsRemark
                ?: "",
            ipEnabledCameraFootageAssessmentsImage = dto.ipEnabledCameraFootageAssessmentsImage,

            isLoading = false
        )
    }


    fun clearDistributedError() {

        _distributedLerningState.update {
            it.copy(error = null)
        }
    }


    fun saveDistributedInspection(
        batchId: Int,
        inspectionId: Int,
        candidateId: String
    ) {

        viewModelScope.launch {
            _distributedLerningState.update {
                it.copy(isLoading = true)
            }

            val state = _distributedLerningState.value

            val request = SaveDistributedLearningMaterialInspectionRequest(

                appVersion = BuildConfig.VERSION_NAME,
                candidateId = candidateId,
                batchId = batchId,
                inspectionId = inspectionId,

                domainCurriculumQid = 1,
                domainCurriculum = state.domainCurriculum ?: "",
                domainCurriculumRemark = state.domainCurriculumRemark,
                domainCurriculumAttachment = state.domainCurriculumImage,

                bilingualTlmItSkillsQid = 2,
                bilingualTlmItSkills = state.bilingualTlmItSkills ?: "",
                bilingualTlmItSkillsRemark = state.bilingualTlmItSkillsRemark,
                bilingualTlmItSkillsAttachment = state.bilingualTlmItSkills,

                bilingualTlmSoftSkillsQid = 3,
                bilingualTlmSoftSkills = state.bilingualTlmSoftSkills ?: "",
                bilingualTlmSoftSkillsRemark = state.bilingualTlmSoftSkillsRemark,
                bilingualTlmSoftSkillsAttachment = state.bilingualTlmSoftSkillsImage,

                bilingualTlmEnglishSkillsQid = 4,
                bilingualTlmEnglishSkills = state.bilingualTlmEnglishSkills ?: "",
                bilingualTlmEnglishSkillsRemark = state.bilingualTlmEnglishSkillsRemark,
                bilingualTlmEnglishSkillsAttachment = state.bilingualTlmEnglishSkillsImage,

                trainingKitQid = 5,
                trainingKit = state.trainingKit ?: "",
                trainingKitRemark = state.trainingKitRemark,
                trainingKitAttachment = state.trainingKitImage,

                bilingualIDCardQid = 6,
                bilingualIDCard = state.bilingualIDCard ?: "",
                bilingualIDCardRemark = state.bilingualIDCardRemark,
                bilingualIDCardAttachment = state.bilingualIDCardImage,

                practicalLearningProvidedQid = 7,
                practicalLearningProvided = state.practicalLearningProvided ?: "",
                practicalLearningProvidedRemark = state.practicalLearningProvidedRemark,
                practicalLearningProvidedAttachment = state.practicalLearningProvidedImage,

                usageLabEquipmentQid = 8,
                usageLabEquipment = state.usageLabEquipment ?: "",
                usageLabEquipmentRemark = state.usageLabEquipmentRemark,
                usageLabEquipmentAttachment = state.usageLabEquipmentImage,

                tabletsUploadedQid = 9,
                tabletsUploaded = state.tabletsUploaded ?: "",
                tabletsUploadedRemark = state.tabletsUploadedRemark,
                tabletsUploadedAttachment = state.tabletsUploadedImage,

                ipEnabledCameraFootageAssessmentsQid = 10,
                ipEnabledCameraFootageAssessments = state.ipEnabledCameraFootageAssessments ?: "",
                ipEnabledCameraFootageAssessmentsRemark = state.ipEnabledCameraFootageAssessmentsRemark,
                ipEnabledCameraFootageAssessmentsAttachment = state.ipEnabledCameraFootageAssessmentsImage
            )

            val result = repository.saveDistributedLearningMaterialInspection(request)

            result.onSuccess {

                _distributedLerningState.value =
                    _distributedLerningState.value.copy(
                        saveSuccess = true,
                        isLoading = false,

                        )


            }

            result.onFailure {

                _distributedLerningState.value =
                    _distributedLerningState.value.copy(
                        error = it.message,
                        isLoading = false,

                        )
            }
        }
    }


    fun updateDistributedLearningState(questions: List<TlmQuestion>) {

        _distributedLerningState.update {
            it.copy(

                domainCurriculum = questions[0].answer,
                domainCurriculumRemark = questions[0].remarks,
                domainCurriculumImage = questions[0].imageBase64,

                bilingualTlmItSkills = questions[1].answer,
                bilingualTlmItSkillsRemark = questions[1].remarks,
                bilingualTlmItSkillsImage = questions[1].imageBase64,

                bilingualTlmSoftSkills = questions[2].answer,
                bilingualTlmSoftSkillsRemark = questions[2].remarks,
                bilingualTlmSoftSkillsImage = questions[2].imageBase64,

                bilingualTlmEnglishSkills = questions[3].answer,
                bilingualTlmEnglishSkillsRemark = questions[3].remarks,
                bilingualTlmEnglishSkillsImage = questions[3].imageBase64,

                trainingKit = questions[4].answer,
                trainingKitRemark = questions[4].remarks,
                trainingKitImage = questions[4].imageBase64,

                bilingualIDCard = questions[5].answer,
                bilingualIDCardRemark = questions[5].remarks,
                bilingualIDCardImage = questions[5].imageBase64,

                practicalLearningProvided = questions[6].answer,
                practicalLearningProvidedRemark = questions[6].remarks,
                practicalLearningProvidedImage = questions[6].imageBase64,

                usageLabEquipment = questions[7].answer,
                usageLabEquipmentRemark = questions[7].remarks,
                usageLabEquipmentImage = questions[7].imageBase64,

                tabletsUploaded = questions[8].answer,
                tabletsUploadedRemark = questions[8].remarks,
                tabletsUploadedImage = questions[8].imageBase64,

                ipEnabledCameraFootageAssessments = questions[9].answer,
                ipEnabledCameraFootageAssessmentsRemark = questions[9].remarks,
                ipEnabledCameraFootageAssessmentsImage = questions[9].imageBase64
            )
        }
    }

    /*-------EntitlementsDistribution--------------*/


    private val _entitlementState = MutableStateFlow(EntitlementsDistributionUiState())
    val entitlementState: StateFlow<EntitlementsDistributionUiState> =
        _entitlementState.asStateFlow()


    fun loadEntitlements(batchId: Int, inspectionId: Int, candidateId: String) {
        viewModelScope.launch {
            val result = repository.getEntitlementsDistributionInspection(
                GetEntitlementsDistributionInspectionRequest(
                    BuildConfig.VERSION_NAME,
                    candidateId,
                    batchId,
                    inspectionId
                )
            )

            result.onSuccess {

                val dto = it.firstOrNull()

                if (dto != null) {

                    _entitlementState.value = EntitlementsDistributionUiState(

                        trainingFree = dto.trainingFree,
                        trainingFreeRemark = dto.trainingFreeRemark ?: "",

                        bankAccountOpened = dto.bankAccountOpened,
                        bankAccountOpenedRemark = dto.bankAccountOpenedRemark ?: "",

                        entitlementsPaid = dto.entitlementsPaid,
                        entitlementsPaidRemark = dto.entitlementsPaidRemark ?: "",

                        receivedFreeTrainingMaterial = dto.receivedFreeTrainingMaterial,
                        receivedFreeTrainingMaterialRemark = dto.receivedFreeTrainingMaterialRemark
                            ?: "",

                        uniformProvidedinFirstMonth = dto.uniformProvidedinFirstMonth,
                        uniformProvidedinFirstMonthRemark = dto.uniformProvidedinFirstMonthRemark
                            ?: "",

                        padsMasksProvided = dto.padsMasksProvided,
                        padsMasksProvidedRemark = dto.padsMasksProvidedRemark ?: "",

                        medicineProvided = dto.medicineProvided,
                        medicineProvidedRemark = dto.medicineProvidedRemark ?: "",

                        insuranceBenefitsProvided = dto.insuranceBenefitsProvided,
                        insuranceBenefitsProvidedRemark = dto.insuranceBenefitsProvidedRemark ?: ""
                    )
                }
            }
        }
    }


    fun saveEntitlements(batchId: Int, inspectionId: Int, candidateId: String) {

        viewModelScope.launch {

            val state = _entitlementState.value

            val request = SaveEntitlementsDistributionInspectionRequest(

                appVersion = BuildConfig.VERSION_NAME,
                candidateId = candidateId,
                batchId = batchId,
                inspectionId = inspectionId,

                trainingFreeQid = 1,
                trainingFree = state.trainingFree ?: "",
                trainingFreeRemark = state.trainingFreeRemark,

                bankAccountOpenedQid = 2,
                bankAccountOpened = state.bankAccountOpened ?: "",
                bankAccountOpenedRemark = state.bankAccountOpenedRemark,

                entitlementsPaidQid = 3,
                entitlementsPaid = state.entitlementsPaid ?: "",
                entitlementsPaidRemark = state.entitlementsPaidRemark,

                receivedFreeTrainingMaterialQid = 4,
                receivedFreeTrainingMaterial = state.receivedFreeTrainingMaterial ?: "",
                receivedFreeTrainingMaterialRemark = state.receivedFreeTrainingMaterialRemark,

                uniformProvidedinFirstMonthQid = 5,
                uniformProvidedinFirstMonth = state.uniformProvidedinFirstMonth ?: "",
                uniformProvidedinFirstMonthRemark = state.uniformProvidedinFirstMonthRemark,

                padsMasksProvidedQid = 6,
                padsMasksProvided = state.padsMasksProvided ?: "",
                padsMasksProvidedRemark = state.padsMasksProvidedRemark,

                medicineProvidedQid = 7,
                medicineProvided = state.medicineProvided ?: "",
                medicineProvidedRemark = state.medicineProvidedRemark,

                insuranceBenefitsProvidedQid = 8,
                insuranceBenefitsProvided = state.insuranceBenefitsProvided ?: "",
                insuranceBenefitsProvidedRemark = state.insuranceBenefitsProvidedRemark
            )

            repository.saveEntitlementsDistributionInspection(request)
        }
    }


    fun updateEntitlementState(
        trainingFree: String,
        bankAccount: String,
        residential: String,
        trainingMaterial: String,
        uniform: String,
        sanitary: String,
        medicine: String,
        insurance: String,
        trainingFreeRemark: String,
        bankAccountRemark: String,
        residentialRemark: String,
        trainingMaterialRemark: String,
        uniformRemark: String,
        sanitaryRemark: String,
        medicineRemark: String,
        insuranceRemark: String
    ) {

        _entitlementState.update {

            it.copy(

                trainingFree = trainingFree,
                bankAccountOpened = bankAccount,
                entitlementsPaid = residential,
                receivedFreeTrainingMaterial = trainingMaterial,
                uniformProvidedinFirstMonth = uniform,
                padsMasksProvided = sanitary,
                medicineProvided = medicine,
                insuranceBenefitsProvided = insurance,

                trainingFreeRemark = trainingFreeRemark,
                bankAccountOpenedRemark = bankAccountRemark,
                entitlementsPaidRemark = residentialRemark,
                receivedFreeTrainingMaterialRemark = trainingMaterialRemark,
                uniformProvidedinFirstMonthRemark = uniformRemark,
                padsMasksProvidedRemark = sanitaryRemark,
                medicineProvidedRemark = medicineRemark,
                insuranceBenefitsProvidedRemark = insuranceRemark
            )
        }
    }

    fun clearEntitlementError() {

        _entitlementState.update {
            it.copy(error = null)
        }
    }

    /*-------Residential Facility Verification--------------*/


    private val _residentialState = MutableStateFlow(ResidentialFacilityUiState())

    val residentialState: StateFlow<ResidentialFacilityUiState> = _residentialState.asStateFlow()

    fun loadResidentialFacility(
        batchId: Int,
        inspectionId: Int,
        candidateId: String
    ) {

        viewModelScope.launch {

            _residentialState.update { it.copy(isLoading = true) }

            val result = repository.getResidentialFacilityVerification(
                GetResidentialFacilityVerificationRequest(
                    BuildConfig.VERSION_NAME,
                    candidateId,
                    batchId,
                    inspectionId
                )
            )

            result.onSuccess { list ->

                val dto = list.firstOrNull()

                if (dto != null) {

                    val answers = listOf(
                        dto.separateHostels,
                        dto.hostelNameBoardAvailable,
                        dto.contactDetailsBoard,
                        dto.entitlementResponsibilitiesBoard,
                        dto.basicInformationBoard,
                        dto.biometricAttendance,
                        dto.pickupDropFacility,
                        dto.grievanceRegister,
                        dto.individualBed,
                        dto.kitchenDiningHygienic,
                        dto.diningRecreationSpace,
                        dto.toiletSignage,
                        dto.foodQualityHygiene,
                        dto.foodCommitteeFormed,
                        dto.foodAsPerMenu,
                        dto.drinkingWaterAvailable,
                        dto.toiletHygieneMaintained,
                        dto.overheadTankCleaned,
                        dto.quarterlyHealthCheckup,
                        dto.firstAidKit,
                        dto.doctorOnCall,
                        dto.securityWardenPresent,
                        dto.gensetPowerCut,
                        dto.tvCableAvailable,
                        dto.indoorGamesEquipment,
                        dto.wardenPoliceVerification
                    )

                    val remarks = listOf(
                        dto.separateHostelsRemark,
                        dto.hostelNameBoardAvailableRemark,
                        dto.contactDetailsBoardRemark,
                        dto.entitlementResponsibilitiesBoardRemark,
                        dto.basicInformationBoardRemark,
                        dto.biometricAttendanceRemark,
                        dto.pickupDropFacilityRemark,
                        dto.grievanceRegisterRemark,
                        dto.individualBedRemark,
                        dto.kitchenDiningHygienicRemark,
                        dto.diningRecreationSpaceRemark,
                        dto.toiletSignageRemark,
                        dto.foodQualityHygieneRemark,
                        dto.foodCommitteeFormedRemark,
                        dto.foodAsPerMenuRemark,
                        dto.drinkingWaterAvailableRemark,
                        dto.toiletHygieneMaintainedRemark,
                        dto.overheadTankCleanedRemark,
                        dto.quarterlyHealthCheckupRemark,
                        dto.firstAidKitRemark,
                        dto.doctorOnCallRemark,
                        dto.securityWardenPresentRemark,
                        dto.gensetPowerCutRemark,
                        dto.tvCableAvailableRemark,
                        dto.indoorGamesEquipmentRemark,
                        dto.wardenPoliceVerificationRemark
                    )

                    _residentialState.value =
                        ResidentialFacilityUiState(
                            answers = answers,
                            remarks = remarks.map { it ?: "" },
                            washbasins = dto.numberOfWashbasins ?: "",
                            washbasinsRemark = dto.numberOfWashbasinsRemark ?: "",
                            isLoading = false
                        )

                } else {

                    _residentialState.update {
                        it.copy(
                            isLoading = false,
                            error = "No data available"
                        )
                    }
                }
            }
            result.onFailure {
                _residentialState.update {
                    it.copy(
                        isLoading = false,
                        error = it.error
                    )
                }
            }
        }
    }

    fun updateResidentialState(
        answers: List<String>,
        remarks: List<String?>,
        washbasins: String
    ) {

        _residentialState.update {

            it.copy(
                answers = answers,
                remarks = remarks.map { it ?: "" },
                washbasins = washbasins
            )
        }
    }


    fun clearResidentialError() {

        _residentialState.update {
            it.copy(error = null)
        }
    }

    fun clearResidentialSuccess() {

        _residentialState.update {
            it.copy(saveSuccess = false)
        }
    }


    fun saveResidentialFacility(
        batchId: Int,
        inspectionId: Int,
        candidateId: String
    ) {

        viewModelScope.launch {
            _residentialState.update { it.copy(isLoading = true) }

            val state = _residentialState.value

            val request = SaveResidentialFacilityVerificationRequest(

                appVersion = BuildConfig.VERSION_NAME,
                candidateId = candidateId,
                batchId = batchId,
                inspectionId = inspectionId,

                separateHostelsQid = "1",
                separateHostels = state.answers[0] ?: "",
                separateHostelsRemark = state.remarks[0],

                hostelNameBoardAvailableQid = "2",
                hostelNameBoardAvailable = state.answers[1] ?: "",
                hostelNameBoardAvailableRemark = state.remarks[1],

                contactDetailsBoardQid = 3,
                contactDetailsBoard = state.answers[2] ?: "",
                contactDetailsBoardRemark = state.remarks[2],

                entitlementResponsibilitiesBoardQid = 4,
                entitlementResponsibilitiesBoard = state.answers[3] ?: "",
                entitlementResponsibilitiesBoardRemark = state.remarks[3],

                basicInformationBoardQid = 5,
                basicInformationBoard = state.answers[4] ?: "",
                basicInformationBoardRemark = state.remarks[4],

                biometricAttendanceQid = 6,
                biometricAttendance = state.answers[5] ?: "",
                biometricAttendanceRemark = state.remarks[5],

                pickupDropFacilityQid = 7,
                pickupDropFacility = state.answers[6] ?: "",
                pickupDropFacilityRemark = state.remarks[6],

                grievanceRegisterQid = 8,
                grievanceRegister = state.answers[7] ?: "",
                grievanceRegisterRemark = state.remarks[7],

                individualBedQid = 9,
                individualBed = state.answers[8] ?: "",
                individualBedRemark = state.remarks[8],

                kitchenDiningHygienicQid = 10,
                kitchenDiningHygienic = state.answers[9] ?: "",
                kitchenDiningHygienicRemark = state.remarks[9],

                diningRecreationSpaceQid = 11,
                diningRecreationSpace = state.answers[10] ?: "",
                diningRecreationSpaceRemark = state.remarks[10],

                numberOfWashbasinsQid = 12,
                numberOfWashbasins = state.washbasins,
                numberOfWashbasinsRemark = state.washbasinsRemark,

                toiletSignageQid = 13,
                toiletSignage = state.answers[11] ?: "",
                toiletSignageRemark = state.remarks[11],

                foodQualityHygieneQid = 14,
                foodQualityHygiene = state.answers[12] ?: "",
                foodQualityHygieneRemark = state.remarks[12],

                foodCommitteeFormedQid = 15,
                foodCommitteeFormed = state.answers[13] ?: "",
                foodCommitteeFormedRemark = state.remarks[13],

                foodAsPerMenuQid = 16,
                foodAsPerMenu = state.answers[14] ?: "",
                foodAsPerMenuRemark = state.remarks[14],

                drinkingWaterAvailableQid = 17,
                drinkingWaterAvailable = state.answers[15] ?: "",
                drinkingWaterAvailableRemark = state.remarks[15],

                toiletHygieneMaintainedQid = 18,
                toiletHygieneMaintained = state.answers[16] ?: "",
                toiletHygieneMaintainedRemark = state.remarks[16],

                overheadTankCleanedQid = 19,
                overheadTankCleaned = state.answers[17] ?: "",
                overheadTankCleanedRemark = state.remarks[17],

                quarterlyHealthCheckupQid = 20,
                quarterlyHealthCheckup = state.answers[18] ?: "",
                quarterlyHealthCheckupRemark = state.remarks[18],

                firstAidKitQid = 21,
                firstAidKit = state.answers[19] ?: "",
                firstAidKitRemark = state.remarks[19],

                doctorOnCallQid = 22,
                doctorOnCall = state.answers[20] ?: "",
                doctorOnCallRemark = state.remarks[20],

                securityWardenPresentQid = 23,
                securityWardenPresent = state.answers[21] ?: "",
                securityWardenPresentRemark = state.remarks[21],

                gensetPowerCutQid = 24,
                gensetPowerCut = state.answers[22] ?: "",
                gensetPowerCutRemark = state.remarks[22],

                tvCableAvailableQid = 25,
                tvCableAvailable = state.answers[23] ?: "",
                tvCableAvailableRemark = state.remarks[23],

                indoorGamesEquipmentQid = 26,
                indoorGamesEquipment = state.answers[24] ?: "",
                indoorGamesEquipmentRemark = state.remarks[24],

                wardenPoliceVerificationQid = 27,
                wardenPoliceVerification = state.answers[25] ?: "",
                wardenPoliceVerificationRemark = state.remarks[25]
            )

            val result = repository.saveResidentialFacilityVerification(request)

            result.onSuccess {

                _residentialState.update {
                    it.copy(
                        isLoading = false,
                        saveSuccess = true
                    )
                }
            }

            result.onFailure {

                _residentialState.update {
                    it.copy(
                        isLoading = false,
                        error = it.error
                    )
                }
            }
        }
    }
}



