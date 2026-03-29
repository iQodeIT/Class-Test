package com.soulstice.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulstice.app.data.local.dao.SoulsticeDao
import com.soulstice.app.data.local.entities.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val dao: SoulsticeDao
) : ViewModel() {

    fun upsertTask(
        id: String? = null,
        title: String,
        description: String? = null,
        status: String = "todo",
        type: String = "task",
        priority: String = "medium",
        energy: String = "high",
        projectId: String? = null,
        dueDate: Long? = null
    ) {
        viewModelScope.launch {
            val task = Task(
                id = id ?: UUID.randomUUID().toString(),
                title = title,
                description = description,
                status = status,
                type = type,
                priority = priority,
                energy = energy,
                projectId = projectId,
                dueDate = dueDate,
                createdAt = System.currentTimeMillis()
            )
            dao.insertTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            dao.deleteTask(task)
        }
    }

    fun updateTaskStatus(task: Task, newStatus: String) {
        viewModelScope.launch {
            dao.insertTask(task.copy(status = newStatus))
        }
    }
}
