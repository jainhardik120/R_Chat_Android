package com.jainhardik120.rchat.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SenderId(
    val _id: String,
    val username: String
)