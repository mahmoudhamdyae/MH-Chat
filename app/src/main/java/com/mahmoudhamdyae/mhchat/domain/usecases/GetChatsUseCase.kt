package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.domain.models.Message
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService

class GetChatsUseCase (
    private val usersDatabaseService: UsersDatabaseService,
    private val chatDatabaseService: ChatDatabaseService,
) {

    suspend operator fun invoke(result: (List<Pair<User?, Message?>>) -> Unit) {
        var ret: List<Pair<User?, Message?>> = listOf()
        usersDatabaseService.userChats.collect { userChats ->
            userChats?.forEach { userChat ->
                if (userChat != null) {
                    // Get User
                    usersDatabaseService.getUser(userChat.toUserId).collect { user ->
                        // Get Last Messages
                        chatDatabaseService.lastMessage(userChat.chatId).collect { message ->
                            if (ret.none { it.first?.userId != user?.userId }) {
                                // Add pair to ret
                                ret = ret + Pair(user, message)
                                result(ret)
                            }
                        }
                    }
                }
            }
        }
    }
}