package com.apartment.planner.data.local.dao

import androidx.room.*
import com.apartment.planner.data.local.entity.PinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PinDao {
    @Query("SELECT * FROM pins WHERE roomId = :roomId ORDER BY displayOrder ASC")
    fun getPinsByRoom(roomId: String): Flow<List<PinEntity>>

    @Query("SELECT * FROM pins WHERE id = :pinId")
    suspend fun getPinById(pinId: String): PinEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPin(pin: PinEntity)

    @Update
    suspend fun updatePin(pin: PinEntity)

    @Delete
    suspend fun deletePin(pin: PinEntity)
}
