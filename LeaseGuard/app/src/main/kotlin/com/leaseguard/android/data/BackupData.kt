package com.leaseguard.android.data

import kotlinx.serialization.Serializable

@Serializable
data class BackupData(
    val tenants: List<Tenant>,
    val leases: List<Lease>,
    val documents: List<Document>
)
