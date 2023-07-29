package com.jainhardik120.rchat.ui.presentation.screens.login

sealed class LoginEvent {
    data class LoginEmailChanged(val email: String) : LoginEvent()
    data class LoginPasswordChanged(val password: String) : LoginEvent()
    data class RegisterMailChanged(val email: String) : LoginEvent()
    data class RegisterPasswordChanged(val password: String) : LoginEvent()
    data class RegisterUsernameChanged(val username: String) : LoginEvent()
    object LoginButtonClicked : LoginEvent()
    object RegisterButtonClicked : LoginEvent()
}