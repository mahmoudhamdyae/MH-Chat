package com.mahmoudhamdyae.mhchat.domain.services

import com.mahmoudhamdyae.mhchat.domain.models.User

interface DatabaseService {

    // Users Table
    suspend fun saveUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser()
    suspend fun getUser(userId: String): User?
    suspend fun getUsers(): List<User>

    // Chats Table
}