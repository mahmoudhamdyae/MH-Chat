package com.mahmoudhamdyae.mhchat.ui.screens.home

import com.mahmoudhamdyae.mhchat.data.services.PreferencesRepository
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.login.LogInDestination
import com.mahmoudhamdyae.mhchat.ui.screens.messages.MessagesDestination
import com.mahmoudhamdyae.mhchat.ui.screens.onboarding.OnBoardingDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val accountService: AccountService,
    private val databaseService: UsersDatabaseService,
    private val chatDatabaseService: ChatDatabaseService,
    preferencesRepository: PreferencesRepository,
    logService: LogService
): ChatViewModel(logService) {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState>
        get() = _uiState

    private val isFirstTime: Flow<Boolean> =
        preferencesRepository.isFirstTime

    fun initialize(navigate: (String) -> Unit) {
        if (!accountService.hasUser) {
            launchCatching {
                isFirstTime.collect {
                    if (it) {
                        navigate(OnBoardingDestination.route)
                        this.cancel()
                    } else {
                        navigate(LogInDestination.route)
                        this.cancel()
                    }
                }
            }
        } else {
            getChats()
        }
    }

    private fun getChats() {
        launchCatching {
            databaseService.getCurrentUser().collect {
                it?.chats?.forEach { userChat ->
                    // Get Users
                    databaseService.getUser(userChat.toUserId).collect { user ->
                        _uiState.value.users?.add(user)
                    }
                    // Get Chats
                    chatDatabaseService.getLastMessage(userChat.chatId).collect { message ->
                        _uiState.value.lastMessages?.add(message)
                    }
                }
            }
        }
    }

    fun onSignOut(navigate: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            navigate(LogInDestination.route)
        }
    }

    fun onItemClick(user: User, navigateTo: (String) -> Unit) {
        navigateTo("${MessagesDestination.route}/${user.userId}")
    }
}