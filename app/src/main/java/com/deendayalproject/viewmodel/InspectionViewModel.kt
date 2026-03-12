package com.deendayalproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.deendayalproject.BuildConfig
import com.deendayalproject.model.request.CandidatePreviousBatchReq
import com.deendayalproject.model.request.GetAttendanceDetailsReq
import com.deendayalproject.model.request.GetImageListReq
import com.deendayalproject.model.request.GetTcInspectionList
import com.deendayalproject.model.request.InspectionPreviousBatchList
import com.deendayalproject.model.request.InspectionTcDetailsReq
import com.deendayalproject.model.request.OngoingSubmitBasicRecordsReq
import com.deendayalproject.model.request.SubjectDeleteReq
import com.deendayalproject.model.request.SubjectReq
import com.deendayalproject.model.request.TrainerListReq
import com.deendayalproject.model.request.assesmentInspection.GetTrainerAttendanceInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.SaveTrainerAttendanceInspectionRequest
import com.deendayalproject.model.request.saveTrainerClassObservationInspectionReq
import com.deendayalproject.model.response.CandidateAssessmentResponse.TrainerAttendanceInspectionResponse
import com.deendayalproject.model.response.CandidatePreviousBatchRes
import com.deendayalproject.model.response.GetAttendanceDetailsRes
import com.deendayalproject.model.response.GetImageListRes
import com.deendayalproject.model.response.GetTcInspectionRes
import com.deendayalproject.model.response.InsertRes
import com.deendayalproject.model.response.InspectionPreviousBatchRes
import com.deendayalproject.model.response.InspectionTcDetailsRes
import com.deendayalproject.model.response.SubjectDeleteRes
import com.deendayalproject.model.response.SubjectListRes
import com.deendayalproject.model.response.TrainerListRes
import com.deendayalproject.model.uistate.TrainerAttendanceUiState
import com.deendayalproject.model.uistate.TrainerClassObservationUiState
import com.deendayalproject.repository.repomanager.RepositoryManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException

class InspectionViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repositoryManager =
        RepositoryManager.getInstance(application.applicationContext)



    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _sessionExpired = MutableSharedFlow<Unit>()
    val sessionExpired = _sessionExpired.asSharedFlow()



    private fun <T> executeApiCall(
        apiCall: suspend () -> Result<T>,
        onSuccess: suspend (T) -> Unit
    ) {

        viewModelScope.launch {

            _loading.emit(true)

            try {

                val result = apiCall()

                result
                    .onSuccess { data ->
                        onSuccess(data)
                    }
                    .onFailure { throwable ->

                        if (throwable is HttpException &&
                            throwable.code() == 401
                        ) {
                            _sessionExpired.emit(Unit)
                        } else {
                            _errorMessage.emit(
                                throwable.localizedMessage
                                    ?: "Something went wrong"
                            )
                        }
                    }

            } catch (e: Exception) {
                _errorMessage.emit(
                    e.localizedMessage ?: "Unknown error"
                )
            } finally {
                _loading.emit(false)
            }
        }
    }



    private val _dueDiligenceList =
        MutableStateFlow<GetTcInspectionRes?>(null)

    val dueDiligenceList:
            StateFlow<GetTcInspectionRes?> =
        _dueDiligenceList.asStateFlow()

    fun getDueDiligenceDetails(
        request: GetTcInspectionList,
        header: String
    ) {

        executeApiCall(
            apiCall = {
                repositoryManager
                    .inspectionRepo
                    .getDueDiligenceDetails(request, header)
            },
            onSuccess = { response ->

                when (response.responseCode) {
                    200 -> _dueDiligenceList.emit(response)
                    202 -> _errorMessage.emit("No data available.")
                    301 -> _errorMessage.emit("Please upgrade your app.")
                    else -> _errorMessage.emit(
                        response.responseDesc.ifEmpty {
                            "Unknown server error"
                        }
                    )
                }
            }
        )
    }



    private val _tcDetails =
        MutableStateFlow<InspectionTcDetailsRes?>(null)

    val tcDetails:
            StateFlow<InspectionTcDetailsRes?> =
        _tcDetails.asStateFlow()

    fun getDueDiligenceTcDetails(
        request: InspectionTcDetailsReq,
        header: String
    ) {

        executeApiCall(
            apiCall = {
                repositoryManager
                    .inspectionRepo
                    .getDueDiligenceTcDetails(request, header)
            },
            onSuccess = { response ->

                when (response.responseCode) {
                    200 -> _tcDetails.emit(response)
                    202 -> _errorMessage.emit("No data available.")
                    301 -> _errorMessage.emit("Please upgrade your app.")
                    else -> _errorMessage.emit(
                        response.responseDesc.ifEmpty {
                            "Unknown server error"
                        }
                    )
                }
            }
        )
    }



    private val _previousBatchList =
        MutableStateFlow<InspectionPreviousBatchRes?>(null)

    val previousBatchList:
            StateFlow<InspectionPreviousBatchRes?> =
        _previousBatchList.asStateFlow()

    fun getInspectionPreviousBatchList(
        request: InspectionPreviousBatchList,
        header: String
    ) {

        executeApiCall(
            apiCall = {
                repositoryManager
                    .inspectionRepo
                    .getInspectionPreviousBatchList(request, header)
            },
            onSuccess = { response ->

                when (response.responseCode) {
                    200 -> _previousBatchList.emit(response)
                    202 -> {
                        _previousBatchList.emit(null)
                        _errorMessage.emit("No batch data available.")
                    }

                    301 -> _errorMessage.emit("Please upgrade your app.")
                    else -> _errorMessage.emit(
                        response.responseDesc?.ifEmpty {
                            "Unknown server error"
                        } ?: ""
                    )
                }
            }
        )
    }



    private val _candidatePrevBatchList =
        MutableStateFlow<CandidatePreviousBatchRes?>(null)

    val candidatePrevBatchList:
            StateFlow<CandidatePreviousBatchRes?> =
        _candidatePrevBatchList.asStateFlow()


    fun getCandidateForPreviousBatch(
        request: CandidatePreviousBatchReq,
        header: String
    ) {

        executeApiCall(
            apiCall = {
                repositoryManager
                    .inspectionRepo
                    .getCandidateForPreviousBatch(request, header)
            },
            onSuccess = { response ->

                when (response.responseCode) {

                    200 -> _candidatePrevBatchList.emit(response)


                    202 -> {
                        _candidatePrevBatchList.emit(null)
                        _errorMessage.emit("No candidate data available.")
                    }

                    301 -> _errorMessage.emit("Please upgrade your app.")

                    else -> _errorMessage.emit(
                        response.responseDesc?.ifEmpty {
                            "Unknown server error"
                        } ?: "Unknown error"
                    )
                }
            }
        )
    }








    private val _onGoingBatchList =
        MutableStateFlow<InspectionPreviousBatchRes?>(null)

    val onGoingBatchList:
            StateFlow<InspectionPreviousBatchRes?> =
        _onGoingBatchList.asStateFlow()

    fun getInspectionOngoingBatchList(
        request: InspectionPreviousBatchList,
        header: String
    ) {

        executeApiCall(
            apiCall = {
                repositoryManager
                    .inspectionRepo
                    .getInspectionOngoingBatchList(request, header)
            },
            onSuccess = { response ->

                when (response.responseCode) {
                    200 -> _onGoingBatchList.emit(response)

                    202 -> {
                        _onGoingBatchList.emit(null)
                        _errorMessage.emit("No batch data available.")
                    }

                    301 -> _errorMessage.emit("Please upgrade your app.")
                    else -> _errorMessage.emit(
                        response.responseDesc?.ifEmpty {
                            "Unknown server error"
                        } ?: ""
                    )
                }
            }
        )
    }





    private val _getSubjectList =
        MutableStateFlow<SubjectListRes?>(null)

    val getSubjectList:
            StateFlow<SubjectListRes?> =
        _getSubjectList.asStateFlow()

    fun getSubjectList(
        request: SubjectReq,
        header: String
    ) {

        executeApiCall(
            apiCall = {
                repositoryManager
                    .inspectionRepo
                    .getSubjectList(request, header)
            },
            onSuccess = { response ->

                when (response.responseCode) {
                    200 -> _getSubjectList.emit(response)

                    202 -> {

                        _getSubjectList.emit(
                            SubjectListRes(
                                wrappedList = emptyList(),
                                responseCode = 202,
                                responseDesc = "No Data",
                                responseMsg = null,
                                appCode = null
                            )
                        )

                    }

                    301 -> _errorMessage.emit("Please upgrade your app.")
                    else -> _errorMessage.emit(
                        response.responseDesc?.ifEmpty {
                            "Unknown server error"
                        } ?: ""
                    )
                }
            }
        )
    }



    fun clearDeleteSubjectResponse() {
        _deleteSubjectItem.value = null
    }


    private val _deleteSubjectItem =
        MutableStateFlow<SubjectDeleteRes?>(null)

    val deleteSubjectItem:
            StateFlow<SubjectDeleteRes?> =
        _deleteSubjectItem.asStateFlow()

    fun deleteSubjectItem(
        request: SubjectDeleteReq,
        header: String
    ) {

        executeApiCall(
            apiCall = {
                repositoryManager
                    .inspectionRepo
                    .deleteSubjectItem(request, header)
            },
            onSuccess = { response ->

                when (response.responseCode) {
                    200 -> _deleteSubjectItem.emit(response)

                    202 -> {
                        _deleteSubjectItem.emit(null)
                        _errorMessage.emit("No batch data available.")
                    }

                    301 -> _errorMessage.emit("Please upgrade your app.")
                    else -> _errorMessage.emit(
                        response.responseDesc?.ifEmpty {
                            "Unknown server error"
                        } ?: ""
                    )
                }
            }
        )
    }




    private val _candidateOngoingBatchList =
        MutableStateFlow<CandidatePreviousBatchRes?>(null)

    val candidateOngoingBatchList:
            StateFlow<CandidatePreviousBatchRes?> =
        _candidateOngoingBatchList.asStateFlow()


    fun getOngoingBatchCandiate(
        request: CandidatePreviousBatchReq,
        header: String
    ) {

        executeApiCall(
            apiCall = {
                repositoryManager
                    .inspectionRepo
                    .getOngoingBatchCandiate(request, header)
            },
            onSuccess = { response ->

                when (response.responseCode) {

                    200 -> _candidateOngoingBatchList.emit(response)



                    202 -> {
                        _candidateOngoingBatchList.emit(null)
                        _errorMessage.emit("No candidate data available.")
                    }

                    301 -> _errorMessage.emit("Please upgrade your app.")

                    else -> _errorMessage.emit(
                        response.responseDesc?.ifEmpty {
                            "Unknown server error"
                        } ?: "Unknown error"
                    )
                }
            }
        )
    }



    private val _getCandidateImageRecords =
        MutableStateFlow<GetImageListRes?>(null)

    val getCandidateImageRecords:
            StateFlow<GetImageListRes?> =
        _getCandidateImageRecords.asStateFlow()


    fun getCandidateImageRecords(
        request: GetImageListReq,
        header: String
    ) {

        executeApiCall(
            apiCall = {
                repositoryManager
                    .inspectionRepo
                    .getCandidateImageRecords(request, header)
            },
            onSuccess = { response ->

                when (response.responseCode) {

                    200 -> _getCandidateImageRecords.emit(response)

                    202 -> _errorMessage.emit("No candidate data available.")


                    301 -> _errorMessage.emit("Please upgrade your app.")

                    else -> _errorMessage.emit(
                        response.responseDesc?.ifEmpty {
                            "Unknown server error"
                        } ?: "Unknown error"
                    )
                }
            }
        )
    }




//    fun clearSubmitResponse() {
//
//        _submitBasicRecordResponse.value = null
//
//    }

//    private val _isSubmittingBasicRecord = MutableStateFlow(false)
//    val isSubmittingBasicRecord = _isSubmittingBasicRecord.asStateFlow()
//
//
//    private val _submitBasicRecordResponse =
//        MutableStateFlow<InsertRes?>(null)
//
//    val submitBasicRecordResponse =
//        _submitBasicRecordResponse.asStateFlow()
//
//
//
//    fun submitBasicRecords(req: OngoingSubmitBasicRecordsReq, header: String) {
//
//        viewModelScope.launch {
//
//            _isSubmittingBasicRecord.emit(true)
//
//            executeApiCall(
//                apiCall = {
//                    repositoryManager
//                        .inspectionRepo
//                        .saveCandidateBasicRecords(req, header)
//                },
//                onSuccess = { response ->
//
//                    _submitBasicRecordResponse.value = response
//
//                    _isSubmittingBasicRecord.value = false
//                }
//
//            )
//        }
//    }






    private val _getCandidateTodayAttendanceStatus =
        MutableStateFlow<GetAttendanceDetailsRes?>(null)

    val getCandidateTodayAttendanceStatus:
            StateFlow<GetAttendanceDetailsRes?> =
        _getCandidateTodayAttendanceStatus.asStateFlow()


    fun getCandidateTodayAttendanceStatus(
        request: GetAttendanceDetailsReq
    ) {

        executeApiCall(
            apiCall = {
                repositoryManager
                    .inspectionRepo
                    .getCandidateTodayAttendanceStatus(request)
            },
            onSuccess = { response ->

                when (response.responseCode) {

                    200 -> _getCandidateTodayAttendanceStatus.emit(response)



                    202 -> {
                        _getCandidateTodayAttendanceStatus.emit(null)
                        _errorMessage.emit("No candidate data available.")
                    }

                    301 -> _errorMessage.emit("Please upgrade your app.")

                    else -> _errorMessage.emit(
                        response.responseDesc?.ifEmpty {
                            "Unknown server error"
                        } ?: "Unknown error"
                    )
                }
            }
        )
    }






    private val _getTrainersListInspection =
        MutableStateFlow<TrainerListRes?>(null)

    val getTrainersListInspection:
            StateFlow<TrainerListRes?> =
        _getTrainersListInspection.asStateFlow()


    fun getTrainersListInspection(
        request: TrainerListReq
    ) {

        executeApiCall(
            apiCall = {
                repositoryManager
                    .inspectionRepo
                    .getTrainersListInspection(request)
            },
            onSuccess = { response ->

                when (response.responseCode) {

                    200 -> _getTrainersListInspection.emit(response)



                    202 -> {
                        _getTrainersListInspection.emit(null)
                        _errorMessage.emit("No candidate data available.")
                    }

                    301 -> _errorMessage.emit("Please upgrade your app.")

                    else -> _errorMessage.emit(
                        response.responseDesc?.ifEmpty {
                            "Unknown server error"
                        } ?: "Unknown error"
                    )
                }
            }
        )
    }


    /*Training Attendence Inspection*/

    private val _trainerState =
        MutableStateFlow(TrainerAttendanceUiState())

    val trainerState: StateFlow<TrainerAttendanceUiState> =
        _trainerState.asStateFlow()
    //repositoryManager.inspectionRepo


    fun loadTrainerAttendance(
        trainerCode:Int,
        inspectionId: Int,
    ) {

        viewModelScope.launch {

            _trainerState.update {
                it.copy(isLoading = true)
            }

            val result =
                repositoryManager.inspectionRepo.getTrainerAttendanceInspection(

                    GetTrainerAttendanceInspectionRequest(
                        BuildConfig.VERSION_NAME,
                        trainerCode,
                        inspectionId,
                    )
                )

            result.onSuccess { list ->

                val dto = list.firstOrNull()

                if (dto != null) {

                    _trainerState.value =
                        mapTrainerDtoToUiState(dto)

                } else {

                    _trainerState.update {

                        it.copy(
                            isLoading = false,
                            error = "No data available."
                        )
                    }
                }
            }

            result.onFailure {

                _trainerState.update {

                    it.copy(
                        isLoading = false,
                        error = it.error
                    )
                }
            }
        }
    }


    fun saveTrainerAttendance(
        trainerCode:Int,
        inspectionId: Int,

    ) {

        viewModelScope.launch {
            _trainerState.update {
                it.copy(isLoading = true)
            }

            val request =
                mapTrainerUiStateToRequest(
                    trainerCode,
                    inspectionId
                )

            val result =
                repositoryManager.inspectionRepo.saveTrainerAttendanceInspection(request)

            result.onSuccess {
                _trainerState.update {

                    it.copy(
                        isLoading = false,
                        saveSuccess = true
                    )
                }
            }

            result.onFailure {

                _trainerState.update {

                    it.copy(
                        isLoading = false,
                        error = it.error
                    )
                }
            }
        }
    }


    private fun mapTrainerDtoToUiState(
        dto: TrainerAttendanceInspectionResponse
    ): TrainerAttendanceUiState {

        return TrainerAttendanceUiState(

            inspectionId = dto.inspectionId,
            trainerAttendanceMatch = dto.trainerAttendanceMatch,
            trainerCode = dto.trainerCode,
            trainerAttendanceMatchRemark =
                dto.trainerAttendanceMatchRemark ?: "",

            trainerCounsellingArranged =
                dto.trainerCounsellingArranged,

            trainerCounsellingArrangedRemark =
                dto.trainerCounsellingArrangedRemark ?: "",

            trainerEntryExitAclp =
                dto.trainerEntryExitAclp,

            trainerEntryExitAclpRemark =
                dto.trainerEntryExitAclpRemark ?: "",

            isLoading = false
        )
    }

    private fun mapTrainerUiStateToRequest(
        trainerCode: Int,
        inspectionId: Int,
    ): SaveTrainerAttendanceInspectionRequest {

        val state = _trainerState.value

        return SaveTrainerAttendanceInspectionRequest(

            appVersion = BuildConfig.VERSION_NAME,
            inspectionId = inspectionId,
            trainerCode =trainerCode ,
            trainerAttendanceMatchQid = 1,
            trainerAttendanceMatch = state.trainerAttendanceMatch ?: "",
            trainerAttendanceMatchRemark =
                state.trainerAttendanceMatchRemark,

            trainerCounsellingArrangedQid = 2,
            trainerCounsellingArranged =
                state.trainerCounsellingArranged ?: "",
            trainerCounsellingArrangedRemark =
                state.trainerCounsellingArrangedRemark,

            trainerEntryExitAclpQid = 3,
            trainerEntryExitAclp =
                state.trainerEntryExitAclp ?: "",
            trainerEntryExitAclpRemark =
                state.trainerEntryExitAclpRemark
        )
    }

    fun clearTrainerError() {

        _trainerState.update {
            it.copy(error = null)
        }
    }

    fun clearTrainerSuccess() {

        _trainerState.update {
            it.copy(saveSuccess = false)
        }
    }

    fun updateTrainerState(

        trainerAttendanceMatch: String?,
        trainerCounsellingArranged: String?,
        trainerEntryExitAclp: String?,

        trainerAttendanceMatchRemark: String?,
        trainerCounsellingArrangedRemark: String?,
        trainerEntryExitAclpRemark: String?

    ) {

        _trainerState.update {

            it.copy(

                trainerAttendanceMatch = trainerAttendanceMatch,
                trainerAttendanceMatchRemark = trainerAttendanceMatchRemark ?: "",

                trainerCounsellingArranged = trainerCounsellingArranged,
                trainerCounsellingArrangedRemark = trainerCounsellingArrangedRemark ?: "",

                trainerEntryExitAclp = trainerEntryExitAclp,
                trainerEntryExitAclpRemark = trainerEntryExitAclpRemark ?: ""

            )
        }
    }



/* Save Trainer Class Observation */

    private val _trainerClassObservationState =
        MutableStateFlow(TrainerClassObservationUiState())

    val trainerClassObservationState =
        _trainerClassObservationState.asStateFlow()


    fun updateTrainerClassObservationState(

        answers: List<String?>,
        remarks: List<String>,
        subject: String

    ) {

        _trainerClassObservationState.value =
            _trainerClassObservationState.value.copy(

                answers = answers,
                remarks = remarks,
                subject = subject
            )
    }

    fun saveTrainerClassObservation(
        inspectionId: Int
    ) {

        viewModelScope.launch {

            val state = _trainerClassObservationState.value

            _trainerClassObservationState.value =
                state.copy(isLoading = true)

            val request = mapTrainerClassObservationDto(state, inspectionId)

            val result =  repositoryManager.inspectionRepo.saveTrainerClassObservationInspection(request)

            result.onSuccess {

                if (it.responseCode == 200) {
                    _trainerClassObservationState.value =
                        state.copy(
                            isLoading = false,
                            saveSuccess = true
                        )

                } else {
                    _trainerClassObservationState.value =
                        state.copy(
                            isLoading = false,
                            error = it.responseDesc
                        )
                }
            }
        }
    }

    private fun mapTrainerClassObservationDto(

        state: TrainerClassObservationUiState,
        inspectionId: Int

    ): saveTrainerClassObservationInspectionReq {

        return saveTrainerClassObservationInspectionReq(

            appVersion = BuildConfig.VERSION_NAME,

            inspectionId = inspectionId,

            subject = state.subject,

            trainerFacingClassQid = 1,
            trainerFacingClass = state.answers[0] ?: "",
            trainerFacingClassRemark = state.remarks[0],

            trainerAddressingCandidatesQid = 2,
            trainerAddressingCandidates = state.answers[1] ?: "",
            trainerAddressingCandidatesRemark = state.remarks[1],

            sessionAsPerLessonPlanQid = 3,
            sessionAsPerLessonPlan = state.answers[2] ?: "",
            sessionAsPerLessonPlanRemark = state.remarks[2],

            maintainsClassDisciplineQid = 4,
            maintainsClassDiscipline = state.answers[3] ?: "",
            maintainsClassDisciplineRemark = state.remarks[3],

            trainerConfidentCommunicationQid = 5,
            trainerConfidentCommunication = state.answers[4] ?: "",
            trainerConfidentCommunicationRemark = state.remarks[4],

            trainerWithoutMaterialRefQid = 6,
            trainerWithoutMaterialRef = state.answers[5] ?: "",
            trainerWithoutMaterialRefRemark = state.remarks[5],

            usesAudiovisualAidsQid = 7,
            usesAudiovisualAids = state.answers[6] ?: "",
            usesAudiovisualAidsRemark = state.remarks[6],

            sessionInteractiveQid = 8,
            sessionInteractive = state.answers[7] ?: "",
            sessionInteractiveRemark = state.remarks[7],

            encouragesCandidateQuestionsQid = 9,
            encouragesCandidateQuestions = state.answers[8] ?: "",
            encouragesCandidateQuestionsRemark = state.remarks[8],

            answersQueriesClearlyQid = 10,
            answersQueriesClearly = state.answers[9] ?: "",
            answersQueriesClearlyRemark = state.remarks[9],

            usesExamplesMethodsQid = 11,
            usesExamplesMethods = state.answers[10] ?: "",
            usesExamplesMethodsRemark = state.remarks[10],

            internalAssessmentOnScheduleQid = 12,
            internalAssessmentOnSchedule = state.answers[11] ?: "",
            internalAssessmentOnScheduleRemark = state.remarks[11],

            evaluatesPerformanceFeedbackQid = 13,
            evaluatesPerformanceFeedback = state.answers[12] ?: "",
            evaluatesPerformanceFeedbackRemark = state.remarks[12],

            guidesJobReadinessQid = 14,
            guidesJobReadiness = state.answers[13] ?: "",
            guidesJobReadinessRemark = state.remarks[13]
        )
    }

    fun clearTrainerClassObservationError() {

        _trainerClassObservationState.value =
            _trainerClassObservationState.value.copy(
                error = null
            )
    }

    fun clearTrainerClassObservationSuccess() {

        _trainerClassObservationState.value =
            _trainerClassObservationState.value.copy(
                saveSuccess = false
            )
    }



}