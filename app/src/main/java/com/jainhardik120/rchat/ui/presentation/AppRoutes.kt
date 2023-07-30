package com.jainhardik120.rchat.ui.presentation

sealed class AppRoutes(val route: String) {
    object LoginScreen : AppRoutes("login_screen")
    object HomeScreen : AppRoutes("home_screen")
}