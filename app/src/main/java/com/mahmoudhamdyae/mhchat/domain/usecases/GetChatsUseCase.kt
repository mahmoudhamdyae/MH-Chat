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
        usersDatabaseService.userChats.collect { userChats ->
            val usersIds: MutableList<String?> = mutableListOf()
            val chatsIds: MutableList<String?> = mutableListOf()
            userChats?.forEach {
                usersIds.add(it?.toUserId)
                chatsIds.add(it?.chatId)
            }
            if (usersIds.isNotEmpty()) {
                usersDatabaseService.specificUsers(usersIds).collect { users ->
                    chatDatabaseService.lastMessages(chatsIds).collect { messages ->
                        val ret: MutableList<Pair<User?, Message?>> = mutableListOf()
                        users.forEach {  user ->
                            val returnedMessage = messages.find { message ->
                                message?.fromUserId == user.userId || message?.toUserId == user.userId
                            }
                            ret.add(Pair(user, returnedMessage))
                        }
                        result(ret)
                    }
                }
            }
        }
    }
}