package com.deendayalproject.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deendayalproject.model.request.InspectionRequestBody
import com.deendayalproject.model.uistate.InspectionTab
import com.deendayalproject.model.uistate.InspectionUiState
import com.deendayalproject.repository.repomanager.RepositoryManager
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreviousAndDueViewModel(
    application: Application
) : AndroidViewModel(application) {



        private val repositoryManager = RepositoryManager.getInstance(application.applicationContext)

        private val _uiState = MutableStateFlow(InspectionUiState())
          val uiState: StateFlow<InspectionUiState> = _uiState

    fun loadData(request: InspectionRequestBody) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val previousDeferred = async { repositoryManager.inspectionRepo.getPreviousInspection(request) }
            val dueDeferred = async { repositoryManager.inspectionRepo.getDueDiligence(request) }

            val previous = previousDeferred.await()
            val due = dueDeferred.await()
            Log.d("API_DEBUG", "Previous size = ${previous.getOrNull()?.size}")
            Log.d("API_DEBUG", "Previous size = ${previous.getOrNull()?.size}")

            _uiState.update {
                it.copy(
                    isLoading = false,
                    previousList = previous.getOrNull() ?: emptyList(),
                    dueDiligenceList = due.getOrNull() ?: emptyList(),
                    error = previous.exceptionOrNull()?.message
                        ?: due.exceptionOrNull()?.message
                )
            }
        }
    }

    fun selectTab(tab: InspectionTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}