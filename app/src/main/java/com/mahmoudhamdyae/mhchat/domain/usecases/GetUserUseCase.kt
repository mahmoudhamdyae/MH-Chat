package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import kotlinx.coroutines.flow.Flow

class GetUserUseCase(
    private val accountService: AccountService,
    private val usersDatabaseService: UsersDatabaseService,
) {

    suspend operator fun invoke(userId: String = accountService.currentUserId): Flow<User?> {
        return usersDatabaseService.getUser(userId)
    }
}