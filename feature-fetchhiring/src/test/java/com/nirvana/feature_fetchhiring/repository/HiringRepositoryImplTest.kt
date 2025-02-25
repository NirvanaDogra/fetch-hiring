package com.nirvana.feature_fetchhiring.repository

import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.nirvana.feature_fetchhiring.model.HiringDTO
import com.nirvana.feature_fetchhiring.model.HiringDao
import com.nirvana.feature_fetchhiring.model.HiringEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class HiringRepositoryImplTest {

    private lateinit var repository: HiringRepositoryImpl
    @Mock
    private lateinit var hiringDao: HiringDao

    @BeforeEach
    fun setup() {
        repository = HiringRepositoryImpl(hiringDao)
    }

    @Test
    fun `insertHiringData should call dao with mapped entities`() {
        // Given
        val hiringDTOs = listOf(
            HiringDTO(id = 1, listId = 1, name = "Hiring 1"),
            HiringDTO(id = 2, listId = 1, name = "Hiring 2")
        )

        // When
        repository.insertHiringData(hiringDTOs)

        // Then
        verify(hiringDao, times(1)).insertHiringData(
            hiringDTOs.map { HiringEntity.fromDTO(it) }
        )
    }

    @Test
    fun `getAllHiringData should return mapped DTOs`() {
        // Given
        val hiringEntities = listOf(
            HiringEntity(id = 1, listId = 1, name = "Hiring 1"),
            HiringEntity(id = 2, listId = 1, name = "Hiring 2")
        )
        whenever(hiringDao.getAllHiringData()).thenReturn(hiringEntities)

        // When
        val result = repository.getAllHiringData()

        // Then
        val expectedDTOs = hiringEntities.map { it.toDTO() }
        assert(result == expectedDTOs) { "Expected $expectedDTOs but got $result" }
    }
}