package com.birthday.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.birthday.planner.data.model.Pin
import com.birthday.planner.data.model.PinStatus
import com.birthday.planner.data.repository.PartyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BoardViewModel @Inject constructor(
    private val repository: PartyRepository
) : ViewModel() {

    private val _selectedBoardId = MutableStateFlow<Long?>(null)

    val pins = _selectedBoardId.flatMapLatest { boardId ->
        if (boardId != null) repository.getPinsForBoard(boardId)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectBoard(boardId: Long) {
        _selectedBoardId.value = boardId
    }

    fun addPin(imageUrl: String, notes: String?, cost: Float?, qty: Int?) {
        val boardId = _selectedBoardId.value ?: return
        viewModelScope.launch {
            repository.insertPin(
                Pin(
                    boardId = boardId,
                    imageUrl = imageUrl,
                    sourceLink = null,
                    notes = notes,
                    estimatedCost = cost,
                    quantity = qty,
                    status = PinStatus.IDEA,
                    isFavorite = false
                )
            )
        }
    }

    fun updatePinStatus(pin: Pin, status: PinStatus) {
        viewModelScope.launch {
            repository.updatePin(pin.copy(status = status))
        }
    }

    fun deletePin(pin: Pin) {
        viewModelScope.launch {
            repository.deletePin(pin)
        }
    }
}
