package dev.paraspatil.trackmate.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val assignedTo: String,
    val createdBy: String,
    val priority: String,
    val status: TaskStatus,
    val dueDate: Long,
    val createdAt: Long,
    val location: LocationData? = null
){
    companion object{
        fun create(
            title: String,
            description: String,
            assignedTo: String,
            createdBy: String,
            priority: String,
            dueDate: Long,
            location: LocationData? = null
        ) = Task(
            id = "${System.currentTimeMillis()}",
            title = title,
            description = description,
            assignedTo = assignedTo,
            createdBy = createdBy,
            priority = priority,
            status = TaskStatus.PENDING,
            dueDate = dueDate,
            createdAt = System.currentTimeMillis(),
            location = location

        )
    }
}

@Serializable
enum class TaskStatus{
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

@Serializable
enum class TaskPriority{
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}

@Serializable
enum class LocationData (
    val latitude: Double,
    val longitude: Double,
    val address: String=""
)


