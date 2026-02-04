package com.soulstice.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val status: String, // "active", "completed", "archived"
    val createdAt: Long
)
