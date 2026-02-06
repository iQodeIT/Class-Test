package com.apartment.planner.di

import com.apartment.planner.data.local.dao.*
import com.apartment.planner.data.repository.PlannerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePlannerRepository(
        apartmentDao: ApartmentDao,
        roomDao: RoomDao,
        pinDao: PinDao,
        checklistDao: ChecklistDao,
        budgetDao: BudgetDao
    ): PlannerRepository {
        return PlannerRepository(apartmentDao, roomDao, pinDao, checklistDao, budgetDao)
    }
}
