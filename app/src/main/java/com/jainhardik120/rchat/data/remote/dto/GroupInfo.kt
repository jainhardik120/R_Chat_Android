package com.jainhardik120.rchat.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GroupInfo(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val creator: String,
    val members: List<String>,
    val name: String,
    val type: String,
    val updatedAt: String
)