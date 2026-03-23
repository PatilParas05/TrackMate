package dev.paraspatil.trackmate.ui.screens

import android.app.AlertDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import dev.paraspatil.trackmate.ui.composables.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertListScreen(
    viewModel: AlertViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    var shoeCreateDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text("Alerts (${state.value.unreadCount} unread)")
                }
            )
            if (state.value.isLoading) {
                LoadingScreen()
            } else if (state.value.alerts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No alerts yet.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.value.alerts, key = { it.id }) { alert ->
                        AlertCard(
                            alert = alert,
                            onClick = {
                                if (!alert.isRead) {
                                    viewModel.processIntent(AlertIntent.MarkAsRead(alert.id))
                                }
                            },
                            onMarkRead = {
                                viewModel.processIntent(AlertIntent.MarkAsRead(alert.id))
                            },
                            onDelete = {
                                viewModel.processIntent(AlertIntent.DeleteAlert(alert.id))
                            }
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = {shoeCreateDialog = true},
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Send Alert")
        }
    }
    if (shoeCreateDialog){
        CreateAlertDialog(
            onDismiss = { shoeCreateDialog = false },
            onSend = { alert ->
                    viewModel.processIntent(AlertIntent.SendAlert(alert))
                shoeCreateDialog = false
            }
        )
    }
}

@Composable
fun CreateAlertDialog(
    onDismiss: () -> Unit,
    onSend: (Alert) -> Unit
){
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Send Alert") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Message") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()){
                        onSend(
                            Alert.create(
                                title = title,
                                message = message,
                                type = AlertType.INFO,
                                senderId = "user_1",
                                senderName = "Current User",
                                recipientIds = listOf("user_1"),
                                priority = AlertPriority.MEDIUM
                            )
                        )
                    }
                }
            ) {
                Text("Send")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}