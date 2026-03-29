package com.soulstice.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "inbox_items")
data class InboxItem(
    @PrimaryKey val id: String,
    val content: String,
    val status: String, // "unprocessed", "processed"
    val createdAt: Long
)
