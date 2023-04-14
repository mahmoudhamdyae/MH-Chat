package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.snackbar.SnackBarManager
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService

class SendMessageUseCase (
    private val chatDatabaseService: ChatDatabaseService
) {

    suspend operator fun invoke(chatId: String, messageBody: String, toUserId: String) {
        if (validateMessageBody(messageBody)) {
            chatDatabaseService.updateChat(chatId, messageBody, toUserId)
        }
    }

    private suspend fun validateMessageBody(messageBody: String): Boolean {
        return if (messageBody.isNotEmpty()) {
            true
        } else {
            SnackBarManager.showMessage(R.string.message_empty)
            false
        }
    }
}