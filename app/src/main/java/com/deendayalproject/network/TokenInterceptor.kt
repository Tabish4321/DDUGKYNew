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
      //  Log.d(context.toString(), "intercept: $token")

        val newRequest = originalRequest.newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                addHeader("ddugkyappauth", "Bearer $token")
            }
        }.build()
        return chain.proceed(newRequest)
    }
}

