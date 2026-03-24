package dev.paraspatil.trackmate.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.paraspatil.trackmate.ui.alert.AlertViewModel
import dev.paraspatil.trackmate.ui.screens.AlertListScreen
import dev.paraspatil.trackmate.ui.screens.DashboardScreen
import dev.paraspatil.trackmate.ui.screens.LocationTrackingScreen
import dev.paraspatil.trackmate.ui.screens.TaskManagementScreen
import dev.paraspatil.trackmate.ui.screens.TeamScreen

sealed class Screen(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Dashboard : Screen("dashboard", "Home", Icons.Filled.Dashboard, Icons.Outlined.Dashboard)
    object Location : Screen("location", "Map", Icons.Filled.LocationOn, Icons.Outlined.LocationOn)
    object Tasks : Screen("tasks", "Tasks", Icons.Filled.Task, Icons.Outlined.Task)
    object Alerts : Screen("alerts", "Alerts", Icons.Filled.Notifications, Icons.Outlined.Notifications)
    object Team : Screen("team", "Team", Icons.Filled.People, Icons.Outlined.People)
}

val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.Location,
    Screen.Tasks,
    Screen.Alerts,
    Screen.Team
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val alertViewModel: AlertViewModel = hiltViewModel()
    val alertState by alertViewModel.state.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (screen is Screen.Alerts && alertState.unreadCount > 0) {
                                        Badge { Text("${alertState.unreadCount}") }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                    contentDescription = screen.label
                                )
                            }
                        },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(navController = navController, contentPadding = innerPadding)
            }
            composable(Screen.Location.route) {
                LocationTrackingScreen(contentPadding = innerPadding)
            }
            composable(Screen.Tasks.route) {
                TaskManagementScreen(contentPadding = innerPadding)
            }
            composable(Screen.Alerts.route) {
                AlertListScreen(contentPadding = innerPadding, viewModel = alertViewModel)
            }
            composable(Screen.Team.route) {
                TeamScreen(contentPadding = innerPadding)
            }
        }
    }
}