package com.mahmoudhamdyae.mhchat.domain.services

import com.mahmoudhamdyae.mhchat.domain.models.Chat
import com.mahmoudhamdyae.mhchat.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface ChatDatabaseService {

    val chat: (String) -> Flow<Chat?>
    val lastMessages: (List<String?>) -> Flow<List<Message?>>

    suspend fun createChat(toUserId: String, chatId: String)
    suspend fun updateChat(chatId: String, messageBody: String, toUserId: String)
    suspend fun delChat(chatId: String)
}