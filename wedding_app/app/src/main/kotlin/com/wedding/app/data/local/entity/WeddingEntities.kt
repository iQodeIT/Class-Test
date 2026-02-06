package com.wedding.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "local_user",
    val name: String,
    val partnerName: String?,
    val profileImage: String? = null
)

@Entity(tableName = "weddings")
data class WeddingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val date: Long, // Timestamp
    val location: String?,
    val budgetRangeMin: Double,
    val budgetRangeMax: Double,
    val notes: String? = null,
    val coverImage: String? = null
)
