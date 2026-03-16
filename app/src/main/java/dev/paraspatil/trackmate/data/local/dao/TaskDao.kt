package dev.paraspatil.trackmate.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.paraspatil.trackmate.data.local.entity.TaskEntity
import dev.paraspatil.trackmate.domain.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER By created_at DESC")
    fun observeAllTasks():Flow<List<TaskEntity>>
    @Query("SELECT * FROM tasks WHERE assigned_to =:userId ORDER BY created_at DESC")
    fun observeTaskForUser(userId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: String): Flow<Task>

    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: String)

}