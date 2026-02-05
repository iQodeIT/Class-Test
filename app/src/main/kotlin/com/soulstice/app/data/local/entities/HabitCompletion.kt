package com.soulstice.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_completions")
data class HabitCompletion(
    @PrimaryKey val id: String,
    val habitId: String,
    val date: Long, // Midnight of the completion date
    val createdAt: Long
)
