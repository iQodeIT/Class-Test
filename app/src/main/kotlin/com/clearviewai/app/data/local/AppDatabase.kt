package com.clearviewai.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.clearviewai.app.data.local.dao.MediaDao
import com.clearviewai.app.data.local.entities.MediaItem

@Database(entities = [MediaItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
}
