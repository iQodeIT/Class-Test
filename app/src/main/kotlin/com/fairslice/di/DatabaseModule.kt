package com.fairslice.di

import android.content.Context
import androidx.room.Room
import com.fairslice.data.local.FairSliceDao
import com.fairslice.data.local.FairSliceDatabase
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
    fun provideDatabase(@ApplicationContext context: Context): FairSliceDatabase {
        return Room.databaseBuilder(
            context,
            FairSliceDatabase::class.java,
            "fairslice_db"
        ).build()
    }

    @Provides
    fun provideDao(database: FairSliceDatabase): FairSliceDao {
        return database.dao()
    }
}
