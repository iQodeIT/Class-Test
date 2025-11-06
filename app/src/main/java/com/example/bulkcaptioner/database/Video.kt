package com.example.bulkcaptioner.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class Video(
    @PrimaryKey val serialNumber: String,
    val uri: String,
    val fileName: String,
    val duration: Long,
    val thumbnailPath: String
)
