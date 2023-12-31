package com.jainhardik120.rchat.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatRoom(
    val _id: String,
    val chatroomName: String,
    val lastMessage: MessageDto? = null,
    val type: String
)