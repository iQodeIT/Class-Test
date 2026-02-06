package com.apartment.planner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apartments")
data class ApartmentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String, // STUDIO, ONE_BR, TWO_BR
    val sizeRange: String?, // UNDER_30, 30_TO_50, OVER_50
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long
)
