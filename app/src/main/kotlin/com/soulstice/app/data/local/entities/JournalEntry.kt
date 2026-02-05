package com.soulstice.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey val id: String,
    val content: String,
    val date: Long,
    val projectId: String? = null,
    val isWin: Boolean = false,
    val createdAt: Long
)
