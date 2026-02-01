package com.clearviewai.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_items")
data class MediaItem(
    @PrimaryKey val id: Long,
    val uri: String,
    val type: String, // "IMAGE" or "VIDEO"
    val dateAdded: Long,
    val isBlurry: Boolean = false,
    val isDuplicate: Boolean = false,
    val clutterType: String? = null, // "MEME", "SCREENSHOT"
    val status: String = "UNDECIDED", // "UNDECIDED", "KEEP", "TRASH"
    val score: Float = 0f
)
