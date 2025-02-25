package com.nirvana.feature_fetchhiring.usecase

import com.nirvana.feature_fetchhiring.model.BaseDTO

interface FetchHiringUseCase {
    suspend fun getPreprocessedHiringData(): List<BaseDTO>
}
