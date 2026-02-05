package com.soulstice.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val status: String, // "todo", "in_progress", "done"
    val type: String = "task", // "task", "idea"
    val priority: String, // "low", "medium", "high"
    val energy: String, // "low", "high"
    val projectId: String?,
    val recurringLogic: String? = null,
    val dueDate: Long?,
    val createdAt: Long
)
