package com.soulstice.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.soulstice.app.ui.screens.*
import com.soulstice.app.ui.theme.*
import com.soulstice.app.ui.viewmodel.InboxViewModel
import com.soulstice.app.ui.viewmodel.MainViewModel
import com.soulstice.app.ui.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val theme by viewModel.theme.collectAsState(initial = "Light")
            val isDark = when (theme) {
                "Dark" -> true
                "Light" -> false
                else -> isSystemInDarkTheme()
            }
            SoulsticeTheme(darkTheme = isDark) {
                MainScreen()
            }
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Cockpit", Icons.Default.Home)
    object Inbox : Screen("inbox", "Inbox", Icons.Default.Inbox)
    object Projects : Screen("projects", "Projects", Icons.Default.GridView)
    object CreativeStudio : Screen("creative_studio", "Creative Studio", Icons.Default.Brush)
    object BusinessHub : Screen("business_hub", "Business Hub", Icons.Default.BusinessCenter)
    object KnowledgeBase : Screen("knowledge_base", "Knowledge Base", Icons.Default.MenuBook)
    object Journal : Screen("journal", "Journal", Icons.Default.Edit)
    object Habits : Screen("habits", "Habits", Icons.Default.AutoGraph)
    object UserGuide : Screen("user_guide", "User Guide", Icons.Default.HelpCenter)
    object Focus : Screen("focus", "Focus Timer", Icons.Default.Timer)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    object Review : Screen("review", "Daily Review", Icons.Default.AutoAwesome)
    object ProjectDetail : Screen("project_detail/{projectId}", "Project Detail", Icons.Default.GridView)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    taskViewModel: TaskViewModel = hiltViewModel(),
    inboxViewModel: InboxViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showQuickCapture by remember { mutableStateOf(false) }

    val items = listOf(
        Screen.Dashboard,
        Screen.Inbox,
        Screen.Projects,
        Screen.CreativeStudio,
        Screen.BusinessHub,
        Screen.KnowledgeBase,
        Screen.Journal,
        Screen.Habits,
        Screen.UserGuide,
        Screen.Focus,
        Screen.Settings,
        Screen.Review
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    if (showQuickCapture) {
        QuickCaptureDialog(
            onDismiss = { showQuickCapture = false },
            onCapture = { text ->
                inboxViewModel.addItem(text)
                showQuickCapture = false
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Soulstice",
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Taupe900
                )
                items.forEach { screen ->
                    NavigationDrawerItem(
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
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showQuickCapture = true },
                    containerColor = Sage600,
                    contentColor = androidx.compose.ui.graphics.Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Quick Capture")
                }
            },
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Soulstice") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(navController, startDestination = Screen.Dashboard.route, Modifier.padding(innerPadding)) {
                composable(Screen.Dashboard.route) { DashboardScreen() }
                composable(Screen.Inbox.route) { InboxScreen() }
                composable(Screen.Projects.route) {
                    ProjectsScreen(onProjectClick = { projectId ->
                        navController.navigate("project_detail/$projectId")
                    })
                }
                composable(Screen.ProjectDetail.route) { backStackEntry ->
                    val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
                    ProjectDetailScreen(projectId = projectId)
                }
                composable(Screen.CreativeStudio.route) { CreativeStudioScreen() }
                composable(Screen.BusinessHub.route) { BusinessHubScreen() }
                composable(Screen.KnowledgeBase.route) { KnowledgeBaseScreen() }
                composable(Screen.Journal.route) { JournalScreen() }
                composable(Screen.Habits.route) { HabitsScreen() }
                composable(Screen.UserGuide.route) { UserGuideScreen() }
                composable(Screen.Focus.route) { FocusScreen() }
                composable(Screen.Settings.route) { SettingsScreen() }
                composable(Screen.Review.route) { ReviewScreen() }
            }
        }
    }
}

@Composable
fun QuickCaptureDialog(onDismiss: () -> Unit, onCapture: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Quick Capture") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("What's on your mind?") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = { if (text.isNotBlank()) onCapture(text) }) {
                Text("Capture")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
