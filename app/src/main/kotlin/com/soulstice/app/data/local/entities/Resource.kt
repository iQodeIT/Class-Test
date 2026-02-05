package com.soulstice.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resources")
data class Resource(
    @PrimaryKey val id: String,
    val title: String,
    val type: String, // "note", "link", "document", "image"
    val content: String?,
    val url: String?,
    val localUri: String? = null,
    val projectId: String?,
    val createdAt: Long
)
