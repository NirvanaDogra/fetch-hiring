package com.nirvana.common_network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class EtagInterceptor @Inject constructor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val sharedPreferences = context.getSharedPreferences("etag_cache", Context.MODE_PRIVATE)
        val storedEtag = sharedPreferences.getString("etag_hiring_api", null)
        val requestBuilder = chain.request().newBuilder()
        storedEtag?.let {
            requestBuilder.addHeader("If-None-Match", it)
        }
        val response = chain.proceed(requestBuilder.build())
        response.header("ETag")?.let { newETag ->
            sharedPreferences.edit().putString("etag_hiring_api", newETag).apply()
        }

        return response
    }
}