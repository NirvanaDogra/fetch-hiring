package com.nirvana.feature_fetchhiring.repository

import com.nirvana.feature_fetchhiring.model.HiringDTO
import retrofit2.http.GET

interface HiringService {
    companion object {
        const val HIRING_ENDPOINT = "/hiring.json"
    }

    @GET(HIRING_ENDPOINT)
    suspend fun getHiringData(): List<HiringDTO>
}