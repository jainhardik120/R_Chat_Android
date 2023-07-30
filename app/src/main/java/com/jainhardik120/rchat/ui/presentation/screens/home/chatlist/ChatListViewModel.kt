package com.jainhardik120.rchat.ui.presentation.screens.home.chatlist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.jainhardik120.rchat.data.remote.ChatSocketService
import com.jainhardik120.rchat.data.remote.RChatApi
import com.jainhardik120.rchat.data.remote.dto.ChatRoom
import com.jainhardik120.rchat.data.remote.dto.MessageDto
import com.jainhardik120.rchat.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val socketService: ChatSocketService,
    private val api: RChatApi
) : BaseViewModel() {

    companion object {
        private const val TAG = "ChatListViewModel"
    }

    private val _state = mutableStateOf(ChatListState())
    val state: State<ChatListState> = _state

    private val chatIdMap: MutableMap<String, Int> = hashMapOf()

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
                    chatroomName = "Loading...",
                    type = "",
                    lastMessage = message
                )
            ) + _state.value.chatRooms
            constructMap(updatedList)
            _state.value = _state.value.copy(
                chatRooms = updatedList
            )
            loadAndUpdateSingleRoom(chatRoom)
        }
    }

    private fun constructMap(chatRooms: List<ChatRoom>) {
        chatIdMap.clear()
        chatRooms.forEachIndexed { index, chatRoom ->
            chatIdMap[chatRoom._id] = index
        }
    }

    private fun checkSocketState() {
        viewModelScope.launch {
            socketService.checkAndReload()
        }
    }

    private fun loadAndUpdateSingleRoom(roomId: String) {
        makeApiCall({
            api.getRoomDetails(roomId)
        }) { room ->
            chatIdMap[roomId]?.let { index ->
                val list = _state.value.chatRooms.toMutableList()
                if (list[index]._id == room._id) {
                    list[index] = room
                    _state.value = _state.value.copy(chatRooms = list)
                    constructMap(list)
                }
            }
        }
    }

    fun loadList() {
        makeApiCall(
            call = {
                api.chatRooms()
            },
            preExecuting = { _state.value = _state.value.copy(isLoading = true) },
            onDoneExecuting = {
                _state.value = _state.value.copy(isLoading = false)
                checkSocketState()
            }) {
            constructMap(it)
            _state.value = _state.value.copy(chatRooms = it)
        }
    }

}