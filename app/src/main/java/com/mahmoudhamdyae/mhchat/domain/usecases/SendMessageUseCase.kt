package com.mahmoudhamdyae.mhchat.domain.usecases

import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.snackbar.SnackBarManager
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import org.json.JSONException
import org.json.JSONObject

class SendMessageUseCase (
    private val chatDatabaseService: ChatDatabaseService
) {

    suspend operator fun invoke(
        chatId: String,
        messageBody: String,
        toUserId: String,
        fromUserId: String,
        fromUserName: String,
        fromUserImageUrl: String?,
        requestQueue: RequestQueue
    ) {
        if (validateMessageBody(messageBody)) {
            sendNotification(chatId, messageBody, fromUserId, fromUserName, fromUserImageUrl, requestQueue)
            chatDatabaseService.updateChat(chatId, messageBody, toUserId)
        }
    }

    private suspend fun validateMessageBody(messageBody: String): Boolean {
        return if (messageBody.isNotEmpty()) {
            true
        } else {
            SnackBarManager.showMessage(R.string.message_empty)
            false
        }
    }

    private val fcmApi = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "key=" + "AAAAOIJk1Io:APA91bEL5uT9T6kCeAc5HJRMzdh83JCSiT6oAKWC3dmm8RJAFIoZJqm6XDjLm97LNjS8TGGnYhnt23eI2sZLBit3YGNQWYksSPi8zZATMIXKfhqVDpPPi7ddw_aeXZXl8Tj9KfTjxaaS"
    private val contentType = "application/json"

    private fun sendNotification(
        chatId: String,
        messageBody: String,
        fromUserId: String,
        fromUserName: String,
        fromUserImageUrl: String?,
        requestQueue: RequestQueue
    ) {
        val topic = "/topics/$chatId"
        val notification = JSONObject()
        val notificationBody = JSONObject()
        try {
            notificationBody.put("title", fromUserName)
            notificationBody.put("userId", fromUserId)
            notificationBody.put("message", messageBody)
            notificationBody.put("userImageUrl", fromUserImageUrl)
            notification.put("to", topic)
            notification.put("data", notificationBody)

            val jsonObjectRequest = object: JsonObjectRequest(fcmApi, notification,
                Response.Listener<JSONObject> { response ->
                    Log.i("FCM", "onResponse: $response")
            },
                Response.ErrorListener {
                    Log.i("FCM", "onErrorResponse: Didn't work")
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["Authorization"] = serverKey
                    params["Content-Type"] = contentType
                    return params
                }
            }
            requestQueue.add(jsonObjectRequest)
        } catch (e: JSONException) {
            Log.e("FCM", e.message.toString())
        }
    }
}