package com.jainhardik120.rchat.ui.presentation.screens.home.chatlist

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainhardik120.rchat.data.remote.ChatSocketService
import com.jainhardik120.rchat.data.remote.dto.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun ChatListScreen() {

    val viewModel = hiltViewModel<ChatListViewModel>()

    var room by rememberSaveable {
        mutableStateOf("")
    }

    var message by rememberSaveable {
        mutableStateOf("Hello Message")
    }

    Column {


        OutlinedTextField(value = room, onValueChange = { room = it })
        OutlinedTextField(value = message, onValueChange = { message = it })

        Button(onClick = {
            viewModel.sendMessage(room, message)
        }) {
            Text(text = "Send Message")
        }

    }
}


@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val socketService: ChatSocketService
) : ViewModel() {

    companion object {
        private const val TAG = "ChatListViewModel"
    }
    
    private val messageFlow = socketService.messagesFlow

    init {
        viewModelScope.launch { 
            messageFlow.collect{message->
                Log.d(TAG, "NewMessage: $message")
            }
        }
    }


    fun sendMessage(chatRoomId: String, message: String) {
        viewModelScope.launch {
            socketService.sendMessage(ChatMessage(chatRoomId, message))
        }
    }
}