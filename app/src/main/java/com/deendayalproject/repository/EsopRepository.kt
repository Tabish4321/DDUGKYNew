package com.deendayalproject.repository

import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.esop.certificate.CertificateRequest
import com.deendayalproject.esop.exam.FinalSubmitResponse
import com.deendayalproject.esop.exam.InsertRequest
import com.deendayalproject.esop.result.GetResultItem
import com.deendayalproject.esop.result.GetResultViewRequest
import com.deendayalproject.model.request.CaptivePiaOfficerSelfieRequest
import com.deendayalproject.model.request.EsopCandidateRequest
import com.deendayalproject.model.request.EsopResultRequest
import com.deendayalproject.model.request.ModulesCandidateByOjtRequest
import com.deendayalproject.model.request.ModulesCandidateByOjtRequest2
import com.deendayalproject.model.request.ModulesOJTBatchRequest
import com.deendayalproject.model.request.ModulesOJTCompleteOjtRequest
import com.deendayalproject.model.request.ModulesOJTSanctionOrderRequest
import com.deendayalproject.model.request.ModulesOJTTrainingCenterRequest
import com.deendayalproject.model.request.TrainingCenterOpenStatusReq
import com.deendayalproject.model.response.CandidateOjtVerificationDetails
import com.deendayalproject.model.response.CandidateOjtVerificationRequest
import com.deendayalproject.model.response.CaptivePiaOfficerSelfieResponse
import com.deendayalproject.model.response.CertificateRes
import com.deendayalproject.model.response.EsopCandidateRes
import com.deendayalproject.model.response.LoginResponse
import com.deendayalproject.model.response.OJTList_Res
import com.deendayalproject.model.response.OJT_BatchList_Res
import com.deendayalproject.model.response.OJT_OjtVerifiedTrainingCenter_Res
import com.deendayalproject.model.response.OJT_Sanction_Res
import com.deendayalproject.model.response.OJT_TrainingCenter_Res
import com.deendayalproject.model.response.OJT_VerifiedBatchListSRLM_Res
import com.deendayalproject.model.response.OjtListByBatchSRLMRes
import com.deendayalproject.model.response.OjtListByBatch_Res
import com.deendayalproject.model.response.OjtListChildSRLMRes
import com.deendayalproject.model.response.OjtRes
import com.deendayalproject.model.response.OjtSRLMRes
import com.deendayalproject.model.response.SaveCandidateOjtVerificationResponse
import com.deendayalproject.model.response.TrainingCenterOpenStatusRes
import com.deendayalproject.network.ApiService
import com.example.esop.AswersOptionSubmit.SubmitExamRequest
import com.example.esop.AswersOptionSubmit.SubmitResponse
import com.example.esop.fialAnsweredSubmitApi.ResultInsertReq
import com.example.esop.mytest.MyTestResponse
import com.example.esop.quetions_esop.QuestionResponse
import com.example.esop.quetions_esop.QuestiontReq
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


// Ajit Ranjan create 11/June/2026 ESOP Implimentation
class EsopRepository(context: Context) : BaseRepository<ApiService>(context) {

    suspend fun esoprolecategory(request: EsopCandidateRequest, token: String): Result<EsopCandidateRes> =
        safeApiCallWithToken(token) {
            apiService.esoprolecategory(request)
        }



    suspend fun getresultAll(request: EsopResultRequest, token: String): Result<MyTestResponse> =
        safeApiCallWithToken(token) {
            apiService.getresultAll(request)
        }

    suspend fun getResultView(request: GetResultViewRequest, token: String): Result<GetResultItem> =
        safeApiCallWithToken(token) {
            apiService.getResultView(request)
        }


    suspend fun getQuestionsView(request: QuestiontReq, token: String): Result<QuestionResponse> =
        safeApiCallWithToken(token) {
            apiService.getQuestionsView(request)
        }



    suspend fun insertresultsubmit(request: SubmitExamRequest, token: String): Result<SubmitResponse> =
        safeApiCallWithToken(token) {
            apiService.insertresultsubmit(request)
        }


    suspend fun insertfinalsubmit(request: InsertRequest, token: String): Result<FinalSubmitResponse> =
        safeApiCallWithToken(token) {
            apiService.insertfinalsubmit(request)
        }

    suspend fun getcertificate(request: CertificateRequest, token: String): Result<CertificateRes> =
        safeApiCallWithToken(token) {
            apiService.getcertificate(request)
        }



}