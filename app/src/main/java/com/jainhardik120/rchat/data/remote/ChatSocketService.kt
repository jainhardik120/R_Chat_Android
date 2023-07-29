package com.jainhardik120.rchat.data.remote

import com.jainhardik120.rchat.data.remote.dto.ChatMessage
import com.jainhardik120.rchat.data.remote.dto.MessageDto
import kotlinx.coroutines.flow.SharedFlow

interface ChatSocketService {

    val messagesFlow: SharedFlow<MessageDto>

    suspend fun init()
    suspend fun isActive(): Boolean
    suspend fun close()

    suspend fun sendMessage(message: ChatMessage)
}

