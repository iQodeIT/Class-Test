package com.soulstice.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey val id: String,
    val duration: Long, // in milliseconds
    val mode: String, // "work", "break"
    val date: Long,
    val createdAt: Long
)
