package com.birthday.planner.di

import android.content.Context
import androidx.room.Room
import com.birthday.planner.data.AppDatabase
import com.birthday.planner.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "birthday_planner_db"
        ).build()
    }

    @Provides
    fun providePartyDao(db: AppDatabase): PartyDao = db.partyDao()

    @Provides
    fun provideBoardDao(db: AppDatabase): BoardDao = db.boardDao()

    @Provides
    fun providePinDao(db: AppDatabase): PinDao = db.pinDao()

    @Provides
    fun provideChecklistDao(db: AppDatabase): ChecklistDao = db.checklistDao()

    @Provides
    fun provideBudgetDao(db: AppDatabase): BudgetDao = db.budgetDao()
}
