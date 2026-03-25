package dev.paraspatil.trackmate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.paraspatil.trackmate.domain.model.TaskStatus
import dev.paraspatil.trackmate.ui.alert.AlertViewModel
import dev.paraspatil.trackmate.ui.composables.AlertCard
import dev.paraspatil.trackmate.ui.composables.TaskCard
import dev.paraspatil.trackmate.ui.navigation.Screen
import dev.paraspatil.trackmate.ui.task.TaskIntent
import dev.paraspatil.trackmate.ui.task.TaskViewModel
import dev.paraspatil.trackmate.ui.theme.LowBlue
import dev.paraspatil.trackmate.ui.theme.OnlineGreen
import dev.paraspatil.trackmate.ui.theme.UrgentRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    contentPadding: PaddingValues = PaddingValues(),
    taskViewModel: TaskViewModel = hiltViewModel(),
    alertViewModel: AlertViewModel = hiltViewModel()
) {
    val taskState by taskViewModel.state.collectAsState()
    val alertState by alertViewModel.state.collectAsState()

    val completedTasks = taskState.tasks.count { it.status == TaskStatus.COMPLETED }
    val pendingTasks = taskState.tasks.count { it.status == TaskStatus.PENDING || it.status == TaskStatus.IN_PROGRESS }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("TrackMate", style = MaterialTheme.typography.headlineMedium) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(Modifier.weight(1f), "Active Tasks", pendingTasks.toString(),
                    Icons.Default.Task, LowBlue) { navController.navigate(Screen.Tasks.route) }
                StatCard(Modifier.weight(1f), "Completed", completedTasks.toString(),
                    Icons.Default.CheckCircle, OnlineGreen) { navController.navigate(Screen.Tasks.route) }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(Modifier.weight(1f), "Unread Alerts", alertState.unreadCount.toString(),
                    Icons.Default.Notifications, UrgentRed) { navController.navigate(Screen.Alerts.route) }
                StatCard(Modifier.weight(1f), "Live Location",
                    "ON", Icons.Default.LocationOn, OnlineGreen) { navController.navigate(Screen.Location.route) }
            }

            // Recent Tasks
            if (taskState.tasks.isNotEmpty()) {
                SectionHeader("Recent Tasks") { navController.navigate(Screen.Tasks.route) }
                taskState.tasks.take(3).forEach { task ->
                    TaskCard(
                        task = task,
                        onClick = { taskViewModel.processIntent(TaskIntent.SelectTask(task.id)) }
                    )
                }
            }

            // Recent Alerts
            if (alertState.alerts.isNotEmpty()) {
                SectionHeader("Recent Alerts") { navController.navigate(Screen.Alerts.route) }
                alertState.alerts.take(3).forEach { alert ->
                    AlertCard(alert = alert, onClick = {})
                }
            }

            Spacer(Modifier.height(8.dp))
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
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(22.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium)
            Text(title, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(
            "See all →",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}