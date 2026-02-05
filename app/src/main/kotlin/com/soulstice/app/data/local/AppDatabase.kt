package com.soulstice.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soulstice.app.data.local.dao.SoulsticeDao
import com.soulstice.app.data.local.entities.*

@Database(
    entities = [
        Task::class,
        Project::class,
        Habit::class,
        HabitCompletion::class,
        Resource::class,
        Client::class,
        JournalEntry::class,
        InboxItem::class,
        FocusSession::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun soulsticeDao(): SoulsticeDao
}
