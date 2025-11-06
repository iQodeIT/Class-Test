package com.example.bulkcaptioner.database

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CaptionPosition {
    TOP, CENTER, BOTTOM, SMART_AUTO
}

@Entity(tableName = "captions")
data class Caption(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val videoSerialNumber: String,
    val text: String,
    val startTimeMs: Long,
    val endTimeMs: Long,
    val position: CaptionPosition,
    val offsetY: Float
)
