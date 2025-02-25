package com.nirvana.feature_fetchhiring.usecase

import com.nirvana.feature_fetchhiring.model.BaseDTO
import com.nirvana.feature_fetchhiring.model.HeadingDTO
import com.nirvana.feature_fetchhiring.model.HiringDTO
import com.nirvana.feature_fetchhiring.repository.HiringRepository
import com.nirvana.feature_fetchhiring.repository.HiringService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject


class FetchHiringUseCaseImpl @Inject constructor(
    private val hiringService: HiringService,
    private val hiringRepository: HiringRepository
) : FetchHiringUseCase {
    override suspend fun getPreprocessedHiringData(): List<BaseDTO> {
        return withContext(Dispatchers.IO) {
            try {
                makeApiCall()
            } catch (e: HttpException) {
                if (e.code() == 304) {
                    val cachedData = hiringRepository.getAllHiringData()
                    if (cachedData.isEmpty()) {
                        makeApiCall()
                    } else {
                        preProcessHiringData(cachedData)
                    }
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