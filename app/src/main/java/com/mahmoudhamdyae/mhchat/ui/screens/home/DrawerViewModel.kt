package com.mahmoudhamdyae.mhchat.ui.screens.home

import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.usecases.BaseUseCase
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.auth.login.LogInDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(
    val accountService: AccountService,
    private val useCase: BaseUseCase,
    logService: LogService
): ChatViewModel(logService) {

    var currentUser: Flow<User?> = flow { }

    init {
        if (accountService.hasUser) {
            launchCatching {
                currentUser = useCase.getUserUseCase()
            }
        }
    }

    fun onSignOut(navigate: (String) -> Unit) {
        launchCatching {
            useCase.signOutUseCase()
            navigate(LogInDestination.route)
        }
    }
}