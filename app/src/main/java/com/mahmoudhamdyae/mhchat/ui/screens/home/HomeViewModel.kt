package com.mahmoudhamdyae.mhchat.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.models.UserChat
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
        if (accountService.currentUserId.isEmpty()) {
            navigate(LogInDestination.route)
        } else {
            getChats()
        }
    }

    private fun getChats() {
        launchCatching {
            var currentUserChats: List<UserChat>? = null
            databaseService.getCurrentUser().collect {
                currentUserChats = it?.chats
            }

            currentUserChats?.forEach { userChat ->
                // Get Users
                databaseService.getUser(userChat.toUserId).collect {
                    uiState.value.users?.add(it)
                }
                // Get Chats
                chatDatabaseService.getLastMessage(userChat.chatId).collect {
                    uiState.value.lastMessages?.add(it)
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