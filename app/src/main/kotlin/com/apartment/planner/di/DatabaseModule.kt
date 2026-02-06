package com.apartment.planner.di

import android.content.Context
import androidx.room.Room
import com.apartment.planner.data.local.PlannerDatabase
import com.apartment.planner.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.annotation.Nullable
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PlannerDatabase {
        return Room.databaseBuilder(
            context,
            PlannerDatabase::class.java,
            "planner_db"
        ).build()
    }

    @Provides
    fun provideApartmentDao(database: PlannerDatabase): ApartmentDao = database.apartmentDao()

    @Provides
    fun provideRoomDao(database: PlannerDatabase): RoomDao = database.roomDao()

    @Provides
    fun providePinDao(database: PlannerDatabase): PinDao = database.pinDao()

    @Provides
    fun provideChecklistDao(database: PlannerDatabase): ChecklistDao = database.checklistDao()

    @Provides
    fun provideBudgetDao(database: PlannerDatabase): BudgetDao = database.budgetDao()
}
