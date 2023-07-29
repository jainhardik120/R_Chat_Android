package com.jainhardik120.rchat.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    @SerialName("chatRoomId")
    val roomId : String,
    @SerialName("message")
    val message : String
)
