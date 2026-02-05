package com.soulstice.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulstice.app.data.local.PreferenceManager
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
    private val dao: SoulsticeDao,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    val energyLevel = preferenceManager.energyLevel

    val activeTasks = energyLevel.flatMapLatest { level ->
        dao.getActiveTasksByEnergy(level)
    }

    val allActiveTasks = dao.getActiveTasks()

    val todayTasks = dao.getAllTasks().map { tasks ->
        val today = System.currentTimeMillis() // Simple today check, should ideally be start/end of day
        tasks.filter { it.dueDate != null && it.dueDate!! <= today && it.status != "done" }
    }

    val incubatorIdeas = dao.getTasksByType("idea")

    val businessLeads = dao.getAllClients()

    val globalProgress = dao.getAllTasks().map { tasks ->
        if (tasks.isEmpty()) 0f
        else tasks.count { it.status == "done" }.toFloat() / tasks.size
    }

    val velocity = dao.getAllTasks().map { tasks ->
        val now = System.currentTimeMillis()
        val dayMillis = 24 * 60 * 60 * 1000L
        val last7Days = (0..6).map { i ->
            val start = (now / dayMillis - i) * dayMillis
            val end = start + dayMillis
            tasks.count { it.status == "done" && it.createdAt >= start && it.createdAt < end }
        }.reversed()
        last7Days
    }

    val averageVelocity = velocity.map { it.average().toFloat() }

    fun toggleEnergy() {
        viewModelScope.launch {
            preferenceManager.toggleEnergy()
        }
    }

    fun completeTask(task: Task) {
        viewModelScope.launch {
            dao.insertTask(task.copy(status = "done"))
        }
    }
}
