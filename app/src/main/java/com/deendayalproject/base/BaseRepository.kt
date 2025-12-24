package com.deendayalproject.base

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder

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
                Log.e("API_CHECK", "API method invoked")

                val body = response.body()
                val json = Gson().toJson(body)
                Log.d("Api", json)

                response.body()?.let {
                    Result.success(it)
                }
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