package com.jainhardik120.rchat.data.remote

object APIRoutes {
    private const val BASE_URL = "https://rchat-server.onrender.com/api"

    private const val USER_ROUTE = "$BASE_URL/user"
    const val LOGIN = "$USER_ROUTE/login"
    const val SIGNUP = "$USER_ROUTE/signup"

    private const val MESSAGE_ROUTE = "$BASE_URL/message"
    const val CHAT_ROOMS = "$MESSAGE_ROUTE/chat-rooms"
    fun DIRECT_CHAT(userId: String): String = "$MESSAGE_ROUTE/direct-chat/$userId"
    fun CHAT_HISTORY(chatRoomId: String): String = "$MESSAGE_ROUTE/chat-history/$chatRoomId"
    fun ADD_TO_GROUP(groupId: String): String = "$MESSAGE_ROUTE/add-user-to-group/$groupId"
    fun REMOVE_FROM_GROUP(groupId: String): String =
        "$MESSAGE_ROUTE/remove-users-from-group/$groupId"

    fun LEAVE_GROUP(groupId: String): String = "$MESSAGE_ROUTE/leave-group/$groupId"
    const val CREATE_GROUP = "$MESSAGE_ROUTE/create-group"
}