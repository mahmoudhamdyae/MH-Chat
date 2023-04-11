package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.domain.models.Chat
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import kotlinx.coroutines.flow.Flow

class GetMessagesUseCase (
    private val chatDatabaseService: ChatDatabaseService,
) {

    operator fun invoke(chatId: String): Flow<Chat?> {
        return chatDatabaseService.chat(chatId)
    }
}