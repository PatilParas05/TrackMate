package dev.paraspatil.trackmate.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
import androidx.compose.ui.unit.dp
import dev.paraspatil.trackmate.domain.model.Task
import dev.paraspatil.trackmate.domain.model.TaskPriority
import dev.paraspatil.trackmate.domain.model.TaskStatus
import dev.paraspatil.trackmate.ui.theme.HighOrange
import dev.paraspatil.trackmate.ui.theme.LowBlue
import dev.paraspatil.trackmate.ui.theme.MediumAmber
import dev.paraspatil.trackmate.ui.theme.OnlineGreen
import dev.paraspatil.trackmate.ui.theme.UrgentRed
import dev.paraspatil.trackmate.utils.toFormattedDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (task.status) {
                TaskStatus.COMPLETED -> MaterialTheme.colorScheme.surfaceVariant
                TaskStatus.CANCELLED -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    task.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                if (onDelete != null) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete, "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            if (task.description.isNotBlank()) {
                Text(
                    task.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = {},
                    label = { Text(task.priority.name, style = MaterialTheme.typography.labelSmall) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = priorityColor(task.priority).copy(alpha = 0.15f),
                        labelColor = priorityColor(task.priority)
                    )
                )
                AssistChip(
                    onClick = {},
                    label = { Text(task.status.name.replace("_", " "), style = MaterialTheme.typography.labelSmall) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = statusColor(task.status).copy(alpha = 0.15f),
                        labelColor = statusColor(task.status)
                    )
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Schedule, null,
                    modifier = Modifier.width(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    "Due ${task.dueDate.toFormattedDate()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "→ ${task.assignedTo}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

fun priorityColor(priority: TaskPriority): Color = when (priority) {
    TaskPriority.LOW -> LowBlue
    TaskPriority.MEDIUM -> MediumAmber
    TaskPriority.HIGH -> HighOrange
    TaskPriority.URGENT -> UrgentRed
}

fun statusColor(status: TaskStatus): Color = when (status) {
    TaskStatus.PENDING -> MediumAmber
    TaskStatus.IN_PROGRESS -> LowBlue
    TaskStatus.COMPLETED -> OnlineGreen
    TaskStatus.CANCELLED -> Color.Gray
}