package com.jainhardik120.rchat.ui.presentation.screens.home.chatmessages

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChatMessagesScreen() {
    val viewModel = hiltViewModel<ChatMessagesViewModel>()
    val state = viewModel.state.value

    LazyColumn(content = {
        itemsIndexed(state.messages) { _, item ->
            Row {
                Text(text = item.content)
            }
        }
        item {
            TextField(value = state.newMessageBody, onValueChange = {
                viewModel.onEvent(
                    ChatMessageEvent.NewMessageBodyChanged(
                        it
                    )
                )
            })
            Button(
                onClick = { viewModel.onEvent(ChatMessageEvent.SendButtonClicked) },
                enabled = state.newMessageBody.isNotEmpty()
            ) {
                Text(text = "Send")
            }
        }
    })
}