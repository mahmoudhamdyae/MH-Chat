package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetChatId(
    private val accountService: AccountService,
    private val usersDatabaseService: UsersDatabaseService,
) {

    operator fun invoke(toUserId: String): Flow<String?> {
        return if (accountService.currentUserId != toUserId) {
            usersDatabaseService.userChats.map { userChats ->
                userChats?.findLast { userChat ->
                    userChat?.toUserId == toUserId
                }?.chatId
            }
        } else {
            flow {  }
        }
    }
}