package com.apartment.planner.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "budget_items",
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
data class BudgetItemEntity(
    @PrimaryKey val id: String,
    val roomId: String,
    val name: String,
    val category: String,
    val estimatedCost: Double,
    val actualCost: Double?,
    val linkedPinId: String?,
    val status: String, // PLANNED, BOUGHT
    val createdAt: Long
)
