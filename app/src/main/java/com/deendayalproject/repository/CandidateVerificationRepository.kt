package com.deendayalproject.repository

/**
 * Created by Rishi Porwal
 */

import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.base.BaseResponse
import com.deendayalproject.model.request.SaveBatchVerificationRequest
import com.deendayalproject.model.response.CandidateInspectionDetails
import com.deendayalproject.model.response.CandidateInspectionDetailsResponse
import com.deendayalproject.model.uistate.CandidateInspectionDto
import com.deendayalproject.model.uistate.GetCandidateInspectionRequest
import com.deendayalproject.network.ApiService

class CandidateerificationRepository(
    context : Context
) : BaseRepository<ApiService>(context){

    suspend fun getCandidateDetails(
        request: GetCandidateInspectionRequest
    ): Result<List<CandidateInspectionDetails>> =
        safeApiCallN {
            apiService.getCandidateInspectionDetails(request)
        }

    suspend fun saveVerification(
        request: SaveBatchVerificationRequest
    ): Result<List<Nothing>> =
        safeApiCallN {
            apiService.saveBatchDataVerification(request)
        }
}