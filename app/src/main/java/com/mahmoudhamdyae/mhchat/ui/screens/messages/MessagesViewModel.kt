package com.mahmoudhamdyae.mhchat.ui.screens.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mahmoudhamdyae.mhchat.domain.models.Chat
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.usecases.GetMessagesUseCase
import com.mahmoudhamdyae.mhchat.domain.usecases.SendMessageUseCase
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class MessagesViewModel @AssistedInject constructor(
    getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    @Assisted("chatId") private val chatId: String,
    logService: LogService
): ChatViewModel(logService) {

    var chat: Flow<Chat?> = getMessagesUseCase(chatId)

    fun onMessageSend(messageBody: String) {
        launchCatching {
            sendMessageUseCase(chatId, messageBody)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("chatId") chatId: String,
        ): MessagesViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            chatId: String,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(chatId) as T
            }
        }
    }
}