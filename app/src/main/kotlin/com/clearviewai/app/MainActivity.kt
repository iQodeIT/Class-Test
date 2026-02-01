package com.clearviewai.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.clearviewai.app.ui.screens.SettingsScreen
import com.clearviewai.app.ui.screens.SwipeScreen
import com.clearviewai.app.ui.screens.TrashReviewScreen
import com.clearviewai.app.ui.theme.ClearViewAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClearViewAITheme {
                com.clearviewai.app.ui.components.PermissionHandler {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "swipe") {
        composable("swipe") {
            SwipeScreen(
                onNavigateToTrash = { navController.navigate("trash") }
            )
        }
        composable("trash") {
            TrashReviewScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("settings") {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
