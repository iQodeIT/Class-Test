package com.apartment.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apartment.planner.data.local.entity.ApartmentEntity
import com.apartment.planner.data.local.entity.RoomEntity
import com.apartment.planner.data.repository.PlannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class HomeUiState(
    val apartment: ApartmentEntity? = null,
    val rooms: List<RoomEntity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PlannerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                repository.getApartment(),
                repository.getAllRooms()
            ) { apartment, rooms ->
                if (apartment == null) {
                    // Create a default apartment if none exists
                    val newApartment = ApartmentEntity(
                        id = UUID.randomUUID().toString(),
                        name = "My Studio Apartment",
                        type = "STUDIO",
                        sizeRange = "UNDER_30",
                        notes = "",
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    repository.insertApartment(newApartment)

                    // Add default rooms
                    val defaultRooms = listOf(
                        RoomEntity(UUID.randomUUID().toString(), newApartment.id, "Living Area", "🛋️", null, 0, System.currentTimeMillis()),
                        RoomEntity(UUID.randomUUID().toString(), newApartment.id, "Bedroom", "🛏️", null, 1, System.currentTimeMillis()),
                        RoomEntity(UUID.randomUUID().toString(), newApartment.id, "Kitchen", "🍳", null, 2, System.currentTimeMillis())
                    )
                    defaultRooms.forEach { repository.insertRoom(it) }

                    HomeUiState(apartment = newApartment, rooms = defaultRooms, isLoading = false)
                } else {
                    HomeUiState(apartment = apartment, rooms = rooms, isLoading = false)
                }
            }.collect {
                _uiState.value = it
            }
        }
    }
}
