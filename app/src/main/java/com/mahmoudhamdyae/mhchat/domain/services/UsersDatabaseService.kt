package com.mahmoudhamdyae.mhchat.domain.services

import com.mahmoudhamdyae.mhchat.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UsersDatabaseService {

    val users: Flow<List<User>>

    suspend fun getUser(userId: String): Flow<User?>
    suspend fun getCurrentUser(): Flow<User?>
    suspend fun saveUser(user: User)
    suspend fun updateProfileImage(imageUri: String)
    suspend fun createChat(toUserId: String, chatId: String)
}