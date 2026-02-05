package com.soulstice.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "clients")
data class Client(
    @PrimaryKey val id: String,
    val name: String,
    val email: String?,
    val phone: String?,
    val status: String, // "lead", "active", "previous"
    val createdAt: Long
)
