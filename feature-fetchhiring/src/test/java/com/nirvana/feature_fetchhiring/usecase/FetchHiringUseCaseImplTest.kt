package com.nirvana.feature_fetchhiring.usecase

import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.whenever
import com.nirvana.feature_fetchhiring.model.HeadingDTO
import com.nirvana.feature_fetchhiring.model.HiringDTO
import com.nirvana.feature_fetchhiring.repository.HiringRepository
import com.nirvana.feature_fetchhiring.repository.HiringService
import com.nirvana.feature_fetchhiring.usecase.FetchHiringUseCaseImpl.Companion.NOT_MODIFIED_CONFIRMATION
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
class FetchHiringUseCaseImplTest {

    @Mock
    lateinit var hiringService: HiringService

    @Mock
    lateinit var hiringRepository: HiringRepository

    private lateinit var fetchHiringUseCaseImpl: FetchHiringUseCaseImpl

    @BeforeEach
    fun setup() {
        fetchHiringUseCaseImpl = FetchHiringUseCaseImpl(hiringService, hiringRepository)
    }

    @Test
    fun `when API call is successful, data should be inserted and returned`() = runTest {
        // Given
        val hiringData = listOf(
            HiringDTO(102, 1, "102"),
            HiringDTO(103, 2, "103")
        )
        whenever(hiringService.getHiringData()).thenReturn(hiringData)

        // When
        val result = fetchHiringUseCaseImpl.getPreprocessedHiringData()

        // Then
        verify(hiringRepository).insertHiringData(hiringData)
        verify(hiringService, times(1)).getHiringData()
        assertEquals(4, result.size) // 2 items + 2 heading
    }

    @Test
    fun `when API call is successful, handle grouping and sorting with single group`() = runTest {
        // Given
        val hiringData = listOf(
            HiringDTO(101, 1, null),
            HiringDTO(102, 1, "null"),
            HiringDTO(103, 1, ""),
            HiringDTO(1010, 1, "list 1010"),
            HiringDTO(104, 1, "list 104"),
            HiringDTO(105, 1, "list 105")
        )
        whenever(hiringService.getHiringData()).thenReturn(hiringData)

        // When
        val result = fetchHiringUseCaseImpl.getPreprocessedHiringData()

        // Then
        verify(hiringRepository).insertHiringData(hiringData)
        verify(hiringService, times(1)).getHiringData()
        assertEquals(4, result.size) // 3 items + 1 heading
        assertEquals(HeadingDTO(1), result[0])
        assertEquals(HiringDTO(id = 104, listId = 1, name = "list 104"), result[1])
        assertEquals(HiringDTO(id = 105, listId = 1, name = "list 105"), result[2])
        assertEquals(HiringDTO(id = 1010, listId = 1, name = "list 1010"), result[3])
    }

    @Test
    fun `when API call is successful, handle grouping and sorting with two group`() = runTest {
        // Given
        val hiringData = listOf(
            HiringDTO(102, 1, "null"),
            HiringDTO(103, 1, ""),
            HiringDTO(1010, 1, "list 1010"),
            HiringDTO(104, 1, "list 104"),
            HiringDTO(105, 1, "list 105"),
            HiringDTO(105, 2, "list 105")
        )
        whenever(hiringService.getHiringData()).thenReturn(hiringData)

        // When
        val result = fetchHiringUseCaseImpl.getPreprocessedHiringData()

        // Then
        verify(hiringRepository).insertHiringData(hiringData)
        verify(hiringService, times(1)).getHiringData()
        assertEquals(6, result.size) // 3 items + 1 heading
        assertEquals(HeadingDTO(1), result[0])
        assertEquals(HiringDTO(id = 104, listId = 1, name = "list 104"), result[1])
        assertEquals(HiringDTO(id = 105, listId = 1, name = "list 105"), result[2])
        assertEquals(HiringDTO(id = 1010, listId = 1, name = "list 1010"), result[3])
        assertEquals(HeadingDTO(2), result[4])
        assertEquals(HiringDTO(id = 105, listId = 2, name = "list 105"), result[5])
    }

    @Test
    fun `when API call has error, handle data retrieval from cache`() = runTest {
        // Given
        val exception = RuntimeException(NOT_MODIFIED_CONFIRMATION)
        doThrow(exception).whenever(hiringService).getHiringData()
        whenever(hiringRepository.getAllHiringData()).thenReturn(listOf(HiringDTO(1, 1, "name1")))
        // When

        val result = fetchHiringUseCaseImpl.getPreprocessedHiringData()
        // Then
        verify(hiringService, times(1)).getHiringData()
        verify(hiringRepository).getAllHiringData()
        assertEquals(result[0], HeadingDTO(1))
        assertEquals(result[1], HiringDTO(1, 1, "name1"))
    }

    @Test
    fun `when API call has unknown error, handle data retrieval from cache`() = runTest {
        // Given
        val exception = RuntimeException("unknown error")
        doThrow(exception).whenever(hiringService).getHiringData()
        // When
        try {
            fetchHiringUseCaseImpl.getPreprocessedHiringData()
        } catch (e: Exception) {
            // then
            assertEquals(exception.message, e.message)
        }

    }
}

