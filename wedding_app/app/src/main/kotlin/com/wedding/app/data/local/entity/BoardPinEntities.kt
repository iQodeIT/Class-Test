package com.wedding.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "boards",
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
data class BoardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weddingId: Long,
    val name: String,
    val icon: String? = null,
    val color: Int? = null,
    val order: Int = 0
)

@Entity(
    tableName = "pins",
    foreignKeys = [
        ForeignKey(
            entity = BoardEntity::class,
            parentColumns = ["id"],
            childColumns = ["boardId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("boardId")]
)
data class PinEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val boardId: Long,
    val imageUrl: String,
    val sourceUrl: String? = null,
    val notes: String? = null,
    val price: Double? = null,
    val status: String = "INSPIRED", // INSPIRED, SHORTLISTED, FINAL_CHOICE
    val isFinal: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
