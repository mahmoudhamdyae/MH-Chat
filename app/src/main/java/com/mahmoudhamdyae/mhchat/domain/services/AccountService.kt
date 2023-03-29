package com.mahmoudhamdyae.mhchat.domain.services

import android.app.Application
import com.mahmoudhamdyae.mhchat.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<User>

    suspend fun authenticate(email: String, password: String)
    suspend fun authenticateWithGoogle(application: Application)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}