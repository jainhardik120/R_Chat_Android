package com.jainhardik120.rchat.ui.presentation.screens.login

data class LoginState(
    val loginEmail: String = "",
    val loginPassword: String = "",
    val registerMail: String = "",
    val registerPassword: String = "",
    val registerUsername: String = "",
    val loading: Boolean = false
)