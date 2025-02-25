package com.nirvana.feature_fetchhiring.usecase

import com.nirvana.feature_fetchhiring.model.BaseDTO
import com.nirvana.feature_fetchhiring.model.HeadingDTO
import com.nirvana.feature_fetchhiring.model.HiringDTO
import com.nirvana.feature_fetchhiring.repository.HiringRepository
import com.nirvana.feature_fetchhiring.repository.HiringService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FetchHiringUseCaseImpl @Inject constructor(
    private val hiringService: HiringService,
    private val hiringRepository: HiringRepository
) : FetchHiringUseCase {
    companion object {
        const val NOT_MODIFIED_CONFIRMATION = "HTTP 304 Not Modified"
    }
    override suspend fun getPreprocessedHiringData(): List<BaseDTO> {
        return withContext(Dispatchers.IO) {
            try {
                makeApiCall()
            } catch (e: Exception) {
                if (e.message == NOT_MODIFIED_CONFIRMATION) {
                    val cachedData = hiringRepository.getAllHiringData()
                    preProcessHiringData(cachedData)
                } else {
                    throw e
                }
            }
        }
    }

private suspend fun makeApiCall(): List<BaseDTO> {
    val data = hiringService.getHiringData()
    hiringRepository.insertHiringData(data)
    return preProcessHiringData(data)
}

private fun preProcessHiringData(hiringData: List<HiringDTO>): List<BaseDTO> = hiringData
    .filter { !it.name.isNullOrBlank() && it.name != "null" }
    .groupBy { it.listId }
    .toSortedMap()
    .flatMap { (listId, items) ->
        listOf(HeadingDTO(listId)) + items.sortedBy { it.id }
    }
}