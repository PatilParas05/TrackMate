package dev.paraspatil.trackmate.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Alert(
    val id: String,
    val title: String,
    val message: String,
    val type: AlertType,
    val senderId: String,
    val senderName: String,
    val recipientIds: List<String>,
    val timestamp: Long,
    val isRead: Boolean = false,
    val priority: AlertPriority = AlertPriority.MEDIUM
    ){
    companion object{
        fun create(
            title:String,
            message:String,
            type:AlertType,
            senderId:String,
            senderName:String,
            recipientIds:List<String>,
            priority:AlertPriority = AlertPriority.MEDIUM
        ) = Alert(
            id = "${System.currentTimeMillis()}",
            title = title,
            message = message,
            type = type,
            senderId = senderId,
            senderName = senderName,
            recipientIds = recipientIds,
            timestamp = System.currentTimeMillis(),
            isRead = false,
            priority = priority
        )
    }
}
@Serializable
enum class AlertType{
    TASK_ASSIGNED,
    LOCATION_UPDATE,
    EMERGENCY,
    INFO,
    WARNING
}
@Serializable
enum class AlertPriority{
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}