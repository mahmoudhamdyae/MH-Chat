package com.mahmoudhamdyae.mhchat.ui.screens.messages

import androidx.compose.runtime.mutableStateOf
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.snackbar.SnackBarManager
import com.mahmoudhamdyae.mhchat.domain.models.Chat
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val usersDatabaseService: UsersDatabaseService,
    private val chatDatabaseService: ChatDatabaseService,
    logService: LogService
): ChatViewModel(logService) {

    var uiState = mutableStateOf(MessagesUiState())
        private set
    var chat: Flow<Chat?> = chatDatabaseService.chat(uiState.value.chatId)

    fun createChat(toUserId: String) {
        launchCatching {
            // Returns null if it is first time
            val chatIdOrNull = usersDatabaseService.getCurrentUser().map { user ->
                user?.chats?.firstOrNull { userChat ->
                    userChat.toUserId == toUserId
                }?.chatId
            }.firstOrNull()
            if (!chatIdOrNull.isNullOrEmpty()) { // Not null
                // Not first time to chat between these two users
                uiState.value.chatId = chatIdOrNull
            } else { // Null
                // First time to chat between these two users
                chatDatabaseService.createChat(toUserId, uiState.value.chatId)
            }
        }
    }

    fun onMessageSend(messageBody: String, toUserId: String) {
        if (validateMessageBody(messageBody)) {
            launchCatching {
                chatDatabaseService.updateChat(
                    chatId = uiState.value.chatId,
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
}