package com.mahmoudhamdyae.mhchat.domain.usecases

import android.util.Log
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
            Log.i("usecase", userChats.toString())
            val usersIds: MutableList<String?> = mutableListOf()
            val chatsIds: MutableList<String?> = mutableListOf()
            userChats?.forEach {
                usersIds.add(it?.toUserId)
                chatsIds.add(it?.chatId)
            }
            Log.i("usecase usersIds", usersIds.toString())
            Log.i("usecase chatsIds", chatsIds.toString())
            if (usersIds.isNotEmpty()) {
                usersDatabaseService.specificUsers(usersIds).collect { users ->
                    chatDatabaseService.lastMessages(chatsIds).collect { messages ->
                        Log.i("usecase users", users.toString())
                        Log.i("usecase messages", messages.toString())
                        val ret: MutableList<Pair<User?, Message?>> = mutableListOf()
                        users.forEach {  user ->
                            val returnedMessage = messages.find { message ->
                                message?.fromUserId == user.userId || message?.toUserId == user.userId
                            }
                            ret.add(Pair(user, returnedMessage))
                        }
                        Log.i("usecase ret" , ret.toString())
                        result(ret)
                    }
                }
            }
        }
    }
}