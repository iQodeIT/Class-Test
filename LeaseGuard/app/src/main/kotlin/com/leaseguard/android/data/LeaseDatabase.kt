package com.leaseguard.android.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tenant::class, Lease::class, Document::class, AppMeta::class], version = 1, exportSchema = false)
abstract class LeaseDatabase : RoomDatabase() {
    abstract fun leaseDao(): LeaseDao

    companion object {
        @Volatile
        private var INSTANCE: LeaseDatabase? = null

        fun getDatabase(context: Context): LeaseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LeaseDatabase::class.java,
                    "leaseguard_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
