package com.jainhardik120.rchat.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedMessageResponse(
    val messages: List<MessageDto>,
    val page: Int,
    val totalMessages: Int,
    val totalPages: Int
)