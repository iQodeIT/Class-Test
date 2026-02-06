package com.apartment.planner.data.repository

import com.apartment.planner.data.local.dao.*
import com.apartment.planner.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlannerRepository @Inject constructor(
    private val apartmentDao: ApartmentDao,
    private val roomDao: RoomDao,
    private val pinDao: PinDao,
    private val checklistDao: ChecklistDao,
    private val budgetDao: BudgetDao
) {
    // Apartment
    fun getApartment(): Flow<ApartmentEntity?> = apartmentDao.getApartment()
    suspend fun insertApartment(apartment: ApartmentEntity) = apartmentDao.insertApartment(apartment)
    suspend fun updateApartment(apartment: ApartmentEntity) = apartmentDao.updateApartment(apartment)

    // Rooms
    fun getAllRooms(): Flow<List<RoomEntity>> = roomDao.getAllRooms()
    suspend fun getRoomById(roomId: String): RoomEntity? = roomDao.getRoomById(roomId)
    suspend fun insertRoom(room: RoomEntity) = roomDao.insertRoom(room)
    suspend fun updateRoom(room: RoomEntity) = roomDao.updateRoom(room)
    suspend fun deleteRoom(room: RoomEntity) = roomDao.deleteRoom(room)

    // Pins
    fun getPinsByRoom(roomId: String): Flow<List<PinEntity>> = pinDao.getPinsByRoom(roomId)
    suspend fun getPinById(pinId: String): PinEntity? = pinDao.getPinById(pinId)
    suspend fun insertPin(pin: PinEntity) = pinDao.insertPin(pin)
    suspend fun updatePin(pin: PinEntity) = pinDao.updatePin(pin)
    suspend fun deletePin(pin: PinEntity) = pinDao.deletePin(pin)

    // Checklist
    fun getChecklistByRoom(roomId: String): Flow<List<ChecklistItemEntity>> = checklistDao.getItemsByRoom(roomId)
    suspend fun insertChecklistItem(item: ChecklistItemEntity) = checklistDao.insertItem(item)
    suspend fun updateChecklistItem(item: ChecklistItemEntity) = checklistDao.updateItem(item)
    suspend fun deleteChecklistItem(item: ChecklistItemEntity) = checklistDao.deleteItem(item)

    // Budget
    fun getBudgetByRoom(roomId: String): Flow<List<BudgetItemEntity>> = budgetDao.getItemsByRoom(roomId)
    fun getAllBudgetItems(): Flow<List<BudgetItemEntity>> = budgetDao.getAllBudgetItems()
    suspend fun insertBudgetItem(item: BudgetItemEntity) = budgetDao.insertItem(item)
    suspend fun updateBudgetItem(item: BudgetItemEntity) = budgetDao.updateItem(item)
    suspend fun deleteBudgetItem(item: BudgetItemEntity) = budgetDao.deleteItem(item)
}
