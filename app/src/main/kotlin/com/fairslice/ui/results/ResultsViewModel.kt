package com.fairslice.ui.results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairslice.data.local.BillWithParticipants
import com.fairslice.data.repository.FairSliceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ResultsUiState(
    val billWithParticipants: BillWithParticipants? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val repository: FairSliceRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val billId: Long = savedStateHandle.get<Long>("billId") ?: -1L

    private val _uiState = MutableStateFlow(ResultsUiState())
    val uiState: StateFlow<ResultsUiState> = _uiState.asStateFlow()

    init {
        loadResults()
    }

    private fun loadResults() {
        viewModelScope.launch {
            val results = repository.getBillWithParticipants(billId)
            _uiState.value = ResultsUiState(billWithParticipants = results, isLoading = false)
        }
    }
}
