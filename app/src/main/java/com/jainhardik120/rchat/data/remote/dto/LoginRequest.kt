package com.jainhardik120.rchat.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    val email : String,
    val password : String
)


