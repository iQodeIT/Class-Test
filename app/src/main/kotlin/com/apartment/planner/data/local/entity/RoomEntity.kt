package com.apartment.planner.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rooms",
    foreignKeys = [
        ForeignKey(
            entity = ApartmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["apartmentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["apartmentId"])]
)
data class RoomEntity(
    @PrimaryKey val id: String,
    val apartmentId: String,
    val name: String,
    val icon: String,
    val colorTheme: String?,
    val displayOrder: Int,
    val createdAt: Long
)
