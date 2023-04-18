package com.mahmoudhamdyae.mhchat.common.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.mahmoudhamdyae.mhchat.MainActivity
import com.mahmoudhamdyae.mhchat.R

/**
 * Builds and delivers the notification.
 *
 * @param applicationContext, activity context.
 */
fun NotificationManager.sendNotification(title: String, messageBody: String, userId: String, userImageUrl: String, applicationContext: Context) {

    createChannel(applicationContext)

    val intent = Intent(applicationContext, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, NOTIFICATION_ID, intent, PendingIntent.FLAG_IMMUTABLE)

    // Key for the string that's delivered in the action's intent.
//    val keyTextReply = "key_text_reply"
//    val replyLabel: String = keyTextReply
//    val remoteInput: RemoteInput = RemoteInput.Builder(keyTextReply).run {
//        setLabel(replyLabel)
//        build()
//    }
//    val replyPendingIntent: PendingIntent =
//        PendingIntent.getBroadcast(applicationContext,
//            NOTIFICATION_ID,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT)
//
//    val action: NotificationCompat.Action =
//        NotificationCompat.Action.Builder(R.drawable.send,
//            replyLabel, replyPendingIntent)
//            .addRemoteInput(remoteInput)
//            .build()

    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_launcher_foreground)
//        .setLargeIcon(ProfileImage())
        .setContentTitle(title)
        .setContentText(messageBody)
        .setStyle(NotificationCompat.BigTextStyle()
            .bigText(messageBody))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
//        .addAction(action)

        .setPriority(NotificationCompat.PRIORITY_HIGH)

    // Since your app will only have one active notification at a given time
    // you can use the same ID foll all your notifications
    notify(NOTIFICATION_ID, builder.build())
}

/**
 * Cancels all notifications.
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}

fun createChannel(context: Context) {
    val notificationChannel = NotificationChannel(
        CHANNEL_ID,
//        context.getString(R.string.channel_name),
        "111",

                NotificationManager.IMPORTANCE_HIGH
    )
        .apply {
            setShowBadge(false)
        }

    notificationChannel.enableLights(true)
    notificationChannel.lightColor = Color.RED
    notificationChannel.enableVibration(true)
//    notificationChannel.description = context.getString(R.string.notification_channel_description)
    notificationChannel.description = "notification_channel_description"

    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(notificationChannel)
}

private const val NOTIFICATION_ID = 0
private const val CHANNEL_ID = "mainChannel"