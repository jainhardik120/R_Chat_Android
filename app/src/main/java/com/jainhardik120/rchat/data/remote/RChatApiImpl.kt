package com.jainhardik120.rchat.data.remote

import com.jainhardik120.rchat.Result
import com.jainhardik120.rchat.data.KeyValueStorage
import com.jainhardik120.rchat.data.remote.dto.ChatRoom
import com.jainhardik120.rchat.data.remote.dto.GroupInfo
import com.jainhardik120.rchat.data.remote.dto.LoginRequest
import com.jainhardik120.rchat.data.remote.dto.LoginResponse
import com.jainhardik120.rchat.data.remote.dto.MessageDto
import com.jainhardik120.rchat.data.remote.dto.MessageError
import com.jainhardik120.rchat.data.remote.dto.SignupRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class RChatApiImpl(
    private val client: HttpClient,
    private val keyValueStorage: KeyValueStorage
) : RChatApi {

    override fun saveLoginResponse(loginResponse: LoginResponse) {
        keyValueStorage.saveLoginResponse(loginResponse)
    }

    private suspend inline fun <T, reified R> performApiRequest(
        call: () -> T
    ): Result<T, R> {
        return try {
            val response = call.invoke()
            Result.Success(response)
        } catch (e: ClientRequestException) {
            Result.ClientException(e.response.body<R>(), e.response.status)
        } catch (e: Exception) {
            Result.Exception(e.message)
        }
    }

    private fun HttpRequestBuilder.tokenAuthHeaders(headers: HeadersBuilder.() -> Unit = {}) {
        headers {
            bearerAuth(token = keyValueStorage.getToken() ?: return@headers)
            headers()
        }
    }

    private suspend inline fun <reified T, reified R> requestBuilder(
        url: String,
        method: HttpMethod,
        body: T
    ): R {
        return client.request(url) {
            this.method = method
            contentType(ContentType.Application.Json)
            setBody(body)
            tokenAuthHeaders()
        }.body()
    }

    private suspend inline fun <reified T> requestBuilder(
        url: String,
        method: HttpMethod
    ): T {
        return client.request(url) {
            this.method = method
            tokenAuthHeaders()
        }.body()
    }

    override suspend fun login(loginRequest: LoginRequest): Result<LoginResponse, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.LOGIN, HttpMethod.Post, loginRequest)
        }
    }

    override suspend fun signup(signupRequest: SignupRequest): Result<LoginResponse, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.SIGNUP, HttpMethod.Post, signupRequest)
        }
    }


    override suspend fun chatRooms(): Result<List<ChatRoom>, MessageError> {
        return performApiRequest { requestBuilder(APIRoutes.CHAT_ROOMS, HttpMethod.Get) }
    }


    override suspend fun chatMessages(chatRoomId: String): Result<List<MessageDto>, MessageError> {
        return performApiRequest {
            requestBuilder(
                APIRoutes.chatHistory(chatRoomId),
                HttpMethod.Get
            )
        }
    }

    override suspend fun createGroup(groupName: String): Result<GroupInfo, MessageError> {
        return performApiRequest {
            requestBuilder(
                APIRoutes.CREATE_GROUP,
                HttpMethod.Post,
                buildJsonObject {
                    put("groupName", groupName)
                }
            )
        }
    }

    override suspend fun addUser(
        groupId: String,
        vararg users: String
    ): Result<GroupInfo, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.addToGroup(groupId), HttpMethod.Put, buildJsonObject {
                put("userIdsToAdd", buildJsonArray {
                    users.forEach { user ->
                        this.add(user)
                    }
                })
            })
        }
    }

    override suspend fun removeUser(
        groupId: String,
        vararg users: String
    ): Result<GroupInfo, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.removeFromGroup(groupId), HttpMethod.Put, buildJsonObject {
                put("userIdsToRemove", buildJsonArray {
                    users.forEach { user ->
                        this.add(user)
                    }
                })
            })
        }
    }

    override suspend fun leaveGroup(groupId: String): Result<GroupInfo, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.leaveGroup(groupId), HttpMethod.Put)
        }
    }

    override suspend fun getDirectChat(userId: String): Result<List<MessageDto>, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.directChat(userId), HttpMethod.Get)
        }
    }

    override suspend fun getRoomDetails(roomId: String): Result<ChatRoom, MessageError> {
        return performApiRequest {
            requestBuilder(APIRoutes.chatRoom(roomId), HttpMethod.Get)
        }
    }
}