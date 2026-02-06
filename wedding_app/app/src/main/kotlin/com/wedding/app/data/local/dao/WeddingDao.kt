package com.wedding.app.data.local.dao

import androidx.room.*
import com.wedding.app.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeddingDao {
    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM weddings ORDER BY date ASC")
    fun getWeddings(): Flow<List<WeddingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWedding(wedding: WeddingEntity): Long

    @Query("SELECT * FROM boards WHERE weddingId = :weddingId ORDER BY `order` ASC")
    fun getBoards(weddingId: Long): Flow<List<BoardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoard(board: BoardEntity)

    @Query("SELECT * FROM pins WHERE boardId = :boardId ORDER BY createdAt DESC")
    fun getPins(boardId: Long): Flow<List<PinEntity>>

    @Query("SELECT * FROM pins WHERE id = :pinId LIMIT 1")
    fun getPin(pinId: Long): Flow<PinEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPin(pin: PinEntity)

    @Update
    suspend fun updatePin(pin: PinEntity)

    @Delete
    suspend fun deletePin(pin: PinEntity)

    @Query("UPDATE pins SET status = 'INSPIRED', isFinal = 0 WHERE boardId = :boardId")
    suspend fun resetPinsFinalStatus(boardId: Long)

    @Query("UPDATE pins SET status = 'FINAL_CHOICE', isFinal = 1 WHERE id = :pinId")
    suspend fun markPinAsFinal(pinId: Long)

    @Transaction
    suspend fun selectFinalPin(pinId: Long, boardId: Long) {
        resetPinsFinalStatus(boardId)
        markPinAsFinal(pinId)
    }

    @Query("SELECT * FROM checklist_items WHERE weddingId = :weddingId ORDER BY phase ASC")
    fun getChecklist(weddingId: Long): Flow<List<ChecklistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklistItem(item: ChecklistItemEntity)

    @Update
    suspend fun updateChecklistItem(item: ChecklistItemEntity)

    @Query("SELECT * FROM budget_items WHERE weddingId = :weddingId")
    fun getBudgetItems(weddingId: Long): Flow<List<BudgetItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgetItem(item: BudgetItemEntity)
}
