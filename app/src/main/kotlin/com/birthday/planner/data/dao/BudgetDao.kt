package com.birthday.planner.data.dao

import androidx.room.*
import com.birthday.planner.data.model.BudgetItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget_items WHERE partyId = :partyId")
    fun getBudgetForParty(partyId: Long): Flow<List<BudgetItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgetItem(item: BudgetItem): Long

    @Update
    suspend fun updateBudgetItem(item: BudgetItem)

    @Delete
    suspend fun deleteBudgetItem(item: BudgetItem)
}
