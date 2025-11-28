package com.deendayalproject.base

import android.content.Context
import com.deendayalproject.network.ApiService

abstract class BaseRepository<T : Any>(private val context: Context) {

    protected val apiService: T by lazy {
        RetrofitClient.getApiService(context) as T
    }

    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> retrofit2.Response<T>
    ): Result<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response body"))
            } else {
                when (response.code()) {
                    401 -> Result.failure(Exception("Unauthorized"))
                    202 -> Result.failure(Exception("No data available"))
                    else -> Result.failure(
                        Exception("API call failed: ${response.code()} - ${response.message()}")
                    )
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    protected suspend fun <T> safeApiCallWithToken(
        token: String,
        apiCall: suspend () -> retrofit2.Response<T>
    ): Result<T> {
        // Token validation logic can be added here
        return safeApiCall(apiCall)
    }
}