package com.apartment.planner.data.local.dao

import androidx.room.*
import com.apartment.planner.data.local.entity.ChecklistItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {
    @Query("SELECT * FROM checklist_items WHERE roomId = :roomId ORDER BY displayOrder ASC")
    fun getItemsByRoom(roomId: String): Flow<List<ChecklistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ChecklistItemEntity)

    @Update
    suspend fun updateItem(item: ChecklistItemEntity)

    @Delete
    suspend fun deleteItem(item: ChecklistItemEntity)
}
