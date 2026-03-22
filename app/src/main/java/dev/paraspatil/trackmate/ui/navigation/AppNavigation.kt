package dev.paraspatil.trackmate.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.paraspatil.trackmate.ui.screens.DashboardScreen
import dev.paraspatil.trackmate.ui.screens.LocationTrackingScreen

sealed class Screen(val route: String,val label: String,val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Home", Icons.Default.Home)
    object Location : Screen("location", "Location", Icons.Default.LocationOn)


}
val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.Location,
)

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = {Icon(screen.icon, contentDescription = screen.label)},
                        label = {Text(screen.label)},
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Screen.Dashboard.route){ DashboardScreen(navController) }
            composable(Screen.Location.route) { LocationTrackingScreen() }

        }
    }
}