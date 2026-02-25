package com.fairslice.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fairslice.data.models.Bill
import com.fairslice.data.models.Participant
import com.fairslice.data.models.UserProfile

@Database(entities = [Bill::class, Participant::class, UserProfile::class], version = 1)
abstract class FairSliceDatabase : RoomDatabase() {
    abstract fun dao(): FairSliceDao
}
