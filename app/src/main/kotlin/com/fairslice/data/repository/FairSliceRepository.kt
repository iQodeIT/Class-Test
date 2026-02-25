package com.fairslice.data.repository

import com.fairslice.data.local.FairSliceDao
import com.fairslice.data.models.Bill
import com.fairslice.data.models.Participant
import com.fairslice.data.models.UserProfile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FairSliceRepository @Inject constructor(
    private val dao: FairSliceDao
) {
    fun getAllBills(): Flow<List<Bill>> = dao.getAllBills()

    suspend fun insertBill(bill: Bill): Long = dao.insertBill(bill)

    suspend fun insertParticipants(participants: List<Participant>) = dao.insertParticipants(participants)

    suspend fun insertBillWithParticipants(bill: Bill, participants: List<Participant>): Long =
        dao.insertBillWithParticipants(bill, participants)

    fun getUserProfile(): Flow<UserProfile?> = dao.getUserProfile()

    suspend fun upsertUserProfile(profile: UserProfile) = dao.upsertUserProfile(profile)

    suspend fun getBillWithParticipants(billId: Long) = dao.getBillWithParticipants(billId)
}
