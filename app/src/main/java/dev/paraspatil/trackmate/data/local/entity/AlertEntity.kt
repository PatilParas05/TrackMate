package dev.paraspatil.trackmate.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.paraspatil.trackmate.domain.model.Alert
import dev.paraspatil.trackmate.domain.model.AlertPriority
import dev.paraspatil.trackmate.domain.model.AlertType

@Entity(tableName = "alerts")
data class AlertEntity (
    @PrimaryKey
    val id: String,
    val title: String,
    val message: String,
    val type: String,
    @ColumnInfo(name = "sender_id")
    val senderId: String,
    @ColumnInfo(name = "sender_name")
    val senderName: String,
    @ColumnInfo(name = "recipient_ids")
    val recipientIds: String,
    val timestamp: Long,
    @ColumnInfo(name = "is_read")
    val isRead: Boolean = false,
    val priority: String = "MEDIUM"

    ){
    fun toDomain(): Alert = Alert(
        id = id,
        title = title,
        message = message,
        type = AlertType.valueOf(type),
        senderId = senderId,
        senderName = senderName,
        recipientIds = recipientIds.split(",").filter { it.isNotEmpty() },
        timestamp = timestamp,
        isRead = isRead,
        priority = AlertPriority.valueOf(priority)
        )
    companion object{
        fun fromDomain(alert: Alert): AlertEntity = AlertEntity(
            id = alert.id,
            title = alert.title,
            message = alert.message,
            type = alert.type.name,
            senderId = alert.senderId,
            senderName = alert.senderName,
            recipientIds = alert.recipientIds.joinToString(","),
            timestamp = alert.timestamp,
            isRead = alert.isRead,
            priority = alert.priority.name
        )
    }
}