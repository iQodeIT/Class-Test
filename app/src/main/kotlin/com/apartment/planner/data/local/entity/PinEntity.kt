package com.apartment.planner.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pins",
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
data class PinEntity(
    @PrimaryKey val id: String,
    val roomId: String,
    val imageUri: String,
    val sourceUrl: String?,
    val notes: String,
    val estimatedPrice: Double?,
    val sizeTag: String, // SMALL, MEDIUM, LARGE
    val category: String, // FURNITURE, LIGHTING, etc.
    val status: String, // IDEA, SHORTLISTED, FINAL_CHOICE
    val isFavorite: Boolean,
    val displayOrder: Int,
    val createdAt: Long
)
