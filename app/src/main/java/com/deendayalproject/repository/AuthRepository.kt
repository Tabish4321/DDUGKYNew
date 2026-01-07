package com.deendayalproject.repository

import android.content.Context
import com.deendayalproject.BuildConfig.USER_NAME_FOR_APP
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.network.ApiService
import com.deendayalproject.model.LoginErrorResponse
import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.model.request.ModulesRequest
import com.deendayalproject.model.response.LoginResponse
import com.deendayalproject.model.response.ModuleResponse
import com.google.gson.Gson

class AuthRepository(context: Context) : BaseRepository<ApiService>(context) {

    suspend fun loginUser(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = apiService.loginUser(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.responseCode == 200 && !body.accessToken.isNullOrEmpty()) {
                    Result.success(body)
                } else if(body?.responseDesc == USER_NAME_FOR_APP){
                    Result.success(body)
                }else {
                    Result.failure(Exception(body?.responseDesc ?: "Login failed"))
                }
            } else {
                val error = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(error, LoginErrorResponse::class.java)
                Result.failure(Exception(errorResponse?.errorMsg ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchModules(request: ModulesRequest, token: String): Result<ModuleResponse> {
        return if(request.loginId == USER_NAME_FOR_APP)safeApiCall{  apiService.fetchModules(request)  }
          else safeApiCallWithToken(token) {
            apiService.fetchModules(request)
        }
    }
}