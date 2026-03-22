package dev.paraspatil.trackmate.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.paraspatil.trackmate.domain.model.Task
import dev.paraspatil.trackmate.domain.model.TaskPriority
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit,
    onDeleted: (() -> Unit)? = null
){
    Card(
        modifier = Modifier.fillMaxSize(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(task.title, style = MaterialTheme.typography.titleMedium)
                if (task.description.isNotBlank()) {
                    Text(task.description, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2)
                }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SuggestionChip(
                    onClick = { },
                    label = {
                        Text(
                            task.status.name,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                )
            }
                SuggestionChip(
                    onClick = { /*TODO*/ },
                    label = { Text(
                        task.priority.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = priorityColor(task.priority)
                    ) }
                )
            }
            Text("Due : ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(task.dueDate)}",
                style = MaterialTheme.typography.bodySmall,
                )
        }
        if (onDeleted != null){
            IconButton(onClick = onDeleted) {
                Icon(Icons.Default.Delete, contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}


fun priorityColor(priority: TaskPriority): Color = when (priority) {
    TaskPriority.LOW -> Color(0xFF4CAF50)
    TaskPriority.MEDIUM -> Color(0xFFFF9800)
    TaskPriority.HIGH -> Color(0xFFF44336)
    TaskPriority.URGENT -> Color(0xFF9C27B0)
}