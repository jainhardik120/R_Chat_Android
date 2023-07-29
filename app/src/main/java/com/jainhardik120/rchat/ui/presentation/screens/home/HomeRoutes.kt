package com.jainhardik120.rchat.ui.presentation.screens.home

sealed class HomeRoutes(val route: String) {
    object ChatList : HomeRoutes("chat_list")
    object ChatMessages : HomeRoutes("chat_message")


    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}