package com.birthday.planner.data.dao

import androidx.room.*
import com.birthday.planner.data.model.Party
import kotlinx.coroutines.flow.Flow

@Dao
interface PartyDao {
    @Query("SELECT * FROM parties")
    fun getAllParties(): Flow<List<Party>>

    @Query("SELECT * FROM parties WHERE id = :id")
    suspend fun getPartyById(id: Long): Party?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParty(party: Party): Long

    @Update
    suspend fun updateParty(party: Party)

    @Delete
    suspend fun deleteParty(party: Party)
}
