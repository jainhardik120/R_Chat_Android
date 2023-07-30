package com.jainhardik120.rchat.ui.presentation.screens.home.chatmessages

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jainhardik120.rchat.data.remote.ChatSocketService
import com.jainhardik120.rchat.data.remote.RChatApi
import com.jainhardik120.rchat.data.remote.dto.ChatMessage
import com.jainhardik120.rchat.data.remote.dto.MessageDto
import com.jainhardik120.rchat.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatMessagesViewModel @Inject constructor(
    private val socketService: ChatSocketService,
    private val api: RChatApi,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private var _state = mutableStateOf(ChatMessagesState())
    val state: State<ChatMessagesState> = _state


    private var chatRoomId: String? = null

    init {
        chatRoomId = savedStateHandle["chatId"]
        viewModelScope.launch {
            socketService.messagesFlow.collect { message ->
                if (message.chatRoomId == chatRoomId) {
                    handleNewMessage(message)
                }
            }
        }
        refreshMessages()
    }

    private fun sendMessage(message: String) {
        chatRoomId?.let { id ->
            viewModelScope.launch {
                socketService.sendMessage(ChatMessage(id, message))
            }
        }
    }

    private fun refreshMessages() {
        chatRoomId?.let {
            makeApiCall({
                api.chatMessages(it)
            }) {
                _state.value = _state.value.copy(messages = it)
            }
        }
    }

    private fun handleNewMessage(message: MessageDto) {
        _state.value = _state.value.copy(messages = _state.value.messages + message)
    }

    fun onEvent(event: ChatMessageEvent) {
        when (event) {
            is ChatMessageEvent.NewMessageBodyChanged -> {
                _state.value = _state.value.copy(newMessageBody = event.newMessage)
            }

            ChatMessageEvent.SendButtonClicked -> {
                sendMessage(_state.value.newMessageBody)
                _state.value = _state.value.copy(newMessageBody = "")
            }
        }
    }

}