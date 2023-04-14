package com.mahmoudhamdyae.mhchat.ui.screens.notifications

import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    logService: LogService
): ChatViewModel(logService) {
}