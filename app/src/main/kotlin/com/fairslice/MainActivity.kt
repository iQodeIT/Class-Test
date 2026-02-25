package com.fairslice

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fairslice.ui.dashboard.DashboardScreen
import com.fairslice.ui.onboarding.OnboardingScreen
import com.fairslice.ui.newsplit.AddPeopleScreen
import com.fairslice.ui.history.HistoryScreen
import com.fairslice.ui.newsplit.NewSplitScreen
import com.fairslice.ui.newsplit.NewSplitViewModel
import com.fairslice.ui.results.ResultsScreen
import com.fairslice.ui.settings.SettingsScreen
import com.fairslice.ui.navigation.Screen
import com.fairslice.ui.theme.FairSliceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FairSliceTheme {
                val context = LocalContext.current
                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    // Handle result if needed
                }

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FairSliceNavigation()
                }
            }
        }
    }
}

@Composable
fun FairSliceNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Onboarding.route) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToNewSplit = { navController.navigate(Screen.NewSplit.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.NewSplit.route) {
            val viewModel: NewSplitViewModel = hiltViewModel()
            NewSplitScreen(
                onNext = { name, amount, category ->
                    navController.navigate(Screen.AddPeople.createRoute(name, amount.toDouble(), category))
                },
                viewModel = viewModel
            )
        }
        composable(
            route = Screen.AddPeople.route,
            arguments = listOf(
                navArgument("billName") { type = NavType.StringType },
                navArgument("totalAmount") { type = NavType.FloatType },
                navArgument("category") { type = NavType.StringType }
            )
        ) {
            val viewModel: NewSplitViewModel = hiltViewModel() // In a real app, we'd share this
            AddPeopleScreen(
                onResults = { billId ->
                    navController.navigate(Screen.Results.createRoute(billId)) {
                        popUpTo(Screen.Dashboard.route)
                    }
                },
                viewModel = viewModel
            )
        }
        composable(
            route = Screen.Results.route,
            arguments = listOf(navArgument("billId") { type = NavType.LongType })
        ) {
            ResultsScreen(
                onDone = { navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Dashboard.route) { inclusive = true }
                } }
            )
        }
        composable(Screen.History.route) {
            HistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
