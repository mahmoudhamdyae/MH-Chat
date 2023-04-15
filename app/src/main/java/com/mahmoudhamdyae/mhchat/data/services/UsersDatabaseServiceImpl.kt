package com.mahmoudhamdyae.mhchat.data.services

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.models.UserChat
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.trace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsersDatabaseServiceImpl @Inject constructor(
    firestore: FirebaseFirestore,
    private val accountService: AccountService
) : UsersDatabaseService {

    private val userCollection = firestore.collection(USER_COLLECTION)

    override val users: Flow<List<User>>
        get() = userCollection.snapshots().map { snapshot -> snapshot.toObjects() }

    override val specificUsers: (List<String?>) -> Flow<List<User>>
        get() = { usersIds ->
            userCollection.whereIn("userId", usersIds).snapshots().map { snapshot -> snapshot.toObjects() }
        }

    override val userChats: Flow<List<UserChat?>?>
        get() =
            userCollection.document(accountService.currentUserId).collection(CHATS_COLLECTION)
                .snapshots().map { snapshot -> snapshot.toObjects() }

    override suspend fun getUser(userId: String): Flow<User?> {
        return userCollection.document(userId).snapshots().map { snapshot -> snapshot.toObject() }
    }

    override suspend fun getCurrentUser(): Flow<User?> {
        return if (accountService.hasUser) getUser(accountService.currentUserId) else flow { }
    }

    override suspend fun saveUser(user: User) {
        trace(CREATE_USER_TRACE) {
            userCollection.document(user.userId).set(user).await()
        }
    }

    override suspend fun updateProfileImage(imageUri: String) {
        trace(UPDATE_USER_TRACE) {
            userCollection.document(accountService.currentUserId)
                .update("imageUrl", imageUri).await()
        }
    }

    override suspend fun createChat(toUserId: String, chatId: String) {
        trace(CREATE_CHAT_IN_USER_TRACE) {
            userCollection.document(accountService.currentUserId).collection(CHATS_COLLECTION)
                .document(toUserId).set(UserChat(toUserId, chatId)).await()

            userCollection.document(toUserId).collection(CHATS_COLLECTION)
                .document(accountService.currentUserId).
                set(UserChat(accountService.currentUserId, chatId)).await()
        }
    }

    companion object {
        private const val USER_COLLECTION = "users"
        private const val CHATS_COLLECTION = "chats"
        private const val CREATE_USER_TRACE = "create_user"
        private const val UPDATE_USER_TRACE = "update_user"
        private const val CREATE_CHAT_IN_USER_TRACE = "create_chat_in_users"
    }
}