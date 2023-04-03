package com.mahmoudhamdyae.mhchat.ui.screens.home

import com.mahmoudhamdyae.mhchat.domain.models.Message
import com.mahmoudhamdyae.mhchat.domain.models.User

data class HomeUiState(
    var users: MutableList<User?>? = null,
    var lastMessages: MutableList<Message?>? = null
)
