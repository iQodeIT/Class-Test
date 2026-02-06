package com.wedding.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.wedding.app.ui.screens.budget.BudgetScreen
import com.wedding.app.ui.screens.checklist.ChecklistScreen
import com.wedding.app.ui.screens.dashboard.DashboardScreen
import com.wedding.app.ui.screens.moodboard.MoodBoardDetailScreen
import com.wedding.app.ui.screens.moodboard.MoodBoardListScreen
import com.wedding.app.ui.screens.pin.DecisionModeScreen
import com.wedding.app.ui.screens.pin.PinDetailScreen
import com.wedding.app.ui.screens.timeline.TimelineScreen
import com.wedding.app.ui.screens.onboarding.OnboardingScreen
import com.wedding.app.ui.screens.wedding.CreateWeddingScreen
import com.wedding.app.ui.viewmodel.WeddingViewModel

@Composable
fun WeddingNavGraph(viewModel: WeddingViewModel) {
    val navController = rememberNavController()
    val user by viewModel.user.collectAsState()
    val weddings by viewModel.weddings.collectAsState()
    val currentWedding by viewModel.currentWedding.collectAsState()

    val startDestination = when {
        user == null -> "onboarding"
        weddings.isEmpty() -> "create_wedding"
        else -> "dashboard"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("onboarding") {
            OnboardingScreen(viewModel) {
                navController.navigate("create_wedding") {
                    popUpTo("onboarding") { inclusive = true }
                }
            }
        }
        composable("create_wedding") {
            CreateWeddingScreen(viewModel) {
                navController.navigate("dashboard") {
                    popUpTo("create_wedding") { inclusive = true }
                }
            }
        }
        composable("dashboard") {
            DashboardScreen(
                viewModel,
                onMoodBoardClick = { navController.navigate("mood_boards") },
                onChecklistClick = { navController.navigate("checklist") },
                onBudgetClick = { navController.navigate("budget") },
                onTimelineClick = { navController.navigate("timeline") }
            )
        }
        composable("budget") {
            BudgetScreen(viewModel)
        }
        composable("checklist") {
            ChecklistScreen(viewModel)
        }
        composable("timeline") {
            TimelineScreen(viewModel) { navController.popBackStack() }
        }
        composable("mood_boards") {
            MoodBoardListScreen(viewModel) { boardId ->
                navController.navigate("mood_board_detail/$boardId")
            }
        }
        composable(
            "mood_board_detail/{boardId}",
            arguments = listOf(navArgument("boardId") { type = NavType.LongType })
        ) { backStackEntry ->
            val boardId = backStackEntry.arguments?.getLong("boardId") ?: return@composable
            MoodBoardDetailScreen(
                viewModel,
                boardId,
                onPinClick = { pinId -> navController.navigate("pin_detail/$pinId") },
                onEnterDecisionMode = { navController.navigate("decision_mode/$boardId") }
            )
        }
        composable(
            "pin_detail/{pinId}",
            arguments = listOf(navArgument("pinId") { type = NavType.LongType })
        ) { backStackEntry ->
            val pinId = backStackEntry.arguments?.getLong("pinId") ?: return@composable
            PinDetailScreen(viewModel, pinId) { navController.popBackStack() }
        }
        composable(
            "decision_mode/{boardId}",
            arguments = listOf(navArgument("boardId") { type = NavType.LongType })
        ) { backStackEntry ->
            val boardId = backStackEntry.arguments?.getLong("boardId") ?: return@composable
            DecisionModeScreen(viewModel, boardId) { navController.popBackStack() }
        }
    }
}
