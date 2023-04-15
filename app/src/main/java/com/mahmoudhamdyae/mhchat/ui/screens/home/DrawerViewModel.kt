package com.mahmoudhamdyae.mhchat.ui.screens.home

import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.usecases.BaseUseCase
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.auth.login.LogInDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
open class DrawerViewModel @Inject constructor(
    private val useCase: BaseUseCase,
    logService: LogService,
) : ChatViewModel(logService) {

    private var _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    fun getCurrentUser() {
        launchCatching {
            useCase.getUserUseCase().collect {
                _currentUser.value = it
            }
        }
    }

    fun onSignOut(navigate: (String) -> Unit) {
        launchCatching {
            useCase.signOutUseCase()
            navigate(LogInDestination.route)
            _currentUser.value = null
        }
    }
}