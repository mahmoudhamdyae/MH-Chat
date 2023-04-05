package com.mahmoudhamdyae.mhchat.domain.services

import com.mahmoudhamdyae.mhchat.domain.models.Chat
import com.mahmoudhamdyae.mhchat.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface ChatDatabaseService {

    val chat: (String) -> Flow<Chat?>

    suspend fun getLastMessage(chatId: String): Flow<Message?>
    suspend fun createChat(toUserId: String, chatId: String)
    suspend fun updateChat(chatId: String, toUserId: String, messageBody: String)
}