package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetUsersUseCase (
    private val accountService: AccountService,
    private val usersDatabaseService: UsersDatabaseService
) {

    operator fun invoke(): Flow<List<User>> {
        return usersDatabaseService.users.map {
            it.filter { user ->
                user.userId != accountService.currentUserId
            }
        }
    }
}