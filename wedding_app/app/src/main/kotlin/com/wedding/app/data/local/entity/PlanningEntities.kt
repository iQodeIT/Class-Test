package com.wedding.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "checklist_items",
    foreignKeys = [
        ForeignKey(
            entity = WeddingEntity::class,
            parentColumns = ["id"],
            childColumns = ["weddingId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("weddingId")]
)
data class ChecklistItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weddingId: Long,
    val title: String,
    val dueDate: Long?,
    val isCompleted: Boolean = false,
    val phase: String, // 12 Months, 9 Months, etc.
    val linkedBoardId: Long? = null
)

@Entity(
    tableName = "budget_items",
    foreignKeys = [
        ForeignKey(
            entity = WeddingEntity::class,
            parentColumns = ["id"],
            childColumns = ["weddingId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("weddingId")]
)
data class BudgetItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weddingId: Long,
    val category: String, // Venue, Decor, etc.
    val allocatedAmount: Double,
    val spentAmount: Double = 0.0,
    val linkedPinIds: String? = null // Comma-separated or use a separate mapping table
)
