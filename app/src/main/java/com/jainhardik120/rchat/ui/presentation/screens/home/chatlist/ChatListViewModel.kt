package com.jainhardik120.rchat.ui.presentation.screens.home.chatlist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainhardik120.rchat.Result
import com.jainhardik120.rchat.data.remote.ChatSocketService
import com.jainhardik120.rchat.data.remote.RChatApi
import com.jainhardik120.rchat.data.remote.dto.ChatRoom
import com.jainhardik120.rchat.data.remote.dto.MessageDto
import com.jainhardik120.rchat.data.remote.dto.MessageError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val socketService: ChatSocketService,
    private val api: RChatApi
) : ViewModel() {

    companion object {
        private const val TAG = "ChatListViewModel"
    }

    private val _state = mutableStateOf(ChatListState())
    val state: State<ChatListState> = _state

    private val chatIdMap: MutableMap<String, Int> = hashMapOf()

    private fun <T, R> makeApiCall(
        call: suspend () -> Result<T, R>,
        preExecuting: (() -> Unit)? = {

        },
        onDoneExecuting: (() -> Unit)? = {

        },
        onException: (String) -> Unit = { errorMessage ->
            Log.d(TAG, "makeApiCall: $errorMessage")
        },
        onError: (R) -> Unit = { errorBody ->
            if (errorBody is MessageError) {
                onException(errorBody.error)
            }
        },
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            preExecuting?.invoke()
            val result = call.invoke()
            onDoneExecuting?.invoke()
            when (result) {
                is Result.ClientException -> {
                    result.errorBody?.let(onError)
                }

                is Result.Exception -> {
                    result.errorMessage?.let(onException)
                }

                is Result.Success -> {
                    result.data?.let(onSuccess)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            socketService.messagesFlow.collect {
                Log.d(TAG, "NewMessage: $it")
                handleNewMessage(it)
            }
        }
        loadList()
    }

    private fun handleNewMessage(message: MessageDto) {
        val chatRoom = message.chatRoomId
        if (chatIdMap.containsKey(chatRoom)) {
            chatIdMap[chatRoom]?.let { index ->
                val updatedList = _state.value.chatRooms.toMutableList()
                val newItem = updatedList[index].copy(lastMessage = message)
                if (index != 0) {
                    updatedList.removeAt(index)
                    updatedList.add(0, newItem)
                    constructMap(updatedList)
                } else {
                    updatedList[index] = newItem
                }
                _state.value = _state.value.copy(chatRooms = updatedList)
            }
        } else {
            val updatedList = listOf(
                ChatRoom(
                    _id = chatRoom,
                    chatroomName = "New Chat",
                    type = "",
                    lastMessage = message
                )
            ) + _state.value.chatRooms
            constructMap(updatedList)
            _state.value = _state.value.copy(
                chatRooms = updatedList
            )
        }
    }

    private fun constructMap(chatRooms: List<ChatRoom>) {
        chatIdMap.clear()
        chatRooms.forEachIndexed { index, chatRoom ->
            chatIdMap[chatRoom._id] = index
        }
    }

    fun loadList() {
        makeApiCall(call = {
            api.chatRooms()
        }) {
            constructMap(it)
            _state.value = _state.value.copy(chatRooms = it)
        }
    }
}