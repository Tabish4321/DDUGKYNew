package com.deendayalproject.repository

import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.model.request.ModulesCandidateByOjtRequest
import com.deendayalproject.model.request.ModulesCandidateByOjtRequest2
import com.deendayalproject.model.request.ModulesOJTBatchRequest
import com.deendayalproject.model.request.ModulesOJTCompleteOjtRequest
import com.deendayalproject.model.request.ModulesOJTSanctionOrderRequest
import com.deendayalproject.model.request.ModulesOJTTrainingCenterRequest
import com.deendayalproject.model.response.CandidateOjtVerificationRequest
import com.deendayalproject.model.response.LoginResponse
import com.deendayalproject.model.response.OJTList_Res
import com.deendayalproject.model.response.OJT_BatchList_Res
import com.deendayalproject.model.response.OJT_Sanction_Res
import com.deendayalproject.model.response.OJT_TrainingCenter_Res
import com.deendayalproject.model.response.OjtListByBatch_Res
import com.deendayalproject.model.response.OjtRes
import com.deendayalproject.model.response.SaveCandidateOjtVerificationResponse
import com.deendayalproject.network.ApiService


// Ajit Ranjan create 27/Jan/2026 OJT Implimentation
class OJTRepository(context: Context) : BaseRepository<ApiService>(context) {

    suspend fun fetchOJTSanctionOrderNumberList(request: ModulesOJTSanctionOrderRequest, token: String): Result<OJT_Sanction_Res> =
        safeApiCallWithToken(token) {
            apiService.getOJTSanctionOrderNumber(request)
//            apiService.getOJTSanctionOrderNumber(request)
        }

    suspend fun fetchOJTTrainingCenterList(request: ModulesOJTTrainingCenterRequest, token: String): Result<OJT_TrainingCenter_Res> =
        safeApiCallWithToken(token) {
            apiService.getOJTTrainingCenter(request)
        }

    suspend fun fetchOJTBatchList(request: ModulesOJTBatchRequest, token: String): Result<OJT_BatchList_Res> =
        safeApiCallWithToken(token) {
            apiService.getOJTBatch(request)
        }
    suspend fun fetchOJTCompleteOjtList(request: ModulesOJTCompleteOjtRequest, token: String): Result<OJTList_Res> =
        safeApiCallWithToken(token) {
            apiService.getCompleteOjtList(request)
        }

    suspend fun CandidateOjtVerification(request: CandidateOjtVerificationRequest, token: String): Result<SaveCandidateOjtVerificationResponse> =
        safeApiCallWithToken(token) {
            apiService.saveCandidateOjtVerification(request)
        }



    suspend fun fetchOJTgetOjtListBy(request: ModulesCandidateByOjtRequest2, token: String): Result<OjtListByBatch_Res> =
        safeApiCallWithToken(token) {

            apiService.getOjtListBy(request)
        }


    suspend fun fetchCandidateByOjtBy(request: ModulesCandidateByOjtRequest, token: String): Result<OjtRes> =
        safeApiCallWithToken(token) {

            apiService.getCandidateByOjt(request)
        }
    suspend fun logOutUser( token: String): Result<LoginResponse>{
        return safeApiCallWithToken(token) {
            apiService.logOutUser()
        }
    }





}