package com.jainhardik120.rchat.data.remote

import com.jainhardik120.rchat.Result
import com.jainhardik120.rchat.data.remote.dto.ChatRoom
import com.jainhardik120.rchat.data.remote.dto.GroupInfo
import com.jainhardik120.rchat.data.remote.dto.MessageError
import com.jainhardik120.rchat.data.remote.dto.LoginRequest
import com.jainhardik120.rchat.data.remote.dto.LoginResponse
import com.jainhardik120.rchat.data.remote.dto.Message
import com.jainhardik120.rchat.data.remote.dto.SignupRequest

interface RChatApi {
    fun saveLoginResponse(loginResponse: LoginResponse)

    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse, MessageError>
    suspend fun signup(signupRequest: SignupRequest): Result<LoginResponse, MessageError>
    suspend fun chatRooms(): Result<List<ChatRoom>, MessageError>
    suspend fun chatMessages(chatRoomId: String): Result<List<Message>, MessageError>
    suspend fun createGroup(groupName: String): Result<GroupInfo, MessageError>
    suspend fun addUser(groupId: String, vararg users: String): Result<GroupInfo, MessageError>
    suspend fun removeUser(groupId: String, vararg users: String): Result<GroupInfo, MessageError>
    suspend fun leaveGroup(groupId: String): Result<GroupInfo, MessageError>
    suspend fun getDirectChat(userId: String): Result<List<Message>, MessageError>
}