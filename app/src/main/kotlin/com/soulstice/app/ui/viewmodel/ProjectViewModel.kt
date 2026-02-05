package com.soulstice.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulstice.app.data.local.dao.SoulsticeDao
import com.soulstice.app.data.local.entities.Project
import com.soulstice.app.data.local.entities.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val dao: SoulsticeDao
) : ViewModel() {

    val projects = dao.getAllProjects()

    data class ProjectWithStats(
        val project: Project,
        val progress: Float,
        val tasks: List<Task>
    )

    val projectsWithStats = combine(dao.getAllProjects(), dao.getAllTasks()) { projects, tasks ->
        projects.map { project ->
            val projectTasks = tasks.filter { it.projectId == project.id }
            val progress = if (projectTasks.isEmpty()) 0f
            else projectTasks.count { it.status == "done" }.toFloat() / projectTasks.size
            ProjectWithStats(project, progress, projectTasks)
        }
    }

    fun addProject(name: String, description: String?, colorCode: String?) {
        viewModelScope.launch {
            val project = Project(
                id = UUID.randomUUID().toString(),
                name = name,
                description = description,
                colorCode = colorCode,
                status = "active",
                createdAt = System.currentTimeMillis()
            )
            dao.insertProject(project)
        }
    }

    fun updateTaskStatus(task: Task, newStatus: String) {
        viewModelScope.launch {
            dao.insertTask(task.copy(status = newStatus))
        }
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch {
            dao.deleteProject(project)
        }
    }
}
