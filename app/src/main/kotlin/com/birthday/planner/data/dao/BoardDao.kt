package com.birthday.planner.data.dao

import androidx.room.*
import com.birthday.planner.data.model.Board
import kotlinx.coroutines.flow.Flow

@Dao
interface BoardDao {
    @Query("SELECT * FROM boards WHERE partyId = :partyId")
    fun getBoardsForParty(partyId: Long): Flow<List<Board>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoard(board: Board): Long

    @Delete
    suspend fun deleteBoard(board: Board)
}
