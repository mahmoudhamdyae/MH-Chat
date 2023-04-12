package com.mahmoudhamdyae.mhchat.domain.models

data class User(
    val userId: String = "",
    val email: String = "",
    val userName: String = "",
    val imageUrl: String? = null,
    val token: String = "",
    val chats: List<UserChat>? = null
)

data class UserChat(
    val toUserId: String = "",
    val chatId: String = "",
)
