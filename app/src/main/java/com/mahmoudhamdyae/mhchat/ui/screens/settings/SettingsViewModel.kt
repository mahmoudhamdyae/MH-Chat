package com.mahmoudhamdyae.mhchat.ui.screens.settings

import androidx.lifecycle.viewModelScope
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.snackbar.SnackBarManager
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.auth.login.LogInDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountService: AccountService,
    private val databaseService: UsersDatabaseService,
    private val chatDatabaseService: ChatDatabaseService,
    logService: LogService
): ChatViewModel(logService) {

    private lateinit var currentUserEmail: String

    fun initialize() {
        viewModelScope.launch {
            currentUserEmail = accountService.currentUserEmail
        }
    }

    fun onDeleteAccount(password: String, navigate: (String) -> Unit) {
        launchCatching {
            if (validatePassword(password)) {
                databaseService.getCurrentUser().collect {
                    val userChats = it?.chats
                    userChats?.forEach {  userChat ->
                        chatDatabaseService.delChat(userChat.chatId)
                    }
                }

                accountService.deleteAccount(currentUserEmail, password)
                navigate(LogInDestination.route)
            }
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