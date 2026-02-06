package com.wedding.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wedding.app.data.local.dao.WeddingDao
import com.wedding.app.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        WeddingEntity::class,
        BoardEntity::class,
        PinEntity::class,
        ChecklistItemEntity::class,
        BudgetItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WeddingDatabase : RoomDatabase() {
    abstract fun weddingDao(): WeddingDao

    companion object {
        @Volatile
        private var INSTANCE: WeddingDatabase? = null

        fun getDatabase(context: Context): WeddingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeddingDatabase::class.java,
                    "wedding_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
