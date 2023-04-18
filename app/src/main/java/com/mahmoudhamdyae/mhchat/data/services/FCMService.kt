package com.mahmoudhamdyae.mhchat.data.services

import android.app.NotificationManager
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mahmoudhamdyae.mhchat.common.utils.sendNotification
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FCMService: FirebaseMessagingService() {

    @Inject
    lateinit var accountService: AccountService

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        remoteMessage.data.let {
            val title = it["title"].orEmpty()
            val message = it["message"].orEmpty()
            val userId = it["userId"].orEmpty()
            val userImageUrl = it["userImageUrl"].orEmpty()
            if (userId != accountService.currentUserId) {
                sendNotification(title, message, userId, userImageUrl)
            }
        }

        super.onMessageReceived(remoteMessage)
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(title: String, messageBody: String, userId: String, userImageUrl: String) {
        val notificationManager =
            ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)
        notificationManager?.sendNotification(title, messageBody, userId, userImageUrl, applicationContext)
    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}