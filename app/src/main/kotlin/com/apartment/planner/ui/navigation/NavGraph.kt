package com.apartment.planner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Text
import com.apartment.planner.ui.screens.splash.SplashScreen
import com.apartment.planner.ui.screens.onboarding.OnboardingScreen
import com.apartment.planner.ui.screens.home.HomeScreen
import com.apartment.planner.ui.screens.room.RoomDetailsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.RoomDetails.route) {
            RoomDetailsScreen(navController)
        }
        composable(Screen.PinDetails.route) { backStackEntry ->
            val pinId = backStackEntry.arguments?.getString("pinId")
            // TODO: PinDetailsScreen
            Text("Pin Details: $pinId")
        }
        composable(Screen.Budget.route) {
            // TODO: BudgetScreen
            Text("Budget")
        }
        composable(Screen.Settings.route) {
            // TODO: SettingsScreen
            Text("Settings")
        }
    }
}
