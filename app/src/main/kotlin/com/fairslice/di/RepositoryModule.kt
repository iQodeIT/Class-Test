package com.fairslice.di

import com.fairslice.data.local.FairSliceDao
import com.fairslice.data.repository.FairSliceRepository
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
    fun provideRepository(dao: FairSliceDao): FairSliceRepository {
        return FairSliceRepository(dao)
    }
}
