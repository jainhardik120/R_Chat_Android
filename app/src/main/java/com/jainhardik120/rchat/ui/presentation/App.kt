package com.jainhardik120.rchat.ui.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jainhardik120.rchat.ui.presentation.screens.home.HomeScreen
import com.jainhardik120.rchat.ui.presentation.screens.login.LoginScreen

@Composable
fun App() {
    val viewModel = hiltViewModel<ApplicationViewModel>()
    val navController = rememberNavController()
    val isLoggedIn = viewModel.isLoggedIn
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.popBackStack()
            navController.navigate(AppRoutes.HomeScreen.route)
        } else {
            navController.popBackStack()
            navController.navigate(AppRoutes.LoginScreen.route)
        }
    }
    NavHost(
        navController = navController, startDestination = if (isLoggedIn) {
            AppRoutes.HomeScreen.route
        } else {
            AppRoutes.LoginScreen.route
        }
    ) {
        composable(AppRoutes.LoginScreen.route){
            LoginScreen()
        }
        composable(AppRoutes.HomeScreen.route){
            HomeScreen()
        }
    }
}

