package com.jainhardik120.rchat.ui.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jainhardik120.rchat.data.remote.ChatSocketService
import com.jainhardik120.rchat.ui.presentation.screens.home.chatlist.ChatListScreen
import com.jainhardik120.rchat.ui.presentation.screens.home.chatmessages.ChatMessagesScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<HomeViewModel>()
    NavHost(navController = navController, startDestination = HomeRoutes.ChatList.route) {
        composable(HomeRoutes.ChatList.route) {
            ChatListScreen()
        }
        composable(HomeRoutes.ChatMessages.route) {
            ChatMessagesScreen()
        }
    }
}

sealed class HomeRoutes(val route: String) {
    object ChatList : HomeRoutes("chat_list")
    object ChatMessages : HomeRoutes("chat_message")
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val socketService: ChatSocketService
) : ViewModel() {

    init {
        connect()
    }

    private fun connect() {
        viewModelScope.launch {
            socketService.init()
        }
    }

    private fun disconnect() {
        viewModelScope.launch {
            socketService.close()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}
