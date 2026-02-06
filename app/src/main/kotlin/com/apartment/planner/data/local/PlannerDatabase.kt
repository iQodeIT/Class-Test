package com.apartment.planner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apartment.planner.data.local.dao.*
import com.apartment.planner.data.local.entity.*

@Database(
    entities = [
        ApartmentEntity::class,
        RoomEntity::class,
        PinEntity::class,
        ChecklistItemEntity::class,
        BudgetItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PlannerDatabase : RoomDatabase() {
    abstract fun apartmentDao(): ApartmentDao
    abstract fun roomDao(): RoomDao
    abstract fun pinDao(): PinDao
    abstract fun checklistDao(): ChecklistDao
    abstract fun budgetDao(): BudgetDao
}
