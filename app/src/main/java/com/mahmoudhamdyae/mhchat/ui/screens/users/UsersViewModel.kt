package com.mahmoudhamdyae.mhchat.ui.screens.users

import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.messages.MessagesDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    accountService: AccountService,
    private val chatDatabaseService: ChatDatabaseService,
    databaseService: UsersDatabaseService,
    logService: LogService
): ChatViewModel(logService) {

    private val currentUserId = accountService.currentUserId

    val users = databaseService.users.map {
        it.filter { user ->
            user.userId != currentUserId
        }
    }

    fun onItemClick(user: User, navigateTo: (String) -> Unit) {
        navigateTo("${MessagesDestination.route}/${user.userId}/${getChatId(user)}")
    }

    private fun getChatId(user: User): String {
        val chatId = user.chats?.firstOrNull() {
            it.toUserId == currentUserId
        }?.chatId

        return if (chatId == null) {
            val newChatId = UUID.randomUUID().toString()
            createChat(user.userId, newChatId)
            newChatId
        } else {
            chatId
        }
    }

    private fun createChat(toUserId: String, chatId: String) {
        launchCatching {
            chatDatabaseService.createChat(toUserId, chatId)
        }
    }
}