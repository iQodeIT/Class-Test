package com.fairslice.ui.newsplit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.fairslice.data.models.Bill
import com.fairslice.data.models.Participant
import com.fairslice.data.repository.FairSliceRepository
import com.fairslice.logic.IncomeType
import com.fairslice.logic.ParticipantIncome
import com.fairslice.logic.SplitEngine
import com.fairslice.worker.ReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ParticipantUi(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val income: String = "",
    val incomeType: IncomeType = IncomeType.ANNUAL
)

data class NewSplitUiState(
    val billName: String = "",
    val totalAmount: String = "",
    val category: String = "🍽️ Dining",
    val participants: List<ParticipantUi> = listOf(ParticipantUi()), // Start with one participant
    val isAnonymized: Boolean = false,
    val isSaving: Boolean = false,
    val savedBillId: Long? = null
)

@HiltViewModel
class NewSplitViewModel @Inject constructor(
    private val repository: FairSliceRepository,
    private val workManager: WorkManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        NewSplitUiState(
            billName = savedStateHandle.get<String>("billName") ?: "",
            totalAmount = savedStateHandle.get<Float>("totalAmount")?.toString() ?: "",
            category = savedStateHandle.get<String>("category") ?: "🍽️ Dining"
        )
    )
    val uiState: StateFlow<NewSplitUiState> = _uiState.asStateFlow()

    fun onBillNameChange(name: String) {
        _uiState.update { it.copy(billName = name) }
    }

    fun onAmountChange(amount: String) {
        // Simple validation: only digits and at most one dot
        if (amount.count { it == '.' } <= 1 && amount.all { it.isDigit() || it == '.' }) {
            _uiState.update { it.copy(totalAmount = amount) }
        }
    }

    fun onCategoryChange(category: String) {
        _uiState.update { it.copy(category = category) }
    }

    fun toggleAnonymity() {
        _uiState.update { it.copy(isAnonymized = !it.isAnonymized) }
    }

    fun addParticipant() {
        _uiState.update { it.copy(participants = it.participants + ParticipantUi()) }
    }

    fun updateParticipant(id: String, name: String? = null, income: String? = null, incomeType: IncomeType? = null) {
        _uiState.update { state ->
            state.copy(
                participants = state.participants.map { p ->
                    if (p.id == id) {
                        p.copy(
                            name = name ?: p.name,
                            income = income ?: p.income,
                            incomeType = incomeType ?: p.incomeType
                        )
                    } else p
                }
            )
        }
    }

    fun removeParticipant(id: String) {
        if (_uiState.value.participants.size > 1) {
            _uiState.update { state ->
                state.copy(participants = state.participants.filter { it.id != id })
            }
        }
    }

    fun saveSplit() {
        val state = _uiState.value
        val totalAmountCents = ( (state.totalAmount.toDoubleOrNull() ?: 0.0) * 100).toLong()

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val participantIncomes = state.participants.map {
                ParticipantIncome(it.name, it.income.toDoubleOrNull() ?: 0.0, it.incomeType)
            }

            val splitResults = SplitEngine.calculateSplit(totalAmountCents, participantIncomes)

            val bill = Bill(
                name = state.billName,
                totalAmountCents = totalAmountCents,
                category = state.category
            )

            val participants = state.participants.map { p ->
                Participant(
                    billId = 0, // Assigned by transaction
                    name = p.name,
                    income = p.income.toDoubleOrNull() ?: 0.0,
                    incomeType = p.incomeType.name,
                    amountOwedCents = splitResults[p.name] ?: 0L
                )
            }

            val billId = repository.insertBillWithParticipants(bill, participants)

            scheduleReminder(state.billName)

            _uiState.update { it.copy(isSaving = false, savedBillId = billId) }
        }
    }

    private fun scheduleReminder(billName: String) {
        val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(workDataOf("billName" to billName))
            .setInitialDelay(1, java.util.concurrent.TimeUnit.DAYS) // Schedule for 1 day later
            .build()
        workManager.enqueue(reminderRequest)
    }
}
