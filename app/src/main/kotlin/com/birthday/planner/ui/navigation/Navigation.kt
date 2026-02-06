package com.birthday.planner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.birthday.planner.ui.screen.*
import com.birthday.planner.ui.viewmodel.BoardViewModel
import com.birthday.planner.ui.viewmodel.PartyViewModel

object Routes {
    const val ONBOARDING = "onboarding"
    const val DASHBOARD = "dashboard"
    const val PARTY_WORKSPACE = "party_workspace/{partyId}"
    const val BOARD_DETAIL = "board_detail/{boardId}"
    const val CHECKLIST = "checklist/{partyId}"
    const val BUDGET = "budget/{partyId}"
    const val DECISION_MODE = "decision_mode/{boardId}"
    const val THEME_GENERATOR = "theme_generator"
}

@Composable
fun BirthdayPlannerNavHost() {
    val navController = rememberNavController()
    val partyViewModel: PartyViewModel = hiltViewModel()
    val boardViewModel: BoardViewModel = hiltViewModel()
    val generatorViewModel: com.birthday.planner.ui.viewmodel.ThemeGeneratorViewModel = hiltViewModel()

    val selectedParty by partyViewModel.selectedParty.collectAsState()

    com.birthday.planner.ui.theme.BirthdayPlannerTheme(partyType = selectedParty?.partyType) {
    NavHost(navController = navController, startDestination = Routes.ONBOARDING) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(onGetStarted = {
                navController.navigate(Routes.DASHBOARD)
            })
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                viewModel = partyViewModel,
                onPartyClick = { partyId ->
                    partyViewModel.selectParty(partyId)
                    navController.navigate("party_workspace/$partyId")
                },
                onAddPartyClick = {
                    // Logic to add a new party would go here
                },
                onGenerateThemeClick = {
                    navController.navigate(Routes.THEME_GENERATOR)
                }
            )
        }
        composable(
            route = Routes.PARTY_WORKSPACE,
            arguments = listOf(navArgument("partyId") { type = NavType.LongType })
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getLong("partyId") ?: return@composable
            partyViewModel.selectParty(partyId)
            PartyWorkspaceScreen(
                viewModel = partyViewModel,
                onBackClick = { navController.popBackStack() },
                onBoardClick = { boardId ->
                    boardViewModel.selectBoard(boardId)
                    navController.navigate("board_detail/$boardId")
                },
                onChecklistClick = { navController.navigate("checklist/$partyId") },
                onBudgetClick = { navController.navigate("budget/$partyId") }
            )
        }
        composable(
            route = Routes.BOARD_DETAIL,
            arguments = listOf(navArgument("boardId") { type = NavType.LongType })
        ) { backStackEntry ->
            val boardId = backStackEntry.arguments?.getLong("boardId") ?: return@composable
            BoardDetailScreen(
                viewModel = boardViewModel,
                onBackClick = { navController.popBackStack() },
                onAddPinClick = { /* logic */ },
                onPinClick = { /* logic */ },
                onDecisionModeClick = { navController.navigate("decision_mode/$boardId") }
            )
        }
        composable(
            route = Routes.CHECKLIST,
            arguments = listOf(navArgument("partyId") { type = NavType.LongType })
        ) {
            ChecklistScreen(
                viewModel = partyViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.BUDGET,
            arguments = listOf(navArgument("partyId") { type = NavType.LongType })
        ) {
            BudgetTrackerScreen(
                viewModel = partyViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.DECISION_MODE,
            arguments = listOf(navArgument("boardId") { type = NavType.LongType })
        ) {
            DecisionModeScreen(
                viewModel = boardViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.THEME_GENERATOR) {
            ThemeGeneratorScreen(
                viewModel = generatorViewModel,
                onBackClick = { navController.popBackStack() },
                onUseTheme = { suggestion ->
                    // For now, just go back
                    navController.popBackStack()
                }
            )
        }
    }
    }
}
