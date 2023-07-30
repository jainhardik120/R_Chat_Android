package com.jainhardik120.rchat.ui.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jainhardik120.rchat.ui.presentation.screens.home.chatlist.ChatListScreen
import com.jainhardik120.rchat.ui.presentation.screens.home.chatmessages.ChatMessagesScreen

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<HomeViewModel>()
    NavHost(navController = navController, startDestination = HomeRoutes.ChatList.route) {
        composable(HomeRoutes.ChatList.route) {
            ChatListScreen(navController)
        }
        composable(HomeRoutes.ChatMessages.route + "/{chatId}", arguments = listOf(
            navArgument("chatId") {
                type = NavType.StringType
                nullable = false
            }
        )) {
            ChatMessagesScreen()
        }
    }
}

