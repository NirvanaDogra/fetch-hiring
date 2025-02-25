package com.nirvana.feature_fetchhiring.repository

import com.nirvana.feature_fetchhiring.model.HiringDTO

interface HiringRepository {
    fun insertHiringData(hiringDTOs: List<HiringDTO>)
    fun getAllHiringData(): List<HiringDTO>
}