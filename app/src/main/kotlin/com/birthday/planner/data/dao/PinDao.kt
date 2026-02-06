package com.birthday.planner.data.dao

import androidx.room.*
import com.birthday.planner.data.model.Pin
import kotlinx.coroutines.flow.Flow

@Dao
interface PinDao {
    @Query("SELECT * FROM pins WHERE boardId = :boardId")
    fun getPinsForBoard(boardId: Long): Flow<List<Pin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPin(pin: Pin): Long

    @Update
    suspend fun updatePin(pin: Pin)

    @Delete
    suspend fun deletePin(pin: Pin)
}
