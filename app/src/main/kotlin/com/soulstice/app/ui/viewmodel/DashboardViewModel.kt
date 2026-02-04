package com.soulstice.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulstice.app.data.local.dao.SoulsticeDao
import com.soulstice.app.data.local.entities.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dao: SoulsticeDao
) : ViewModel() {

    private val _energyLevel = MutableStateFlow("high")
    val energyLevel: StateFlow<String> = _energyLevel

    val tasks = dao.getAllTasks()

    fun toggleEnergy() {
        _energyLevel.value = if (_energyLevel.value == "high") "low" else "high"
    }
}
