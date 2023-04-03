package com.mahmoudhamdyae.mhchat.ui.screens.users

import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val accountService: AccountService,
    databaseService: UsersDatabaseService,
    logService: LogService
): ChatViewModel(logService) {

    val users = databaseService.users.map {
        it.filter { user ->
            user.userId != accountService.currentUserId
        }
    }
}