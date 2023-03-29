package com.mahmoudhamdyae.mhchat.ui.screens.home

import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.login.LogInDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
): ChatViewModel(logService) {

    fun initialize(navigate: (String) -> Unit) {
        if (accountService.currentUserId.isEmpty()) {
            navigate(LogInDestination.route)
        }
    }

    fun onSignOut(navigate: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            navigate(LogInDestination.route)
        }
    }
}