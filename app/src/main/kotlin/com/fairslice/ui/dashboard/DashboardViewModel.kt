package com.fairslice.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairslice.data.models.Bill
import com.fairslice.data.repository.FairSliceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class DashboardUiState(
    val recentBills: List<Bill> = emptyList(),
    val youAreOwed: Double = 0.0,
    val youOwe: Double = 0.0
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: FairSliceRepository
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = repository.getAllBills()
        .map { bills ->
            DashboardUiState(
                recentBills = bills.take(5),
                youAreOwed = bills.filter { !it.isPaid }.sumOf { it.totalAmountCents }.toDouble() / 100.0,
                youOwe = 0.0
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState())
}
