package com.apartment.planner.data.local.dao

import androidx.room.*
import com.apartment.planner.data.local.entity.BudgetItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget_items WHERE roomId = :roomId ORDER BY createdAt ASC")
    fun getItemsByRoom(roomId: String): Flow<List<BudgetItemEntity>>

    @Query("SELECT * FROM budget_items ORDER BY createdAt ASC")
    fun getAllBudgetItems(): Flow<List<BudgetItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: BudgetItemEntity)

    @Update
    suspend fun updateItem(item: BudgetItemEntity)

    @Delete
    suspend fun deleteItem(item: BudgetItemEntity)
}
