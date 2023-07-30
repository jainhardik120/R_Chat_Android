package com.jainhardik120.rchat.data.remote

import com.jainhardik120.rchat.data.KeyValueStorage
import com.jainhardik120.rchat.data.remote.dto.ChatMessage
import com.jainhardik120.rchat.data.remote.dto.MessageDto
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChatSocketServiceImpl(
    private val client: HttpClient,
    private val keyValueStorage: KeyValueStorage
) : ChatSocketService {

    private var socket: WebSocketSession? = null
    private val _messagesFlow = MutableSharedFlow<MessageDto>()

    override val messagesFlow: SharedFlow<MessageDto>
        get() = _messagesFlow


    override suspend fun init() {
        keyValueStorage.getToken()?.let { token ->
            try {
                socket = client.webSocketSession {
                    url(APIRoutes.chatSocket(token))
                }
                socket?.incoming?.receiveAsFlow()?.filter { it is Frame.Text }?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val messageDto = Json.decodeFromString<MessageDto>(json)
                    messageDto
                }?.collect { message ->
                    _messagesFlow.emit(message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override suspend fun sendMessage(message: ChatMessage) {
        try {
            socket?.send(Frame.Text(Json.encodeToString(message)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun isActive(): Boolean = (socket?.isActive == true)

    override suspend fun close() {
        socket?.close()
    }

    override suspend fun checkAndReload() {
        try {
            if (!isActive()) {
                init()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}