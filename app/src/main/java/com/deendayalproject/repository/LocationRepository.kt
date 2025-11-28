package com.deendayalproject.repository

import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.model.request.BlockRequest
import com.deendayalproject.model.request.DistrictRequest
import com.deendayalproject.model.request.GpRequest
import com.deendayalproject.model.request.StateRequest
import com.deendayalproject.model.request.VillageReq
import com.deendayalproject.model.response.BlockResponse
import com.deendayalproject.model.response.DistrictResponse
import com.deendayalproject.model.response.GpResponse
import com.deendayalproject.model.response.StateResponse
import com.deendayalproject.model.response.VillageRes
import com.deendayalproject.network.ApiService

class LocationRepository(context: Context) : BaseRepository<ApiService>(context) {

    suspend fun getStateList(request: StateRequest, token: String): Result<StateResponse> =
        safeApiCallWithToken(token) {
            apiService.getStateList(request)
        }

    suspend fun getDistrictList(request: DistrictRequest, token: String): Result<DistrictResponse> =
        safeApiCallWithToken(token) {
            apiService.getDistrictList(request)
        }

    suspend fun getBlockList(request: BlockRequest, token: String): Result<BlockResponse> =
        safeApiCallWithToken(token) {
            apiService.getBlockList(request)
        }

    suspend fun getGpList(request: GpRequest, token: String): Result<GpResponse> =
        safeApiCallWithToken(token) {
            apiService.getGPList(request)
        }

    suspend fun getVillageList(request: VillageReq, token: String): Result<VillageRes> =
        safeApiCallWithToken(token) {
            apiService.getVillageList(request)
        }
}