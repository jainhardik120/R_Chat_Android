package com.jainhardik120.rchat.ui.presentation.screens.home.chatmessages

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jainhardik120.rchat.data.KeyValueStorage
import com.jainhardik120.rchat.data.remote.ChatSocketService
import com.jainhardik120.rchat.data.remote.RChatApi
import com.jainhardik120.rchat.data.remote.dto.ChatMessage
import com.jainhardik120.rchat.data.remote.dto.MessageDto
import com.jainhardik120.rchat.ui.BaseViewModel
import com.jainhardik120.rchat.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatMessagesViewModel @Inject constructor(
    private val socketService: ChatSocketService,
    private val api: RChatApi,
    keyValueStorage: KeyValueStorage,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private var _state = mutableStateOf(ChatMessagesState())
    val state: State<ChatMessagesState> = _state

    private var chatRoomId: String? = null

    init {
        keyValueStorage.getValue(KeyValueStorage.USER_ID_KEY)?.let { selfId ->
            _state.value = _state.value.copy(selfId = selfId)
        }
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

    fun sendMessage(message: String) {
        chatRoomId?.let { id ->
            viewModelScope.launch {
                socketService.sendMessage(ChatMessage(id, message))
            }
        }
    }

    override fun onTextException(message: String) {
        sendUiEvent(UiEvent.ShowToast(message))
    }

    private fun refreshMessages() {
        chatRoomId?.let {
            makeApiCall({
                api.chatMessages(it)
            }) {
                _state.value = _state.value.copy(messages = it.messages)
            }
        }
    }

    private fun handleNewMessage(message: MessageDto) {
        val prevMessages = _state.value.messages.toMutableList()
        prevMessages.add(0, message)
        _state.value = _state.value.copy(messages = prevMessages)
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