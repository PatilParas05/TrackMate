package dev.paraspatil.trackmate.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.paraspatil.trackmate.domain.model.Task
import dev.paraspatil.trackmate.domain.repository.TaskRepository
import dev.paraspatil.trackmate.domain.model.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel(){
    private val _state = MutableStateFlow(TaskState())
    val state: StateFlow<TaskState> = _state

    private val _effect = MutableSharedFlow<TaskEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadTasks()
    }

    fun processIntent(intent: TaskIntent) {
        when (intent) {
            is TaskIntent.LoadTasks -> loadTasks()
            is TaskIntent.CreateTask -> createTask(intent.task)
            is TaskIntent.UpdateTask -> updateTask(intent.task)
            is TaskIntent.DeleteTask -> deleteTask(intent.taskId)
            is TaskIntent.SelectTask -> selectTask(intent.taskId)
            is TaskIntent.ClearError -> clearError()
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            taskRepository.observeTasks().collect { result ->
                when (result) {
                    is Result.Success -> {
                    _state.value = _state.value.copy(
                    tasks = result.data,
                    isLoading = false,
                    error = null
                    )
                }
                    is Result.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                    is Result.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                }
            }
        }
    }
    private fun createTask(task: Task){
        viewModelScope.launch {
            when(taskRepository.createTask(task)) {
                is Result.Success -> {
                    _effect.emit(TaskEffect.TaskCreated)
                    loadTasks()
                }

                is Result.Error -> {
                    _effect.emit(TaskEffect.ShowMessage("Failed to create task"))
                }

                else -> {}
            }
        }
    }
    private fun updateTask(task: Task){
        viewModelScope.launch {
            when(taskRepository.updateTask(task)) {
                is Result.Success -> {
                    _effect.emit(TaskEffect.TaskUpdated)
                    loadTasks()
                }

                is Result.Error -> {
                    _effect.emit(TaskEffect.ShowMessage("Failed to update task"))
                }

                else -> {}
            }
        }
    }
    private fun deleteTask(taskId: String){
        viewModelScope.launch {
            when(taskRepository.deleteTask(taskId)){
            is Result.Success -> {
                _effect.emit(TaskEffect.TaskDeleted)
                loadTasks()
            }
                is Result.Error -> {
                    _effect.emit(TaskEffect.ShowMessage("Failed to delete task"))
                }
                else -> {}
            }
        }
    }
    private fun selectTask(taskId: String) {
    val task = _state.value.tasks.find { it.id == taskId }
    _state.value = _state.value.copy(selectedTask = task)
    }
    private fun clearError(){
        _state.value = _state.value.copy(error = null)
    }

}