package com.soulstice.app.data.local.dao

import androidx.room.*
import com.soulstice.app.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SoulsticeDao {
    @Query("SELECT * FROM tasks WHERE status != 'done' AND type = 'task' ORDER BY createdAt DESC")
    fun getActiveTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE type = :type ORDER BY createdAt DESC")
    fun getTasksByType(type: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: String): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId")
    fun getCompletionsForHabit(habitId: String): Flow<List<HabitCompletion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitCompletion(completion: HabitCompletion)

    @Delete
    suspend fun deleteHabitCompletion(completion: HabitCompletion)

    @Query("SELECT * FROM resources")
    fun getAllResources(): Flow<List<Resource>>

    @Query("SELECT * FROM resources WHERE projectId = :projectId")
    fun getResourcesByProject(projectId: String): Flow<List<Resource>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResource(resource: Resource)

    @Delete
    suspend fun deleteResource(resource: Resource)

    @Query("SELECT * FROM clients")
    fun getAllClients(): Flow<List<Client>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: Client)

    @Delete
    suspend fun deleteClient(client: Client)

    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    fun getAllJournalEntries(): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_entries WHERE date >= :startOfDay AND date <= :endOfDay")
    fun getJournalEntryForDay(startOfDay: Long, endOfDay: Long): Flow<List<JournalEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalEntry(entry: JournalEntry)

    @Delete
    suspend fun deleteJournalEntry(entry: JournalEntry)

    @Query("SELECT * FROM inbox_items WHERE status = 'unprocessed' ORDER BY createdAt DESC")
    fun getUnprocessedInboxItems(): Flow<List<InboxItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInboxItem(item: InboxItem)

    @Update
    suspend fun updateInboxItem(item: InboxItem)

    @Delete
    suspend fun deleteInboxItem(item: InboxItem)

    @Query("SELECT * FROM focus_sessions ORDER BY date DESC")
    fun getAllFocusSessions(): Flow<List<FocusSession>>

    @Query("SELECT COUNT(*) FROM focus_sessions WHERE date >= :startOfDay AND mode = 'work'")
    fun getFocusSessionCountForDay(startOfDay: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFocusSession(session: FocusSession)
}
