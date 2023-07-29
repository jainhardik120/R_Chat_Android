package com.jainhardik120.rchat.ui.presentation.screens.home.chatlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.rchat.R
import com.jainhardik120.rchat.ui.presentation.screens.home.HomeRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    navigate: (String) -> Unit
) {
    val viewModel = hiltViewModel<ChatListViewModel>()
    val state by viewModel.state
    val context = LocalContext.current

    Scaffold(topBar = {
        TopAppBar(title = { context.resources.getString(R.string.app_name) }, actions = {
            IconButton(onClick = { viewModel.loadList() }) {
                Icon(imageVector = Icons.Rounded.Refresh, contentDescription = "Refresh Icon")
            }
        })
    }) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            itemsIndexed(state.chatRooms, key = { _, item ->
                item._id
            }) { _, item ->
                Surface(onClick = {
                    navigate(HomeRoutes.ChatMessages.withArgs(item._id))
                }) {
                    Column {
                        Text(text = item.chatroomName)
                        Text(text = item.lastMessage?.content ?: "New Chat")
                    }
                }
            }
        }
    }
}


