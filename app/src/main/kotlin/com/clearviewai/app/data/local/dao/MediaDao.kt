package com.clearviewai.app.data.local.dao

import androidx.room.*
import com.clearviewai.app.data.local.entities.MediaItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Query("SELECT * FROM media_items ORDER BY dateAdded DESC")
    fun getAllMediaItems(): Flow<List<MediaItem>>

    @Query("SELECT * FROM media_items WHERE status = 'UNDECIDED' ORDER BY dateAdded DESC")
    fun getUndecidedMediaItems(): Flow<List<MediaItem>>

    @Query("SELECT * FROM media_items WHERE status = 'TRASH' ORDER BY dateAdded DESC")
    fun getTrashItems(): Flow<List<MediaItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMediaItems(items: List<MediaItem>)

    @Update
    suspend fun updateMediaItem(item: MediaItem)

    @Query("UPDATE media_items SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)

    @Delete
    suspend fun deleteMediaItems(items: List<MediaItem>)

    @Query("DELETE FROM media_items WHERE id = :id")
    suspend fun deleteById(id: Long)
}
