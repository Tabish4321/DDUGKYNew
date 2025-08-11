package com.deendayalproject.network

import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.model.request.ModulesRequest
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.response.LoginResponse
import com.deendayalproject.model.response.ModuleResponse
import com.deendayalproject.model.response.TrainingCenterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("modulenforms")
    suspend fun fetchModules(@Body request: ModulesRequest): Response<ModuleResponse>


    @POST("getTrainingCenterList")
    suspend fun getTrainingCenterList(@Body request: TrainingCenterRequest): Response<TrainingCenterResponse>
    }

