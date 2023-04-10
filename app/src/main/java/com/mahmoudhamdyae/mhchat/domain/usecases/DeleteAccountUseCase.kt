package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.snackbar.SnackBarManager
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.ui.screens.auth.login.LogInDestination
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val accountService: AccountService,
    private val usersDatabaseService: UsersDatabaseService,
    private val chatDatabaseService: ChatDatabaseService,
) {

    suspend operator fun invoke(password: String, navigate: (String) -> Unit) {
        if (validatePassword(password)) {
            usersDatabaseService.getCurrentUser().collect {
                val userChats = it?.chats
                userChats?.forEach {  userChat ->
                    chatDatabaseService.delChat(userChat.chatId)
                }
            }
            accountService.deleteAccount(accountService.currentUserEmail, password)
            navigate(LogInDestination.route)
        }
    }

    private suspend fun validatePassword(password: String): Boolean {
        return if (password.isBlank()) {
            SnackBarManager.showMessage(R.string.empty_password_error)
            false
        } else {
            true
        }
    }
}