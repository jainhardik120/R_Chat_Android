package com.jainhardik120.rchat.ui.presentation.screens.home.chatlist

import com.jainhardik120.rchat.data.remote.dto.ChatRoom

data class ChatListState(
    val chatRooms: List<ChatRoom> = emptyList(),
    val isLoading: Boolean = false
)