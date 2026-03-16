package com.deendayalproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.deendayalproject.BuildConfig
import com.deendayalproject.model.request.SaveInspectionStandardFormRequest
import com.deendayalproject.model.request.assesmentInspection.GetInspectionStandardFormRequest
import com.deendayalproject.model.request.assesmentInspection.InspectionStandardQuestionRequest
import com.deendayalproject.model.response.CandidateAssessmentResponse.InspectionStandardFormDto
import com.deendayalproject.model.uistate.InspectionStandardFormUiState
import com.deendayalproject.repository.CandidateAssessmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DocumentMaintainViewModel (application: Application) : AndroidViewModel(application) {

    private val repository = CandidateAssessmentRepository(application)


    private val _inspectionStandardFormState =
        MutableStateFlow(InspectionStandardFormUiState())

    val inspectionStandardFormState: StateFlow<InspectionStandardFormUiState> =
        _inspectionStandardFormState.asStateFlow()

    fun loadInspectionStandardForm(
        inspectionId: Int
    ) {

        viewModelScope.launch {

            _inspectionStandardFormState.value =
                _inspectionStandardFormState.value.copy(
                    isLoading = true
                )

            val result =
                repository.getInspectionStandardForm(
                    GetInspectionStandardFormRequest(
                        BuildConfig.VERSION_NAME,
                        inspectionId
                    )
                )

            result.onSuccess { list ->

                if (list.isEmpty()) {

                    _inspectionStandardFormState.value =
                        _inspectionStandardFormState.value.copy(
                            isLoading = false,
                            error = "No data available"
                        )

                } else {

                    _inspectionStandardFormState.value =
                        mapStandardFormDto(list)
                }
            }

            result.onFailure {
                _inspectionStandardFormState.value =
                    _inspectionStandardFormState.value.copy(
                        isLoading = false,
                        error = it.message
                    )
            }
        }
    }

    private fun mapStandardFormDto(

        list: List<InspectionStandardFormDto>

    ): InspectionStandardFormUiState {

        val answers = MutableList<String?>(45) { null }

        val remarks = MutableList(45) { "" }

        list.forEach {

            val index = it.questionId - 1

            if (index in answers.indices) {

                answers[index] = it.answer

                remarks[index] = it.remark ?: ""
            }
        }

        return InspectionStandardFormUiState(

            inspectionId = list.firstOrNull()?.inspectionId,

            answers = answers,

            remarks = remarks,

            isLoading = false
        )
    }

    fun updateStandardAnswer(

        index: Int,

        answer: String

    ) {

        val state = _inspectionStandardFormState.value

        val updated = state.answers.toMutableList()

        updated[index] = answer

        _inspectionStandardFormState.value =
            state.copy(answers = updated)
    }

    fun updateStandardRemark(

        index: Int,

        remark: String

    ) {

        val state = _inspectionStandardFormState.value

        val updated = state.remarks.toMutableList()

        updated[index] = remark

        _inspectionStandardFormState.value =
            state.copy(remarks = updated)
    }

    fun clearInspectionStandardFormError() {
        _inspectionStandardFormState.value =
            _inspectionStandardFormState.value.copy(
                error = null
            )
    }


    /*Save Inspection Standard Form */

    fun saveInspectionStandardForm(

        inspectionId: Int

    ) {

        viewModelScope.launch {

            val state = _inspectionStandardFormState.value

            _inspectionStandardFormState.value =
                state.copy(isLoading = true)

            val request = mapInspectionStandardFormDto(
                state,
                inspectionId
            )

            val result =
                repository.saveInspectionStandardForm(request)

            result.onSuccess {

                if (it.responseCode == 200) {

                    _inspectionStandardFormState.value =
                        state.copy(
                            isLoading = false,
                            saveSuccess = true
                        )

                } else {

                    _inspectionStandardFormState.value =
                        state.copy(
                            isLoading = false,
                            error = it.responseDesc
                        )
                }
            }

            result.onFailure {

                _inspectionStandardFormState.value =
                    state.copy(
                        isLoading = false,
                        error = it.message
                    )
            }
        }
    }



    private fun mapInspectionStandardFormDto(
        state: InspectionStandardFormUiState,
        inspectionId: Int

    ): SaveInspectionStandardFormRequest {

        val questions = mutableListOf<InspectionStandardQuestionRequest>()

        state.answers.forEachIndexed { index, answer ->

            questions.add(

                InspectionStandardQuestionRequest(

                    questionId = (index + 1).toString(),

                    answer = answer ?: "",

                    remark = state.remarks[index]
                )
            )
        }

        return SaveInspectionStandardFormRequest(

            appVersion = BuildConfig.VERSION_NAME,

            inspectionId = inspectionId,

            questionsDetails = questions
        )
    }

    fun clearInspectionStandardSaveSuccess() {

        _inspectionStandardFormState.value =
            _inspectionStandardFormState.value.copy(
                saveSuccess = false
            )
    }

    fun clearInspectionStandardError() {

        _inspectionStandardFormState.value =
            _inspectionStandardFormState.value.copy(
                error = null
            )
    }




}