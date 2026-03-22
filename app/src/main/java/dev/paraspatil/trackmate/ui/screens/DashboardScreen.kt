package dev.paraspatil.trackmate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.paraspatil.trackmate.ui.alert.AlertViewModel
import dev.paraspatil.trackmate.ui.composables.AlertCard
import dev.paraspatil.trackmate.ui.composables.TaskCard
import dev.paraspatil.trackmate.ui.task.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    taskViewModel: TaskViewModel = hiltViewModel(),
    alertViewModel: AlertViewModel = hiltViewModel()
){
    val taskState = taskViewModel.state.collectAsState()
    val alertState = alertViewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(title = {Text("TrackMate")})
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Overview", style = MaterialTheme.typography.titleLarge)

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Tasks",
                value = taskState.value.tasks.size.toString(),
                icon = Icons.Default.Task,
                onClick = { navController.navigate(Screen.Tasks.route) }
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Unread",
                value = alertState.value.unreadCount.toString(),
                icon = Icons.Default.Notifications,
                onClick = { navController.navigate(Screen.Alerts.route) }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
            modifier = Modifier.weight(1f),
            title = "Tasking",
            value = "Active",
            icon = Icons.Default.LocationOn,
            onClick = { navController.navigate(Screen.Location.route) }
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Team",
                value = "View",
                icon = Icons.Default.Person,
                onClick = { navController.navigate(Screen.Team.route) }
            )
        }
        if (taskState.value.tasks.isNotEmpty()){
            Text("Recent Tasks", style = MaterialTheme.typography.titleMedium)
            taskState.value.tasks.take(3).forEach { task ->
                TaskCard(task = task, onClick = {})
            }
        }
        if (alertState.value.alerts.isNotEmpty()){
            Text("Recent Alerts", style = MaterialTheme.typography.titleMedium)
            alertState.value.alerts.take(3).forEach { alert ->
                AlertCard(alert = alert, onClick = {},onMarkRead ={})
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    onClick: ()-> Unit
){
    Card(
        modifier = modifier,
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(value, style = MaterialTheme.typography.headlineMedium)
            Text(title, style = MaterialTheme.typography.bodySmall)
        }
    }
}