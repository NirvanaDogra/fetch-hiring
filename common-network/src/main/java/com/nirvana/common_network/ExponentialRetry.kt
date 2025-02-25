package com.nirvana.common_network

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.math.pow

class ExponentialRetry(private val maxRetries: Int = 3) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var attempt = 0
        val request = chain.request()

        while (attempt < maxRetries) {
            try {
                return callAndValidate(request, chain)
            } catch (e: Exception) {
                if (attempt >= maxRetries - 1) throw e
                attempt++
                runBlocking {
                    delay((1000L * 2.0.pow(attempt.toDouble())).toLong())
                }
            }
        }
        throw IOException("Max retry attempts reached")
    }

    private fun callAndValidate(request: Request, chain: Interceptor.Chain): Response {
        val response = chain.proceed(request)
        if (response.code == 304 || response.isSuccessful) {
            return response
        }
        throw IOException("Failed request with status code: ${response.code}")
    }
}
