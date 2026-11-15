package com.example.w7.data.remote

import okhttp3.Interceptor
import okhttp3.Response


class ApiKeyInterceptor(private val apiKeyProvider: () -> String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val key = apiKeyProvider()
        val requestBuilder = original.newBuilder()
        if (key.isNotBlank()) {
            requestBuilder.header("Authorization", "Bearer $key")
        }
        return chain.proceed(requestBuilder.build())
    }
}
