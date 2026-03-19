package dev.paraspatil.trackmate.ui.task

import dev.paraspatil.trackmate.domain.model.Task

data class TaskState(
    val tasks: List<Task> = emptyList(),
    val selectedTask: Task? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
sealed class TaskEffect{
    data class ShowMessage(val message: String): TaskEffect()
    object TaskCreated: TaskEffect()
    object TaskUpdated: TaskEffect()
    object TaskDeleted: TaskEffect()
}
