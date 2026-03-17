package dev.paraspatil.trackmate.data.repository

import dev.paraspatil.trackmate.data.local.dao.TaskDao
import dev.paraspatil.trackmate.data.local.entity.TaskEntity
import dev.paraspatil.trackmate.domain.model.Task
import dev.paraspatil.trackmate.domain.model.Result
import dev.paraspatil.trackmate.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun observeTasks(): Flow<Result<List<Task>>> = taskDao.observeAllTasks()
        .map { tasks ->
            Result.Success(tasks.map { it.toDomain() }) as Result<List<Task>>
        }
        .catch { emit(Result.Error(it)) }

    override fun observeTasksForUser(userId: String): Flow<Result<List<Task>>> =
        taskDao.observeTaskForUser(userId)
            .map { tasks ->
                Result.Success(tasks.map { it.toDomain() }) as Result<List<Task>>
            }
            .catch { emit(Result.Error(it)) }

    override suspend fun createTask(task: Task): Result<Unit> {
        return try {
            taskDao.insertTask(TaskEntity.fromDomain(task))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit> {
        return try {
            taskDao.updateTask(TaskEntity.fromDomain(task))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            taskDao.deleteTaskById(taskId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        return try {
            val task = taskDao.getTaskById(taskId)
            if (task != null) {
                Result.Success(task.toDomain())
            } else {
                Result.Error(Exception("Task not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}