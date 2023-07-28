package com.jainhardik120.rchat.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val __v: Int,
    val _id: String,
    val chatRoomId: String,
    val content: String,
    val createdAt: String,
    val senderId: SenderId,
    val updatedAt: String
)