package com.deendayalproject.repository

import android.R.attr.version
import android.content.Context
import android.widget.Toast
import com.deendayalproject.BuildConfig.USER_NAME_FOR_APP
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.network.ApiService
import com.deendayalproject.model.LoginErrorResponse
import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.model.request.ModulesRequest
import com.deendayalproject.model.request.SaltRequest
import com.deendayalproject.model.response.LoginResponse
import com.deendayalproject.model.response.ModuleResponse
import com.deendayalproject.util.AppUtil
import com.google.gson.Gson

class AuthRepository(context: Context) : BaseRepository<ApiService>(context) {
    suspend fun loginUser(request: LoginRequest): Result<LoginResponse> {
        return try {
            val saltResp = apiService.getSalt(SaltRequest(request.loginId))
            val saltedRequest = request.copy(
                nonce = saltResp.nonce
            )

            if(saltResp.responseCode==404){
                Toast.makeText(context.applicationContext, saltResp?.responseDesc ?: "Invalid Login ID.", Toast.LENGTH_SHORT).show()
            }

            val response = apiService.loginUser(saltedRequest)
            if (response.isSuccessful) {
                val body = response.body()
                when {
                    body?.responseCode == 200 && body.accessToken.isNotEmpty() -> Result.success(body)

                    body?.responseDesc == "DDUGKYUSERDESC" -> Result.success(body)

                    else -> Result.failure(Exception(body?.responseDesc ?: "Login failed"))
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

    suspend fun logOutUser( token: String): Result<LoginResponse>{
       return safeApiCallWithToken(token) {
            apiService.logOutUser()
        }
    }

}