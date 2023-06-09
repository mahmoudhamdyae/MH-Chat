package com.mahmoudhamdyae.mhchat.domain.models

import android.annotation.SuppressLint
import androidx.annotation.Keep
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Keep
data class Message(
    val body: String = "",
    val fromUserId: String = "",
    val toUserId: String = "",
    val dayOfMonth: String = SimpleDateFormat("d MMM").format(Date()),
    val time: String = SimpleDateFormat("HH:mm").format(Date()),
    val messageId: String = UUID.randomUUID().toString(),
)
