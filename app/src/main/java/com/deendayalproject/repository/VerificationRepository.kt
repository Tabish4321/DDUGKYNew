package com.deendayalproject.repository

import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.model.request.RFQteamVerificationRequest
import com.deendayalproject.model.request.TcQTeamInsertReq
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.response.FinalSubmitRes
import com.deendayalproject.model.response.InsertTcGeneralDetailsResponse
import com.deendayalproject.model.response.RfQTeamListRes
import com.deendayalproject.network.ApiService

class VerificationRepository(context: Context) : BaseRepository<ApiService>(context) {

    suspend fun insertQTeamVerification(request: TcQTeamInsertReq): Result<InsertTcGeneralDetailsResponse> =
        safeApiCall {
            apiService.insertQTeamVerification(request)
        }

    suspend fun insertSrlmVerification(request: TcQTeamInsertReq): Result<InsertTcGeneralDetailsResponse> =
        safeApiCall {
            apiService.insertSrlmVerification(request)
        }

    suspend fun getFinalSubmitinsertRFQteamVerificationRequestData(request: RFQteamVerificationRequest): Result<FinalSubmitRes> =
        safeApiCall {
            apiService.getFinalSubmitInsertRFQteamVerificationData(request)
        }

    suspend fun getFinalSubmitinsertRFinsertRFSrlmVerificationRequestData(request: RFQteamVerificationRequest): Result<FinalSubmitRes> =
        safeApiCall {
            apiService.getFinalSubmitInsertRFinsertRFSrlmVerificationData(request)
        }

    suspend fun fetchRFSRLMVerificationList(request: TrainingCenterRequest, token: String): Result<RfQTeamListRes> =
        safeApiCallWithToken(token) {
            apiService.getRFSRLMVerification(request)
        }






}