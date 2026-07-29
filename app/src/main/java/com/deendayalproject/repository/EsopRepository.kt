package com.deendayalproject.repository

import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.model.request.CertificateRequest
import com.deendayalproject.model.response.FinalSubmitResponse
import com.deendayalproject.model.request.InsertRequest
import com.deendayalproject.model.response.GetResultItem
import com.deendayalproject.model.request.GetResultViewRequest
import com.deendayalproject.model.request.EsopCandidateRequest
import com.deendayalproject.model.request.EsopResultRequest
import com.deendayalproject.model.response.CertificateRes
import com.deendayalproject.model.response.EsopCandidateRes
import com.deendayalproject.network.ApiService
import com.example.esop.AswersOptionSubmit.SubmitExamRequest
import com.example.esop.AswersOptionSubmit.SubmitResponse
import com.example.esop.mytest.MyTestResponse
import com.example.esop.quetions_esop.QuestionResponse
import com.deendayalproject.model.request.QuestiontReq


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