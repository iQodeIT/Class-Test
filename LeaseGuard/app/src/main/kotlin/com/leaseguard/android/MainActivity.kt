package com.leaseguard.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.leaseguard.android.ui.dashboard.DashboardScreen
import com.leaseguard.android.ui.detail.LeaseDetailScreen
import com.leaseguard.android.ui.settings.SettingsScreen
import com.leaseguard.android.ui.theme.LeaseGuardTheme

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Leases : Screen("leases", "Leases", Icons.Default.Description)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    object Detail : Screen("detail/{leaseId}", "Detail", Icons.Default.Description)
    object Paywall : Screen("paywall", "Paywall", Icons.Default.Description)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LeaseGuardTheme {
                DisableSelection {
                    val navController = rememberNavController()
                    val items = listOf(
                        Screen.Dashboard,
                        Screen.Leases,
                        Screen.Settings
                    )

                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                items.forEach { screen ->
                                    NavigationBarItem(
                                        icon = { Icon(screen.icon, contentDescription = null) },
                                        label = { Text(screen.label) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
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
                            navController,
                            startDestination = Screen.Dashboard.route,
                            modifier = Modifier.padding(innerPadding),
                            enterTransition = { fadeIn() + slideInHorizontally { it } },
                            exitTransition = { fadeOut() + slideOutHorizontally { -it } },
                            popEnterTransition = { fadeIn() + slideInHorizontally { -it } },
                            popExitTransition = { fadeOut() + slideOutHorizontally { it } }
                        ) {
                            composable(Screen.Dashboard.route) {
                                DashboardScreen(navController)
                            }
                            composable(Screen.Leases.route) {
                                DashboardScreen(navController)
                            }
                            composable(Screen.Settings.route) {
                                SettingsScreen()
                            }
                            composable(Screen.Detail.route) { backStackEntry ->
                                val leaseId = backStackEntry.arguments?.getString("leaseId")?.toLong() ?: 0L
                                LeaseDetailScreen(leaseId, navController)
                            }
                            composable(Screen.Paywall.route) {
                                com.leaseguard.android.ui.components.PaywallScreen(onDismiss = { navController.popBackStack() })
                            }
                        }
                    }
                }
            }
        }
    }
}
