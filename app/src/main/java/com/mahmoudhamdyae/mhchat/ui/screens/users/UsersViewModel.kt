package com.mahmoudhamdyae.mhchat.ui.screens.users

import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.domain.usecases.BaseUseCase
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.messages.MessagesDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val chatDatabaseService: ChatDatabaseService,
    private val usersDatabaseService: UsersDatabaseService,
    useCase: BaseUseCase,
    logService: LogService
): ChatViewModel(logService) {

    val users = useCase.getUsersUseCase()

    fun onItemClick(user: User, navigateTo: (String) -> Unit) {
        getChatId(user) { chatId ->
            navigateTo("${MessagesDestination.route}/${chatId}/${user.imageUrl}/${user.userName}")
        }
    }

    private fun getChatId(user: User, navigate: (String) -> Unit) {
        launchCatching {
            usersDatabaseService.userChats.collect { userChats ->
                userChats?.forEach { userChat ->
                    if (userChat != null) {
                        if (userChat.toUserId == user.userId) {
                            navigate(userChat.chatId)
                            return@collect
                        }
                    }
                }
                createNewId(user.userId, navigate)
            }
        }
    }

    private fun createNewId(toUserId: String, navigate: (String) -> Unit): String {
        val newChatId = UUID.randomUUID().toString()
        createChat(toUserId, newChatId)
        navigate(newChatId)
        return newChatId
    }

    private fun createChat(toUserId: String, chatId: String) {
        launchCatching {
            chatDatabaseService.createChat(toUserId, chatId)
        }
    }
}