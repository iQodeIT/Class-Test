package com.soulstice.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulstice.app.data.local.dao.SoulsticeDao
import com.soulstice.app.data.local.entities.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dao: SoulsticeDao
) : ViewModel() {

    private val _energyLevel = MutableStateFlow("high")
    val energyLevel: StateFlow<String> = _energyLevel

    val tasks = _energyLevel.flatMapLatest { level ->
        dao.getAllTasks().map { tasks ->
            tasks.filter { it.energy == level }
        }
    }

    fun toggleEnergy() {
        _energyLevel.value = if (_energyLevel.value == "high") "low" else "high"
    }
}
