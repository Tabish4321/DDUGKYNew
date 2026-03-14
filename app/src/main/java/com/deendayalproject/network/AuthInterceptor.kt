package com.deendayalproject.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
        if (response.code == 401) {
            SessionManager.notifySessionExpired()
        }
        return response
    }
}


//Response{protocol=http/1.1, code=200, message=, url=https://kaushal.rural.gov.in/backend/ddugkyapp/login}

//Request{method=POST, url=https://kaushal.rural.gov.in/backend/ddugkyapp/login, headers=[Content-Type:application/json, Content-Length:215, Host:kaushal.rural.gov.in, Connection:Keep-Alive, Accept-Encoding:gzip, User-Agent:okhttp/4.12.0], tags={class retrofit2.Invocation=com.deendayalproject.network.ApiService.loginUser() [LoginRequest(loginId=DDUGKYUSER, password=b1627175e05bb4d203cdcb189adb5f36f454982e19d2497a19472fd666eef5db88daa9cf035061cb8fd726d6a64314911109677fedb5970d51f920b733754dda, imeiNo=168828a50eda312c, appVersion=1.3.1)]}}
//Request{method=POST, url=https://kaushal.rural.gov.in/backend/ddugkyapp/login, tags={class retrofit2.Invocation=com.deendayalproject.network.ApiService.loginUser() [LoginRequest(loginId=BCSOPS, password=0ed435d23e4b32d108c35206e064ba46ca27b5337db8ce7f54e97817bce770a41a6f7d9e70f7db519b93ced8bfdfcd260adaf61e982188a86ee92420cd942aec, imeiNo=168828a50eda312c, appVersion=1.3.1)]}}