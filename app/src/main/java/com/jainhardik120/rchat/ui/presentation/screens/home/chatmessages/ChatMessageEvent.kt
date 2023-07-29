package com.jainhardik120.rchat.ui.presentation.screens.home.chatmessages

sealed class ChatMessageEvent {
    data class NewMessageBodyChanged(val newMessage: String) : ChatMessageEvent()
    object SendButtonClicked : ChatMessageEvent()
}