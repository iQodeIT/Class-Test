package com.fairslice.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bills")
data class Bill(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val totalAmountCents: Long,
    val category: String,
    val date: Long = System.currentTimeMillis(),
    val isPaid: Boolean = false
)

@Entity(tableName = "participants")
data class Participant(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val billId: Long,
    val name: String,
    val income: Double, // Income can stay Double as it's often large/normalized
    val incomeType: String, // ANNUAL, MONTHLY, HOURLY
    val amountOwedCents: Long = 0,
    val isPaid: Boolean = false
)

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val annualIncome: Double,
    val currency: String = "USD"
)
