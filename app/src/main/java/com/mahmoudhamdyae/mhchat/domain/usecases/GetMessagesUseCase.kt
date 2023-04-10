package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.domain.models.Chat
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val chatDatabaseService: ChatDatabaseService,
) {

    operator fun invoke(chatId: String): Flow<Chat?> {
        return chatDatabaseService.chat(chatId)
    }
}