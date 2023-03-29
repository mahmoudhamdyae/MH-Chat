package com.mahmoudhamdyae.mhchat.domain.services

import com.mahmoudhamdyae.mhchat.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<User>

    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount(email: String, password: String)
    suspend fun signOut()
}