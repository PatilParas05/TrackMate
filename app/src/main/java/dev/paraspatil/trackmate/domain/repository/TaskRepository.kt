package dev.paraspatil.trackmate.domain.repository

import dev.paraspatil.trackmate.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTasks(): Flow<Result<List<Task>>>
    fun observeTaskForUserId(userId: String): Flow<Result<List<Task>>>
    suspend fun createTask(task: Task): Result<Unit>
    suspend fun updateTask(task: Task): Result<Unit>
    suspend fun deleteTask(taskId: String): Result<Unit>
    suspend fun getTaskById(taskId: String): Result<Task>
}
