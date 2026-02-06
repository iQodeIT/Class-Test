package com.apartment.planner.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apartment.planner.data.local.entity.BudgetItemEntity
import com.apartment.planner.data.local.entity.ChecklistItemEntity
import com.apartment.planner.data.local.entity.PinEntity
import com.apartment.planner.data.local.entity.RoomEntity
import android.widget.Toast
import android.content.Context
import com.apartment.planner.data.repository.PlannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class RoomUiState(
    val room: RoomEntity? = null,
    val pins: List<PinEntity> = emptyList(),
    val checklist: List<ChecklistItemEntity> = emptyList(),
    val budget: List<BudgetItemEntity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val repository: PlannerRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val roomId: String = checkNotNull(savedStateHandle["roomId"])

    private val _uiState = MutableStateFlow(RoomUiState())
    val uiState: StateFlow<RoomUiState> = _uiState.asStateFlow()

    init {
        loadRoomData()
    }

    private fun loadRoomData() {
        viewModelScope.launch {
            val room = repository.getRoomById(roomId)
            combine(
                repository.getPinsByRoom(roomId),
                repository.getChecklistByRoom(roomId),
                repository.getBudgetByRoom(roomId)
            ) { pins, checklist, budget ->
                RoomUiState(
                    room = room,
                    pins = pins,
                    checklist = checklist,
                    budget = budget,
                    isLoading = false
                )
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun toggleChecklistItem(item: ChecklistItemEntity) {
        viewModelScope.launch {
            repository.updateChecklistItem(item.copy(isCompleted = !item.isCompleted))
        }
    }

    fun confirmPinChoice(selectedPin: PinEntity) {
        viewModelScope.launch {
            // Set selected pin as FINAL_CHOICE
            repository.updatePin(selectedPin.copy(status = "FINAL_CHOICE"))

            Toast.makeText(context, "${selectedPin.notes} decided! 🎉", Toast.LENGTH_SHORT).show()

            // Optionally archive others or just leave them as SHORTLISTED
            // For now, let's just update the selected one.
        }
    }

    fun addPin(imageUri: String, notes: String) {
        viewModelScope.launch {
            val pin = PinEntity(
                id = UUID.randomUUID().toString(),
                roomId = roomId,
                imageUri = imageUri,
                sourceUrl = null,
                notes = notes,
                estimatedPrice = null,
                sizeTag = "MEDIUM",
                category = "FURNITURE",
                status = "IDEA",
                isFavorite = false,
                displayOrder = _uiState.value.pins.size,
                createdAt = System.currentTimeMillis()
            )
            repository.insertPin(pin)
        }
    }
}
