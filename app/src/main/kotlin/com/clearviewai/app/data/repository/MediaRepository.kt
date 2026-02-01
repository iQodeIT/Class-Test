package com.clearviewai.app.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.clearviewai.app.data.local.dao.MediaDao
import com.clearviewai.app.data.local.entities.MediaItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mediaDao: MediaDao,
    private val cloudSyncService: CloudSyncService
) {
    private val contentResolver: ContentResolver = context.contentResolver

    fun getUndecidedMedia(): Flow<List<MediaItem>> = mediaDao.getUndecidedMediaItems()
    fun getTrashMedia(): Flow<List<MediaItem>> = mediaDao.getTrashItems()

    suspend fun syncWithMediaStore() = withContext(Dispatchers.IO) {
        val mediaStoreItems = fetchMediaStoreItems()
        mediaDao.insertMediaItems(mediaStoreItems)
    }

    private fun fetchMediaStoreItems(): List<MediaItem> {
        val items = mutableListOf<MediaItem>()
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.DATE_ADDED
        )

        // Query Images
        queryMediaStore(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, "IMAGE", items)
        // Query Videos
        queryMediaStore(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, "VIDEO", items)

        return items
    }

    private fun queryMediaStore(
        uri: android.net.Uri,
        projection: Array<String>,
        type: String,
        items: MutableList<MediaItem>
    ) {
        contentResolver.query(
            uri,
            projection,
            null,
            null,
            "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val dateAdded = cursor.getLong(dateAddedColumn)
                val contentUri = ContentUris.withAppendedId(uri, id).toString()

                items.add(
                    MediaItem(
                        id = id,
                        uri = contentUri,
                        type = type,
                        dateAdded = dateAdded
                    )
                )
            }
        }
    }

    suspend fun updateStatus(id: Long, status: String) {
        mediaDao.updateStatus(id, status)
    }

    suspend fun deletePermanently(item: MediaItem) = withContext(Dispatchers.IO) {
        try {
            val uri = android.net.Uri.parse(item.uri)
            contentResolver.delete(uri, null, null)

            // Sync with cloud
            cloudSyncService.syncDeletion(item)

            mediaDao.deleteById(item.id)
        } catch (e: Exception) {
            // On Android 10+, this might fail if we don't have permission for the specific file
            // In a real app, we would catch RecoverableSecurityException and ask for permission
            e.printStackTrace()
        }
    }

    suspend fun deleteMultiplePermanently(items: List<MediaItem>) = withContext(Dispatchers.IO) {
        items.forEach { deletePermanently(it) }
    }
}
