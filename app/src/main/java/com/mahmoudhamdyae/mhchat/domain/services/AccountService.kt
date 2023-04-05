package com.mahmoudhamdyae.mhchat.domain.services

interface AccountService {

    val currentUserId: String
    val currentUserEmail: String
    val hasUser: Boolean

    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount(email: String, password: String)
    suspend fun signOut()
}