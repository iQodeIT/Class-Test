package com.apartment.planner.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "checklist_items",
    foreignKeys = [
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["roomId"])]
)
data class ChecklistItemEntity(
    @PrimaryKey val id: String,
    val roomId: String,
    val title: String,
    val isCompleted: Boolean,
    val linkedPinIds: String, // Stored as comma-separated or JSON
    val notes: String,
    val displayOrder: Int,
    val isOptional: Boolean
)
