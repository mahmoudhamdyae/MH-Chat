package com.mahmoudhamdyae.mhchat.data.services

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.mahmoudhamdyae.mhchat.domain.models.Chat
import com.mahmoudhamdyae.mhchat.domain.models.Message
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.trace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatDatabaseServiceImpl @Inject constructor(
    firestore: FirebaseFirestore,
    private val accountService: AccountService,
    private val usersDatabaseServiceImpl: UsersDatabaseServiceImpl,
): ChatDatabaseService {

    private val chatCollection = firestore.collection(CHAT_COLLECTION)

    override val chat: (String) -> Flow<Chat?>
        get() = { chatId ->
            chatCollection.document(chatId).snapshots().map { snapshot -> snapshot.toObject() }
    }

    override val lastMessage: (String) -> Flow<Message?>
        get() = { chatId ->
            chatCollection.document(chatId).snapshots().map { snapshot ->
                val chat = snapshot.toObject() as Chat?
                chat?.messages?.last()
            }
        }

    override suspend fun createChat(toUserId: String, chatId: String) {
        val chat = Chat(mutableListOf(), chatId)
        trace(CREATE_CHAT) {
            usersDatabaseServiceImpl.createChat(toUserId, chatId)
            chatCollection.document(chatId).set(chat).await()
        }
    }

    override suspend fun updateChat(chatId: String, messageBody: String) {
        val message = Message(
            body = messageBody,
            author = accountService.currentUserId,
        )
        trace(UPDATE_CHAT) {
            chatCollection.document(chatId).update("messages", FieldValue.arrayUnion(message)).await()
        }
    }

    override suspend fun delChat(chatId: String) {
        trace(DELETE_CHAT) {
            chatCollection.document(chatId).delete().await()
        }
    }

    companion object {
        private const val CHAT_COLLECTION = "chats"
        private const val CREATE_CHAT = "create_chat"
        private const val UPDATE_CHAT = "update_chat"
        private const val DELETE_CHAT = "delete_chat"
    }
}