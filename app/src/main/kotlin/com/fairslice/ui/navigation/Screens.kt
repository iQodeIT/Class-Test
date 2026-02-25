package com.fairslice.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard")
    object NewSplit : Screen("new_split")
    object AddPeople : Screen("add_people/{billName}/{totalAmount}/{category}") {
        fun createRoute(billName: String, totalAmount: Double, category: String) =
            "add_people/$billName/$totalAmount/$category"
    }
    object Results : Screen("results/{billId}") {
        fun createRoute(billId: Long) = "results/$billId"
    }
    object History : Screen("history")
    object Settings : Screen("settings")
}
