package com.leaseguard.android.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LeaseDao {
    @Query("""
        SELECT * FROM leases
        JOIN tenants ON leases.tenant_id = tenants.id
        WHERE leases.status = 'active'
        ORDER BY leases.end_date ASC
    """)
    fun getActiveLeasesWithTenants(): Flow<Map<Lease, Tenant>>

    @Insert
    suspend fun insertTenant(tenant: Tenant): Long

    @Insert
    suspend fun insertLease(lease: Lease): Long

    @Insert
    suspend fun insertDocument(document: Document): Long

    @Query("SELECT * FROM tenants WHERE id = :id")
    suspend fun getTenantById(id: Long): Tenant?

    @Query("SELECT * FROM leases WHERE id = :id")
    suspend fun getLeaseById(id: Long): Lease?

    @Query("SELECT * FROM documents WHERE lease_id = :leaseId")
    fun getDocumentsForLease(leaseId: Long): Flow<List<Document>>

    @Update
    suspend fun updateLease(lease: Lease)

    @Delete
    suspend fun deleteLease(lease: Lease)

    @Query("DELETE FROM leases WHERE id = :leaseId")
    suspend fun deleteLeaseById(leaseId: Long)

    @Query("SELECT COUNT(*) FROM leases WHERE status = 'active'")
    suspend fun getActiveLeaseCount(): Int

    @Query("SELECT * FROM leases WHERE end_date <= :threshold AND status = 'active'")
    suspend fun getExpiringLeases(threshold: Long): List<Lease>

    @Transaction
    suspend fun deleteEverything() {
        deleteAllTenants()
        deleteAllLeases()
        deleteAllDocuments()
        deleteAllAppMeta()
    }

    @Query("DELETE FROM tenants")
    suspend fun deleteAllTenants()

    @Query("DELETE FROM leases")
    suspend fun deleteAllLeases()

    @Query("DELETE FROM documents")
    suspend fun deleteAllDocuments()

    @Query("DELETE FROM app_meta")
    suspend fun deleteAllAppMeta()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppMeta(meta: AppMeta)

    @Query("SELECT * FROM app_meta WHERE `key` = :key")
    suspend fun getAppMeta(key: String): AppMeta?

    @Query("SELECT * FROM tenants")
    suspend fun getAllTenants(): List<Tenant>

    @Query("SELECT * FROM leases")
    suspend fun getAllLeases(): List<Lease>

    @Query("SELECT * FROM documents")
    suspend fun getAllDocuments(): List<Document>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTenants(tenants: List<Tenant>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeases(leases: List<Lease>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(documents: List<Document>)
}
