package com.mahmoudhamdyae.mhchat.domain.models

import androidx.annotation.Keep
import java.util.UUID

@Keep
data class Chat(
    val messages: MutableList<Message> = mutableListOf(),
    val chatId: String = UUID.randomUUID().toString(),
)