package com.mahmoudhamdyae.mhchat.ui.screens.home

import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.usecases.BaseUseCase
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.auth.login.LogInDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val useCase: BaseUseCase,
    logService: LogService
): ChatViewModel(logService) {

    fun onSignOut(navigate: (String) -> Unit) {
        launchCatching {
            useCase.signOutUseCase()
            navigate(LogInDestination.route)
        }
    }
}