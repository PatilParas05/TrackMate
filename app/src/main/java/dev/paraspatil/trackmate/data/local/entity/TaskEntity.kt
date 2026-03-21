package dev.paraspatil.trackmate.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.paraspatil.trackmate.domain.model.LocationData
import dev.paraspatil.trackmate.domain.model.Task
import dev.paraspatil.trackmate.domain.model.TaskPriority
import dev.paraspatil.trackmate.domain.model.TaskStatus


@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    @ColumnInfo(name = "assigned_to")
    val assignedTo: String,
    @ColumnInfo(name = "created_by")
    val createdBy: String,
    val priority: String,
    val status: String,
    @ColumnInfo(name = "due_date")
    val dueDate: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "location_latitude")
    val locationLatitude: Double?=null,
    @ColumnInfo(name = "location_longitude")
    val locationLongitude: Double?=null,
    @ColumnInfo(name = "location_address")
    val locationAddress: String?=null,
) {
    fun toDomain(): Task = Task(
        id = id,
        title = title,
        description = description,
        assignedTo = assignedTo,
        createdBy = createdBy,
        priority =  TaskPriority.valueOf(priority),
        status = TaskStatus.valueOf(status),
        dueDate = dueDate,
        createdAt = createdAt,
        location = if (locationLongitude != null && locationLatitude != null) {
            LocationData(
                latitude = locationLatitude,
                longitude = locationLongitude,
                address = locationAddress ?: ""
            )
        }else null

    )
    companion object{
        fun fromDomain(task: Task): TaskEntity = TaskEntity(
            id = task.id,
            title = task.title,
            description = task.description,
            assignedTo = task.assignedTo,
            createdBy = task.createdBy,
            priority = task.priority.name,
            status = task.status.name,
            dueDate = task.dueDate,
            createdAt = task.createdAt,
            locationLatitude = task.location?.latitude,
            locationLongitude = task.location?.longitude,
            locationAddress = task.location?.address
        )
    }
}