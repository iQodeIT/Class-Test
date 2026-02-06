package com.birthday.planner.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.birthday.planner.data.converter.Converters
import com.birthday.planner.data.dao.*
import com.birthday.planner.data.model.*

@Database(
    entities = [
        Party::class,
        Board::class,
        Pin::class,
        ChecklistItem::class,
        BudgetItem::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun partyDao(): PartyDao
    abstract fun boardDao(): BoardDao
    abstract fun pinDao(): PinDao
    abstract fun checklistDao(): ChecklistDao
    abstract fun budgetDao(): BudgetDao
}
