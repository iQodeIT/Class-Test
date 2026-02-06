package com.apartment.planner.data.local.dao

import androidx.room.*
import com.apartment.planner.data.local.entity.ApartmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApartmentDao {
    @Query("SELECT * FROM apartments LIMIT 1")
    fun getApartment(): Flow<ApartmentEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApartment(apartment: ApartmentEntity)

    @Update
    suspend fun updateApartment(apartment: ApartmentEntity)
}
