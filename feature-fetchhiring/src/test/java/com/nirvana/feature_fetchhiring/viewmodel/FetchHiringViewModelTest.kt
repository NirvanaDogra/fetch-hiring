package com.nirvana.feature_fetchhiring.viewmodel


import android.content.Context
import com.nhaarman.mockitokotlin2.whenever
import com.nirvana.feature_fetchhiring.model.HeadingDTO
import com.nirvana.feature_fetchhiring.model.HiringDTO
import com.nirvana.feature_fetchhiring.usecase.FetchHiringUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.junit.jupiter.MockitoExtension
import java.io.IOException


@ExtendWith(MockitoExtension::class)
class FetchHiringViewModelTest {

    @Mock
    private lateinit var mockUseCase: FetchHiringUseCase

    @Mock
    private lateinit var mockContext: Context

    private lateinit var viewModel: FetchHiringViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `initial state should be Loading`() = runTest {
        viewModel = FetchHiringViewModel(mockContext, mockUseCase)
        assertTrue(viewModel.state.value is HiringState.Loading)
    }

    @Test
    fun `should emit Success state with data on successful fetch`() = runTest {
        val testData = listOf(
            HeadingDTO(1),
            HiringDTO(1, 1, "Item 1")
        )
        whenever(mockUseCase.getPreprocessedHiringData()).thenReturn(testData)
        viewModel = FetchHiringViewModel(mockContext, mockUseCase)
        val state = viewModel.state.take(2).toList()
        assertEquals(state[0], HiringState.Loading)
        assertEquals(state[1], HiringState.Success(testData))
    }

    @Test
    fun `test getPreprocessedHiringData with IO Exception`() = runTest {
        whenever(mockContext.getString(anyInt())).thenReturn("Network error")
        whenever(mockUseCase.getPreprocessedHiringData()).thenAnswer {
            throw IOException("IO Error Network error")
        }
        viewModel = FetchHiringViewModel(mockContext, mockUseCase)
        val state = viewModel.state.take(2).toList()
        assertEquals(state[0], HiringState.Loading)
        assertEquals(state[1], HiringState.Error("Network error"))
    }

    @Test
    fun `test getPreprocessedHiringData with Unknown error`() = runTest {
        whenever(mockUseCase.getPreprocessedHiringData()).thenAnswer {
            throw RuntimeException("Error Network error")
        }
        viewModel = FetchHiringViewModel(mockContext, mockUseCase)
        val state = viewModel.state.take(2).toList()
        assertEquals(state[0], HiringState.Loading)
        assertEquals(state[1], HiringState.Error("Error Network error"))
    }

    @Test
    fun `test getPreprocessedHiringData with Unknown error with message null`() = runTest {
        whenever(mockContext.getString(anyInt())).thenReturn("Unknown error")
        whenever(mockUseCase.getPreprocessedHiringData()).thenAnswer {
            throw RuntimeException()
        }
        viewModel = FetchHiringViewModel(mockContext, mockUseCase)
        val state = viewModel.state.take(2).toList()
        assertEquals(state[0], HiringState.Loading)
        assertEquals(state[1], HiringState.Error("Unknown error"))
    }
}