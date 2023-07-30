package com.jainhardik120.rchat.ui.presentation.screens.home.chatlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jainhardik120.rchat.R
import com.jainhardik120.rchat.ui.CollectUiEvents
import com.jainhardik120.rchat.ui.presentation.screens.home.HomeRoutes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ChatListScreen(
    navHostController: NavHostController
) {
    val viewModel = hiltViewModel<ChatListViewModel>()
    val state by viewModel.state
    val context = LocalContext.current
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { viewModel.loadList() })
    val hostState = remember {
        SnackbarHostState()
    }
    CollectUiEvents(
        navHostController = navHostController,
        events = viewModel.uiEvent,
        hostState = hostState
    )
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = context.resources.getString(R.string.app_name)) })
    }, snackbarHost = { SnackbarHost(hostState) }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pullRefresh(pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(state.chatRooms, key = { _, item ->
                    item._id
                }) { _, item ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                navHostController.navigate(HomeRoutes.ChatMessages.withArgs(item._id))
                            }
                            .padding(16.dp)) {
                        Text(text = item.chatroomName)
                        Text(text = item.lastMessage?.content ?: "New Chat")
                    }

                }
            }
            PullRefreshIndicator(
                refreshing = state.isLoading, state = pullRefreshState, modifier = Modifier.align(
                    Alignment.TopCenter
                ), scale = true
            )
        }
    }
}

