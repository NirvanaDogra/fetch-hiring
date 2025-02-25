package com.nirvana.feature_fetchhiring.repository

import com.nirvana.feature_fetchhiring.model.HiringDTO
import com.nirvana.feature_fetchhiring.model.HiringDao
import com.nirvana.feature_fetchhiring.model.HiringEntity
import javax.inject.Inject

class HiringRepositoryImpl @Inject constructor(
    private val hiringDao: HiringDao
): HiringRepository {

    override fun insertHiringData(hiringDTOs: List<HiringDTO>) {
        hiringDao.insertHiringData(
            hiringDTOs.map { HiringEntity.fromDTO(it) }
        )
    }

    override fun getAllHiringData(): List<HiringDTO> {
        return hiringDao.getAllHiringData().map { it.toDTO() }
    }
}