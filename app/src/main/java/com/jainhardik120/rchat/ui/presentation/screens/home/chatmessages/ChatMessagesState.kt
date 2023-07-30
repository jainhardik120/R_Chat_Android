package com.jainhardik120.rchat.ui.presentation.screens.home.chatmessages

import com.jainhardik120.rchat.data.remote.dto.MessageDto

data class ChatMessagesState(
    val messages: List<MessageDto> = emptyList(),
    val newMessageBody: String = "",
    val selfId: String = ""
)