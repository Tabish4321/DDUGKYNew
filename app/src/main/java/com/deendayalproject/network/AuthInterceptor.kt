package com.deendayalproject.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
        when (response.code) {

            401 -> {
                SessionManager.notifySessionExpired()
            }

            301 -> {
                AppUpdateNotifier.notifyUpdateRequired()
            }
        }

        return response
    }
}
