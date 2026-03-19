package dev.paraspatil.trackmate.ui.task

import dev.paraspatil.trackmate.domain.model.Task

sealed class TaskIntent {
     object LoadTasks : TaskIntent()
     data class CreateTask(val task: Task) : TaskIntent()
    data class UpdateTask(val task: Task) : TaskIntent()
    data class DeleteTask(val taskId: String) : TaskIntent()
    data class SelectTask(val taskId: String) : TaskIntent()
    object ClearError : TaskIntent()
}