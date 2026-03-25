package dev.paraspatil.trackmate.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.paraspatil.trackmate.domain.model.Alert
import dev.paraspatil.trackmate.domain.model.AlertPriority
import dev.paraspatil.trackmate.domain.model.AlertType
import dev.paraspatil.trackmate.ui.theme.HighOrange
import dev.paraspatil.trackmate.ui.theme.LowBlue
import dev.paraspatil.trackmate.ui.theme.MediumAmber
import dev.paraspatil.trackmate.ui.theme.UrgentRed
import dev.paraspatil.trackmate.utils.timeAgo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertCard(
    alert: Alert,
    onClick: () -> Unit,
    onMarkRead: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    val priorityColor = alertPriorityColor(alert.priority)

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = if (!alert.isRead) 3.dp else 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!alert.isRead)
                priorityColor.copy(alpha = 0.08f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = alertTypeIcon(alert.type),
                contentDescription = null,
                tint = priorityColor,
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 2.dp)
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        alert.title,
                        style = if (!alert.isRead)
                            MaterialTheme.typography.titleSmall
                        else
                            MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        alert.timestamp.timeAgo(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    alert.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
                Text(
                    "From: ${alert.senderName}  •  ${alert.priority.name}",
                    style = MaterialTheme.typography.labelSmall,
                    color = priorityColor
                )
            }
        }
        if (onMarkRead != null || onDelete != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 4.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (!alert.isRead && onMarkRead != null) {
                    IconButton(onClick = onMarkRead) {
                        Icon(Icons.Default.DoneAll, "Mark read",
                            tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    }
                }
                if (onDelete != null) {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, "Delete",
                            tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

fun alertPriorityColor(priority: AlertPriority): Color = when (priority) {
    AlertPriority.LOW -> LowBlue
    AlertPriority.MEDIUM -> MediumAmber
    AlertPriority.HIGH -> HighOrange
    AlertPriority.URGENT -> UrgentRed
}

fun alertTypeIcon(type: AlertType): ImageVector = when (type) {
    AlertType.EMERGENCY -> Icons.Default.Emergency
    AlertType.WARNING -> Icons.Default.Warning
    AlertType.TASK_ASSIGNED -> Icons.Default.Task
    AlertType.LOCATION_UPDATE -> Icons.Default.LocationOn
    AlertType.INFO -> Icons.Default.Info
}