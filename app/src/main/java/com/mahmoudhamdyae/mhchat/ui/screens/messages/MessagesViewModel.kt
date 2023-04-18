package com.mahmoudhamdyae.mhchat.ui.screens.messages

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import com.mahmoudhamdyae.mhchat.domain.models.Chat
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.usecases.BaseUseCase
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class MessagesViewModel @AssistedInject constructor(
    private val useCase: BaseUseCase,
    @Assisted("chatId") private val chatId: String,
    logService: LogService
): ChatViewModel(logService) {

    var chat: Flow<Chat?> = useCase.getMessagesUseCase(chatId)
    lateinit var currentUser: Flow<User?>

    init {
        val topic = "/topics/$chatId"
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d("FCM", msg)
            }
        launchCatching {
            currentUser = useCase.getUserUseCase()
        }
    }

    fun onMessageSend(messageBody: String, toUserId: String, currentUser: User, context: Context) {


        val requestQueue: RequestQueue by lazy {
            Volley.newRequestQueue(context)
        }



        launchCatching {
            useCase.sendMessageUseCase(
                chatId = chatId,
                messageBody = messageBody,
                toUserId = toUserId,
                fromUserId = currentUser.userId,
                fromUserName = currentUser.userName,
                fromUserImageUrl = currentUser.imageUrl,
                requestQueue = requestQueue
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("chatId") chatId: String,
        ): MessagesViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            chatId: String,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(chatId) as T
            }
        }
    }
}