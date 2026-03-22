package dev.paraspatil.trackmate.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
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
import androidx.compose.ui.unit.dp
import dev.paraspatil.trackmate.domain.model.Alert
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertCard(
    alert: Alert,
    onClick: () -> Unit,
    onMarkRead: (() -> Unit)?=null,
    onDelete: (() -> Unit)?=null
){
    Card(
        modifier = Modifier.fillMaxSize(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (alert.isRead)
                MaterialTheme.colorScheme.surface
            else
                MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(alert.title, style = MaterialTheme.typography.titleMedium)
                Text(alert.message, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,maxLines = 2)
               Text(
                "${alert.type.name} • ${alert.priority.name} • ${alert.senderName}",
                style = MaterialTheme.typography.bodySmall
                )
                Text(
                    SimpleDateFormat("MMM dd HH:mm", Locale.getDefault()).format(alert.timestamp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (!alert.isRead && onMarkRead  != null){
                IconButton(onClick = onMarkRead){
                    Icon(Icons.Default.DoneAll, contentDescription = "Mark Read",
                        tint = MaterialTheme.colorScheme.primary)
                }
            }
            if (onDelete !=null){
                IconButton(onClick = onDelete){
                    Icon(Icons.Default.Delete, contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error)
                }
            }
        }
        }
}