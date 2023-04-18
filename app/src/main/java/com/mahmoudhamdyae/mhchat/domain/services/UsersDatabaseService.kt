package com.mahmoudhamdyae.mhchat.domain.services

import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.models.UserChat
import kotlinx.coroutines.flow.Flow

interface UsersDatabaseService {

    val users: Flow<List<User>>
    val specificUsers: (List<String?>) -> Flow<List<User>>
    val userChats: Flow<List<UserChat?>?>

    suspend fun getUser(userId: String): Flow<User?>
    suspend fun getCurrentUser(): Flow<User?>
    suspend fun saveUser(user: User)
    suspend fun updateProfileImage(imageUri: String)
    suspend fun updateUserName(userName: String)
    suspend fun updateBio(bio: String)
    suspend fun createChat(toUserId: String, chatId: String)
}