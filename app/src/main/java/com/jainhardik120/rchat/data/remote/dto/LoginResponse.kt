package com.jainhardik120.rchat.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token : String,
    val userId : String,
    val email : String
)


