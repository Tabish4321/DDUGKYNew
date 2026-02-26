package com.deendayalproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.deendayalproject.model.request.CandidatePreviousBatchReq
import com.deendayalproject.model.request.GetTcInspectionList
import com.deendayalproject.model.request.InspectionPreviousBatchList
import com.deendayalproject.model.request.InspectionTcDetailsReq
import com.deendayalproject.model.response.CandidatePreviousBatchRes
import com.deendayalproject.model.response.GetTcInspectionRes
import com.deendayalproject.model.response.InspectionPreviousBatchRes
import com.deendayalproject.model.response.InspectionTcDetailsRes
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
                    202 -> _errorMessage.emit("No batch data available.")
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



    private val _candidateList =
        MutableStateFlow<CandidatePreviousBatchRes?>(null)

    val candidateList:
            StateFlow<CandidatePreviousBatchRes?> =
        _candidateList.asStateFlow()


    fun getCandidateForInspection(
        request: CandidatePreviousBatchReq,
        header: String
    ) {

        executeApiCall(
            apiCall = {
                repositoryManager
                    .inspectionRepo
                    .getCandidateForInspection(request, header)
            },
            onSuccess = { response ->

                when (response.responseCode) {

                    200 -> _candidateList.emit(response)

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

}