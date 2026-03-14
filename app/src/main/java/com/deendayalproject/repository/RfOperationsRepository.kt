package com.deendayalproject.repository

import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.model.request.AddNewRFReq
import com.deendayalproject.model.request.ModifyRfList
import com.deendayalproject.model.request.RfFinalSubmitReq
import com.deendayalproject.model.request.UrinalWashbasinReq
import com.deendayalproject.model.response.AddNewRFRes
import com.deendayalproject.model.response.ITLAbDetailsErrorResponse
import com.deendayalproject.model.response.ModifyRFRes
import com.deendayalproject.model.response.RfFinalSubmitRes
import com.deendayalproject.network.ApiService

class RfOperationsRepository(context: Context) : BaseRepository<ApiService>(context) {

    suspend fun insertRFFinalSubmission(request: RfFinalSubmitReq): Result<RfFinalSubmitRes> =
        safeApiCall {
            apiService.insertRFFinalSubmission(request)
        }

    suspend fun saveInitialResidentialFacility(request: AddNewRFReq): Result<AddNewRFRes> =
        safeApiCall {
            apiService.saveInitialResidentialFacility(request)
        }

    suspend fun getResidentialList(request: ModifyRfList): Result<ModifyRFRes> =
        safeApiCall {
            apiService.getResidentialList(request)
        }

    suspend fun insertRfToiletWashRoomDetail(request: UrinalWashbasinReq): Result<ITLAbDetailsErrorResponse> =
        safeApiCall {
            apiService.insertRfToiletWashRoomDetail(request)
        }
}