package com.mahmoudhamdyae.mhchat.ui.screens.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.snackbar.SnackBarManager
import com.mahmoudhamdyae.mhchat.domain.models.Chat
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class MessagesViewModel @AssistedInject constructor(
    private val chatDatabaseService: ChatDatabaseService,
    @Assisted("chatId") private val chatId: String,
    @Assisted("toUserId") private val toUserId: String,
    logService: LogService
): ChatViewModel(logService) {

    var chat: Flow<Chat?> = chatDatabaseService.chat(chatId)

    fun onMessageSend(messageBody: String) {
        if (validateMessageBody(messageBody)) {
            launchCatching {
                chatDatabaseService.updateChat(
                    chatId = chatId,
                    toUserId = toUserId,
                    messageBody = messageBody
                )
            }
        }
    }

    private fun validateMessageBody(messageBody: String): Boolean {
        return if (messageBody.isNotEmpty()) {
            true
        } else {
            SnackBarManager.showMessage(R.string.message_empty)
            false
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("chatId") chatId: String,
            @Assisted("toUserId") toUserId: String,
        ): MessagesViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            chatId: String,
            toUserId: String,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(chatId, toUserId) as T
            }
        }
    }
}