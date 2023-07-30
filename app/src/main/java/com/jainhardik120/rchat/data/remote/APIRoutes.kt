package com.jainhardik120.rchat.data.remote

object APIRoutes {
    private const val HOST = "rchat-server.onrender.com"
    private const val BASE_URL = "https://$HOST/api"

    private const val USER_ROUTE = "$BASE_URL/user"
    const val LOGIN = "$USER_ROUTE/login"
    const val SIGNUP = "$USER_ROUTE/signup"

    private const val MESSAGE_ROUTE = "$BASE_URL/message"
    const val CHAT_ROOMS = "$MESSAGE_ROUTE/chat-rooms"
    fun chatRoom(roomId: String): String = "$MESSAGE_ROUTE/chat-rooms/$roomId"
    fun directChat(userId: String): String = "$MESSAGE_ROUTE/direct-chat/$userId"
    fun chatHistory(chatRoomId: String, page: Int = 1, limit: Int = 10): String =
        "$MESSAGE_ROUTE/chat-history/$chatRoomId?page=$page&limit=$limit"

    fun addToGroup(groupId: String): String = "$MESSAGE_ROUTE/add-user-to-group/$groupId"
    fun removeFromGroup(groupId: String): String =
        "$MESSAGE_ROUTE/remove-users-from-group/$groupId"

    fun leaveGroup(groupId: String): String = "$MESSAGE_ROUTE/leave-group/$groupId"
    const val CREATE_GROUP = "$MESSAGE_ROUTE/create-group"

    fun chatSocket(token : String) : String = "wss://$HOST?token=$token"
}