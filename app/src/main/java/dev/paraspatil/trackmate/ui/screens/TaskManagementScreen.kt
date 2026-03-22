package dev.paraspatil.trackmate.ui.screens

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
import dev.paraspatil.trackmate.domain.model.Task
import dev.paraspatil.trackmate.domain.model.TaskPriority
import dev.paraspatil.trackmate.ui.composables.LoadingScreen
import dev.paraspatil.trackmate.ui.composables.TaskCard
import dev.paraspatil.trackmate.ui.task.TaskIntent
import dev.paraspatil.trackmate.ui.task.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManagementScreen (
    viewModel: TaskViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(title = { Text("Tasks") })

            if (state.value.isLoading){
                LoadingScreen()
            }else if (state.value.tasks.isEmpty()){
                Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
                    Text("No tasks yet. Tap + to create one.")
                }
            }else{
                LazyColumn(
                contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    items(state.value.tasks,key = {it.id}){ task ->
                        TaskCard(
                            task = task,
                            onClick = {viewModel.processIntent(TaskIntent.SelectTask(task.id))},
                            onDeleted = {viewModel.processIntent(TaskIntent.DeleteTask(task.id))}
                        )
                    }
                }
            }
        }
        FloatingActionButton(
        onClick = { showCreateDialog = true },
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp)
        ){
            Icon(Icons.Default.Add, contentDescription = "Create Task")
        }
    }
    if (showCreateDialog){
        CreateTaskDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { task ->
                viewModel.processIntent(TaskIntent.CreateTask(task))
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun CreateTaskDialog(
    onDismiss: () -> Unit,
    onCreate: (Task) -> Unit
){
    var title by remember { mutableStateOf("")  }
    var description by remember { mutableStateOf("") }
    var assignedTo by remember { mutableStateOf("user_1") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Task") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
                OutlinedTextField(
                    value = assignedTo,
                    onValueChange = { assignedTo = it },
                    label = { Text("Assigned To (userId)") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()){
                        onCreate(
                            Task.create(
                                title = title,
                                description = description,
                                assignedTo = assignedTo,
                                createdBy = "user_1",
                                priority = TaskPriority.MEDIUM,
                                dueDate = System.currentTimeMillis() + 86400000L
                            )
                        )
                    }
                }
            ) { Text("Create")}
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

