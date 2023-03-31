package com.mahmoudhamdyae.mhchat.data.services

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.DatabaseService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DatabaseServiceImpl @Inject constructor(
    firestore: FirebaseFirestore,
    private val accountService: AccountService
) : DatabaseService {

    private val userCollection = firestore.collection(USER_COLLECTION)

    override suspend fun saveUser(user: User) {
        userCollection.document(user.userId).set(user).await()
    }

    override suspend fun updateUser(user: User) {
        userCollection.document(user.userId).set(user).await()
    }

    override suspend fun deleteUser() {
        userCollection.document(accountService.currentUserId).delete().await()
    }

    override suspend fun getUser(userId: String): User? {
        return userCollection.document(userId).get().await().toObject()
    }

    override suspend fun getUsers(): List<User> {
        return userCollection.get().await().toObjects()
    }

    companion object {
        private const val USER_COLLECTION = "users"
    }
}