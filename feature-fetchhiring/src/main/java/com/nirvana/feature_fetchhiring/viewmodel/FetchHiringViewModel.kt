package com.nirvana.feature_fetchhiring.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nirvana.common_ui.R.string.network_error
import com.nirvana.common_ui.R.string.something_went_wrong
import com.nirvana.common_ui.R.string.time_out
import com.nirvana.feature_fetchhiring.model.BaseDTO
import com.nirvana.feature_fetchhiring.usecase.FetchHiringUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.io.IOException
import javax.inject.Inject

sealed class HiringState {
    data object Loading : HiringState()
    data class Success(val data: List<BaseDTO>) : HiringState()
    data class Error(val message: String) : HiringState()
}

@HiltViewModel
class FetchHiringViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val useCase: FetchHiringUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<HiringState>(HiringState.Loading)
    val state: StateFlow<HiringState> = _state.asStateFlow()

    init {
        fetchHiringData()
    }

    fun fetchHiringData() {
        viewModelScope.launch {
            _state.value = HiringState.Loading
            try {
                val result = withTimeout(3000) {
                    useCase.getPreprocessedHiringData()
                }
                _state.value = HiringState.Success(result)
            } catch (e: Exception) {
                _state.value = HiringState.Error(
                    when (e) {
                        is TimeoutCancellationException -> context.getString(time_out)
                        is IOException -> context.getString(network_error)
                        else -> e.message ?: context.getString(something_went_wrong)
                    }
                )
            }
        }
    }
}
