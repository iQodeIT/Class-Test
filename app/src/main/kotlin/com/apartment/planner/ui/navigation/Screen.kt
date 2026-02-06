package com.apartment.planner.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object RoomDetails : Screen("room_details/{roomId}") {
        fun createRoute(roomId: String) = "room_details/$roomId"
    }
    object PinDetails : Screen("pin_details/{pinId}") {
        fun createRoute(pinId: String) = "pin_details/$pinId"
    }
    object Budget : Screen("budget")
    object Settings : Screen("settings")
}
