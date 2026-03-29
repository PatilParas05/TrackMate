package dev.paraspatil.trackmate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.paraspatil.trackmate.domain.model.Alert
import dev.paraspatil.trackmate.domain.model.AlertPriority
import dev.paraspatil.trackmate.domain.model.AlertType
import dev.paraspatil.trackmate.ui.alert.AlertIntent
import dev.paraspatil.trackmate.ui.alert.AlertViewModel
import dev.paraspatil.trackmate.ui.composables.AlertCard
import dev.paraspatil.trackmate.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertListScreen(
    contentPadding: PaddingValues = PaddingValues(),
    alertViewModel: AlertViewModel = hiltViewModel()
) {
    val state by alertViewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(contentPadding)) {

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Column {
                        Text("Alerts")
                        if (state.unreadCount > 0)
                            Text("${state.unreadCount} unread",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary)
                    }
                }
            )

            if (state.alerts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.NotificationsNone, null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("No alerts yet", style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.alerts, key = { it.id }) { alert ->
                        AlertCard(
                            alert = alert,
                            onClick = {
                                if (!alert.isRead)
                                    alertViewModel.processIntent(AlertIntent.MarkAsRead(alert.id))
                            },
                            onMarkRead = { alertViewModel.processIntent(AlertIntent.MarkAsRead(alert.id)) },
                            onDelete = { alertViewModel.processIntent(AlertIntent.DeleteAlert(alert.id)) }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
        ) {
            Icon(Icons.Default.Add, "Send Alert")
        }
    }

    if (showDialog) {
        SendAlertDialog(
            onDismiss = { showDialog = false },
            onSend = { alert ->
                alertViewModel.processIntent(AlertIntent.SendAlert(alert))
                showDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendAlertDialog(onDismiss: () -> Unit, onSend: (Alert) -> Unit) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(AlertType.INFO) }
    var priority by remember { mutableStateOf(AlertPriority.MEDIUM) }
    var typeExpanded by remember { mutableStateOf(false) }
    var priorityExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Send Alert") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title, onValueChange = { title = it },
                    label = { Text("Title *") }, singleLine = true,
                    modifier = Modifier
                )
                OutlinedTextField(
                    value = message, onValueChange = { message = it },
                    label = { Text("Message") }, minLines = 2
                )
                ExposedDropdownMenuBox(expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = !typeExpanded }) {
                    OutlinedTextField(
                        value = type.name, onValueChange = {}, readOnly = true,
                        label = { Text("Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(typeExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true)
                    )
                    ExposedDropdownMenu(expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }) {
                        AlertType.values().forEach { t ->
                            DropdownMenuItem(text = { Text(t.name.replace("_", " ")) },
                                onClick = { type = t; typeExpanded = false })
                        }
                    }
                }
                ExposedDropdownMenuBox(expanded = priorityExpanded,
                    onExpandedChange = { priorityExpanded = !priorityExpanded }) {
                    OutlinedTextField(
                        value = priority.name, onValueChange = {}, readOnly = true,
                        label = { Text("Priority") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(priorityExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true)
                    )
                    ExposedDropdownMenu(expanded = priorityExpanded,
                        onDismissRequest = { priorityExpanded = false }) {
                        AlertPriority.values().forEach { p ->
                            DropdownMenuItem(text = { Text(p.name) },
                                onClick = { priority = p; priorityExpanded = false })
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isNotBlank()) {
                    onSend(Alert.create(
                        title = title.trim(),
                        message = message.trim(),
                        type = type,
                        senderId = Constants.CURRENT_USER_ID,
                        senderName = Constants.CURRENT_USER_NAME,
                        recipientIds = listOf("all"),
                        priority = priority
                    ))
                }
            }) { Text("Send") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}