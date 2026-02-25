package com.fairslice.data.local

import androidx.room.*
import com.fairslice.data.models.Bill
import com.fairslice.data.models.Participant
import com.fairslice.data.models.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface FairSliceDao {
    @Query("SELECT * FROM bills ORDER BY date DESC")
    fun getAllBills(): Flow<List<Bill>>

    @Insert
    suspend fun insertBill(bill: Bill): Long

    @Query("SELECT * FROM participants WHERE billId = :billId")
    suspend fun getParticipantsForBill(billId: Long): List<Participant>

    @Insert
    suspend fun insertParticipants(participants: List<Participant>)

    @Query("SELECT * FROM user_profile LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Upsert
    suspend fun upsertUserProfile(profile: UserProfile)

    @Transaction
    suspend fun insertBillWithParticipants(bill: Bill, participants: List<Participant>): Long {
        val billId = insertBill(bill)
        val participantsWithId = participants.map { it.copy(billId = billId) }
        insertParticipants(participantsWithId)
        return billId
    }

    @Transaction
    @Query("SELECT * FROM bills WHERE id = :billId")
    suspend fun getBillWithParticipants(billId: Long): BillWithParticipants
}

data class BillWithParticipants(
    @Embedded val bill: Bill,
    @Relation(
        parentColumn = "id",
        entityColumn = "billId"
    )
    val participants: List<Participant>
)
