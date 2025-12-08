package com.deendayalproject.repository

import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.model.request.FieldVerificationDetailRequest
import com.deendayalproject.model.request.FieldVerificationFinalSubmit
import com.deendayalproject.model.request.FieldVerificationListRequest
import com.deendayalproject.model.response.FieldVerificationDetailResponse
import com.deendayalproject.model.response.FieldVerificationListResponse
import com.deendayalproject.network.ApiService

class FieldVerificationRepository(context: Context) : BaseRepository<ApiService>(context) {

    suspend fun fetchFieldVerificationList(
        request: FieldVerificationListRequest,
        token: String
    ): Result<FieldVerificationListResponse> =
        safeApiCallWithToken(token) {
            apiService.getFieldVerificationList(request)
        }

    suspend fun getFieldVerificationDetail(request: FieldVerificationDetailRequest): Result<FieldVerificationDetailResponse> =
        safeApiCall {
            apiService.getFieldVerificationDetail(request)
        }

    suspend fun getFieldVerificationFinDetail(request: FieldVerificationDetailRequest): Result<FieldVerificationDetailResponse> =
        safeApiCall {
            apiService.getFieldVerificationFinDetail(request)
        }

    suspend fun getFieldVerificationTrainingDetail(request: FieldVerificationDetailRequest): Result<FieldVerificationDetailResponse> =
        safeApiCall {
            apiService.getFieldVerificationTrainingDetail(request)
        }

    suspend fun getFieldVerificationTrainingInfraDetail(request: FieldVerificationDetailRequest): Result<FieldVerificationDetailResponse> =
        safeApiCall {
            apiService.getFieldVerificationTrainingInfraDetail(request)
        }

    suspend fun getFieldVerificationCertificationDetail(request: FieldVerificationDetailRequest): Result<FieldVerificationDetailResponse> =
        safeApiCall {
            apiService.getFieldVerificationCertificationDetail(request)
        }

    suspend fun getFieldVerificationPlacementDetail(request: FieldVerificationDetailRequest): Result<FieldVerificationDetailResponse> =
        safeApiCall {
            apiService.getFieldVerificationPlacementDetail(request)
        }

    suspend fun submitFieldVerification(request: FieldVerificationFinalSubmit): Result<FieldVerificationDetailResponse> =
        safeApiCall {
            apiService.submitFieldVerification(request)
        }

//    suspend fun submitFieldVerification(
//        request: FieldVerificationFinalSubmit
//    ): Result<FieldVerificationDetailResponse> {
//        safeApiCall {
//            apiService.submitFieldVerification(request)
//        }

}
