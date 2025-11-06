package com.example.bulkcaptioner.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Video::class, Caption::class, StyleTemplate::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
public abstract class AppDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao
    // Add other DAOs here

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "video_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
