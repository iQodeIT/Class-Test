package com.soulstice.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "projects")
data class Project(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val colorCode: String? = null,
    val status: String, // "active", "completed", "archived"
    val createdAt: Long
)
