package com.jainhardik120.rchat.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val email : String,
    val password : String,
    val username : String
)