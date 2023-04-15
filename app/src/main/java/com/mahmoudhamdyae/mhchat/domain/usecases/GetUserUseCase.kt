package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import kotlinx.coroutines.flow.Flow

class GetUserUseCase(
    private val usersDatabaseService: UsersDatabaseService,
) {

    suspend operator fun invoke(userId: String? = null): Flow<User?> {
        if (userId == null) return usersDatabaseService.getCurrentUser()
        return usersDatabaseService.getUser(userId)
    }
}