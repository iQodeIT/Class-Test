package com.soulstice.app.di

import android.content.Context
import androidx.room.Room
import com.soulstice.app.data.local.AppDatabase
import com.soulstice.app.data.local.dao.SoulsticeDao
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "soulstice_db"
        ).build()
    }

    @Provides
    fun provideSoulsticeDao(database: AppDatabase): SoulsticeDao {
        return database.soulsticeDao()
    }
}
