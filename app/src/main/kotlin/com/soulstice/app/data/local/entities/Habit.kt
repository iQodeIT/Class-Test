package com.soulstice.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey val id: String,
    val title: String,
    val frequency: String, // "daily", "weekly"
    val streak: Int,
    val lastCompleted: Long?,
    val createdAt: Long
)
