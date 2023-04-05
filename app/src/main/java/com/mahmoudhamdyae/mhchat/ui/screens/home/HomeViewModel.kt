package com.mahmoudhamdyae.mhchat.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.login.LogInDestination
import com.mahmoudhamdyae.mhchat.ui.screens.messages.MessagesDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val accountService: AccountService,
    private val databaseService: UsersDatabaseService,
    private val chatDatabaseService: ChatDatabaseService,
    logService: LogService
): ChatViewModel(logService) {

    var uiState = mutableStateOf(HomeUiState())
        private set

    fun initialize(navigate: (String) -> Unit) {
        if (!accountService.hasUser) {
            navigate(LogInDestination.route)
        } else {
            getChats()
        }
    }

    private fun getChats() {
        launchCatching {
            databaseService.getCurrentUser().collect {
                it?.chats?.forEach { userChat ->
                    // Get Users
                    databaseService.getUser(userChat.toUserId).collect { user ->
                        uiState.value.users?.add(user)
                    }
                    // Get Chats
                    chatDatabaseService.getLastMessage(userChat.chatId).collect { message ->
                        uiState.value.lastMessages?.add(message)
                    }
                }
            }
        }
    }

    fun onSignOut(navigate: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            navigate(LogInDestination.route)
        }
    }

    fun onItemClick(user: User, navigateTo: (String) -> Unit) {
        val chatId = "fde18e5a-bf96-4932-b050-f5aa0671133d"
        navigateTo("${MessagesDestination.route}/${user.userId}")
    }
}