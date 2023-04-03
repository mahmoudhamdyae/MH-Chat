package com.mahmoudhamdyae.mhchat.domain.models

import java.util.*

data class Chat(
    val messages: MutableList<Message> = mutableListOf(),
    val chatId: String = UUID.randomUUID().toString(),
)