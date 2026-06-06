package com.leaseguard.android.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "tenants")
data class Tenant(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String,
    val property_address: String,
    val unit_number: String,
    val created_at: Long = System.currentTimeMillis()
)

@Serializable
@Entity(tableName = "leases")
data class Lease(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tenant_id: Long,
    val start_date: Long,
    val end_date: Long,
    val monthly_rent: Double,
    val status: String = "active", // 'active', 'renewed'
    val reminder_90_scheduled: Boolean = false,
    val reminder_60_scheduled: Boolean = false,
    val reminder_30_scheduled: Boolean = false,
    val created_at: Long = System.currentTimeMillis()
)

@Serializable
@Entity(tableName = "documents")
data class Document(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lease_id: Long,
    val display_name: String,
    val uri: String,
    val doc_type: String, // 'lease_agreement', 'inspection', 'receipt', 'other'
    val uploaded_at: Long = System.currentTimeMillis()
)

@Serializable
@Entity(tableName = "app_meta")
data class AppMeta(
    @PrimaryKey val key: String,
    val value: String
)
