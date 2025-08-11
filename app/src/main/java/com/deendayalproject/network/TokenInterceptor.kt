package com.deendayalproject.network
import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip token header for login endpoint if needed
        if (originalRequest.url.encodedPath.endsWith("/ddugkyapp/login")) {
            return chain.proceed(originalRequest)
        }

        val prefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)
        Log.d(context.toString(), "intercept: $token")

        val newRequest = originalRequest.newBuilder().apply {

              // addHeader("ddugkyauth","Bearer eyJhbGciOiJIUzUxMiJ9.eyJwYXlsb2FkIjp7ImxvZ2luSWQiOiJNQU5BU0kiLCJsZXZlbENkIjoiMTMiLCJsZXZlbFJvbGVDZCI6bnVsbCwiZW50aXR5Q29kZSI6IjI5Iiwib3JnSWQiOiJXQjIwMjVSRjAxMTQiLCJocklkIjoiNjkyIiwiaW5zdGl0dXRlIjpudWxsLCJ0cmFpbmdDZW50ZXJDb2RlIjpudWxsfSwic3ViIjoiTUFOQVNJIiwiaWF0IjoxNzUzNzg1MzcyLCJleHAiOjE3NTM3OTEzNzJ9.6vH_1_zEC8PeLxjLH78_pvqqI-QPFdV50B0GZ1t-e8QkmUbYgWy55jL9CpIu2YzQjzpbUxZTjEeS9kIQ87VwXw")
                addHeader("ddugkyappauth","Bearer $token")

        }.build()

        return chain.proceed(newRequest)

    }
}

