package com.mahmoudhamdyae.mhchat.domain.usecases

import android.util.Log
import com.mahmoudhamdyae.mhchat.domain.models.Message
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class GetChatsUseCase (
    private val usersDatabaseService: UsersDatabaseService,
    private val chatDatabaseService: ChatDatabaseService,
) {

    suspend operator fun invoke(result: (List<Pair<User?, Message?>>) -> Unit) {
        usersDatabaseService.userChats.map { userChats ->
            Log.i("usecase", userChats.toString())
            val usersIds: MutableList<String?> = mutableListOf()
            val chatsIds: MutableList<String?> = mutableListOf()
            userChats?.forEach {
                usersIds.add(it?.toUserId)
                chatsIds.add(it?.chatId)
            }
            Log.i("usecase usersIds", usersIds.toString())
            Log.i("usecase chatsIds", chatsIds.toString())
            usersDatabaseService.specificUsers(usersIds).map { users ->
                chatDatabaseService.lastMessages(chatsIds).map { messages ->
                    Log.i("usecase users", users.toString())
                    Log.i("usecase messages", messages.toString())
                    Log.i("usecase ret" , users.zip(messages).toString())
                    result(users.zip(messages))
                }.collect()
            }.collect()
        }.collect()
    }
}